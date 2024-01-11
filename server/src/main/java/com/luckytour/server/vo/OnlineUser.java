package com.luckytour.server.vo;

import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.entity.User;
import com.luckytour.server.mapstruct.IUserMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author qing
 * @date Created in 2023/8/4 20:22
 */
@Data
public class OnlineUser {

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 手机
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 生日
	 */
	private Long birthday;

	/**
	 * 性别，男-1，女-2
	 */
	private Integer sex;

	public static OnlineUser create(User user) {
		// 脱敏
		user.setPhone(StringUtils.overlay(user.getPhone(), Consts.SYMBOL_ASTERISK, 3, 7));
		user.setEmail(StringUtils.overlay(user.getEmail(), Consts.SYMBOL_ASTERISK, 1, StringUtils.indexOfIgnoreCase(user.getEmail(), Consts.SYMBOL_EMAIL)));
		return IUserMapper.INSTANCE.User2OnlineUser(user);
	}
}
