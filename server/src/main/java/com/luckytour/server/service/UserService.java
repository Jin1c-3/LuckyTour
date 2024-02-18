package com.luckytour.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckytour.server.entity.User;

import java.util.Optional;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qing
 * @since 2024-01-12
 */
public interface UserService extends IService<User> {
	Optional<User> getOptByEmailOrPhone(String emailOrPhone);

	boolean emailOrPhoneIsExist(String emailOrPhone);

	boolean idIsExist(String id);

	boolean idIsExist(User user);

}
