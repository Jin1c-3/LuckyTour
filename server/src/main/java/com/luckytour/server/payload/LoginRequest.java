package com.luckytour.server.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qing
 * @date Created in 2023/8/2 8:45
 */
@Data
public class LoginRequest implements Serializable {

	/**
	 * 用户名或邮箱或手机号
	 */
	@NotBlank(message = "用户名不能为空")
	private String usernameOrEmailOrPhone;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空")
	private String password;

	/**
	 * 记住我
	 */
	private Boolean rememberMe = false;
}
