package com.luckytour.server.common.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckytour.server.common.BaseException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Schema(name = "ServerResponseEntity", description = "统一响应体")
@Getter
@ToString
@Slf4j
public class ServerResponseEntity<T> implements Serializable {

	@Serial
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
	private ServerResponseEntity(Integer code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
		if (data != null && !(data instanceof Serializable)) {
			log.error(data.getClass().getName() + "未实现Serializable接口");
		}
	}

	/**
	 * 构造一个自定义的API返回
	 *
	 * @param code    状态码
	 * @param message 返回内容
	 * @param data    返回数据
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> of(Integer code, String message, T data) {
		return new ServerResponseEntity<>(code, message, data);
	}

	/**
	 * 构造一个有状态的API返回
	 *
	 * @param status 状态{@link ServerStatus}
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofStatus(IServerStatus status) {
		return ofStatus(status, null);
	}

	/**
	 * 构造一个有状态且带数据的API返回
	 *
	 * @param status 状态{@link ServerStatus}
	 * @param data   返回数据
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofStatus(IServerStatus status, T data) {
		return of(status.getCode(), status.getMessage(), data);
	}

	/**
	 * 构造一个成功的API返回
	 *
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofSuccess() {
		return ofStatus(ServerStatus.SUCCESS);
	}

	/**
	 * 构造一个成功且带数据的API返回
	 *
	 * @param data 返回内容
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofSuccess(T data) {
		return ofStatus(ServerStatus.SUCCESS, data);
	}

	/**
	 * 构造一个成功、带自定义消息的API返回
	 *
	 * @param message 返回内容
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofSuccessMsg(String message) {
		return of(ServerStatus.SUCCESS.getCode(), message, null);
	}

	/**
	 * 构造一个成功、带自定义消息、带数据的API返回
	 *
	 * @param message 返回内容
	 * @param data    返回数据
	 * @return ApiResponse
	 */
	/*public static <T> ServerResponseEntity<T> ofSuccessMsg(String message, T data) {
		return of(ServerStatus.SUCCESS.getCode(), message, data);
	}*/

	/**
	 * 构造一个失败的API返回
	 *
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofFailMsg() {
		return ofStatus(ServerStatus.BAD_REQUEST);
	}

	/**
	 * 构造一个失败、带自定义消息的API返回
	 *
	 * @param message 返回内容
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofFailMsg(String message) {
		return of(ServerStatus.BAD_REQUEST.getCode(), message, null);
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
	public static <T> ServerResponseEntity<T> ofException(Exception e, T data) {
		if (e instanceof BaseException be) {
			return of(be.getCode(), be.getMessage(), data);
		}
		return ofStatus(ServerStatus.UNKNOWN_ERROR, data);
	}

	/**
	 * 构造一个带异常的API返回
	 *
	 * @param e 异常
	 * @return ApiResponse
	 */
	public static <T> ServerResponseEntity<T> ofException(Exception e) {
		return ofException(e, null);
	}

}
