package com.faujor.service.privileges;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.UserData;
import com.faujor.entity.privileges.UserDO;

/**
 * 用户相关业务部分
 */
public interface SysUserService {

	public Map<String, Object> selectByFy(Map<String, Object> param);

	public List<SysUserDO> list(RowBounds rb, Map<String, Object> map);

	public int count(Map<String, Object> map);

	public int save(SysUserDO user);

	public int update(SysUserDO user);

	public int remove(Long userId);

	public boolean exit(Map<String, Object> params);

	public Set<String> listRoles(Long userId);

	public int resetPwd(SysUserDO user);

	public int batchremove(List<Long> userIds);

	public SysUserDO get(Long id);

	public int sysUserUpdate(UserData ud);

	public UserData findByUserMsg(Map<String, Object> map);

	public List<UserDO> listNotSelf(Long id);

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @return
	 */
	public int changePwd(UserDO user);

	public List<SysUserDO> findUsers();

	public List<SysUserDO> findUsersByParams(SysUserDO sysUserDO);

	/**
	 * 获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	public SysUserDO findUserByUsername(String username);
}
