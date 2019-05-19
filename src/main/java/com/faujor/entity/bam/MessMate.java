package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessMate implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;// VARCHAR2(50) NOT NULL,
	private String messId;// 主键ID
	private String mateCode;// 成品物料编码
	private String mateName;// 成品物料名称
	private Double mateNumber;// 调拨单的数量，相当于预约数量
	private Double mateAmount;// 方量
	private String orderId; // 调拨单号
	private String unit;// 单位
	private String frequency;// 调拨项次
	private String poId; // 半成品采购订单
	private String semiMateCode;// 半成品物料编码
	private String semiMateName;// 半成品物料名称
	private Double semiMateNumber;// 半成品物料数量，相当于送货量
	private String semiMateAmount;// 半成品物料方量
	private String semiUnit;// 半成品物料单位
	private String semiFrequency;// 半成品物料调拨项次
	private Double calculNumber;// 计算未交量
	private Double unpaNumber;// 订单未交量
	@DateTimeFormat(pattern = "yyy-MM-dd")
	private Date subeDate;// 订单日期
	private String zzoem; // zzoem供应商编码sap_id
	private Double semiVolume; // 半成品体积
	private String suppRange; // 供应商子范围编码

	
	/**
	 * @return the suppRange
	 */
	public String getSuppRange() {
		return suppRange;
	}

	/**
	 * @param suppRange the suppRange to set
	 */
	public void setSuppRange(String suppRange) {
		this.suppRange = suppRange;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessId() {
		return messId;
	}

	public void setMessId(String messId) {
		this.messId = messId;
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

	public Double getMateNumber() {
		return mateNumber;
	}

	public void setMateNumber(Double mateNumber) {
		this.mateNumber = mateNumber;
	}

	public Double getMateAmount() {
		return mateAmount;
	}

	public void setMateAmount(Double mateAmount) {
		this.mateAmount = mateAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public String getSemiMateCode() {
		return semiMateCode;
	}

	public void setSemiMateCode(String semiMateCode) {
		this.semiMateCode = semiMateCode;
	}

	public String getSemiMateName() {
		return semiMateName;
	}

	public void setSemiMateName(String semiMateName) {
		this.semiMateName = semiMateName;
	}

	public Double getSemiMateNumber() {
		return semiMateNumber;
	}

	public void setSemiMateNumber(Double semiMateNumber) {
		this.semiMateNumber = semiMateNumber;
	}

	public String getSemiMateAmount() {
		return semiMateAmount;
	}

	public void setSemiMateAmount(String semiMateAmount) {
		this.semiMateAmount = semiMateAmount;
	}

	public String getSemiUnit() {
		return semiUnit;
	}

	public void setSemiUnit(String semiUnit) {
		this.semiUnit = semiUnit;
	}

	public String getSemiFrequency() {
		return semiFrequency;
	}

	public void setSemiFrequency(String semiFrequency) {
		this.semiFrequency = semiFrequency;
	}

	public Double getCalculNumber() {
		return calculNumber;
	}

	public void setCalculNumber(Double calculNumber) {
		this.calculNumber = calculNumber;
	}

	public Double getUnpaNumber() {
		return unpaNumber;
	}

	public void setUnpaNumber(Double unpaNumber) {
		this.unpaNumber = unpaNumber;
	}

	public Date getSubeDate() {
		return subeDate;
	}

	public void setSubeDate(Date subeDate) {
		this.subeDate = subeDate;
	}

	public String getZzoem() {
		return zzoem;
	}

	public void setZzoem(String zzoem) {
		this.zzoem = zzoem;
	}

	public Double getSemiVolume() {
		return semiVolume;
	}

	public void setSemiVolume(Double semiVolume) {
		this.semiVolume = semiVolume;
	}
}
