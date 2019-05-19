package com.faujor.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 页面用具
 */
public class PageUtils<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String msg;
	private String code;
	// 总记录
	private int count;
	// 列表数据
	private List<T> data;

	/**
	 * 分页
	 * 
	 * @param list       列表数据
	 * @param totalCount 总记录数
	 * @param pageSize   每页记录数
	 * @param currPage   当前页数
	 */
	public PageUtils(List<T> data, int count, String msg, String code) {
		this.data = data;
		this.count = count;
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
