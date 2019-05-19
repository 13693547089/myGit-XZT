package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceMate implements Serializable {
	private static final long serialVersionUID = 1L;
	// private String
	private String id;// VARCHAR2(50) NOT NULL,
	private String receId;// VARCHAR2(50),
	private String orderId;// VARCHAR2(50),
	private String mateCode;// VARCHAR2(50),
	private String mateName;// VARCHAR2(30),
	private Double deliNumber;// NUMBER(10),
	private Double receNumber;// NUMBER(10),
	private String unit;// VARCHAR2(20),
	private String prodPatchNum;// VARCHAR2(50),
	private String remark;// VARCHAR2(100)
	private String frequency;// 项次
	private String inboDeliCode;// 内向交货单号 inbo_deli_code
	private String factoryAddr; // 工厂信息
	private String storLocation;// 库位信息
	private String isOccupy;// 是否占用

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReceId() {
		return receId;
	}

	public void setReceId(String receId) {
		this.receId = receId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public Double getDeliNumber() {
		return deliNumber;
	}

	public void setDeliNumber(Double deliNumber) {
		this.deliNumber = deliNumber;
	}

	public Double getReceNumber() {
		return receNumber;
	}

	public void setReceNumber(Double receNumber) {
		this.receNumber = receNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProdPatchNum() {
		return prodPatchNum;
	}

	public void setProdPatchNum(String prodPatchNum) {
		this.prodPatchNum = prodPatchNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getInboDeliCode() {
		return inboDeliCode;
	}

	public void setInboDeliCode(String inboDeliCode) {
		this.inboDeliCode = inboDeliCode;
	}

	public String getFactoryAddr() {
		return factoryAddr;
	}

	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}

	public String getStorLocation() {
		return storLocation;
	}

	public void setStorLocation(String storLocation) {
		this.storLocation = storLocation;
	}

	public String getIsOccypy() {
		return isOccupy;
	}

	public void setIsOccypy(String isOccupy) {
		this.isOccupy = isOccupy;
	}
}
