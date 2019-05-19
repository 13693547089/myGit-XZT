package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.ActuallyReach;
import com.faujor.entity.bam.psm.BusyStock;
import com.faujor.entity.bam.psm.InnerControl;
import com.faujor.entity.bam.psm.PdrDetailVo;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.mdm.QualSupp;

public interface ActReachService {
	/**
	 * 分页获取实际达成数据
	 * @param map
	 * @return
	 */
	LayuiPage<ActuallyReach> getActReachByPage(Map<String, Object> map);
	
	/**
	 * 实际达成导出报表数据
	 * @param map
	 * @return
	 */
	List<ActuallyReach> getExportActReach(Map<String, Object> map);
	
	/**
	 * 根据物料获取该物料下每个供应商的实际达成
	 * @param map
	 * @return
	 */
	List<ActuallyReach> getMateSuppActReach(Map<String, Object> map);
	
	/**
	 * 获取供应商维度的内部管控表
	 * @param map
	 * @return
	 */
	List<InnerControl> getSuppInnerControl(Map<String, Object> map);
	/**
	 * 获取物料维度的内部管控表
	 * @param map
	 * @return
	 */
	List<InnerControl> getMateInnerControl(Map<String, Object> map);
	/**
	 * 获取供应商维度的旺季备货表
	 * @param map
	 * @return
	 */
	List<BusyStock> getSuppBusyStock(Map<String, Object> map);
	/**
	 * 获取物料维度的旺季备货表
	 * @param map
	 * @return
	 */
	List<BusyStock> getMateBusyStock(Map<String, Object> map);
	/**
	 * 根据物料 年月 获取该物料的供应商
	 * @param map
	 * @return
	 */
	List<QualSupp> getSuppByMateMonth(Map<String, Object> map);
	/**
	 * 查看 供应商的产能上报
	 * @param map
	 * @return
	 */
	List<PdrDetailVo> getPrdDetailByMap(Map<String, Object> map);
	/**
	 * 获取供应商汇总的内部管控表
	 * @param list
	 * @return
	 */
	List<InnerControl> getSuppSumInnerControl(List<InnerControl> list);
	
}
