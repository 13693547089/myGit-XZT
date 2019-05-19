package com.faujor.dao.master.bam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.ActuallyReach;
import com.faujor.entity.bam.psm.BusyStock;
import com.faujor.entity.bam.psm.InnerControl;
import com.faujor.entity.bam.psm.PdrDetailVo;
import com.faujor.entity.bam.psm.PrdDlvInfo;
import com.faujor.entity.mdm.QualSupp;

public interface ActReachMapper {
	/**
	 * 分页获取实际达成数据（来自生产交货计划表）
	 * @param map
	 * @return
	 */
	List<ActuallyReach> getActReachByPage(Map<String, Object> map);
	/**
	 * 获取实际达成的条数
	 * @param map
	 * @return
	 */
	Integer getActReachCount(Map<String, Object> map);
	/**
	 * 获取实际达成报表数据
	 * @param map
	 * @return
	 */
	List<ActuallyReach>  getExportActData(Map<String, Object> map);
	/**
	 * 获取供应商某个物料的生产计划和交货计划
	 * @param map
	 * @return
	 */
	ActuallyReach getPlanPrdDevNum(Map<String, Object> map);
	/**
	 * 获取供应商的实际生产的数量
	 * @param map
	 * @return
	 */
	ActuallyReach getActPrdNum(Map<String, Object> map);
	/**
	 * 获取供应商的实际库存
	 * @param map
	 * @return
	 */
	BigDecimal getActStoreNum(Map<String, Object> map);
	
	/**
	 * 获取某月的内部管控报表物料供应商数据
	 * @param map
	 * @return
	 */
	List<InnerControl> getSuppInnerControl(Map<String, Object> map);
	/**
	 * 获取某月的内部管控报表物料数据
	 * @param map
	 * @return
	 */
	List<InnerControl> getMateInnerControl(Map<String, Object> map);
	
	/**
	 * 获取某月的内部管控报表供应商物料数据
	 * @param map
	 * @return
	 */
	List<BusyStock> getSuppBusyStock(Map<String, Object> map);
	/**
	 * 获取某月的内部管控报表物料数据
	 * @param map
	 * @return
	 */
	List<BusyStock> getMateBusyStock(Map<String, Object> map);
	/**
	 * 根据物料供应商 计划月份获取 生产计划  交货计划  期末预计库存
	 * @param map
	 * @return
	 */
	PrdDlvInfo getPrdDlvInfo(Map<String, Object> map);
	/**
	 * 根据物料供应商 计划月份获取 生产计划  交货计划  期末预计库存
	 * @param map
	 * @return
	 */
	PrdDlvInfo getSuppPrdDlvInfo(Map<String, Object> map);
	/**
	 * 根据物料 年月 获取该物料的供应商
	 * @param map
	 * @return
	 */
	List<QualSupp> getSuppByMateMonth(Map<String , Object> map);
	/**
	 * 查看实际达成
	 * @param map
	 * @return
	 */
	List<PdrDetailVo> getPrdDetailByMap(Map<String, Object> map);
}
