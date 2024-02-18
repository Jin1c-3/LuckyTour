package com.luckytour.server.controller;

import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.pojo.Position;
import com.luckytour.server.service.DynamicCheckService;
import com.luckytour.server.service.PushService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.JiguangNotification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 动态监视控制器
 *
 * @author qing
 * @date Created in 2024/2/16 17:00
 */
@RestController
@RequestMapping("/dynamic")
@Tag(name = "动态监视控制器")
@CrossOrigin
@Slf4j
@Validated
public class DynamicController {

	private final DynamicCheckService dynamicCheckService;

	@Resource
	private final PushService jiguangPushService;

	private final UserService userService;

	@Autowired
	public DynamicController(DynamicCheckService dynamicCheckService, PushService jiguangPushService, UserService userService) {
		this.dynamicCheckService = dynamicCheckService;
		this.jiguangPushService = jiguangPushService;
		this.userService = userService;
	}

	@PostMapping("/monitor")
	@Operation(summary = "监视用户状态")
	public Mono<ServerResponseEntity<String>> monitor(@NotBlank(message = Alert.PARAM_IS_NULL) String latitudeAndLongitude, HttpServletRequest request) {
		Mono<String> weatherCheck = dynamicCheckService.checkRealTimeWeather(Position.create(latitudeAndLongitude));
		Mono<String> somethingElseCheck = dynamicCheckService.checkSomeThingElse();

		return Mono.zip(weatherCheck, somethingElseCheck)
				.flatMap(tuple -> {
					String weatherCheckResult = tuple.getT1();
					String somethingElseCheckResult = tuple.getT2();

					if (Boolean.TRUE.toString().equals(weatherCheckResult) && Boolean.TRUE.toString().equals(somethingElseCheckResult)) {
						return Mono.just(ServerResponseEntity.ofSuccess());
					}
					String nonTrueResult = Boolean.TRUE.toString().equals(weatherCheckResult) ? somethingElseCheckResult : weatherCheckResult;
					userService.getOptById(JwtUtil.parseId(request))
							.ifPresent(user -> {
								// 根据用户的极光推送ID发送推送
								if (StringUtils.isNotBlank(user.getJiguangRegistrationId())) {
									if (jiguangPushService.sendPushByRegistrationID(new JiguangNotification(nonTrueResult), user.getJiguangRegistrationId())) {
										log.debug("极光推送检测状态成功 {} -> {}", nonTrueResult, user.getJiguangRegistrationId());
									} else {
										log.debug("极光推送检测状态失败 {} -> {}", nonTrueResult, user.getJiguangRegistrationId());
									}
								}
							});
					return Mono.just(ServerResponseEntity.ofSuccess(nonTrueResult));
				});
	}
}
