package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;

/**
 * @author qing
 * @date Created in 2024/1/12 19:46
 */
public class SmsException extends BaseException {

	public SmsException() {
		super(ApiStatus.MYSQL_ERROR);
	}

	public SmsException(String message, Object data) {
		super(ApiStatus.MYSQL_ERROR.getCode(), message, data);
	}
}
