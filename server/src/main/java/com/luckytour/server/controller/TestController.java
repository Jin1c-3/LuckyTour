package com.luckytour.server.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.exception.JsonException;
import com.luckytour.server.service.JiguangPushService;
import com.luckytour.server.vo.JiguangNotification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/1/10 19:53
 */
@RestController
@Tag(name = "测试接口")
@CrossOrigin
@Slf4j
@RequestMapping("/test")
public class TestController {

	@Autowired
	private JiguangPushService jiguangPushService;

	/**
	 * 测试websocket接收简单数据并返回
	 */
	@MessageMapping("/websocket")
	@SendTo("/topic/hello")
	@Operation(summary = "测试websocket接收简单数据并返回")
	public <T> ApiResponse<T> websocketHello(String message) {
		return ApiResponse.ofSuccessMsg("websocket message: " + message);
	}

	/**
	 * 测试websocket在用户url上接收简单数据并返回
	 */
	/*@MessageMapping("/websocket")
	@SendTo("/topic/hello")
	@Operation(summary = "测试websocket接收简单数据并返回")
	public <T> ApiResponse<T> websocketHelloOnUser(String message) {
		return ApiResponse.ofSuccessMsg("websocket message: " + message);
	}*/

	/**
	 * 测试websocket心跳检测
	 */
	@MessageMapping("/queue/test")
	@SendTo("/topic/queuetest")
	@Operation(summary = "测试websocket心跳检测")
	public <T> ApiResponse<T> queueTest() {
		return ApiResponse.ofSuccessMsg("queue message: test");
	}

	/**
	 * 测试失败返回值
	 */
	@GetMapping("/fail")
	@Operation(summary = "测试失败返回值")
	public <T> ApiResponse<T> fail() {
		return ApiResponse.ofFailMsg("失败啦！这是自定义的消息哟！");
	}

	/**
	 * 测试成功返回值并携带数据
	 */
	@Operation(summary = "测试成功返回值并携带数据")
	@GetMapping("/success")
	public ApiResponse<Map<String, String>> success() {
		Map<String, String> map = Map.of("name", "qing", "age", "18");
		return ApiResponse.ofSuccess(map);
	}

	/**
	 * 测试JSON异常
	 *
	 * @return
	 */
	@Operation(summary = "测试JSON异常")
	@Parameter(name = "map", description = "测试参数", required = true, example = "{\"name\":\"张三\",\"age\":18}", schema = @Schema(implementation = Map.class))
	@PostMapping("/jsonException")
	public <T> ApiResponse<T> jsonException(Map<String, Object> map) {
		throw new JsonException();
	}

	@Operation(summary = "测试利用极光向我自己的设备发送通知")
	@GetMapping("/notification")
	public void testSendNotification() throws APIConnectionException, APIRequestException {
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>());
		jiguangPushService.updateDeviceTagAlias("160a3797c90471c3a54", "yjy", null, null);
		jiguangPushService.sendPushByAlias(notification, "yjy");
		jiguangPushService.sendPushToAll(notification);
//		System.out.println(pushResult.getResponseCode());
		//log.info(String.valueOf(pushResult.statusCode));
//		log.info(String.valueOf(pushResult.sendno));
		//设置、更新、设备的 tag, alias 信息。140fe1da9e38e9efd3e
	}
}
