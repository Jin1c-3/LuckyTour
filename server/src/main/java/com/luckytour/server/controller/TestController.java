package com.luckytour.server.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.luckytour.server.exception.JsonException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.SimpleChatRequest;
import com.luckytour.server.service.GaodeService;
import com.luckytour.server.service.GptService;
import com.luckytour.server.service.JiguangPushService;
import com.luckytour.server.vo.JiguangNotification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
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
@Profile({"test", "dev"})
public class TestController {

	@Autowired
	private JiguangPushService jiguangPushService;

	@Autowired
	private GaodeService gaodeService;

	@Autowired
	private GptService gptService;

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
	@MessageMapping("/queue/test" +
			"")
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
	 * 测试使用响应式编程Mono的返回值并携带数据
	 */
	@Operation(summary = "测试使用响应式编程Mono的返回值并携带数据")
	@GetMapping("/mono")
	public Mono<ApiResponse<Map<String, String>>> mono() {
		Map<String, String> map = Map.of("name", "qing", "age", "18");
		return Mono.just(ApiResponse.ofSuccess(map));
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

	@Operation(summary = "测试利用极光向所有安卓设备发送通知，每天只能十次")
	@GetMapping("/notification")
	public void testSendNotification() throws APIConnectionException, APIRequestException {
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>());
//		jiguangPushService.updateDeviceTagAlias("160a3797c90471c3a54", "yjy", null, null);
//		jiguangPushService.sendPushByAlias(notification, "yjy");
		jiguangPushService.sendPushToAndroid(notification);
//		System.out.println(pushResult.getResponseCode());
		//log.info(String.valueOf(pushResult.statusCode));
//		log.info(String.valueOf(pushResult.sendno));
		//设置、更新、设备的 tag, alias 信息。140fe1da9e38e9efd3e
	}

	@Operation(summary = "测试利用极光的sendPushByAlias向游轩的安卓设备160a3797c903b80eda8发送通知")
	@GetMapping("/notificationyxAlias")
	public void testSendYxNotificationByAlias() throws APIConnectionException, APIRequestException {
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>());
		jiguangPushService.updateDeviceTagAlias("160a3797c903b80eda8", "yx", null, null);
		jiguangPushService.sendPushByAlias(notification, "yx");
//		jiguangPushService.sendPushToAndroid(notification);
	}

	@Operation(summary = "测试利用极光的sendPushByAlias向于靖怿的安卓设备120c83f76125b36068d发送通知")
	@GetMapping("/notificationyjyAlias")
	public void testSendYjyNotificationByAlias() throws APIConnectionException, APIRequestException {
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>());
		jiguangPushService.updateDeviceTagAlias("120c83f76125b36068d", "yjy", null, null);
		jiguangPushService.sendPushByAlias(notification, "yjy");
//		jiguangPushService.sendPushToAndroid(notification);
	}

	@Operation(summary = "测试利用极光的sendPushByRegistrationID向游轩的安卓设备160a3797c903b80eda8发送通知")
	@GetMapping("/notificationyxRid")
	public void testSendYxNotificationByRid() throws APIConnectionException, APIRequestException {
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>());
		jiguangPushService.sendPushByRegistrationID(notification, "160a3797c903b80eda8");
//		jiguangPushService.sendPushToAndroid(notification);
	}

	@Operation(summary = "测试利用极光的sendPushByRegistrationID向于靖怿的安卓设备120c83f76125b36068d发送通知")
	@GetMapping("/notificationyjyRid")
	public void testSendYjyNotificationByRid() throws APIConnectionException, APIRequestException {
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>());
		jiguangPushService.sendPushByRegistrationID(notification, "120c83f76125b36068d");
//		jiguangPushService.sendPushToAndroid(notification);
	}

	@Operation(summary = "测试flask-chat接口调用")
	@GetMapping("/getflaskapi")
	public Mono<ApiResponse<String>> getFlaskApi() {
		return gptService.chat(new SimpleChatRequest("你好，你叫什么名字？"))
				.flatMap(chatResponse -> {
					log.info(chatResponse);
					return Mono.just(ApiResponse.ofSuccess(chatResponse));
				});
	}

	@Operation(summary = "测试高德直线距离接口调用")
	@GetMapping("/getgdapi")
	public Mono<ApiResponse<String>> getGdApi() {
		return gaodeService.getStraightDistance(List.of("105.393178,28.860415"), "105.4536,28.884362")
				.flatMap(distance -> {
					log.info(distance.toString());
					return Mono.just(ApiResponse.ofSuccess(distance.toString()));
				});
	}
}
