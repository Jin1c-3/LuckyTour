package com.luckytour.server.pojo;

import com.luckytour.server.service.ScheduledExecutorSupplier;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户监视容器
 *
 * @author qing
 * @date Created in 2024/2/18 22:35
 */
@Component
@Getter
public class UserMonitorCache {

	private final ConcurrentHashMap<String, UserMonitor> cache = new ConcurrentHashMap<>();

	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public UserMonitorCache(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void addOrUpdateUserMonitor(UserMonitor userMonitor) {
		cache.put(userMonitor.getUserId(), userMonitor);
		eventPublisher.publishEvent(new UserMonitorCacheUpdatedEvent(this, userMonitor));
	}

	public UserMonitor getUserMonitor(String userId) {
		return cache.get(userId);
	}

	public void removeUserMonitor(String userId) {
		cache.remove(userId);
	}
}
