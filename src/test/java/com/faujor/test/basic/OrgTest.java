package com.faujor.test.basic;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.service.privileges.OrgService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrgTest {
	@Autowired
	private OrgService orgService;

	@Test
	public void manageSub() {
		Map<String, Object> params = new HashMap<String, Object>();
		long ownId = 1;
		params.put("ownId", ownId);
		params.put("orgCode", "");
		params.put("isContainOwn", false);
		orgService.manageSubordinateUsers(params);
	}
}
