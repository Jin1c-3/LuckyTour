package com.luckytour.server.common;

import com.luckytour.server.common.http.IServerStatus;
import com.luckytour.server.common.http.ServerStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常基类
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
	private final Integer code;

	/**
	 * 异常信息
	 */
	private final String message;

	/**
	 * 异常数据
	 */
	private transient Object data;

	public BaseException() {
		throw new BaseException(ServerStatus.UNKNOWN_ERROR);
	}

	public BaseException(IServerStatus status) {
		super(status.getMessage());
		this.code = status.getCode();
		this.message = status.getMessage();
	}

	public BaseException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BaseException(IServerStatus status, Object data) {
		this(status);
		this.data = data;
	}

	public BaseException(Integer code, String message, Object data) {
		this(code, message);
		this.data = data;
	}
}
