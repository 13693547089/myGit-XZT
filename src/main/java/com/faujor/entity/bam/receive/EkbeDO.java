package com.faujor.entity.bam.receive;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EkbeDO implements Serializable {
	private static final long serialVersionUID = 5108079029652862616L;

	private String orderNo;// 采购订单
	private String frequency;// 项次
	private String inboDeliNo;// 内向交货单
	private String successIden;// 成功与否表 E,成功
	private String pnIden; // 正反标识 101+,102-
	private Double menge; // 单据数量

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getInboDeliNo() {
		return inboDeliNo;
	}

	public void setInboDeliNo(String inboDeliNo) {
		this.inboDeliNo = inboDeliNo;
	}

	public String getSuccessIden() {
		return successIden;
	}

	public void setSuccessIden(String successIden) {
		this.successIden = successIden;
	}

	public String getPnIden() {
		return pnIden;
	}

	public void setPnIden(String pnIden) {
		this.pnIden = pnIden;
	}

	public Double getMenge() {
		return menge;
	}

	public void setMenge(Double menge) {
		this.menge = menge;
	}
}
