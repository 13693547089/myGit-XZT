package com.faujor.entity.document;
/**
 * 文件上传的返回信息
 * code必须返回否则默认为接口异常
 * 默认0
 * @author hql
 *
 */
public class UploadResult {
	
	private String code="0";
	
	private String msg="操作成功！";
	
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}
