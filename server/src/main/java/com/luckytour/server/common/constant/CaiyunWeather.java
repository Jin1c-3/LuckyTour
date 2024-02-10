package com.luckytour.server.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/1/29 22:08
 */
public final class CaiyunWeather {
	private CaiyunWeather() {
	}

	public static final Map<String, String> WEATHER_MAP = new HashMap<>();

	static {
		WEATHER_MAP.put("CLEAR_DAY", "晴（白天）");
		WEATHER_MAP.put("CLEAR_NIGHT", "晴（夜间）");
		WEATHER_MAP.put("PARTLY_CLOUDY_DAY", "多云（白天）");
		WEATHER_MAP.put("PARTLY_CLOUDY_NIGHT", "多云（夜间）");
		WEATHER_MAP.put("CLOUDY", "阴");
		WEATHER_MAP.put("WIND", "大风");
		WEATHER_MAP.put("LIGHT_RAIN", "小雨");
		WEATHER_MAP.put("MODERATE_RAIN", "中雨");
		WEATHER_MAP.put("HEAVY_RAIN", "大雨");
		WEATHER_MAP.put("STORM_RAIN", "暴雨");
		WEATHER_MAP.put("FOG", "雾");
		WEATHER_MAP.put("LIGHT_SNOW", "小雪");
		WEATHER_MAP.put("MODERATE_SNOW", "中雪");
		WEATHER_MAP.put("HEAVY_SNOW", "大雪");
		WEATHER_MAP.put("STORM_SNOW", "暴雪");
		WEATHER_MAP.put("DUST", "浮尘");
		WEATHER_MAP.put("SAND", "沙尘");
		WEATHER_MAP.put("LIGHT_HAZE", "轻度雾霾");
		WEATHER_MAP.put("MODERATE_HAZE", "中度雾霾");
		WEATHER_MAP.put("HEAVY_HAZE", "重度雾霾");
	}
}
