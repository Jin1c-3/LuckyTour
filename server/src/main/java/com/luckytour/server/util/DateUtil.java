package com.luckytour.server.util;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.Regex;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author qing
 * @date Created in 2023/8/2 17:48
 */
public class DateUtil {
	/**
	 * 日期偏移
	 *
	 * @param date   日期
	 * @param offset 偏移量
	 * @param unit   偏移单位
	 * @return 偏移后的日期
	 */
	public static Date offset(Date date, int offset, ChronoUnit unit) {
		Instant instant = date.toInstant();
		instant = instant.plus(offset, unit);
		return Date.from(instant.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 编译日期
	 *
	 * @param date 日期
	 * @return 编译后的日期
	 */

	public static Date parse(String date) {
		if (!date.matches(Regex.DATE_REGEX)) {
			throw new BaseException(500, "日期格式错误");
		}
		return Date.from(LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
