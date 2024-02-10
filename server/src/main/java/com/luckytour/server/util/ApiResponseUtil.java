package com.luckytour.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.IApiStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * <p>
 * API响应工具类
 * </p>
 *
 * @author qing
 * @date Created in 2023/8/1 11:01
 */
@Slf4j
public class ApiResponseUtil {

	/**
	 * 私有构造函数
	 */
	private ApiResponseUtil() {
	}

	/**
	 * 往 Response 中写入 Json 数据
	 *
	 * @param response 响应
	 * @param status   状态
	 * @param data     数据
	 */
	public static void renderJson(HttpServletResponse response, IApiStatus status, Object data) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(status.getCode());
		try {
			response.getWriter().write(new ObjectMapper().writeValueAsString(ApiResponse.ofStatus(status, data)));
		} catch (IOException e) {
			log.error("ApiResponse写入Json异常", new RuntimeException(e));
		}
	}

	public static void renderJson(HttpServletResponse response, BaseException exception) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(200);
			response.getWriter().write(new ObjectMapper().writeValueAsString(ApiResponse.ofException(exception)));
		} catch (IOException e) {
			log.error("Response写入Json异常", new RuntimeException(e));
		}
	}

}
