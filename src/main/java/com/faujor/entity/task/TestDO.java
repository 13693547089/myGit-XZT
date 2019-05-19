package com.faujor.entity.task;

import java.io.Serializable;

public class TestDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
