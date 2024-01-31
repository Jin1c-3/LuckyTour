package com.luckytour.server.service.serviceImpl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.common.constant.CaiyunWeather;
import com.luckytour.server.common.constant.Judgment;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.mapper.PlanMapper;
import com.luckytour.server.payload.Spot;
import com.luckytour.server.service.CaiyunService;
import com.luckytour.server.service.GaodeService;
import com.luckytour.server.service.PlanService;
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

	@Autowired
	private GaodeService gaodeService;

	@Autowired
	private CaiyunService caiyunService;

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

}
