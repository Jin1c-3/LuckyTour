package com.luckytour.server.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * JWT 响应返回
 * </p>
 *
 * @author qing
 * @date Created in 2023/8/4 9:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "JwtResponse", description = "JWT响应token返回")
public class JwtResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "token")
	private String token;

	@Schema(description = "token类型")
	private String tokenType = "Bearer";

	public JwtResponse(String token) {
		this.token = token;
	}
}
