package com.luckytour.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.common.AopLog;
import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.util.AopLoggerUtil;
import com.luckytour.server.util.UserAgentUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author qing
 * @date Created in 2023/7/12 19:35
 */
@Aspect
@Component
@Slf4j
public class AopLogger4Controller {

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
		// 开始打印请求日志
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

		// 打印请求相关参数
		long startTime = System.currentTimeMillis();
		Object result = point.proceed();
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);

		final AopLog l = AopLog.builder()
				.threadId(Long.toString(Thread.currentThread().threadId()))
				.threadName(Thread.currentThread().getName())
				.ip(UserAgentUtil.getIp(request))
				.url(request.getRequestURL().toString())
				.classMethod(String.format("%s.%s",
						point.getSignature().getDeclaringTypeName(),
						point.getSignature().getName()))
				.httpMethod(request.getMethod())
				.requestParams(AopLoggerUtil.getNameAndValue(point))
				.result(result.toString().length() > Consts.AOP_LOG_MAX_LENGTH ? result.toString().substring(0, Consts.AOP_LOG_MAX_LENGTH) + "(... %s more)".formatted(result.toString().length() - Consts.AOP_LOG_MAX_LENGTH) : result.toString())
				.timeCost(System.currentTimeMillis() - startTime)
				.userAgent(header)
				.browser(userAgent.getBrowser().toString())
				.operatingSystem(userAgent.getOperatingSystem().toString()).build();
		try {
			log.info("请求日志: {}", new ObjectMapper().writeValueAsString(l));
		} catch (Exception e) {
			log.warn("AopLogger记录失败", e);
		}

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
