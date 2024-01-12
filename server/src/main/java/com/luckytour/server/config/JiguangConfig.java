package com.luckytour.server.config;

import cn.jpush.api.JPushClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author qing
 * @date Created in 2024/1/11 20:10
 */
@Configuration
public class JiguangConfig {

	@Value("${jpush.appkey}")
	private String appkey;

	@Value("${jpush.master-secret}")
	private String masterSecret;

	@Value("${jpush.live-time}")
	private Long liveTime;

	@Value("${jpush.apns-production}")
	private Boolean apnsProduction;

	private JPushClient jPushClient;

	/**
	 * 推送客户端
	 *
	 * @return
	 */
	@PostConstruct
	public void initJPushClient() {
		jPushClient = new JPushClient(masterSecret, appkey, apnsProduction, liveTime);
	}

	/**
	 * 获取推送客户端
	 *
	 * @return
	 */
	public JPushClient getJPushClient() {
		return jPushClient;
	}
}
