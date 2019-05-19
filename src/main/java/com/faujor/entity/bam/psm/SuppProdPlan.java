package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 供应商生产计划
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SuppProdPlan implements Serializable{
	private static final long serialVersionUID = 1L;
	//	id
	private String id;
//	供应商排产id
	private String mainId;
//	日期
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date planDate;
//	计划数量
	private BigDecimal planNum;
//	状态
	private String status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public BigDecimal getPlanNum() {
		return planNum;
	}
	public void setPlanNum(BigDecimal planNum) {
		this.planNum = planNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
