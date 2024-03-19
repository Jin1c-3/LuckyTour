package com.luckytour.server.util;

import com.luckytour.server.common.constant.Regex;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author qing
 * @date Created in 2024/2/28 7:29
 */
public class URIUtil {

	private URIUtil() {
	}

	/**
	 * 判断是否是url
	 *
	 * @param url url
	 * @return boolean
	 */
	private static boolean isUrl(String url) {
		return Regex.isURI(url);
	}

	/**
	 * 创建url
	 *
	 * @param request request
	 * @param url     url
	 * @return url
	 */
	private static String create(HttpServletRequest request, String url) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + url;
	}

	public static String parse(HttpServletRequest request, String url) {
		return !isUrl(url) ? create(request, url) : url;
	}
}
