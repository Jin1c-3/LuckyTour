package com.luckytour.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/1/11 20:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiguangNotification {
	/**
	 * android专用 可选, 通知标题
	 * 如果指定了，则通知里原来展示 App名称的地方，将展示成这个字段。
	 */
	private String title;
	/**
	 * 必填, 通知内容, 内容可以为空字符串，则表示不展示到通知栏。
	 */
	private String content;
	/**
	 * 可选, 附加信息, 供业务使用。
	 */
	private Map<String, String> extras;
}
