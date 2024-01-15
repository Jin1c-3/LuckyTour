package com.luckytour.server.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author qing
 * @date Created in 2023/8/2 8:39
 */
@Slf4j
@Component
@Aspect
public class AopLogger4GlobalExceptionHandler {

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
		Object[] args = point.getArgs();
		log.error("异常捕捉结束，方法返回值是 {}",
				result,
				(Exception) args[0]);
	}
}
