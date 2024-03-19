package com.luckytour.server.controller;

import com.luckytour.server.common.annotation.UserLoginRequired;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.entity.User;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.payload.front.UserUpdateRequest;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.CodeUtil;
import com.luckytour.server.util.FileUploadUtil;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.SimpleUserVO;
import com.luckytour.server.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 用户信息控制器
 *
 * @author qing
 * @date Created in 2024/1/12 16:23
 */
@RestController
@Tag(name = "用户信息控制器")
@CrossOrigin
@Slf4j
@RequestMapping("/user")
@Validated
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/getinfo")
	@Operation(summary = "token获取用户信息")
	@UserLoginRequired
	public ServerResponseEntity<UserVO> getOnlineUserInfo(HttpServletRequest request) throws MysqlException {
		return userService.getOptById(JwtUtil.parseId(request))
				.map(user -> UserVO.create(request, user))
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}

	@GetMapping("/getinfoById")
	@Operation(summary = "用户id获取用户信息")
	@UserLoginRequired
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<UserVO> getOnlineUserInfoById(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid, HttpServletRequest request) throws MysqlException {
		return userService.getOptById(uid)
				.map(user -> UserVO.create(request, user))
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}

	@GetMapping("/getSimpleInfoById")
	@Operation(summary = "用户id获取用户信息（极简用户）")
	@UserLoginRequired
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<SimpleUserVO> getSimpleUserDescriptionInfoById(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid, HttpServletRequest request) throws MysqlException {
		return userService.getOptById(uid)
				.map(user -> SimpleUserVO.create(request, user))
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}

	@PostMapping("/getinfoList")
	@Operation(summary = "用户id列表获取用户信息")
	@UserLoginRequired
	@Parameter(name = "userIds", description = "用户id列表", required = true)
	public ServerResponseEntity<List<UserVO>> getOnlineInfoList(@RequestBody @NotEmpty(message = "用户id列表不能为空") String[] userIds, HttpServletRequest request) throws MysqlException {
		return ServerResponseEntity.ofSuccess(
				userService.listByIds(Arrays.stream(userIds).toList()).stream()
						.filter(userService::idIsExist)
						.map(user -> UserVO.create(request, user))
						.toList()
		);
	}

	@GetMapping("/new")
	@Operation(summary = "新建用户")
	@UserLoginRequired(required = false)
	@Parameter(name = "emailOrPhone", description = "邮箱或手机号", required = true)
	public ServerResponseEntity<Object> newUser(@Pattern(regexp = Regex.MOBILE_OR_EMAIL_REGEX, message = "邮箱或手机号输入错误") String emailOrPhone) throws MysqlException {
		if (userService.emailOrPhoneIsExist(emailOrPhone)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_ALREADY_EXIST);
		}
		User newUser = new User();
		if (Regex.isMobile(emailOrPhone)) {
			newUser.setPhone(emailOrPhone);
		} else if (Regex.isEmail(emailOrPhone)) {
			newUser.setEmail(emailOrPhone);
		} else {
			throw new IllegalArgumentException(Alert.PARAM_NOT_EMAIL_OR_PHONE);
		}
		do {
			newUser.setId(CodeUtil.generateUserId());
		} while (userService.idIsExist(newUser.getId()));
		return userService.save(newUser)
				? ServerResponseEntity.ofSuccess()
				: ServerResponseEntity.ofStatus(ServerStatus.REGISTRY_FAILED);
	}

	@PostMapping("/update")
	@Operation(summary = "更新用户信息")
	@UserLoginRequired
	@Parameters({
			@Parameter(name = "userUpdateRequest", description = "用户更新请求", required = true),
			@Parameter(name = "avatarPic", description = "用户头像", required = false)
	})
	public <T> ServerResponseEntity<T> update(@Valid UserUpdateRequest userUpdateRequest, MultipartFile avatarPic) throws MysqlException {
		if (!userService.idIsExist(userUpdateRequest.getId())) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		User newUser = User.create(userUpdateRequest);
		Optional.ofNullable(avatarPic)
				.ifPresent(file -> newUser.setAvatar(FileUploadUtil.storeAvatarByUid(newUser.getId(), file)));
		return userService.updateById(newUser)
				? ServerResponseEntity.ofSuccess()
				: ServerResponseEntity.ofStatus(ServerStatus.USER_UPDATE_FAILED);
	}
}
