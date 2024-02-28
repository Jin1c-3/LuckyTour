package com.luckytour.server.payload.front;

import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.pojo.BlogContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author qing
 * @date Created in 2024/2/10 10:48
 */
@Getter
@Setter
@Schema(name = "BlogCreateRequest", description = "博客创建请求")
public class BlogCreateRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/*@Schema(description = "用户id，varchar(50)")
	@NotBlank(message = Alert.USER_ID_IS_NULL)
	private String uid;*/

	@Schema(description = "计划id，timestamp")
	@NotBlank(message = Alert.PLAN_ID_IS_NULL)
	private String pid;

	@Schema(description = "博客标题，nvarchar(255)")
	@Pattern(regexp = Regex.BLOG_TITLE_REGEX, message = Alert.BLOG_TITLE_FORMAT_ERROR)
	private String title;

	@Schema(description = "博客具体内容")
	@NotEmpty(message = Alert.BLOG_CONTENT_IS_NULL)
	private List<BlogContent> content;

}
