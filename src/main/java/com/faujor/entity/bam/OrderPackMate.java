package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class OrderPackMate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;	//varcher2	50	编号
	private String orderPackId;	//	varcher2	50	关联包材采购订单编号
	private String mateCode;	//	VARCHER2    50		料号
	private String mateName;	//	varchar2		品名（物料名称）
	private String packCode;	//	varcher2	50	包材编号
	private String packName;	//	varchar2	100	包材名称
	private Double packNum	;	//number(13,3)		采购数量(包材数量)
	private String packSupp;	//	varchar2	100	包材供应商
	private String orderCode;	//	varchar2	50	订单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date orderDate;	//	date		订单日期
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date deliDate;	//	date		交货日期
	private String acceFileId;	//	varchar2	50	订单附件编号
	private String acceFileName;	//	varchar2	100	订单附件名称
	private String acceFileUrl;	//	varchar2	200	订单附件url
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
	 * @return the packNum
	 */
	public Double getPackNum() {
		return packNum;
	}
	/**
	 * @param packNum the packNum to set
	 */
	public void setPackNum(Double packNum) {
		this.packNum = packNum;
	}
	/**
	 * @return the packSupp
	 */
	public String getPackSupp() {
		return packSupp;
	}
	/**
	 * @param packSupp the packSupp to set
	 */
	public void setPackSupp(String packSupp) {
		this.packSupp = packSupp;
	}
	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}
	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the deliDate
	 */
	public Date getDeliDate() {
		return deliDate;
	}
	/**
	 * @param deliDate the deliDate to set
	 */
	public void setDeliDate(Date deliDate) {
		this.deliDate = deliDate;
	}
	/**
	 * @return the acceFileId
	 */
	public String getAcceFileId() {
		return acceFileId;
	}
	/**
	 * @param acceFileId the acceFileId to set
	 */
	public void setAcceFileId(String acceFileId) {
		this.acceFileId = acceFileId;
	}
	/**
	 * @return the acceFileName
	 */
	public String getAcceFileName() {
		return acceFileName;
	}
	/**
	 * @param acceFileName the acceFileName to set
	 */
	public void setAcceFileName(String acceFileName) {
		this.acceFileName = acceFileName;
	}
	/**
	 * @return the acceFileUrl
	 */
	public String getAcceFileUrl() {
		return acceFileUrl;
	}
	/**
	 * @param acceFileUrl the acceFileUrl to set
	 */
	public void setAcceFileUrl(String acceFileUrl) {
		this.acceFileUrl = acceFileUrl;
	}

	
	
}
