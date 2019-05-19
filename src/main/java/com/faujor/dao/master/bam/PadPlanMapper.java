package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.psm.PadMateMess;
import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.entity.bam.psm.PadPlanRecord;
import com.faujor.entity.bam.psm.Psi;
import com.faujor.entity.common.BaseEntity;

/**
 * 生产/交货计划
 * @author Vincent
 *
 */
public interface PadPlanMapper {
	/**
	 * 分页获取 生产/交货计划 主表信息
	 * @param map
	 * @return
	 */
	public List<PadPlan> getPadPlanByPage(Map<String, Object> map);
	

	/**
	 * 获取 生产/交货计划 主表数量
	 * @param mainId
	 * @return
	 */
	public int getPadPlanCount(Map<String, Object> map);
	
	/**
	 * 根据条件获取单条 生产/交货计划 主表信息
	 * @param map
	 * @return
	 */
	public PadPlan getPadPlanByMap(Map<String, Object> map);
	
	/**
	 * 根据条件获取单条 生产/交货计划 数据
	 * @param map
	 * @return
	 */
	public List<PadPlan> getPadPlanListByMap(Map<String, Object> map);
	
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
	 * 更新 生产/交货计划 主表信息
	 * @param padPlan
	 * @return
	 */
	public int updatePadPlan(PadPlan padPlan);
	
	/**
	 * 修改 生产/交货计划 主表 的状态(多参数处理@Param)
	 * @param status
	 * @param id
	 * @return
	 */
	public int updatePadPlanStatus(@Param("status")String status,@Param("id")String id);
	
	/**
	 * 修改 生产/交货计划 主表 的同步标记(多参数处理@Param)
	 * @param syncFlag
	 * @param id
	 * @return
	 */
	public int updatePadPlanSyncFlag(@Param("syncFlag")String syncFlag,@Param("id")String id);
	
	/**
	 * 获取最大的计划月份
	 * @return
	 */
	public String getMaxPadPlanMonth();
	
	/**
	 * 获取 生产/交货计划 明细页面列表信息
	 * @param map
	 * @return
	 */
	public List<PadPlanDetail> getPadPlanDetailPage(Map<String, Object> map);
	
	/**
	 * 获取 生产/交货计划 明细列表信息
	 * @param mainId
	 * @return
	 */
	public List<PadPlanDetail> getPadPlanDetailListByMainId(String mainId);
	
	/**
	 * 获取 生产/交货计划 明细数量
	 * @param mainId
	 * @return
	 */
	public int getPadPlanDetailCount(String mainId);
	
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
	
	/**
	 * 根据用户管理的系列保存明细数据
	 * @param map，明细数据，用户编码
	 * @return
	 */
	public int savePadPlanDetailListByUserSeries(Map<String, Object> map);
	
	/**
	 * 根据用户管理的系列删除明细数据
	 * @param map，主表id，用户编码
	 * @return
	 */
	public int delPadPlanDetailByUserSeries(Map<String, Object> map);
	
	/**
	 * 根据年月获取 生产/交货计划 所有物料的产品系列
	 * @param ym
	 * @return
	 */
	public List<BaseEntity> getMatProdSeriesByYearMonth(Map<String, Object> map);
	
	/**
	 * 根据年月、产品系列获取 生产/交货计划 明细数据
	 * @param ym
	 * @param prodSeries
	 * @return
	 */
	public List<PadPlanDetail> getPadPlanDetailByMap(Map<String, Object> map);
	
	/**
	 * 根据条件获取 物料进销存报表 信息
	 * @param map
	 * @return
	 */
	public List<Psi> getPsiInfoByMap(Map<String, Object> map);
	
	/**
	 * 根据条件获取 物料进销存报表大品项汇总信息
	 * @param map
	 * @return
	 */
	public List<Psi> getPsiSumByMap(Map<String, Object> map);
	
	/**
	 * 获取临时数据
	 * @param map
	 * @return
	 */
	public List<PadPlanDetail> getPadPlanTempDetailByMap(Map<String, Object> map);
	
	/**
	 * 根据本月计划更新下个月计划的明细
	 * @param map
	 */
	public void updateNextPadPlanDetail(Map<String, Object> map);
	
	/**
	 * 通过 t_ora_cxjh表更新某月 生产交货计划明细数据
	 * @param map
	 */
	public void updateMonthPadDetail(Map<String, Object> map);
	
	/**
	 * 未来月份的多余物料插入
	 * @param map
	 * @return
	 */
	public int saveFutureMonthExtraMat(Map<String, Object> map);
	
	/**
	 * 当前及以前月份的多余物料插入
	 * @param map
	 * @return
	 */
	public int saveCurrPreMonthExtraMat(Map<String, Object> map);
	
	/**
	 * 未来月起，下个月中的上个月全国库存获取
	 * @param map
	 * @return
	 */
	public int updateNextPlanPreStock(Map<String, Object> map);
	
	/**
	 * 修改后续月份的计算列 
	 * @param map
	 * @return
	 */
	public int updateFutureMonthCalc(Map<String, Object> map);
	
	/**
	 * 修改当前及以前月份月份的计算列
	 * @param map
	 * @return
	 */
	public int updateCurrPreMonthCalc(Map<String, Object> map);
	
	/**
	 * 保存中间表中额外的物料至生产交货计划中
	 * @param map
	 * @return
	 */
	public int saveExtraMaterialInPadPlan(Map<String, Object> map);
	
	/**
	 * 月底最后一日，使用实际销量更新销售预测
	 * @param map
	 * @return
	 */
	public int updatePadPlanSaleForeByCxjh(Map<String, Object> map);
	
	/**
	 * 为未来月份删除多余物料
	 * @param map
	 * @return
	 */
	public int delFutureMonthExtraMat(Map<String, Object> map);
	//-----------------------物料交货计划整年调整开始-----------------------------
	/**
	 * 获取物料未来一年的生产交货计划详情数据
	 * @param map
	 * @return
	 */
	public List<PadPlanDetail> getYearPadDetailByMap(Map<String, Object> map);
	//-----------------------物料交货计划整年调整结束-----------------------------

	
	//-----------------------批量修改物料--------------------------
	/**
	 * 根据计划编号和物料编码查询物料是否存储
	 * @param planCode
	 * @param matCode
	 * @return
	 */
	public String getMateByPlanCodeAndMatCode(@Param("planCode")String planCode, @Param("matCode")String matCode);

	/**
	 * 获取弹窗列表数据
	 * @param matCode
	 * @return
	 */
	public List<PadMateMess> getPlanMessageListOfMateByMatCode(String matCode);
	
	/**
	 * 修改交货计划下其中一个物料的信息
	 * @param map
	 */
	public void updatePadPlanDetailByid(Map<String, Object> map);

	/**
	 * 查询需要修改的交货计划物料数据
	 * @param minPlanMonthFormat
	 * @param matCode
	 * @return
	 */
	public List<PadPlanDetail> getMateByPlanMonthAndMatCode(@Param("minPlanMonthFormat")String minPlanMonthFormat, @Param("matCode")String matCode);

	/**
	 * 修改生产/交货计划物料数据
	 * @param pad
	 * @return
	 */
	public int updatePadPlandDetailsById(PadPlanDetail pad);

	/**
	 * 备份生产交货计划主表数据
	 * @param param
	 */
	public void copyPadPlanData(Map<String, Object> param);

	/**
	 * 备份生产交货计划从表数据
	 * @param param
	 */
	public void copyPadPlanDetailListData(Map<String, Object> param);



	
	//-----------------------批量修改物料--------------------------
	
	
	/**
	 * 获取生产交货计划记录数据
	 * @param map
	 * @return
	 */
	public List<PadPlanRecord> getPadPlanRecordListByPage(Map<String, Object> map);
	
	/**
	 * 获取生产交货计划记录数据的总数量
	 * @param map
	 * @return
	 */
	public int getPadPlanRecordListByPageCount(Map<String, Object> map);

	/**
	 * 获取导出生产交货计划记录的数据
	 * @param map
	 * @return
	 */
	public List<PadPlanRecord> getPadPlanRecordList(Map<String, Object> map);
}
