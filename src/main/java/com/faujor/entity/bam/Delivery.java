package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Delivery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String Date
	
	private String deliId;// VARCHAR2(50) NOT NULL,
	private String status;// VARCHAR2(20), ---‘已保存’，‘已发货’，‘已收货’
	private String deliCode;// VARCHAR2(50),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date deliDate;// DATE,   送货日期---
	private String affirmDate; //确认送货时间
	private String suppId;// VARCHAR2(50),
	private String  suppName;// VARCHAR2(30),
	private Double deliNumber;// NUMBER(10),
	private Double deliAmount;// NUMBER(10),
	private Integer truckNum;// NUMBER(10),
	private String createId;// VARCHAR2(50),
	private String creator;// VARCHAR2(20),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;// DATE,
	private String  deliType;// VARCHAR2(20),  --（1:预约送货）（0:直发送货）（2:特殊送货）
	private String mapgCode;// VARCHAR2(50),
	private String receUnit;// VARCHAR2(50),
	private String receAddr;// VARCHAR2(100),
	private String  contact ;//VARCHAR2(20),
	private String  phone ;//VARCHAR2(30),
	private String signerId ;//VARCHAR2(50),
	private String signer;// VARCHAR2(30),
	private String appoNumber;
	private String qrurl;
	private String post;//收货单位的岗位人员(useId)
	private String suppRange;//供应商子范围编码
	private String suppRangeDesc;//供应商子范围描述
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date signDate;// DATE
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	public Delivery() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Delivery(String deliId, String status, String deliCode, Date deliDate, String affirmDate, String suppId,
			String suppName, Double deliNumber, Double deliAmount, Integer truckNum, String createId, String creator,
			Date createDate, String deliType, String mapgCode, String receUnit, String receAddr, String contact,
			String phone, String signerId, String signer, String appoNumber, String qrurl, String post,
			String suppRange, String suppRangeDesc, Date signDate, Date startDate, Date endDate) {
		super();
		this.deliId = deliId;
		this.status = status;
		this.deliCode = deliCode;
		this.deliDate = deliDate;
		this.affirmDate = affirmDate;
		this.suppId = suppId;
		this.suppName = suppName;
		this.deliNumber = deliNumber;
		this.deliAmount = deliAmount;
		this.truckNum = truckNum;
		this.createId = createId;
		this.creator = creator;
		this.createDate = createDate;
		this.deliType = deliType;
		this.mapgCode = mapgCode;
		this.receUnit = receUnit;
		this.receAddr = receAddr;
		this.contact = contact;
		this.phone = phone;
		this.signerId = signerId;
		this.signer = signer;
		this.appoNumber = appoNumber;
		this.qrurl = qrurl;
		this.post = post;
		this.suppRange = suppRange;
		this.suppRangeDesc = suppRangeDesc;
		this.signDate = signDate;
		this.startDate = startDate;
		this.endDate = endDate;
	}



	/**
	 * @return the suppRange
	 */
	public String getSuppRange() {
		return suppRange;
	}

	/**
	 * @param suppRange the suppRange to set
	 */
	public void setSuppRange(String suppRange) {
		this.suppRange = suppRange;
	}

	/**
	 * @return the suppRangeDesc
	 */
	public String getSuppRangeDesc() {
		return suppRangeDesc;
	}

	/**
	 * @param suppRangeDesc the suppRangeDesc to set
	 */
	public void setSuppRangeDesc(String suppRangeDesc) {
		this.suppRangeDesc = suppRangeDesc;
	}

	/**
	 * @return the qrurl
	 */
	public String getQrurl() {
		return qrurl;
	}

	/**
	 * @param qrurl the qrurl to set
	 */
	public void setQrurl(String qrurl) {
		this.qrurl = qrurl;
	}

	/**
	 * @return the appoNumber
	 */
	public String getAppoNumber() {
		return appoNumber;
	}

	/**
	 * @param appoNumber the appoNumber to set
	 */
	public void setAppoNumber(String appoNumber) {
		this.appoNumber = appoNumber;
	}


	/**
	 * @return the affirmDate
	 */
	public String getAffirmDate() {
		return affirmDate;
	}

	/**
	 * @param affirmDate the affirmDate to set
	 */
	public void setAffirmDate(String affirmDate) {
		this.affirmDate = affirmDate;
	}

	/**
	 * @return the deliId
	 */
	public String getDeliId() {
		return deliId;
	}
	/**
	 * @param deliId the deliId to set
	 */
	public void setDeliId(String deliId) {
		this.deliId = deliId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the deliCode
	 */
	public String getDeliCode() {
		return deliCode;
	}
	/**
	 * @param deliCode the deliCode to set
	 */
	public void setDeliCode(String deliCode) {
		this.deliCode = deliCode;
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
	 * @return the suppId
	 */
	public String getSuppId() {
		return suppId;
	}
	/**
	 * @param suppId the suppId to set
	 */
	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}
	/**
	 * @return the suppName
	 */
	public String getSuppName() {
		return suppName;
	}
	/**
	 * @param suppName the suppName to set
	 */
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	
	/**
	 * @return the deliNumber
	 */
	public Double getDeliNumber() {
		return deliNumber;
	}

	/**
	 * @param deliNumber the deliNumber to set
	 */
	public void setDeliNumber(Double deliNumber) {
		this.deliNumber = deliNumber;
	}

	/**
	 * @return the deliAmount
	 */
	public Double getDeliAmount() {
		return deliAmount;
	}

	/**
	 * @param deliAmount the deliAmount to set
	 */
	public void setDeliAmount(Double deliAmount) {
		this.deliAmount = deliAmount;
	}

	/**
	 * @return the truckNum
	 */
	public Integer getTruckNum() {
		return truckNum;
	}
	/**
	 * @param truckNum the truckNum to set
	 */
	public void setTruckNum(Integer truckNum) {
		this.truckNum = truckNum;
	}
	/**
	 * @return the createId
	 */
	public String getCreateId() {
		return createId;
	}
	/**
	 * @param createId the createId to set
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the deliType
	 */
	public String getDeliType() {
		return deliType;
	}
	/**
	 * @param deliType the deliType to set
	 */
	public void setDeliType(String deliType) {
		this.deliType = deliType;
	}
	/**
	 * @return the mapgCode
	 */
	public String getMapgCode() {
		return mapgCode;
	}
	/**
	 * @param mapgCode the mapgCode to set
	 */
	public void setMapgCode(String mapgCode) {
		this.mapgCode = mapgCode;
	}
	/**
	 * @return the receUnit
	 */
	public String getReceUnit() {
		return receUnit;
	}
	/**
	 * @param receUnit the receUnit to set
	 */
	public void setReceUnit(String receUnit) {
		this.receUnit = receUnit;
	}
	/**
	 * @return the receAddr
	 */
	public String getReceAddr() {
		return receAddr;
	}
	/**
	 * @param receAddr the receAddr to set
	 */
	public void setReceAddr(String receAddr) {
		this.receAddr = receAddr;
	}
	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the signerId
	 */
	public String getSignerId() {
		return signerId;
	}
	/**
	 * @param signerId the signerId to set
	 */
	public void setSignerId(String signerId) {
		this.signerId = signerId;
	}
	/**
	 * @return the signer
	 */
	public String getSigner() {
		return signer;
	}
	/**
	 * @param signer the signer to set
	 */
	public void setSigner(String signer) {
		this.signer = signer;
	}
	/**
	 * @return the signDate
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}

	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Delivery [deliId=" + deliId + ", status=" + status + ", deliCode=" + deliCode + ", deliDate=" + deliDate
				+ ", affirmDate=" + affirmDate + ", suppId=" + suppId + ", suppName=" + suppName + ", deliNumber="
				+ deliNumber + ", deliAmount=" + deliAmount + ", truckNum=" + truckNum + ", createId=" + createId
				+ ", creator=" + creator + ", createDate=" + createDate + ", deliType=" + deliType + ", mapgCode="
				+ mapgCode + ", receUnit=" + receUnit + ", receAddr=" + receAddr + ", contact=" + contact + ", phone="
				+ phone + ", signerId=" + signerId + ", signer=" + signer + ", appoNumber=" + appoNumber + ", qrurl="
				+ qrurl + ", post=" + post + ", suppRange=" + suppRange + ", suppRangeDesc=" + suppRangeDesc
				+ ", signDate=" + signDate + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	
	
	
}
