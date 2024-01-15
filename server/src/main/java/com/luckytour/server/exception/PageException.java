package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;
import lombok.Getter;

/**
 * @author qing
 * @date Created in 2023/7/12 17:13
 */
@Getter
public class PageException extends BaseException {
	public PageException() {
		super(ApiStatus.UNKNOWN_ERROR);
	}

	public PageException(ApiStatus status) {
		super(status);
	}

	public PageException(Integer code, String message) {
		super(code, message);
	}
}
