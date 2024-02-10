package com.luckytour.server.service;

import com.luckytour.server.payload.SimpleChatRequest;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/1/31 23:04
 */
public interface GptService {
	Mono<String> chat(SimpleChatRequest params);

	Mono<Map<String, List<Object>>> createPlan(Map<String, Object> params);
}
