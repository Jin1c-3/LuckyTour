package com.luckytour.server.payload;

import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.common.constant.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qing
 * @date Created in 2024/1/12 22:14
 */
@Data
@Schema(name = "UserUpdateRequest", description = "用户更新请求")
public class UserUpdateRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id，使用uuid标识")
	@NotBlank(message = "用户id不能为空")
	private String id;

	@Schema(description = "用户昵称，默认值是“匿名用户”，允许汉字，允许相同姓名，长度3~16", defaultValue = "匿名用户")
	@Pattern(regexp = Regex.NICKNAME_OR_BLANK_REGEX, message = "昵称格式不正确")
	private String nickname;

	@Schema(description = "登录密码，30位字母或数字长度，允许为空")
	@Pattern(regexp = Regex.PASSWORD_OR_BLANK_REGEX, message = "密码格式不正确")
	private String password;

	@Schema(description = "用户手机号码，可变长，最大30")
	@Pattern(regexp = Regex.MOBILE_OR_BLANK_REGEX, message = "手机号码格式不正确")
	private String phone;

	@Schema(description = "用户邮箱，最长50字节")
	@Pattern(regexp = Regex.EMAIL_OR_BLANK_REGEX, message = "邮箱格式不正确")
	private String email;

	@Schema(description = "用户生日，采用时间戳")
	private LocalDateTime birthday;

	@Schema(description = "用户性别，1-男、2-女")
	private Integer sex;
}
