package com.luckytour.server.controller;

import com.luckytour.server.common.ApiResponse;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.entity.User;
import com.luckytour.server.exception.SecurityException;
import com.luckytour.server.payload.LoginRequest;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 认证controller
 * </p>
 *
 * @author qing
 * @date Created in 2023/8/4 8:43
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证接口")
@Slf4j
@CrossOrigin
public class AuthController {

	@Autowired
	private UserService userService;

	@Operation(summary = "登录")
	@PostMapping("/login")
	public ApiResponse<JwtResponse> login(HttpServletRequest request,@Valid @RequestBody LoginRequest loginRequest) {
		/*TODO: 1.根据用户名查询用户信息
		 * 2.校验密码是否正确
		 * 3.异常处理
		 */
		User user = User.builder()
				.username(loginRequest.getUsernameOrEmailOrPhone())
				.id(loginRequest.getUsernameOrEmailOrPhone() + 111)
				.build();
		String jwt = JwtUtil.create(user.getId(), user.getUsername(), loginRequest.getRememberMe());
		request.setAttribute(Consts.TOKEN_KEY, jwt);
		return ApiResponse.ofSuccess(new JwtResponse(jwt));
	}

	@Operation(summary = "登出")
	@PostMapping("/logout")
	public <T> ApiResponse<T> logout(HttpServletRequest request) {
		try {
			// 设置JWT过期
			JwtUtil.invalidate(request);
		} catch (SecurityException e) {
			throw new SecurityException(ApiStatus.UNAUTHORIZED);
		}
		return ApiResponse.ofStatus(ApiStatus.LOGOUT);
	}
}
