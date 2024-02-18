package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.entity.Blog;
import com.luckytour.server.entity.BlogView;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.payload.front.BlogCreateRequest;
import com.luckytour.server.service.BlogService;
import com.luckytour.server.service.BlogViewService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

/**
 * 博客控制器
 *
 * @author qing
 * @since 2024-02-09
 */
@RestController
@RequestMapping("/blog")
@Tag(name = "博客控制器")
@CrossOrigin
@Slf4j
@Validated
public class BlogController {

	private final UserService userService;

	private final BlogViewService blogViewService;

	private final BlogService blogService;

	@Autowired
	public BlogController(UserService userService, BlogViewService blogViewService, BlogService blogService) {
		this.userService = userService;
		this.blogViewService = blogViewService;
		this.blogService = blogService;
	}

	@Operation(summary = "用户id获取博客列表")
	@GetMapping("/getBlogByUid")
	public ServerResponseEntity<List<BlogView>> getBlogByUid(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		return userService.getOptById(uid)
				.map(user -> ServerResponseEntity.ofSuccess(blogViewService.list(new QueryWrapper<BlogView>().eq("uid", uid))))
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST));
	}

	@Operation(summary = "用户id获取博客列表")
	@GetMapping("/getBlogByRequest")
	public ServerResponseEntity<List<BlogView>> getBlogByRequest(HttpServletRequest request) {
		return getBlogByUid(JwtUtil.parseId(request));
	}

	@Operation(summary = "博客id获取博客")
	@GetMapping("/getBlogByBid")
	public ServerResponseEntity<BlogView> getBlogByBid(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogViewService.getOptById(bid)
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST));
	}

	@Operation(summary = "博客id列表获取博客列表")
	@PostMapping("/getBlogByBidList")
	public ServerResponseEntity<BlogView> getBlogByBidList(@NotEmpty(message = Alert.USER_ID_IS_NULL) String[] bid) {
		return blogViewService.getOptById(bid)
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST));
	}

	@Operation(summary = "博客id增加一次点击量")
	@GetMapping("/addClick")
	public ServerResponseEntity<Object> addClick(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogService.getOptById(bid)
				.map(blog -> {
					blog.setClickVolume(blog.getClickVolume() + 1);
					blogService.updateById(blog);
					return ServerResponseEntity.ofSuccess();
				}).orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST));
	}

	@PostMapping("/create")
	@Operation(summary = "创建博客")
	public <T> ServerResponseEntity<T> create(@RequestBody @Valid BlogCreateRequest bRequest) {
		return blogService.save(Blog.createByBlogCreateRequest(bRequest))
				? ServerResponseEntity.ofSuccess()
				: ServerResponseEntity.ofStatus(ServerStatus.BLOG_CREATE_FAIL);
	}

	@GetMapping("/title")
	@Operation(summary = "修改博客标题")
	public ServerResponseEntity<Object> updateTitle(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid, @Pattern(regexp = Regex.BLOG_TITLE_REGEX, message = Alert.BLOG_TITLE_FORMAT_ERROR) String title) {
		return blogService.getOptById(bid)
				.map(blog -> {
					blog.setTitle(title);
					blogService.updateById(blog);
					return ServerResponseEntity.ofSuccess();
				}).orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST));
	}

}
