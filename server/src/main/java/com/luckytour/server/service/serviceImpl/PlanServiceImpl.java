package com.luckytour.server.service.serviceImpl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.common.constant.CaiyunWeather;
import com.luckytour.server.common.constant.Judgment;
import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.exception.PlanCreateReachLimitException;
import com.luckytour.server.mapper.PlanMapper;
import com.luckytour.server.payload.Spot;
import com.luckytour.server.service.CaiyunService;
import com.luckytour.server.service.GaodeService;
import com.luckytour.server.service.GptService;
import com.luckytour.server.service.PlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

	private final GaodeService gaodeService;

	private final CaiyunService caiyunService;

	private final GptService gptService;

	private final ExternalApiConfig externalApiConfig;

	@Autowired
	public PlanServiceImpl(GaodeService gaodeService, CaiyunService caiyunService, GptService gptService, ExternalApiConfig externalApiConfig) {
		this.gaodeService = gaodeService;
		this.caiyunService = caiyunService;
		this.gptService = gptService;
		this.externalApiConfig = externalApiConfig;
	}

	public Mono<String> check(List<Map.Entry<Spot, String>> datedLocatedSpots) {
		return Flux.fromIterable(datedLocatedSpots)
				.flatMap(entry -> caiyunService.getWeather(entry.getKey().getLocation(), entry.getValue())
						.map(weather -> {
							String caiyunWeatherExplanation = CaiyunWeather.WEATHER_MAP.get(weather);
							return Judgment.GOOD_CAIYUN_WEATHER.stream()
									.noneMatch(caiyunWeatherExplanation::equals)
									? Judgment.getBadWeatherPrompt(String.valueOf(LocalDate.parse(entry.getValue()).getDayOfMonth()), entry.getKey().getCityname() + entry.getKey().getName(), caiyunWeatherExplanation)
									: null;
						})
				)
				.filter(Objects::nonNull)
				.next()
				.switchIfEmpty(
						Flux.range(0, datedLocatedSpots.size() - 1)
								.flatMap(i -> gaodeService.getStraightDistance(List.of(datedLocatedSpots.get(i).getKey().getLocation()), datedLocatedSpots.get(i + 1).getKey().getLocation())
										// 这里只取了第一个距离
										.map(distance -> distance.get(0) > Judgment.STRAIGHT_DISTANCE_TOO_FAR
												? Judgment.getTooFarPrompt(datedLocatedSpots.get(i).getKey().getName(), datedLocatedSpots.get(i + 1).getKey().getName())
												: null
										)
								)
								.filter(Objects::nonNull)
								.next()
				)
				.switchIfEmpty(Mono.just("true"));
	}

	public Mono<Map<String, List<Object>>> create(Map<String, Object> prompt) {
		/*int i = externalApiConfig.getFlask().getPlanCreateCount();
		boolean goodPlan = false;
		Map<String, List<Object>> createdPlan = null;
		while (i != 0) {
			createdPlan = gptService.createPlan(prompt).block();
			List<Map.Entry<Spot, String>> datedLocatedSpots = null;
			if (createdPlan != null) {
				datedLocatedSpots = createdPlan.entrySet().stream()
						.flatMap(entry -> entry.getValue().stream()
								.map(spot -> (Spot) spot)
								.filter(spot -> spot.getLocation() != null)
								.map(spot -> Map.entry(spot, entry.getKey())))
						.toList();
			}
			goodPlan = "true".equals(check(datedLocatedSpots).block());
			if (goodPlan) break;
			i--;
		}
		// 把规定生成次数都用了还不是一个好计划就抛出异常
		if (i <= 0 && !goodPlan) {
			throw new PlanCreateReachLimitException();
		}
		return Mono.just(createdPlan);*/

		return Mono.defer(() -> {
			int i = externalApiConfig.getFlask().getPlanGenerator().getCounter();
			return createPlanAndCheck(prompt, i);
		});
	}

	private Mono<Map<String, List<Object>>> createPlanAndCheck(Map<String, Object> prompt, int remainingAttempts) {
		if (remainingAttempts == 0) {
			return Mono.error(new PlanCreateReachLimitException());
		}

		return gptService.createPlan(prompt)
				.flatMap(createdPlan -> {
					List<Map.Entry<Spot, String>> datedLocatedSpots = createdPlan.entrySet().stream()
							.flatMap(entry -> entry.getValue().stream()
									.map(Spot.class::cast)
									.filter(spot -> spot.getLocation() != null)
									.map(spot -> Map.entry(spot, entry.getKey())))
							.toList();

					return check(datedLocatedSpots)
							.flatMap((isGoodPlanOrBetterPrompt) -> {
								// 如果是好计划就直接返回了
								if ("true".equals(isGoodPlanOrBetterPrompt)) {
									return Mono.just(createdPlan);
								}
								// 看一下原先有没有提示词这个key，有的话就把原来的加上去
								if (prompt.get(externalApiConfig.getFlask().getPlanGenerator().getPromptField()) != null || StringUtils.isNotBlank(prompt.get(externalApiConfig.getFlask().getPlanGenerator().getPromptField()).toString())) {
									prompt.put(externalApiConfig.getFlask().getPlanGenerator().getPromptField(), prompt.get(externalApiConfig.getFlask().getPlanGenerator().getPromptField()).toString() + isGoodPlanOrBetterPrompt);
								} else {
									prompt.put(externalApiConfig.getFlask().getPlanGenerator().getPromptField(), isGoodPlanOrBetterPrompt);
								}
								return createPlanAndCheck(prompt, remainingAttempts - 1);
							});
				});
	}

}
