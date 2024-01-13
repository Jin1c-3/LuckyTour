package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.entity.User;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.UserUpdateRequest;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.FileUploadUtil;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.util.ValidationUtil;
import com.luckytour.server.vo.OnlineUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author qing
 * @date Created in 2024/1/12 16:23
 */
@RestController
@Tag(name = "用户信息接口")
@CrossOrigin
@Slf4j
@RequestMapping("/user")
@Validated
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/getinfo")
	@Operation(summary = "通过token获取在线用户信息")
	public ApiResponse<OnlineUser> getOnlineUserInfo(HttpServletRequest request) throws MysqlException {
		String token = request.getHeader(Consts.TOKEN_KEY);
		String userId = JwtUtil.parse(token).getId();
		OnlineUser onlineUser = OnlineUser.create(userService.getOne(new QueryWrapper<User>().eq("id", userId)));
		return ApiResponse.ofSuccess(onlineUser);
	}

	@GetMapping("/new")
	@Operation(summary = "新建用户")
	public <T> ApiResponse<T> newUser(@Valid @Pattern(regexp = Regex.MOBILE_OR_EMAIL_REGEX, message = "邮箱或手机号输入错误") String emailOrPhone) throws MysqlException {
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
		return ApiResponse.ofStatus(ApiStatus.REGISTRY_SUCCESS);
	}

	@PostMapping("/update")
	@Operation(summary = "更新用户信息")
	@Parameter(name = "userUpdateRequest", description = "用户更新信息")
	public <T> ApiResponse<T> update(@Valid UserUpdateRequest userUpdateRequest, MultipartFile avatarPic, HttpServletRequest request) throws MysqlException {
		User newUser = new User();
		BeanUtils.copyProperties(userUpdateRequest, newUser);
		if (avatarPic != null) {
			String avatarUrl = FileUploadUtil.storeAvatar(request, newUser.getId(), avatarPic);
			newUser.setAvatar(avatarUrl);
		}
		userService.updateById(newUser);
		return ApiResponse.ofSuccess();
	}
}
