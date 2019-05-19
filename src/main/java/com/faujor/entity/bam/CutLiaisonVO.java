package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class CutLiaisonVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String liaiId;// VARCHAR2(50) NOT NULL,
	private String status;// VARCHAR2(50),
	private String cutMonth;// VARCHAR2(50),
	private String liaiCode;// VARCHAR2(50),
	private String  headerFields ;//头部字段
	private String  mateCode;// VARCHAR2(50),
	private String version;//物料版本
	private String  fields ;//VARCHAR2(4000)
	private String mainStru;//主包材
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
	 * @return the headerFields
	 */
	public String getHeaderFields() {
		return headerFields;
	}
	/**
	 * @param headerFields the headerFields to set
	 */
	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
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
	 * @return the mainStru
	 */
	public String getMainStru() {
		return mainStru;
	}
	/**
	 * @param mainStru the mainStru to set
	 */
	public void setMainStru(String mainStru) {
		this.mainStru = mainStru;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutLiaisonVO [liaiId=" + liaiId + ", status=" + status + ", cutMonth=" + cutMonth + ", liaiCode="
				+ liaiCode + ", headerFields=" + headerFields + ", mateCode=" + mateCode + ", version=" + version
				+ ", fields=" + fields + ", mainStru=" + mainStru + "]";
	}
	
	
	
	
	
}
