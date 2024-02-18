package com.luckytour.server.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户监视
 *
 * @author qing
 * @date Created in 2024/2/18 22:34
 */
@Getter
@Setter
@Builder
public class UserMonitor implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String userId;
	private Position position;
	private int monitorCount;
}
