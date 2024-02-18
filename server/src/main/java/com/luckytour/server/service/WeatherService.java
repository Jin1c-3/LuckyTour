package com.luckytour.server.service;

import com.luckytour.server.pojo.Position;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/31 23:17
 */
public interface WeatherService {

	/**
	 * 通过经纬度和日期获取某一天的天气
	 *
	 * @param position 经纬度
	 * @param date     日期
	 * @return 返回天气
	 */
	Mono<String> getDailyWeather(Position position, String date);

	/**
	 * 通过经纬度获取未来n个小时的天气
	 *
	 * @param position 经纬度
	 * @return 返回天气
	 */
	Mono<List<String>> getHourlyWeather(Position position);
}
