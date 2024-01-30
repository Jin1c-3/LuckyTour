package com.luckytour.server.common.constant;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/17 20:27
 */
public class Judgment {
	/**
	 * 直线距离过远，单位米
	 */
	public static final int STRAIGHT_DISTANCE_TOO_FAR = 1000 * 1000;

	/**
	 * 直线距离过远提示
	 */
	private static final String STRAIGHT_DISTANCE_TOO_FAR_PROMPT = "%s和%s的距离过远，超过" + STRAIGHT_DISTANCE_TOO_FAR / 1000 + "km，计划不可行";

	/**
	 * 定义彩云天气中的好天气
	 */
	public static final List<String> GOOD_CAIYUN_WEATHER = List.of("晴（白天）", "晴（夜间）", "多云（白天）", "多云（夜间）", "阴");

	/**
	 * 天气不佳提示
	 * 何时何地天气不佳
	 */
	private static final String BAD_WEATHER_PROMPT = "%s日%s天气为%s，不适合出行";

	public static String getTooFarPrompt(String location1, String location2) {
		return String.format(STRAIGHT_DISTANCE_TOO_FAR_PROMPT, location1, location2);
	}

	public static String getBadWeatherPrompt(String date, String location, String weather) {
		return String.format(BAD_WEATHER_PROMPT, date, location, weather);
	}
}
