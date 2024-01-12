package com.luckytour.server.vo;

import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.entity.User;
import com.luckytour.server.mapstruct.IUserMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author qing
 * @date Created in 2023/8/4 20:22
 */
@Data
@Schema(name = "OnlineUser", description = "前端显示的用户信息")
public class OnlineUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id，使用uuid标识")
	private Long id;

	@Schema(description = "用户昵称，允许汉字，允许相同姓名，长度6~16")
	private String nickname;

	@Schema(description = "用户手机号码，中间几位会变成*")
	private String phone;

	@Schema(description = "用户邮箱，中间几位会变成*")
	private String email;

	@Schema(description = "用户生日，采用时间戳")
	private Long birthday;

	@Schema(description = "用户性别，1-男、2-女")
	private Integer sex;

	@Schema(description = "用户头像，存储一个网址")
	private String avatar;

	@Schema(description = "用户vip级别")
	private Integer vip;

	@Schema(description = "用户等级")
	private Integer level;

	public static OnlineUser create(User user) {
		// 脱敏
		user.setPhone(StringUtils.overlay(user.getPhone(), Consts.SYMBOL_ASTERISK, 3, 7));
		user.setEmail(StringUtils.overlay(user.getEmail(), Consts.SYMBOL_ASTERISK, 1, StringUtils.indexOfIgnoreCase(user.getEmail(), Consts.SYMBOL_EMAIL)));
		return IUserMapper.INSTANCE.User2OnlineUser(user);
	}
}
