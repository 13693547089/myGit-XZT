package com.faujor.service.sapcenter.bam.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.sapcenter.bam.SapPadPlanMapper;
import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.service.sapcenter.bam.SapPadPlanService;

/**
 * 生产交货sap同步实现类
 * 
 * @ClassName:   SapPadPlanServiceImpl
 * @Instructions:
 * @author: Vincent
 * @date:   2018年8月29日 上午11:32:56
 *
 */
@Service
public class SapPadPlanServiceImpl implements SapPadPlanService{

	@Autowired
	private SapPadPlanMapper sapPadPlanMapper;
	
	/**
	 * 保存生产计划数据,sap中间库
	 * @param padPlan
	 * @param detailList
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@Override
	public int saveSapPadPlanInfo(PadPlan padPlan, List<PadPlanDetail> detailList) throws Exception {
		
		String id = padPlan.getId();
		
		// 删除
		delSapPadPlanInfo(id);
		
		if(detailList.size()>0){
			sapPadPlanMapper.savePadPlanDetailList(detailList);
		}
		
		sapPadPlanMapper.savePadPlan(padPlan);
		return 1;
	}

	/**
	 * 删除生产计划数据,sap中间库
	 * @param id
	 * @throws Exception
	 */
	@Transactional
	@Override
	public void delSapPadPlanInfo(String id) throws Exception {
		sapPadPlanMapper.delPadPlanDetailByMainId(id);
		sapPadPlanMapper.delPadPlanById(id);
	}

}
