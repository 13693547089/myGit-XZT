package com.faujor.service.privileges.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.common.RoleMenuMapper;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.common.UserRoleMapper;
import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.dao.master.privilege.RoleMapper;
import com.faujor.entity.common.RoleMenuDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.Role;
import com.faujor.entity.privileges.RoleDO;
import com.faujor.service.privileges.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleMapper roleMapper;
	@Autowired
	RoleMenuMapper roleMenuMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	UserRoleMapper userRoleMapper;
	@Autowired
	OrgMapper orgMapper;
	private String parentIds = "";

	public List<RoleDO> list() {
		List<RoleDO> roles = roleMapper.listRole();
		return roles;
	}

	public List<RoleDO> list(Long userId) {
		List<Long> rolesIds = userRoleMapper.listRoleId(userId);
		List<RoleDO> roles = roleMapper.listRole();
		for (RoleDO roleDO : roles) {
			roleDO.setRoleSign("false");
			for (Long roleId : rolesIds) {
				if (roleDO.getRoleId() == roleId) {
					roleDO.setRoleSign("true");
					break;
				}
			}
		}
		return roles;
	}

	@Transactional
	public int save(RoleDO role) {
		int count = roleMapper.save(role);
		List<Long> menuIds = role.getMenuIds();
		Long roleId = role.getRoleId();
		List<RoleMenuDO> rms = new ArrayList<RoleMenuDO>();
		for (Long menuId : menuIds) {
			RoleMenuDO rmDo = new RoleMenuDO();
			rmDo.setRoleId(roleId);
			rmDo.setMenuId(menuId);
			rms.add(rmDo);
		}
		roleMenuMapper.removeByRoleId(roleId);
		if (rms.size() > 0) {
			roleMenuMapper.batchSave(rms);
		}
		return count;
	}

	@Transactional
	public int remove(Long id) {
		int count = roleMapper.remove(id);
		roleMenuMapper.remove(id);
		return count;
	}

	public RoleDO get(Long id) {
		RoleDO roleDO = roleMapper.get(id);
		return roleDO;
	}

	@Transactional
	public int update(RoleDO role) {
		int r = roleMapper.update(role);
		List<Long> menuIds = role.getMenuIds();
		Long roleId = role.getRoleId();
		roleMenuMapper.removeByRoleId(roleId);
		List<RoleMenuDO> rms = new ArrayList<RoleMenuDO>();
		for (Long menuId : menuIds) {
			RoleMenuDO rmDo = new RoleMenuDO();
			rmDo.setRoleId(roleId);
			rmDo.setMenuId(menuId);
			rms.add(rmDo);
		}
		roleMenuMapper.removeByRoleId(roleId);
		if (rms.size() > 0) {
			roleMenuMapper.batchSave(rms);
		}
		return r;
	}

	@Override
	public List<Long> roleIdsByUserId(Long userId) {
		OrgDo org = orgMapper.findOrgByUserId(userId);
		// List<Long> orgIds = new ArrayList<Long>();
		// orgIds.add(org.getMenuId());
		// if (org.getParentId() != 0) {
		// orgIds.add(org.getParentId());
		// parentIds(org.getParentId());
		// // 组织机构的ids
		// String[] orgId = parentIds.split(",");
		// for (int i = 0; i < orgId.length; i++) {
		// String r = orgId[i];
		// if (r != "") {
		// orgIds.add(Long.valueOf(r));
		// }
		// }
		// }
		String sfids = org.getSfids();
		String[] orgId = sfids.split("/");
		List<Long> orgIds = new ArrayList<Long>();
		for (String id : orgId) {
			if (!StringUtils.isEmpty(id)) {
				long l = Long.parseLong(id);
				orgIds.add(l);
			}
		}
		List<Long> roleIds = orgMapper.findRoleIdsByOrgIds(orgIds);
		return roleIds;
	}

	// 递归获取所有父节点
	private void parentIds(Long orgId) {
		OrgDo org = orgMapper.getOrgById(orgId);
		if (org != null && org.getParentId() != 0) {
			parentIds += org.getParentId() + ",";
			parentIds(org.getParentId());
		}
	}

	@Override
	public boolean isIncludeRole(Map<String, Object> params) {
		List<Role> list = roleMapper.isIncludeRole(params);
		if (list.size() > 0)
			return true;
		return false;
	}
}
