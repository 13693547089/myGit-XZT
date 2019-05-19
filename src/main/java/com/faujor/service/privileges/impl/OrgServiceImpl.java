package com.faujor.service.privileges.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.common.KvSequenceMapper;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.common.UserRoleMapper;
import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.entity.common.KvSequenceDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.Tree;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.common.KvSequenceService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.BuildTree;
import com.faujor.utils.UUIDUtil;

@Service(value = "orgService")
public class OrgServiceImpl implements OrgService {

	@Autowired
	private OrgMapper orgMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private KvSequenceService kvService;
	@Autowired
	private KvSequenceMapper kvMapper;

	// 子节点
	List<UserDO> childUser = null;

	@Override
	public List<OrgDo> orgList() {
		return orgMapper.orgList();
	}

	@Override
	public int save(OrgDo org, SysUserDO userDO) {
		KvSequenceDO kv1 = kvService.getKvSequenceByKeyName("orgId");
		long orgId = 1;
		if (kv1 == null) {
			kv1 = new KvSequenceDO();
			kv1.setId(UUIDUtil.getUUID());
			kv1.setKeyName("orgId");
			kv1.setValName(orgId);
		} else {
			orgId = kv1.getValName();
			orgId += 1;
			kv1.setValName(orgId);
		}
		org.setMenuId(orgId);
		String stype = org.getStype();
		// 创建的是人员
		if ("psn".equals(stype)) {
			KvSequenceDO kv = kvService.getKvSequenceByKeyName("userCode");
			long v = 0;
			if (kv == null) {
				kv = new KvSequenceDO();
				kv.setId(UUIDUtil.getUUID());
				kv.setKeyName("userCode");
				kv.setValName(v);
			} else {
				v = kv.getValName();
				v += 1;
				kv.setValName(v);
			}
			SysUserDO user = new SysUserDO();
			user.setUserId(v);
			user.setName(org.getSname());
			user.setSuppNo(org.getSuppNo());
			user.setStatus(userDO.getStatus());
			user.setUserType("user");
			user.setPassword("123456");
			user.setLeader(userDO.getLeader());
			user.setUsername(org.getScode());
			int k = userMapper.save(user);
			if (k > 0) {
				if (v == 0) {
					kvMapper.save(kv);
				} else {
					kvMapper.update(kv);
				}
			}
			org.setSpersonId(user.getUserId());
		}
		// 设置全路径
		org = setFparentInfo(org);
		int i = orgMapper.save(org);
		if (i > 0) {
			if (orgId == 1) {
				kvMapper.save(kv1);
			} else {
				kvMapper.update(kv1);
			}

		}
		return i;
	}

	/**
	 * 获取父级组织机构的信息 包括SFID,SFNAME,SFCODE
	 * 
	 * @param parentId
	 * @return
	 */
	public OrgDo setFparentInfo(OrgDo org) {
		long parentId = org.getParentId();
		OrgDo org1 = orgMapper.getOrgById(parentId);
		String sfid = org.getMenuId() + "." + org.getStype();
		String sfname = org.getSname() + "." + org.getStype();
		String sfcode = org.getScode() + "." + org.getStype();
		String sfids = org.getMenuId() + "";
		String sfnames = org.getSname();
		String sfcodes = org.getScode();
		if (org1 != null) {
			sfid = org1.getSfid() + "/" + sfid;
			sfname = org1.getSfname() + "/" + sfname;
			sfcode = org1.getSfcode() + "/" + sfcode;
			sfids = org1.getSfids() + "/" + sfids;
			sfnames = org1.getSfnames() + "/" + sfnames;
			sfcodes = org1.getSfcodes() + "/" + sfcodes;
		}
		org.setSfid(sfid);
		org.setSfcode(sfcode);
		org.setSfname(sfname);
		org.setSfids(sfids);
		org.setSfcodes(sfcodes);
		org.setSfnames(sfnames);
		return org;
	}

	@Override
	public OrgDo getOrgById(long id) {
		OrgDo org = new OrgDo();
		org = orgMapper.getOrgById(id);
		return org;
	}

	@Override
	public int remove(long id) {
		// 在org表中需要删除的id
		List<Long> ids = new ArrayList<Long>();
		OrgDo orgDo = orgMapper.getOrgById(id);
		if ("psn".equals(orgDo.getStype())) {
			userRoleMapper.removeByUserId(orgDo.getSpersonId());
			userMapper.remove(orgDo.getSpersonId());
		} else {
			String result = findSID(id);
			String[] res = result.split(",");
			for (String str : res) {
				if (!"".equals(str) && str != null) {
					Long r = Long.valueOf(str);
					ids.add(r);
				}
			}
		}
		ids.add(id);
		int k = orgMapper.remove(ids);
		return k;
	}

	/**
	 * 通过递归查询出树下所有子id
	 */
	public String findSID(long id) {
		String result = "";
		List<OrgDo> childList = orgMapper.findOrgListByParentId(id);
		for (OrgDo org : childList) {
			long orgId = org.getMenuId();
			if ("psn".equals(org.getStype())) {
				// 如果是人员类型的，则直接先把人员表的数据删除
				long psnId = org.getSpersonId();
				userRoleMapper.removeByUserId(psnId);
				userMapper.remove(psnId);
			}
			result += "," + orgId;
			result += findSID(orgId);
		}
		return result;
	}

	@Override
	public int update(OrgDo org, SysUserDO userDO) {
		org = setFparentInfo(org);
		String stype = org.getStype();
		// 重新计算sf
		org = setFparentInfo(org);
		if ("psn".equals(stype)) {
			// 人员，同步更新用户表
			SysUserDO user = userMapper.findUserById(org.getSpersonId());
			user.setUsername(org.getScode());
			user.setSuppNo(org.getSuppNo());
			user.setName(org.getSname());
			user.setLeader(userDO.getLeader());
			user.setStatus(userDO.getStatus());
			userMapper.update(user);
		}
		int i = orgMapper.update(org);
		return i;
	}

	@Override
	public Tree<OrgDo> getOrgSelectTree(String roleId) {
		// 根据roleId查询权限
		List<Long> orgIds = orgMapper.listOrgIdByRoleId(roleId);
		List<Tree<OrgDo>> trees = new ArrayList<Tree<OrgDo>>();
		List<OrgDo> orgDOs = orgMapper.orgList();
		for (OrgDo orgDo : orgDOs) {
			Tree<OrgDo> tree = new Tree<OrgDo>();
			long menuId = orgDo.getMenuId();
			tree.setId(String.valueOf(menuId));
			tree.setParentId(String.valueOf(orgDo.getParentId()));
			tree.setText(orgDo.getSname());
			Map<String, Object> state = new HashMap<String, Object>();
			if (orgIds.contains(menuId)) {
				state.put("selected", true);
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为0，根据数据库实际情况调整
		Tree<OrgDo> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public List<UserDO> manageSubordinateUsers(Map<String, Object> params) {
		// 所要查询的人员
		long ownId = (Long) params.get("ownId");
		UserDO user = userMapper.findUserDOById(ownId);
		// 管理的哪一类型的人员(采购员，物料部等)
		String orgCode = (String) params.get("orgCode");
		// if (!StringUtils.isEmpty(orgCode))
		// orgCode = "%" + orgCode + "%";
		// 查询出待递归的人员信息
		OrgDo org = new OrgDo();
		List<UserDO> userList = orgMapper.findOrgByParams(org);

		childUser = new ArrayList<UserDO>();
		List<UserDO> child = managePubUsers(userList, ownId);
		// 是否包含自己
		boolean isContainOwn = (boolean) params.get("isContainOwn");
		if (!StringUtils.isEmpty(orgCode)) {
			List<UserDO> result = new ArrayList<UserDO>();
			for (UserDO userDO : child) {
				String sfcodes = userDO.getSfcodes();
				if (sfcodes.contains(orgCode))
					result.add(userDO);
			}
			if (isContainOwn)
				result.add(user);
			return result;
		}
		if (isContainOwn)
			child.add(user);
		return child;
	}

	@Override
	public List<UserDO> manageOrgByCode(long personId, String orgCode, String params) {
		List<Long> spersonIds = orgMapper.userListByOrgCode(orgCode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("params", params);
		List<UserDO> userList = userMapper.findUserListByParams(map);
		childUser = new ArrayList<UserDO>();
		List<UserDO> child = manageUsers(userList, spersonIds, personId);
		return child;
	}

	@Override
	public List<UserDO> manageOrgs(long id) {
		List<UserDO> userList = userMapper.findUserList();
		childUser = new ArrayList<UserDO>();
		List<UserDO> child = managePubUsers(userList, id);
		return child;
	}

	// 遍历用户表获取所管理的下级组织
	public List<UserDO> managePubUsers(List<UserDO> users, long leader) {
		for (UserDO u : users) {
			if (u != null && u.getLeader() == leader) {
				// 遍历下级节点
				managePubUsers(users, u.getId());
				childUser.add(u);
			}
		}
		return childUser;
	}

	// 遍历用户表获取所管理的下级组织
	public List<UserDO> manageUsers(List<UserDO> users, List<Long> spersonIds, long leader) {
		for (UserDO u : users) {
			if (u.getLeader() == leader && spersonIds.contains(u.getId())) {
				// 遍历下级节点
				manageUsers(users, spersonIds, u.getId());
				childUser.add(u);
			}
		}
		return childUser;
	}

	@Override
	public int checkCode(String scode, String sid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scode", scode);
		map.put("sid", sid);
		int i = orgMapper.checkCode(map);
		return i;
	}

	/**
	 * 根据条件获取组织的用户
	 * 
	 * @param org
	 * @return
	 */
	public List<UserDO> getOrgUserByCondition(OrgDo org) {
		return orgMapper.findOrgByParams(org);
	}

	/**
	 * 根据用户ID 获取对应组织信息
	 * @param userId
	 * @return
	 */
	@Override
	public OrgDo findOrgByUserId(Long userId){
		return orgMapper.findOrgByUserId(userId);
	}

	@Override
	public List<OrgDo> findOrgsByOrgCode(String code) {
		return orgMapper. findOrgsByOrgCode(code);
	}
}
