package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CutPlan implements Serializable{
	private static final long serialVersionUID = 1L;
//	打切计划主键
	private String planId;
//	状态
	private String status;
//	打切月份
	private String cutMonth;
//	打切计划编号
	private String cutPlanCode;
//	计划名称
	private String planName;
//	创建人编号
	private String createId;
//	创建人名称
	private String creater;
//	创建时间
	private Date createDate;
	//引入打切进度的打切计划单号
	private String citeCode;
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCutMonth() {
		return cutMonth;
	}
	public void setCutMonth(String cutMonth) {
		this.cutMonth = cutMonth;
	}
	public String getCutPlanCode() {
		return cutPlanCode;
	}
	public void setCutPlanCode(String cutPlanCode) {
		this.cutPlanCode = cutPlanCode;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the citeCode
	 */
	public String getCiteCode() {
		return citeCode;
	}
	/**
	 * @param citeCode the citeCode to set
	 */
	public void setCiteCode(String citeCode) {
		this.citeCode = citeCode;
	}

}
