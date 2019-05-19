package com.faujor.entity.rm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginFrequence {

	private String month_date;
	private Integer inner_login_count;
	private Integer inner_operate_count;
	private Integer outer_login_count;
	private Integer outer_operate_count;

	private String user_name;
	private String user_count;

	public String getMonth_date() {
		return month_date;
	}

	public void setMonth_date(String month_date) {
		this.month_date = month_date;
	}

	public Integer getInner_login_count() {
		return inner_login_count;
	}

	public void setInner_login_count(Integer inner_login_count) {
		this.inner_login_count = inner_login_count;
	}

	public Integer getInner_operate_count() {
		return inner_operate_count;
	}

	public void setInner_operate_count(Integer inner_operate_count) {
		this.inner_operate_count = inner_operate_count;
	}

	public Integer getOuter_login_count() {
		return outer_login_count;
	}

	public void setOuter_login_count(Integer outer_login_count) {
		this.outer_login_count = outer_login_count;
	}

	public Integer getOuter_operate_count() {
		return outer_operate_count;
	}

	public void setOuter_operate_count(Integer outer_operate_count) {
		this.outer_operate_count = outer_operate_count;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_count() {
		return user_count;
	}

	public void setUser_count(String user_count) {
		this.user_count = user_count;
	}
}
