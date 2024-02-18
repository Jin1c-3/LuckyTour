package com.luckytour.server.exception;


import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.http.ServerStatus;

/**
 * @author qing
 * @date Created in 2023/8/2 14:23
 */
public class RedisException extends BaseException {

	public RedisException() {
		super(ServerStatus.REDIS_ERROR);
	}

	public RedisException(String message, Object data) {
		super(ServerStatus.REDIS_ERROR.getCode(), message, data);
	}
}
