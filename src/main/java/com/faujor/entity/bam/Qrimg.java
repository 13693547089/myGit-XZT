package com.faujor.entity.bam;

import java.io.Serializable;

public class Qrimg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String qrCode;
	private String qrUrl;
	public Qrimg() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Qrimg(String id, String qrCode, String qrUrl) {
		super();
		this.id = id;
		this.qrCode = qrCode;
		this.qrUrl = qrUrl;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the qrCode
	 */
	public String getQrCode() {
		return qrCode;
	}
	/**
	 * @param qrCode the qrCode to set
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	/**
	 * @return the qrUrl
	 */
	public String getQrUrl() {
		return qrUrl;
	}
	/**
	 * @param qrUrl the qrUrl to set
	 */
	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Qrimg [id=" + id + ", qrCode=" + qrCode + ", qrUrl=" + qrUrl + "]";
	}
	
	
}
