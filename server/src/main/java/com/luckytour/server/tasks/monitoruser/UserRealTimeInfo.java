package com.luckytour.server.tasks.monitoruser;

import com.luckytour.server.pojo.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * 用户监视
 *
 * @author qing
 * @date Created in 2024/2/18 22:34
 */
@Getter
@Builder
@ToString
public class UserRealTimeInfo implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String userId;
	private String pid;
	private Position position;
	private int monitorCount;
	private transient Future<?> future;

	public void incrementMonitorCount() {
		monitorCount++;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}
}
