package com.luckytour.server.exception;


import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.http.IServerStatus;

/**
 * @author qing
 * @date Created in 2023/8/2 21:57
 */
public class SecurityException extends BaseException {

	public SecurityException(IServerStatus apiStatus) {
		super(apiStatus);
	}

	public SecurityException(Integer code, String message, Object data) {
		super(code, message, data);
	}
}
