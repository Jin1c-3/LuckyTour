package com.luckytour.server.service.serviceImpl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.entity.Follow;
import com.luckytour.server.mapper.FollowMapper;
import com.luckytour.server.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
