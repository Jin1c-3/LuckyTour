package com.luckytour.server.pojo;

import com.luckytour.server.service.DynamicCheckService;
import com.luckytour.server.service.PushService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.vo.JiguangNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

/**
 * @author qing
 * @date Created in 2024/2/18 22:39
 */
@Slf4j
public class UserMonitorTask implements Runnable {
	private final String uid;
	private final DynamicCheckService dynamicCheckService;
	private final PushService jiguangPushService;
	private final UserService userService;
	private final UserMonitorCache userMonitorCache;

	public UserMonitorTask(String uid, DynamicCheckService dynamicCheckService, PushService jiguangPushService, UserService userService, UserMonitorCache userMonitorCache) {
		this.uid = uid;
		this.dynamicCheckService = dynamicCheckService;
		this.jiguangPushService = jiguangPushService;
		this.userService = userService;
		this.userMonitorCache = userMonitorCache;
	}

	@Override
	public void run() {
		UserMonitor userMonitor = userMonitorCache.getUserMonitor(uid);
		if (userMonitor != null) {
			log.debug("用户 {} 被监视第 {} 次", userMonitor.getUserId(), userMonitor.getMonitorCount());
			Position position = userMonitor.getPosition();
			Mono<String> weatherCheck = dynamicCheckService.checkRealTimeWeather(position);
			Mono<String> somethingElseCheck = dynamicCheckService.checkSomeThingElse();

			Mono.zip(weatherCheck, somethingElseCheck)
					.flatMap(tuple -> {
						String weatherCheckResult = tuple.getT1();
						String somethingElseCheckResult = tuple.getT2();

						if (!Boolean.TRUE.toString().equals(weatherCheckResult) || !Boolean.TRUE.toString().equals(somethingElseCheckResult)) {
							userService.getOptById(uid)
									.ifPresent(user -> {
										// 根据用户的极光推送ID发送推送
										if (StringUtils.isNotBlank(user.getJiguangRegistrationId())) {
											String nonTrueResult = Boolean.TRUE.toString().equals(weatherCheckResult) ? somethingElseCheckResult : weatherCheckResult;
											if (jiguangPushService.sendPushByRegistrationID(new JiguangNotification(nonTrueResult), user.getJiguangRegistrationId())) {
												log.debug("极光推送检测状态成功 {} -> {}", nonTrueResult, user.getJiguangRegistrationId());
											} else {
												log.debug("极光推送检测状态失败 {} -> {}", nonTrueResult, user.getJiguangRegistrationId());
											}
										}
									});
						}

						userMonitor.setMonitorCount(userMonitor.getMonitorCount() + 1);
						if (userMonitor.getMonitorCount() >= 3) {
							userMonitorCache.removeUserMonitor(uid);
						}
						return Mono.empty();
					}).subscribe();
			log.debug("用户 {} 一轮监视结束", userMonitor.getUserId());
		}
	}
}