package com.faujor.entity.task;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessRuleBackDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id; // 主键id
	private String mainId;// 主表主键
	private Integer node; // 当前节点
	private String backConfirm;// 回退确认
	private String backForce; // 强制回退
	private String backSave; // 是否回退保存
	private String effectCondition;// 生效条件
	private String backWay;// 回退方式
	private String backRange;// 回退范围
	private Integer backNode;// 回退节点

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

	public String getBackConfirm() {
		return backConfirm;
	}

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	public void setBackConfirm(String backConfirm) {
		this.backConfirm = backConfirm;
	}

	public String getBackForce() {
		return backForce;
	}

	public void setBackForce(String backForce) {
		this.backForce = backForce;
	}

	public String getBackSave() {
		return backSave;
	}

	public void setBackSave(String backSave) {
		this.backSave = backSave;
	}

	public String getEffectCondition() {
		return effectCondition;
	}

	public void setEffectCondition(String effectCondition) {
		this.effectCondition = effectCondition;
	}

	public String getBackWay() {
		return backWay;
	}

	public void setBackWay(String backWay) {
		this.backWay = backWay;
	}

	public String getBackRange() {
		return backRange;
	}

	public void setBackRange(String backRange) {
		this.backRange = backRange;
	}

	public Integer getBackNode() {
		return backNode;
	}

	public void setBackNode(Integer backNode) {
		this.backNode = backNode;
	}
}
