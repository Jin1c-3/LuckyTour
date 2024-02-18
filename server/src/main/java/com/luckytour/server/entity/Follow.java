package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.luckytour.server.payload.front.FollowUnfollowRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * <p>
 * 关注表
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
@TableName("follow")
@Schema(name = "Follow", description = "关注表")
public class Follow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "关注发起者的uid")
    @TableField("follower_uid")
    @MppMultiId
    private String followerUid;

    @Schema(description = "被关注者的uid")
    @TableField("followed_uid")
    @MppMultiId
    private String followedUid;

    @Schema(description = "0表示关注过，1表示正在关注")
    @TableField("status")
    private Byte status;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    public Follow create(FollowUnfollowRequest followUnfollowRequest){
        this.followerUid=followUnfollowRequest.getFollowerUid();
        this.followedUid=followUnfollowRequest.getFollowedUid();
        return this;
    }
}
