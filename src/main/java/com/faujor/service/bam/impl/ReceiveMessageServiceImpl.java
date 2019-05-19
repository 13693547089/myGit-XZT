package com.faujor.service.bam.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.ReceiveMessageMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.entity.bam.ReceiveMessage;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.mdm.QualSuppService;

@Service(value = "receiveMessageService")
public class ReceiveMessageServiceImpl implements ReceiveMessageService {

	@Autowired
	private ReceiveMessageMapper receiveMessageMapper;
	@Autowired
	private QualSuppMapper qualSuppMapper;

	@Override
	public ReceiveMessage queryReceiveMessByReceUnit(String receUnit) {
		ReceiveMessage rece = receiveMessageMapper.queryReceiveMessByReceUnit(receUnit);
		return rece;
	}

	@Override
	public Map<String, Object> queryReceiveMessByPage(Map<String, Object> map) {
		List<ReceiveMessage> list = receiveMessageMapper.queryReceiveMessByPage(map);
		int count = receiveMessageMapper.queryReceiveMessByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", "0");
		page.put("msg", "");
		return page;
	}

	@Override
	@Transactional
	public boolean deleteReceMessById(String[] ids) {
		int i = receiveMessageMapper.deleteReceMessById(ids);
		if (i == ids.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addReceiveMessage(ReceiveMessage receMess) {
		int i = receiveMessageMapper.addReceiveMessage(receMess);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ReceiveMessage queryReceMessById(String id) {
		return receiveMessageMapper.queryReceMessById(id);
	}

	@Override
	@Transactional
	public boolean udpateReceiveMessage(ReceiveMessage receMess) {
		int i = receiveMessageMapper.udpateReceiveMessage(receMess);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> queryAllReceUnitOfReceiveMess() {
		return receiveMessageMapper.queryAllReceUnitOfReceiveMess();
	}

	@Override
	public List<QualSupp> findSuppInfo(String params) {
		List<QualSupp> list = new ArrayList<QualSupp>();
		Map<String, Object> map = new HashMap<String, Object>();
		if ("all".equals(params)) {
			map.put("all", "%DS%");
			list = qualSuppMapper.findQualSuppInfoByParams(map);
		} else if ("ds".equals(params)) {
			map.put("ds", "%DS%");
			list = qualSuppMapper.findQualSuppInfoByParams(map);
		}
		return list;
	}

	@Override
	public String queryReceUnitbyPost(Long userId) {
		List<ReceiveMessage> rmList = receiveMessageMapper.queryReceiveMessByPost(userId.toString());
		// receiveMessageMapper.queryReceUnitbyPost(userId);
		String str = "";
		if (rmList.size() > 0) {
			for (int i = 0; i < rmList.size(); i++) {
				str += rmList.get(i).getReceUnit();
				if (i < rmList.size() - 1) {
					str += "+";
				}
			}
		} else {// 查询出全部收到单位信息
			List<String> list = receiveMessageMapper.queryAllReceUnitOfReceiveMess();
			for (int i = 0; i < list.size(); i++) {
				str += list.get(i);
				if (i < list.size() - 1) {
					str += "+";
				}
			}
		}
		return str;
	}

	@Override
	public List<ReceiveMessage> findReceiveAddr() {

		return receiveMessageMapper.findReceiveAddr();
	}

}
