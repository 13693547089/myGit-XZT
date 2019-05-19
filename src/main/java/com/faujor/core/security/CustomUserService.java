package com.faujor.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.common.MenuMapper;
import com.faujor.dao.master.common.RoleMenuMapper;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.privileges.RoleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {
	@Autowired
	UserMapper userDao;
	@Autowired
	RoleMenuMapper roleMenuDao;
	@Autowired
	MenuMapper menuDao;
	@Autowired
	RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		SysUserDO user = userDao.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("找不到当前用户");
		}
		Long userId = user.getUserId();
		if ("supplier".equals(user.getUserType()))
			userId = user.getOrgSpersonId();
		List<Long> roleIds = roleService.roleIdsByUserId(userId);
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

		List<MenuDO> list = menuDao.findMenuListForAuthByRoleIds(roleIds);
		for (MenuDO menu : list) {
			if (menu != null && menu.getPerms() != null && !"".equals(menu.getPerms())) {
				authorities.add(new SimpleGrantedAuthority(menu.getPerms().toUpperCase()));
			}
		}
		return new User(user.getUsername(), user.getPassword(), authorities);
	}
}
