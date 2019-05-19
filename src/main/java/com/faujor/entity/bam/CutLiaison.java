package com.faujor.entity.bam;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class CutLiaison implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String liaiId;// VARCHAR2(50) NOT NULL,
	private String status;// VARCHAR2(50),
	private String cutMonth;// VARCHAR2(50),
	private String liaiCode;// VARCHAR2(50),
	private String suppId;//VARCHAR2(50),
	private String suppName;// VARCHAR2(50),
	private String createId;//VARCHAR2(50),
	private String creator;// VARCHAR2(50),
	private String  fields ;
	private String  citeLiaiCode;//引用的包材打切联络单号
	private String isSpecial;//是否是特殊打切联络单；
	private Object  fieldsBlob;//存储物料列表的列头信息，数据库对应数据类型为Blob
 	private String canceUser;//作废人员
 	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date  canceDate;//作废打切联络单的时间
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;// VARCHAR2(50)
	
	private String suppInfo;
	public CutLiaison() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CutLiaison(String liaiId, String status, String cutMonth, String liaiCode, String suppId, String suppName,
			String createId, String creator, String fields, String citeLiaiCode, String isSpecial, Object fieldsBlob,
			String canceUser, Date canceDate, Date createDate, String suppInfo) {
		super();
		this.liaiId = liaiId;
		this.status = status;
		this.cutMonth = cutMonth;
		this.liaiCode = liaiCode;
		this.suppId = suppId;
		this.suppName = suppName;
		this.createId = createId;
		this.creator = creator;
		this.fields = fields;
		this.citeLiaiCode = citeLiaiCode;
		this.isSpecial = isSpecial;
		this.fieldsBlob = fieldsBlob;
		this.canceUser = canceUser;
		this.canceDate = canceDate;
		this.createDate = createDate;
		this.suppInfo = suppInfo;
	}


	/**
	 * @return the citeLiaiCode
	 */
	public String getCiteLiaiCode() {
		return citeLiaiCode;
	}

	/**
	 * @param citeLiaiCode the citeLiaiCode to set
	 */
	public void setCiteLiaiCode(String citeLiaiCode) {
		this.citeLiaiCode = citeLiaiCode;
	}

	/**
	 * @return the fields
	 */
	public String getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}

	/**
	 * @return the suppInfo
	 */
	public String getSuppInfo() {
		return suppInfo;
	}

	/**
	 * @param suppInfo the suppInfo to set
	 */
	public void setSuppInfo(String suppInfo) {
		this.suppInfo = suppInfo;
	}

	/**
	 * @return the liaiId
	 */
	public String getLiaiId() {
		return liaiId;
	}
	/**
	 * @param liaiId the liaiId to set
	 */
	public void setLiaiId(String liaiId) {
		this.liaiId = liaiId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
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
	 * @return the liaiCode
	 */
	public String getLiaiCode() {
		return liaiCode;
	}
	/**
	 * @param liaiCode the liaiCode to set
	 */
	public void setLiaiCode(String liaiCode) {
		this.liaiCode = liaiCode;
	}
	/**
	 * @return the suppId
	 */
	public String getSuppId() {
		return suppId;
	}
	/**
	 * @param suppId the suppId to set
	 */
	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}
	/**
	 * @return the suppName
	 */
	public String getSuppName() {
		return suppName;
	}
	/**
	 * @param suppName the suppName to set
	 */
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	/**
	 * @return the createId
	 */
	public String getCreateId() {
		return createId;
	}
	/**
	 * @param createId the createId to set
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * @return the isSpecial
	 */
	public String getIsSpecial() {
		return isSpecial;
	}

	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	/**
	 * @return the fieldsBlob
	 */
	public Object getFieldsBlob() {
		return fieldsBlob;
	}

	/**
	 * @param fieldsBlob the fieldsBlob to set
	 */
	public void setFieldsBlob(Object fieldsBlob) {
		this.fieldsBlob = fieldsBlob;
	}

	/**
	 * @return the canceUser
	 */
	public String getCanceUser() {
		return canceUser;
	}

	/**
	 * @param canceUser the canceUser to set
	 */
	public void setCanceUser(String canceUser) {
		this.canceUser = canceUser;
	}

	/**
	 * @return the canceDate
	 */
	public Date getCanceDate() {
		return canceDate;
	}

	/**
	 * @param canceDate the canceDate to set
	 */
	public void setCanceDate(Date canceDate) {
		this.canceDate = canceDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutLiaison [liaiId=" + liaiId + ", status=" + status + ", cutMonth=" + cutMonth + ", liaiCode="
				+ liaiCode + ", suppId=" + suppId + ", suppName=" + suppName + ", createId=" + createId + ", creator="
				+ creator + ", fields=" + fields + ", citeLiaiCode=" + citeLiaiCode + ", isSpecial=" + isSpecial
				+ ", fieldsBlob=" + fieldsBlob + ", canceUser=" + canceUser + ", canceDate=" + canceDate
				+ ", createDate=" + createDate + ", suppInfo=" + suppInfo + "]";
	}

	

	
}
