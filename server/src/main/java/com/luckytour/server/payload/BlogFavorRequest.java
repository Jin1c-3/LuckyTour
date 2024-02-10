package com.luckytour.server.payload;

import com.luckytour.server.common.constant.Alert;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qing
 * @date Created in 2024/2/9 23:29
 */
@Schema(name = "BlogFavorRequest", description = "收藏博客请求")
@Getter
@Setter
public class BlogFavorRequest {

	@Schema(description = "博客id")
	@NotBlank(message = Alert.BLOG_ID_IS_NULL)
	private String uid;

	@Schema(description = "用户id")
	@NotBlank(message = Alert.USER_ID_IS_NULL)
	private String bid;
}
