package com.faujor.service.privileges.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.privilege.AssignMapper;
import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.dao.master.privilege.RoleMapper;
import com.faujor.entity.common.LayuiTree;
import com.faujor.entity.privileges.AssignDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.Role;
import com.faujor.entity.privileges.RoleDO;
import com.faujor.service.privileges.AssignService;
import com.faujor.service.privileges.RoleService;
import com.faujor.utils.BuildLayuiTree;

@Service(value = "assignService")
public class AssignServiceImpl implements AssignService {
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private AssignMapper assignMapper;
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private RoleService roleService;

	@Override
	public Map<String, Object> roleList(RoleDO role, RowBounds rb) {
		List<RoleDO> list = roleMapper.roleListByPage(rb, role);
		int count = roleMapper.countRoleListByPage(role);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		map.put("count", count);
		map.put("data", list);
		return map;
	}

	@Override
	public Map<String, Object> assignListByRoleId(RowBounds rb, RoleDO role) {
		List<OrgDo> list = assignMapper.assignListByRoleId(rb, role);
		int count = assignMapper.countAssignListByRoleId(role);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "");
		map.put("count", count);
		map.put("data", list);
		return map;
	}

	@Override
	public int updateByRoleId(AssignDO assign) {
		long roleId = assign.getRoleId();
		// 之前分配好的
		List<Long> assigned = assignMapper.assignOrgIdsByRoleId(roleId);
		// 现在分配的
		List<Long> assigning = assign.getOrgIds();
		// 新增的
		List<AssignDO> addList = new ArrayList<AssignDO>();
		// 删除的
		List<Long> delList = new ArrayList<Long>();
		// 之前选中，之后没有了，需要删除的
		for (long a : assigned) {
			if (!assigning.contains(a)) {
				delList.add(a);
			}
		}
		for (Long orgId : assigning) {
			if (!assigned.contains(orgId)) {
				AssignDO assignDO = new AssignDO();
				assignDO.setRoleId(roleId);
				assignDO.setOrgId(orgId);
				addList.add(assignDO);
			}
		}
		if (delList.size() > 0) {
			removeByOrgIds(delList, roleId);
		}
		if (addList.size() > 0) {
			return assignMapper.updateAssignList(addList);
		} else {
			return -1;
		}
	}

	@Override
	public int removeByOrgIds(List<Long> orgIds, long roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		params.put("orgIds", orgIds);
		return assignMapper.removeByOrgIds(params);
	}

	@Override
	public LayuiTree<OrgDo> orgList(String orgName) {
		List<LayuiTree<OrgDo>> trees = new ArrayList<LayuiTree<OrgDo>>();
		List<OrgDo> orgDos = new ArrayList<OrgDo>();
		boolean spread = false;
		if (StringUtils.isEmpty(orgName)) {
			orgDos = orgMapper.orgList();
		} else {
			// 处理获取到数据集，并将展开设置为true
			OrgDo org = new OrgDo();
			orgName = "%" + orgName + "%";
			org.setSname(orgName);
			List<OrgDo> list = orgMapper.findOrgListByParams(org);
			List<String> sids = new ArrayList<String>();
			Set<String> set = new HashSet<String>();
			if (list.size() > 0) {
				for (OrgDo orgdo : list) {
					String sid = orgdo.getMenuId() + "";
					sids.add(sid);
					set.add(sid);
					// 不带后缀的sfcodes
					String sfids = orgdo.getSfids();
					String[] ids = sfids.split("/");
					for (String id : ids) {
						if (set.add(id))
							sids.add(id);
					}

				}
				orgDos = orgMapper.findOrgListBySIDs(sids);
			}
			spread = true;
		}
		for (OrgDo sysMenuDO : orgDos) {
			LayuiTree<OrgDo> tree = new LayuiTree<OrgDo>();
			tree.setId(String.valueOf(sysMenuDO.getMenuId()));
			tree.setParentId(String.valueOf(sysMenuDO.getParentId()));
			tree.setName(sysMenuDO.getSname());
			tree.setSpread(spread);
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		LayuiTree<OrgDo> q = BuildLayuiTree.build(trees, "组织机构");
		return q;
	}

	@Override
	public Map<String, Object> assignListByOrgId(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		if (!"".equals(orgId)) {
			long org = Long.parseLong(orgId);
			List<Role> list = assignMapper.assignListByOrgId(org);
			map.put("data", list);
		}
		map.put("code", "0");
		map.put("msg", "");
		map.put("count", "");
		return map;
	}

	@Override
	public List<JSONObject> roleSelected(long orgId) {
		List<Long> selected = assignMapper.assignRoleIdsByOrgId(orgId);
		List<RoleDO> list = roleService.list();
		List<JSONObject> result = new ArrayList<JSONObject>();
		for (RoleDO r : list) {
			JSONObject json = new JSONObject();
			if (selected.contains(r.getRoleId())) {
				json.put("LAY_CHECKED", true);
			} else {
				json.put("LAY_CHECKED", false);
			}
			json.put("id", r.getRoleId());
			json.put("roleName", r.getRoleName());
			json.put("roleSign", r.getRoleSign());
			json.put("remark", r.getRemark());
			result.add(json);
		}
		return result;
	}

	@Override
	public int updateByOrgId(long orgId, String roleIds) {
		// 刚刚分配的
		String[] assign = roleIds.split(",");
		// 已经分配的
		List<Long> assigned = assignMapper.assignRoleIdsByOrgId(orgId);
		// 需要新增的
		List<AssignDO> addList = new ArrayList<AssignDO>();
		for (String str : assign) {
			if (!"".equals(str)) {
				long roleId = Long.parseLong(str);
				if (!assigned.contains(roleId)) {
					AssignDO a = new AssignDO();
					a.setRoleId(roleId);
					a.setOrgId(orgId);
					addList.add(a);
				}
			}
		}
		// 需要删除的
		List<Long> delList = new ArrayList<Long>();
		for (long l : assigned) {
			if (!roleIds.contains(String.valueOf(l))) {
				delList.add(l);
			}
		}
		int r = 0;
		if (delList.size() > 0) {
			r += removeByRoleIds(delList, orgId);
		}
		if (addList.size() > 0) {
			r += assignMapper.updateAssignList(addList);
		}
		return r;
	}

	@Override
	public int removeByRoleIds(List<Long> roleIds, long orgId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("roleIds", roleIds);
		return assignMapper.removeByRoleIds(params);
	}

}
