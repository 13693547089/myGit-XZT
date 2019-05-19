package com.faujor.utils;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.faujor.dao.master.common.UserMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.UserDO;

/**
 * 获取当前用户
 */
@Component
public class UserCommon {
	@Autowired
	private UserMapper userDao;
	private static UserMapper userDaoStatic;

	@PostConstruct
	public void init() {
		userDaoStatic = userDao;
	}

	public static SysUserDO getUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SysUserDO user = userDaoStatic.findByUserName(userDetails.getUsername());
		return user;
	}

	public static List<UserDO> findUserList() {
		return userDaoStatic.findUserList();
	}
	
	/**
	 * 根据用户名获取用户信息
	 * @param username
	 * @return
	 */
	public static SysUserDO getUserByUsername(String username) {
		SysUserDO user = userDaoStatic.findByUserName(username);
		return user;
	}
}
