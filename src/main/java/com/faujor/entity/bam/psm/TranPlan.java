package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：TranPlan
 * @tableName PS_TRAN_PLAN
 * @tableDesc 调拨计划主表
 * @author Vincent
 * @date 2018-03-01
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranPlan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer sn;
	private String tranCode;	// 计划编码
	private String tranName;	// 计划名称
	private String tranDate;		// 调拨日期
	private String status;		// 状态
	private String crtDate;
	private String crtUser;
	private String uptDate;
	private String uptUser;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getTranCode() {
		return tranCode;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public String getTranName() {
		return tranName;
	}
	public void setTranName(String tranName) {
		this.tranName = tranName;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public String getCrtUser() {
		return crtUser;
	}
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	public String getUptDate() {
		return uptDate;
	}
	public void setUptDate(String uptDate) {
		this.uptDate = uptDate;
	}
	public String getUptUser() {
		return uptUser;
	}
	public void setUptUser(String uptUser) {
		this.uptUser = uptUser;
	}
}
