package com.luckytour.server.mapper;

import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.entity.Liked;
import com.luckytour.server.vo.SimpleUserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 点赞表 Mapper 接口
 * </p>
 *
 * @author qing
 * @since 2024-02-10
 */
public interface LikedMapper extends MppBaseMapper<Liked> {

	@Select("SELECT l.bid, l.uid, l.status, u.id, u.nickname, u.avatar FROM liked l INNER JOIN user u ON l.uid = u.id WHERE l.bid = #{bid} AND l.status = " + ConstsPool.LIKED)
	List<SimpleUserVO> getLikersByBid(@Param("bid") String bid);
}
