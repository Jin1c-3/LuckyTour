package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.payload.front.BlogCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog")
@Schema(name = "Blog", description = "博客表")
public class Blog implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "博客id")
	@TableId(value = "bid", type = IdType.AUTO)
	private Integer bid;

	@Schema(description = "用户id，varchar(50)")
	@TableField("uid")
	private String uid;

	@Schema(description = "计划id，timestamp")
	@TableField("pid")
	private LocalDateTime pid;

	@Schema(description = "博客标题，nvarchar(255)")
	@TableField("title")
	private String title;

	@Schema(description = "博客具体内容")
	@TableField("content")
	private String content;

	@Schema(description = "创建时间，自动填充")
	@TableField("create_time")
	private LocalDateTime createTime;

	@Schema(description = "点击量")
	@TableField("click_volume")
	private Long clickVolume;

	public static Blog create(BlogCreateRequest request) {
		return Blog.builder()
				.pid(LocalDateTime.parse(request.getPid(), ConstsPool.DATE_TIME_FORMATTER))
				.title(request.getTitle())
				.build();
	}
}
