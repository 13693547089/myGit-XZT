package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StraMessDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messId;// VARCHAR2(50) NOT NULL,
	private String messCode;// VARCHAR2(30),
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date arriDate;// DATE,
	private String messStatus;// VARCHAR2(20),
	private Double mateNumber;// NUMBER(10),
	private Double mateAmount;// NUMBER(10),
	private String createId;// VARCHAR2(50),
	private String creator;// VARCHAR2(30),
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;// DATE,
	private String zzoem;// varchar2(50)
	private String oemSupp;
	private String post;

	public StraMessDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StraMessDO(String messId, String messCode, Date arriDate, String messStatus, Double mateNumber,
			Double mateAmount, String createId, String creator, Date createDate, String zzoem, String oemSupp,
			String post) {
		super();
		this.messId = messId;
		this.messCode = messCode;
		this.arriDate = arriDate;
		this.messStatus = messStatus;
		this.mateNumber = mateNumber;
		this.mateAmount = mateAmount;
		this.createId = createId;
		this.creator = creator;
		this.createDate = createDate;
		this.zzoem = zzoem;
		this.oemSupp = oemSupp;
		this.post = post;
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
	 * @return the arriDate
	 */
	public Date getArriDate() {
		return arriDate;
	}

	/**
	 * @param arriDate
	 *            the arriDate to set
	 */
	public void setArriDate(Date arriDate) {
		this.arriDate = arriDate;
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
	 * @return the oemSupp
	 */
	public String getOemSupp() {
		return oemSupp;
	}

	/**
	 * @param oemSupp
	 *            the oemSupp to set
	 */
	public void setOemSupp(String oemSupp) {
		this.oemSupp = oemSupp;
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
		return "StraMessDO [messId=" + messId + ", messCode=" + messCode + ", arriDate=" + arriDate + ", messStatus="
				+ messStatus + ", mateNumber=" + mateNumber + ", mateAmount=" + mateAmount + ", createId=" + createId
				+ ", creator=" + creator + ", createDate=" + createDate + ", zzoem=" + zzoem + ", oemSupp=" + oemSupp
				+ ", post=" + post + "]";
	}

	
}
