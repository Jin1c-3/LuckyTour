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
}
