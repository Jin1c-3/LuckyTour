package com.luckytour.server.common.constant;

/**
 * @author qing
 * @date Created in 2024/1/17 20:27
 */
public interface Judgment {
	/**
	 * 直线距离过远，单位米
	 */
	int STRAIGHT_DISTANCE_TOO_FAR = 100000;

	/**
	 * 直线距离过远提示
	 */
	String STRAIGHT_DISTANCE_TOO_FAR_PROMPT = "%s和%s的距离过远，超过" + STRAIGHT_DISTANCE_TOO_FAR / 1000 + "km，计划不可行";
}
