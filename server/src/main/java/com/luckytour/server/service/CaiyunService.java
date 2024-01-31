package com.luckytour.server.service;

import reactor.core.publisher.Mono;

/**
 * @author qing
 * @date Created in 2024/1/31 23:17
 */
public interface CaiyunService {

	/**
	 * 调用彩云api获取天气
	 * <a href="https://docs.caiyunapp.com/docs/daily/">彩云文档</a>
	 *
	 * @param position 经纬度
	 * @param date     日期
	 * @return 返回天气
	 */
	Mono<String> getWeather(String position, String date);
}
