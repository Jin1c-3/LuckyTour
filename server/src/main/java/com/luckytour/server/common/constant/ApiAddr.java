package com.luckytour.server.common.constant;

/**
 * @author qing
 * @date Created in 2024/1/16 14:39
 */
public interface ApiAddr {
	/**
	 * flask单步chat接口
	 */
	String FLASK_CHAT = "/chat";

	/**
	 * 高德距离api
	 */
	String GAODE_DISTANCE = "https://restapi.amap.com/v3/distance?origins={origins}&destination={destination}&key={key}";

	/**
	 * 彩云天气api
	 */
	String CAIYUN_DAILY_WEATHER = "https://api.caiyunapp.com/v2.6/{key}/%s/daily?dailysteps=15";
}
