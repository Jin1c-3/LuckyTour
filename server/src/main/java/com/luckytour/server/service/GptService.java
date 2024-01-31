package com.luckytour.server.service;

import com.luckytour.server.payload.SimpleChatRequest;
import reactor.core.publisher.Mono;

/**
 * @author qing
 * @date Created in 2024/1/31 23:04
 */
public interface GptService {
	public Mono<String> chat(SimpleChatRequest params);
}
