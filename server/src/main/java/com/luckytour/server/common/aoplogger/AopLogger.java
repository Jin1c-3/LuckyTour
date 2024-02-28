package com.luckytour.server.common.aoplogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.util.UserAgentUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
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
import java.util.*;
import java.util.stream.Collectors;

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
		private Map<String, Serializable> requestParams;
		// ip
		private String ip;
		// url
		private String url;
		// 接口耗时
		private Long timeCost;
		// 返回参数
		private Serializable result;
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
	private Map<String, Object> getParamNameAndValue(JoinPoint joinPoint) {
		final Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		final String[] names = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		/*args = Arrays.stream(args)
				.map(arg -> {
					try {
						if (arg == null) return null;
						return switch (arg) {
							case Exception e -> e.getMessage();
							case RequestFacade rf -> objectMapper.writeValueAsString(rf.getParameterMap());
							case MultipartFile mf -> mf.getOriginalFilename();
							case HttpServletRequest hr -> objectMapper.writeValueAsString(hr.getParameterMap());
							default -> objectMapper.writeValueAsString(arg);
						};
					} catch (JsonProcessingException e) {
						return arg;
					}
				}).toArray();*/
		if (ArrayUtils.isEmpty(names) || ArrayUtils.isEmpty(args)) {
			return Collections.emptyMap();
		}
		if (names.length != args.length) {
			log.warn("{} 方法参数名和参数值数量不一致", getClassMethod(joinPoint));
			return Collections.emptyMap();
		}
		return Arrays.stream(names).collect(HashMap::new, (m, v) -> m.put(v, args[m.size()]), HashMap::putAll);
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

		aopLog = AopLog.builder()
				.threadId(Long.toString(Thread.currentThread().threadId()))
				.threadName(Thread.currentThread().getName())
				.ip(UserAgentUtil.getIp(request))
				.url(request.getRequestURL().toString())
				.classMethod(String.format("%s.%s",
						signature.getDeclaringTypeName(),
						signature.getName()))
				.httpMethod(request.getMethod())
				.requestParams(getParamNameAndValue(point).entrySet().stream()
						.collect(Collectors.toMap(
								Map.Entry::getKey,
								entry -> truncate(serialize(entry.getValue()))
						)))
				.timeCost(System.currentTimeMillis() - startTime)
				.userAgent(header)
				.browser(userAgent.getBrowser().toString())
				.operatingSystem(userAgent.getOperatingSystem().toString())
				.result(truncate(serialize(result)))
				.build();
		return this;
	}

	private String truncate(String str) {
		if (str == null) return "null";
		if (str.length() <= aopMaxLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		sb.setLength(aopMaxLength);
		sb.append(String.format("...(%d more)", str.length() - aopMaxLength));
		return sb.toString();
	}

	private String serialize(Object obj) {
		try {
			return switch (obj) {
				case Exception exception -> exception.getMessage();
				case RequestFacade requestFacade -> objectMapper.writeValueAsString(requestFacade.getParameterMap());
				case MultipartFile multipartFile ->
						Optional.ofNullable(multipartFile.getOriginalFilename()).orElse(multipartFile.getName());
				case HttpServletRequest httpServletRequest ->
						objectMapper.writeValueAsString(httpServletRequest.getHeader(ConstsPool.TOKEN_KEY));
				case null, default -> objectMapper.writeValueAsString(obj);
			};
		} catch (JsonProcessingException e) {
			return Optional.ofNullable(obj).map(Object::toString).orElse("null");
		}
	}

	public void info() {
		try {
			log.info("请求日志: {}", aopLog);
		} catch (Exception e) {
			fail(e);
		}
	}

	public void warn(Exception e) {
		try {
			log.warn("报错日志: {}", aopLog, e);
		} catch (Exception e1) {
			fail(e1);
		}
	}

	public void warn() {
		warn(null);
	}

	private void fail(Exception e) {
		log.warn("AopLogger记录失败，下述错误属于AopLogger，并非服务报错", e);
	}

}
