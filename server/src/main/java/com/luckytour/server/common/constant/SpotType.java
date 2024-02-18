package com.luckytour.server.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qing
 * @date Created in 2024/2/17 20:39
 */
@AllArgsConstructor
@Getter
public enum SpotType {

	HOTEL("hotel"),

	RESTAURANT("restaurant"),

	SCENIC_SPOT("scenicSpot");

	private final String type;
}
