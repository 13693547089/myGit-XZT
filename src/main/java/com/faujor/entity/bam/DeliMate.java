package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliMate implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// private String Date

	private String id;// VARCHAR2(50) NOT NULL,
	private String deliId;// VARCHAR2(50),
	private String orderId;// VARCHAR2(50),
	private String mateCode;// VARCHAR2(50),
	private String mateName;// VARCHAR2(30), --（SKU中文名称）

	private Double deliNumber;// NUMBER(10),送货数量
	private String unit;// VARCHAR2(20),
	private String prodPatchNum;// VARCHAR2(50),
	private String remark;// VARCHAR2(100)
	private String frequency;// 项次
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date subeDate;// sube_date,采购订单的日期
	private Double appoNumber;// 预约数量
	private Double unpaNumber;// 订单上的原始未交数量
	private Double calculNumber; // 计算得到的未交数量
	private String sort;
	private String addCate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeliId() {
		return deliId;
	}

	public void setDeliId(String deliId) {
		this.deliId = deliId;
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

	public Date getSubeDate() {
		return subeDate;
	}

	public void setSubeDate(Date subeDate) {
		this.subeDate = subeDate;
	}

	public Double getAppoNumber() {
		return appoNumber;
	}

	public void setAppoNumber(Double appoNumber) {
		this.appoNumber = appoNumber;
	}

	public Double getUnpaNumber() {
		return unpaNumber;
	}

	public void setUnpaNumber(Double unpaNumber) {
		this.unpaNumber = unpaNumber;
	}

	public Double getCalculNumber() {
		return calculNumber;
	}

	public void setCalculNumber(Double calculNumber) {
		this.calculNumber = calculNumber;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the addCate
	 */
	public String getAddCate() {
		return addCate;
	}

	/**
	 * @param addCate the addCate to set
	 */
	public void setAddCate(String addCate) {
		this.addCate = addCate;
	}
	
	
}
