package com.faujor.entity.common;

import java.util.Date;

/**
 * 接口日志
 * @author hql
 *
 */
public class InterfaceLogMain {
	
	private String id;
//	接口编号或者方法名称
	private String interfaceNum;
//	接口或者方法描述
	private String interfaceDesc;
//	传入参数
	private String inJson;
//	传出参数
	private String outJson;
//	调用时间
	private Date invokeTime;
//	状态
	private String status;
//	消息
	private String message;
//	调用人
	private String invoker;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInterfaceNum() {
		return interfaceNum;
	}
	public void setInterfaceNum(String interfaceNum) {
		this.interfaceNum = interfaceNum;
	}
	public String getInterfaceDesc() {
		return interfaceDesc;
	}
	public void setInterfaceDesc(String interfaceDesc) {
		this.interfaceDesc = interfaceDesc;
	}
	public String getInJson() {
		return inJson;
	}
	public void setInJson(String inJson) {
		this.inJson = inJson;
	}
	public String getOutJson() {
		return outJson;
	}
	public void setOutJson(String outJson) {
		this.outJson = outJson;
	}
	public Date getInvokeTime() {
		return invokeTime;
	}
	public void setInvokeTime(Date invokeTime) {
		this.invokeTime = invokeTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getInvoker() {
		return invoker;
	}
	public void setInvoker(String invoker) {
		this.invoker = invoker;
	}
}
