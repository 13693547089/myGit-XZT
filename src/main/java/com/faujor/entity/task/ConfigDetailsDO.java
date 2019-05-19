package com.faujor.entity.task;

import java.io.Serializable;
import java.util.Date;

public class ConfigDetailsDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String confId;
	private Integer node;
	private String nodeName;
	private String executor;
	private String executorType;
	private Date createTime;
	private String creator;
	private String creatorName;
	private Date modifyTime;
	private String modifier;
	private String modifierName;
	private String LAY_TABLE_INDEX;
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
	public String getExecutorType() {
		return executorType;
	}
	public void setExecutorType(String executorType) {
		this.executorType = executorType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public String getLAY_TABLE_INDEX() {
		return LAY_TABLE_INDEX;
	}
	public void setLAY_TABLE_INDEX(String lAY_TABLE_INDEX) {
		LAY_TABLE_INDEX = lAY_TABLE_INDEX;
	}
}
