package com.luckytour.server.common.constant;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/17 20:27
 */
public final class Judgment {
	/**
	 * 直线距离过远，单位米
	 */
	public static final int STRAIGHT_DISTANCE_TOO_FAR = 1000 * 1000;

	/**
	 * 直线距离过远提示
	 */
	private static final String STRAIGHT_DISTANCE_TOO_FAR_PROMPT = "%s和%s的距离过远，超过" + STRAIGHT_DISTANCE_TOO_FAR / 1000 + "km，计划不可行~";

	/**
	 * 定义彩云天气中的好天气
	 * <p>
	 * 所有天气如下：
	 * 晴（白天）	CLEAR_DAY	cloudrate < 0.2
	 * 晴（夜间）	CLEAR_NIGHT	cloudrate < 0.2
	 * 多云（白天）	PARTLY_CLOUDY_DAY	0.8 >= cloudrate > 0.2
	 * 多云（夜间）	PARTLY_CLOUDY_NIGHT	0.8 >= cloudrate > 0.2
	 * 阴	CLOUDY	cloudrate > 0.8
	 * 轻度雾霾	LIGHT_HAZE	PM2.5 100~150
	 * 中度雾霾	MODERATE_HAZE	PM2.5 150~200
	 * 重度雾霾	HEAVY_HAZE	PM2.5 > 200
	 * 小雨	LIGHT_RAIN	见 降水强度
	 * 中雨	MODERATE_RAIN	见 降水强度
	 * 大雨	HEAVY_RAIN	见 降水强度
	 * 暴雨	STORM_RAIN	见 降水强度
	 * 雾	FOG	能见度低，湿度高，风速低，温度低
	 * 小雪	LIGHT_SNOW	见 降水强度
	 * 中雪	MODERATE_SNOW	见 降水强度
	 * 大雪	HEAVY_SNOW	见 降水强度
	 * 暴雪	STORM_SNOW	见 降水强度
	 * 浮尘	DUST	AQI > 150, PM10 > 150，湿度 < 30%，风速 < 6 m/s
	 * 沙尘	SAND	AQI > 150, PM10> 150，湿度 < 30%，风速 > 6 m/s
	 * 大风	WIND
	 * </p>
	 */
	public static final List<String> GOOD_CAIYUN_WEATHER = List.of(
			"晴（白天）",
			"晴（夜间）",
			"多云（白天）",
			"多云（夜间）",
			"阴",
			"小雪",
			"小雨",
			"轻度雾霾"
	);

	/**
	 * 天气不佳提示
	 * 何时何地天气不佳
	 */
	private static final String BAD_WEATHER_PROMPT = "%s日%s天气为%s，不适合出行~";

	private static final String BAD_WEATHER_FRIENDLY_ALERT = "所处位置未来%s小时天气为%s...";

	public static String getTooFarPrompt(String location1, String location2) {
		return String.format(STRAIGHT_DISTANCE_TOO_FAR_PROMPT, location1, location2);
	}

	public static String getBadWeatherPrompt(String date, String location, String weather) {
		return String.format(BAD_WEATHER_PROMPT, date, location, weather);
	}

	public static String getBadWeatherFriendlyAlert(String date, String weather) {
		return String.format(BAD_WEATHER_FRIENDLY_ALERT, date, weather);
	}

	/**
	 * 私有构造方法
	 */
	private Judgment() {
	}
}
