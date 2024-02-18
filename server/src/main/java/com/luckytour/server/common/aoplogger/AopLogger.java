package com.luckytour.server.common.aoplogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.luckytour.server.util.UserAgentUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2023/8/2 8:33
 */
@Slf4j
@Component
public class AopLogger {

	private final ObjectMapper objectMapper;

	private final int aopMaxLength;

	private AopLog aopLog;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AopLog {
		// http方法 GET POST PUT DELETE PATCH
		private String httpMethod;
		// 类方法
		private String classMethod;
		// 请求参数
		private Object requestParams;
		// ip
		private String ip;
		// url
		private String url;
		// 接口耗时
		private Long timeCost;
		// 返回参数
		private Object result;
		// 线程id
		private String threadId;
		// 线程名称
		private String threadName;
		// 操作系统
		private String operatingSystem;
		// 浏览器
		private String browser;
		// user-agent
		private String userAgent;

	}

	/**
	 * 私有构造函数
	 */
	@Autowired
	private AopLogger(ObjectMapper objectMapper, @Value("${logging.aop.max-length}") int aopMaxLength) {
		this.aopMaxLength = aopMaxLength;
		this.objectMapper = objectMapper;
		SimpleModule module = new SimpleModule();
		module.addSerializer(HttpMethod.class, new HttpMethodSerializer());
		objectMapper.registerModule(module);
	}

	/**
	 * 获取方法参数名和参数值
	 *
	 * @param joinPoint
	 * @return
	 */
	private Map<String, Object> getNameAndValue(JoinPoint joinPoint) {
		final Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		final String[] names = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		args = Arrays.stream(args)
				.map(arg -> {
					if (!(arg instanceof Serializable)) {
						log.debug("{} 方法参数 {} 不可序列化，尝试用 toString() 替代", getClassMethod(joinPoint), arg);
						try {
							return arg.toString(); // 使用toString()代替
						} catch (Exception e) {
							log.warn("{} 方法参数 {} 无法用 toString() 替代", getClassMethod(joinPoint), arg);
							return null;
						}
					} else if (arg instanceof MultipartFile file) {
						// File保存在内存中，处理完毕后会引起jackson找不到文件的异常，所以需要特殊处理
						return file.getOriginalFilename();
					} else if (arg instanceof String s && s.length() > aopMaxLength) {
						// 字符串太长了，截取一部分
						return s.substring(0, aopMaxLength) + "(..." + (s.length() - aopMaxLength) + " more)";
					} else {
						return arg;
					}
				}).toArray();
		if (ArrayUtils.isEmpty(names) || ArrayUtils.isEmpty(args)) {
			return Collections.emptyMap();
		}
		if (names.length != args.length) {
			log.warn("{}方法参数名和参数值数量不一致", getClassMethod(joinPoint));
			return Collections.emptyMap();
		}
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < names.length; i++) {
			map.put(names[i], args[i]);
		}
		return map;
	}

	/**
	 * 获取当前类方法
	 *
	 * @param point
	 * @return
	 */
	private String getClassMethod(JoinPoint point) {
		return String.format("%s.%s", point.getSignature().getDeclaringTypeName(),
				point.getSignature().getName());
	}

	public AopLogger buildAopLog(HttpServletRequest request, long startTime, JoinPoint point, Object result) {
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		Signature signature = point.getSignature();
		Map<String, Object> nameAndValue = getNameAndValue(point);

		aopLog = AopLog.builder()
				.threadId(Long.toString(Thread.currentThread().threadId()))
				.threadName(Thread.currentThread().getName())
				.ip(UserAgentUtil.getIp(request))
				.url(request.getRequestURL().toString())
				.classMethod(String.format("%s.%s",
						signature.getDeclaringTypeName(),
						signature.getName()))
				.httpMethod(request.getMethod())
				.requestParams(nameAndValue)
				.timeCost(System.currentTimeMillis() - startTime)
				.userAgent(header)
				.browser(userAgent.getBrowser().toString())
				.operatingSystem(userAgent.getOperatingSystem().toString())
				.result(result).build();
		return this;
	}

	public AopLogger makeSerializable() {
		try {
			String resultString = objectMapper.writeValueAsString(aopLog.getResult());
			String requestParamsString;
			if (aopLog.getRequestParams() instanceof Exception e) {
				requestParamsString = e.getMessage();
			} else {
				requestParamsString = objectMapper.writeValueAsString(aopLog.getRequestParams());
			}
			if (resultString.length() > aopMaxLength) {
				aopLog.setResult(resultString.substring(0, aopMaxLength) +
						"(... %s more)".formatted(resultString.length() - aopMaxLength));
			} else {
				aopLog.setResult(resultString);
			}
			if (requestParamsString.length() > aopMaxLength) {
				aopLog.setRequestParams(requestParamsString.substring(0, aopMaxLength) +
						"(... %s more)".formatted(requestParamsString.length() - aopMaxLength));
			} else {
				aopLog.setRequestParams(requestParamsString);
			}
		} catch (JsonProcessingException e) {
			fail(e);
		}
		return this;
	}

	public AopLogger info() {
		try {
			log.info("请求日志: {}", aopLog);
		} catch (Exception e) {
			fail(e);
		}
		return this;
	}

	public AopLogger warn(Exception e) {
		try {
			log.warn("报错日志: {}", aopLog, e);
		} catch (Exception e1) {
			fail(e1);
		}
		return this;
	}

	public AopLogger warn() {
		warn(null);
		return this;
	}

	private void fail(Exception e) {
		log.warn("AopLogger记录失败，下述错误属于AopLogger，并非服务报错", e);
	}

}
