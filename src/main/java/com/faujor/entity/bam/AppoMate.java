package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)  ///将这个注解写在类上之后，就会忽略类中不存在的字段
public class AppoMate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String  
	private String id;// VARCHAR2(50) NOT NULL,
	private String appoId;// VARCHAR2(50),
	private String mateCode;//VARCHAR2(50),
	private String mateName;// VARCHAR2(30),
	private Double mateNumber;// NUMBER(10),
	private Double mateAmount;// NUMBER(10)//实际方量（供应商填写）
	private Double mateBluk;//物料的体积(整箱，单位立方分米)
	private Double predAmount;//预计放量（立方米） ==（物料的数量×物料的体积）/1000（程序计算，只是给供应商填写方量时作为一个参考的作用）
	private String remark;//备注
	public AppoMate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppoMate(String id, String appoId, String mateCode, String mateName, Double mateNumber, Double mateAmount,
			Double mateBluk, Double predAmount, String remark) {
		super();
		this.id = id;
		this.appoId = appoId;
		this.mateCode = mateCode;
		this.mateName = mateName;
		this.mateNumber = mateNumber;
		this.mateAmount = mateAmount;
		this.mateBluk = mateBluk;
		this.predAmount = predAmount;
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
	 * @return the mateCode
	 */
	public String getMateCode() {
		return mateCode;
	}
	/**
	 * @param mateCode the mateCode to set
	 */
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	/**
	 * @return the mateName
	 */
	public String getMateName() {
		return mateName;
	}
	/**
	 * @param mateName the mateName to set
	 */
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	/**
	 * @return the mateNumber
	 */
	public Double getMateNumber() {
		return mateNumber;
	}
	/**
	 * @param mateNumber the mateNumber to set
	 */
	public void setMateNumber(Double mateNumber) {
		this.mateNumber = mateNumber;
	}
	/**
	 * @return the mateAmount
	 */
	public Double getMateAmount() {
		return mateAmount;
	}
	/**
	 * @param mateAmount the mateAmount to set
	 */
	public void setMateAmount(Double mateAmount) {
		this.mateAmount = mateAmount;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * @return the mateBluk
	 */
	public Double getMateBluk() {
		return mateBluk;
	}


	/**
	 * @param mateBluk the mateBluk to set
	 */
	public void setMateBluk(Double mateBluk) {
		this.mateBluk = mateBluk;
	}


	/**
	 * @return the predAmount
	 */
	public Double getPredAmount() {
		return predAmount;
	}


	/**
	 * @param predAmount the predAmount to set
	 */
	public void setPredAmount(Double predAmount) {
		this.predAmount = predAmount;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppoMate [id=" + id + ", appoId=" + appoId + ", mateCode=" + mateCode + ", mateName=" + mateName
				+ ", mateNumber=" + mateNumber + ", mateAmount=" + mateAmount + ", mateBluk=" + mateBluk
				+ ", predAmount=" + predAmount + ", remark=" + remark + "]";
	}


	
	
}
