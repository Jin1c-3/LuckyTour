package com.luckytour.server.common.constant;

/**
 * @author qing
 * @date Created in 2023/8/1 10:59
 */
public interface IApiStatus {

	/**
	 * 获取状态码
	 *
	 * @return
	 */
	Integer getCode();

	/**
	 * 获取状态信息
	 *
	 * @return
	 */
	String getMessage();
}
