package com.luckytour.server.util;

import com.luckytour.server.common.constant.Regex;

/**
 * @author qing
 * @date Created in 2024/1/12 15:46
 */
public class ValidationUtil {
	public static boolean isMobile(String mobile) {
		return mobile.matches(Regex.MOBILE_REGEX);
	}

	public static boolean isEmail(String email) {
		return email.matches(Regex.EMAIL_REGEX);
	}
}
