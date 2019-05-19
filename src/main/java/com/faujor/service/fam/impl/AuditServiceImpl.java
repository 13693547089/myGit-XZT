package com.faujor.service.fam.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.fam.AuditMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.fam.AuditMate;
import com.faujor.entity.fam.AuditMould;
import com.faujor.entity.fam.AuditOrder;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.common.CodeService;
import com.faujor.service.fam.AuditService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service("auditService")
public class AuditServiceImpl implements AuditService {
	@Autowired
	private AuditMapper auditMapper;
	@Autowired
	private CodeService codeService;
	@Autowired
	private TaskService taskService;
	@Override
	public Map<String, Object> findAuditListByParams(AuditOrder ao, RowBounds rb) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AuditOrder> list = auditMapper.findAuditListByParams(ao, rb);
		int count = auditMapper.countAuditListByParams(ao);
		map.put("data", list);
		map.put("count", count);
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@Override
	public AuditOrder findAuditById(String auditId) {
		return auditMapper.findAuditById(auditId);
	}

	@Override
	public Map<String, Object> getMateData(String suppId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AuditMate> list = auditMapper.findAuditMateByAuditId(suppId);
		map.put("data", list);
		map.put("count", list.size());
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@Override
	public Map<String, Object> getMouldData(String auditId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AuditMould> list = auditMapper.findAuditMouldByAuditId(auditId);
		map.put("data", list);
		map.put("count", list.size());
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@Override
	public int saveAuditData(AuditOrder ao, String mateList, String mouldList) {
		String auditId = ao.getId();
		// 对比已经存在的数据，进行变更或者删除或者新增处理
		List<AuditMate> mal = JsonUtils.jsonToList(mateList, AuditMate.class);
		List<String> omaIDs = auditMapper.findAuditMateIDsByAuditId(auditId);
		List<AuditMate> addMateList = new ArrayList<AuditMate>();
		for (AuditMate am : mal) {
			String amId = am.getId();
			if (omaIDs.contains(amId)) {
				// 更新处理
				// 暂时没有找到批量更新的代码
				// updMateList.add(am);
				auditMapper.updateAuditMate(am);
				omaIDs.remove(omaIDs.indexOf(amId));
			} else {
				// 新增处理
				amId = UUIDUtil.getUUID();
				am.setId(amId);
				am.setAuditId(auditId);
				addMateList.add(am);
			}
		}
		// 删除新表单表格里不存在的mate数据
		if (omaIDs.size() > 0)
			auditMapper.batchRemoveAuditMateByIDs(omaIDs);
		if (addMateList.size() > 0)
			auditMapper.batchSaveAuditMate(addMateList);
		List<AuditMould> mol = JsonUtils.jsonToList(mouldList, AuditMould.class);
		List<String> omoIDs = auditMapper.findAuditMouldIDsByAuditId(auditId);
		List<AuditMould> addMouldList = new ArrayList<AuditMould>();
		for (AuditMould am : mol) {
			String amId = am.getId();
			if (omoIDs.contains(amId)) {
				// 更新处理
				// updMouldList.add(am);
				auditMapper.updateAuditMould(am);
				omoIDs.remove(omoIDs.indexOf(amId));
			} else {
				// 新增处理
				amId = UUIDUtil.getUUID();
				am.setId(amId);
				am.setAuditId(auditId);
				addMouldList.add(am);
			}
		}
		if (omoIDs.size() > 0)
			auditMapper.batchRemoveAuditMouldByIDs(omoIDs);
		if (addMouldList.size() > 0)
			auditMapper.batchSaveAuditMould(addMouldList);
		int i = 0;
		// 稽核编码
		String code = ao.getAuditCode();
		if (StringUtils.isEmpty(code)) {
			code = codeService.getCodeByCodeType("auditCode");
			ao.setAuditCode(code);
		}
		SysUserDO user = UserCommon.getUser();
		AuditOrder auditOrder = auditMapper.findAuditById(auditId);
		if (auditOrder != null) {
			// 更新
			ao.setModifier(user.getUserId().toString());
			ao.setModifierName(user.getName());
			ao.setModifyTime(new Date());
			i += auditMapper.updateAuditOrder(ao);
		} else {
			// 保存
			ao.setCreator(user.getUserId().toString());
			ao.setCreatorName(user.getName());
			i += auditMapper.saveAuditOrder(ao);
		}
		return i;
	}

	@Override
	public int removeAuditOrder(String id) {
		auditMapper.removeAuditMateByAuditId(id);
		auditMapper.removeAuditMouldByAuditId(id);
		int k = auditMapper.removeAuditOrder(id);
		//删除任务数据
		TaskParamsDO params = new TaskParamsDO();
		params.setSdata1(id);
		taskService.removeTaskBySdata1(params );
		return k;
	}

	@Override
	public int batchRemoveAuditOrder(String rows) {
		List<AuditOrder> list = JsonUtils.jsonToList(rows, AuditOrder.class);
		List<String> ids = new ArrayList<String>();
		for (AuditOrder ao : list) {
			ids.add(ao.getId());
		}
		auditMapper.batchRemoveAuditMateByAuditIDs(ids);
		auditMapper.batchRemoveAuditMouldByAuditIDs(ids);
		int i = auditMapper.batchRemoveAuditOrder(ids);
		TaskParamsDO params = new TaskParamsDO();
		for (String id : ids) {
			params.setSdata1(id);
			taskService.removeTaskBySdata1(params );
		}
		return i;
	}

	@Override
	public int auditConfirm(String auditIds, String type) {
		SysUserDO user = UserCommon.getUser();
		int i = 0;
		if ("batch".equals(type)) {
			List<AuditOrder> list = JsonUtils.jsonToList(auditIds, AuditOrder.class);
			for (AuditOrder ao : list) {
				ao.setAuditStatus("已审核");
				ao.setConfirm(user.getUserId().toString());
				ao.setConfirmName(user.getName());
				ao.setConfirmTime(new Date());
				i += auditMapper.updateAuditOrder(ao);
			}
		} else if ("single".equals(type)) {
			AuditOrder ao = auditMapper.findAuditById(auditIds);
			ao.setAuditStatus("已审核");
			ao.setConfirm(user.getUserId().toString());
			ao.setConfirmName(user.getName());
			ao.setConfirmTime(new Date());
			i += auditMapper.updateAuditOrder(ao);
		}
		return i;
	}

	@Override
	public Map<String, Object> auditConfirmData(AuditOrder ao, RowBounds rb) {
		List<AuditOrder> list = auditMapper.auditConfirmData(ao, rb);
		int i = auditMapper.countAuditConfirmData(ao);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", i);
		map.put("msg", "");
		map.put("code", "0");
		return map;
	}

	@Override
	public int updateAuditOrder(AuditOrder order) {

		return auditMapper.updateAuditOrder(order);
	}

	@Override
	public AuditMate queryLastMonthBala(Map<String, Object> param) {
		return auditMapper.queryLastMonthBala(param);
	}

	@Override
	public boolean updateAuditSatusByAuditId(Map<String, Object> map) {
		int i = auditMapper.updateAuditSatusByAuditId(map);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

}
