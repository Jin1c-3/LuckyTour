package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;

/**
 * @author qing
 * @date Created in 2024/1/13 16:08
 */
public class FileException extends BaseException {
	public FileException() {
		super(ApiStatus.FILE_ERROR);
	}

	public FileException(String message, Object data) {
		super(ApiStatus.FILE_ERROR.getCode(), message, data);
	}
}
