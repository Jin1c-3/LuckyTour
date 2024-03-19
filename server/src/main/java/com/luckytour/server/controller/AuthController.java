package com.luckytour.server.controller;

import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.exception.EMailException;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.exception.SecurityException;
import com.luckytour.server.exception.SmsException;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.payload.front.JwtResponse;
import com.luckytour.server.payload.front.LoginRequest;
import com.luckytour.server.service.EMailService;
import com.luckytour.server.service.PushService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.CodeUtil;
import com.luckytour.server.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录登出控制器
 *
 * @author qing
 * @date Created in 2023/8/4 8:43
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "用户登录登出控制器")
@Slf4j
@CrossOrigin
@Validated
public class AuthController {

	private final UserService userService;

	private final EMailService eMailService;

	@Resource
	private final PushService jiguangPushService;

	@Autowired
	public AuthController(UserService userService, EMailService eMailService, PushService jiguangPushService) {
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

	private ServerResponseEntity<String> sendCode(String emailOrPhone, boolean userExists) throws MysqlException {
		if (userExists != userService.emailOrPhoneIsExist(emailOrPhone)) {
			return ServerResponseEntity.ofStatus(userExists ? ServerStatus.USER_NOT_EXIST : ServerStatus.USER_ALREADY_EXIST);
		}
		String code = CodeUtil.generateVerificationCode();
		if (sendVerificationCode(emailOrPhone, code)) {
			return ServerResponseEntity.ofSuccess(code);
		}
		return ServerResponseEntity.ofStatus(ServerStatus.SEND_CODE_FAILED);
	}

	@GetMapping("/getCodeNotInDB")
	@Operation(summary = "新建用户之前的验证码获取")
	@Parameter(name = "emailOrPhone", description = "邮箱或手机号", required = true, example = "12345678901")
	public ServerResponseEntity<String> getCodeNotInDB(@Valid @Pattern(regexp = Regex.MOBILE_OR_EMAIL_REGEX, message = Alert.PARAM_NOT_EMAIL_OR_PHONE) String emailOrPhone) throws MysqlException {
		return sendCode(emailOrPhone, false);
	}

	@GetMapping("/getCodeInDB")
	@Operation(summary = "无密码登录的验证码获取")
	@Parameter(name = "emailOrPhone", description = "邮箱或手机号", required = true, example = "12345678901")
	public ServerResponseEntity<String> getCodeInDB(@Valid @Pattern(regexp = Regex.MOBILE_OR_EMAIL_REGEX, message = Alert.PARAM_NOT_EMAIL_OR_PHONE) String emailOrPhone) throws MysqlException {
		return sendCode(emailOrPhone, true);
	}

	@Operation(summary = "登录")
	@PostMapping("/login")
	@Parameter(name = "loginRequest", description = "登录请求", required = true, example = "{\"emailOrPhone\":\"test@qq.com\",\"password\":\"123456\"}")
	public ServerResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws MysqlException {
		return userService.getOptByEmailOrPhone(loginRequest.getEmailOrPhone())
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
					return ServerResponseEntity.ofStatus(ServerStatus.LOGIN_SUCCESS, new JwtResponse(jwt));
				})
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USERNAME_PASSWORD_ERROR));
	}

	@Operation(summary = "登出")
	@PostMapping("/logout")
	public <T> ServerResponseEntity<T> logout(HttpServletRequest request) {
		try {
			// 设置JWT过期
			JwtUtil.invalidate(request);
		} catch (SecurityException e) {
			throw new SecurityException(ServerStatus.UNAUTHORIZED);
		}
		return ServerResponseEntity.ofStatus(ServerStatus.LOGOUT);
	}
}
