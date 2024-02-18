package com.luckytour.server.service.impl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.entity.Liked;
import com.luckytour.server.mapper.LikedMapper;
import com.luckytour.server.service.LikedService;
import com.luckytour.server.vo.SimpleUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 点赞表 服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@Service
public class LikedServiceImpl extends MppServiceImpl<LikedMapper, Liked> implements LikedService {

	private final LikedMapper likedMapper;

	@Autowired
	public LikedServiceImpl(LikedMapper likedMapper) {
		this.likedMapper = likedMapper;
	}

	public List<SimpleUserVO> getBlogLikers(String bid) {
		return likedMapper.getLikersByBid(bid);
	}
}
