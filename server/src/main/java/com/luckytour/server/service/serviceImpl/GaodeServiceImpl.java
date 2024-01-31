package com.luckytour.server.service.serviceImpl;

import com.luckytour.server.common.constant.ApiAddr;
import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.payload.GaodeResponse;
import com.luckytour.server.service.GaodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/31 21:35
 */
@Service
@Slf4j
public class GaodeServiceImpl implements GaodeService {
	@Autowired
	private ExternalApiConfig externalApiConfig;

	public Mono<List<Integer>> getStraightDistance(List<String> origins, String destination) {
		if (origins.isEmpty() || !destination.contains(",") || externalApiConfig.getGaode().getKey() == null) {
			log.warn("请检查参数：origins: {}, destination: {}, GAODE_KEY: {}", origins, destination, externalApiConfig.getGaode().getKey());
			return Mono.just(List.of(0));
		}

		String originStr = origins.size() > 1 ? String.join("|", origins) : origins.get(0);
		String url = String.format("?origins=%s&destination=%s&key=%s",
				originStr,
				destination,
				externalApiConfig.getGaode().getKey());

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(externalApiConfig.getHttpClient()))
				.baseUrl(externalApiConfig.getGaode().getStraightDistance())
				.build()
				.get()
				.uri(url)
				.retrieve()
				.bodyToMono(GaodeResponse.class)
				.map(gdResponse -> {
					if ("0".equals(gdResponse.getStatus())) {
						return List.of(0);
					}
					return gdResponse.getResults().stream().map(result -> Integer.parseInt(result.get("distance"))).toList();
				});
	}
}
