package com.luckytour.server.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qing
 * @date Created in 2023/8/2 8:45
 */
@Data
@Schema(name = "LoginRequest", description = "登录请求")
public class LoginRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "邮箱或手机号", example = "test@qq.com")
	@NotBlank(message = "邮箱或手机号不能为空")
	private String emailOrPhone;

	@Schema(description = "密码", example = "123456")
	private String password;

	@Schema(description = "极光registrationID，用于推送")
	private String jrid;

	@Schema(description = "记住我，true代表记住更长时间，不传默认false")
	private Boolean rememberMe = false;
}
