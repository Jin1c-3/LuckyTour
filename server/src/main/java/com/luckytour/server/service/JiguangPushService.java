package com.luckytour.server.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import com.luckytour.server.vo.JiguangNotification;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author qing
 * @date Created in 2024/1/11 20:31
 */
public interface JiguangPushService {

	/**
	 * 推送全部android
	 *
	 * @return
	 */
	public void sendPushToAndroid(JiguangNotification notification) throws APIConnectionException, APIRequestException;

	/**
	 * 推送全部ios
	 *
	 * @return
	 */
	public void sendPushToIos(JiguangNotification notification) throws APIConnectionException, APIRequestException;

	/**
	 * 推送全部用户
	 *
	 * @return
	 */
	public void sendPushToAll(JiguangNotification notification) throws APIConnectionException, APIRequestException;

	/**
	 * 设置、更新、设备的 tag, alias 信息。
	 *
	 * @param registrationId 设备的registrationId
	 * @param alias          更新设备的别名属性
	 * @param tagsToAdd      添加设备的tag属性
	 * @param tagsToRemove   移除设备的tag属性
	 */
	public void updateDeviceTagAlias(String registrationId, String alias, Set<String> tagsToAdd, Set<String> tagsToRemove) throws APIConnectionException, APIRequestException;


	/**
	 * 根据别名推送消息
	 */
	public PushResult sendPushByAlias(JiguangNotification notification, String... alias) throws APIConnectionException, APIRequestException;

	/**
	 * 根据标签推送消息
	 */
	public PushResult sendPushByTags(JiguangNotification notification, String... tags) throws APIConnectionException, APIRequestException;

	/**
	 * 根据RegistrationID推送消息
	 */
	public PushResult sendPushByRegistrationID(JiguangNotification notification, String... registrationID) throws APIConnectionException, APIRequestException;

}
