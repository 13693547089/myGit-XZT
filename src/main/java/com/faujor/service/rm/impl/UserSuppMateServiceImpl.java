package com.faujor.service.rm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.rm.UserSuppMateMapper;
import com.faujor.entity.rm.UserSuppMate;
import com.faujor.service.rm.UserSuppMateService;
@Service(value="userSuppMateService")
public class UserSuppMateServiceImpl implements UserSuppMateService {

	@Autowired
	private UserSuppMateMapper userSuppMateMapper;

	@Override
	public Map<String, Object> getUserSuppMateListByPage(Map<String, Object> map) {
		List<UserSuppMate> list = userSuppMateMapper.getUserSuppMateListByPage(map);
		int count = userSuppMateMapper.getUserSuppMateListByPageCount(map);
		Map<String, Object> result = new HashMap<>();
		result.put("data", list);
		result.put("count", count);
		result.put("msg", "");
		result.put("code", 0);
		return result;
	}

	@Override
	public List<UserSuppMate> getUserSuppMateList(UserSuppMate userSuppMate) {
		return userSuppMateMapper.getUserSuppMateList(userSuppMate);
	}
}
