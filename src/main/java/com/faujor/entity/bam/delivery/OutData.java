package com.faujor.entity.bam.delivery;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 接口返回值实体
 * 
 * @author martian
 *
 */
public class OutData implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("BOLNR")
	private String receiveNo;

	@JsonProperty("EBELN")
	private String orderNo;// 采购订单号

	@JsonProperty("VBELN")
	private String deliveryNo;// 内向交货单号

	@JsonProperty("FLAG")
	private String flag;// 成功与否标识

	@JsonProperty("MESSAGE")
	private String errorMsg;// 错误信息

	public String getReceiveNo() {
		return receiveNo;
	}

	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
