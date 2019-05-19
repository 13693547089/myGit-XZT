package com.faujor.entity.basic;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveDO implements Serializable {
	private static final long serialVersionUID = 5901474866951392534L;
	private String id;// 主键ID
	private String mainId;// 业务表主键
	private String apprId;// 审批人ID
	private String apprName;// 审批人姓名
	private Date apprTime;// 审批时间
	private String apprIdea;// 审批意见
	private String apprStatus;// 审批状态
	private String remark;// 备注

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

	public String getApprId() {
		return apprId;
	}

	public void setApprId(String apprId) {
		this.apprId = apprId;
	}

	public String getApprName() {
		return apprName;
	}

	public void setApprName(String apprName) {
		this.apprName = apprName;
	}

	public Date getApprTime() {
		return apprTime;
	}

	public void setApprTime(Date apprTime) {
		this.apprTime = apprTime;
	}

	public String getApprIdea() {
		return apprIdea;
	}

	public void setApprIdea(String apprIdea) {
		this.apprIdea = apprIdea;
	}

	public String getApprStatus() {
		return apprStatus;
	}

	public void setApprStatus(String apprStatus) {
		this.apprStatus = apprStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
