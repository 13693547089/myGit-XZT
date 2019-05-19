package com.faujor.entity.bam.delivery;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 收货单冲销，接口返回值实体
 * 
 * @author martian
 *
 */
public class OutDataDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("BOLNR")
	private String receiveNo;  //收货单号

	@JsonProperty("MSGTX")
	private String message;// 返回信息

	@JsonProperty("VBELN")
	private String inboDeliCode;// 内向交货单号

	@JsonProperty("MSGTY")
	private String flag;// 成功与否标识

	/**
	 * @return the receiveNo
	 */
	public String getReceiveNo() {
		return receiveNo;
	}

	/**
	 * @param receiveNo the receiveNo to set
	 */
	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	

	/**
	 * @return the inboDeliCode
	 */
	public String getInboDeliCode() {
		return inboDeliCode;
	}

	/**
	 * @param inboDeliCode the inboDeliCode to set
	 */
	public void setInboDeliCode(String inboDeliCode) {
		this.inboDeliCode = inboDeliCode;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	
}
