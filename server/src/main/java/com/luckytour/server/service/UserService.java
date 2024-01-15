package com.luckytour.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckytour.server.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qing
 * @since 2024-01-12
 */
public interface UserService extends IService<User> {
	User findByEmailOrPhone(String emailOrPhone);

}
