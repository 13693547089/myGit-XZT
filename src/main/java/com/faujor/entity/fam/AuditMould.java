package com.faujor.entity.fam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditMould implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String auditId;
	private String mouldName;
	private float mouldNum;
	private int mouldStatus;
	private String storePlace;
	private String qualRate;
	private String belongRight;
	private String imgName;
	private String imgUrl;
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getMouldName() {
		return mouldName;
	}

	public void setMouldName(String mouldName) {
		this.mouldName = mouldName;
	}

	public float getMouldNum() {
		return mouldNum;
	}

	public void setMouldNum(float mouldNum) {
		this.mouldNum = mouldNum;
	}

	public int getMouldStatus() {
		return mouldStatus;
	}

	public void setMouldStatus(int mouldStatus) {
		this.mouldStatus = mouldStatus;
	}

	public String getStorePlace() {
		return storePlace;
	}

	public void setStorePlace(String storePlace) {
		this.storePlace = storePlace;
	}

	public String getQualRate() {
		return qualRate;
	}

	public void setQualRate(String qualRate) {
		this.qualRate = qualRate;
	}

	public String getBelongRight() {
		return belongRight;
	}

	public void setBelongRight(String belongRight) {
		this.belongRight = belongRight;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
