package com.faujor.service.privileges;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.alibaba.fastjson.JSONObject;
import com.faujor.entity.common.LayuiTree;
import com.faujor.entity.privileges.AssignDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.RoleDO;

public interface AssignService {
	Map<String, Object> roleList(RoleDO role, RowBounds rb);

	Map<String, Object> assignListByRoleId(RowBounds rb, RoleDO role);

	int updateByRoleId(AssignDO assign);

	int removeByOrgIds(List<Long> orgIds, long roleId);

	LayuiTree<OrgDo> orgList(String orgName);

	Map<String, Object> assignListByOrgId(String orgId);

	List<JSONObject> roleSelected(long orgId);

	int updateByOrgId(long orgId, String roleIds);
	
	int removeByRoleIds(List<Long> roleIds, long orgId);
}
