package com.luckytour.server.util;


import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.payload.PageCondition;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.data.domain.PageRequest;

/**
 * <p>
 * 分页工具类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-12 18:09
 */
public class PageUtil {
	/**
	 * 校验分页参数，为NULL，设置分页参数默认值
	 *
	 * @param condition 查询参数
	 * @param clazz     类
	 * @param <T>       {@link PageCondition}
	 */
	public static <T extends PageCondition> void checkPageCondition(T condition, Class<T> clazz) {
		if (ObjectUtils.isEmpty(condition)) {
			condition = (T) ReflectUtils.newInstance(clazz);
		}
		// 校验分页参数
		if (ObjectUtils.isEmpty(condition.getCurrentPage())) {
			condition.setCurrentPage(ConstsPool.PAGE_DEFAULT_CURRENT);
		}
		if (ObjectUtils.isEmpty(condition.getPageSize())) {
			condition.setPageSize(ConstsPool.PAGE_DEFAULT_SIZE);
		}
	}

	/**
	 * 根据分页参数构建{@link PageRequest}
	 *
	 * @param condition 查询参数
	 * @param <T>       {@link PageCondition}
	 * @return {@link PageRequest}
	 */
	public static <T extends PageCondition> PageRequest ofPageRequest(T condition) {
		return PageRequest.of(condition.getCurrentPage(), condition.getPageSize());
	}
}
