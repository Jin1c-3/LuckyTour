package com.luckytour.server.vo;

import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author qing
 * @date Created in 2023/8/4 20:22
 */
@Data
@Schema(name = "OnlineUser", description = "前端显示的用户信息")
@Slf4j
public class OnlineUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id，使用uuid标识")
	private String id;

	@Schema(description = "用户昵称，允许汉字，允许相同姓名，长度3~16")
	private String nickname;

	@Schema(description = "用户手机号码，中间几位会变成*")
	private String phone;

	@Schema(description = "用户邮箱，中间几位会变成*")
	private String email;

	@Schema(description = "用户生日,yyyy-MM-dd")
	private String birthday;

	@Schema(description = "用户性别，1-男、2-女")
	private String sex;

	@Schema(description = "用户头像，存储一个网址")
	private String avatar;

	@Schema(description = "用户vip级别")
	private Integer vip;

	@Schema(description = "用户等级")
	private Integer level;

	public static OnlineUser create(User user) {
		OnlineUser onlineUser = new OnlineUser();
		BeanUtils.copyProperties(user, onlineUser);
		if (Objects.nonNull(user.getSex())) {
			if (user.getSex().equals(Consts.MALE_INT)) {
				onlineUser.setSex("男");
			} else if (user.getSex().equals(Consts.FEMALE_INT)) {
				onlineUser.setSex("女");
			}
		}
		log.debug("onlineUser: {} user: {}", onlineUser, user);
		return onlineUser;
	}
}
