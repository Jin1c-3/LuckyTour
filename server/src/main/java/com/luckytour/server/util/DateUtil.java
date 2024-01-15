package com.luckytour.server.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author qing
 * @date Created in 2023/8/2 17:48
 */
public class DateUtil {
	public static Date offset(Date date, int offset, ChronoUnit unit) {
		Instant instant = date.toInstant();
		instant = instant.plus(offset, unit);
		return Date.from(instant.atZone(ZoneId.systemDefault()).toInstant());
	}
}
