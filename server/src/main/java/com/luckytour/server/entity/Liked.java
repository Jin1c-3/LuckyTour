package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.luckytour.server.payload.front.BlogLikeRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 点赞表
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
@TableName("liked")
@Schema(name = "Liked", description = "点赞表")
public class Liked implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "被点赞的博客id")
	@TableField("bid")
	@MppMultiId
	private Integer bid;

	@Schema(description = "点赞发起者的用户id")
	@TableField("uid")
	@MppMultiId
	private String uid;

	@Schema(description = "点赞的时间")
	@TableField("create_time")
	private LocalDateTime createTime;

	@Schema(description = "0表示喜欢过，1表示正在喜欢")
	@TableField("status")
	private Byte status;

	public Liked create(BlogLikeRequest lbRequest) {
		return Liked.builder().bid(Integer.parseInt(lbRequest.getBid())).uid(lbRequest.getUid()).build();
	}
}
