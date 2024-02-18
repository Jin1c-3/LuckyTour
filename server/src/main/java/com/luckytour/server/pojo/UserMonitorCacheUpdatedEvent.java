package com.luckytour.server.pojo;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author qing
 * @date Created in 2024/2/18 23:34
 */
@Getter
public class UserMonitorCacheUpdatedEvent extends ApplicationEvent {
	private final UserMonitor userMonitor;

	public UserMonitorCacheUpdatedEvent(Object source, UserMonitor userMonitor) {
		super(source);
		this.userMonitor = userMonitor;
	}

}