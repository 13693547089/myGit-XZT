package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppoQueryDO implements Serializable {
	private static final long serialVersionUID = -6872722373261566850L;

	private String reportTitle;// 描述
	private int billNum;// 单据数量
	private int billBackNum;// 方量(后)
	private String billStr = "0";// 方量展示值

	private double amountNum;// 方量(前)
	private double amountBackNum;// 方量(后)
	private String amountStr = "0.0";// 方量展示值
	private int truckNum;// 车辆(前)
	private int truckBackNum;// 车辆(后)
	private String truckStr = "0";// 车辆展示值

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public int getBillNum() {
		return billNum;
	}

	public void setBillNum(int billNum) {
		this.billNum = billNum;
	}

	public int getBillBackNum() {
		return billBackNum;
	}

	public void setBillBackNum(int billBackNum) {
		this.billBackNum = billBackNum;
	}

	public String getBillStr() {
		return billStr;
	}

	public void setBillStr(String billStr) {
		this.billStr = billStr;
	}

	public double getAmountNum() {
		return amountNum;
	}

	public void setAmountNum(double amountNum) {
		this.amountNum = amountNum;
	}

	public double getAmountBackNum() {
		return amountBackNum;
	}

	public void setAmountBackNum(double amountBackNum) {
		this.amountBackNum = amountBackNum;
	}

	public String getAmountStr() {
		return amountStr;
	}

	public void setAmountStr(String amountStr) {
		this.amountStr = amountStr;
	}

	public int getTruckNum() {
		return truckNum;
	}

	public void setTruckNum(int truckNum) {
		this.truckNum = truckNum;
	}

	public int getTruckBackNum() {
		return truckBackNum;
	}

	public void setTruckBackNum(int truckBackNum) {
		this.truckBackNum = truckBackNum;
	}

	public String getTruckStr() {
		return truckStr;
	}

	public void setTruckStr(String truckStr) {
		this.truckStr = truckStr;
	}
}
