package com.luckytour.server.exception.handler;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.controller.AopLogger4Controller;
import com.luckytour.server.exception.MysqlException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
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
	public <T> ServerResponseEntity<T> exceptionHandler(Exception e) {
		if (e instanceof NoHandlerFoundException) {
			return ServerResponseEntity.ofStatus(ServerStatus.NOT_FOUND);
		} else if (e instanceof HttpRequestMethodNotSupportedException) {
			return ServerResponseEntity.ofStatus(ServerStatus.METHOD_NOT_ALLOWED);
		} else if (e instanceof MethodArgumentNotValidException me) {
			return ServerResponseEntity.of(ServerStatus.BAD_REQUEST.getCode(),
					me.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
					null);
		} else if (e instanceof ConstraintViolationException ce) {
			String message = ce.getConstraintViolations().stream()
					.findFirst()
					.map(ConstraintViolation::getMessage)
					.orElse(null);
			return ServerResponseEntity.of(ServerStatus.BAD_REQUEST.getCode(), message, null);
		} else if (e instanceof MethodArgumentTypeMismatchException) {
			return ServerResponseEntity.ofStatus(ServerStatus.PARAM_NOT_MATCH);
		} else if (e instanceof APIConnectionException) {
			return ServerResponseEntity.ofStatus(ServerStatus.JIGUANG_CONNECTION_ERROR);
		} else if (e instanceof APIRequestException) {
			return ServerResponseEntity.ofStatus(ServerStatus.JIGUANG_REQUEST_ERROR);
		} else if (e instanceof HttpMessageNotReadableException) {
			return ServerResponseEntity.ofStatus(ServerStatus.PARAM_NOT_NULL);
		} else if (e instanceof MysqlException) {
			return ServerResponseEntity.ofStatus(ServerStatus.MYSQL_ERROR);
		} else if (e instanceof RedisConnectionFailureException) {
			return ServerResponseEntity.ofStatus(ServerStatus.REDIS_ERROR);
		} else if (e instanceof BaseException) {
			return ServerResponseEntity.ofException(e);
		}
		return ServerResponseEntity.ofStatus(ServerStatus.UNKNOWN_ERROR);
	}
}
