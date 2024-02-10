package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.entity.Follow;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.FollowUnfollowRequest;
import com.luckytour.server.service.FollowService;
import com.luckytour.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 关注表 前端控制器
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@RestController
@RequestMapping("/follow")
@Tag(name = "用户关注接口")
@CrossOrigin
@Slf4j
@Validated
public class FollowController {

	private final UserService userService;

	private final FollowService followService;

	@Autowired
	public FollowController(UserService userService, FollowService followService) {
		this.userService = userService;
		this.followService = followService;
	}

	@GetMapping("/getFollower")
	@Operation(summary = "获取用户粉丝列表")
	public ApiResponse<List<String>> getFollower(@NotBlank(message = "用户id不能为空") String uid) {
		return userService.getOptById(uid)
				.map(user -> ApiResponse.ofSuccess(
						followService.list(new QueryWrapper<>(Follow.builder().followedUid(uid).build())).stream()
								.filter(follow -> follow.getStatus() != ConstsPool.FOLLOWED)
								.map(Follow::getFollowerUid).toList()))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@GetMapping("/getFollowerCount")
	@Operation(summary = "获取用户粉丝数量")
	public ApiResponse<Long> getFollowerCount(@NotBlank(message = "用户id不能为空") String uid) {
		return userService.getOptById(uid)
				.map(user -> ApiResponse.ofSuccess(followService.count(new QueryWrapper<>(Follow.builder().followedUid(uid).build()))))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@GetMapping("/getFollowed")
	@Operation(summary = "获取用户关注列表")
	public ApiResponse<List<String>> getFollowed(@NotBlank(message = "用户id不能为空") String uid) {
		return userService.getOptById(uid)
				.map(user -> ApiResponse.ofSuccess(followService.list(new QueryWrapper<>(Follow.builder().followerUid(uid).build())).stream()
						.filter(follow -> follow.getStatus() != ConstsPool.FOLLOWED)
						.map(Follow::getFollowedUid).toList()))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@GetMapping("/getFollowedCount")
	@Operation(summary = "获取用户关注数量")
	public ApiResponse<Long> getFollowedCount(@NotBlank(message = "用户id不能为空") String uid) {
		return userService.getOptById(uid)
				.map(user -> ApiResponse.ofSuccess(followService.count(new QueryWrapper<>(Follow.builder().followerUid(uid).build()))))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@PostMapping("/followUnfollow")
	@Operation(summary = "关注/取消关注")
	public ApiResponse<Object> followUnfollow(@RequestBody @Valid FollowUnfollowRequest request) {
		return userService.getOptById(request.getFollowerUid())
				.map(user -> userService.getOptById(request.getFollowedUid())
						.map(user1 -> {
							Optional.ofNullable(followService.selectByMultiId(new Follow().createByFollowUnfollowRequest(request)))
									.ifPresentOrElse(follow -> {
												follow.setStatus(follow.getStatus() == ConstsPool.FOLLOWED ? ConstsPool.UNFOLLOWED : ConstsPool.FOLLOWED);
												followService.updateById(follow);
											}, () -> followService.save(Follow.builder().followerUid(request.getFollowerUid()).followedUid(request.getFollowedUid()).status(ConstsPool.FOLLOWED).build())
									);
							return ApiResponse.ofSuccess();
						})
						.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST)))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}
}
