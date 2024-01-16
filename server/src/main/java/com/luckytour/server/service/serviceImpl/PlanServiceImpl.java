package com.luckytour.server.service.serviceImpl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.mapper.PlanMapper;
import com.luckytour.server.service.PlanService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-01-16
 */
@Service
public class PlanServiceImpl extends MppServiceImpl<PlanMapper, Plan> implements PlanService {

}
