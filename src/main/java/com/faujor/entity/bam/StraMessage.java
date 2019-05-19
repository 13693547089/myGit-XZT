package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StraMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private String
	private String messId;// VARCHAR2(50) NOT NULL,
	private String messCode;// VARCHAR2(30),
	private String suppId;// VARCHAR2(50),
	private String suppName;// VARCHAR2(30),
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date deliveryDate;// DATE,
	private String messStatus;// VARCHAR2(20),
	private Double mateNumber;// NUMBER(10),
	private Double mateAmount;// NUMBER(10),
	private String receUnit;// VARCHAR2(30),
	private String receAddr;// VARCHAR2(100),
	private String createId;// VARCHAR2(50),
	private String creator;// VARCHAR2(30),
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;// DATE,
	private String modifieId;// VARCHAR2(50),
	private String modifier;// VARCHAR2(30),
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date udpateDate;// DATE
	private String contact;// varchar2(20)
	private String phone;// varchar2(50)
	private String zzoem;// varchar2(50)
	private String qrurl;
	private String alloNo;// 调拨单号
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date arriDate;// 到货时间
	private String  post;//收货单位的岗位人员
	private String  suppRange;//OEM供应商子范围编码
	private String  suppRangeDesc;//OEM供应商子范围描述
	// 直发通知物资信息
	private List<MessMate> messMates;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	public StraMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StraMessage(String messId, String messCode, String suppId, String suppName, Date deliveryDate,
			String messStatus, Double mateNumber, Double mateAmount, String receUnit, String receAddr, String createId,
			String creator, Date createDate, String modifieId, String modifier, Date udpateDate, String contact,
			String phone, String zzoem, String qrurl, String alloNo, Date arriDate, String post, String suppRange,
			String suppRangeDesc, List<MessMate> messMates, Date startDate, Date endDate) {
		super();
		this.messId = messId;
		this.messCode = messCode;
		this.suppId = suppId;
		this.suppName = suppName;
		this.deliveryDate = deliveryDate;
		this.messStatus = messStatus;
		this.mateNumber = mateNumber;
		this.mateAmount = mateAmount;
		this.receUnit = receUnit;
		this.receAddr = receAddr;
		this.createId = createId;
		this.creator = creator;
		this.createDate = createDate;
		this.modifieId = modifieId;
		this.modifier = modifier;
		this.udpateDate = udpateDate;
		this.contact = contact;
		this.phone = phone;
		this.zzoem = zzoem;
		this.qrurl = qrurl;
		this.alloNo = alloNo;
		this.arriDate = arriDate;
		this.post = post;
		this.suppRange = suppRange;
		this.suppRangeDesc = suppRangeDesc;
		this.messMates = messMates;
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
	 * @param qrurl
	 *            the qrurl to set
	 */
	public void setQrurl(String qrurl) {
		this.qrurl = qrurl;
	}

	/**
	 * @return the zzoem
	 */
	public String getZzoem() {
		return zzoem;
	}

	/**
	 * @param zzoem
	 *            the zzoem to set
	 */
	public void setZzoem(String zzoem) {
		this.zzoem = zzoem;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
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
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the messId
	 */
	public String getMessId() {
		return messId;
	}

	/**
	 * @param messId
	 *            the messId to set
	 */
	public void setMessId(String messId) {
		this.messId = messId;
	}

	/**
	 * @return the messCode
	 */
	public String getMessCode() {
		return messCode;
	}

	/**
	 * @param messCode
	 *            the messCode to set
	 */
	public void setMessCode(String messCode) {
		this.messCode = messCode;
	}

	/**
	 * @return the suppId
	 */
	public String getSuppId() {
		return suppId;
	}

	/**
	 * @param suppId
	 *            the suppId to set
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
	 * @param suppName
	 *            the suppName to set
	 */
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	/**
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the messStatus
	 */
	public String getMessStatus() {
		return messStatus;
	}

	/**
	 * @param messStatus
	 *            the messStatus to set
	 */
	public void setMessStatus(String messStatus) {
		this.messStatus = messStatus;
	}

	/**
	 * @return the mateNumber
	 */
	public Double getMateNumber() {
		return mateNumber;
	}

	/**
	 * @param mateNumber
	 *            the mateNumber to set
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
	 * @param mateAmount
	 *            the mateAmount to set
	 */
	public void setMateAmount(Double mateAmount) {
		this.mateAmount = mateAmount;
	}

	/**
	 * @return the receUnit
	 */
	public String getReceUnit() {
		return receUnit;
	}

	/**
	 * @param receUnit
	 *            the receUnit to set
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
	 * @param receAddr
	 *            the receAddr to set
	 */
	public void setReceAddr(String receAddr) {
		this.receAddr = receAddr;
	}

	/**
	 * @return the createId
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * @param createId
	 *            the createId to set
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
	 * @param creator
	 *            the creator to set
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
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the modifieId
	 */
	public String getModifieId() {
		return modifieId;
	}

	/**
	 * @param modifieId
	 *            the modifieId to set
	 */
	public void setModifieId(String modifieId) {
		this.modifieId = modifieId;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier
	 *            the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the udpateDate
	 */
	public Date getUdpateDate() {
		return udpateDate;
	}

	/**
	 * @param udpateDate
	 *            the udpateDate to set
	 */
	public void setUdpateDate(Date udpateDate) {
		this.udpateDate = udpateDate;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the messMates
	 */
	public List<MessMate> getMessMates() {
		return messMates;
	}

	/**
	 * @param messMates
	 *            the messMates to set
	 */
	public void setMessMates(List<MessMate> messMates) {
		this.messMates = messMates;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
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
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAlloNo() {
		return alloNo;
	}

	public void setAlloNo(String alloNo) {
		this.alloNo = alloNo;
	}

	/**
	 * @return the arriDate
	 */
	public Date getArriDate() {
		return arriDate;
	}

	/**
	 * @param arriDate the arriDate to set
	 */
	public void setArriDate(Date arriDate) {
		this.arriDate = arriDate;
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
		return "StraMessage [messId=" + messId + ", messCode=" + messCode + ", suppId=" + suppId + ", suppName="
				+ suppName + ", deliveryDate=" + deliveryDate + ", messStatus=" + messStatus + ", mateNumber="
				+ mateNumber + ", mateAmount=" + mateAmount + ", receUnit=" + receUnit + ", receAddr=" + receAddr
				+ ", createId=" + createId + ", creator=" + creator + ", createDate=" + createDate + ", modifieId="
				+ modifieId + ", modifier=" + modifier + ", udpateDate=" + udpateDate + ", contact=" + contact
				+ ", phone=" + phone + ", zzoem=" + zzoem + ", qrurl=" + qrurl + ", alloNo=" + alloNo + ", arriDate="
				+ arriDate + ", post=" + post + ", suppRange=" + suppRange + ", suppRangeDesc=" + suppRangeDesc
				+ ", messMates=" + messMates + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	
	

}
