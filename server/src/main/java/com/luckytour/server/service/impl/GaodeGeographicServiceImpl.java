package com.luckytour.server.service.impl;

import com.luckytour.server.config.ExternalApiConfig;
import com.luckytour.server.payload.external.GaodeResponse;
import com.luckytour.server.pojo.Position;
import com.luckytour.server.service.GeographicService;
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
@Service("gaodeGeographicService")
@Slf4j
public class GaodeGeographicServiceImpl implements GeographicService {

	private final ExternalApiConfig externalApiConfig;

	@Autowired
	public GaodeGeographicServiceImpl(ExternalApiConfig externalApiConfig) {
		this.externalApiConfig = externalApiConfig;
	}

	/**
	 * 调用高德api获取两地直线距离
	 * <a href="https://lbs.amap.com/api/webservice/guide/api/direction#distance">高德文档</a>
	 *
	 * @param origins     起点经纬度
	 * @param destination 终点经纬度
	 * @return 返回两地直线距离，单位是米
	 */
	private Mono<List<Integer>> getStraightDistance(List<Position> origins, Position destination) {
		String originStr = origins.size() > 1
				? String.join("|", origins.stream().map(Position::toString).toList())
				: origins.get(0).toString();
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

	@Override
	public Mono<Integer> getStraightDistance(Position origins, Position destination) {
		return getStraightDistance(List.of(origins), destination).map(list -> list.get(0));
	}

	/**
	 * 调用高德api获取地址的经纬度
	 * <a href="https://lbs.amap.com/api/webservice/guide/api/georegeo">高德文档</a>
	 *
	 * @param address 地址
	 * @return 返回经纬度，格式是"经度,纬度"
	 */
	public Mono<String> getLongitudeAndLatitude(String address) {
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
