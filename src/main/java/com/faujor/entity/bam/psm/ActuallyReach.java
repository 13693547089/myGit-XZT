package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实际达成
 * @author hql
 *
 */
public class ActuallyReach implements Serializable {

	private static final long serialVersionUID = 1L;
//	品相编码
	private String itemCode;
//	品项描述
	private String itemName;
//	物料名称
	private String mateCode;
//	物料描述
	private String mateDesc;
//	供应商编码
	private String suppNo;
//	供应商名称
	private String suppName;
//	排名
	private String rank;
//	计划月份
	private Date planMonth; 
//	生产计划
	private BigDecimal planPrdNum; 
//	实际生产
	private BigDecimal actPrdNum;
//	实际生产达成
	private String pudReachScale;
//	交货计划
	private BigDecimal planDlvNum;
//	实际交货
	private BigDecimal actDlvNum;
//	交货达成
	private String dlvReachScale;
//	供应商实际库存
	private BigDecimal suppActNum;
//	下个月的交货计划
	private  BigDecimal nextDevNum;
//	安全库存率
	private String safeScale;
	//产品系列编码
	private String prodSeriesCode;
//	产品系列描述
	private String prodSeriesDesc;
	
	
	
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
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Date getPlanMonth() {
		return planMonth;
	}
	public void setPlanMonth(Date planMonth) {
		this.planMonth = planMonth;
	}
	public BigDecimal getPlanPrdNum() {
		return planPrdNum;
	}
	public void setPlanPrdNum(BigDecimal planPrdNum) {
		this.planPrdNum = planPrdNum;
	}
	public BigDecimal getActPrdNum() {
		return actPrdNum;
	}
	public void setActPrdNum(BigDecimal actPrdNum) {
		this.actPrdNum = actPrdNum;
	}
	public String getPudReachScale() {
		return pudReachScale;
	}
	public void setPudReachScale(String pudReachScale) {
		this.pudReachScale = pudReachScale;
	}
	public BigDecimal getPlanDlvNum() {
		return planDlvNum;
	}
	public void setPlanDlvNum(BigDecimal planDlvNum) {
		this.planDlvNum = planDlvNum;
	}
	public BigDecimal getActDlvNum() {
		return actDlvNum;
	}
	public void setActDlvNum(BigDecimal actDlvNum) {
		this.actDlvNum = actDlvNum;
	}
	public String getDlvReachScale() {
		return dlvReachScale;
	}
	public void setDlvReachScale(String dlvReachScale) {
		this.dlvReachScale = dlvReachScale;
	}
	public BigDecimal getSuppActNum() {
		return suppActNum;
	}
	public void setSuppActNum(BigDecimal suppActNum) {
		this.suppActNum = suppActNum;
	}
	public String getSafeScale() {
		return safeScale;
	}
	public void setSafeScale(String safeScale) {
		this.safeScale = safeScale;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public BigDecimal getNextDevNum() {
		return nextDevNum;
	}
	public void setNextDevNum(BigDecimal nextDevNum) {
		this.nextDevNum = nextDevNum;
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
	
}
