package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.luckytour.server.payload.front.PlanCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qing
 * @since 2024-02-06
 */
@Getter
@Setter
@TableName("plan")
@Schema(name = "Plan", description = "计划实体类，双主键")
public class Plan implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "user表中的id列")
	@TableField("uid")
	@MppMultiId
	private String uid;

	@Schema(description = "计划创建的时间戳，双主键之一")
	@TableField("pid")
	@MppMultiId
	private LocalDateTime pid;

	@Schema(description = "json化的plan字符串")
	@TableField("content")
	private String content;

	@Schema(description = "计划标题，nvarchar(255)")
	@TableField("title")
	private String title;

	@Schema(description = "标签数组")
	@TableField("tags")
	private String tags;

	@Schema(description = "计划所处城市，nvarchar(10)")
	@TableField("city")
	private String city;

	public static Plan create(PlanCreateRequest planCreateRequest) {
		Plan plan = new Plan();
		BeanUtils.copyProperties(planCreateRequest, plan);
		if (planCreateRequest.getTags() != null) {
			plan.setTags(String.join(",", planCreateRequest.getTags()));
		}
		return plan;
	}
}
