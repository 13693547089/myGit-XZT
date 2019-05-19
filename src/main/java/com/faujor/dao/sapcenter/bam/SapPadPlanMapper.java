package com.faujor.dao.sapcenter.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;

/**
 * 生产交货计划
 * 
 * @ClassName:   SapPadPlanMapper
 * @Instructions:
 * @author: Vincent
 * @date:   2018年8月29日 上午10:21:27
 *
 */
public interface SapPadPlanMapper {
	
	/**
	 * 根据ID删除 生产/交货计划 主表信息
	 * @param id
	 * @return
	 */
	public int delPadPlanById(String id);
	
	/**
	 * 保存 生产/交货计划 主表信息
	 * @param padPlan
	 * @return
	 */
	public int savePadPlan(PadPlan padPlan);
	
	/**
	 * 保存 生产/交货计划 明细数据
	 * @param list
	 * @return
	 */
	public int savePadPlanDetailList(List<PadPlanDetail> list);
	
	/**
	 * 根据主表ID删除 生产/交货计划 明细信息
	 * @param mainId
	 * @return
	 */
	public int delPadPlanDetailByMainId(String mainId);
}
