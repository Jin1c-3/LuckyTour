package com.luckytour.server.service;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/31 22:20
 */
public interface GaodeService {
	/**
	 * 调用高德api获取两地直线距离
	 * <a href="https://lbs.amap.com/api/webservice/guide/api/direction#distance">高德文档</a>
	 *
	 * @param origins     起点经纬度
	 * @param destination 终点经纬度
	 * @return 返回两地直线距离，单位是米
	 */
	Mono<List<Integer>> getStraightDistance(List<String> origins, String destination);

	/**
	 * 调用高德api获取地址的经纬度
	 * <a href="https://lbs.amap.com/api/webservice/guide/api/georegeo">高德文档</a>
	 *
	 * @param address 地址
	 * @return 返回经纬度，格式是"经度,纬度"
	 */
	Mono<String> getLocation(String address);
}
