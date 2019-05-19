package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.CutPlan;
import com.faujor.entity.bam.CutPlanMate;
import com.faujor.entity.common.LayuiPage;

public interface CutPlanService {
	/**
	 * 分页获取打切计划
	 * @param map
	 * @return
	 */
	LayuiPage<CutPlan> getCutPlanByPage(Map<String, Object> map);
	/**
	 * 保存打切计划
	 * @param cutPlan
	 * @param mates
	 */
	void saveCutPlan(CutPlan cutPlan,List<CutPlanMate> mates);
	/**
	 * 更新打切计划
	 * @param cutPlan
	 * @param mates
	 */
	void udateCutPlan(CutPlan cutPlan,List<CutPlanMate> mates);
	/**
	 * 删除打切计划
	 * @param ids
	 */
	void delCutPlan(List<String> ids);
	/**
	 * 获取打切计划
	 * @param planId
	 * @return
	 */
	CutPlan getCutPlanByPlanId(String planId);
	/**
	 * 根据打切计划单号获取打切计划物料
	 * @param planId
	 * @return
	 */
	List<CutPlanMate> getCutPlanMateByPlanId(String planId);
	/**
	 * 更新打切计划的状态
	 * @param planIds
	 * @param status
	 */
	void changeCutPlanStatus(List<String> planIds,String status);
	/**
	 * 根据月份获取打切计划物料列表
	 * @param planMonth
	 * @param columnName 对应的销售预测字段
	 * @return
	 */
	List<CutPlanMate> getCutPlanMateFromLiaison(String planMonth,String columnName,String saleFcstId);
	/**
	 * 根据查询已作废和已提交的打切计划单号集合
	 * @param cutMonth
	 * @return
	 */
	List<String> queryCutPlanCodeListByStatus();
	/**
	 * 根据引用的打切计划单号，引入物料的打切进度
	 * @param cutMonth
	 * @param citeCode
	 * @return
	 */
	Map<String, Object> getCutScheOfCutPlanMate(String cutMonth, String citeCode);
	/**
	 * 根据月份查询已保存和已提价的打切计划单
	 * @param cutMonth
	 * @return
	 */
	List<String> queryCutPlansByCutMonth(String cutMonth);
	
}
