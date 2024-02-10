package com.luckytour.server.controller;

import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.entity.User;
import com.luckytour.server.exception.EMailException;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.exception.SecurityException;
import com.luckytour.server.exception.SmsException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.JwtResponse;
import com.luckytour.server.payload.LoginRequest;
import com.luckytour.server.service.EMailService;
import com.luckytour.server.service.JiguangPushService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
@Validated
public class AuthController {

	private final UserService userService;

	private final EMailService eMailService;

	private final JiguangPushService jiguangPushService;

	@Autowired
	public AuthController(UserService userService, EMailService eMailService, JiguangPushService jiguangPushService) {
		this.userService = userService;
		this.eMailService = eMailService;
		this.jiguangPushService = jiguangPushService;
	}

	private boolean sendVerificationCode(String emailOrPhone, String code) {
		if (Regex.isMobile(emailOrPhone)) {
			try {
				return jiguangPushService.sendVerificationCode(emailOrPhone, code);
			} catch (Exception e) {
				throw new SmsException("短信发送失败", e);
			}
		} else if (Regex.isEmail(emailOrPhone)) {
			try {
				return eMailService.sendVerificationCode(emailOrPhone, code);
			} catch (Exception e) {
				throw new EMailException("邮件发送失败", e);
			}
		}
		// 既不是邮箱也不是手机号，不知道是什么鬼
		throw new IllegalArgumentException(Alert.PARAM_NOT_EMAIL_OR_PHONE);
	}

	private ApiResponse<String> sendCode(String emailOrPhone, boolean userExists) throws MysqlException {
		Optional<User> userOptional = Optional.ofNullable(userService.findByEmailOrPhone(emailOrPhone));
		if (userExists != userOptional.isPresent()) {
			return ApiResponse.ofStatus(userExists ? ApiStatus.USER_NOT_EXIST : ApiStatus.USER_ALREADY_EXIST);
		}
		String code = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
		if (sendVerificationCode(emailOrPhone, code)) {
			return ApiResponse.ofSuccess(code);
		}
		return ApiResponse.ofStatus(ApiStatus.SEND_CODE_FAILED);
	}

	@GetMapping("/getCodeNotInDB")
	@Operation(summary = "新建用户之前的验证码获取")
	public ApiResponse<String> getCodeNotInDB(@Valid @Pattern(regexp = Regex.MOBILE_OR_EMAIL_REGEX, message = Alert.PARAM_NOT_EMAIL_OR_PHONE) String emailOrPhone) throws MysqlException {
		return sendCode(emailOrPhone, false);
	}

	@GetMapping("/getCodeInDB")
	@Operation(summary = "无密码登录的验证码获取")
	public ApiResponse<String> getCodeInDB(@Valid @Pattern(regexp = Regex.MOBILE_OR_EMAIL_REGEX, message = Alert.PARAM_NOT_EMAIL_OR_PHONE) String emailOrPhone) throws MysqlException {
		return sendCode(emailOrPhone, true);
	}

	@Operation(summary = "登录")
	@PostMapping("/login")
	public ApiResponse<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws MysqlException {
		return Optional.ofNullable(userService.findByEmailOrPhone(loginRequest.getEmailOrPhone()))
				// 密码不存在 或 密码存在且错误
				// 密码不存在，那肯定是之前就获取过验证码了。这是一处安全漏洞
				.filter(user -> StringUtils.isBlank(loginRequest.getPassword()) || JwtUtil.matches(loginRequest.getPassword(), user.getPassword()))
				.map(user -> {
					// 更新极光registrationId，没有就算了
					if (StringUtils.isNotBlank(loginRequest.getJrid())) {
						user.setJiguangRegistrationId(loginRequest.getJrid());
						userService.updateById(user);
					}
					String jwt = JwtUtil.createToken(user.getId(), user.getNickname(), loginRequest.getRememberMe());
					return ApiResponse.ofStatus(ApiStatus.LOGIN_SUCCESS, new JwtResponse(jwt));
				})
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USERNAME_PASSWORD_ERROR));
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
