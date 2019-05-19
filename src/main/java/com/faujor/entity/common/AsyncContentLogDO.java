package com.faujor.entity.common;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @target 同步数据的详细信息
 * @author martian
 * @time 2018-06-14 12:09:13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncContentLogDO implements Serializable {
	private static final long serialVersionUID = 1116335761880187448L;
	private String id;
	private String asyncCode;// 同步的业务数据编码或者ID
	private String asyncCode2; // 从表的标识
	private String asyncType;// 同步的业务类型（比如，采购订单同步，物料主数据同步）
	private String asyncContentStr; // 同步内容，json
	private Object asyncContent; // 同步内容
	private String asyncName; // 同步人
	private Date asyncTime; // 同步时间
	private String asyncOperate; // 同步操作

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAsyncCode() {
		return asyncCode;
	}

	public void setAsyncCode(String asyncCode) {
		this.asyncCode = asyncCode;
	}

	public String getAsyncCode2() {
		return asyncCode2;
	}

	public void setAsyncCode2(String asyncCode2) {
		this.asyncCode2 = asyncCode2;
	}

	public String getAsyncType() {
		return asyncType;
	}

	public void setAsyncType(String asyncType) {
		this.asyncType = asyncType;
	}

	public String getAsyncContentStr() {
		return asyncContentStr;
	}

	public void setAsyncContentStr(String asyncContentStr) {
		this.asyncContentStr = asyncContentStr;
	}

	public Object getAsyncContent() {
		return asyncContent;
	}

	public void setAsyncContent(Object asyncContent) {
		this.asyncContent = asyncContent;
	}

	public String getAsyncName() {
		return asyncName;
	}

	public void setAsyncName(String asyncName) {
		this.asyncName = asyncName;
	}

	public Date getAsyncTime() {
		return asyncTime;
	}

	public void setAsyncTime(Date asyncTime) {
		this.asyncTime = asyncTime;
	}

	public String getAsyncOperate() {
		return asyncOperate;
	}

	public void setAsyncOperate(String asyncOperate) {
		this.asyncOperate = asyncOperate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
