package com.luckytour.server.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.IApiStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@Schema(name = "ApiResponse", description = "统一响应体")
@Getter
@ToString
public class ApiResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("code")
	@Schema(name = "code", description = "状态码")
	private Integer code;

	@JsonProperty("message")
	@Schema(name = "message", description = "内容")
	private String message;

	@JsonProperty("data")
	@Schema(name = "data", description = "数据")
	private T data;

	/**
	 * 私有全参构造器
	 *
	 * @param code    状态码
	 * @param message 返回内容
	 * @param data    返回数据
	 */
	private ApiResponse(Integer code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * 构造一个自定义的API返回
	 *
	 * @param code    状态码
	 * @param message 返回内容
	 * @param data    返回数据
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> of(Integer code, String message, T data) {
		return new ApiResponse<>(code, message, data);
	}

	/**
	 * 构造一个有状态的API返回
	 *
	 * @param status 状态{@link ApiStatus}
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofStatus(IApiStatus status) {
		return ofStatus(status, null);
	}

	/**
	 * 构造一个有状态且带数据的API返回
	 *
	 * @param status 状态{@link ApiStatus}
	 * @param data   返回数据
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofStatus(IApiStatus status, T data) {
		return of(status.getCode(), status.getMessage(), data);
	}

	/**
	 * 构造一个成功的API返回
	 *
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofSuccess() {
		return ofStatus(ApiStatus.SUCCESS);
	}

	/**
	 * 构造一个成功且带数据的API返回
	 *
	 * @param data 返回内容
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofSuccess(T data) {
		return ofStatus(ApiStatus.SUCCESS, data);
	}

	/**
	 * 构造一个成功、带自定义消息的API返回
	 *
	 * @param message 返回内容
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofSuccessMsg(String message) {
		return of(ApiStatus.SUCCESS.getCode(), message, null);
	}

	/**
	 * 构造一个成功、带自定义消息、带数据的API返回
	 *
	 * @param message 返回内容
	 * @param data    返回数据
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofSuccessMsg(String message, T data) {
		return of(ApiStatus.SUCCESS.getCode(), message, data);
	}

	/**
	 * 构造一个失败的API返回
	 *
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofFailMsg() {
		return ofStatus(ApiStatus.BAD_REQUEST);
	}

	/**
	 * 构造一个失败、带自定义消息的API返回
	 *
	 * @param message 返回内容
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofFailMsg(String message) {
		return of(ApiStatus.BAD_REQUEST.getCode(), message, null);
	}

//	/**
//	 * 构造一个带基类异常的API返回
//	 *
//	 * @param e 异常
//	 * @return ApiResponse
//	 */
//	public static <T, E extends BaseException> ApiResponse<T> ofException(E e) {
//		return of(e.getCode(), e.getMessage(), null);
//	}
//
//	/**
//	 * 构造一个带基类异常、带数据的API返回
//	 *
//	 * @param e    异常
//	 * @param data 返回数据
//	 * @return ApiResponse
//	 */
//	public static <T, E extends BaseException> ApiResponse<T> ofException(E e, T data) {
//		return of(e.getCode(), e.getMessage(), data);
//	}

	/**
	 * 构造一个带异常、带数据的API返回
	 *
	 * @param e    异常
	 * @param data 返回数据
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofException(Exception e, T data) {
		if (e instanceof BaseException be) {
			return of(be.getCode(), be.getMessage(), data);
		}
		return ofStatus(ApiStatus.UNKNOWN_ERROR, data);
	}

	/**
	 * 构造一个带异常的API返回
	 *
	 * @param e 异常
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> ofException(Exception e) {
		return ofException(e, null);
	}

}
