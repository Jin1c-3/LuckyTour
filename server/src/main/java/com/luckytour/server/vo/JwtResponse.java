package com.luckytour.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class JwtResponse {
	/**
	 * token 字段
	 */
	private String token;
	/**
	 * token类型
	 */
	private String tokenType = "Bearer";

	public JwtResponse(String token) {
		this.token = token;
	}
}
