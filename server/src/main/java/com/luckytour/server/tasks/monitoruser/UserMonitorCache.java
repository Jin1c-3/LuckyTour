package com.luckytour.server.tasks.monitoruser;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户监视容器
 *
 * @author qing
 * @date Created in 2024/2/18 22:35
 */
@Component
@Getter
@Slf4j
public class UserMonitorCache {

	private final ConcurrentHashMap<String, UserRealTimeInfo> cache = new ConcurrentHashMap<>();

	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public UserMonitorCache(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void addOrUpdateUserMonitor(UserRealTimeInfo userRealTimeInfo) {
		if (cache.containsKey(userRealTimeInfo.getUserId())) {
			userRealTimeInfo.setFuture(cache.get(userRealTimeInfo.getUserId()).getFuture());
		}
		cache.put(userRealTimeInfo.getUserId(), userRealTimeInfo);
		synchronized (cache) {
			// 遍历 ConcurrentHashMap
			cache.forEach((key, value) -> log.debug("用户监视容器更新 {} -> {}", key, value.toString()));
		}
		eventPublisher.publishEvent(new UserMonitorCacheUpdatedEvent(this, userRealTimeInfo));
	}

	public UserRealTimeInfo getUserRealTimeInfo(String userId) {
		return cache.get(userId);
	}

	public void removeUserRealTimeInfo(String userId) {
		cache.remove(userId);
	}
}
