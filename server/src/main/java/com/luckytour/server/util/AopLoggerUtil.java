package com.luckytour.server.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

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
public class AopLoggerUtil {

	/**
	 * 获取方法参数名和参数值
	 *
	 * @param joinPoint
	 * @return
	 */
	public static Map<String, Object> getNameAndValue(JoinPoint joinPoint) {
		final Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		final String[] names = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		args = Arrays.stream(args)
				.map(arg -> {
					if (!(arg instanceof Serializable)) {
						log.debug("{}方法参数{}不可序列化，已经用toString()替代", getClassMethod(joinPoint), arg);
						return arg.toString(); // 使用toString()代替
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
	public static String getClassMethod(JoinPoint point) {
		return String.format("%s.%s", point.getSignature().getDeclaringTypeName(),
				point.getSignature().getName());
	}

}
