package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class OrderPackMess implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;	//varcher2	50	编号
	private String orderPackId;	//	varcher2	50	关联包材采购订单编号
	private String frequency;	//	varchar2	20	项次
	private String mateCode;	//	varchar2	50	料号
	private String mateName;	//	varchar2	100	品名（物料名称）
	private String packElem;	//	varchar2	50	包材组件
	private String packCode;	//	varcher2	50	包材编码
	private String packName;	//	varchar2	100	包材名称
	private Double dosageNum;	//	number(13)		用量（整数）
	private Double packTotalNum;	//	number(13,3)		包材总量
	private Double placedNum;	//	number(13,3)		已下单数量
	private Double residueNum;	//	number(13,3)		剩余可下单数量
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
	 * @return the orderPackId
	 */
	public String getOrderPackId() {
		return orderPackId;
	}
	/**
	 * @param orderPackId the orderPackId to set
	 */
	public void setOrderPackId(String orderPackId) {
		this.orderPackId = orderPackId;
	}
	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
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
	 * @return the packElem
	 */
	public String getPackElem() {
		return packElem;
	}
	/**
	 * @param packElem the packElem to set
	 */
	public void setPackElem(String packElem) {
		this.packElem = packElem;
	}
	/**
	 * @return the packCode
	 */
	public String getPackCode() {
		return packCode;
	}
	/**
	 * @param packCode the packCode to set
	 */
	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	/**
	 * @return the packName
	 */
	public String getPackName() {
		return packName;
	}
	/**
	 * @param packName the packName to set
	 */
	public void setPackName(String packName) {
		this.packName = packName;
	}
	/**
	 * @return the dosageNum
	 */
	public Double getDosageNum() {
		return dosageNum;
	}
	/**
	 * @param dosageNum the dosageNum to set
	 */
	public void setDosageNum(Double dosageNum) {
		this.dosageNum = dosageNum;
	}
	/**
	 * @return the packTotalNum
	 */
	public Double getPackTotalNum() {
		return packTotalNum;
	}
	/**
	 * @param packTotalNum the packTotalNum to set
	 */
	public void setPackTotalNum(Double packTotalNum) {
		this.packTotalNum = packTotalNum;
	}
	/**
	 * @return the placedNum
	 */
	public Double getPlacedNum() {
		return placedNum;
	}
	/**
	 * @param placedNum the placedNum to set
	 */
	public void setPlacedNum(Double placedNum) {
		this.placedNum = placedNum;
	}
	/**
	 * @return the residueNum
	 */
	public Double getResidueNum() {
		return residueNum;
	}
	/**
	 * @param residueNum the residueNum to set
	 */
	public void setResidueNum(Double residueNum) {
		this.residueNum = residueNum;
	}

	
	
}
