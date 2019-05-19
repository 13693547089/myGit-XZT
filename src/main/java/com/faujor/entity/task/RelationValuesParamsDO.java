package com.faujor.entity.task;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationValuesParamsDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tableName; // 数据库表名
	private String idValue; // 主键id的值
	private String idName;// 主键id的标识
	private String condition; // 过滤条件
	private String orderRelation; // 排序关系
	private String returnRelation;// 返回值

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getOrderRelation() {
		return orderRelation;
	}

	public void setOrderRelation(String orderRelation) {
		this.orderRelation = orderRelation;
	}

	public String getReturnRelation() {
		return returnRelation;
	}

	public void setReturnRelation(String returnRelation) {
		this.returnRelation = returnRelation;
	}
}
