package com.faujor.entity.bam.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EkpoDO implements Serializable {
	private static final long serialVersionUID = 4035018637927805910L;
	private String orderNo;
	private String suppNo;
	private String zzoem;
	private String mateCode;
	private Double undeliveredNum;
	private String month;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSuppNo() {
		return suppNo;
	}

	public void setSuppNo(String suppNo) {
		this.suppNo = suppNo;
	}

	public String getZzoem() {
		return zzoem;
	}

	public void setZzoem(String zzoem) {
		this.zzoem = zzoem;
	}

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}

	public Double getUndeliveredNum() {
		return undeliveredNum;
	}

	public void setUndeliveredNum(Double undeliveredNum) {
		this.undeliveredNum = undeliveredNum;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
