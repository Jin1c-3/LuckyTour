package com.luckytour.server.mapstruct;

import com.luckytour.server.entity.User;
import com.luckytour.server.vo.OnlineUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qing
 * @date Created in 2023/8/4 20:24
 */
@Mapper
public interface IUserMapper {
	IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

	OnlineUser User2OnlineUser(User user);
}
