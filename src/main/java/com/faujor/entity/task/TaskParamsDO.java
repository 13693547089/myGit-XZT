package com.faujor.entity.task;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.utils.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskParamsDO implements Serializable {
	private static final long serialVersionUID = -1722574756873278785L;
	private String sdata1;// 业务数据主键
	private String processCode;// 流程编码，唯一的。
	private String actionUrl;// 审批页面路径
	private int node; // 节点序号从1开始
	private String status; // 如果是第一个节点，则表明是提交(submit)还是保存(save)
	private String executorStr;
	private List<UserDO> executorList; // 以json的格式存储，K=userId,V=name;存储下一节点的审批人信息
	private Long executor;
	private String strExec;
	private String executorName;
	private String taskName;// 任务名称
	private String submit;// 任务提交者ID
	private String submitName;// 任务提交者名称
	private String taskStatus;// 任务状态
	private String pagePattern;// 页面类型（处理(read)，还是查看(view))

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

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public int getNode() {
		return node;
	}

	public void setNode(int node) {
		this.node = node;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExecutorStr() {
		return executorStr;
	}

	public void setExecutorStr(String executorStr) {
		this.executorList = JsonUtils.jsonToList(executorStr, UserDO.class);
		this.executorStr = executorStr;
	}

	public List<UserDO> getExecutorList() {
		return executorList;
	}

	public Long getExecutor() {
		return executor;
	}

	public void setExecutor(Long executor) {
		this.executor = executor;
	}

	public String getStrExec() {
		return strExec;
	}

	public void setStrExec(String strExec) {
		this.strExec = strExec;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public String getSubmitName() {
		return submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getPagePattern() {
		return pagePattern;
	}

	public void setPagePattern(String pagePattern) {
		this.pagePattern = pagePattern;
	}
}
