package com.faujor.dao.master.privilege;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.privileges.UserVO;

public interface OrgMapper {

	List<OrgDo> orgList();

	int save(OrgDo org);

	OrgDo getOrgById(long id);

	int removePsnByParentId(long parentId);

	int removeUserPsnByParentId(long parentId);

	List<OrgDo> findOrgListByParentId(long id);

	int remove(List<Long> ids);

	int update(OrgDo org);

	@Select("select t.user_id as orgId from sys_user_role t where t.role_id = #{roleId}")
	List<Long> listOrgIdByRoleId(String roleId);

	@Select("select t.sperson_id as spersonId from sys_org t left join sys_org t1 on t.sparent_id = t1.sid where t.stype = 'psn' and t1.scode = #{orgCode}")
	List<Long> userListByOrgCode(String orgCode);

	OrgDo findOrgByUserId(Long userId);

	@Select("<script> select distinct t.role_id as roleId from sys_user_role t where t.user_id in "
			+ "<foreach collection=\"list\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">"
			+ " #{item} </foreach> </script>")
	List<Long> findRoleIdsByOrgIds(List<Long> orgIds);

	@Delete("delete from sys_org where sperson_id = #{userId}")
	int removeOrgByUserId(Long userId);

	int checkCode(Map<String, Object> map);

	OrgDo findOrgByOrgCode(String orgCode);

	int batchSaveOrg(List<OrgDo> orgList);

	/**
	 * 根据条件查询出所有符合条件的人员信息
	 * 
	 * @param org
	 * @return
	 */
	List<UserDO> findOrgByParams(OrgDo org);
	
	UserDO findOrgByParams2(OrgDo org);

	/**
	 * 根据条件查询出所有符合条件的组织机构信息(按组织机构授权中使用)
	 * 
	 * @param org
	 * @return
	 */
	List<OrgDo> findOrgListByParams(OrgDo org);

	/**
	 * 根据条件查询出所有符合条件的组织机构信息(按组织机构授权中使用)
	 * 
	 * @param scodes
	 * @return
	 */
	List<OrgDo> findOrgListBySIDs(List<String> scodes);

	/**
	 * 根据类型编码获取该类型的组织机构
	 * 
	 * @param code
	 * @return
	 */
	List<OrgDo> findOrgsByOrgCode(String code);

	/**
	 * 根据参数获取用户的IDs
	 * 
	 * @param map
	 * @return
	 */
	List<UserVO> findUserIdsByParams(Map<String, Object> map);
}
