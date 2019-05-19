package com.faujor.service.demo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.task.TaskMapper2;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.ProcTestDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.entity.task.TestDO;
import com.faujor.service.demo.ProcessTestService;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service(value = "processTestService")
public class ProcessTestServiceImpl implements ProcessTestService {
	@Autowired
	private TaskMapper2 taskMapper;

	@Override
	public int saveProcessTest(TestDO td, String executors) {
		String id = td.getId();
		if (StringUtils.isEmpty(id)) {
			id = UUIDUtil.getUUID();
			td.setId(id);
		}
		TaskParamsDO tpd = new TaskParamsDO();
		tpd.setTaskName(td.getRemark());
		SysUserDO user = UserCommon.getUser();
		tpd.setSubmit(user.getUserId().toString());
		tpd.setSubmitName(user.getName());
		tpd.setStatus("save");
		tpd.setSdata1(id);
		tpd.setProcessCode("testAudit");
		tpd.setNode(1);
		// JSONArray ja = new JSONArray();
		// for (SysUserDO sud : list) {
		// JSONObject jo = new JSONObject();
		// jo.put(sud.getUserId().toString(), sud.getName());
		// ja.add(jo);
		// }
		// tpd.setExecutors(ja);
		// int k = taskService.createTask(tpd);
		// int i = taskService.doTask(id, "测试一下咯", "");
		return 0;
	}

	@Override
	public ProcTestDO findTestById(String sdata1) {
		ProcTestDO ptd = taskMapper.findTestById(sdata1);
		return ptd;
	}

	@Override
	public int saveTestInfo(TestDO td) {
		int i = 0;
		ProcTestDO test = taskMapper.findTestById(td.getId());
		if (test == null) {
			i = taskMapper.saveTestInfo(td);
		} else {
			i = taskMapper.updateTestInfo(td);
		}
		return i;
	}

}
