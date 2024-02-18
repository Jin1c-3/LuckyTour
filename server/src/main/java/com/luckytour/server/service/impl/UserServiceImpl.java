package com.luckytour.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.entity.User;
import com.luckytour.server.mapper.UserMapper;
import com.luckytour.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qing
 * @since 2024-01-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	private final UserMapper userMapper;

	@Autowired
	public UserServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	/**
	 * 根据邮箱或者手机号查询用户
	 *
	 * @param emailOrPhone 邮箱或者手机号
	 * @return 用户，如果未找到返回null
	 */
	@Override
	public Optional<User> getOptByEmailOrPhone(String emailOrPhone) {
		if (Regex.isMobile(emailOrPhone)) {
			return Optional.ofNullable(userMapper.selectOne(new QueryWrapper<>(User.builder().phone(emailOrPhone).build())));
		} else if (Regex.isEmail(emailOrPhone)) {
			return Optional.ofNullable(userMapper.selectOne(new QueryWrapper<>(User.builder().email(emailOrPhone).build())));
		} else {
			return Optional.empty();
		}
	}

	public boolean emailOrPhoneIsExist(String emailOrPhone) {
		if (Regex.isMobile(emailOrPhone)) {
			return userMapper.exists(new QueryWrapper<>(User.builder().phone(emailOrPhone).build()));
		} else if (Regex.isEmail(emailOrPhone)) {
			return userMapper.exists(new QueryWrapper<>(User.builder().email(emailOrPhone).build()));
		}
		return false;
	}

	/**
	 * 根据id查询用户是否存在
	 *
	 * @param id 用户id
	 * @return 是否存在
	 */
	@Override
	public boolean idIsExist(String id) {
		return userMapper.selectById(id) != null;
	}

	/**
	 * 查询用户是否存在
	 *
	 * @param user 用户
	 * @return 是否存在
	 */
	@Override
	public boolean idIsExist(User user) {
		return idIsExist(user.getId());
	}
}
