package com.faujor.entity.rm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SemiMatePackForm implements Serializable {

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
	private String packCode;	//	varcher2	50	包材编码
	private String packName;	//	varchar2	100	包材名称
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;		//起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;		//截止日期
	
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
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
	

	
}
