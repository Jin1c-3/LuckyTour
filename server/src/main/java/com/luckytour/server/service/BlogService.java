package com.luckytour.server.service;

import com.luckytour.server.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
public interface BlogService extends IService<Blog> {

	boolean idIsExist(String id);

}
