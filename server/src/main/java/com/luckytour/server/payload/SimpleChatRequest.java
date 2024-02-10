package com.luckytour.server.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qing
 * @date Created in 2024/1/16 14:53
 */
@AllArgsConstructor
@Schema(name = "SimpleChatRequest", description = "简单聊天请求")
public class SimpleChatRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "聊天内容", example = "你好")
	private Object ask;
}
