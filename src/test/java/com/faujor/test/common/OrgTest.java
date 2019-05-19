package com.faujor.test.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.privileges.OrgService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrgTest {
	@Autowired
	private OrgMapper orgMapper;
	
	@Autowired
	private OrgService orgService;

	@Test
	public void testRecursion() {
		String result = findSID(100);
		Map<String, String> map = test(100);
		System.out.println(result);
	}

	public String findSID(long id) {
		String result = "";
		List<OrgDo> childList = orgMapper.findOrgListByParentId(id);
		for (OrgDo org : childList) {
			long orgId = org.getMenuId();
			result += ",'" + orgId + "'";
			result += findSID(orgId);
		}
		return result;
	}

	public Map<String, String> test(long id) {
		Map<String, String> map = new HashMap<String, String>();
		String orgIds = map.get("orgIds");
		String userIds = map.get("userIds");
		List<OrgDo> list = orgMapper.findOrgListByParentId(id);
		for (OrgDo org : list) {
			if ("psn".equals(org.getStype())) {
				userIds += ",'" + org.getSpersonId() + "'";
			}
			orgIds += ",'" + org.getMenuId() + "'";
			map.put("orgIds", orgIds);
			map.put("userIds", userIds);
			orgIds += test(org.getMenuId()).get("orgIds");
		}
		return map;
	}
	
	
	@Test
	public void manageOrgs() {
		List<UserDO> list = orgService.manageOrgByCode(1, "PURCHAROR",null);
		for(UserDO u:list){
			System.out.println(u);
		}
	}
}
