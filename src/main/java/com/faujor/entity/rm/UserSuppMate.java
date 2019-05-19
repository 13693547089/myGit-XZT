package com.faujor.entity.rm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSuppMate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String userId;
	private String name;
	private String suppName;
	private String sapId;
	private String mateCode;
	private String mateName;
	private String mateGroupExpl;
	private String mateType;
	private String basicUnit;
	private String procUnit;
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the sapId
	 */
	public String getSapId() {
		return sapId;
	}
	/**
	 * @param sapId the sapId to set
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
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
	 * @return the mateGroupExpl
	 */
	public String getMateGroupExpl() {
		return mateGroupExpl;
	}
	/**
	 * @param mateGroupExpl the mateGroupExpl to set
	 */
	public void setMateGroupExpl(String mateGroupExpl) {
		this.mateGroupExpl = mateGroupExpl;
	}
	/**
	 * @return the mateType
	 */
	public String getMateType() {
		return mateType;
	}
	/**
	 * @param mateType the mateType to set
	 */
	public void setMateType(String mateType) {
		this.mateType = mateType;
	}
	/**
	 * @return the basicUnit
	 */
	public String getBasicUnit() {
		return basicUnit;
	}
	/**
	 * @param basicUnit the basicUnit to set
	 */
	public void setBasicUnit(String basicUnit) {
		this.basicUnit = basicUnit;
	}
	/**
	 * @return the procUnit
	 */
	public String getProcUnit() {
		return procUnit;
	}
	/**
	 * @param procUnit the procUnit to set
	 */
	public void setProcUnit(String procUnit) {
		this.procUnit = procUnit;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserSuppMate [userId=" + userId + ", name=" + name + ", suppName=" + suppName + ", sapId=" + sapId
				+ ", mateCode=" + mateCode + ", mateName=" + mateName + ", mateGroupExpl=" + mateGroupExpl
				+ ", mateType=" + mateType + ", basicUnit=" + basicUnit + ", procUnit=" + procUnit + "]";
	}
	
	
}
