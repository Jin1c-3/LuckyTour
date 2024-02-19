package com.luckytour.server.tasks.monitoruser;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author qing
 * @date Created in 2024/2/18 23:34
 */
@Getter
public class UserMonitorCacheUpdatedEvent extends ApplicationEvent {

	private final UserRealTimeInfo userRealTimeInfo;

	public UserMonitorCacheUpdatedEvent(Object source, UserRealTimeInfo userRealTimeInfo) {
		super(source);
		this.userRealTimeInfo = userRealTimeInfo;
	}

}