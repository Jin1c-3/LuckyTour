package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.entity.Favorite;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.BlogFavorRequest;
import com.luckytour.server.service.BlogService;
import com.luckytour.server.service.FavoriteService;
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
 * 收藏总表 前端控制器
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@RestController
@RequestMapping("/favorite")
@Tag(name = "博客收藏接口")
@CrossOrigin
@Slf4j
@Validated
public class FavoriteController {

	private final FavoriteService favoriteService;

	private final UserService userService;

	private final BlogService blogService;

	@Autowired
	public FavoriteController(FavoriteService favoriteService, UserService userService, BlogService blogService) {
		this.favoriteService = favoriteService;
		this.userService = userService;
		this.blogService = blogService;
	}

	@GetMapping("/getFavorByUid")
	@Operation(summary = "获取用户收藏列表")
	public ApiResponse<List<Favorite>> getFavorByUid(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		return userService.getOptById(uid)
				.map(user -> ApiResponse.ofSuccess(favoriteService.list(new QueryWrapper<>(Favorite.builder().uid(uid).build()))))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@GetMapping("/getFavorCountByBid")
	@Operation(summary = "获取博客收藏数量")
	public ApiResponse<Long> getFavorCountByBid(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogService.getOptById(bid)
				.map(blog -> ApiResponse.ofSuccess(favoriteService.count(new QueryWrapper<>(Favorite.builder().bid(Integer.parseInt(bid)).build()))))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.BLOG_NOT_EXIST));
	}

	@PostMapping("/favorUnfavor")
	@Operation(summary = "收藏")
	public ApiResponse<Object> favorUnfavor(@Valid @RequestBody BlogFavorRequest request) {
		return userService.getOptById(request.getUid())
				.map(user -> blogService.getOptById(request.getBid())
						.map(blog -> {
							Optional.ofNullable(favoriteService.selectByMultiId(new Favorite().createByBlogFavorRequest(request)))
									.ifPresentOrElse(favorite -> {
												favorite.setStatus(favorite.getStatus() == ConstsPool.FAVOR ? ConstsPool.UNFAVOR : ConstsPool.FAVOR);
												favoriteService.saveOrUpdateByMultiId(favorite);
											}, () -> favoriteService.save(Favorite.builder().uid(request.getUid()).bid(Integer.parseInt(request.getBid())).status(ConstsPool.FAVOR).build())
									);
							return ApiResponse.ofSuccess();
						})
						.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.BLOG_NOT_EXIST)))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

}
