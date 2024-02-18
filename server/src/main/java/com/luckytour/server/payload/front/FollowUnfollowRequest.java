package com.luckytour.server.payload.front;

import com.luckytour.server.common.constant.Alert;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qing
 * @date Created in 2024/2/10 11:15
 */
@Getter
@Setter
@Schema(name = "FollowUnfollowRequest", description = "关注/取消关注请求")
public class FollowUnfollowRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "关注发起者的uid")
	@NotBlank(message = Alert.USER_ID_IS_NULL)
	private String followerUid;

	@Schema(description = "被关注者的uid")
	@NotBlank(message = Alert.USER_ID_IS_NULL)
	private String followedUid;

}
