package com.luckytour.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.http.IServerStatus;
import com.luckytour.server.common.http.ServerResponseEntity;
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
public class ServerResponseUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 私有构造函数
	 */
	private ServerResponseUtil() {
	}

	/**
	 * 往 Response 中写入 Json 数据
	 *
	 * @param response 响应
	 * @param status   状态
	 * @param data     数据
	 */
	public static void renderJson(HttpServletResponse response, IServerStatus status, Object data) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(status.getCode());
		try {
			response.getWriter().write(objectMapper.writeValueAsString(ServerResponseEntity.ofStatus(status, data)));
		} catch (IOException e) {
			log.error("ServerResponse写入Json异常", e);
		}
	}

	public static void renderJson(HttpServletResponse response, BaseException exception) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(200);
		try {
			response.getWriter().write(objectMapper.writeValueAsString(ServerResponseEntity.ofException(exception)));
		} catch (IOException e) {
			log.error("ServerResponse写入Json异常", e);
		}
	}

}
