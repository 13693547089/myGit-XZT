package com.faujor.entity.bam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class CutBaoCaiMate implements Serializable {

	private static final long serialVersionUID = 1L;
	private String liaiMateId;// VARCHAR2(50) NOT NULL,
	private String  liaiId;// VARCHAR2(50),
	private String  mateCode;// VARCHAR2(50),
	private String mateName;// VARCHAR2(100),
	private String  fields ;//VARCHAR2(4000)
	private String version;//物料版本
	private String isSpecial;//是否是特殊打切品
	private String oemSuppName;//oem供应商
	private String oemSuppCode;//oem供应商编号

	public CutBaoCaiMate() {
		super();
	}

	public CutBaoCaiMate(String liaiMateId, String liaiId, String mateCode, String mateName,
                         String fields, String version,
                         String isSpecial,String oemSuppName,String oemSuppCode) {
		super();
		this.liaiMateId = liaiMateId;
		this.liaiId = liaiId;
		this.mateCode = mateCode;
		this.mateName = mateName;
		this.fields = fields;
		this.version = version;
		this.isSpecial = isSpecial;
		this.oemSuppName = oemSuppName;
		this.oemSuppCode = oemSuppCode;
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
	 * @return the liaiMateId
	 */
	public String getLiaiMateId() {
		return liaiMateId;
	}
	/**
	 * @param liaiMateId the liaiMateId to set
	 */
	public void setLiaiMateId(String liaiMateId) {
		this.liaiMateId = liaiMateId;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutLiaiMate [liaiMateId=" + liaiMateId + ", liaiId=" + liaiId
				+ ", mateCode=" + mateCode + ", mateName=" + mateName
				+ ", fields=" + fields + ", version=" + version + ", isSpecial=" + isSpecial
				+ ", oemSuppCode=" + oemSuppCode + ", oemSuppName=" + oemSuppName
				+ "]";
	}

	public String getOemSuppName() {
		return oemSuppName;
	}

	public void setOemSuppName(String oemSuppName) {
		this.oemSuppName = oemSuppName;
	}

	public String getOemSuppCode() {
		return oemSuppCode;
	}

	public void setOemSuppCode(String oemSuppCode) {
		this.oemSuppCode = oemSuppCode;
	}
}
