package com.faujor.dao.master.bam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.psm.SuppProd;
/**
 * 供应商排产
 * @author hql
 *
 */
import com.faujor.entity.bam.psm.SuppProdPlan;
import com.faujor.entity.mdm.QualSupp;
public interface SuppProdMapper {
	/**
	 * 获取供应商排产的分页数据
	 * @param map
	 * @return
	 */
	List<SuppProd> getSuppProdByPage(Map<String, Object> map);
	/**
	 * 获取供应商排产条数
	 * @param map
	 * @return
	 */
	int getSuppProdNum(Map<String, Object> map);
	
	/**
	 * 供应商分页查看各自排产
	 * @param map
	 * @return
	 */
	List<SuppProd> findSuppProdByPage(Map<String , Object> map);
	/**
	 * 获取供应商各自的排产数目
	 * @param map
	 * @return
	 */
	int findSuppProdNum(Map<String, Object> map);
	
	/**
	 * 根据编码获取供应商排产
	 * @param id
	 * @return
	 */
	SuppProd getSuppProdById(String id);
	/**
	 * 保存供应商排产数据
	 * @param suppProd
	 * @return
	 */
	int saveSuppProd(SuppProd suppProd);
	/**
	 * 更新供应商排产数据
	 * @param suppProd
	 * @return
	 */
	int updateSuppProd(SuppProd suppProd);

	/**
	 * 批量插入排产计划
	 * @param list
	 * @return
	 */
	int saveSuppProds(List<SuppProd> list);
	
	/**
	 * 根据Iid 删除
	 * @param id
	 * @return
	 */
	int delSuppProdById(String id);
	/**
	 * 根据PlanDetailId 删除排产计划
	 * @param id
	 * @return
	 */
	int delSuppProdByPlanDetailId(String id);
	/**
	 * 删除排产计划详情根据ID
	 * @param mainId
	 * @return
	 */
	int delSuppProdPlanByMainId(String mainId);
	/**
	 * 根据主表Id删除供应商排产信息
	 * @param mainId
	 * @return
	 */
	int delSuppProdByMainId(String mainId);
	/**
	 * 更新排产的状态
	 * @param ids
	 * @param status
	 * @return
	 */
	int changeSuppProdStatus(@Param("ids")List<String> ids,@Param("status")String status);
	/**
	 * 获取供应商的排产计划
	 * @param mainId
	 * @return
	 */
	List<SuppProdPlan> getSuppProdPlanByMainId(String mainId);
	/**
	 * 保存排产计划
	 * @param plan
	 * @return
	 */
	int saveProdPlan(SuppProdPlan plan);
	/**
	 * 更新排产计划
	 * @param map
	 * @return
	 */
	int updateProdPlan(SuppProdPlan plan);
	/**
	 * 更新排查计划的状态
	 * @param map
	 * @retur
	 */
	int changeProdPlanStatus(Map<String, Object > map);
	/**
	 * 根据备货计划Id获取排产计划
	 * @param id
	 * @return
	 */
	List<SuppProd> getSuppProdByInvenId(String id);
	/**
	 * 根据mainID获取排产计划列表
	 * @param mainId
	 * @return
	 */
	List<SuppProd> getSuppProdByMainId(String mainId);
	/**
	 * 根据mainID获取排产计划列表
	 * @param mainId
	 * @return
	 */
	List<SuppProd> getSuppProdByPlanDetailId(String planDetailId);
	/**
	 * 根据
	 * @param id
	 * @return
	 */
	SuppProdPlan getSuppProdPlanById(String id);
	/**
	 * 根据mainId和日期获取排产计划详情对象
	 * @param SuppProdPlan
	 * @return
	 */
	SuppProdPlan getSuppProdPlanByPlan(SuppProdPlan SuppProdPlan);
	/**
	 * 更新排产计划的状态和剩余未分配数量
	 * @param map
	 */
	void changeStatusAndRemainNum(Map<String , Object> map);
	/**
	 * 根据物料供应商编码 计划月份等信息获取 排产计划
	 * @param map
	 * @return
	 */
	List<SuppProd> getSuppProdByMap(Map<String, Object> map );
	/**
	 * 修改排产的安全库存率
	 * @param map
	 */
	void changeSafeScale(Map<String , Object> map);
	/**
	 * 获取实际分配数量
	 * @param mainId
	 * @return
	 */
	BigDecimal getActDistNum(String mainId);
	/**
	 * 备货计划物料分配到供应商的交货计划的数量
	 * @param mainId
	 * @return
	 */
	BigDecimal getPlanDlvNumByPlanDetailId(String planDetailId);
	
	/***
	 * 根据备货计划详情   供应商物料关系  删除已经解除的物料供应商关系
	 * @param map
	 * @return
	 */
	int deleteSuppProdByQualSupp(Map<String, Object> map);
}
