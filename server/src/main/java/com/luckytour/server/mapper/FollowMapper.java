package com.luckytour.server.mapper;

import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.entity.Follow;
import com.luckytour.server.vo.SimpleUserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 关注表 Mapper 接口
 * </p>
 *
 * @author qing
 * @since 2024-02-10
 */
public interface FollowMapper extends MppBaseMapper<Follow> {
	@Select("SELECT f.follower_uid, f.followed_uid, f.status, u.id, u.nickname, u.avatar FROM follow f INNER JOIN user u ON f.follower_uid = u.id WHERE f.followed_uid = #{uid} AND f.status = " + ConstsPool.FOLLOWED)
	List<SimpleUserVO> getFollowersByUid(@Param("uid") String uid);

	@Select("SELECT f.follower_uid, f.followed_uid, f.status, u.id, u.nickname, u.avatar FROM follow f INNER JOIN user u ON f.followed_uid = u.id WHERE f.follower_uid = #{uid} AND f.status = " + ConstsPool.FOLLOWED)
	List<SimpleUserVO> getFollowedsByUid(@Param("uid") String uid);
}
