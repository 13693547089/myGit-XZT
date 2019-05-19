package com.faujor.service.basic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.core.plugins.redis.RedisClient;
import com.faujor.dao.master.basic.LogMapper;
import com.faujor.entity.basic.LogDO;
import com.faujor.service.basic.LogService;

@Service("logService")
public class LogServiceImpl implements LogService {

	@Autowired
	private LogMapper logMapper;
//
//	@Autowired
//	private RedisClient redisClient;

	@Override
	public Map<String, Object> getLogPage(LogDO logDO, RowBounds rb) {
		List<LogDO> list = logMapper.findLogListByLogDO(rb, logDO);
		for (LogDO logDO2 : list) {
			String userName = logDO2.getUserName();
//			String str = redisClient.get(userName);
//			if (StringUtils.isEmpty(str)) {
//				logDO2.setIsOnline("离线");
//			} else {
//				logDO2.setIsOnline("在线");
//			}
		}
		int count = logMapper.countLogListByLogDO(logDO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("count", count);
		result.put("msg", "");
		result.put("code", "0");
		return result;
	}

	@Override
	public LogDO getLogInfoByUserName(String userName) {
		LogDO logDO = logMapper.findLogInfoByUserName(userName);
//		String str = redisClient.get(userName);
//		if (StringUtils.isEmpty(str)) {
//			logDO.setIsOnline("离线");
//		} else {
//			logDO.setIsOnline("在线");
//		}
		return logDO;
	}

	@Override
	public Map<String, Object> findLogDetailsByUserName(RowBounds rb, String userName) {
		List<LogDO> list = logMapper.findLogDetailsByUserName(rb, userName);
		int count = logMapper.countLogDetailsByUserName(userName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("count", count);
		result.put("msg", "");
		result.put("code", "0");
		return result;
	}

}
