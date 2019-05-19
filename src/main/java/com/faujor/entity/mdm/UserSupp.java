package com.faujor.entity.mdm;

import java.io.Serializable;
import java.util.Date;

public class UserSupp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String
	
	private String id;// VARCHAR2(50) NOT NULL,
	private Integer buyerId;// number(20),
	private String suppId;// VARCHAR2(50),
	private String buyerRole;// VARCHAR2(20),
	private String creator;// VARCHAR2(20),
	private Date createTime;// DATE,
	private String modifier;// VARCHAR2(20),
	private Date updateTime ;//DATE,
	private String version;// VARCHAR2(10),
	private Date startTime;// DATE,
	private Date endTime;// DATE,
	
	public UserSupp() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserSupp(String id, Integer buyerId, String suppId, String buyerRole, String creator, Date createTime,
			String modifier, Date updateTime, String version, Date startTime, Date endTime) {
		super();
		this.id = id;
		this.buyerId = buyerId;
		this.suppId = suppId;
		this.buyerRole = buyerRole;
		this.creator = creator;
		this.createTime = createTime;
		this.modifier = modifier;
		this.updateTime = updateTime;
		this.version = version;
		this.startTime = startTime;
		this.endTime = endTime;
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
	 * @return the buyerId
	 */
	public Integer getBuyerId() {
		return buyerId;
	}
	/**
	 * @param buyerId the buyerId to set
	 */
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
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
	 * @return the buyerRole
	 */
	public String getBuyerRole() {
		return buyerRole;
	}
	/**
	 * @param buyerRole the buyerRole to set
	 */
	public void setBuyerRole(String buyerRole) {
		this.buyerRole = buyerRole;
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
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
		return "UserSupp [id=" + id + ", buyerId=" + buyerId + ", suppId=" + suppId + ", buyerRole=" + buyerRole
				+ ", creator=" + creator + ", createTime=" + createTime + ", modifier=" + modifier + ", updateTime="
				+ updateTime + ", version=" + version + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
	
	
}
