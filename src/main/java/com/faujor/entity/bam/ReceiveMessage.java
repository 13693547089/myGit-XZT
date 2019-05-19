package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class ReceiveMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;// VARCHAR2(50) NOT NULL,
	private String receUnit;//	varchar2(100),
	private String receAddr;//	varchar2(200),
	private String contact;//	varchar2(20),
	private String phone;//	varchar2(50),
	private String createId ;//VARCHAR2(50),
	private	String creator;// VARCHAR2(20),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private	Date createDate;// DATE,
	private String 	modifieId;// VARCHAR2(50),
	private	String modifier;// VARCHAR2(20),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private	Date udpateDate;// DATE
	private String post;  //岗位  --预约发布根据岗位来过滤
	private String freightRange;  //运费子范围
	public ReceiveMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ReceiveMessage(String id, String receUnit, String receAddr, String contact, String phone, String createId,
			String creator, Date createDate, String modifieId, String modifier, Date udpateDate, String post,
			String freightRange) {
		super();
		this.id = id;
		this.receUnit = receUnit;
		this.receAddr = receAddr;
		this.contact = contact;
		this.phone = phone;
		this.createId = createId;
		this.creator = creator;
		this.createDate = createDate;
		this.modifieId = modifieId;
		this.modifier = modifier;
		this.udpateDate = udpateDate;
		this.post = post;
		this.freightRange = freightRange;
	}


	/**
	 * @return the freightRange
	 */
	public String getFreightRange() {
		return freightRange;
	}


	/**
	 * @param freightRange the freightRange to set
	 */
	public void setFreightRange(String freightRange) {
		this.freightRange = freightRange;
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
		return "ReceiveMessage [id=" + id + ", receUnit=" + receUnit + ", receAddr=" + receAddr + ", contact=" + contact
				+ ", phone=" + phone + ", createId=" + createId + ", creator=" + creator + ", createDate=" + createDate
				+ ", modifieId=" + modifieId + ", modifier=" + modifier + ", udpateDate=" + udpateDate + ", post="
				+ post + ", freightRange=" + freightRange + "]";
	}

	
	
	
}
