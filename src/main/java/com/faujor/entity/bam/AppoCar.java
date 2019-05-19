package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class AppoCar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id ;//VARCHAR2(50) NOT NULL,
	private String appoId;// VARCHAR2(50),
	private String carType;//  VARCHAR2(50),
	private Integer carNumber;// NUMBER(10),
	private String remark;// VARCHAR2(200)
	public AppoCar() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppoCar(String id, String appoId, String carType, Integer carNumber, String remark) {
		super();
		this.id = id;
		this.appoId = appoId;
		this.carType = carType;
		this.carNumber = carNumber;
		this.remark = remark;
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
	 * @return the appoId
	 */
	public String getAppoId() {
		return appoId;
	}
	/**
	 * @param appoId the appoId to set
	 */
	public void setAppoId(String appoId) {
		this.appoId = appoId;
	}
	/**
	 * @return the carType
	 */
	public String getCarType() {
		return carType;
	}
	/**
	 * @param carType the carType to set
	 */
	public void setCarType(String carType) {
		this.carType = carType;
	}
	/**
	 * @return the carNumber
	 */
	public Integer getCarNumber() {
		return carNumber;
	}
	/**
	 * @param carNumber the carNumber to set
	 */
	public void setCarNumber(Integer carNumber) {
		this.carNumber = carNumber;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppoCar [id=" + id + ", appoId=" + appoId + ", carType=" + carType + ", carNumber=" + carNumber
				+ ", remark=" + remark + "]";
	}
	
	
	
}
