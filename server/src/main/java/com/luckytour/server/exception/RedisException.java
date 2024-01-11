package com.luckytour.server.exception;


import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;

/**
 * @author qing
 * @date Created in 2023/8/2 14:23
 */
public class RedisException extends BaseException {

	public RedisException() {
		super(ApiStatus.REDIS_ERROR);
	}

	public RedisException(String message, Object data) {
		super(ApiStatus.REDIS_ERROR.getCode(), message, data);
	}
}
