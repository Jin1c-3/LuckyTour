package com.luckytour.server.service.serviceImpl;

import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.payload.CaiyunResponse;
import com.luckytour.server.service.CaiyunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author qing
 * @date Created in 2024/1/31 23:08
 */
@Service
@Slf4j
public class CaiyunServiceImpl implements CaiyunService {

	@Autowired
	private ExternalApiConfig externalApiConfig;

	public Mono<String> getWeather(String position, String date) {
		if (!position.contains(",")) {
			return Mono.error(new IllegalArgumentException("location格式错误，position: " + position));
		}
		LocalDate targetLocalDate = LocalDate.parse(date);
		if (targetLocalDate.isBefore(LocalDate.now()) || targetLocalDate.isAfter(LocalDate.now().plusDays(Consts.CAIYUN_MAX_DAYS))) {
			return Mono.error(new IllegalArgumentException("天气查询日期不在范围内，date: " + date));
		}

		String baseUrl = String.format(externalApiConfig.getCaiyun().getDailyWeather(), externalApiConfig.getCaiyun().getKey(), position);

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(externalApiConfig.getHttpClient()))
				.baseUrl(baseUrl)
				.build()
				.get()
				.retrieve()
				.bodyToMono(CaiyunResponse.class)
				.flatMap(cyResponse -> {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					return cyResponse.getResult().getDaily().getSkycon().stream()
							.filter(skycon -> targetLocalDate.equals(LocalDate.parse(skycon.getDate().substring(0, 10), formatter)))
							.findFirst()
							.map(skycon -> Mono.just(skycon.getValue()))
							.orElseGet(() -> Mono.error(new RuntimeException("彩云api调用成功，但没有找到对应日期 " + targetLocalDate)));
				});
	}
}
