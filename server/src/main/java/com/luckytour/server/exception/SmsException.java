package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.http.ServerStatus;

/**
 * @author qing
 * @date Created in 2024/1/12 19:46
 */
public class SmsException extends BaseException {

	public SmsException() {
		super(ServerStatus.MYSQL_ERROR);
	}

	public SmsException(String message, Object data) {
		super(ServerStatus.MYSQL_ERROR.getCode(), message, data);
	}
}
