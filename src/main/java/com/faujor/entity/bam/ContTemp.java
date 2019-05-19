package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

public class ContTemp implements Serializable {
		
	private static final long serialVersionUID = 1L;
	private String id;
//	模板状态
	private String tempStatus;
//	状态描述
	private String statusName;
//	模板编码
	private String tempNo;
//	模板名称
	private String tempName;
	private Date startDate;
	private Date endDate;
	private String createUser;
	private Date createTime;
	private String modifyUser;
	private Date modifyTime;
//	合同类型
	private String contType;
//	可同类型名称
	private String contTypeName;
//	合同版本
	private String contVersion;
//	合同说明
	private String contRemark;
//	版本依据
	private String versionBasis;
//	版本依据文件名称
	private String basisName;
	
	private String creater;
	
	private String modifier;
	
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTempStatus() {
		return tempStatus;
	}
	public void setTempStatus(String tempStatus) {
		this.tempStatus = tempStatus;
	}
	public String getTempNo() {
		return tempNo;
	}
	public void setTempNo(String tempNo) {
		this.tempNo = tempNo;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
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
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getContType() {
		return contType;
	}
	public void setContType(String contType) {
		this.contType = contType;
	}
	public String getContVersion() {
		return contVersion;
	}
	public void setContVersion(String contVersion) {
		this.contVersion = contVersion;
	}
	public String getContRemark() {
		return contRemark;
	}
	public void setContRemark(String contRemark) {
		this.contRemark = contRemark;
	}
	public String getVersionBasis() {
		return versionBasis;
	}
	public void setVersionBasis(String versionBasis) {
		this.versionBasis = versionBasis;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getBasisName() {
		return basisName;
	}
	public void setBasisName(String basisName) {
		this.basisName = basisName;
	}
	public String getContTypeName() {
		return contTypeName;
	}
	public void setContTypeName(String contTypeName) {
		this.contTypeName = contTypeName;
	}
	
}
