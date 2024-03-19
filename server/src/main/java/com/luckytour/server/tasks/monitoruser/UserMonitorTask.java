package com.luckytour.server.tasks.monitoruser;

import com.luckytour.server.pojo.Position;
import com.luckytour.server.service.DynamicCheckService;
import com.luckytour.server.service.PushService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.vo.JiguangNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

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
	private ScheduledFuture<?> future; // Add this field

	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}

	public UserMonitorTask(String uid, DynamicCheckService dynamicCheckService, PushService jiguangPushService, UserService userService, UserMonitorCache userMonitorCache) {
		this.uid = uid;
		this.dynamicCheckService = dynamicCheckService;
		this.jiguangPushService = jiguangPushService;
		this.userService = userService;
		this.userMonitorCache = userMonitorCache;
	}

	@Override
	public void run() {
		UserRealTimeInfo userRealTimeInfo = userMonitorCache.getUserRealTimeInfo(uid);
		if (userRealTimeInfo == null) {
			log.debug("用户 {} 不存在", uid);
			return;
		}
		log.debug("用户 {} 被监视第 {} 次", userRealTimeInfo.getUserId(), userRealTimeInfo.getMonitorCount() + 1);
		Position position = userRealTimeInfo.getPosition();
		Mono<String> weatherCheck = dynamicCheckService.checkRealTimeWeather(position);
		Mono<String> somethingElseCheck = dynamicCheckService.checkSomeThingElse();

		Mono.zip(weatherCheck, somethingElseCheck)
				.flatMap(tuple -> {
					String weatherCheckResult = tuple.getT1();
					String somethingElseCheckResult = tuple.getT2();

					if (Boolean.TRUE.toString().equals(weatherCheckResult) && Boolean.TRUE.toString().equals(somethingElseCheckResult)) {
						return Mono.empty();
					}

					userService.getOptById(uid)
							.ifPresent(user -> {
								// 根据用户的极光推送ID发送推送
								if (StringUtils.isNotBlank(user.getJiguangRegistrationId())) {
									String nonTrueResult = Boolean.TRUE.toString().equals(weatherCheckResult) ? somethingElseCheckResult : weatherCheckResult;
									var varMap = new HashMap<String, String>();
									varMap.put("type", "weather");
									varMap.put("reason", nonTrueResult + "不适宜出行，将今天此时刻之后的景点更改为室内景点");
									varMap.put("pid", userRealTimeInfo.getPid());
									log.debug("推送状态：{} 内容：{} -> {}",
											jiguangPushService.sendPushByRegistrationID(new JiguangNotification("云栖天气通知", nonTrueResult, varMap), user.getJiguangRegistrationId()),
											"请注意，您" + nonTrueResult,
											user.getJiguangRegistrationId());
								}
							});

					userRealTimeInfo.incrementMonitorCount();
					if (userRealTimeInfo.getMonitorCount() >= 3) {
						userMonitorCache.removeUserRealTimeInfo(uid);
						future.cancel(false); // Cancel the task after it has been executed three times
					}
					return Mono.empty();
				}).subscribe();
	}
}