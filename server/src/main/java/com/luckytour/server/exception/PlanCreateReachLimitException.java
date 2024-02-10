package com.luckytour.server.exception;

import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;

/**
 * @author qing
 * @date Created in 2024/2/7 17:06
 */
public class PlanCreateReachLimitException extends BaseException {
	public PlanCreateReachLimitException() {
		super(ApiStatus.PLAN_CREATE_REACH_LIMIT);
	}

	public PlanCreateReachLimitException(Integer code, String message) {
		super(code, message);
	}
}