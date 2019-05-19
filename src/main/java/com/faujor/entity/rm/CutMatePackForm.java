package com.faujor.entity.rm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CutMatePackForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cutMonth;		//打切月份
	private String oemSapId;		//OEM供应商编码
	private String oemSuppName;		//OEM供应商名称
	private String mateCode;		//物料编码--料号
	private String mateName;		//物料名称--品名
	private String baoSapId;	//	包材供应商编码
	private String baoSuppName;	//	包材供应商名称
	/**
	 * @return the cutMonth
	 */
	public String getCutMonth() {
		return cutMonth;
	}
	/**
	 * @param cutMonth the cutMonth to set
	 */
	public void setCutMonth(String cutMonth) {
		this.cutMonth = cutMonth;
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
	
	
	
	

	
}
