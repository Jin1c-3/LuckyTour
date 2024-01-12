package com.luckytour.server.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckytour.server.entity.User;
import com.luckytour.server.mapper.UserMapper;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private UserMapper userMapper;

	/**
	 * 根据邮箱或者手机号查询用户
	 *
	 * @param emailOrPhone 邮箱或者手机号
	 * @return 用户，如果未找到返回null
	 */
	@Override
	public User findByEmailOrPhone(String emailOrPhone) {
		if (ValidationUtil.isMobile(emailOrPhone)) {
			return userMapper.selectOne(new QueryWrapper<User>().eq("phone", emailOrPhone));
		} else if (ValidationUtil.isEmail(emailOrPhone)) {
			return userMapper.selectOne(new QueryWrapper<User>().eq("email", emailOrPhone));
		}
		return null;
	}
}
