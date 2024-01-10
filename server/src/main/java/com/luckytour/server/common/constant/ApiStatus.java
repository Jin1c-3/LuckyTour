package com.luckytour.server.common.constant;

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
@Schema(name = "ApiStatus", description = "状态码封装")
public enum ApiStatus implements IApiStatus {
	/**
	 * 操作成功
	 */
	SUCCESS(HttpStatus.OK.value(), "操作成功"),

	/**
	 * 登陆成功
	 */
	LOGIN_SUCCESS(200, "登录成功！"),

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
	TOKEN_OUT_OF_CTRL(5003, "当前用户已在别处登录，请尝试更改密码或重新登录！");

	/**
	 * 状态码
	 */
	@Schema(name = "code", description = "状态码")
	private Integer code;

	/**
	 * 返回信息
	 */
	@Schema(name = "message", description = "状态码描述")
	private String message;

	public static ApiStatus fromCode(int code) {
		for (ApiStatus status : ApiStatus.values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return SUCCESS;
	}
}
