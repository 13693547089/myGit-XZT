package com.faujor.entity.mdm;

import java.io.Serializable;

public class QualSuppDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String suppId; //VARCHAR2(50) NOT NULL,--供应商编码
	private String sapId; //VARCHAR2(50),          --sap编码
	private String suppName; //VARCHAR2(50),       --供应商名称
	private String username; //用户名称
	private String suppInfo;
	public QualSuppDO(String suppId, String sapId, String suppName, String username, String suppInfo) {
		super();
		this.suppId = suppId;
		this.sapId = sapId;
		this.suppName = suppName;
		this.username = username;
		this.suppInfo = suppInfo;
	}
	public QualSuppDO() {
		super();
		// TODO Auto-generated constructor stub
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QualSuppDO [suppId=" + suppId + ", sapId=" + sapId + ", suppName=" + suppName + ", username=" + username
				+ ", suppInfo=" + suppInfo + "]";
	}
	
	
}
