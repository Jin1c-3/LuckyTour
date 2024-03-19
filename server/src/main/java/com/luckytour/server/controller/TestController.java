package com.luckytour.server.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.exception.JsonException;
import com.luckytour.server.payload.external.SimpleChatRequest;
import com.luckytour.server.service.*;
import com.luckytour.server.vo.JiguangNotification;
import com.luckytour.server.vo.SimpleUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试控制器
 *
 * @author qing
 * @date Created in 2024/1/10 19:53
 */
@RestController
@Tag(name = "测试控制器")
@CrossOrigin
@Slf4j
@RequestMapping("/test")
@Profile({"test", "dev"})
public class TestController {

	@Resource
	private final PushService jiguangPushService;

	@Resource
	private final GeographicService geographicService;

	private final GptService gptService;

	private final FollowController followController;

	@Autowired
	public TestController(PushService jiguangPushService, GeographicService geographicService, GptService gptService, FollowService followService, UserService userService, FollowController followController) {
		this.jiguangPushService = jiguangPushService;
		this.geographicService = geographicService;
		this.gptService = gptService;
		this.followController = followController;
	}

	/**
	 * 测试websocket接收简单数据并返回
	 */
/*	@MessageMapping("/websocket")
	@SendTo("/topic/hello")
	@Operation(summary = "测试websocket接收简单数据并返回")
	public <T> ApiResponse<T> websocketHello(String message) {
		return ApiResponse.ofSuccessMsg("websocket message: " + message);
	}*/

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
	/*@MessageMapping("/queue/test" +
			"")
	@SendTo("/topic/queuetest")
	@Operation(summary = "测试websocket心跳检测")
	public <T> ApiResponse<T> queueTest() {
		return ApiResponse.ofSuccessMsg("queue message: test");
	}*/
	@GetMapping("/fail")
	@Operation(summary = "失败函数 ofFailMsg()")
	public <T> ServerResponseEntity<T> fail() {
		return ServerResponseEntity.ofFailMsg("失败啦！这是自定义的消息哟！");
	}

	/**
	 * 测试成功返回值并携带数据
	 */
	@Operation(summary = "成功且携带数据函数 ofSuccess(data)")
	@GetMapping("/success")
	public ServerResponseEntity<Map<String, String>> success() {
		Map<String, String> map = Map.of("name", "qing", "age", "18");
		return ServerResponseEntity.ofSuccess(map);
	}

	@Operation(summary = "探究responseentity")
	@GetMapping("/responseentity")
	public ResponseEntity<ServerResponseEntity<Map<String, String>>> responseentity() {
		Map<String, String> map = Map.of("name", "qing", "age", "18");
		ServerResponseEntity<Map<String, String>> responseEntity = ServerResponseEntity.ofSuccess(map);
		return ResponseEntity.ok(responseEntity);
	}

	@Operation(summary = "成功的Mono且携带数据函数 Mono.just(ApiResponse.ofSuccess(data))")
	@GetMapping("/mono")
	public Mono<ServerResponseEntity<Map<String, String>>> mono() {
		Map<String, String> map = Map.of("name", "qing", "age", "18");
		return Mono.just(ServerResponseEntity.ofSuccess(map));
	}

	@Operation(summary = "自定义异常 throw new JsonException()")
	@Parameter(name = "map", description = "测试参数", required = true, example = "{\"name\":\"张三\",\"age\":18}", schema = @Schema(implementation = Map.class))
	@PostMapping("/jsonException")
	public <T> ServerResponseEntity<T> jsonException(Map<String, Object> map) {
		throw new JsonException();
	}

	@Operation(summary = "极光发送全体安卓通知，每天只能十次")
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

	@Operation(summary = "极光发送Alias安卓通知，含extra字段")
	@GetMapping("/notificationAlias")
	public void testSendYxNotificationByAlias(String rid, String alias) throws APIConnectionException, APIRequestException {
		if(StringUtils.isBlank(rid)) {
			rid = "160a3797c903b80eda8";
		}
		if(StringUtils.isBlank(alias)) {
			alias = "yx";
		}
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>() {{
			put("type", "test");
		}});
		jiguangPushService.updateDeviceTagAlias(rid, alias, null, null);
		jiguangPushService.sendPushByAlias(notification, alias);
//		jiguangPushService.sendPushToAndroid(notification);
	}

	@Operation(summary = "极光发送RegistrationID安卓通知，含extra字段")
	@GetMapping("/notificationRid")
	public void testSendYxNotificationByRid(String rid) throws APIConnectionException, APIRequestException {
		if(StringUtils.isBlank(rid)) {
			rid = "160a3797c903b80eda8";
		}
		JiguangNotification notification = new JiguangNotification("云栖自定义标题", "云栖自定义通知内容！包含emoji😘", new HashMap<>() {{
			put("type", "test");
		}});
		jiguangPushService.sendPushByRegistrationID(notification, rid);
//		jiguangPushService.sendPushToAndroid(notification);
	}

	@Operation(summary = "flask-chat接口调用")
	@GetMapping("/getflaskapi")
	public Mono<ServerResponseEntity<String>> getFlaskApi() {
		return gptService.chat(new SimpleChatRequest("你好，你叫什么名字？"))
				.flatMap(chatResponse -> {
					log.info(chatResponse);
					return Mono.just(ServerResponseEntity.ofSuccess(chatResponse));
				});
	}

/*	@Operation(summary = "高德直线距离接口调用")
	@GetMapping("/getgdapi")
	public Mono<ServerResponseEntity<String>> getGdApi() {
		return geographicService.getStraightDistance("105.393178,28.860415", "105.4536,28.884362")
				.flatMap(distance -> {
					log.info(distance.toString());
					return Mono.just(ServerResponseEntity.ofSuccess(distance.toString()));
				});
	}*/

	@GetMapping("/getFollowed")
	@Operation(summary = "用户id获取关注列表（极简用户）")
	public ServerResponseEntity<List<SimpleUserVO>> getFollowed(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		return followController.getFollowed(uid);
	}
}
