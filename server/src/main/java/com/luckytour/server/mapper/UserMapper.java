package com.luckytour.server.mapper;

import com.luckytour.server.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qing
 * @since 2024-01-12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
