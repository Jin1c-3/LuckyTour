package com.luckytour.server.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取请求头中的 User-Agent 信息
 *
 * @author qing
 * @date Created in 2023/7/13 14:44
 */
@Slf4j
public class UserAgentUtil {

	private static final String UNKNOWN = "unknown";

	/**
	 * 获取ip地址
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		String comma = ",";
		String localhost = "127.0.0.1";
		if (ip.contains(comma)) {
			ip = ip.split(",")[0];
		}
		if (localhost.equals(ip)) {
			// 获取本机真正的ip地址
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				log.error(e.getMessage(), e);
			}
		}
		return ip;
	}

	/**
	 * 获取发起请求的浏览器名称
	 */
	public static String getBrowserName(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		Browser browser = userAgent.getBrowser();
		return browser.getName();
	}

	/**
	 * 获取发起请求的浏览器版本号
	 */
	public static String getBrowserVersion(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		//获取浏览器信息
		Browser browser = userAgent.getBrowser();
		//获取浏览器版本号
		Version version = browser.getVersion(header);
		return version.getVersion();
	}

	/**
	 * 获取发起请求的操作系统名称
	 */
	public static String getOsName(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		OperatingSystem operatingSystem = userAgent.getOperatingSystem();
		return operatingSystem.getName();
	}
}
