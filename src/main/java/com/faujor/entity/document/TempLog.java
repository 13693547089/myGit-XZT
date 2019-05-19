package com.faujor.entity.document;

import java.io.Serializable;
import java.util.Date;

public class TempLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Id;
	//操作
	private String operation;
//	操作人
	private String operator;
//	操作时间
	private Date operateTime;
//	模板编号
	private String tempNo;
//	操作人名称
	private String operatorName;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getTempNo() {
		return tempNo;
	}

	public void setTempNo(String tempNo) {
		this.tempNo = tempNo;
	}
	
	

}
