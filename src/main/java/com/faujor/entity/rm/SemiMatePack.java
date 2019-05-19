package com.faujor.entity.rm;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SemiMatePack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String oemOrderCode; //OEM订单编码
	private Date orderDate;		//订单日期
	private String oemSapId;		//OEM供应商编码
	private String oemSuppName;		//OEM供应商名称
	private String mateCode;		//物料编码--料号
	private String mateName;		//物料名称--品名
	private Double purcQuan;		//采购数量
	private Double quanMate;		//已交数量
	private Double unpaQuan;		//未交数量
	private String packCode;	//	varcher2	50	包材编码
	private String packName;	//	varchar2	100	包材名称
	private Double packTotalNum;	//	number(13,3)		包材总量
	private Double placedNum;	//	number(13,3)		已下单数量
	private Double residueNum;	//	number(13,3)		剩余可下单数量
	/**
	 * @return the oemOrderCode
	 */
	public String getOemOrderCode() {
		return oemOrderCode;
	}
	/**
	 * @param oemOrderCode the oemOrderCode to set
	 */
	public void setOemOrderCode(String oemOrderCode) {
		this.oemOrderCode = oemOrderCode;
	}
	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the oemSapId
	 */
	public String getOemSapId() {
		return oemSapId;
	}
	/**
	 * @param oemSapId the oemSapId to set
	 */
	public void setOemSapId(String oemSapId) {
		this.oemSapId = oemSapId;
	}
	/**
	 * @return the oemSuppName
	 */
	public String getOemSuppName() {
		return oemSuppName;
	}
	/**
	 * @param oemSuppName the oemSuppName to set
	 */
	public void setOemSuppName(String oemSuppName) {
		this.oemSuppName = oemSuppName;
	}
	/**
	 * @return the mateCode
	 */
	public String getMateCode() {
		return mateCode;
	}
	/**
	 * @param mateCode the mateCode to set
	 */
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	/**
	 * @return the mateName
	 */
	public String getMateName() {
		return mateName;
	}
	/**
	 * @param mateName the mateName to set
	 */
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	/**
	 * @return the purcQuan
	 */
	public Double getPurcQuan() {
		return purcQuan;
	}
	/**
	 * @param purcQuan the purcQuan to set
	 */
	public void setPurcQuan(Double purcQuan) {
		this.purcQuan = purcQuan;
	}
	/**
	 * @return the quanMate
	 */
	public Double getQuanMate() {
		return quanMate;
	}
	/**
	 * @param quanMate the quanMate to set
	 */
	public void setQuanMate(Double quanMate) {
		this.quanMate = quanMate;
	}
	/**
	 * @return the unpaQuan
	 */
	public Double getUnpaQuan() {
		return unpaQuan;
	}
	/**
	 * @param unpaQuan the unpaQuan to set
	 */
	public void setUnpaQuan(Double unpaQuan) {
		this.unpaQuan = unpaQuan;
	}
	/**
	 * @return the packCode
	 */
	public String getPackCode() {
		return packCode;
	}
	/**
	 * @param packCode the packCode to set
	 */
	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	/**
	 * @return the packName
	 */
	public String getPackName() {
		return packName;
	}
	/**
	 * @param packName the packName to set
	 */
	public void setPackName(String packName) {
		this.packName = packName;
	}
	/**
	 * @return the packTotalNum
	 */
	public Double getPackTotalNum() {
		return packTotalNum;
	}
	/**
	 * @param packTotalNum the packTotalNum to set
	 */
	public void setPackTotalNum(Double packTotalNum) {
		this.packTotalNum = packTotalNum;
	}
	/**
	 * @return the placedNum
	 */
	public Double getPlacedNum() {
		return placedNum;
	}
	/**
	 * @param placedNum the placedNum to set
	 */
	public void setPlacedNum(Double placedNum) {
		this.placedNum = placedNum;
	}
	/**
	 * @return the residueNum
	 */
	public Double getResidueNum() {
		return residueNum;
	}
	/**
	 * @param residueNum the residueNum to set
	 */
	public void setResidueNum(Double residueNum) {
		this.residueNum = residueNum;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SemiMatePack [oemOrderCode=" + oemOrderCode + ", orderDate=" + orderDate + ", oemSapId=" + oemSapId
				+ ", oemSuppName=" + oemSuppName + ", mateCode=" + mateCode + ", mateName=" + mateName + ", purcQuan="
				+ purcQuan + ", quanMate=" + quanMate + ", unpaQuan=" + unpaQuan + ", packCode=" + packCode
				+ ", packName=" + packName + ", packTotalNum=" + packTotalNum + ", placedNum=" + placedNum
				+ ", residueNum=" + residueNum + "]";
	}

	
}
