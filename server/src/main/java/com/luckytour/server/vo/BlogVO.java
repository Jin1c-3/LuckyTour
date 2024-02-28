package com.luckytour.server.vo;

import com.luckytour.server.entity.BlogView;
import com.luckytour.server.pojo.BlogContent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author qing
 * @date Created in 2024/2/27 21:02
 */
@Getter
@Setter
@Schema(name = "BlogVO", description = "博客视图对象")
public class BlogVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String uid;

	@Schema(description = "用户头像，varchar(255)")
	private String avatar;

	private String pid;

	@Schema(description = "博客标题，nvarchar(255)")
	private String title;

	@Schema(description = "随机抽取一张图片")
	private String view;

	private List<BlogContent> content;

	public static BlogVO create(BlogView blogView) {
		BlogVO blogVO = new BlogVO();
		blogVO.setUid(blogView.getUid());
		blogVO.setPid(String.valueOf(blogView.getPid()));
		blogVO.setTitle(blogView.getTitle());
		return blogVO;
	}
}
