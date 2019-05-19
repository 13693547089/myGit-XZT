package com.faujor.service.privileges.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.common.UserRoleMapper;
import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.UserData;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.privileges.SysUserService;
import com.faujor.utils.MD5Utils;

/**
 * 用户相关业务部分
 */
@Service("userService")
public class SysUserServiceImpl implements SysUserService{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;

	public Map<String, Object> selectByFy(Map<String, Object> param) {
		// bootstrap-table要求服务器返回的json须包含：totlal，rows
		Map<String, Object> result = new HashMap<String, Object>();
		int total = userMapper.findByUserNameList(null).size();
		List<SysUserDO> rows = userMapper.findByUserNameList(param);
		result.put("total", total);
		result.put("rows", rows);
		return result;
	}

	public List<SysUserDO> list(RowBounds rb, Map<String, Object> map) {

		return userMapper.userListByParams(rb, map);
	}

	public int count(Map<String, Object> map) {
		return userMapper.countUserListByParams(map);
	}

	@Transactional
	public int save(SysUserDO user) {
		String password = user.getPassword();
		String encrypt = MD5Utils.encrypt(password);
		user.setPassword(encrypt);
		user.setPlainCode(encrypt);
		int count = userMapper.save(user);
		// Long userId = user.getUserId();
		// List<Long> roles = user.getroleIds();
		// userRoleMapper.removeByUserId(userId);
		// List<UserRoleDO> list = new ArrayList<UserRoleDO>();
		// for (Long roleId : roles) {
		// UserRoleDO ur = new UserRoleDO();
		// ur.setUserId(userId);
		// ur.setRoleId(roleId);
		// list.add(ur);
		// }
		// if (list.size() > 0) {
		// userRoleMapper.batchSave(list);
		// }
		return count;
	}

	public int update(SysUserDO user) {
		String password = user.getPassword();
		String encrypt = MD5Utils.encrypt(password);
		user.setPassword(encrypt);
		user.setPlainCode(password);
		int r = userMapper.update(user);
		// Long userId = user.getUserId();
		// List<Long> roles = user.getroleIds();
		// userRoleMapper.removeByUserId(userId);
		// List<UserRoleDO> list = new ArrayList<UserRoleDO>();
		// for (Long roleId : roles) {
		// UserRoleDO ur = new UserRoleDO();
		// ur.setUserId(userId);
		// ur.setRoleId(roleId);
		// list.add(ur);
		// }
		// if (list.size() > 0) {
		// userRoleMapper.batchSave(list);
		// }
		return r;
	}

	public int remove(Long userId) {
		userRoleMapper.removeByUserId(userId);
		orgMapper.removeOrgByUserId(userId);
		return userMapper.remove(userId);
	}

	public boolean exit(Map<String, Object> params) {
		boolean exit;
		exit = userMapper.list(params).size() > 0;
		return exit;
	}

	public Set<String> listRoles(Long userId) {
		return null;
	}

	public int resetPwd(SysUserDO user) {
		String password = user.getPassword();
		String encrypt = MD5Utils.encrypt(password);
		user.setPassword(encrypt);
		user.setPlainCode(password);
		int r = userMapper.update(user);
		return r;
	}

	@Transactional
	public int batchremove(List<Long> userIds) {
		int count = userMapper.batchRemove(userIds);
		userRoleMapper.batchRemoveByUserId(userIds);
		return count;
	}

	public SysUserDO get(Long id) {
		List<Long> roleIds = userRoleMapper.listRoleId(id);
		SysUserDO user = userMapper.get(id);
		user.setroleIds(roleIds);
		return user;
	}

	public int sysUserUpdate(UserData ud) {
		int i = userMapper.updateUserMsg(ud);
		return i;
	}

	public UserData findByUserMsg(Map<String, Object> map) {
		UserData userData = userMapper.findByUserMsg(map);
		return userData;
	}

	public List<UserDO> listNotSelf(Long id) {

		return userMapper.listNotSelf(id);
	}

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @return
	 */
	public int changePwd(UserDO user) {
		return userMapper.changePwd(user);
	}

	public List<SysUserDO> findUsers() {
		SysUserDO userDO = new SysUserDO();
		return userMapper.findUsersByParams(userDO);
	}

	public List<SysUserDO> findUsersByParams(SysUserDO sysUserDO) {
		return userMapper.findUsersByParams(sysUserDO);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	public SysUserDO findUserByUsername(String username) {

		return userMapper.findUserByUsername(username);
	}
}
