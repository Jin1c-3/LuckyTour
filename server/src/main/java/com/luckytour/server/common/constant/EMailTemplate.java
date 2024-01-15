package com.luckytour.server.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qing
 * @date Created in 2024/1/13 20:21
 */
@Getter
@AllArgsConstructor
public enum EMailTemplate {
	
	VERIFICATION_CODE("[云栖科技]验证码", "您的验证码为：%s，5分钟内有效，请勿将验证码告诉他人。"),

	GREETINGS("[云栖科技]问候", "您好，%s，感谢信任云栖科技！祝您使用愉快！");

	private final String subject;

	private final String content;

	public String fillOneParam(String param) {
		return String.format(this.content, param);
	}
}
