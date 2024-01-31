package com.luckytour.server.service.serviceImpl;

import com.luckytour.server.common.constant.ApiAddr;
import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.SimpleChatRequest;
import com.luckytour.server.service.GptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author qing
 * @date Created in 2024/1/31 22:38
 */
@Service
@Slf4j
public class GptServiceImpl implements GptService {
	@Autowired
	private ExternalApiConfig externalApiConfig;

	public Mono<String> chat(SimpleChatRequest params) {
		// 构建请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(externalApiConfig.getHttpClient()))
				.baseUrl(externalApiConfig.getFlask().getBase())
				.build()
				.post()
				.uri(externalApiConfig.getFlask().getChat())
				.headers(httpHeaders -> httpHeaders.addAll(headers))
				.bodyValue(params)
				.retrieve()
				.bodyToMono(ApiResponse.class)
				.map(response -> response.getData().toString());
	}
}
