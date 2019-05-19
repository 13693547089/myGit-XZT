package com.faujor.entity.common;

import java.util.List;
/**
 * Layui table 分页数据
 * @author hql
 * @param <T>
 */
public class LayuiPage<T> {
//	页码
	private Integer page;
//	页面行数
	private Integer limit;
//	分页的开始
	private Integer from;
//	分页的结束
	private Integer to;
//	返回信息
	private String code="0";
//	返回信息
	private String msg="操作成功！";
//	返回数据的条数
	private Integer count;
//	分页数据
	private List<T> data;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
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
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public Integer getTo() {
		return to;
	}
	public void setTo(Integer to) {
		this.to = to;
	}
	public void calculatePage(){
		this.from=(this.page-1)*limit;
		this.to=this.page*limit;
	}
}
