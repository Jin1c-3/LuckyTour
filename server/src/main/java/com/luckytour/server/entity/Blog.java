package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.luckytour.server.payload.BlogCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
public class Blog implements Serializable{

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

    public Blog createByBlogCreateRequest(BlogCreateRequest request) {
        return Blog.builder()
                .uid(request.getUid())
                .pid(request.getPid())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

}
