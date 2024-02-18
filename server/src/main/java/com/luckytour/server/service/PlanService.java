package com.luckytour.server.service;

import com.github.jeffreyning.mybatisplus.service.IMppService;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.pojo.Spot;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qing
 * @since 2024-01-16
 */
public interface PlanService extends IMppService<Plan> {

	Mono<String> check(List<Map.Entry<Spot, String>> datedLocatedSpots);

	Mono<Map<String, List<Object>>> create(Map<String, Object> prompt);
}
