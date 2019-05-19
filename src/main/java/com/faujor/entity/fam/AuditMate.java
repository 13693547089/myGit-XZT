package com.faujor.entity.fam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditMate implements Serializable {
	private static final long serialVersionUID = 6439417588046194415L;
	private String id;
	private String auditId;
	private String mateId;
	private String mateCode;
	private String mateName;
	private float lastMonthBala;
	private float outWarehouse;
	private float inWarehouse;
	private float topLoss;
	private float suppLoss;
	private float noAdd;
	private float addLoss;
	private float monthStock;

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

	public String getMateId() {
		return mateId;
	}

	public void setMateId(String mateId) {
		this.mateId = mateId;
	}

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}

	public String getMateName() {
		return mateName;
	}

	public void setMateName(String mateName) {
		this.mateName = mateName;
	}

	public float getLastMonthBala() {
		return lastMonthBala;
	}

	public void setLastMonthBala(float lastMonthBala) {
		this.lastMonthBala = lastMonthBala;
	}

	public float getOutWarehouse() {
		return outWarehouse;
	}

	public void setOutWarehouse(float outWarehouse) {
		this.outWarehouse = outWarehouse;
	}

	public float getInWarehouse() {
		return inWarehouse;
	}

	public void setInWarehouse(float inWarehouse) {
		this.inWarehouse = inWarehouse;
	}

	public float getTopLoss() {
		return topLoss;
	}

	public void setTopLoss(float topLoss) {
		this.topLoss = topLoss;
	}

	public float getSuppLoss() {
		return suppLoss;
	}

	public void setSuppLoss(float suppLoss) {
		this.suppLoss = suppLoss;
	}

	public float getNoAdd() {
		return noAdd;
	}

	public void setNoAdd(float noAdd) {
		this.noAdd = noAdd;
	}

	public float getAddLoss() {
		return addLoss;
	}

	public void setAddLoss(float addLoss) {
		this.addLoss = addLoss;
	}

	public float getMonthStock() {
		return monthStock;
	}

	public void setMonthStock(float monthStock) {
		this.monthStock = monthStock;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
