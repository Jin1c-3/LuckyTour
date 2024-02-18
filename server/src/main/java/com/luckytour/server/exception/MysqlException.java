package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.http.ServerStatus;

/**
 * @author qing
 * @date Created in 2024/1/12 16:20
 */
public class MysqlException extends BaseException {

	public MysqlException() {
		super(ServerStatus.MYSQL_ERROR);
	}

	public MysqlException(String message, Object data) {
		super(ServerStatus.MYSQL_ERROR.getCode(), message, data);
	}
}
