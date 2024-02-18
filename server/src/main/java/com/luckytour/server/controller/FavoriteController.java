package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.entity.Favorite;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.service.BlogService;
import com.luckytour.server.service.FavoriteService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * 博客收藏控制器
 *
 * @author qing
 * @since 2024-02-09
 */
@RestController
@RequestMapping("/favorite")
@Tag(name = "博客收藏控制器")
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
	@Operation(summary = "用户id获取收藏列表")
	public ServerResponseEntity<List<Favorite>> getFavorByUid(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		return userService.getOptById(uid)
				.map(user -> Optional.ofNullable(favoriteService.list(new QueryWrapper<>(Favorite.builder().uid(uid).build())))
						.map(ServerResponseEntity::ofSuccess)
						.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.NO_FAVORITE))
				)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}

	@GetMapping("/getFavorByRequest")
	@Operation(summary = "用户id获取收藏列表")
	public ServerResponseEntity<List<Favorite>> getFavorByRequest(HttpServletRequest request) {
		return getFavorByUid(JwtUtil.parseId(request));
	}

	@GetMapping("/getFavorCountByBid")
	@Operation(summary = "博客id获取博客被收藏数")
	public ServerResponseEntity<Long> getFavorCountByBid(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogService.getOptById(bid)
				.map(blog -> ServerResponseEntity.ofSuccess(
						favoriteService.count(new QueryWrapper<>(Favorite.builder().bid(Integer.parseInt(bid)).build()))
				))
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST));
	}

	@GetMapping("/favorUnfavor")
	@Operation(summary = "收藏和取消收藏")
	@Parameter(name = "bid", description = "收藏的博客id", required = true)
	public ServerResponseEntity<Object> favorUnfavor(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid, HttpServletRequest request) {
		String uid = JwtUtil.parseId(request);
		return userService.getOptById(uid)
				.map(user -> blogService.getOptById(bid)
						.map(blog -> {
							Optional.ofNullable(favoriteService.selectByMultiId(Favorite.builder().uid(uid).bid(Integer.parseInt(bid)).build()))
									.ifPresentOrElse(favorite -> {
												favorite.setStatus(favorite.getStatus() == ConstsPool.FAVOR ? ConstsPool.UNFAVOR : ConstsPool.FAVOR);
												favoriteService.saveOrUpdateByMultiId(favorite);
											}, () -> favoriteService.save(Favorite.builder().uid(uid).bid(Integer.parseInt(bid)).status(ConstsPool.FAVOR).build())
									);
							return ServerResponseEntity.ofSuccess();
						})
						.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST)))
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}

}
