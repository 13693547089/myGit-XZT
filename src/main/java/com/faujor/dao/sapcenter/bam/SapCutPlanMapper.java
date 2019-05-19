package com.faujor.dao.sapcenter.bam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.faujor.entity.bam.CutPlanMate;

public interface SapCutPlanMapper {
	/**
	 * 从SAP获取预测信息
	 * @param map
	 * @return
	 */
	List<CutPlanMate> getCutPlanMateFromSap(Map<String, Object> map);
	/**
	 * 根据年月  物料获取销量预测
	 * @param map
	 * @return
	 */
	BigDecimal getForecastNumByMap(Map<String, Object> map);
	/**
	 * 根据年月  物料获取销量预测
	 * @param map
	 * @return
	 */
	BigDecimal getLastMonthStockByMap(Map<String, Object> map);
}