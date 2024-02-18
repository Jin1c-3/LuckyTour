package com.luckytour.server.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qing
 * @date Created in 2024/2/18 11:27
 */
@AllArgsConstructor
@Getter
public enum TrafficType {

	FOOT("onFoot"),

	TAXI("taxi"),

	BUS("bus"),

	CAR("byCar"),

	NOT_SURE("notSure");

	private final String type;
}
