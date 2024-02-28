package com.luckytour.server.common.http;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * <p>
 * 状态码封装
 * </p>
 *
 * @author qing
 * @date Created in 2023-07-12 16:11
 */
@Getter
@AllArgsConstructor
public enum ServerStatus implements IServerStatus {
	/**
	 * 操作成功
	 */
	SUCCESS(HttpStatus.OK.value(), "操作成功"),

	/**
	 * 登陆成功
	 */
	LOGIN_SUCCESS(200, "登录成功！"),

	/**
	 * 注册成功
	 */
	REGISTRY_SUCCESS(200, "注册成功，即将返回首页"),

	/**
	 * 退出成功！
	 */
	LOGOUT(200, "退出成功！"),

	/**
	 * 请先登录！
	 */
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "请先登录！"),

	/**
	 * 暂无权限访问！
	 */
	FORBIDDEN(HttpStatus.FORBIDDEN.value(), "权限不足！"),

	/**
	 * 请求不存在！
	 */
	NOT_FOUND(HttpStatus.NOT_FOUND.value(), "请求不存在！"),

	/**
	 * 请求方式不支持！
	 */
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方式不支持！"),

	/**
	 * 请求异常！
	 */
	BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "请求异常！"),

	/**
	 * 参数不匹配！
	 */
	PARAM_NOT_MATCH(400, "参数不匹配！"),

	/**
	 * 参数不能为空！
	 */
	PARAM_NOT_NULL(400, "参数不能为空！"),

	/**
	 * 未知异常
	 */
	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器开小差去啦~"),

	/**
	 * JSON转换异常
	 */
	JSON_ERROR(512, "JSON转换异常"),

	/**
	 * redis异常
	 */
	REDIS_ERROR(513, "redis异常"),

	/**
	 * mysql异常
	 */
	MYSQL_ERROR(514, "mysql异常"),

	/**
	 * email异常
	 */
	EMAIL_ERROR(515, "email异常"),

	/**
	 * SMS短信异常
	 */
	SMS_ERROR(516, "sms短信异常"),

	/**
	 * 文件系统异常
	 */
	FILE_ERROR(517, "文件系统异常"),

	/**
	 * 连接极光异常
	 */
	JIGUANG_CONNECTION_ERROR(518, "连接极光异常"),

	/**
	 * 向极光发送请求异常
	 */
	JIGUANG_REQUEST_ERROR(518, "向极光发送请求异常"),

	/**
	 * 博客创建失败
	 */
	BLOG_CREATE_FAIL(519, "博客创建失败！"),

	/**
	 * 验证码发送失败
	 */
	SEND_CODE_FAILED(520, "验证码发送失败，请稍后重试"),

	/**
	 * 参数错误
	 */
	ILLEGAL_ARGUMENT(521, "参数错误！请检查参数"),

	/**
	 * 注册失败
	 */
	REGISTRY_FAILED(5006, "注册失败，请稍后重试"),

	/**
	 * 更新失败
	 */
	USER_UPDATE_FAILED(5007, "更新失败，请稍后重试"),

	/**
	 * 用户名或密码错误！
	 */
	USERNAME_PASSWORD_ERROR(5001, "用户名或密码错误！"),

	/**
	 * token 已过期，请重新登录！
	 */
	TOKEN_EXPIRED(5002, "token 已过期，请重新登录！"),

	/**
	 * token 解析失败，请尝试重新登录！
	 */
	TOKEN_PARSE_ERROR(5002, "token 解析失败，请尝试重新登录！"),

	/**
	 * 当前用户已在别处登录，请尝试更改密码或重新登录！
	 */
	TOKEN_OUT_OF_CTRL(5003, "当前用户已在别处登录，请尝试更改密码或重新登录！"),

	/**
	 * 注册时用户已存在
	 */
	USER_ALREADY_EXIST(5004, "用户已存在！"),

	/**
	 * 查询时用户不存在
	 */
	USER_NOT_EXIST(5005, "用户不存在！"),

	/**
	 * 计划生成次数在配置文件中配置
	 */
	PLAN_CREATE_REACH_LIMIT(5101, "计划生成超过限制次数！"),

	/**
	 * 计划不存在
	 */
	PLAN_NOT_EXIST(5102, "计划不存在！"),

	/**
	 * 计划不存在
	 */
	BLOG_NOT_EXIST(5103, "博客不存在！"),

	/**
	 * 博客已经被喜欢
	 */
	BLOG_LIKED_ALREADY(5104, "博客已经被喜欢！"),

	/**
	 * 博客已经被收藏
	 */
	BLOG_ALREADY_FAVOR(5105, "博客已经被收藏！"),

	/**
	 * 用户没有粉丝
	 */
	NO_FOLLOWER(5106, "暂时没有粉丝哟~"),

	/**
	 * 用户没有关注
	 */
	NO_FOLLOWED(5107, "暂时没有关注任何人哟~"),

	/**
	 * 用户没有收藏
	 */
	NO_FAVORITE(5107, "你还没有收藏哟~"),

	/**
	 * 用户没有收藏
	 */
	BLOG_HAS_NO_LIKE(5108, "暂时没有人点赞哟~"),

	/**
	 * 用户没有给任何博客点赞
	 */
	NO_LIKED_BLOG(5109, "暂时没有给任何博客点赞哟~"),

	/**
	 * 不允许自交
	 */
	CANNOT_FOLLOW_SELF(5110, "你不能关注自己！"),

	/**
	 * 计划无法创建
	 */
	PLAN_CREATE_FAIL(5111, "计划自己溜走啦~请稍后重试"),

	/**
	 * 用户没有博客
	 */
	USER_HAS_BO_BLOG(5111, "还没有博客呢~"),

	/**
	 * 用户没有博客
	 */
	USER_HAS_NO_PLAN(5112, "还没有计划呢~"),

	/**
	 * 动态计划开启时间不合理
	 */
	ILLEGAL_MONITOR_TIME(5201, "当前计划不能开启动态模式哟~");

	/**
	 * 状态码
	 */
	@Schema(name = "code", description = "状态码")
	private final Integer code;

	/**
	 * 返回信息
	 */
	@Schema(name = "message", description = "状态码描述")
	private final String message;

	public static ServerStatus fromCode(int code) {
		for (ServerStatus status : ServerStatus.values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return SUCCESS;
	}
}
