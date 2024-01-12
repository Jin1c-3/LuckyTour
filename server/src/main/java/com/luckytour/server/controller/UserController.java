package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.entity.User;
import com.luckytour.server.exception.EMailException;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.service.EMailService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.util.ValidationUtil;
import com.luckytour.server.vo.OnlineUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author qing
 * @date Created in 2024/1/12 16:23
 */
@RestController
@Tag(name = "用户信息接口")
@CrossOrigin
@Slf4j
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/getinfo")
	@Operation(summary = "通过token获取在线用户信息")
	public ApiResponse<OnlineUser> getOnlineUserInfo(String token) throws MysqlException {
		String userId = JwtUtil.parse(token).getId();
		OnlineUser onlineUser = OnlineUser.create(userService.getOne(new QueryWrapper<User>().eq("id", userId)));
		return ApiResponse.ofSuccess(onlineUser);
	}

	@GetMapping("/new")
	@Operation(summary = "新建用户")
	public <T> ApiResponse<T> newUser(String emailOrPhone) throws MysqlException {
		if (userService.findByEmailOrPhone(emailOrPhone) != null) {
			return ApiResponse.ofStatus(ApiStatus.USER_ALREADY_EXIST);
		}
		User newUser = new User();
		if (ValidationUtil.isMobile(emailOrPhone)) {
			newUser.setPhone(emailOrPhone);
		} else if (ValidationUtil.isEmail(emailOrPhone)) {
			newUser.setEmail(emailOrPhone);
		} else {
			return ApiResponse.ofStatus(ApiStatus.USERNAME_PASSWORD_ERROR);
		}
		newUser.setId(UUID.randomUUID().toString());
		userService.save(newUser);
		return ApiResponse.ofStatus(ApiStatus.SUCCESS);
	}
}
