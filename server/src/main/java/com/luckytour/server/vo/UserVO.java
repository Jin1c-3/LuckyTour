package com.luckytour.server.vo;

import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.entity.User;
import com.luckytour.server.util.URIUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author qing
 * @date Created in 2023/8/4 20:22
 */
@Data
@Schema(name = "OnlineUser", description = "前端显示的用户信息")
public class UserVO implements Serializable {

	@Serial
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

	public static UserVO create(HttpServletRequest request, User user) {
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		if (Objects.nonNull(user.getSex())) {
			if (user.getSex().equals(ConstsPool.MALE_INT)) {
				userVO.setSex("男");
			} else if (user.getSex().equals(ConstsPool.FEMALE_INT)) {
				userVO.setSex("女");
			}
		}
		userVO.setAvatar(URIUtil.parse(request, user.getAvatar()));
		return userVO;
	}
}
