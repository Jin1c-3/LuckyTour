package com.luckytour.server.config;

import cn.jiguang.common.ClientConfig;
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

	@Value("${jpush.app-key}")
	private String appKey;

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
		ClientConfig config = ClientConfig.getInstance();
		config.setGlobalPushSetting(apnsProduction, liveTime);
		jPushClient = new JPushClient(masterSecret, appKey, null, config);
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
