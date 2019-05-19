package com.faujor.service.sapcenter.bam;

import java.util.List;

import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;

/**
 * 生产交货计划同步至sap服务类
 * 
 * @ClassName:   SapPadPlanService
 * @Instructions:
 * @author: Vincent
 * @date:   2018年8月29日 上午11:26:17
 *
 */
public interface SapPadPlanService {
	
	/**
	 * 保存生产计划数据,sap中间库
	 * @param padPlan
	 * @param detailList
	 * @return
	 * @throws Exception
	 */
	public int saveSapPadPlanInfo(PadPlan padPlan,List<PadPlanDetail> detailList) throws Exception;
	
	/**
	 * 删除生产计划数据,sap中间库
	 * @param id
	 * @throws Exception
	 */
	public void delSapPadPlanInfo(String id) throws Exception;
}
