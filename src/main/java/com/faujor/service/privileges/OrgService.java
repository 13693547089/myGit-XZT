package com.faujor.service.privileges;

import java.util.List;
import java.util.Map;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.Tree;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;

public interface OrgService {
	public List<OrgDo> orgList();

	public int save(OrgDo org, SysUserDO user);

	public OrgDo getOrgById(long id);

	public int remove(long id);

	public int update(OrgDo org, SysUserDO user);

	public Tree<OrgDo> getOrgSelectTree(String roleId);

	/**
	 * 某人管理的下级组织成员，参数为岗位编码 例如采购一部部长管理的采购员
	 * 
	 * @param orgCode
	 * @return
	 */
	public List<UserDO> manageOrgByCode(long personId, String orgCode, String params);

	/**
	 * 某人管理的下级组织成员，无参数 即管理的所有成员
	 * 
	 * @return
	 */
	public List<UserDO> manageOrgs(long personId);

	/**
	 * 校验编码是否重复
	 * 
	 * @param scode
	 * @param sid
	 * @return
	 */
	public int checkCode(String scode, String sid);

	/**
	 * 获取管理的下级组织
	 * 
	 * @param ownId,当前用户id
	 * @param orgCode，组织机构的部门编码，例如（采购员=PURCHAROR）
	 * @param isContainOwn，是否包含本身，true包括，false不包括
	 * @return
	 */
	public List<UserDO> manageSubordinateUsers(Map<String, Object> params);

	/**
	 * 根据条件获取组织的用户
	 * 
	 * @param org
	 * @return
	 */
	public List<UserDO> getOrgUserByCondition(OrgDo org);

	/**
	 * 根据用户id获取对应组织信息
	 * 
	 * @param userId
	 * @return
	 */
	public OrgDo findOrgByUserId(Long userId);

	/**
	 * 根据code查询出部门
	 * 
	 * @param code
	 * @return
	 */
	public List<OrgDo> findOrgsByOrgCode(String code);
}
