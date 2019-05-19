package com.faujor.service.bam;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.bam.psm.BusyStock;
import com.faujor.entity.bam.psm.InvenPadCompare;
import com.faujor.entity.bam.psm.InvenPlan;
import com.faujor.entity.bam.psm.InvenPlanDetail;
import com.faujor.entity.bam.psm.SuppProd;
import com.faujor.entity.bam.psm.SuppProdVo;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.BaseEntity;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;

public interface InvenPlanService {
	/**
	 * 分页获取备货计划列表
	 * 
	 * @param map
	 * @return
	 */
	LayuiPage<InvenPlan> getInvenPlanByPage(Map<String, Object> map);

	/**
	 * 根据Id获取备货计划
	 * 
	 * @param Id
	 * @return
	 */
	InvenPlan getInvenPlanById(String Id);
	/**
	 * 根据code获取备货计划
	 * @param Id
	 * @return
	 */
	InvenPlan getInvenPlanByCode(String planCode);

	/**
	 * 保存备货计划
	 * 
	 * @param invenPlan
	 * @param planDetails
	 */
	void saveInvenPlan(InvenPlan invenPlan, List<InvenPlanDetail> planDetails);

	/**
	 * 更新备货计划
	 * 
	 * @param invenPlan
	 * @param planDetails
	 */
	void updateInvenPlan(InvenPlan invenPlan, List<InvenPlanDetail> planDetails);

	/**
	 * 根据Id删除备货计划
	 * 
	 * @param id
	 * @return
	 */
	RestCode delInvenPlan(String id);

	/**
	 * 根据备货计划Id获取备货计划详情列表数据
	 * 
	 * @param mainId
	 * @return
	 */
	List<SuppProd> getPlanDetailByMainId(Map<String, Object> map);

	/**
	 * 获取备货计划 状态数量
	 * 
	 * @param ids
	 * @param status
	 * @return
	 */
	Integer getStatusNumsByMainIds(List<String> ids, String status);

	/**
	 * 更改备货计划的状态
	 * 
	 * @param ids
	 * @param status
	 */
	void changeInvenPlanStatus(List<String> ids, String status);

	/**
	 * 取消分解计划
	 * 
	 * @param id
	 * @param status
	 */
	void cancleDecompose(String id, String status);

	/**
	 * 删除备货计划
	 * 
	 * @param ids
	 */
	void delInvenPlan(List<String> ids);

	/**
	 * 根据采购员Id获取采购员的管理的产品
	 * 
	 * @param userId
	 * @return
	 */
	List<BaseEntity> getSeriesByUserId(List<Long> userIds);

	/**
	 * 查看改约备货计划是否存在该物料
	 * 
	 * @param map
	 * @return
	 */
	RestCode isExistMonthMates(Map<String, Object> map);

	/**
	 * 根据月份初始化备货详情数据
	 * 
	 * @param planMonth
	 * @param mainId
	 * @return
	 */
	List<SuppProd> initalPlanDetailsData(String planMonth, String mainId);

	/**
	 * 根据计划年月和抽取人id抽取按物料汇总报表
	 * 
	 * @param suppProd
	 * @param userId
	 * @return
	 */
	List<SuppProd> getReportByMate(SuppProd suppProd, Long userId);

	/**
	 * 根据计划年月和抽取人id抽取按供应商汇总报表
	 * 
	 * @param suppProd
	 * @param userId
	 * @return
	 */
	List<SuppProd> getReportBySupp(Map<String, Object> params);

	/**
	 * 保存备货计划详情信息
	 * 
	 * @param invenPlan
	 * @param details
	 * @return
	 */
	RestCode saveInvenPlanInfo(InvenPlan invenPlan, List<SuppProd> details);

	/**
	 * 根据id和月份获取备货计划数量
	 * 
	 * @param plan
	 * @return
	 */
	int countInvenPlanByIdAndPlanMonth(InvenPlan plan);

	/**
	 * 查询改采购组长下面所有的备货信息
	 * 
	 * @param planMonth
	 * @return
	 */
	List<SuppProd> getPlanDetailForLeader(String planMonth);

	/**
	 * 采购组长审核保存
	 * 
	 * @param invenPlan
	 * @param details
	 * @return
	 */
	RestCode saveInvenPlanAuditInfo(InvenPlan invenPlan, List<SuppProd> details);
	
	
	/**
	 * 获取单据的品相信息
	 * @param map
	 * @return
	 */
	List<Dic> getItemList(Map<String, Object> map);
	/**
	 * 获取单据的物料信息
	 * @param map
	 * @return
	 */
	List<MateDO> getMateSelectList(Map<String, Object> map);
	/**
	 * 获取单据物料的系列信息
	 * @param map
	 * @return
	 */
	List<BaseEntity> getProdSeriers(Map<String, Object> map);
	/**
	 * 单据的供应商 信息
	 * @param map
	 * @return
	 */
	List<QualSupp> getSuppList(Map<String, Object> map);
	
	
	
	//------------------------根据汇总生成备货计划详情开始3-----------------------------
	
	/**
	 * 根据月份初始化备货详情数据
	 * @param planMonth
	 * @return
	 */
	List<InvenPlanDetail> initPlanDetailsData(String planMonth );
	/**
	 * 根据备货计划Id获取备货计划详情列表数据
	 * 
	 * @param mainId
	 * @return
	 */
	List<InvenPlanDetail> getMatePlanDetailByMainId(Map<String, Object> map);	
	
	
	/**
	 * 获取单据的品相信息
	 * @param map
	 * @return
	 */
	List<Dic> getItemInfo(Map<String, Object> map);
	/**
	 * 获取单据的物料信息
	 * @param map
	 * @return
	 */
	List<MateDO> getMateSelectInfo(Map<String, Object> map);
	/**
	 * 获取单据物料的系列信息
	 * @param map
	 * @return
	 */
	List<BaseEntity> getSelectProdSeriers(Map<String, Object> map);
		
	/**
	 * 根据供应商编码获取供应商的备货计划信息
	 * @param params
	 * @return
	 */
	List<SuppProd> getSuppProdBySuppNo(Map<String, Object> params);
	//------------------------根据汇总生成备货计划详情结束3-----------------------------

	//------------------------根据备货计划的更新开始-----------------------------
	void updateInvenMate(String planCode);
	//------------------------根据备货计划的更新结束-----------------------------
	
	
	
	//------------------------备货计划的导入开始-----------------------------
	/**
	 * 获取需要导出的数据
	 * @param planCode
	 * @return
	 */
	List<SuppProdVo> getExportTempData(String planCode);
	
	/**
	 * 导入数据的合法性
	 * @param list
	 * @return
	 */
	List<SuppProdVo> checkTempData(List<SuppProdVo> list,List<SuppProdVo> totalList,Date planMonth);
	/**
	 * 保存导入的数据
	 * @param list
	 * @param totalList
	 * @return
	 */
	void  saveTempData( List<SuppProdVo> list,List<SuppProdVo> totalList,Date planMonth);

	
	//------------------------备货计划的导入结束-----------------------------
	
	InvenPlanDetail getPlanDetailById(String planDetailId);
	
	
	//删除已经解除物料供应商关系的排产计划
	
	int deleteSuppProdByQualSupp(String planDetailId,List<QualSupp> supps);
	
	//----------------------------备货计划差异比较----------------------------
	/**
	 * 根据备货计划编码回去差异比较
	 * @param planCode
	 * @return
	 */
	List<InvenPadCompare> getGetComPareByPlanCode(String planCode,Date planMonth);
}
