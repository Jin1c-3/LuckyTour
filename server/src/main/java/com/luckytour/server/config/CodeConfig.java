package com.luckytour.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qing
 * @date Created in 2024/2/13 16:12
 */
@ConfigurationProperties(prefix = "code")
@Getter
@Setter
@Configuration
public class CodeConfig {
	private UserId userId;
	private Verification verification;

	@Getter
	@Setter
	public static class UserId {
		private int maxLength;
		private int minLength;
	}

	@Getter
	@Setter
	public static class Verification {
		private int codeLength;
		private long expireTime;
	}
}
