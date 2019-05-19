package com.faujor.entity.common;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncLog implements Serializable {
	private static final long serialVersionUID = 3762024395023276123L;
	private String id; // 主键ID
	private String asyncName; // 同步业务名称
	private Date startDate;// 开始时间
	private Date endDate;// 结束时间
	private String dateStr;// 查询时用到的时间参数
	private String asyncStatus;// 同步状态
	private Long asyncUser; // 同步者id
	private String asyncUserName; // 同步者名称
	private Integer asyncNum; // 同步条数
	private String errorMsg; // 异常信息

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAsyncName() {
		return asyncName;
	}

	public void setAsyncName(String asyncName) {
		this.asyncName = asyncName;
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

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getAsyncStatus() {
		return asyncStatus;
	}

	public void setAsyncStatus(String asyncStatus) {
		this.asyncStatus = asyncStatus;
	}

	public Long getAsyncUser() {
		return asyncUser;
	}

	public void setAsyncUser(Long asyncUser) {
		this.asyncUser = asyncUser;
	}

	public String getAsyncUserName() {
		return asyncUserName;
	}

	public void setAsyncUserName(String asyncUserName) {
		this.asyncUserName = asyncUserName;
	}

	public Integer getAsyncNum() {
		return asyncNum;
	}

	public void setAsyncNum(Integer asyncNum) {
		this.asyncNum = asyncNum;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
