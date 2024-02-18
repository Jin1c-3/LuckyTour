package com.luckytour.server.pojo;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.common.http.ServerStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qing
 * @date Created in 2024/2/16 20:00
 */
@Slf4j
public class Position {

	private final String latitude;

	private final String longitude;

	private static final String SEPARATOR = ",";

	@Override
	public String toString() {
		return longitude + SEPARATOR + latitude;
	}

	/**
	 * 私有化构造函数
	 */
	private Position(String longitude, String latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public static Position create(String positionStr) {
		log.debug("positionStr: {}", positionStr);
		if (!positionStr.contains(SEPARATOR)) {
			throw new BaseException(ServerStatus.ILLEGAL_ARGUMENT);
		}
		String[] split = positionStr.split(SEPARATOR);
		if (!Regex.isLongitude(split[0]) || !Regex.isLatitude(split[1])) {
			throw new BaseException(ServerStatus.ILLEGAL_ARGUMENT);
		}
		Position position = new Position(split[0], split[1]);
		if (!isValid(position)) {
			throw new BaseException(ServerStatus.ILLEGAL_ARGUMENT);
		}
		return position;
	}

	public static boolean isValid(Position position) {
		return Regex.isLatitude(position.latitude) && Regex.isLongitude(position.longitude);
	}

	/**
	 * 格式化key，用于cache key
	 * 经度保留3位小数，纬度保留4位小数
	 *
	 * @return key
	 */
	public String formatKey() {
		return String.format("%.3f,%.4f", Float.parseFloat(longitude), Float.parseFloat(latitude));
	}
}
