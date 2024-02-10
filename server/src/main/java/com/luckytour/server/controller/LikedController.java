package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.entity.Liked;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.BlogLikeRequest;
import com.luckytour.server.service.BlogService;
import com.luckytour.server.service.LikedService;
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
 * 点赞表 前端控制器
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@RestController
@RequestMapping("/liked")
@Tag(name = "博客赞同接口")
@CrossOrigin
@Slf4j
@Validated
public class LikedController {

	private final LikedService likedService;

	private final BlogService blogService;

	private final UserService userService;

	@Autowired
	public LikedController(LikedService likedService, BlogService blogService, UserService userService) {
		this.likedService = likedService;
		this.blogService = blogService;
		this.userService = userService;
	}

	@GetMapping("/getLikedBlogUserList")
	@Operation(summary = "获取给博客点赞的用户列表")
	public ApiResponse<List<String>> getLikedBlogUserList(@Valid @NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogService.getOptById(bid)
				.map(blog -> ApiResponse.ofSuccess(
						likedService.list(new QueryWrapper<>(Liked.builder().bid(Integer.parseInt(bid)).build())).stream()
								.map(Liked::getUid).toList()))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.BLOG_NOT_EXIST));
	}

	@GetMapping("/getUserLikedBlog")
	@Operation(summary = "获取用户点赞的博客列表")
	public ApiResponse<List<String>> getUserLikedBlog(@Valid @NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		return userService.getOptById(uid)
				.map(user -> ApiResponse.ofSuccess(
						likedService.list(new QueryWrapper<>(Liked.builder().uid(uid).build())).stream()
								.map(liked -> String.valueOf(liked.getBid())).toList()))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@PostMapping("/likeUnliked")
	@Operation(summary = "点赞")
	public ApiResponse<Object> likeUnliked(@RequestBody @Valid BlogLikeRequest lbRequest) {
		return userService.getOptById(lbRequest.getUid())
				.map(user -> blogService.getOptById(lbRequest.getBid())
						.map(blog -> {
							Optional.ofNullable(likedService.selectByMultiId(new Liked().createByBlogLikeRequest(lbRequest)))
									.ifPresentOrElse(liked -> {
												liked.setStatus(liked.getStatus() == ConstsPool.LIKED ? ConstsPool.UNLIKED : ConstsPool.LIKED);
												likedService.updateById(liked);
											}, () -> likedService.save(Liked.builder().uid(lbRequest.getUid()).bid(Integer.parseInt(lbRequest.getBid())).status(ConstsPool.LIKED).build())
									);
							return ApiResponse.ofSuccess();
						})
						.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.BLOG_NOT_EXIST)))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

}
