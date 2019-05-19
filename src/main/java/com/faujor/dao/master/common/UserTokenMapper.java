package com.faujor.dao.master.common;

import org.springframework.stereotype.Component;

import com.faujor.entity.common.User;


@Component
public interface UserTokenMapper {
	public User queryUser(User user);
}
