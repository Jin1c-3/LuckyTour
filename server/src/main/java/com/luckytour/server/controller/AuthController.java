package com.luckytour.server.controller;

import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.entity.User;
import com.luckytour.server.exception.EMailException;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.exception.SecurityException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.JwtResponse;
import com.luckytour.server.payload.LoginRequest;
import com.luckytour.server.service.EMailService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

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

	@Autowired
	private EMailService eMailService;

	@GetMapping("/getcode")
	@Operation(summary = "新建用户之前和无密码登录的验证码获取")
	public ApiResponse<String> getcode(String emailOrPhone) throws MysqlException {
		if (userService.findByEmailOrPhone(emailOrPhone) != null) {
			return ApiResponse.ofStatus(ApiStatus.USER_ALREADY_EXIST);
		}
		String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
		if (ValidationUtil.isMobile(emailOrPhone)) {
			//TODO: 发送短信验证码
		} else if (ValidationUtil.isEmail(emailOrPhone)) {
			try {
				eMailService.sendVerificationCode(emailOrPhone, code);
			} catch (Exception e) {
				throw new EMailException("邮件发送失败", e);
			}
		}
		return ApiResponse.ofSuccess(code);
	}

	@Operation(summary = "登录")
	@PostMapping("/login")
	public ApiResponse<JwtResponse> login(HttpServletRequest request, @Valid @RequestBody LoginRequest loginRequest) throws MysqlException {
		User user = userService.findByEmailOrPhone(loginRequest.getEmailOrPhone());
		// 用户不存在或密码存在且错误
		if (user == null || StringUtils.isNotBlank(user.getPassword()) && !user.getPassword().equals(loginRequest.getPassword())) {
			return ApiResponse.ofStatus(ApiStatus.USERNAME_PASSWORD_ERROR);
		}
		// 密码不存在，那肯定是之前就获取过验证码了
		// 更新极光registrationId
		if (StringUtils.isNotBlank(loginRequest.getJrid())) {
			user.setJiguangRegistrationId(loginRequest.getJrid());
			userService.updateById(user);
		}

		String jwt = JwtUtil.create(user.getId(), user.getNickname(), loginRequest.getRememberMe());
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
