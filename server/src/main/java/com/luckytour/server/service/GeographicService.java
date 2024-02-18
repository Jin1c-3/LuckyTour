package com.luckytour.server.service;

import com.luckytour.server.pojo.Position;
import reactor.core.publisher.Mono;

/**
 * @author qing
 * @date Created in 2024/1/31 22:20
 */
public interface GeographicService {

	/**
	 * 通过经纬度获取两地直线距离
	 *
	 * @param origins     起点经纬度
	 * @param destination 终点经纬度
	 * @return 返回两地直线距离，单位是米
	 */
	Mono<Integer> getStraightDistance(Position origins, Position destination);

	/**
	 * 通过地址获取经纬度
	 *
	 * @param address 地址
	 * @return 返回经纬度
	 */
	Mono<String> getLongitudeAndLatitude(String address);
}
