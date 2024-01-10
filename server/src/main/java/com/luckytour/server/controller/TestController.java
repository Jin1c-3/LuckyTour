package com.luckytour.server.controller;

import com.luckytour.server.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author qing
 * @date Created in 2024/1/10 19:53
 */
@Controller
@Tag(name = "测试接口")
@CrossOrigin
@Slf4j
public class TestController {

	/**
	 * 测试websocket接收简单数据并返回
	 */
	@MessageMapping("/websocket")
	@SendTo("/topic/hello")
	@Operation(summary = "测试websocket接收简单数据并返回")
	public <T> ApiResponse<T> websocketHello(String message) {
		log.debug("websocket message: {}", message);
		return ApiResponse.ofSuccessMsg("websocket message: " + message);
	}

	/**
	 * 测试websocket心跳检测
	 */
	@MessageMapping("/queue/test")
	@SendTo("/topic/queuetest")
	@Operation(summary = "测试websocket心跳检测")
	public <T> ApiResponse<T> queueTest() {
		return ApiResponse.ofSuccessMsg("queue message: test");
	}
}
