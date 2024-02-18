package com.luckytour.server.service.impl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.entity.Follow;
import com.luckytour.server.mapper.FollowMapper;
import com.luckytour.server.service.FollowService;
import com.luckytour.server.vo.SimpleUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 关注表 服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@Service
public class FollowServiceImpl extends MppServiceImpl<FollowMapper, Follow> implements FollowService {
	private final FollowMapper followMapper;

	@Autowired
	public FollowServiceImpl(FollowMapper followMapper) {
		this.followMapper = followMapper;
	}

	public List<SimpleUserVO> getFollowers(String uid) {
		return followMapper.getFollowersByUid(uid);
	}

	public List<SimpleUserVO> getFolloweds(String uid) {
		return followMapper.getFollowedsByUid(uid);
	}
}
