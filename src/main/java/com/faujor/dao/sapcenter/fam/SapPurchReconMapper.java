package com.faujor.dao.sapcenter.fam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.faujor.entity.fam.PurchReconMate;

public interface SapPurchReconMapper {
	/**
	 * 获取采购凭证的物料列表
	 * @param map
	 * @return
	 */
	List<PurchReconMate> getPurchReconMateByMap(Map<String, Object> map);
	/**
	 * 获取物料或者供应商的交货数量
	 * @param map
	 * @return
	 */
	BigDecimal getDevNum(Map<String, Object> map);
	
}	
