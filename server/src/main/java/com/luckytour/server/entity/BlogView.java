package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@Getter
@Setter
@TableName("blog_view")
@Schema(name = "BlogView", description = "VIEW")
public class BlogView implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "博客id")
    @TableField("bid")
    private Integer bid;

    @Schema(description = "用户id，varchar(50)")
    @TableField("uid")
    private String uid;

    @Schema(description = "博客标题，nvarchar(255)")
    @TableField("title")
    private String title;

    @Schema(description = "博客具体内容")
    @TableField("content")
    private String content;

    @Schema(description = "创建时间，自动填充")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "计划id，timestamp")
    @TableField("pid")
    private LocalDateTime pid;

    @Schema(description = "点击量")
    @TableField("click_volume")
    private Long clickVolume;

    @TableField("like_count")
    private Long likeCount;

    @TableField("favorite_count")
    private Long favoriteCount;

}
