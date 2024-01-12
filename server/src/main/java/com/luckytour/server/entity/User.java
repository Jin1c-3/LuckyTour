package com.luckytour.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qing
 * @since 2024-01-12
 */
@TableName("user")
@Schema(name = "User", description = "数据库中的用户表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends Model<User> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id，使用uuid标识")
	@TableId("id")
	private String id;

	@Schema(description = "用户昵称，默认值是“匿名用户”，允许汉字，允许相同姓名，长度6~16",defaultValue = "匿名用户")
	@TableField("nickname")
	private String nickname;

	@Schema(description = "登录密码，30位字母或数字长度，允许为空")
	@TableField("password")
	private String password;

	@Schema(description = "用户手机号码，可变长，最大30")
	@TableField("phone")
	private String phone;

	@Schema(description = "用户邮箱，最长50字节")
	@TableField("email")
	private String email;

	@Schema(description = "用户生日，采用时间戳")
	@TableField("birthday")
	private LocalDateTime birthday;

	@Schema(description = "用户被首次创建的时间，创建时自动填充")
	@TableField("create_time")
	private LocalDateTime createTime;

	@Schema(description = "用户信息更新时间，更新时自动刷新")
	@TableField("update_time")
	private LocalDateTime updateTime;

	@Schema(description = "用户性别，1-男、2-女")
	@TableField("sex")
	private Integer sex;

	@Schema(description = "用户头像，存储一个网址")
	@TableField("avatar")
	private String avatar;

	@Schema(description = "用户vip级别")
	@TableField("vip")
	private Integer vip;

	@Schema(description = "用户等级")
	@TableField("level")
	private Integer level;

	@Schema(description = "极光registrationID，用于推送")
	@TableField("jiguang_registration_id")
	private String jiguangRegistrationId;
}
