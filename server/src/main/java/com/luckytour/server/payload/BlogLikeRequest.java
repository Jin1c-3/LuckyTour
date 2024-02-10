package com.luckytour.server.payload;

import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qing
 * @date Created in 2024/2/9 22:51
 */
@Getter
@Setter
@Schema(name = "BlogLikeRequest", description = "点赞博客请求")
public class BlogLikeRequest {

	@Schema(description = "博客id")
	@Pattern(regexp = Regex.POSITIVE_INTEGER_REGEX, message = Alert.BLOG_ID_ILLIGAL)
	private String bid;

	@Schema(description = "用户id")
	@NotBlank(message = Alert.USER_ID_IS_NULL)
	private String uid;
}
