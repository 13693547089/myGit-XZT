package com.faujor.service.bam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.SuppProd;
import com.faujor.entity.bam.psm.SuppProdPlan;
import com.faujor.entity.common.LayuiPage;

public interface SuppProdService {
	/**
	 * 分页获取供应商排产计划列表
	 * @param map
	 * @return
	 */
	LayuiPage<SuppProd> getSuppProdByPage(Map<String, Object> map);
	/**
	 * 保存供应商备货计划
	 * @param suppProd
	 */
	void saveSuppProds(List<SuppProd> suppProds,String mainId,String safeScale,BigDecimal prodPlan);
	/**
	 * 更新排产计划状态
	 * @param ids
	 * @param status
	 */
	void changeSuppProdStatus(List<String> ids,String status);
	/**
	 * 根据备货计划Id获取排产计划
	 * @param id
	 * @return
	 */
	List<SuppProd> getSuppProdByInvenId(String id);
	/**
	 * 根据mainId 
	 * @param mainId
	 * @return
	 */
	List<SuppProd> getSuppProdByMainId(String mainId);
	/**
	 * 根据mainId 
	 * @param mainId
	 * @return
	 */
	List<SuppProd> getSuppProdByPlanDetailId(String planDetailId);
	/**
	 * 根据排产计划Id获取供应商排产计划列表
	 * @return
	 */
	List<SuppProdPlan> getSuppProdPlanByMainId(String mainId);
	/**
	 * 根据id获取排产计划
	 * @param id
	 * @return
	 */
	SuppProd getSuppProdById(String id);
	/**
	 * 保存排产计划详情
	 * @param suppPlans
	 */
	void saveSuppProdPlan(List<SuppProdPlan> suppPlans,String mainId,String status,BigDecimal remainNum);
	/**
	 * 平局排产
	 * @param ids
	 */
	void avgAllSuppProdPlan(List<String> ids);
	/**
	 * 根据物料供供应商年月信息获取排产计划
	 * @param map
	 * @return
	 */
	List<SuppProd> getSuppProdByMap(Map<String, Object> map );
}

