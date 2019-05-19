package com.faujor.service.mdm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.mdm.LatentPapersMapper;
import com.faujor.dao.master.mdm.LatentSuppMapper;
import com.faujor.entity.mdm.LatentPapers;
import com.faujor.entity.mdm.LatentSupp;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.mdm.LatentSuppService;
import com.faujor.service.task.TaskService;

@Service(value = "LatentSuppService")
public class LatentSuppServiceImpl implements LatentSuppService {

	@Autowired
	private LatentSuppMapper latentSuppMapper;
	@Autowired
	private LatentPapersMapper latentPapersMapper;
	@Autowired
	private TaskService taskService;
	@Override
	@Transactional
	public boolean insertLatentSupp(LatentSupp latentSupp) {
		int i = latentSuppMapper.insertLatentSupp(latentSupp);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map<String,Object> queryLatentSuppByPage(Map<String,Object> map) {
		List<LatentSupp> list = latentSuppMapper.queryLatentSuppByPage(map);
		int total = latentSuppMapper.queryLatentSuppCount(map);
		Map<String,Object> page =new HashMap<String,Object>();
		page.put("data", list);
		page.put("code", 0);
		page.put("count", total);
		return page;
	}

	@Override
	@Transactional
	public boolean insertLatentPapers(LatentPapers latentPapers) {
		int i = latentSuppMapper.insertLatentPapers(latentPapers);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public boolean deleteLatentSuppBySuppId(String[] suppId) {
		int i = latentPapersMapper.deleteLatentPapersBySuppId(suppId);
		int j = latentSuppMapper.deleteLatentSuppBySuppId(suppId);
		if(j==suppId.length){
			TaskParamsDO params = new TaskParamsDO();
			for(int k=0;k<suppId.length;k++){
				params.setSdata1(suppId[k]);
				int h = taskService.removeTaskBySdata1(params);
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	public LatentSupp queryOneLatentSuppBySuppId(String suppId) {
		LatentSupp supp = latentSuppMapper.queryOneLatentSuppBySuppId(suppId);
		return supp;
	}

	@Override
	@Transactional
	public boolean updateLatentSupp(LatentSupp latentSupp,List<LatentPapers> list) {
		int i = latentSuppMapper.updateLatentSuppBySuppId(latentSupp);
		String[] suppIds = new String[1];
		suppIds[0]=latentSupp.getSuppId();
		int j = latentPapersMapper.deleteLatentPapersBySuppId(suppIds );
		for(LatentPapers latentPapers:list){
			latentPapers.setSuppId(latentSupp.getSuppId());
			latentSuppMapper.insertLatentPapers(latentPapers);
		}
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public boolean approveLatentSupp(LatentSupp latentSupp) {
		int i = latentSuppMapper.updateLatentSuppAtAudit(latentSupp);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map<String,Object> queryLatentSuppToMini(Map<String, Object> map) {
		Map<String,Object> page =new HashMap<String,Object>();
		List<LatentSupp> list = latentSuppMapper.queryLatentSuppToMini(map);
		int count = latentSuppMapper.queryLatentSuppToMiniCount(map);
		page.put("data", list);
		page.put("msg","");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	@Transactional
	public boolean subLatentSuppToOA(LatentSupp latentSupp) {
		int i = latentSuppMapper.subLatentSuppToOA(latentSupp);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional
	public boolean buyerSendBackLatentSupp(LatentSupp latentSupp) {
		int i = latentSuppMapper.buyerSendBackLatentSupp(latentSupp);
		if(i==1){
			return true;
		}else{
			return false;
		}
	}

}
