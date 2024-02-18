package com.luckytour.server.service;

import com.luckytour.server.pojo.Position;
import reactor.core.publisher.Mono;

/**
 * @author qing
 * @date Created in 2024/2/16 19:57
 */
public interface DynamicCheckService {

	Mono<String> checkRealTimeWeather(Position position);

	Mono<String> checkSomeThingElse();
}
