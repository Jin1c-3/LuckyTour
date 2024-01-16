package com.luckytour.server.util;

import com.luckytour.server.common.constant.ApiAddr;
import com.luckytour.server.payload.ApiResponse;
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

import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/1/16 14:28
 */
@Component
@Slf4j
public class ApiRequestUtil {
	@Value("${api.flask}")
	private String flaskUrl;

	@PostConstruct
	public void init() {
		FLASK_URL = flaskUrl;
	}

	/**
	 * flask api url
	 */
	private static String FLASK_URL;

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
}
