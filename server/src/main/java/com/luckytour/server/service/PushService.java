package com.luckytour.server.service;

import com.luckytour.server.vo.JiguangNotification;

import java.util.Set;

/**
 * @author qing
 * @date Created in 2024/1/11 20:31
 */
public interface PushService {

	/**
	 * 发送验证码
	 *
	 * @param phone
	 * @param code
	 * @return
	 */
	boolean sendVerificationCode(String phone, String code);

	/**
	 * 推送全部android
	 *
	 * @return
	 */
	boolean sendPushToAndroid(JiguangNotification notification);

	/**
	 * 推送全部ios
	 *
	 * @return
	 */
	boolean sendPushToIos(JiguangNotification notification);

	/**
	 * 推送全部用户
	 *
	 * @return
	 */
	boolean sendPushToAll(JiguangNotification notification);

	/**
	 * 设置、更新、设备的 tag, alias 信息。
	 *
	 * @param registrationId 设备的registrationId
	 * @param alias          更新设备的别名属性
	 * @param tagsToAdd      添加设备的tag属性
	 * @param tagsToRemove   移除设备的tag属性
	 */
	boolean updateDeviceTagAlias(String registrationId, String alias, Set<String> tagsToAdd, Set<String> tagsToRemove);


	/**
	 * 根据别名推送消息
	 */
	boolean sendPushByAlias(JiguangNotification notification, String... alias);

	/**
	 * 根据标签推送消息
	 */
	boolean sendPushByTags(JiguangNotification notification, String... tags);

	/**
	 * 根据RegistrationID推送消息
	 */
	boolean sendPushByRegistrationID(JiguangNotification notification, String... registrationID);

}
