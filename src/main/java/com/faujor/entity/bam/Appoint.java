package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Appoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String 
	private String appoId;// VARCHAR2(50) NOT NULL,
	private String suppId;// VARCHAR2(50),
	private String suppName;// VARCHAR2(20),
	private String appoStatus;// VARCHAR2(20),
	private String appoCode;// VARCHAR2(30),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date appoDate;// DATE,
	private String expectDate;// String,
	private String affirmDate;// String,
	private String truckType;// VARCHAR2(30),
	private Integer truckNum;// NUMBER(10),
	private Double mateNumber;// NUMBER(10),
	private Double mateAmount;// NUMBER(10),
	private String createId;// VARCHAR2(50),
	private String creator;// VARCHAR2(20),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;// DATE,
	private String modifieId;// VARCHAR2(50),
	private String modifier;// VARCHAR2(20),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date udpateDate;// DATE,
	private String priority;// VARCHAR2(20),
	private String prodVeriId;// VARCHAR2(50),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date prodVeriDate;// DATE,
	private String prodVeriStatus;// VARCHAR2(20),
	private String cdcPublId;// VARCHAR2(50),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date cdcPublDate;// DATE,
	private String cdcPublStatus ;//VARCHAR2(20)
	private String receUnit;//	varchar2(100)
	private String receAddr;//	varchar2(200)
	private String contact;//	varchar2(20)
	private String phone;//	varchar2(50)
	private String post; //岗位 --预约发布需要根据岗位过滤
	private String refuseReason;  //拒绝原因
	private String citeAppo;//引用预约单
	private String suppRange;//供应商子范围编码
	private String suppRangeDesc;//供应商子范围描述
	private String emailStatus;//邮箱发送状态
	private Date emailDate;//邮箱发送时间
	//预约申请物资信息
	private List<AppoMate> appoMates;
	
	private String createDateStart;
	private String createDateEnd;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	
	
	public Appoint() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Appoint(String appoId, String suppId, String suppName, String appoStatus, String appoCode, Date appoDate,
			String expectDate, String affirmDate, String truckType, Integer truckNum, Double mateNumber,
			Double mateAmount, String createId, String creator, Date createDate, String modifieId, String modifier,
			Date udpateDate, String priority, String prodVeriId, Date prodVeriDate, String prodVeriStatus,
			String cdcPublId, Date cdcPublDate, String cdcPublStatus, String receUnit, String receAddr, String contact,
			String phone, String post, String refuseReason, String citeAppo, String suppRange, String suppRangeDesc,
			String emailStatus, Date emailDate, List<AppoMate> appoMates, String createDateStart, String createDateEnd,
			Date startDate, Date endDate) {
		super();
		this.appoId = appoId;
		this.suppId = suppId;
		this.suppName = suppName;
		this.appoStatus = appoStatus;
		this.appoCode = appoCode;
		this.appoDate = appoDate;
		this.expectDate = expectDate;
		this.affirmDate = affirmDate;
		this.truckType = truckType;
		this.truckNum = truckNum;
		this.mateNumber = mateNumber;
		this.mateAmount = mateAmount;
		this.createId = createId;
		this.creator = creator;
		this.createDate = createDate;
		this.modifieId = modifieId;
		this.modifier = modifier;
		this.udpateDate = udpateDate;
		this.priority = priority;
		this.prodVeriId = prodVeriId;
		this.prodVeriDate = prodVeriDate;
		this.prodVeriStatus = prodVeriStatus;
		this.cdcPublId = cdcPublId;
		this.cdcPublDate = cdcPublDate;
		this.cdcPublStatus = cdcPublStatus;
		this.receUnit = receUnit;
		this.receAddr = receAddr;
		this.contact = contact;
		this.phone = phone;
		this.post = post;
		this.refuseReason = refuseReason;
		this.citeAppo = citeAppo;
		this.suppRange = suppRange;
		this.suppRangeDesc = suppRangeDesc;
		this.emailStatus = emailStatus;
		this.emailDate = emailDate;
		this.appoMates = appoMates;
		this.createDateStart = createDateStart;
		this.createDateEnd = createDateEnd;
		this.startDate = startDate;
		this.endDate = endDate;
	}





	/**
	 * @return the emailDate
	 */
	public Date getEmailDate() {
		return emailDate;
	}


	/**
	 * @param emailDate the emailDate to set
	 */
	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}


	/**
	 * @return the emailStatus
	 */
	public String getEmailStatus() {
		return emailStatus;
	}

	/**
	 * @param emailStatus the emailStatus to set
	 */
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
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
	 * @return the appoStatus
	 */
	public String getAppoStatus() {
		return appoStatus;
	}
	/**
	 * @param appoStatus the appoStatus to set
	 */
	public void setAppoStatus(String appoStatus) {
		this.appoStatus = appoStatus;
	}
	/**
	 * @return the appoCode
	 */
	public String getAppoCode() {
		return appoCode;
	}
	/**
	 * @param appoCode the appoCode to set
	 */
	public void setAppoCode(String appoCode) {
		this.appoCode = appoCode;
	}
	/**
	 * @return the appoDate
	 */
	public Date getAppoDate() {
		return appoDate;
	}
	/**
	 * @param appoDate the appoDate to set
	 */
	public void setAppoDate(Date appoDate) {
		this.appoDate = appoDate;
	}
	
	/**
	 * @return the expectDate
	 */
	public String getExpectDate() {
		return expectDate;
	}

	/**
	 * @param expectDate the expectDate to set
	 */
	public void setExpectDate(String expectDate) {
		this.expectDate = expectDate;
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
	 * @return the truckType
	 */
	public String getTruckType() {
		return truckType;
	}
	/**
	 * @param truckType the truckType to set
	 */
	public void setTruckType(String truckType) {
		this.truckType = truckType;
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
	 * @return the modifieId
	 */
	public String getModifieId() {
		return modifieId;
	}
	/**
	 * @param modifieId the modifieId to set
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
	 * @param modifier the modifier to set
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
	 * @param udpateDate the udpateDate to set
	 */
	public void setUdpateDate(Date udpateDate) {
		this.udpateDate = udpateDate;
	}
	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return the prodVeriId
	 */
	public String getProdVeriId() {
		return prodVeriId;
	}
	/**
	 * @param prodVeriId the prodVeriId to set
	 */
	public void setProdVeriId(String prodVeriId) {
		this.prodVeriId = prodVeriId;
	}
	/**
	 * @return the prodVeriDate
	 */
	public Date getProdVeriDate() {
		return prodVeriDate;
	}
	/**
	 * @param prodVeriDate the prodVeriDate to set
	 */
	public void setProdVeriDate(Date prodVeriDate) {
		this.prodVeriDate = prodVeriDate;
	}
	/**
	 * @return the prodVeriStatus
	 */
	public String getProdVeriStatus() {
		return prodVeriStatus;
	}
	/**
	 * @param prodVeriStatus the prodVeriStatus to set
	 */
	public void setProdVeriStatus(String prodVeriStatus) {
		this.prodVeriStatus = prodVeriStatus;
	}
	/**
	 * @return the cdcPublId
	 */
	public String getCdcPublId() {
		return cdcPublId;
	}
	/**
	 * @param cdcPublId the cdcPublId to set
	 */
	public void setCdcPublId(String cdcPublId) {
		this.cdcPublId = cdcPublId;
	}
	/**
	 * @return the cdcPublDate
	 */
	public Date getCdcPublDate() {
		return cdcPublDate;
	}
	/**
	 * @param cdcPublDate the cdcPublDate to set
	 */
	public void setCdcPublDate(Date cdcPublDate) {
		this.cdcPublDate = cdcPublDate;
	}
	/**
	 * @return the cdcPublStatus
	 */
	public String getCdcPublStatus() {
		return cdcPublStatus;
	}
	/**
	 * @param cdcPublStatus the cdcPublStatus to set
	 */
	public void setCdcPublStatus(String cdcPublStatus) {
		this.cdcPublStatus = cdcPublStatus;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the appoMates
	 */
	public List<AppoMate> getAppoMates() {
		return appoMates;
	}
	/**
	 * @param appoMates the appoMates to set
	 */
	public void setAppoMates(List<AppoMate> appoMates) {
		this.appoMates = appoMates;
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
	 * @return the createDateStart
	 */
	public String getCreateDateStart() {
		return createDateStart;
	}

	/**
	 * @param createDateStart the createDateStart to set
	 */
	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	/**
	 * @return the createDateEnd
	 */
	public String getCreateDateEnd() {
		return createDateEnd;
	}

	/**
	 * @param createDateEnd the createDateEnd to set
	 */
	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	/**
	 * @return the refuseReason
	 */
	public String getRefuseReason() {
		return refuseReason;
	}

	/**
	 * @param refuseReason the refuseReason to set
	 */
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	/**
	 * @return the citeAppo
	 */
	public String getCiteAppo() {
		return citeAppo;
	}

	/**
	 * @param citeAppo the citeAppo to set
	 */
	public void setCiteAppo(String citeAppo) {
		this.citeAppo = citeAppo;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Appoint [appoId=" + appoId + ", suppId=" + suppId + ", suppName=" + suppName + ", appoStatus="
				+ appoStatus + ", appoCode=" + appoCode + ", appoDate=" + appoDate + ", expectDate=" + expectDate
				+ ", affirmDate=" + affirmDate + ", truckType=" + truckType + ", truckNum=" + truckNum + ", mateNumber="
				+ mateNumber + ", mateAmount=" + mateAmount + ", createId=" + createId + ", creator=" + creator
				+ ", createDate=" + createDate + ", modifieId=" + modifieId + ", modifier=" + modifier + ", udpateDate="
				+ udpateDate + ", priority=" + priority + ", prodVeriId=" + prodVeriId + ", prodVeriDate="
				+ prodVeriDate + ", prodVeriStatus=" + prodVeriStatus + ", cdcPublId=" + cdcPublId + ", cdcPublDate="
				+ cdcPublDate + ", cdcPublStatus=" + cdcPublStatus + ", receUnit=" + receUnit + ", receAddr=" + receAddr
				+ ", contact=" + contact + ", phone=" + phone + ", post=" + post + ", refuseReason=" + refuseReason
				+ ", citeAppo=" + citeAppo + ", suppRange=" + suppRange + ", suppRangeDesc=" + suppRangeDesc
				+ ", emailStatus=" + emailStatus + ", emailDate=" + emailDate + ", appoMates=" + appoMates
				+ ", createDateStart=" + createDateStart + ", createDateEnd=" + createDateEnd + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}


	

	
	
}
