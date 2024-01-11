package com.luckytour.server.exception.handler;

import com.luckytour.server.common.ApiResponse;
import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.controller.AopLogger4Controller;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * <p>
 * 统一异常处理
 * 请在 {@link AopLogger4Controller} 中考虑日志问题
 * </p>
 *
 * @author qing
 * @date Created in 2023/7/12 17:14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * 统一 异常处理
	 * {@link ConstraintViolationException}校验框架(如Hibernate Validator)校验失败时抛出,用于处理参数校验和返回值校验失败的场景
	 *
	 * @param e Exception
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public <T> ApiResponse<T> exceptionHandler(Exception e) {
		if (e instanceof NoHandlerFoundException) {
			return ApiResponse.ofStatus(ApiStatus.NOT_FOUND);
		} else if (e instanceof HttpRequestMethodNotSupportedException) {
			return ApiResponse.ofStatus(ApiStatus.METHOD_NOT_ALLOWED);
		} else if (e instanceof MethodArgumentNotValidException me) {
			return ApiResponse.of(ApiStatus.BAD_REQUEST.getCode(),
					me.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
					null);
		} else if (e instanceof ConstraintViolationException ce) {
			return ApiResponse.of(ApiStatus.BAD_REQUEST.getCode(),
					CollectionUtils.get(ce.getConstraintViolations(), 0).getMessage(),
					null);
		} else if (e instanceof MethodArgumentTypeMismatchException) {
			return ApiResponse.ofStatus(ApiStatus.PARAM_NOT_MATCH);
		} else if (e instanceof HttpMessageNotReadableException) {
			return ApiResponse.ofStatus(ApiStatus.PARAM_NOT_NULL);
		} else if (e instanceof BaseException) {
			return ApiResponse.ofException(e);
		}
		return ApiResponse.ofStatus(ApiStatus.UNKNOWN_ERROR);
	}
}
