package com.luckytour.server.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * <p>
 * Swagger 配置
 * </p>
 *
 * @author qing
 * @date Created in 2023/8/1 16:45
 */
@Configuration
@Profile({"dev", "test"})
public class SwaggerConfig {
	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("基础模板接口")
				.pathsToMatch("/**")
				.build();
	}
}
