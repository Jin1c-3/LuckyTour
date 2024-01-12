package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;

/**
 * @author qing
 * @date Created in 2024/1/12 19:43
 */
public class EMailException extends BaseException {
	public EMailException() {
		super(ApiStatus.EMAIL_ERROR);
	}

	public EMailException(String message, Object data) {
		super(ApiStatus.EMAIL_ERROR.getCode(), message, data);
	}
}
