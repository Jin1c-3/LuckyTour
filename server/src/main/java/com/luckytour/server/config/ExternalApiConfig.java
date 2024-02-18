package com.luckytour.server.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

/**
 * @author qing
 * @date Created in 2024/1/31 21:51
 */
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
@Configuration
public class ExternalApiConfig {

	private Gaode gaode;
	private Caiyun caiyun;
	private Flask flask;

	@Getter
	@Setter
	public static class Gaode {
		private String key;
		private String distance;
		private String geocode;
	}

	@Getter
	@Setter
	public static class Caiyun {
		private String key;
		private String dailyWeather;
		private String hourlyWeather;
		private Integer checkHours;
	}

	@Getter
	@Setter
	public static class Flask {
		private String chat;
		private String base;
		private PlanGenerator planGenerator;

		@Getter
		@Setter
		public static class PlanGenerator {
			private String url;
			private Integer counter;
			private String promptField;
			private Long waitTime;
		}
	}

	private HttpClient httpClient = HttpClient.create()
			.resolver(DefaultAddressResolverGroup.INSTANCE);

}
