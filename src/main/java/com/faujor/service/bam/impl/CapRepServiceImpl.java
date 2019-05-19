package com.faujor.service.bam.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.master.bam.CapRepMapper;
import com.faujor.dao.master.bam.CapRepMateMapper;
import com.faujor.dao.master.bam.CapRepOrderMapper;
import com.faujor.dao.master.bam.CapRepStockMapper;
import com.faujor.dao.master.document.DocumentMapper;
import com.faujor.entity.bam.psm.CapRep;
import com.faujor.entity.bam.psm.CapRepMate;
import com.faujor.entity.bam.psm.CapRepOrder;
import com.faujor.entity.bam.psm.CapRepStock;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.document.Document;
import com.faujor.service.bam.CapRepService;
import com.faujor.service.document.DocumentService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service
public class CapRepServiceImpl implements CapRepService{
	
	@Autowired
	private CapRepMapper capRepMapper;
	@Autowired
	private CapRepMateMapper capRepMateMapper;
	@Autowired
	private CapRepOrderMapper capRepOrderMapper;
	@Autowired
	private CapRepStockMapper capRepStockMapper;
	@Autowired
	private DocumentService documentService;
	
	@Override
	public CapRep getById(String id) {
		return capRepMapper.selectByPrimaryKey(id);
	}
	@Override
	@Transactional
	public int save(CapRep capRep, List<CapRepMate> mates, List<CapRepOrder> orders, List<CapRepStock> stocks) {
		//创建者信息
		SysUserDO user = UserCommon.getUser();
		capRep.setCreateTime(new Date());
		capRep.setCreater(user.getUserId()+"");
		capRep.setCreaterName(user.getName());
		int count = capRepMapper.insert(capRep);
		//保存从表数据
		if(mates.size()>0){
			capRepMateMapper.batchInsert(mates);			
		}
		if(orders.size()>0){
			capRepOrderMapper.batchInsert(orders);
		}
		if(stocks.size()>0){
			capRepStockMapper.batchInsert(stocks);
		}
		return count;
	}
	@Override
	@Transactional
	public int update(CapRep capRep, List<CapRepMate> mates, List<CapRepOrder> orders, List<CapRepStock> stocks) {
		//修改者信息
		SysUserDO user = UserCommon.getUser();
		capRep.setModifyTime(new Date());
		capRep.setModifier(user.getUserId()+"");
		capRep.setModifierName(user.getName());
		int count = capRepMapper.updateByPrimaryKey(capRep);
		//更新从表信息
		String mainId = capRep.getId();
		capRepMateMapper.deleteByMainId(mainId);
		capRepOrderMapper.deleteByMainId(mainId);
		capRepStockMapper.deleteByMainId(mainId);
		if(mates.size()>0){
			capRepMateMapper.batchInsert(mates);			
		}
		if(orders.size()>0){
			capRepOrderMapper.batchInsert(orders);
		}
		if(stocks.size()>0){
			capRepStockMapper.batchInsert(stocks);
		}
		return count;
	}
	@Override
	public LayuiPage<CapRep> getCapRepByPage(Map<String, Object> map) {
		LayuiPage<CapRep> page=new LayuiPage<>();
		List<CapRep> data = capRepMapper.getCapRepByPage(map);
		Integer count = capRepMapper.getCapRepCount(map);
		page.setCount(count);
		page.setData(data);
		return page;
	}
	@Override
	public List<CapRepMate> getMateByMainId(String mainId) {
		return capRepMateMapper.selectByMainId(mainId);
	}
	@Override
	public List<CapRepOrder> getOrderByMainId(String mainId) {
		return capRepOrderMapper.selectByMainId(mainId);
	}
	@Override
	public List<CapRepStock> getStockByMainId(String mainId) {
		return capRepStockMapper.selectByMainId(mainId);
	}
	@Override
	public List<CapRepMate> getMateBySuppNo(String suppNo,String mainId) {
		List<CapRepMate> mates = capRepMateMapper.selectBySuppNo(suppNo);
		for (CapRepMate mate : mates) {
			mate.setId(UUIDUtil.getUUID());
			mate.setMainId(mainId);
		}
		return mates;
	}
	@Override
	public int delete(List<String> ids) {
		int num=0;
		for (String id : ids) {
			num=capRepMapper.deleteByPrimaryKey(id);
			capRepMateMapper.deleteByMainId(id);
			//删除附件
			List<CapRepOrder> orders = capRepOrderMapper.selectByMainId(id);
			List<String> docIds=new ArrayList<>();
			for (CapRepOrder order : orders) {
				String orderAttach = order.getOrderAttach();
				if(StringUtil.isNotNullOrEmpty(orderAttach)){
					Document document = JsonUtils.jsonToPojo(orderAttach, Document.class);
					docIds.add(document.getId());
				}
			}
			documentService.deleteFile(docIds);
			capRepOrderMapper.deleteByMainId(id);
			capRepStockMapper.deleteByMainId(id);
		}
		return num;
	}
	@Override
	public int updateStstus(List<String> ids, String status) {
		return capRepMapper.updateStatus(ids, status);
	}
	@Override
	public boolean checkRepeat(Map<String, Object> map) {
		Integer count = capRepMapper.getCountByMap(map);
		return count>0?true:false;
	}
}
