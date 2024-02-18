package com.luckytour.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qing
 * @date Created in 2024/1/16 16:07
 */
@Data
@AllArgsConstructor
public class Spot implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	/**
	 * 景点名称
	 */
	private String name;
	/**
	 * 景点所在城市
	 */
	private String cityname;
	/**
	 * 经纬度，逗号分隔
	 */
	private String location;
	/**
	 * 景点类型
	 */
	private String type;
	/**
	 * 交通方式
	 */
	private String trafficType;

}