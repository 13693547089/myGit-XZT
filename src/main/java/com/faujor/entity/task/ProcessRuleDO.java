package com.faujor.entity.task;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessRuleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;// 主键ID
	private String mainId;// 主表ID
	private Integer node;// 节点
	private Integer openChooseDialog;// 是否弹出确认选择框(1弹出，其他不弹出）
	private String executor; // 待选执行者ids
	private String execName; // 待选执行者名称
	private String nodeType; // 节点类型
	private String taskName;// 任务标题（任务规则）
	private String defaultAudit;// 默认审批人（上级领导）
	private String execType;// 执行者类型
	private String execStr;// 执行者表达式
	private String execRange;// 执行者范围表达式
	private String ruleType;// 规则类型（执行规则，回退规则，转发规则，流转规则，
	private String condition;// 条件环节的条件
	private Integer tBackNode;// 条件为真时输出节点
	private Integer fBackNode;// 条件为假时输出节点

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

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	public Integer getOpenChooseDialog() {
		return openChooseDialog;
	}

	public void setOpenChooseDialog(Integer openChooseDialog) {
		this.openChooseDialog = openChooseDialog;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getExecName() {
		return execName;
	}

	public void setExecName(String execName) {
		this.execName = execName;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getDefaultAudit() {
		return defaultAudit;
	}

	public void setDefaultAudit(String defaultAudit) {
		this.defaultAudit = defaultAudit;
	}

	public String getExecType() {
		return execType;
	}

	public void setExecType(String execType) {
		this.execType = execType;
	}

	public String getExecStr() {
		return execStr;
	}

	public void setExecStr(String execStr) {
		this.execStr = execStr;
	}

	public String getExecRange() {
		return execRange;
	}

	public void setExecRange(String execRange) {
		this.execRange = execRange;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Integer gettBackNode() {
		return tBackNode;
	}

	public void settBackNode(Integer tBackNode) {
		this.tBackNode = tBackNode;
	}

	public Integer getfBackNode() {
		return fBackNode;
	}

	public void setfBackNode(Integer fBackNode) {
		this.fBackNode = fBackNode;
	}
}
