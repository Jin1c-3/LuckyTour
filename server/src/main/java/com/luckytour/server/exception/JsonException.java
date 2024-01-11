package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;
import lombok.Getter;

/**
 * <p>
 * JSON异常
 * </p>
 *
 * @author qing
 * @date Created in 2023/7/12 17:10
 */
@Getter
public class JsonException extends BaseException {
	public JsonException() {
		super(ApiStatus.JSON_ERROR);
	}

	public JsonException(ApiStatus status) {
		super(status);
	}

	public JsonException(Integer code, String message) {
		super(code, message);
	}
}
