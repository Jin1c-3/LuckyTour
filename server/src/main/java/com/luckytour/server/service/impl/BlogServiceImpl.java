package com.luckytour.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckytour.server.entity.Blog;
import com.luckytour.server.mapper.BlogMapper;
import com.luckytour.server.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

	private final BlogMapper blogMapper;

	@Autowired
	public BlogServiceImpl(BlogMapper blogMapper) {
		this.blogMapper = blogMapper;
	}

	@Override
	public boolean idIsExist(String id) {
		return blogMapper.exists(new QueryWrapper<>(Blog.builder().bid(Integer.parseInt(id)).build()));
	}
}
