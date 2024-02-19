package com.luckytour.server.service.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.model.SMSPayload;
import com.luckytour.server.config.JiguangConfig;
import com.luckytour.server.service.PushService;
import com.luckytour.server.vo.JiguangNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qing
 * @date Created in 2024/1/12 15:10
 */
@Service("jiguangPushService")
@Slf4j
public class JiguangPushServiceImpl implements PushService {

	private final JiguangConfig jPushConfig;

	@Autowired
	public JiguangPushServiceImpl(JiguangConfig jPushConfig) {
		this.jPushConfig = jPushConfig;
	}

	public boolean sendVerificationCode(String phone, String code) {
		Map<String, String> data = new HashMap<>();
		data.put("code", code);
		if (sendTemplateSMS(phone, 1, data)) {
			log.info("发送验证码成功:{}", phone);
			return true;
		} else {
			log.error("发送验证码失败:{}", phone);
			return false;
		}

	}

	/**
	 * 发送一条模板短信
	 *
	 * @param phone
	 * @param templateId
	 * @param data
	 * @return
	 */
	private boolean sendTemplateSMS(String phone, int templateId, Map<String, String> data) {
		SMSPayload payload = SMSPayload.newBuilder()
				.setMobileNumber(phone)
				.setTempId(templateId)
				.setTempPara(data)
				.build();
		try {
			SendSMSResult res = jPushConfig.getSmsClient().sendTemplateSMS(payload);
			return res.isResultOK();
		} catch (Exception e) {
			fail(e);
			return false;
		}
	}

	/**
	 * 推送全部android
	 *
	 * @return
	 */
	public boolean sendPushToAndroid(JiguangNotification notification) {
		try {
			return jPushConfig.getJPushClient().sendPush(PushPayload.newBuilder()
							.setPlatform(Platform.android())
							.setAudience(Audience.all())
							.setNotification(Notification.android(notification.getContent(), notification.getTitle(), notification.getExtras()))
							.build())
					.isResultOK();
		} catch (APIConnectionException | APIRequestException e) {
			fail(e);
		}
		return false;
	}

	/**
	 * 推送全部ios
	 *
	 * @return
	 */
	public boolean sendPushToIos(JiguangNotification notification) {
		try {
			return jPushConfig.getJPushClient().sendPush(PushPayload.newBuilder()
							.setPlatform(Platform.ios())
							.setAudience(Audience.all())
							.setNotification(Notification.android(notification.getContent(), notification.getTitle(), notification.getExtras()))
							.build())
					.isResultOK();
		} catch (APIConnectionException | APIRequestException e) {
			fail(e);
		}
		return false;
	}

	/**
	 * 推送全部用户
	 *
	 * @return
	 */
	public boolean sendPushToAll(JiguangNotification notification) {
		try {
			return jPushConfig.getJPushClient().sendPush(PushPayload.newBuilder()
							.setPlatform(Platform.android_ios())
							.setAudience(Audience.all())
							.setNotification(Notification.android(notification.getContent(), notification.getTitle(), notification.getExtras()))
							.build())
					.isResultOK();
		} catch (APIConnectionException | APIRequestException e) {
			fail(e);
		}
		return false;
	}

	/**
	 * 设置、更新、设备的 tag, alias 信息。
	 *
	 * @param registrationId 设备的registrationId
	 * @param alias          更新设备的别名属性
	 * @param tagsToAdd      添加设备的tag属性
	 * @param tagsToRemove   移除设备的tag属性
	 * @return
	 */
	public boolean updateDeviceTagAlias(String registrationId, String alias, Set<String> tagsToAdd, Set<String> tagsToRemove) {
		try {
			jPushConfig.getJPushClient().updateDeviceTagAlias(registrationId, alias, tagsToAdd, tagsToRemove);
		} catch (APIConnectionException | APIRequestException e) {
			fail(e);
		}
		return false;
	}

	/**
	 * 根据别名推送消息
	 */
	public boolean sendPushByAlias(JiguangNotification notification, String... alias) {
		notification.setExtras(Optional.ofNullable(notification.getExtras()).orElseGet(HashMap::new));
		String[] newAlias = removeArrayEmptyElement(alias);
		try {
			return jPushConfig.getJPushClient().sendPush(PushPayload.newBuilder()
							.setPlatform(Platform.android_ios())
							.setAudience(ArrayUtils.isEmpty(newAlias) ? Audience.all() : Audience.alias(newAlias))
							.setNotification(Notification.newBuilder().setAlert(notification.getContent())
									.addPlatformNotification(AndroidNotification.newBuilder().setTitle(notification.getTitle()).addExtras(notification.getExtras()).build()).build())
							.build())
					.isResultOK();
		} catch (APIConnectionException | APIRequestException e) {
			fail(e);
		}
		return false;
	}

	/**
	 * 根据标签推送消息
	 */
	public boolean sendPushByTags(JiguangNotification notification, String... tags) {
		notification.setExtras(Optional.ofNullable(notification.getExtras()).orElseGet(HashMap::new));
		//批量删除数组中的空元素
		String[] newTags = removeArrayEmptyElement(tags);
		try {
			return jPushConfig.getJPushClient().sendPush(PushPayload.newBuilder()
							.setPlatform(Platform.android_ios())
							//设置标签
							.setAudience(ArrayUtils.isEmpty(newTags) ? Audience.all() : Audience.tag(newTags))
							//设置 推送的标签标题
							// 设置通知方式(以alert方式提醒)
							.setNotification(Notification.newBuilder().setAlert(notification.getContent()).addPlatformNotification(AndroidNotification.newBuilder().setTitle(notification.getTitle()).addExtras(notification.getExtras()).build()).build())
							//sendno int 可选 推送序号 纯粹用来作为 API 调用标识
							//离线消息保留时长 推送当前用户不在线时，为该用户保留多长时间的离线消息(默认 86400 （1 天），最长 10 天。设置为 0 表示不保留离线消息，只有推送当前在线的用户可以收到)
							//apns_production boolean 可选 APNs是否生产环境  True 表示推送生产环境，False 表示要推送开发环境； 如果不指定则为推送生产环境
							//big_push_duration int 可选 定速推送时长（分钟） 又名缓慢推送，把原本尽可能快的推送速度，降低下来，在给定的 n 分钟内，均匀地向这次推送的目标用户推送。最大值为 1440。未设置则不是定速推送
							//.setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(8600).setBigPushDuration(1).build())
							//设置通知内容
							//.setMessage(Message.newBuilder().setTitle("").setMsgContent("").setContentType("").build())
							.build())
					.isResultOK();
		} catch (APIConnectionException | APIRequestException e) {
			fail(e);
		}
		return false;
	}

	/**
	 * 根据RegistrationID推送消息
	 */
	public boolean sendPushByRegistrationID(JiguangNotification notification, String... registrationID) {
		notification.setExtras(Optional.ofNullable(notification.getExtras()).orElseGet(HashMap::new));
		//批量删除数组中的空元素
		String[] rids = removeArrayEmptyElement(registrationID);
		try {
			return jPushConfig.getJPushClient()
					.sendAndroidNotificationWithRegistrationID(notification.getTitle(), notification.getContent(), notification.getExtras(), rids)
					.isResultOK();
		} catch (APIConnectionException | APIRequestException e) {
			fail(e);
		}
		return false;
	}

	/**
	 * 删除别名中的空元素（需删除如：null,""," "）
	 *
	 * @param strArray
	 * @return String[]
	 */
	private String[] removeArrayEmptyElement(String... strArray) {
		if (null == strArray || strArray.length == 0) {
			return null;
		}
		List<String> strList = new ArrayList<>();
		for (String str : strArray) {
			// 消除空格后再做比较
			if (null != str && !str.trim().isEmpty()) {
				strList.add(str);
			}
		}
		// 若仅输入"",则会将数组长度置为0
		return strList.toArray(new String[0]);
	}

	private void fail(Exception e) {
		log.error("极光推送失败:{}", e.getMessage(), e);
	}
}
