package com.faujor.entity.fam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 采购对账单物资信息
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PurchReconMate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	//对账单号
	private String reconCode;
	//入库日期
	private Date incomingDate;
	//采购订单号
	private String purchCode;
	//供应商范围
	private String suppScope;
	//物料编码
	private String mateCode;
	//物料名称
	private String mateDesc;
	//单位编码
	private String unitCode;
	//单位描述
	private String unitDesc;
	//仓库
	private String wareHouse;
	//入库数量
	private BigDecimal incomingNum;
	//含税单价
	private BigDecimal taxPrice;
	//入库单价
	private BigDecimal incomingPrice;
	//入库总价
	private BigDecimal incomingTotalPrice;
	//入库单号
	private String incomingCode;
	//行项目号
	private String rowNum;
	//内向交货单号
	private String innerDeliveryCode;
	//订单原因
	private String orderReason;
	//以折金额
	private BigDecimal discountAmount;
	//折前含税价
	private BigDecimal taxCost;
	//进场支持单价
	private BigDecimal supportPrice;
	//进场支持费用
	private BigDecimal supportCost;
	//供应商子范围编码
	private String suppRange;
	//供应商子范围描述
	private String suppRangeDesc;
	//出厂价
	private BigDecimal factoryPrice;
	//入库运费
	private BigDecimal incomingFreight;
	//所属CDC
	private String theirCdc;
	
	private String loan;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReconCode() {
		return reconCode;
	}
	public void setReconCode(String reconCode) {
		this.reconCode = reconCode;
	}
	public Date getIncomingDate() {
		return incomingDate;
	}
	public void setIncomingDate(Date incomingDate) {
		this.incomingDate = incomingDate;
	}
	public String getPurchCode() {
		return purchCode;
	}
	public void setPurchCode(String purchCode) {
		this.purchCode = purchCode;
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
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitDesc() {
		return unitDesc;
	}
	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}
	public String getWareHouse() {
		return wareHouse;
	}
	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}
	public BigDecimal getIncomingNum() {
		return incomingNum;
	}
	public void setIncomingNum(BigDecimal incomingNum) {
		this.incomingNum = incomingNum;
	}
	public BigDecimal getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}
	public BigDecimal getIncomingPrice() {
		return incomingPrice;
	}
	public void setIncomingPrice(BigDecimal incomingPrice) {
		this.incomingPrice = incomingPrice;
	}
	public BigDecimal getIncomingTotalPrice() {
		return incomingTotalPrice;
	}
	public void setIncomingTotalPrice(BigDecimal incomingTotalPrice) {
		this.incomingTotalPrice = incomingTotalPrice;
	}
	public String getIncomingCode() {
		return incomingCode;
	}
	public void setIncomingCode(String incomingCode) {
		this.incomingCode = incomingCode;
	}
	public String getInnerDeliveryCode() {
		return innerDeliveryCode;
	}
	public void setInnerDeliveryCode(String innerDeliveryCode) {
		this.innerDeliveryCode = innerDeliveryCode;
	}
	public String getOrderReason() {
		return orderReason;
	}
	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	public BigDecimal getTaxCost() {
		return taxCost;
	}
	public void setTaxCost(BigDecimal taxCost) {
		this.taxCost = taxCost;
	}
	public BigDecimal getSupportPrice() {
		return supportPrice;
	}
	public void setSupportPrice(BigDecimal supportPrice) {
		this.supportPrice = supportPrice;
	}
	public BigDecimal getSupportCost() {
		return supportCost;
	}
	public void setSupportCost(BigDecimal supportCost) {
		this.supportCost = supportCost;
	}
	public String getLoan() {
		return loan;
	}
	public void setLoan(String loan) {
		this.loan = loan;
	}
	public String getRowNum() {
		return rowNum;
	}
	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	
	public String getSuppScope() {
		return suppScope;
	}
	public void setSuppScope(String suppScope) {
		this.suppScope = suppScope;
	}
	
	
	/**
	 * @return the suppRange
	 */
	public String getSuppRange() {
		return suppRange;
	}
	/**
	 * @param suppRange the suppRange to set
	 */
	public void setSuppRange(String suppRange) {
		this.suppRange = suppRange;
	}
	/**
	 * @return the suppRangeDesc
	 */
	public String getSuppRangeDesc() {
		return suppRangeDesc;
	}
	/**
	 * @param suppRangeDesc the suppRangeDesc to set
	 */
	public void setSuppRangeDesc(String suppRangeDesc) {
		this.suppRangeDesc = suppRangeDesc;
	}
	/**
	 * @return the factoryPrice
	 */
	public BigDecimal getFactoryPrice() {
		return factoryPrice;
	}
	/**
	 * @param factoryPrice the factoryPrice to set
	 */
	public void setFactoryPrice(BigDecimal factoryPrice) {
		this.factoryPrice = factoryPrice;
	}
	/**
	 * @return the incomingFreight
	 */
	public BigDecimal getIncomingFreight() {
		return incomingFreight;
	}
	/**
	 * @param incomingFreight the incomingFreight to set
	 */
	public void setIncomingFreight(BigDecimal incomingFreight) {
		this.incomingFreight = incomingFreight;
	}
	/**
	 * @return the theirCdc
	 */
	public String getTheirCdc() {
		return theirCdc;
	}
	/**
	 * @param theirCdc the theirCdc to set
	 */
	public void setTheirCdc(String theirCdc) {
		this.theirCdc = theirCdc;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((incomingCode == null) ? 0 : incomingCode.hashCode());
		result = prime * result + ((rowNum == null) ? 0 : rowNum.hashCode());
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
		PurchReconMate other = (PurchReconMate) obj;
		if (incomingCode == null) {
			if (other.incomingCode != null)
				return false;
		} else if (!incomingCode.equals(other.incomingCode))
			return false;
		if (rowNum == null) {
			if (other.rowNum != null)
				return false;
		} else if (!rowNum.equals(other.rowNum))
			return false;
		return true;
	}
}
