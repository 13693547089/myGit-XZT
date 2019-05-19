package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

public class Contract implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
//	模板状态
	private String contNo;
//	状态描述
	private String contName;
//	合同类型
	private String contType;
//	合同类型名称
	private String contTypeName;
//	甲方
	private String firstPart;
//	甲方名称
	private String firstPartName;
//	乙方
	private String secondPart;
//	乙方名称
	private String secondPartName;
//	合同版本
	private String contVersion;
//	合同说明
	private String contRemark;
	private String createUser;
	private Date createTime;
	private String modifyUser;
	private Date modifyTime;
//	合同状态
	private String contStatus;
//	合同状态描述
	private String contStatusName;
//	创建人名称
	private String creater;
	
	private String modifier;
	
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	private Date startDate;
	
	private Date endDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContNo() {
		return contNo;
	}
	public void setContNo(String contNo) {
		this.contNo = contNo;
	}
	public String getContName() {
		return contName;
	}
	public void setContName(String contName) {
		this.contName = contName;
	}
	public String getContType() {
		return contType;
	}
	public void setContType(String contType) {
		this.contType = contType;
	}
	public String getContTypeName() {
		return contTypeName;
	}
	public void setContTypeName(String contTypeName) {
		this.contTypeName = contTypeName;
	}
	public String getFirstPart() {
		return firstPart;
	}
	public void setFirstPart(String firstPart) {
		this.firstPart = firstPart;
	}
	public String getFirstPartName() {
		return firstPartName;
	}
	public void setFirstPartName(String firstPartName) {
		this.firstPartName = firstPartName;
	}
	public String getSecondPart() {
		return secondPart;
	}
	public void setSecondPart(String secondPart) {
		this.secondPart = secondPart;
	}
	public String getSecondPartName() {
		return secondPartName;
	}
	public void setSecondPartName(String secondPartName) {
		this.secondPartName = secondPartName;
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
	public String getContStatus() {
		return contStatus;
	}
	public void setContStatus(String contStatus) {
		this.contStatus = contStatus;
	}
	public String getContStatusName() {
		return contStatusName;
	}
	public void setContStatusName(String contStatusName) {
		this.contStatusName = contStatusName;
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
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
}
