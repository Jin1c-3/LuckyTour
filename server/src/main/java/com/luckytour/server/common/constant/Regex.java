package com.luckytour.server.common.constant;

/**
 * @author qing
 * @date Created in 2024/1/12 15:42
 */
public interface Regex {
	String MOBILE_REGEX = "^1[3-9]\\d{9}$";
	String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
}
