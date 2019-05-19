package com.faujor.entity.bam;

import java.math.BigDecimal;
import java.sql.Date;

public class ProjectInfo {
//	编号
	private String projCode;
//	名称
	private String projName;
//	负责人Id
	private Integer leaderId;
//	负责人
	private String leader;
//	人工预算成本
	private BigDecimal laborCostBudget;
//	开始日期
	private Date startDate;
//	结束日期
	private Date endDate;
//	类型
	private String projTypeGrade;
//	父编号
	private String parentCode;
//	项目名称
	private String theProjName;
//	阶段一名称
	private String theStageName1;
//	阶段2名称
	private String theStageName2;
//	任务名称
	private String theTaskName;
	public String getProjCode() {
		return projCode;
	}
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public Integer getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public BigDecimal getLaborCostBudget() {
		return laborCostBudget;
	}
	public void setLaborCostBudget(BigDecimal laborCostBudget) {
		this.laborCostBudget = laborCostBudget;
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
	public String getProjTypeGrade() {
		return projTypeGrade;
	}
	public void setProjTypeGrade(String projTypeGrade) {
		this.projTypeGrade = projTypeGrade;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getTheProjName() {
		return theProjName;
	}
	public void setTheProjName(String theProjName) {
		this.theProjName = theProjName;
	}
	public String getTheStageName1() {
		return theStageName1;
	}
	public void setTheStageName1(String theStageName1) {
		this.theStageName1 = theStageName1;
	}
	public String getTheStageName2() {
		return theStageName2;
	}
	public void setTheStageName2(String theStageName2) {
		this.theStageName2 = theStageName2;
	}
	public String getTheTaskName() {
		return theTaskName;
	}
	public void setTheTaskName(String theTaskName) {
		this.theTaskName = theTaskName;
	}
}

