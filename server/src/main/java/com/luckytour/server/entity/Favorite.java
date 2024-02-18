package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.luckytour.server.payload.front.BlogFavorRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * <p>
 * 收藏总表
 * </p>
 *
 * @author qing
 * @since 2024-02-10
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("favorite")
@Schema(name = "Favorite", description = "收藏总表")
public class Favorite implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "收藏发起者的用户id")
    @TableField("uid")
    @MppMultiId
    private String uid;

    @Schema(description = "被收藏博客id")
    @TableField("bid")
    @MppMultiId
    private Integer bid;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "0表示收藏过，1表示正在收藏")
    @TableField("status")
    private Byte status;

    public Favorite createByBlogFavorRequest(BlogFavorRequest fbRequest) {
        return Favorite.builder().uid(fbRequest.getUid()).bid(Integer.parseInt(fbRequest.getBid())).build();
    }
}
