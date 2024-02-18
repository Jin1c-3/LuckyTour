package com.luckytour.server.service.impl;

import com.luckytour.server.common.constant.CaiyunWeather;
import com.luckytour.server.common.constant.Judgment;
import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.pojo.Position;
import com.luckytour.server.service.DynamicCheckService;
import com.luckytour.server.service.WeatherService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author qing
 * @date Created in 2024/2/16 20:29
 */
@Service
public class DynamicCheckServiceImpl implements DynamicCheckService {

	@Resource
	private final WeatherService caiyunWeatherService;

	private final ExternalApiConfig externalApiConfig;

	@Autowired
	private DynamicCheckServiceImpl(WeatherService caiyunWeatherService, ExternalApiConfig externalApiConfig) {
		this.caiyunWeatherService = caiyunWeatherService;
		this.externalApiConfig = externalApiConfig;
	}

	@Override
	public Mono<String> checkRealTimeWeather(Position position) {
		return caiyunWeatherService.getHourlyWeather(position)
				.flatMapMany(Flux::fromIterable)
				.take(externalApiConfig.getCaiyun().getCheckHours())
				.index()
				.map(tuple -> {
					String weather = tuple.getT2();
					String caiyunWeatherExplanation = CaiyunWeather.WEATHER_MAP.get(weather);
					if (Judgment.GOOD_CAIYUN_WEATHER.stream().noneMatch(caiyunWeatherExplanation::equals)) {
						return Judgment.getBadWeatherFriendlyAlert(String.valueOf(tuple.getT1() + 1), caiyunWeatherExplanation);
					} else {
						return "";
					}
				})
				.filter(StringUtils::isNotBlank)
				.next()
				.switchIfEmpty(Mono.just(Boolean.TRUE.toString()));
	}

	@Override
	public Mono<String> checkSomeThingElse() {
		return Mono.empty();
	}
}
