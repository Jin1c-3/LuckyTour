package com.luckytour.server.controller;

import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.entity.User;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.UserUpdateRequest;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.FileUploadUtil;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.OnlineUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/getinfo")
	@Operation(summary = "通过token获取在线用户信息")
	public ApiResponse<OnlineUser> getOnlineUserInfo(HttpServletRequest request) throws MysqlException {
		String token = request.getHeader(ConstsPool.TOKEN_KEY);
		String userId = JwtUtil.parse(token).getId();
		return userService.getOptById(userId)
				.map(OnlineUser::create)
				.map(ApiResponse::ofSuccess)
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@PostMapping("/getinfoList")
	@Operation(summary = "通过用户id列表获取用户信息，会校验用户是否存在")
	public ApiResponse<List<OnlineUser>> getOnlineInfoList(@RequestBody @NotEmpty(message = "用户id列表不能为空") String[] userIds) throws MysqlException {
		return ApiResponse.ofSuccess(
				userService.listByIds(Arrays.stream(userIds).toList()).stream()
						.filter(userService::idIsExist)
						.map(OnlineUser::create).toList());
	}

	@GetMapping("/new")
	@Operation(summary = "新建用户")
	public ApiResponse<Object> newUser(@Valid @Pattern(regexp = Regex.MOBILE_OR_EMAIL_REGEX, message = "邮箱或手机号输入错误") String emailOrPhone) throws MysqlException {
		return Optional.ofNullable(userService.findByEmailOrPhone(emailOrPhone))
				.map(user -> ApiResponse.ofStatus(ApiStatus.USER_ALREADY_EXIST))
				.orElseGet(() -> {
					User newUser = new User();
					if (Regex.isMobile(emailOrPhone)) {
						newUser.setPhone(emailOrPhone);
					} else if (Regex.isEmail(emailOrPhone)) {
						newUser.setEmail(emailOrPhone);
					} else {
						throw new IllegalArgumentException(Alert.PARAM_NOT_EMAIL_OR_PHONE);
					}
					newUser.setId(UUID.randomUUID().toString());
					return userService.save(newUser)
							? ApiResponse.ofStatus(ApiStatus.REGISTRY_SUCCESS)
							: ApiResponse.ofStatus(ApiStatus.REGISTRY_FAILED);
				});
	}

	@PostMapping("/update")
	@Operation(summary = "更新用户信息")
	@Parameter(name = "userUpdateRequest", description = "用户更新信息")
	public <T> ApiResponse<T> update(@Valid UserUpdateRequest userUpdateRequest, MultipartFile avatarPic, HttpServletRequest request) throws MysqlException {
		User newUser = new User();
		BeanUtils.copyProperties(userUpdateRequest, newUser);
		if (StringUtils.isNotBlank(newUser.getPassword())) {
			newUser.setPassword(JwtUtil.encrypt(newUser.getPassword()));
		}
		if (StringUtils.isNotBlank(userUpdateRequest.getSex())) {
			if (ConstsPool.MALE_STR.equals(userUpdateRequest.getSex())) {
				newUser.setSex(ConstsPool.MALE_INT);
			} else if (ConstsPool.FEMALE_STR.equals(userUpdateRequest.getSex())) {
				newUser.setSex(ConstsPool.FEMALE_INT);
			}
		}
		if (avatarPic != null) {
			String avatarUrl = FileUploadUtil.storeAvatar(request, newUser.getId(), avatarPic);
			newUser.setAvatar(avatarUrl);
		}
		return userService.updateById(newUser)
				? ApiResponse.ofSuccess()
				: ApiResponse.ofStatus(ApiStatus.USER_UPDATE_FAILED);
	}
}
