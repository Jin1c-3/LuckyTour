package com.luckytour.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckytour.server.common.aoplogger.AopLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 控制器切面日志
 *
 * @author qing
 * @date Created in 2023/7/12 19:35
 */
@Aspect
@Component
@Slf4j
public class AopLogger4Controller {

	private final AopLogger aopLogger;

	@Autowired
	private AopLogger4Controller(AopLogger aopLogger) {
		this.aopLogger = aopLogger;
	}

	/**
	 * 定义controller的切点
	 */
	@Pointcut("execution(public * com.luckytour.server.controller.*.*(..))")
	public void controllerPointcut() {
	}

	/**
	 * controller方法执行前后打印请求日志
	 *
	 * @param point
	 */
	@Around("controllerPointcut()")
	public Object controllerAroundLogger(ProceedingJoinPoint point) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

		long startTime = System.currentTimeMillis();
		Object result = point.proceed();
		if (result instanceof Mono<?> monoResult) {
			return monoResult.doOnSuccess(item -> aopLogger.buildAopLog(request, startTime, point, item)
					.makeSerializable()
					.info());
		}
		aopLogger.buildAopLog(request, startTime, point, result)
				.makeSerializable()
				.info();
		return result;
	}

	//与全局异常处理函数功能重复，暂时注释掉
	/**
	 * controller异常捕捉打印日志
	 *
	 * @param point
	 * @param e
	 * @throws JsonProcessingException
	 */
	/*@AfterThrowing(value = "controllerPointcut()", throwing = "e")
	public void controllerThrowLogger(JoinPoint point, Throwable e) throws JsonProcessingException {
		log.error("{} 发生了异常...，相关参数是 {}",
				AopLoggerUtil.getClassMethod(point),
				new ObjectMapper().writeValueAsString(AopLoggerUtil.getNameAndValue(point)),
				e);
	}*/

}
