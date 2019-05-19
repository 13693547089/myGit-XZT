package com.faujor.service.common.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.common.AsyncLogMapper;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.common.AsyncLogService;
import com.faujor.utils.UserCommon;

@Service("asyncLogService")
public class AsyncLogServiceImpl implements AsyncLogService {

	@Autowired
	private AsyncLogMapper asyncLogMapper;

	@Override
	public int saveAsyncLog(AsyncLog al) {
		String asyncUserName = al.getAsyncUserName();
		if (StringUtils.isEmpty(asyncUserName)) {
			SysUserDO user = UserCommon.getUser();
			al.setAsyncUser(user.getUserId());
			al.setAsyncUserName(user.getName());
		}
		al.setStartDate(new Date());
		al.setAsyncStatus("同步中");
		return asyncLogMapper.saveAsyncLog(al);
	}

	@Override
	public int updateAsyncLog(AsyncLog al) {
		al.setEndDate(new Date());
		return asyncLogMapper.updateAsyncLog(al);
	}

	@Override
	public Map<String, Object> findAsyncLogList(AsyncLog al, RowBounds rb) {
		List<AsyncLog> list = asyncLogMapper.findAsyncLogList(al, rb);
		int k = asyncLogMapper.countAsyncLog(al);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", k);
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@Override
	public void saveAsyncLog2(AsyncLog al) {
		String asyncUserName = al.getAsyncUserName();
		if (StringUtils.isEmpty(asyncUserName)) {
			SysUserDO user = UserCommon.getUser();
			al.setAsyncUser(user.getUserId());
			al.setAsyncUserName(user.getName());
		}
		asyncLogMapper.saveAsyncLog(al);
	}
}
