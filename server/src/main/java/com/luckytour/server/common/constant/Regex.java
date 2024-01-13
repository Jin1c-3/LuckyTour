package com.luckytour.server.common.constant;

/**
 * @author qing
 * @date Created in 2024/1/12 15:42
 */
public interface Regex {
	String MOBILE_REGEX = "^1[3-9]\\d{9}$";
	String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	String EMAIL_OR_BLANK_REGEX = "^$|" + EMAIL_REGEX;
	String SEX_NUM_REGEX = "^[12]$";
	String SEX_NUM_OR_BLANK_REGEX = "^$|" + SEX_NUM_REGEX;
	String MOBILE_OR_BLANK_REGEX = "^$|" + MOBILE_REGEX;
	String NICKNAME_REGEX = "^[\u4e00-\u9fa5a-zA-Z0-9_-]{3,16}$";
	String NICKNAME_OR_BLANK_REGEX = "^$|" + NICKNAME_REGEX;
	String PASSWORD_REGEX = "^[a-zA-Z0-9]{30}$";
	String PASSWORD_OR_BLANK_REGEX = "^$|" + PASSWORD_REGEX;
	String MOBILE_OR_EMAIL_REGEX = "^(" + MOBILE_REGEX + ")|(" + EMAIL_REGEX + ")$";
}
