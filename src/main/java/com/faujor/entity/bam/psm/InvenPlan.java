package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 备货计划
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InvenPlan implements Serializable{

	private static final long serialVersionUID = 1L;
//	id
	private String id;
//	备货计划编号
	private String planCode;
//	备货状态
	private String status;
//	月份
	private Date planMonth;
//	PROD_SERIES_CODE
	private String prodSeriesCode;
//	产品系列标书
	private String prodSeriesDesc;
//	计划名称
	private String planDesc;
//	创建人
	private String createUser;
//	创建时间
	private Date createTime;
//	修改人
	private String modifyUser;
//	修改时间
	private Date modifyTime;
	
	private String creater;
	
	private String modifier;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanCode() {
		return planCode;
	}
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
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getPlanMonth() {
		return planMonth;
	}
	public void setPlanMonth(Date planMonth) {
		this.planMonth = planMonth;
	}
	public String getProdSeriesCode() {
		return prodSeriesCode;
	}
	public void setProdSeriesCode(String prodSeriesCode) {
		this.prodSeriesCode = prodSeriesCode;
	}
	public String getProdSeriesDesc() {
		return prodSeriesDesc;
	}
	public void setProdSeriesDesc(String prodSeriesDesc) {
		this.prodSeriesDesc = prodSeriesDesc;
	}
	public String getPlanDesc() {
		return planDesc;
	}
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
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
}
