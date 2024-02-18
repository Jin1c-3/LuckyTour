package com.luckytour.server.config;

import com.luckytour.server.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/10 21:06
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${storage.static.path}")
	private String staticPath;

	private static final String[] EXCLUDED_PATH_PATTERNS = {
			"/**/doc.html",
			"/**/login",
			"/**/login.html",
			"/**/registry",
			"/**/registry.html",
			"/**/*.js",
			"/**/*.css",
			"/**/*.woff",
			"/**/*.ttf",
			"/**/*.jpg",
			"/**/*.png",
			"/css/**",
			"/js/**",
			"/img/**",
			"/media/**",
			"/vendors/**",
			"/**/avatar/**",
			"/**/download/**",
			"/test/**",
			"/swagger-ui.html/**",
			"/swagger-ui.html#/**",
			"/swagger-ui/**",
			"/swagger-resources/**",
			"/**/webjars/**",
			"/**/v3/**",
			"/**/callback/**",
			"/user/getcode/**",
			"/user/new/**",
			"/auth/**",
			"/plan/check/**",
			"/plan/saveOrUpdate/**",
			"/data/**"
	};

	@Bean
	public TokenInterceptor getTokenInterceptor() {
		return new TokenInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getTokenInterceptor())
				// 排除不需要拦截的路径
				.excludePathPatterns(EXCLUDED_PATH_PATTERNS)
				// 需要拦截的路径
				.addPathPatterns("/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowCredentials(true)
				.allowedMethods("*")
				.maxAge(60 * 60 * 24);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/%s/**".formatted(staticPath)).addResourceLocations("classpath:/%s/".formatted(staticPath), "file:%s/".formatted(staticPath));
	}

	/**
	 * 解决异步请求超时问题
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(50000); // 设置超时时间为50秒
	}
}
