package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Receive implements Serializable {
	private static final long serialVersionUID = 1L;
	private String receId;// VARCHAR2(50) NOT NULL,
	private String status;// VARCHAR2(20),
	private String receCode;// VARCHAR2(50),
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date receDate;// DATE,
	private String suppId;// VARCHAR2(50),
	private String suppName;// VARCHAR2(30),
	private Double receNumber;// NUMBER(10),
	private Double receAmount;// NUMBER(10),
	private Integer truckNum;// NUMBER(10),
	private String createId;// VARCHAR2(50),
	private String creator;// VARCHAR2(20),
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;// DATE,
	private String receUnit;// VARCHAR2(50),
	private String deliCode;// VARCHAR2(50)
	private String qrurl;// VARCHAR2(50)
	private String receType;// VARCHAR2(50) --（1:预约收货）（0:直发收货）（2:特殊收货）
	private String post;// VARCHAR2(50) 收货单位的岗位人员（userId）
	private String suppRange;// 供应商子范围编码
	private String suppRangeDesc;// 供应商子范围描述
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	private String asyncStatus; // 同步状态
	private String errorMsg; // 错误信息

	public String getReceId() {
		return receId;
	}

	public void setReceId(String receId) {
		this.receId = receId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceCode() {
		return receCode;
	}

	public void setReceCode(String receCode) {
		this.receCode = receCode;
	}

	public Date getReceDate() {
		return receDate;
	}

	public void setReceDate(Date receDate) {
		this.receDate = receDate;
	}

	public String getSuppId() {
		return suppId;
	}

	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public Double getReceNumber() {
		return receNumber;
	}

	public void setReceNumber(Double receNumber) {
		this.receNumber = receNumber;
	}

	public Double getReceAmount() {
		return receAmount;
	}

	public void setReceAmount(Double receAmount) {
		this.receAmount = receAmount;
	}

	public Integer getTruckNum() {
		return truckNum;
	}

	public void setTruckNum(Integer truckNum) {
		this.truckNum = truckNum;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getReceUnit() {
		return receUnit;
	}

	public void setReceUnit(String receUnit) {
		this.receUnit = receUnit;
	}

	public String getDeliCode() {
		return deliCode;
	}

	public void setDeliCode(String deliCode) {
		this.deliCode = deliCode;
	}

	public String getQrurl() {
		return qrurl;
	}

	public void setQrurl(String qrurl) {
		this.qrurl = qrurl;
	}

	public String getReceType() {
		return receType;
	}

	public void setReceType(String receType) {
		this.receType = receType;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAsyncStatus() {
		return asyncStatus;
	}

	public void setAsyncStatus(String asyncStatus) {
		this.asyncStatus = asyncStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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
	
}
