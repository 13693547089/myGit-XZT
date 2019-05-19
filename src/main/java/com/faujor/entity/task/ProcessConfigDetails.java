package com.faujor.entity.task;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessConfigDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id; // 主键
	private String confId; // 主表id
	private Integer node; // 节点
	private String nodeName; // 节点名称
	private String executor; // 执行者
	private String execName; // 执行者姓名
	private String execType; // 执行者类型
	private String execRule; // 执行规则
	private String handWay; // 执行方式
	private String handWayDesc; // 执行方式描述
	private String backWay; // 回退方式
	private String backWayDesc; // 回退方式描述
	private String backNode; // 回退节点
	private String isBack; // 是否回退
	private String nodeCondition; // 节点条件
	private String transmitRule; // 转发规则
	private String noticeRule; // 通知规则
	private String processRule; // 流转规则
	private String backRule; // 回退规则
	private String nodeType; // 节点类型
	private String nodeTypeDesc; // 节点类型描述

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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

	public String getExecType() {
		return execType;
	}

	public void setExecType(String execType) {
		this.execType = execType;
	}

	public String getExecRule() {
		return execRule;
	}

	public void setExecRule(String execRule) {
		this.execRule = execRule;
	}

	public String getHandWay() {
		return handWay;
	}

	public void setHandWay(String handWay) {
		this.handWay = handWay;
	}

	public String getHandWayDesc() {
		return handWayDesc;
	}

	public void setHandWayDesc(String handWayDesc) {
		this.handWayDesc = handWayDesc;
	}

	public String getBackWay() {
		return backWay;
	}

	public void setBackWay(String backWay) {
		this.backWay = backWay;
	}

	public String getBackWayDesc() {
		return backWayDesc;
	}

	public void setBackWayDesc(String backWayDesc) {
		this.backWayDesc = backWayDesc;
	}

	public String getBackNode() {
		return backNode;
	}

	public void setBackNode(String backNode) {
		this.backNode = backNode;
	}

	public String getIsBack() {
		return isBack;
	}

	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}

	public String getNodeCondition() {
		return nodeCondition;
	}

	public void setNodeCondition(String nodeCondition) {
		this.nodeCondition = nodeCondition;
	}

	public String getTransmitRule() {
		return transmitRule;
	}

	public void setTransmitRule(String transmitRule) {
		this.transmitRule = transmitRule;
	}

	public String getNoticeRule() {
		return noticeRule;
	}

	public void setNoticeRule(String noticeRule) {
		this.noticeRule = noticeRule;
	}

	public String getProcessRule() {
		return processRule;
	}

	public void setProcessRule(String processRule) {
		this.processRule = processRule;
	}

	public String getBackRule() {
		return backRule;
	}

	public void setBackRule(String backRule) {
		this.backRule = backRule;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeTypeDesc() {
		return nodeTypeDesc;
	}

	public void setNodeTypeDesc(String nodeTypeDesc) {
		this.nodeTypeDesc = nodeTypeDesc;
	}
}
