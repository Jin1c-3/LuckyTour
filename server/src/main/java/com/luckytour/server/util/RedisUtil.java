package com.luckytour.server.util;

import com.luckytour.server.config.RedisConfig;
import com.luckytour.server.exception.RedisOpsResultIsNullException;
import com.luckytour.server.exception.SecurityException;
import com.luckytour.server.exception.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 *
 * @author qing
 * @date Created in 2023/8/2 11:54
 */
@Component
@Slf4j
@Import(RedisConfig.class)
public class RedisUtil implements ApplicationContextAware {

	private static StringRedisTemplate stringRedisTemplate;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		RedisUtil.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
	}

	/**
	 * 获取 key 对应的 value
	 *
	 * @param key key
	 * @return value
	 */
	public static String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * 设置 key-value
	 *
	 * @param key   key
	 * @param value value
	 */
	public static void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 删除 Redis 中的某个或某些 key
	 *
	 * @param key key
	 */
	public static boolean del(String key) {
		Boolean delete = stringRedisTemplate.delete(key);
		if (delete == null) {
			throw new RedisOpsResultIsNullException();
		}
		return delete;
	}

	/**
	 * 删除 Redis 中 key 的集合
	 *
	 * @param keys key 集合
	 */
	public static long del(Collection<String> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			throw new RedisException("键集为空", keys);
		}
		Long count = stringRedisTemplate.delete(keys);
		if (count == null) {
			throw new RedisOpsResultIsNullException();
		}
		if (count != keys.size()) {
			log.warn("部分删除失败，预计删除{}个，实际删除{}个", keys.size(), count);
		}
		return count;
	}

	/**
	 * 给指定的key对应的key-value设置: 多久过时
	 * <p>
	 * 注:过时后，redis会自动删除对应的key-value。
	 * 注:若key不存在，那么也会返回false。
	 *
	 * @param key     指定的key
	 * @param timeout 过时时间
	 * @param unit    timeout的单位
	 * @return 操作是否成功
	 */
	public static boolean expire(String key, long timeout, TimeUnit unit) {
		if (!hasKey(key)) {
			throw new RedisException("键不存在", key);
		}
		Boolean result = stringRedisTemplate.expire(key, timeout, unit);
		if (result == null) {
			throw new RedisOpsResultIsNullException();
		}
		return result;
	}

	/**
	 * 给指定的key对应的key-value设置: 什么时候过时
	 * <p>
	 * 注:过时后，redis会自动删除对应的key-value。
	 * 注:若key不存在，那么也会返回false。
	 *
	 * @param key  指定的key
	 * @param date 啥时候过时
	 * @return 操作是否成功
	 */
	public static boolean expireAt(String key, Date date) {
		if (!hasKey(key)) {
			throw new RedisException("键不存在", key);
		}
		Boolean result = stringRedisTemplate.expireAt(key, date);
		if (result == null) {
			throw new RedisOpsResultIsNullException();
		}
		return result;
	}

	/**
	 * 判断指定的key是否存在
	 *
	 * @param key 指定的key
	 * @return 是否存在
	 */
	public static boolean hasKey(String key) {
		Boolean result = stringRedisTemplate.hasKey(key);
		if (result == null) {
			throw new RedisOpsResultIsNullException();
		}
		return result;
	}

	/**
	 * 移除key对应的key-value的过期时间, 使该key-value一直存在
	 * <p>
	 * 注: 若key对应的key-value，本身就是一直存在(无过期时间的)， 那么persist方法会返回false;
	 * 若没有key对应的key-value存在，本那么persist方法会返回false;
	 *
	 * @param key 定位key-value的key
	 * @return 操作是否成功
	 */
	public static boolean persist(String key) {
		if (!hasKey(key)) {
			throw new RedisException("键不存在", key);
		}
		Boolean result = stringRedisTemplate.persist(key);
		if (result == null) {
			throw new RedisOpsResultIsNullException();
		}
		return result;
	}

}
