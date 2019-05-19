package com.faujor.entity.task;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String taskName;
	private String sdata1;
	private String processCode;
	private int node;
	private String executor;
	private String executorName;
	private String actionUrl;
	private Date createTime;
	private long creator;
	private String creatorName;
	private Date completeTime;
	private long completor;
	private String completeName;
	// 任务类型(正向流转processing，退回back，结束end)
	private String taskType;
	private String taskTypeDesc;
	private String status;// 任务状态（tm_task表中已保存，已回退，审批中/tm_task_his表中已完成，已回退）
	private Boolean isOwn = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getSdata1() {
		return sdata1;
	}

	public void setSdata1(String sdata1) {
		this.sdata1 = sdata1;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public int getNode() {
		return node;
	}

	public void setNode(int node) {
		this.node = node;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreator() {
		return creator;
	}

	public void setCreator(long creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public long getCompletor() {
		return completor;
	}

	public void setCompletor(long completor) {
		this.completor = completor;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskTypeDesc() {
		return taskTypeDesc;
	}

	public void setTaskTypeDesc(String taskTypeDesc) {
		this.taskTypeDesc = taskTypeDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsOwn() {
		return isOwn;
	}

	public void setIsOwn(Boolean isOwn) {
		this.isOwn = isOwn;
	}

}
