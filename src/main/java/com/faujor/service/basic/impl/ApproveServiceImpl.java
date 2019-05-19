package com.faujor.service.basic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.basic.ApproveMapper;
import com.faujor.entity.basic.ApproveDO;
import com.faujor.service.basic.ApproveService;

@Service("approveService")
public class ApproveServiceImpl implements ApproveService {
	@Autowired
	private ApproveMapper approveMapper;

	@Override
	public int saveApproveInfo(ApproveDO approve) {
		int i = approveMapper.saveApproveInfo(approve);
		return i;
	}

	@Override
	public List<ApproveDO> findApproveListByMainId(String mainId) {
		List<ApproveDO> list = approveMapper.findApproveListByMainId(mainId);
		return list;
	}

}
