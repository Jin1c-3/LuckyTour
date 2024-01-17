package com.luckytour.server.util;

import com.luckytour.server.common.constant.ApiAddr;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.GaodeResponse;
import com.luckytour.server.payload.SimpleChatRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author qing
 * @date Created in 2024/1/16 14:28
 */
@Component
@Slf4j
public class ApiRequestUtil {
	@Value("${api.flask}")
	private String flaskUrl;

	@Value("${api.gaode-key}")
	private String gaodeKey;

	@PostConstruct
	public void init() {
		FLASK_URL = flaskUrl;
		GAODE_KEY = gaodeKey;
	}

	/**
	 * flask api url
	 */
	private static String FLASK_URL;

	/**
	 * 高德地图api key
	 */
	private static String GAODE_KEY;

	private static RestTemplate restTemplate = new RestTemplate();

	/**
	 * 调用flask chat接口
	 *
	 * @param params 请求参数
	 * @return 返回聊天结果
	 */
	public static String chatRequest(SimpleChatRequest params) {
		// 构建请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SimpleChatRequest> requestEntity = new HttpEntity<>(params, headers);
		// 发送 POST 请求
		ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(FLASK_URL + ApiAddr.FLASK_CHAT, requestEntity, ApiResponse.class);
		return responseEntity.getBody().getData().toString();
	}

	/**
	 * 调用高德api获取两地直线距离
	 * <a href="https://lbs.amap.com/api/webservice/guide/api/direction#distance">高德文档</a>
	 *
	 * @param origin      起点经纬度
	 * @param destination 终点经纬度
	 * @return 返回两地直线距离，单位是米
	 */
	public static List<Integer> getStraightDistance(String origin, String destination) {
		if (!origin.contains(",") || !destination.contains(",") || origin.equals(destination) || GAODE_KEY == null) {
			log.warn("请检查参数：origin: {}, destination: {}, GAODE_KEY: {}", origin, destination, GAODE_KEY);
			return null;
		}
		Map<String, String> params = Map.of("origins", origin, "destination", destination, "key", GAODE_KEY);
		GaodeResponse gdResponse = restTemplate.getForObject(ApiAddr.GAODE_DISTANCE, GaodeResponse.class, params);
		if (gdResponse == null) {
			log.warn("高德api调用失败，返回空");
			return null;
		}
		if ("0".equals(gdResponse.getStatus())) {
			log.warn("高德api调用失败：{} info:{}", Objects.requireNonNull(gdResponse).getInfo(), gdResponse.getInfo());
			return null;
		}
		log.debug("高德api调用成功，返回结果：{}", gdResponse);
		return gdResponse.getResults().stream().map(result -> Integer.parseInt(result.get("distance"))).toList();
	}
}
