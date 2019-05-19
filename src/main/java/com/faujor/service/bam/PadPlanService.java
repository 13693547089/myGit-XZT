package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.PadMateMess;
import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.entity.bam.psm.PadPlanDetailForm;
import com.faujor.entity.bam.psm.PadPlanRecord;
import com.faujor.entity.bam.psm.Psi;
import com.faujor.entity.common.BaseEntity;
import com.faujor.entity.common.LayuiPage;

/**
 * 生产/交货计划 服务类
 * @author Vincent
 *
 */
public interface PadPlanService {
	/**
	 * 分页获取 生产/交货计划 主表信息
	 * @param map
	 * @return
	 */
	public LayuiPage<PadPlan> getPadPlanByPage(Map<String, Object> map);
	
	/**
	 * 获取分页获取 生产/交货计划 主表数量
	 * @param map
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
	 * 根据ID删除 生产/交货计划 信息
	 * @param id
	 * @return
	 */
	public int delPadPlanInfoById(String id);
	

	/**
	 * 根据IDs批量删除 生产/交货计划 信息
	 * @param ids
	 * @return
	 */
	public int delBatchPadPlanInfoByIds(List<String> ids);
	
	/**
	 * 保存 生产/交货计划 信息
	 * @param padPlan
	 * @param detailList
	 * @param userCode
	 * @return
	 */
	public int savePadPlanInfo(PadPlan padPlan,List<PadPlanDetail> detailList,String userCode);
	
	/**
	 * 更新 生产/交货计划 信息
	 * @param padPlan
	 * @param detailList
	 * @return
	 */
	public int updatePadPlanInfo(PadPlan padPlan,List<PadPlanDetail> detailList);
	
	/**
	 * 获取 生产/交货计划 明细列表页面数据
	 * @param map
	 * @return
	 */
	public LayuiPage<PadPlanDetail> getPadPlanDetailPage(Map<String, Object> map);
	
	
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
	 * 修改 生产/交货计划 主表 的状态
	 * @param status
	 * @param id
	 * @return
	 */
	public int updatePadPlanStatus(String status,String id);
	
	/**
	 * 修改 生产/交货计划 主表 的同步标记
	 * @param syncFlag
	 * @param id
	 * @return
	 */
	public int updatePadPlanSyncFlag(String syncFlag,String id);
	
	/**
	 * 根据年月获取 生产/交货计划 所有物料的产品系列
	 * @param ym
	 * @return
	 */
	public List<BaseEntity> getMatProdSeriesByYearMonth(Map<String, Object> map);
	
	/**
	 * 根据年月、产品系列  物料名称编码 获取生产/交货计划 明细数据
	 * @param map
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
	 * 获取未来月份的计算方式的数据
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
	//-----------------------物料交货计划整年调整开始-----------------------------
	/**
	 * 获取物料未来一年的生产交货计划详情数据
	 * @param map
	 * @return
	 */
	public List<PadPlanDetail> getYearPadDetailByMap(String mateCode,String planMonth);
	
	
	//-----------------------物料交货计划整年调整结束-----------------------------
	
	//-----------------------批量修改物料--------------------------
	/**
	 * 校验生产/交货计划中的被选中物料是否保存
	 * @param planCode
	 * @param matCode
	 * @return
	 */
	public boolean checkMateIsSave(String planCode, String matCode);
	/**
	 * 获取弹窗列表数据
	 * @param matCode
	 * @return
	 */
	public List<PadMateMess> getPlanMessageListOfMateByMatCode(String matCode);
	/**
	 * 批量修改交货计划值，计算周转天数
	 * @param dataJson
	 * @param matCode
	 * @return
	 */
	public Map<String, Object> confirmUpdatePadPlanMess(String dataJson, String matCode);

	
	//-----------------------批量修改物料--------------------------
	/**
	 * 获取生产交货计划记录列表数据
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPadPlanRecordListByPage(Map<String, Object> map);
	/**
	 * 根据版本范围获取列头信息
	 * @param paForm
	 * @return
	 */
	public Map<String, Object> getPadPlanRecordFields(Map<String, Object> map);
	/**
	 * 处理版本范围
	 * @param map
	 * @return
	 */
	public Map<String, Object> dealWithVersionScope(Map<String, Object> map);
	/**
	 * 获取导出生产交货计划记录的数据
	 * @param map
	 * @return
	 */
	public List<PadPlanRecord> getPadPlanRecordList(Map<String, Object> map);
	
	
}
