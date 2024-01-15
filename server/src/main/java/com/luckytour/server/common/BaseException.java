package com.luckytour.server.common;

import com.luckytour.server.common.constant.IApiStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 异常基类
 * </p> *
 *
 * @author qing
 * @date Created in 2023/7/12 17:06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 异常信息
	 */
	private String message;

	/**
	 * 异常数据
	 */
	private Object data;

	public BaseException(IApiStatus status) {
		super(status.getMessage());
		this.code = status.getCode();
		this.message = status.getMessage();
	}

	public BaseException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BaseException(IApiStatus status, Object data) {
		this(status);
		this.data = data;
	}

	public BaseException(Integer code, String message, Object data) {
		this(code, message);
		this.data = data;
	}
}
