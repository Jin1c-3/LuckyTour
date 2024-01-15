package com.luckytour.server.exception;

/**
 * @author qing
 * @date Created in 2023/8/2 14:59
 */
public class RedisOpsResultIsNullException extends RedisException {
	public RedisOpsResultIsNullException() {
		super("Redis 操作结果为空", null);
	}
}
