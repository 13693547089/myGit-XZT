package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class CutStructure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String struId;// VARCHAR2(50) NOT NULL,
	private String inveType;// VARCHAR2(50),
	private String classCode;// VARCHAR2(50),
	private String className;// VARCHAR2(50),
	private String contentCode;// VARCHAR2(50),
	private String contentName;// VARCHAR2(50),
	private String unit;// VARCHAR2(30),
	private String remark;// VARCHAR2(100)
	private String converRule; //折算规则   A：表示按箱入数折算，B：表示1:1折算
	public CutStructure() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CutStructure(String struId, String inveType, String classCode, String className, String contentCode,
			String contentName, String unit, String remark, String converRule) {
		super();
		this.struId = struId;
		this.inveType = inveType;
		this.classCode = classCode;
		this.className = className;
		this.contentCode = contentCode;
		this.contentName = contentName;
		this.unit = unit;
		this.remark = remark;
		this.converRule = converRule;
	}

	/**
	 * @return the struId
	 */
	public String getStruId() {
		return struId;
	}
	/**
	 * @param struId the struId to set
	 */
	public void setStruId(String struId) {
		this.struId = struId;
	}
	/**
	 * @return the inveType
	 */
	public String getInveType() {
		return inveType;
	}
	/**
	 * @param inveType the inveType to set
	 */
	public void setInveType(String inveType) {
		this.inveType = inveType;
	}
	/**
	 * @return the classCode
	 */
	public String getClassCode() {
		return classCode;
	}
	/**
	 * @param classCode the classCode to set
	 */
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the contentCode
	 */
	public String getContentCode() {
		return contentCode;
	}
	/**
	 * @param contentCode the contentCode to set
	 */
	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}
	/**
	 * @return the contentName
	 */
	public String getContentName() {
		return contentName;
	}
	/**
	 * @param contentName the contentName to set
	 */
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the converRule
	 */
	public String getConverRule() {
		return converRule;
	}

	/**
	 * @param converRule the converRule to set
	 */
	public void setConverRule(String converRule) {
		this.converRule = converRule;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutStructure [struId=" + struId + ", inveType=" + inveType + ", classCode=" + classCode + ", className="
				+ className + ", contentCode=" + contentCode + ", contentName=" + contentName + ", unit=" + unit
				+ ", remark=" + remark + ", converRule=" + converRule + "]";
	}
	
	
	
}
