package com.faujor.service.privileges.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.common.UserTokenMapper;
import com.faujor.entity.common.User;
import com.faujor.service.privileges.UserTokenService;

/**
 * 通过  存储和验证 token 的实现类
 * @author ScienJus
 * @date 2015/7/31.
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {
	@Autowired
	private UserTokenMapper userTokenMapper;

	@Override
	public User login(User user) {
	   User userInfo=userTokenMapper.queryUser(user);
	   return userInfo;
	}
	

}