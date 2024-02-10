package com.luckytour.server.service.serviceImpl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.entity.Liked;
import com.luckytour.server.mapper.LikedMapper;
import com.luckytour.server.service.LikedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
