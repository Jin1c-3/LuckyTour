package com.luckytour.server.controller;

import com.luckytour.server.common.annotation.UserLoginRequired;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.entity.Liked;
import com.luckytour.server.service.BlogService;
import com.luckytour.server.service.LikedService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.SimpleUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
 * 博客赞同控制器
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
	@Operation(summary = "博客id获取给该博客点赞的用户列表（极简用户）")
	public ServerResponseEntity<List<SimpleUserVO>> getLikedBlogUserList(@Valid @NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		if (!blogService.idIsExist(bid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST);
		}
		var blogLikers = likedService.getBlogLikers(bid);
		return blogLikers.isEmpty()
				? ServerResponseEntity.ofStatus(ServerStatus.BLOG_HAS_NO_LIKE)
				: ServerResponseEntity.ofSuccess(blogLikers);
	}

	@GetMapping("/getUserLikedBlog")
	@Operation(summary = "用户id获取该用户点赞的博客列表")
	public ServerResponseEntity<List<String>> getUserLikedBlog(@Valid @NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		if (!userService.idIsExist(uid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		var likeList = likedService.lambdaQuery().eq(Liked::getUid, uid).list().stream()
				.filter(liked -> liked.getStatus() == ConstsPool.LIKED)
				.map(liked -> String.valueOf(liked.getBid()))
				.toList();
		return likeList.isEmpty()
				? ServerResponseEntity.ofStatus(ServerStatus.NO_LIKED_BLOG)
				: ServerResponseEntity.ofSuccess(likeList);
	}

	@GetMapping("/getUserLikedBlogByRequest")
	@Operation(summary = "用户id获取该用户点赞的博客列表")
	@UserLoginRequired
	public ServerResponseEntity<List<String>> getUserLikedBlog(HttpServletRequest request) {
		var likeList = likedService.lambdaQuery().eq(Liked::getUid, JwtUtil.parseId(request)).list().stream()
				.filter(liked -> liked.getStatus() == ConstsPool.LIKED)
				.map(liked -> String.valueOf(liked.getBid()))
				.toList();
		return likeList.isEmpty()
				? ServerResponseEntity.ofStatus(ServerStatus.NO_LIKED_BLOG)
				: ServerResponseEntity.ofSuccess(likeList);
	}

	@GetMapping("/likeUnliked")
	@Operation(summary = "点赞或取消点赞")
	@Parameter(name = "bid", required = true, description = "点赞的博客id")
	@UserLoginRequired
	public ServerResponseEntity<Object> likeUnliked(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid, HttpServletRequest request) {
		String uid = JwtUtil.parseId(request);
		return userService.getOptById(uid)
				.map(user -> blogService.getOptById(bid)
						.map(blog -> {
							Optional.ofNullable(likedService.selectByMultiId(Liked.builder().uid(uid).bid(Integer.parseInt(bid)).build()))
									.ifPresentOrElse(liked -> {
												liked.setStatus(liked.getStatus() == ConstsPool.LIKED ? ConstsPool.UNLIKED : ConstsPool.LIKED);
												likedService.updateByMultiId(liked);
											}, () -> likedService.save(Liked.builder().uid(uid).bid(Integer.parseInt(bid)).status(ConstsPool.LIKED).build())
									);
							return ServerResponseEntity.ofSuccess();
						})
						.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST)))
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}

}
