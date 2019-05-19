package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StraMessAndMateDO implements Serializable {
	private static final long serialVersionUID = -9030955934189484522L;
	private String deliCode;// 送货单编码
	private String messCode;// 直发通知单的编码
	private String orderNo;// 采购订单编码
	private String mateCode;// 物料编码
	private Double unpaNumber;// 订单未交量
	private Double calculNumber; // 计算未交量
	private Double occupyNumber; // 直发送货量
	private String msg;// 计算过程中的报错信息

	public String getDeliCode() {
		return deliCode;
	}

	public void setDeliCode(String deliCode) {
		this.deliCode = deliCode;
	}

	public String getMessCode() {
		return messCode;
	}

	public void setMessCode(String messCode) {
		this.messCode = messCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
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

	public Double getOccupyNumber() {
		return occupyNumber;
	}

	public void setOccupyNumber(Double occupyNumber) {
		this.occupyNumber = occupyNumber;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
