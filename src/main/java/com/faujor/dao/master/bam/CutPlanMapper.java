package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.CutPlan;
import com.faujor.entity.bam.CutPlanMate;

public interface CutPlanMapper {
	/**
	 * 分页获取打切计划
	 * @param map
	 * @return
	 */
	List<CutPlan> getCutPlanByPage(Map<String, Object> map);
	/**
	 * 获取打切计划数量
	 * @param map
	 * @return
	 */
	int getCutPlanNum(Map<String, Object> map);
	/**
	 * 根据打切计划Id获取打切计划
	 * @param palnId
	 * @return
	 */
	CutPlan getCutPlanById(String planId);
	/**
	 * 保存打切计划
	 * @param cutPlan
	 * @return
	 */
	int saveCutPlan(CutPlan cutPlan);
	/**
	 * 更新打切计划
	 * @param cutPlan
	 * @return
	 */
	int updateCutPlan(CutPlan cutPlan);
	/**
	 * 根据计划编号删除打切计划
	 * @param planId
	 * @return
	 */
	int delCutPlanById(String planId);
	/**
	 * 更改打切计划
	 * @param ids
	 * @param status
	 */
	void changeCutPlanStatus(@Param("ids")List<String> ids,@Param("status")String status);
	/**
	 * 根据打切单编号获取打切单
	 * @param planId
	 * @return
	 */
	List<CutPlanMate> getCutPlanMateByPlanId(String planId);
	/**
	 * 保存打切计划下的物料
	 * @param mate
	 * @return
	 */
	int saveCutPlanMates(@Param("mates")List<CutPlanMate> mates);
	/**
	 * 根据打切计划Id删除打切计划下的所有物料
	 * @param planId
	 * @return
	 */
	int delCutPlanMateByPlanId(String planId);
	/**
	 * 从打切联络单获取打切物料的详情
	 * @param planMonth
	 * @param columnName 预测销售的字段
	 * @return
	 */
	List<CutPlanMate> getCutPlanMateFromLiaison(@Param("planMonth")String planMonth,@Param("columnName")String columnName,
			@Param("saleFcstId")String saleFcstId);
	/**
	 * 查询已作废和已提交的打切计划单号集合
	 * @param cutMonth
	 * @return
	 */
	List<String> queryCutPlanCodeListByStatus();
	/**
	 * 根据月份查询已保存和已提价的打切计划单
	 * @param cutMonth
	 * @return
	 */
	List<String> queryCutPlansByCutMonth(String cutMonth);
	/**
	 * 获取打切进度
	 * @param planMonth
	 * @param columnName
	 * @param saleFcstId
	 * @return
	 */
	List<CutPlanMate> getCutPlanMateFromLiaison2(@Param("planMonth")String planMonth,@Param("columnName")String columnName,
			@Param("saleFcstId")String saleFcstId,@Param("citeCode")String citeCode);
}
