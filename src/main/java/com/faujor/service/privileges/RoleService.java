package com.faujor.service.privileges;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.faujor.entity.privileges.RoleDO;

/**
 * 角色相关业务部分
 */
@Service
public interface RoleService {
	RoleDO get(Long id);

	List<RoleDO> list();

	int save(RoleDO role);

	int update(RoleDO role);

	int remove(Long id);

	List<RoleDO> list(Long userId);

	/**
	 * 获取改用户的所有角色id
	 * 
	 * @param userId
	 * @return
	 */
	List<Long> roleIdsByUserId(Long userId);

	/**
	 * 判断是否包含某角色
	 * 
	 * @param params
	 * @param roleCode
	 * @param userId
	 * @return
	 */
	boolean isIncludeRole(Map<String, Object> params);
}
