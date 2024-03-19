package com.luckytour.server.controller;

import com.luckytour.server.common.annotation.UserLoginRequired;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.entity.Follow;
import com.luckytour.server.service.FollowService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.SimpleUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * 用户关注控制器
 *
 * @author qing
 * @since 2024-02-09
 */
@RestController
@RequestMapping("/follow")
@Tag(name = "用户关注控制器")
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
	@Operation(summary = "用户id获取粉丝列表（极简用户）")
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<List<SimpleUserVO>> getFollower(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		if (!userService.idIsExist(uid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		var followers = followService.getFollowers(uid);
		if (followers.isEmpty()) {
			return ServerResponseEntity.ofStatus(ServerStatus.NO_FOLLOWER);
		}
		return ServerResponseEntity.ofSuccess(followers);
	}

	@GetMapping("/getFollowerByRequest")
	@Operation(summary = "用户id获取粉丝列表（极简用户）")
	@UserLoginRequired
	public ServerResponseEntity<List<SimpleUserVO>> getFollower(HttpServletRequest request) {
		String uid = JwtUtil.parseId(request);
		var followers = followService.getFollowers(uid);
		if (followers.isEmpty()) {
			return ServerResponseEntity.ofStatus(ServerStatus.NO_FOLLOWER);
		}
		return ServerResponseEntity.ofSuccess(followers);
	}

	@GetMapping("/getFollowerCount")
	@Operation(summary = "用户id获取粉丝数")
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<Long> getFollowerCount(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		if (!userService.idIsExist(uid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		return ServerResponseEntity.ofSuccess(followService.lambdaQuery().eq(Follow::getFollowedUid, uid).count());
	}

	@GetMapping("/getFollowerCountByRequest")
	@Operation(summary = "用户id获取粉丝数")
	@UserLoginRequired
	public ServerResponseEntity<Long> getFollowerCountByRequest(HttpServletRequest request) {
		String uid = JwtUtil.parseId(request);
		return ServerResponseEntity.ofSuccess(followService.lambdaQuery().eq(Follow::getFollowedUid, uid).count());
	}

	@GetMapping("/getFollowed")
	@Operation(summary = "用户id获取关注列表（极简用户）")
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<List<SimpleUserVO>> getFollowed(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		if (!userService.idIsExist(uid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		var followeds = followService.getFolloweds(uid);
		if (followeds.isEmpty()) {
			return ServerResponseEntity.ofStatus(ServerStatus.NO_FOLLOWER);
		}
		return ServerResponseEntity.ofSuccess(followeds);
	}

	@GetMapping("/getFollowedByRequest")
	@Operation(summary = "用户id获取关注列表（极简用户）")
	@UserLoginRequired
	public ServerResponseEntity<List<SimpleUserVO>> getFollowedByRequest(HttpServletRequest request) {
		String uid = JwtUtil.parseId(request);
		var followeds = followService.getFolloweds(uid);
		if (followeds.isEmpty()) {
			return ServerResponseEntity.ofStatus(ServerStatus.NO_FOLLOWER);
		}
		return ServerResponseEntity.ofSuccess(followeds);
	}

	@GetMapping("/getFollowedCount")
	@Operation(summary = "用户id获取关注数")
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<Long> getFollowedCount(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		if (!userService.idIsExist(uid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		long cnt = followService.lambdaQuery().eq(Follow::getFollowerUid, uid).count();
		return cnt > 0 ? ServerResponseEntity.ofSuccess(cnt) : ServerResponseEntity.ofStatus(ServerStatus.NO_FOLLOWED);
	}

	@GetMapping("/getFollowedCountByRequest")
	@Operation(summary = "用户id获取关注数")
	@UserLoginRequired
	public ServerResponseEntity<Long> getFollowedCountByRequest(HttpServletRequest request) {
		long cnt = followService.lambdaQuery().eq(Follow::getFollowerUid, JwtUtil.parseId(request)).count();
		return cnt > 0 ? ServerResponseEntity.ofSuccess(cnt) : ServerResponseEntity.ofStatus(ServerStatus.NO_FOLLOWED);
	}

	@GetMapping("/followUnfollow")
	@Operation(summary = "关注或取消关注")
	@Parameter(name = "followedUid", description = "被关注者的uid", required = true)
	public ServerResponseEntity<Object> followUnfollow(@NotBlank(message = Alert.USER_ID_IS_NULL) String followedUid, HttpServletRequest request) {
		String followerUid = JwtUtil.parseId(request);
		return userService.getOptById(followerUid)
				.map(user -> userService.getOptById(followedUid)
						.map(user1 -> {
							if (followerUid.equals(followedUid)) {
								return ServerResponseEntity.ofStatus(ServerStatus.CANNOT_FOLLOW_SELF);
							}
							Optional.ofNullable(followService.selectByMultiId(Follow.builder().followerUid(followerUid).followedUid(followedUid).build()))
									.ifPresentOrElse(follow -> {
												follow.setStatus(follow.getStatus() == ConstsPool.FOLLOWED ? ConstsPool.UNFOLLOWED : ConstsPool.FOLLOWED);
												followService.updateByMultiId(follow);
											}, () -> followService.save(Follow.builder().followerUid(followerUid).followedUid(followedUid).status(ConstsPool.FOLLOWED).build())
									);
							return ServerResponseEntity.ofSuccess();
						})
						.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST)))
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}
}
