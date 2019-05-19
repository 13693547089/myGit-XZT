package com.faujor.entity.mdm;

import java.io.Serializable;

public class QualProc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 合格供应商对应的采购数据
	 */
	private String id;// VARCHAR2(50) NOT NULL,
	private String suppId;//  VARCHAR2(50),
	private String sapId;//  VARCHAR2(50),
	private String purcOrga;//  VARCHAR2(50),
	private String suppRange;//  VARCHAR2(50),供应商子范围编码
	private String suppRangeDesc;//  VARCHAR2(50),供应商子范围描述
	private String payClause;//  VARCHAR2(50),
	private String currCode;//  VARCHAR2(50),
	private String abcIden;//  VARCHAR2(20),
	private String suppGroup;//  VARCHAR2(50),
	public QualProc() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public QualProc(String id, String suppId, String sapId, String purcOrga, String suppRange, String suppRangeDesc,
			String payClause, String currCode, String abcIden, String suppGroup) {
		super();
		this.id = id;
		this.suppId = suppId;
		this.sapId = sapId;
		this.purcOrga = purcOrga;
		this.suppRange = suppRange;
		this.suppRangeDesc = suppRangeDesc;
		this.payClause = payClause;
		this.currCode = currCode;
		this.abcIden = abcIden;
		this.suppGroup = suppGroup;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSuppId() {
		return suppId;
	}
	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}
	public String getSapId() {
		return sapId;
	}
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}
	public String getPurcOrga() {
		return purcOrga;
	}
	public void setPurcOrga(String purcOrga) {
		this.purcOrga = purcOrga;
	}
	public String getSuppRange() {
		return suppRange;
	}
	public void setSuppRange(String suppRange) {
		this.suppRange = suppRange;
	}
	public String getPayClause() {
		return payClause;
	}
	public void setPayClause(String payClause) {
		this.payClause = payClause;
	}
	public String getCurrCode() {
		return currCode;
	}
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	public String getAbcIden() {
		return abcIden;
	}
	public void setAbcIden(String abcIden) {
		this.abcIden = abcIden;
	}
	public String getSuppGroup() {
		return suppGroup;
	}
	public void setSuppGroup(String suppGroup) {
		this.suppGroup = suppGroup;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * @return the suppRangeDesc
	 */
	public String getSuppRangeDesc() {
		return suppRangeDesc;
	}

	/**
	 * @param suppRangeDesc the suppRangeDesc to set
	 */
	public void setSuppRangeDesc(String suppRangeDesc) {
		this.suppRangeDesc = suppRangeDesc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QualProc [id=" + id + ", suppId=" + suppId + ", sapId=" + sapId + ", purcOrga=" + purcOrga
				+ ", suppRange=" + suppRange + ", suppRangeDesc=" + suppRangeDesc + ", payClause=" + payClause
				+ ", currCode=" + currCode + ", abcIden=" + abcIden + ", suppGroup=" + suppGroup + "]";
	}

	
	
	

}
