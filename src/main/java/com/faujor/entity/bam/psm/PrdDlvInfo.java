package com.faujor.entity.bam.psm;

import java.math.BigDecimal;

/**
 * 生产交货详情
 * @author hql
 *
 */
public class PrdDlvInfo {
	
//	生产计划
	private BigDecimal planPrdNum; 
//	交货计划
	private BigDecimal planDlvNum;
//	期末库存
	private BigDecimal endStock;
//	期末预计安全库存率
	private String safeScale;
	
	public BigDecimal getPlanPrdNum() {
		return planPrdNum;
	}
	public void setPlanPrdNum(BigDecimal planPrdNum) {
		this.planPrdNum = planPrdNum;
	}
	public BigDecimal getPlanDlvNum() {
		return planDlvNum;
	}
	public void setPlanDlvNum(BigDecimal planDlvNum) {
		this.planDlvNum = planDlvNum;
	}
	public BigDecimal getEndStock() {
		return endStock;
	}
	public void setEndStock(BigDecimal endStock) {
		this.endStock = endStock;
	}
	public String getSafeScale() {
		return safeScale;
	}
	public void setSafeScale(String safeScale) {
		this.safeScale = safeScale;
	}
	
}
