package com.faujor.entity.rm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackOrderForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String packOrderCode;//包材订单编号
	private String mateCode;
	private String mateName;
	private String packCode;
	private String packName;
	private String oemSapId;
	private String oemSuppName;
	private String baoSapId;
	private String baoSuppName;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	/**
	 * @return the packOrderCode
	 */
	public String getPackOrderCode() {
		return packOrderCode;
	}
	/**
	 * @param packOrderCode the packOrderCode to set
	 */
	public void setPackOrderCode(String packOrderCode) {
		this.packOrderCode = packOrderCode;
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
	 * @return the baoSapId
	 */
	public String getBaoSapId() {
		return baoSapId;
	}
	/**
	 * @param baoSapId the baoSapId to set
	 */
	public void setBaoSapId(String baoSapId) {
		this.baoSapId = baoSapId;
	}
	/**
	 * @return the baoSuppName
	 */
	public String getBaoSuppName() {
		return baoSuppName;
	}
	/**
	 * @param baoSuppName the baoSuppName to set
	 */
	public void setBaoSuppName(String baoSuppName) {
		this.baoSuppName = baoSuppName;
	}
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
	
	
	
	
}
