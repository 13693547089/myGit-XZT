package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.faujor.entity.bam.psm.TranPlan;
import com.faujor.entity.bam.psm.TranPlanDetail;

/**
 * 调拨计划
 * @author Vincent
 *
 */
public interface TranPlanMapper {
	/**
	 * 分页获取 调拨计划 主表信息
	 * @param map
	 * @return
	 */
	public List<TranPlan> getTranPlanByPage(Map<String, Object> map);
	

	/**
	 * 获取  调拨计划 主表数量
	 * @param map
	 * @return
	 */
	public int getTranPlanCount(Map<String, Object> map);
	
	/**
	 * 根据ID获取单条 调拨计划 主表信息
	 * @param id
	 * @return
	 */
	public TranPlan getTranPlanById(String id);
	
	/**
	 * 根据ID删除 调拨计划  主表信息
	 * @param id
	 * @return
	 */
	public int delTranPlanById(String id);
	
	/**
	 * 保存 调拨计划  主表信息
	 * @param tranPlan
	 * @return
	 */
	public int saveTranPlan(TranPlan tranPlan);
	
	/**
	 * 更新 调拨计划  主表信息
	 * @param tranPlan
	 * @return
	 */
	public int updateTranPlan(TranPlan tranPlan);
	
	/**
	 * 修改 调拨计划  主表 的状态(多参数处理@Param)
	 * @param status
	 * @param id
	 * @return
	 */
	public int updateTranPlanStatus(@Param("status")String status,@Param("id")String id);
	
	/**
	 * 获取 调拨计划 明细页面列表信息
	 * @param map
	 * @return
	 */
	public List<TranPlanDetail> getTranPlanDetailPage(Map<String, Object> map);
	
	/**
	 * 获取 调拨计划 明细列表信息
	 * @param mainId
	 * @return
	 */
	public List<TranPlanDetail> getTranPlanDetailListByMainId(String mainId);
	
	/**
	 * 获取 调拨计划 明细数量
	 * @param mainId
	 * @return
	 */
	public int getTranPlanDetailCount(String mainId);
	
	/**
	 * 保存 调拨计划 明细数据
	 * @param list
	 * @return
	 */
	public int saveTranPlanDetailList(List<TranPlanDetail> list);
	
	/**
	 * 根据主表ID删除 调拨计划 明细信息
	 * @param mainId
	 * @return
	 */
	public int delTranPlanDetailByMainId(String mainId);
	
	/**
	 * 从 生产/交货计划 中获取物料数据
	 * @param map
	 * @return
	 */
	public List<TranPlanDetail> getMatInfoFromPadPlan(Map<String, Object> map);
	
	/**
	 * 从 生产/交货计划 中获取调拨的明细数据
	 * @param map
	 * @return
	 */
	public List<TranPlanDetail> getTranPlanDetailFromPadPlan(Map<String, Object> map);
	
	/**
	 * 根据 生产/交货计划 最新数据修改调拨的数据
	 * @param map
	 * @return
	 */
	public int updateTranPlanDetailFromPadPlan(Map<String, Object> map);
}
