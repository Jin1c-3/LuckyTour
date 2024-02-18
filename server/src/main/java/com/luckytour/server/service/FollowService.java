package com.luckytour.server.service;

import com.github.jeffreyning.mybatisplus.service.IMppService;
import com.luckytour.server.entity.Follow;
import com.luckytour.server.vo.SimpleUserVO;

import java.util.List;

/**
 * <p>
 * 关注表 服务类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
public interface FollowService extends IMppService<Follow> {
	List<SimpleUserVO> getFollowers(String uid);

	List<SimpleUserVO> getFolloweds(String uid);
}
