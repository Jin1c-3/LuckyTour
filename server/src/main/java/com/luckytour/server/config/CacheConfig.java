package com.luckytour.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * 缓存配置
 *
 * @author qing
 * @date Created in 2024/2/18 12:53
 */
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {
	private final RedisConnectionFactory redisConnectionFactory;

	@Value("${cache.ttl}")
	private long ttl;

	@Autowired
	public CacheConfig(RedisConnectionFactory redisConnectionFactory) {
		this.redisConnectionFactory = redisConnectionFactory;
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(ttl)) // 设置缓存有效期半个小时
				.disableCachingNullValues(); // 不缓存空值

		return RedisCacheManager.builder(redisConnectionFactory)
				.cacheDefaults(cacheConfiguration)
				.transactionAware()
				.build();
	}
}