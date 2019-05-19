package com.faujor.dao.master.privilege;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.privileges.AssignDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.Role;
import com.faujor.entity.privileges.RoleDO;

public interface AssignMapper {

	List<OrgDo> assignListByRoleId(RowBounds rb, RoleDO role);

	int updateByRoleId(AssignDO assign);

	@Select("select t.user_id as orgIs from sys_user_role t where t.role_id = #{roleId}")
	List<Long> assignOrgIdsByRoleId(long roleId);

	int updateAssignList(List<AssignDO> list);

	int removeByOrgIds(Map<String, Object> params);

	List<Role> assignListByOrgId(long orgId);

	@Select("select t.role_id as roleId from sys_user_role t where t.user_id = #{orgId}")
	List<Long> assignRoleIdsByOrgId(long orgId);

	int removeByRoleIds(Map<String, Object> params);

	int countAssignListByRoleId(RoleDO role);
	
}
