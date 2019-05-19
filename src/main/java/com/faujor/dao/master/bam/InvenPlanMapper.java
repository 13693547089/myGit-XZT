package com.faujor.dao.master.bam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.psm.InvenPadCompare;
import com.faujor.entity.bam.psm.InvenPlan;
import com.faujor.entity.bam.psm.InvenPlanDetail;
import com.faujor.entity.bam.psm.SuppProd;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.BaseEntity;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.QualSupp;

/**
 * 供应商备货计划
 * 
 * @author hql
 *
 */
public interface InvenPlanMapper {
	/**
	 * 分页获取备货计划数量
	 * 
	 * @param map
	 * @return
	 */
	List<InvenPlan> getInvenPlanByPage(Map<String, Object> map);

	/**
	 * 获取分页数据的数量
	 * 
	 * @param map
	 * @return
	 */
	int getInvenPlanNum(Map<String, Object> map);

	/**
	 * 根据Id获取备货计划
	 * 
	 * @param id
	 * @return
	 */
	InvenPlan getInvenPlanById(String id);
	
	/**
	 * 根据Id获取备货计划
	 * 
	 * @param id
	 * @return
	 */
	InvenPlan getInvenPlanByCode(String planCode);

	/**
	 * 保存供应商备货计划
	 * 
	 * @param plan
	 * @return
	 */
	int saveInvenPlan(InvenPlan plan);

	/**
	 * 更新备货计划
	 * 
	 * @param plan
	 * @return
	 */
	int updateInvenPlan(InvenPlan plan);

	/**
	 * 根据Id删除供应商备货计划
	 * 
	 * @param Id
	 * @return
	 */
	int delInvenPlanById(String id);

	/**
	 * 更新供应商的状态
	 * 
	 * @param ids
	 * @return
	 */
	int changeInvenPlanStatus(@Param("ids") List<String> ids, @Param("status") String status);

	/**
	 * 根据供应商主键获取供应商备货计划详情
	 * 
	 * @param mainId
	 * @return
	 */
	List<SuppProd> getInvenPlanDetaiByMainId(Map<String, Object> map);

	/**
	 * 保存备货计划详情
	 * 
	 * @param detail
	 * @return
	 */
	int saveInvenPlanDetail(InvenPlanDetail detail);

	/**
	 * 更新备货计划详情
	 * 
	 * @param detail
	 * @return
	 */
	int updateInvenPlanDetail(InvenPlanDetail detail);

	/**
	 * 根据供应商备货计划详情Id删除备货详情
	 * 
	 * @param Ids
	 * @return
	 */
	int delInvenPlanDetail(@Param("ids")List<String> ids);

	/**
	 * 根据备货计划Id删除备货计划详情
	 * 
	 * @param mainId
	 * @return
	 */
	int delInvenPlanDetailByMainId(String mainId);

	/**
	 * 根据Id获取排产详情
	 * 
	 * @param id
	 * @return
	 */
	InvenPlanDetail getPlanDetailById(String id);

	/**
	 * 
	 * @param id
	 * @param status
	 */
	void changeInvenPlanDetailStatus(@Param("id") String id, @Param("status") String status);

	/**
	 * 根据备货计划的ID获取备货计划详情的某个状态数量
	 * 
	 * @param ids
	 * @param status
	 * @return
	 */
	Integer getStatusNumsByMainIds(@Param("ids") List<String> ids, @Param("status") String status);

	/**
	 * 更新备货计划详情
	 * 
	 * @param map
	 */
	void updatePlanDetail(Map<String, Object> map);

	/**
	 * 该备货计划物料是否重复
	 * 
	 * @param map
	 * @return
	 */
	int getMounthMateExistCount(Map<String, Object> map);

	/**
	 * 根据采购员初始化改采购员这个月的备货计划
	 * 
	 * @param userId
	 * @param planMonth
	 * @return
	 */
	List<SuppProd> findPlanDetailsDataByParams(Map<String, Object> params);

	/**
	 * 计算某采购员的某种物料的所有供应商数量
	 * 
	 * @param params
	 * @param userId
	 * @param mateCode
	 * @return
	 */
	int countSuppNumByParams(Map<String, Object> params);

	/**
	 * 按照物料获取报表数据
	 * 
	 * @param map
	 * @return
	 */
	List<SuppProd> getReportByMate(Map<String, Object> map);

	/**
	 * 按照供应商获取报表数据
	 * 
	 * @param map
	 * @return
	 */
	List<SuppProd> getReportBySupp(Map<String, Object> map);

	/**
	 * 批量插入详情数据
	 * 
	 * @param details
	 * @return
	 */
	int batchSaveInvenPlanDetails(List<SuppProd> details);

	/**
	 * 根据主表ID删除备货详情数据
	 * 
	 * @param mainId
	 * @return
	 */
	int removeInvenPlanDetailsByMainId(String mainId);

	/**
	 * 根据主表的id获取详情表的ID list
	 * 
	 * @param mainId
	 * @return
	 */
	List<String> findInvenPlanDetailIDsByMainId(String mainId);

	/**
	 * 更新详情表
	 * 
	 * @param suppProd
	 * @return
	 */
	int updateInvenPlanDetails(SuppProd suppProd);

	/**
	 * 获取得到改主数据下物料编码和供应商编码的合并（形如 mateCode,suppNo)
	 * 
	 * @param mainId
	 * @return
	 */
	List<String> findInvenPlanDetailsMateCodeAndSuppNoByMainID(String mainId);

	/**
	 * 根据 主键ID，物料编码， 供应商编码获取备货详情
	 * 
	 * @param query
	 * @return
	 */
	SuppProd findSuppProdByParams(Map<String, Object> query);

	/**
	 * 根据用户id，id和月份获取备货计划数量
	 * 
	 * @param plan
	 * @return
	 */
	int countInvenPlanByIdAndPlanMonth(InvenPlan plan);

	/**
	 * 校验是否需要为采购组长创建一个数据
	 * 
	 * @param leaderId
	 * @return
	 */
	int checkAllSubmit(Long leaderId);

	/**
	 * 采购组长拉取数据
	 * 
	 * @param params
	 * @return
	 */
	List<SuppProd> getPlanDetailForLeader(Map<String, Object> params);

	/**
	 * 更新已审核状态
	 * 
	 * @param params
	 * @return
	 */
	int updateInvenPlanDetailsByParams(Map<String, Object> params);
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
	 * 根据采购员初始化该采购员这个月的备货计划
	 * @param userId
	 * @param planMonth
	 * @return
	 */
	List<InvenPlanDetail> findPlanDetailsByParams(Map<String, Object> params);
	
	/**
	 * 获取物料汇总详情
	 * @param map
	 * @return
	 */
	List<InvenPlanDetail> getMatePlanDetaiByMainId(Map<String, Object> map);
	/**
	 * 获取物料汇总详情(按照系列排序)
	 * @param map
	 * @return
	 */
	List<InvenPlanDetail> getMatePlanDetaiByMainIdOrderBySer(Map<String, Object> map);
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
	/**
	 * 根据月份和物料编码获取备货计划详情
	 * @param map
	 * @return
	 */
	List<InvenPlanDetail> getPlanDetailByMateAndMonth(Map<String, Object> map);
	/**
	 * 更新安全库存
	 * @param map
	 * @return
	 */
	int  updateSafeScale(Map<String, Object> map);
	//------------------------根据汇总生成备货计划详情结束3-----------------------------
	
	//------------------------更新备货计划开始-----------------------------
	InvenPlanDetail getPlanDetailByMateCode(Map<String , Object> map);

	//------------------------更新备货计划结束-----------------------------
	
	//------------------------导入备货计划开始-----------------------------
	/**
	 * 获取自己创建的最新的备货计划
	 * @param userId
	 * @return
	 */
	InvenPlanDetail getLatestPlan(String userId);
	/**
	 * 获取物料的全国库存
	 * @param map
	 * @return
	 */
	BigDecimal getNationalStock(Map<String, Object> map);
	//------------------------导入备货计划结束-----------------------------
	
	//-------------------------差异比较开始-----------------------------
	/**
	 * 根据备货计划编码获取差异信息
	 * @param planCode
	 * @return
	 */
	List<InvenPadCompare> getGetComPareByPlanCode(String planCode);
	/**
	 * 根据物料信息以及月份获取产销采购的交货计划
	 * @param mates
	 * @param planMonth
	 * @return
	 */
	List<InvenPadCompare> getGetComPareByMonthAndMate(@Param("mates")List<InvenPadCompare>mates,@Param("planMonth")Date planMonth );
	
	//-------------------------差异比较结束-----------------------------	
}
