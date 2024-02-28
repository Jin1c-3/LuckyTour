package com.luckytour.server.vo;

import com.luckytour.server.entity.User;
import com.luckytour.server.util.URLUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

/**
 * @author qing
 * @date Created in 2024/2/12 17:42
 */
@Getter
@Setter
@Builder
@Schema(name = "SimpleUser", description = "简单的用户信息，只有id、昵称、头像")
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserVO {

	@Schema(description = "用户id，使用uuid标识")
	private String id;

	@Schema(description = "用户昵称，默认值是“匿名用户”，允许汉字，允许相同姓名，长度3~16", defaultValue = "匿名用户")
	private String nickname;

	@Schema(description = "用户头像，存储一个网址")
	private String avatar;

	public static SimpleUserVO create(HttpServletRequest request, User user) {
		SimpleUserVO simpleUserVO = SimpleUserVO.builder()
				.id(user.getId())
				.nickname(user.getNickname())
				.build();
		if (URLUtil.isNotUrl(user.getAvatar())) {
			simpleUserVO.setAvatar(URLUtil.create(request, user.getAvatar()));
		}
		return simpleUserVO;
	}

}
