package com.faujor.entity.bam.psm;

import java.io.Serializable;
/**
 * 备货计划详情
 * @author hql
 *
 */
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class InvenPlanDetail  implements Serializable{

	private static final long serialVersionUID = 1L;
//	id
	private String id;
//	备货计划Id
	private String mainId;
//	备货计划编号
	private String planCode;
//	状态
	private String status;
//	物料编码
	private String mateCode;
//	物料名称
	private String mateDesc;
//	排名
	private String ranking;
//	期初订单
	private BigDecimal beginOrder;
//	期初库存
	private BigDecimal beginStock;
//	期初可生产订单
	private BigDecimal beginEnableOrder;
//	生产计划
	private BigDecimal prodPlan;
//	期末预计库存
	private BigDecimal endStock;
//	交货计划
	private BigDecimal deliveryPlan;
//	库存安全率
	private String safeScale ;
	
	
	private boolean equal;
	
	//---------------扩展
	private String prodSeriesCode;
	
	private String prodSeriesDesc;
	
	private Date planMonth;
	
	private BigDecimal nextMonthDeliveryNum;
	
	
	// 项次
	private String itemCode;
	private String itemName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMateCode() {
		return mateCode;
	}
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	public String getMateDesc() {
		return mateDesc;
	}
	public void setMateDesc(String mateDesc) {
		this.mateDesc = mateDesc;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public BigDecimal getBeginOrder() {
		return beginOrder;
	}
	public void setBeginOrder(BigDecimal beginOrder) {
		this.beginOrder = beginOrder;
	}
	public BigDecimal getBeginStock() {
		return beginStock;
	}
	public void setBeginStock(BigDecimal beginStock) {
		this.beginStock = beginStock;
	}
	public BigDecimal getBeginEnableOrder() {
		return beginEnableOrder;
	}
	public void setBeginEnableOrder(BigDecimal beginEnableOrder) {
		this.beginEnableOrder = beginEnableOrder;
	}
	public BigDecimal getProdPlan() {
		return prodPlan;
	}
	public void setProdPlan(BigDecimal prodPlan) {
		this.prodPlan = prodPlan;
	}
	public BigDecimal getEndStock() {
		return endStock;
	}
	public void setEndStock(BigDecimal endStock) {
		this.endStock = endStock;
	}
	public BigDecimal getDeliveryPlan() {
		return deliveryPlan;
	}
	public void setDeliveryPlan(BigDecimal deliveryPlan) {
		this.deliveryPlan = deliveryPlan;
	}
	public String getSafeScale() {
		return safeScale;
	}
	public void setSafeScale(String safeScale) {
		this.safeScale = safeScale;
	}
	public String getProdSeriesCode() {
		return prodSeriesCode;
	}
	public void setProdSeriesCode(String prodSeriesCode) {
		this.prodSeriesCode = prodSeriesCode;
	}
	public String getProdSeriesDesc() {
		return prodSeriesDesc;
	}
	public void setProdSeriesDesc(String prodSeriesDesc) {
		this.prodSeriesDesc = prodSeriesDesc;
	}
	public Date getPlanMonth() {
		return planMonth;
	}
	public void setPlanMonth(Date planMonth) {
		this.planMonth = planMonth;
	}
	public BigDecimal getNextMonthDeliveryNum() {
		return nextMonthDeliveryNum;
	}
	public void setNextMonthDeliveryNum(BigDecimal nextMonthDeliveryNum) {
		this.nextMonthDeliveryNum = nextMonthDeliveryNum;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public boolean isEqual() {
		return equal;
	}
	public void setEqual(boolean equal) {
		this.equal = equal;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mateCode == null) ? 0 : mateCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvenPlanDetail other = (InvenPlanDetail) obj;
		if (mateCode == null) {
			if (other.mateCode != null)
				return false;
		} else if (!mateCode.equals(other.mateCode))
			return false;
		return true;
	}
}
