package com.faujor.entity.task;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCheckResult implements Serializable {
	private static final long serialVersionUID = -5116387747730903352L;

	private Integer nexeNode;// 下一个流转的节点
	private String isProcess; // 是否往下流转
	private String isOpen;// 是否打开选择执行者弹出框

	public Integer getNexeNode() {
		return nexeNode;
	}

	public void setNexeNode(Integer nexeNode) {
		this.nexeNode = nexeNode;
	}

	public String getIsProcess() {
		return isProcess;
	}

	public void setIsProcess(String isProcess) {
		this.isProcess = isProcess;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

}
