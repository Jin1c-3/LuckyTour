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
	public Mono<List<Integer>> getStraightDistance(List<String> origins, String destination);
}
