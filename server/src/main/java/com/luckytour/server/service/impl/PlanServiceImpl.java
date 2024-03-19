package com.luckytour.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.common.constant.CaiyunWeather;
import com.luckytour.server.common.constant.Judgment;
import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.exception.PlanCreateReachLimitException;
import com.luckytour.server.mapper.PlanMapper;
import com.luckytour.server.pojo.Position;
import com.luckytour.server.pojo.Spot;
import com.luckytour.server.service.GeographicService;
import com.luckytour.server.service.GptService;
import com.luckytour.server.service.PlanService;
import com.luckytour.server.service.WeatherService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-01-16
 */
@Service
public class PlanServiceImpl extends MppServiceImpl<PlanMapper, Plan> implements PlanService {

	@Resource
	private final GeographicService geographicService;

	@Resource
	private final WeatherService caiyunWeatherService;

	private final GptService gptService;

	private final ExternalApiConfig externalApiConfig;

	private final ObjectMapper objectMapper;

	@Autowired
	public PlanServiceImpl(GeographicService geographicService,
	                       WeatherService caiyunWeatherService,
	                       GptService gptService,
	                       ExternalApiConfig externalApiConfig,
	                       ObjectMapper objectMapper) {
		this.geographicService = geographicService;
		this.caiyunWeatherService = caiyunWeatherService;
		this.gptService = gptService;
		this.externalApiConfig = externalApiConfig;
		this.objectMapper = objectMapper;
	}

	public Mono<String> check(List<Map.Entry<Spot, String>> datedLocatedSpots) {
		if(datedLocatedSpots.isEmpty()){
			return Mono.just(Boolean.TRUE.toString());
		}
		return Flux.fromIterable(datedLocatedSpots)
				.flatMap(entry -> caiyunWeatherService.getDailyWeather(Position.create(entry.getKey().getLocation()), entry.getValue())
						.map(weather -> {
							String caiyunWeatherExplanation = CaiyunWeather.WEATHER_MAP.get(weather);
							return Judgment.GOOD_CAIYUN_WEATHER.stream()
									.noneMatch(caiyunWeatherExplanation::equals)
									? Judgment.getBadWeatherPrompt(String.valueOf(LocalDate.parse(entry.getValue()).getDayOfMonth()), entry.getKey().getCityname() + entry.getKey().getName(), caiyunWeatherExplanation)
									: "";
						})
				)
				.filter(StringUtils::isNotBlank)
				.next()
				.switchIfEmpty(
						Flux.range(0, datedLocatedSpots.size() - 1)
								.flatMap(i -> geographicService.getStraightDistance(Position.create(datedLocatedSpots.get(i).getKey().getLocation()), Position.create(datedLocatedSpots.get(i + 1).getKey().getLocation()))
										// 这里只取了第一个距离
										.map(distance -> distance > Judgment.STRAIGHT_DISTANCE_TOO_FAR
												? Judgment.getTooFarPrompt(datedLocatedSpots.get(i).getKey().getName(), datedLocatedSpots.get(i + 1).getKey().getName())
												: ""
										)
								)
								.filter(StringUtils::isNotBlank)
								.next()
				)
				.switchIfEmpty(Mono.just(Boolean.TRUE.toString()));
	}

	/**
	 * 创建计划，通过递归的方式，如果生成的计划不好就重新生成
	 *
	 * @param prompt 传给flask的Map
	 * @return 生成的计划，如果超过次数还是不好就抛出异常
	 */
	/*public Mono<Map<String, List<Object>>> create(Map<String, Object> prompt) {
		return Mono.defer(() -> {
			int i = externalApiConfig.getFlask().getPlanGenerator().getCounter();
			return createPlanAndCheck(prompt, i);
		});
	}*/

	/**
	 * 创建计划
	 *
	 * @param prompt 传给flask的Map
	 * @return 生成的计划，如果超过次数还是不好就抛出异常
	 */
	public Mono<Map<String, List<Object>>> create(Map<String, Object> prompt) {
		return gptService.createPlan(prompt);
	}

	/**
	 * 递归创建计划，私有方法
	 *
	 * @param prompt            传给flask的Map
	 * @param remainingAttempts 剩余尝试次数
	 * @return 生成的计划，如果超过次数还是不好就抛出异常
	 */
	private Mono<Map<String, List<Object>>> createPlanAndCheck(Map<String, Object> prompt, int remainingAttempts) {
		if (remainingAttempts == 0) {
			return Mono.error(new PlanCreateReachLimitException());
		}
		return gptService.createPlan(prompt)
				.flatMap(createdPlan -> {
					List<Map.Entry<Spot, String>> datedLocatedSpots = createdPlan.entrySet().stream()
							.flatMap(entry -> entry.getValue().stream()
									.map(objectMap -> objectMapper.convertValue(objectMap, Spot.class))
									.filter(spot -> spot.getLocation() != null)
									.map(spot -> Map.entry(spot, entry.getKey())))
							.toList();

					return check(datedLocatedSpots)
							.flatMap(isGoodPlanOrBetterPrompt -> {
								// 如果是好计划就直接返回了
								if (Boolean.TRUE.toString().equals(isGoodPlanOrBetterPrompt)) {
									return Mono.just(createdPlan);
								}
								String fieldName = externalApiConfig.getFlask().getPlanGenerator().getPromptField();
								// 看一下原先有没有提示词这个key，有的话就把原来的加上去
								if (prompt.containsKey(fieldName) && prompt.get(fieldName) != null) {
									prompt.put(fieldName, prompt.get(fieldName).toString() + isGoodPlanOrBetterPrompt);
								} else {
									prompt.put(fieldName, isGoodPlanOrBetterPrompt);
								}
								return createPlanAndCheck(prompt, remainingAttempts - 1);
							});
				})
				.switchIfEmpty(Mono.just(new HashMap<>())); // 提供一个默认值
	}

}
