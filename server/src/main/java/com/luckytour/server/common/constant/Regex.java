package com.luckytour.server.common.constant;

/**
 * @author qing
 * @date Created in 2024/1/12 15:42
 */
public final class Regex {
	public static final String MOBILE_REGEX = "^1[3-9]\\d{9}$";
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	public static final String EMAIL_OR_BLANK_REGEX = "^$|" + EMAIL_REGEX;
	public static final String SEX_NUM_REGEX = "^[12]$";
	public static final String SEX_NUM_OR_BLANK_REGEX = "^$|" + SEX_NUM_REGEX;
	public static final String MOBILE_OR_BLANK_REGEX = "^$|" + MOBILE_REGEX;
	public static final String NICKNAME_REGEX = "^[\u4e00-\u9fa5a-zA-Z0-9_-]{3,16}$";
	public static final String NICKNAME_OR_BLANK_REGEX = "^$|" + NICKNAME_REGEX;
	public static final String PASSWORD_REGEX = "^[a-zA-Z0-9]{6,30}$";
	public static final String PASSWORD_OR_BLANK_REGEX = "^$|" + PASSWORD_REGEX;
	public static final String MOBILE_OR_EMAIL_REGEX = "^(" + MOBILE_REGEX + ")|(" + EMAIL_REGEX + ")$";
	public static final String MALE_FEMALE_REGEX = "^[男女]$";
	public static final String MALE_FEMALE_OR_BLANK_REGEX = "^$|" + MALE_FEMALE_REGEX;
	public static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
	public static final String DATE_OR_BLANK_REGEX = "^$|" + DATE_REGEX;
	public static final String CHINESE_REGEX = "^[\u4e00-\u9fa5]{1,}$";
	public static final String POSITIVE_INTEGER_REGEX = "^[1-9]\\d*$";
	public static final String BLOG_TITLE_REGEX = "^.{1,255}$";
	public static final String LATITUDE_REGEX = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$";
	public static final String LONGITUDE_REGEX = "^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";
	public static final String URL_REGEX = "^(http|https)://[\\w-]+(\\.[\\w-]+)+([\\w-.,@?^=%&:/~+#]*[\\w@?^=%&/~+#])?$";

	public static boolean isMobile(String mobile) {
		return mobile.matches(MOBILE_REGEX);
	}

	public static boolean isEmail(String email) {
		return email.matches(EMAIL_REGEX);
	}

	public static boolean isLatitude(String latitude) {
		return latitude.matches(LATITUDE_REGEX);
	}

	public static boolean isLongitude(String longitude) {
		return longitude.matches(LONGITUDE_REGEX);
	}

	/**
	 * 私有化构造函数
	 */
	private Regex() {
	}
}
