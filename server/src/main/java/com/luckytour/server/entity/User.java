package com.luckytour.server.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author qing
 * @date Created in 2023/8/4 10:23
 */
@Data
@Builder
public class User {

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

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

	/**
	 * 状态，启用-1，禁用-0
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 更新时间
	 */
	private Long updateTime;
}
