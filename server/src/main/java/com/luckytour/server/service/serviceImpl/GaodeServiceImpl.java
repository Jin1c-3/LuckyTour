package com.luckytour.server.service.serviceImpl;

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

	private final ExternalApiConfig externalApiConfig;

	@Autowired
	public GaodeServiceImpl(ExternalApiConfig externalApiConfig) {
		this.externalApiConfig = externalApiConfig;
	}

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
				.baseUrl(externalApiConfig.getGaode().getDistance())
				.build()
				.get()
				.uri(url)
				.retrieve()
				.bodyToMono(GaodeResponse.class)
				.map(gdResponse -> {
					if ("0".equals(gdResponse.getStatus())) {
						log.warn("高德接口调用失败，错误原因 {}", gdResponse.getInfo());
						return List.of(0);
					}
					log.debug("高德接口调用成功，返回结果 {}", gdResponse);
					return gdResponse.getResults().stream().map(result -> Integer.parseInt(result.get("distance"))).toList();
				});
	}

	public Mono<String> getLocation(String address) {
		if (address == null || externalApiConfig.getGaode().getKey() == null) {
			log.warn("请检查参数：address: {}, GAODE_KEY: {}", address, externalApiConfig.getGaode().getKey());
			return Mono.just("");
		}

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(externalApiConfig.getHttpClient()))
				.baseUrl(externalApiConfig.getGaode().getGeocode())
				.build()
				.get()
				.uri(uriBuilder -> uriBuilder
						.queryParam("address", address)
						.queryParam("key", externalApiConfig.getGaode().getKey())
						.build())
				.retrieve()
				.bodyToMono(GaodeResponse.class)
				.map(gdResponse -> {
					if ("0".equals(gdResponse.getStatus())) {
						log.warn("高德接口调用失败，错误原因 {}", gdResponse.getInfo());
						return "";
					}
					log.debug("高德接口调用成功，返回结果 {}", gdResponse);
					return (String) gdResponse.getGeocodes().get(0).get("location");
				});
	}
}
