package com.faujor.service.bam;

import java.util.List;
import java.util.Map;
import com.faujor.entity.bam.psm.TranPlan;
import com.faujor.entity.bam.psm.TranPlanDetail;
import com.faujor.entity.common.LayuiPage;

/**
 * 调拨计划 服务类
 * @author Vincent
 *
 */
public interface TranPlanService {
	/**
	 * 分页获取 调拨计划 主表信息
	 * @param map
	 * @return
	 */
	public LayuiPage<TranPlan> getTranPlanByPage(Map<String, Object> map);
	
	/**
	 * 获取分页获取 调拨计划 主表数量
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
	 * 根据ID删除 调拨计划 信息
	 * @param id
	 * @return
	 */
	public int delTranPlanInfoById(String id);
	
	/**
	 * 根据IDs批量删除 调拨计划 信息
	 * @param ids
	 * @return
	 */
	public int delBatchTranPlanInfoByIds(List<String> ids);
	
	/**
	 * 保存 调拨计划 信息
	 * @param tranPlan
	 * @param detailList
	 * @return
	 */
	public int saveTranPlanInfo(TranPlan tranPlan,List<TranPlanDetail> detailList);
	
	/**
	 * 更新 调拨计划 信息
	 * @param tranPlan
	 * @param detailList
	 * @return
	 */
	public int updateTranPlanInfo(TranPlan tranPlan,List<TranPlanDetail> detailList);
	
	/**
	 * 获取 调拨计划 明细列表页面数据
	 * @param map
	 * @return
	 */
	public LayuiPage<TranPlanDetail> getTranPlanDetailPage(Map<String, Object> map);
	
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
	 * 修改 调拨计划 主表 的状态
	 * @param status
	 * @param id
	 * @return
	 */
	public int updateTranPlanStatus(String status,String id);
	
	/**
	 * 从 生产/交货计划 中获取物料数据
	 * @param ym 年月
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
	public int updateTranPlanDetailFromPadPlan(Map<String, Object> map) throws Exception;
}
