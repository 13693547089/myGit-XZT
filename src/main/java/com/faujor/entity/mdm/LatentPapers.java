package com.faujor.entity.mdm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LatentPapers implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 2847144227013404613L;
	/**
	 * 潜在供应商证件
	 */
	private String id; //VARCHAR2(50) NOT NULL,   --编号
	private String papersId; // VARCHAR2(50),     --证件编号
	private String papersName; // VARCHAR2(20),   --证件名称
	private String papersType; // VARCHAR2(10),   --附件类型
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date startDate; // DATE,            --起始日期
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date endDate; // DATE,              --截止日期
	private String acceUrl; // VARCHAR2(50),      --附件地址
	private String acceOldName; // VARCHAR2(50), --附件原名称
	private String acceNewName; // VARCHAR2(50), --附件新名称
	private String fileId;                       //--附件编号
	private String suppId; // VARCHAR2(50),       --供应商编码
	public LatentPapers() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LatentPapers(String id, String papersId, String papersName, String papersType, Date startDate, Date endDate,
			String acceUrl, String acceOldName, String acceNewName, String fileId, String suppId) {
		super();
		this.id = id;
		this.papersId = papersId;
		this.papersName = papersName;
		this.papersType = papersType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.acceUrl = acceUrl;
		this.acceOldName = acceOldName;
		this.acceNewName = acceNewName;
		this.fileId = fileId;
		this.suppId = suppId;
	}

	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPapersId() {
		return papersId;
	}
	public void setPapersId(String papersId) {
		this.papersId = papersId;
	}
	public String getPapersName() {
		return papersName;
	}
	public void setPapersName(String papersName) {
		this.papersName = papersName;
	}
	public String getPapersType() {
		return papersType;
	}
	public void setPapersType(String papersType) {
		this.papersType = papersType;
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
	public String getAcceUrl() {
		return acceUrl;
	}
	public void setAcceUrl(String acceUrl) {
		this.acceUrl = acceUrl;
	}
	public String getAcceOldName() {
		return acceOldName;
	}
	public void setAcceOldName(String acceOldName) {
		this.acceOldName = acceOldName;
	}
	public String getAcceNewName() {
		return acceNewName;
	}
	public void setAcceNewName(String acceNewName) {
		this.acceNewName = acceNewName;
	}
	public String getSuppId() {
		return suppId;
	}
	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LatentPapers [id=" + id + ", papersId=" + papersId + ", papersName=" + papersName + ", papersType="
				+ papersType + ", startDate=" + startDate + ", endDate=" + endDate + ", acceUrl=" + acceUrl
				+ ", acceOldName=" + acceOldName + ", acceNewName=" + acceNewName + ", fileId=" + fileId + ", suppId="
				+ suppId + "]";
	}
	
	
	
	
}
