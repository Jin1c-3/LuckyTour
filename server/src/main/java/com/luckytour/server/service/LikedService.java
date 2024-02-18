package com.luckytour.server.service;

import com.github.jeffreyning.mybatisplus.service.IMppService;
import com.luckytour.server.entity.Liked;
import com.luckytour.server.vo.SimpleUserVO;

import java.util.List;

/**
 * <p>
 * 点赞表 服务类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
public interface LikedService extends IMppService<Liked> {
	List<SimpleUserVO> getBlogLikers(String bid);
}
