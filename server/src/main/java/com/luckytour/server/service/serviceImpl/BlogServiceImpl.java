package com.luckytour.server.service.serviceImpl;

import com.luckytour.server.entity.Blog;
import com.luckytour.server.mapper.BlogMapper;
import com.luckytour.server.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
