package com.luckytour.server.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckytour.server.common.aoplogger.AopLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author qing
 * @date Created in 2023/8/2 8:39
 */
@Slf4j
@Component
@Aspect
public class AopLogger4GlobalExceptionHandler {

	private final AopLogger aopLogger;

	@Autowired
	private AopLogger4GlobalExceptionHandler(AopLogger aopLogger) {
		this.aopLogger = aopLogger;
	}

	/**
	 * 定义全局异常的切点
	 */
	@Pointcut("execution(public * com.luckytour.server.exception.handler.GlobalExceptionHandler.*(..))")
	public void exceptionHandlerPointcut() {
	}

	/**
	 * 全局异常捕捉打印日志
	 *
	 * @param point
	 * @param result
	 * @throws JsonProcessingException jackson序列化异常
	 */
	@AfterReturning(value = "exceptionHandlerPointcut()", returning = "result")
	public void exceptionHandlerResultLogger(JoinPoint point, Object result) throws JsonProcessingException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
		long startTime = System.currentTimeMillis();
		aopLogger.buildAopLog(request, startTime, point, result)
				.warn((Exception) point.getArgs()[0]);
	}
}
