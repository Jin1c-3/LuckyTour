package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.entity.Blog;
import com.luckytour.server.entity.BlogView;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.BlogCreateRequest;
import com.luckytour.server.service.BlogService;
import com.luckytour.server.service.BlogViewService;
import com.luckytour.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@RestController
@RequestMapping("/blog")
@Tag(name = "博客接口")
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

	@Operation(summary = "通过用户id获取博客列表")
	@GetMapping("/getBlogByUid")
	public ApiResponse<List<BlogView>> getBlogByUid(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid) {
		return userService.getOptById(uid)
				.map(user -> ApiResponse.ofSuccess(blogViewService.list(new QueryWrapper<BlogView>().eq("uid", uid))))
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.USER_NOT_EXIST));
	}

	@Operation(summary = "通过博客id获取博客")
	@GetMapping("/getBlogByBid")
	public ApiResponse<BlogView> getBlogByBid(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogViewService.getOptById(bid)
				.map(ApiResponse::ofSuccess)
				.orElseGet(() -> ApiResponse.ofStatus(ApiStatus.BLOG_NOT_EXIST));
	}

	@Operation(summary = "增加博客点击量")
	@GetMapping("/addClick")
	public ApiResponse<Object> addClick(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogService.getOptById(bid)
				.map(blog -> {
					blog.setClickVolume(blog.getClickVolume() + 1);
					blogService.updateById(blog);
					return ApiResponse.ofSuccess();
				}).orElseGet(() -> ApiResponse.ofStatus(ApiStatus.BLOG_NOT_EXIST));
	}

	@PostMapping("/create")
	@Operation(summary = "创建博客")
	public <T> ApiResponse<T> create(@RequestBody @Valid BlogCreateRequest bRequest) {
		return blogService.save(new Blog().createByBlogCreateRequest(bRequest)) ? ApiResponse.ofSuccess() : ApiResponse.ofStatus(ApiStatus.BLOG_CREATE_FAIL);
	}

	@GetMapping("/title")
	@Operation(summary = "修改博客标题")
	public ApiResponse<Object> updateTitle(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid, @Pattern(regexp = Regex.BLOG_TITLE_REGEX, message = Alert.BLOG_TITLE_FORMAT_ERROR) String title) {
		return blogService.getOptById(bid)
				.map(blog -> {
					blog.setTitle(title);
					blogService.updateById(blog);
					return ApiResponse.ofSuccess();
				}).orElseGet(() -> ApiResponse.ofStatus(ApiStatus.BLOG_NOT_EXIST));
	}

}
