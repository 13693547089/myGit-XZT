package com.faujor.entity.document;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板信息
 * @author hql
 *
 */
public class Template implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
//	模板编号
	private String tempNo;
//	模板名称
	private String tempName;
//	附件数量
	private Integer attachNum;
//	有效开始日期
	private Date startDate;
//	有效结束日期
	private Date endDate;
//	创建人
	private String createUser;
//	创建时间
	private Date createTime;
//	修改人
	private String modifyUser;
//	修改时间
	private Date modifyTime;
//	创建人名称
	private String creater;
//	修改人名称
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Integer getAttachNum() {
		return attachNum;
	}
	public void setAttachNum(Integer attachNum) {
		this.attachNum = attachNum;
	}
	
}
