package com.luckytour.server.common.http;

/**
 * @author qing
 * @date Created in 2023/8/1 10:59
 */
public interface IServerStatus {

	/**
	 * 获取状态码
	 *
	 * @return 状态码
	 */
	Integer getCode();

	/**
	 * 获取状态信息
	 *
	 * @return 状态信息
	 */
	String getMessage();
}
