package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;

/**
 * @author qing
 * @date Created in 2024/1/12 16:20
 */
public class MysqlException extends BaseException {

	public MysqlException() {
		super(ApiStatus.MYSQL_ERROR);
	}

	public MysqlException(String message, Object data) {
		super(ApiStatus.MYSQL_ERROR.getCode(), message, data);
	}
}
