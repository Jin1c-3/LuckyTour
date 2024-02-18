package com.luckytour.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.payload.external.CaiyunResponse;
import com.luckytour.server.pojo.Position;
import com.luckytour.server.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/31 23:08
 */
@Service("caiyunWeatherService")
@Slf4j
public class CaiyunWeatherServiceImpl implements WeatherService {

	private final ExternalApiConfig externalApiConfig;

	private final ObjectMapper objectMapper;

	@Autowired
	public CaiyunWeatherServiceImpl(ExternalApiConfig externalApiConfig, ObjectMapper objectMapper) {
		this.externalApiConfig = externalApiConfig;
		this.objectMapper = objectMapper;
	}

	private void logResponse(CaiyunResponse cyResponse) {
		try {
			log.debug("彩云api调用成功 {}", objectMapper.writeValueAsString(cyResponse));
		} catch (JsonProcessingException e) {
			log.debug("彩云api调用成功 {}", cyResponse.toString());
		}
	}

	/**
	 * 调用彩云api获取天气
	 * <a href="https://docs.caiyunapp.com/docs/daily/">彩云文档</a>
	 *
	 * @param position 经纬度
	 * @param date     日期
	 * @return 返回天气
	 */
	@Cacheable(value = "dailyWeather", key = "#position.formatKey() + #date")
	public Mono<String> getDailyWeather(Position position, String date) {
		LocalDate targetLocalDate = LocalDate.parse(date);
		if (targetLocalDate.isBefore(LocalDate.now()) || targetLocalDate.isAfter(LocalDate.now().plusDays(ConstsPool.CAIYUN_MAX_DAYS))) {
			log.warn("天气查询日期不在范围内，date: {}", date);
			return Mono.just("");
		}

		String baseUrl = String.format(externalApiConfig.getCaiyun().getDailyWeather(), externalApiConfig.getCaiyun().getKey(), position.toString());

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(externalApiConfig.getHttpClient()))
				.baseUrl(baseUrl)
				.build()
				.get()
				.retrieve()
				.bodyToMono(CaiyunResponse.class)
				.flatMap(cyResponse -> {
					logResponse(cyResponse);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					return cyResponse.getResult().getDaily().getSkycon().stream()
							// 这里采用子串操作，可能会出错
							.filter(skycon -> targetLocalDate.equals(LocalDate.parse(skycon.getDate().substring(0, 10), formatter)))
							.findFirst()
							.map(skycon -> Mono.just(skycon.getValue()))
							.orElseGet(() -> Mono.error(new RuntimeException("彩云api调用成功，但没有找到对应日期 " + targetLocalDate)));
				});
	}

	/**
	 * 调用彩云api获取实时天气，最大获取15个小时的天气
	 * <a href="https://docs.caiyunapp.com/docs/realtime">彩云文档</a>
	 *
	 * @param position 经纬度
	 * @return 返回天气
	 */
	@Cacheable(value = "hourlyWeather", key = "#position.formatKey()")
	public Mono<List<String>> getHourlyWeather(Position position) {
		String baseUrl = String.format(externalApiConfig.getCaiyun().getHourlyWeather(), externalApiConfig.getCaiyun().getKey(), position.toString());
		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(externalApiConfig.getHttpClient()))
				.baseUrl(baseUrl)
				.build()
				.get()
				.retrieve()
				.bodyToMono(CaiyunResponse.class)
				.flatMap(cyResponse -> {
					logResponse(cyResponse);
					return Flux.fromIterable(cyResponse.getResult().getHourly().getSkycon())
							.map(CaiyunResponse.Result.ForecastWay.TimeValue::getValue)
							.collectList();
				});
	}
}
