package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author qing
 * @since 2024-01-16
 */
@Getter
@Setter
@TableName("plan")
@Schema(name = "Plan", description = "旅游计划表")
public class Plan extends Model<Plan> implements Serializable{

    private static final long serialVersionUID = 1L;

    @Schema(description = "user表中的id列")
    @MppMultiId
    @TableField("uid")
    @NotBlank(message = "uid不能为空")
    private String uid;

    @Schema(description = "计划创建的时间戳，双主键之一")
    @MppMultiId
    @TableField("pid")
    @NotBlank(message = "pid不能为空")
    private String pid;

    @Schema(description = "json化的plan字符串")
    @TableField("content")
    @NotBlank(message = "plan不能为空")
    private String content;
}
