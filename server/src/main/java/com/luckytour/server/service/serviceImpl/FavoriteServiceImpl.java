package com.luckytour.server.service.serviceImpl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.entity.Favorite;
import com.luckytour.server.mapper.FavoriteMapper;
import com.luckytour.server.service.FavoriteService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收藏总表 服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-02-09
 */
@Service
public class FavoriteServiceImpl extends MppServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

}
