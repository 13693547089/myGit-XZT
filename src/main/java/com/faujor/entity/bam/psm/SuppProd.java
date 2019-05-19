package com.faujor.entity.bam.psm;

import java.io.Serializable;
/**
 * 供应商排产
 * @author hql
 *
 */
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuppProd implements Serializable {

	private static final long serialVersionUID = 1L;
	// id
	private String id;
	// 备货计划子表id
	private String mainId;
	// 状态
	private String status;
	// 物料编码
	private String mateCode;
	// 物料名称
	private String mateDesc;
	// 供应商编码
	private String suppNo;
	// 供应商名称
	private String suppName;
	// 排名
	private String ranking;
	// 期初订单
	private BigDecimal beginOrder;
	// 期初库存
	private BigDecimal beginStock;
	// 期初可生产订单
	private BigDecimal beginEnableOrder;
	// 生产计划
	private BigDecimal prodPlan;
	// 期末预计库存
	private BigDecimal endStock;
	// 交货计划
	private BigDecimal deliveryPlan;
	// 库存安全率
	private String remainNum;
	// 备货计划年月
	private Date planMonth;
	// 下个月的交货计划
	private BigDecimal nextDeliveryNum;
	// 安全库存率
	private String safeScale;
	// 项次
	private String itemCode;
	private String itemName;
	// 创建人
	private Long creator;

	
//	PROD_SERIES_CODE
	private String prodSeriesCode;
//	产品系列标书
	private String prodSeriesDesc;
	
	private String planDetailId;
	
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

	public String getSuppNo() {
		return suppNo;
	}

	public void setSuppNo(String suppNo) {
		this.suppNo = suppNo;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
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

	public String getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(String remainNum) {
		this.remainNum = remainNum;
	}

	public Date getPlanMonth() {
		return planMonth;
	}

	public void setPlanMonth(Date planMonth) {
		this.planMonth = planMonth;
	}

	public BigDecimal getNextDeliveryNum() {
		return nextDeliveryNum;
	}

	public void setNextDeliveryNum(BigDecimal nextDeliveryNum) {
		this.nextDeliveryNum = nextDeliveryNum;
	}

	public String getSafeScale() {
		return safeScale;
	}

	public void setSafeScale(String safeScale) {
		this.safeScale = safeScale;
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

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mateCode == null) ? 0 : mateCode.hashCode());
		result = prime * result + ((suppNo == null) ? 0 : suppNo.hashCode());
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
		SuppProd other = (SuppProd) obj;
		if (mateCode == null) {
			if (other.mateCode != null)
				return false;
		} else if (!mateCode.equals(other.mateCode))
			return false;
		if (suppNo == null) {
			if (other.suppNo != null)
				return false;
		} else if (!suppNo.equals(other.suppNo))
			return false;
		return true;
	}

	public String getPlanDetailId() {
		return planDetailId;
	}

	public void setPlanDetailId(String planDetailId) {
		this.planDetailId = planDetailId;
	}
}
