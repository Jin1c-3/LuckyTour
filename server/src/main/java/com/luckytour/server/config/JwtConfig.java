package com.luckytour.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qing
 * @date Created in 2024/2/10 21:15
 */
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@Configuration
public class JwtConfig {

	private String secretKey;

	private Long shortTtl;

	private Long longTtl;
}
