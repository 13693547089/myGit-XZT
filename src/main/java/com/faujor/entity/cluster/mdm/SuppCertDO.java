package com.faujor.entity.cluster.mdm;

import java.io.Serializable;

public class SuppCertDO implements Serializable {
	private static final long serialVersionUID = -5993131729179267738L;
	private String id;
	private String suppId;
	private String papersId;
	private String papersName;
	private String papersType;
	private String startDate;
	private String endDate;
	private String acceUrl;
	private String acceOldName;
	private String acceNewName;
	private String fileId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSuppId() {
		return suppId;
	}
	public void setSuppId(String suppId) {
		this.suppId = suppId;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
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
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
