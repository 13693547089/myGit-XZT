package com.faujor.test.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.dao.master.common.UserMapper;
import com.faujor.entity.common.SysUserDO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void findByLoginName() {
		// SysUserDO u = userMapper.findByUserName("001");
		// System.out.println(u.toString());
	}

	@Test
	public void list() {
		SysUserDO user = new SysUserDO();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user", user);
		List<SysUserDO> u = userMapper.list(param);

	}

}
