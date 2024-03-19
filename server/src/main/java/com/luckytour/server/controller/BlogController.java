package com.luckytour.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.common.constant.Alert;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.entity.Blog;
import com.luckytour.server.entity.BlogView;
import com.luckytour.server.exception.JsonException;
import com.luckytour.server.payload.front.BlogCreateRequest;
import com.luckytour.server.pojo.BlogContent;
import com.luckytour.server.service.BlogService;
import com.luckytour.server.service.BlogViewService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.FileUploadUtil;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.util.URIUtil;
import com.luckytour.server.vo.BlogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.Optional;

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

	private final ObjectMapper objectMapper;

	@Autowired
	public BlogController(UserService userService, BlogViewService blogViewService, BlogService blogService, ObjectMapper objectMapper) {
		this.userService = userService;
		this.blogViewService = blogViewService;
		this.blogService = blogService;
		this.objectMapper = objectMapper;
	}

	@Operation(summary = "用户id获取博客列表")
	@GetMapping("/getBlogByUid")
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<List<BlogView>> getBlogByUid(@NotBlank(message = Alert.USER_ID_IS_NULL) String uid, HttpServletRequest request) {
		if (!userService.idIsExist(uid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		return Optional.ofNullable(blogViewService.lambdaQuery().eq(BlogView::getUid, uid).list())
				.map(blogViews -> {
					blogViews.forEach(blogView -> {
								blogView.setAvatar(URIUtil.parse(request, blogView.getAvatar()));
								String content = blogView.getContent();
								try {
									List<BlogContent> blogContents = objectMapper.readValue(content, new TypeReference<List<BlogContent>>() {
									});
									blogContents.forEach(blogContent -> blogContent.setPhotos(blogContent.getPhotos().stream()
											.map(photo -> URIUtil.parse(request, photo))
											.toList()));
									blogView.setContent(objectMapper.writeValueAsString(blogContents));
								} catch (Exception e) {
									throw new JsonException(e.getMessage());
								}
							}
					);
					return blogViews;
				})
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_HAS_BO_BLOG));
	}

	@Operation(summary = "用户id获取博客列表")
	@GetMapping("/getBlogByRequest")
	public ServerResponseEntity<List<BlogView>> getBlogByRequest(HttpServletRequest request) {
		return Optional.ofNullable(blogViewService.lambdaQuery().eq(BlogView::getUid, JwtUtil.parseId(request)).list())
				.map(blogViews -> {
					blogViews.forEach(blogView -> {
								blogView.setAvatar(URIUtil.parse(request, blogView.getAvatar()));
								String content = blogView.getContent();
								try {
									List<BlogContent> blogContents = objectMapper.readValue(content, new TypeReference<List<BlogContent>>() {
									});
									blogContents.forEach(blogContent -> blogContent.setPhotos(blogContent.getPhotos().stream()
											.map(photo -> URIUtil.parse(request, photo))
											.toList()));
									blogView.setContent(objectMapper.writeValueAsString(blogContents));
								} catch (Exception e) {
									throw new JsonException(e.getMessage());
								}
							}
					);
					return blogViews;
				})
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.USER_HAS_BO_BLOG));
	}

	@Operation(summary = "博客id获取博客")
	@GetMapping("/getBlogByBid")
	@Parameter(name = "bid", description = "博客id", required = true)
	public ServerResponseEntity<BlogView> getBlogByBid(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid) {
		return blogViewService.lambdaQuery().eq(BlogView::getBid, bid).oneOpt()
				.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST));
	}

	@Operation(summary = "博客id列表获取博客列表")
	@PostMapping("/getBlogByBidList")
	public ServerResponseEntity<List<BlogView>> getBlogByBidList(@NotEmpty(message = Alert.USER_ID_IS_NULL) String[] bid) {
		return ServerResponseEntity.ofSuccess(blogViewService.lambdaQuery().in(BlogView::getBid, bid).list());
	}

	@GetMapping("/delete")
	@Operation(summary = "删除博客")
	@Parameter(name = "bid", description = "博客id", required = true)
	public ServerResponseEntity<Object> delete(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid, HttpServletRequest request) {
		String uid = JwtUtil.parseId(request);
		return blogService.lambdaUpdate().eq(Blog::getBid, bid).eq(Blog::getUid, uid).remove()
				? ServerResponseEntity.ofSuccess()
				: ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST);
	}

	@Operation(summary = "博客id增加一次点击量")
	@GetMapping("/addClick")
	@Parameter(name = "bid", description = "博客id", required = true)
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
	@Parameter(name = "bRequest", description = "博客创建请求", required = true)
	public <T> ServerResponseEntity<T> create(@RequestBody @Valid BlogCreateRequest bRequest, HttpServletRequest request) {
		/*bRequest.getContent().forEach(blogContent -> blogContent.setPhotoUrls(FileUploadUtil.storeFilesByUid(request, "", blogContent.getPhotos())));
		Blog blog = Blog.create(bRequest);
		try {
			blog.setContent(objectMapper.writeValueAsString(bRequest.getContent()));
		} catch (Exception e) {
			throw new JsonException();
		}
		return blogService.save(blog)
				? ServerResponseEntity.ofSuccess()
				: ServerResponseEntity.ofStatus(ServerStatus.BLOG_CREATE_FAIL);*/
		String uid = JwtUtil.parseId(request);
		bRequest.getContent().forEach(
				blogContent -> {
					blogContent.setPhotos(
							FileUploadUtil.saveBlogPicsByUid(
									uid,
									blogContent.getPhotos())
					);
				});
		Blog blog = Blog.create(bRequest);
		blog.setUid(uid);
		try {
			blog.setContent(objectMapper.writeValueAsString(bRequest.getContent()));
		} catch (Exception e) {
			throw new JsonException();
		}
		return blogService.save(blog)
				? ServerResponseEntity.ofSuccess()
				: ServerResponseEntity.ofStatus(ServerStatus.BLOG_CREATE_FAIL);
	}

	@GetMapping("/search")
	@Operation(summary = "搜索博客")
	@Parameter(name = "string", description = "搜索字符串", required = true)
	public ServerResponseEntity<List<BlogVO>> search(@NotBlank(message = Alert.BLOG_TITLE_IS_NULL) String string, HttpServletRequest request) {
		List<BlogView> blogViews = blogViewService.lambdaQuery().like(BlogView::getTitle, string).like(BlogView::getContent, string).list();
		if (blogViews == null) {
			return ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST);
		}
		List<BlogVO> blogVOs = blogViews.stream().map(blogView -> {
			try {
				BlogVO blogVO = BlogVO.create(blogView);
				blogVO.setAvatar(userService.getOptById(blogVO.getUid())
						.map(user -> URIUtil.parse(request, user.getAvatar()))
						.orElse(null));

				List<BlogContent> blogContents = objectMapper.readValue(blogView.getContent(), new TypeReference<List<BlogContent>>() {
				});
				blogContents.forEach(blogContent -> blogContent.setPhotos(blogContent.getPhotos().stream()
						.map(photo -> URIUtil.parse(request, photo))
						.toList()));
				blogVO.setContent(blogContents);
				blogVO.setView(blogVO.getContent().stream()
						.filter(blogContent -> blogContent.getPhotos() != null && !blogContent.getPhotos().isEmpty())
						.findFirst()
						.map(blogContent -> blogContent.getPhotos().get(0))
						.orElse(null));

				return blogVO;
			} catch (Exception e) {
				throw new JsonException(e.getMessage());
			}
		}).toList();
		return ServerResponseEntity.ofSuccess(blogVOs);
	}

	@GetMapping("/title")
	@Operation(summary = "修改博客标题")
	@Parameters({
			@Parameter(name = "bid", description = "博客id", required = true),
			@Parameter(name = "title", description = "博客标题", required = true)
	})
	public ServerResponseEntity<Object> updateTitle(@NotBlank(message = Alert.BLOG_ID_IS_NULL) String bid, @Pattern(regexp = Regex.BLOG_TITLE_REGEX, message = Alert.BLOG_TITLE_FORMAT_ERROR) String title) {
		return blogService.getOptById(bid)
				.map(blog -> {
					blog.setTitle(title);
					blogService.updateById(blog);
					return ServerResponseEntity.ofSuccess();
				}).orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.BLOG_NOT_EXIST));
	}

}
