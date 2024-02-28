package com.luckytour.server.util;

import com.luckytour.server.common.constant.Regex;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author qing
 * @date Created in 2024/2/28 7:29
 */
public class URLUtil {
	public static boolean isNotUrl(String url) {
		if (url == null) {
			return true;
		}
		return !url.matches(Regex.URL_REGEX);
	}

	public static String create(HttpServletRequest request, String url) {
		url = url.replace("\\", "/");
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + url;
	}
}
