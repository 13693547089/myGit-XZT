package com.faujor.entity.rm;

import java.util.List;

public class SqlParams<T> {

	private String main_table_name;// 主表名

	private String where_column;// where条件名

	private String date_formate;// 日期字段的类型，是date还是varchar
	private String date_type; // 日期的类型，是月还是日
	private String start_date; // 开始日期
	private String end_date; // 结束日期
	private String add_where_column;// 主表额外条件
	private String main_filter;// 主表条件

	private List<T> paramsList;

	private String details_table_name; // 详情表名

	private String on_condition;

	private String status_column;
	private String firstMonth;
	private String secondMonth;
	private String thirdMonth;
	private String fourMonth;
	private String fiveMonth;
	private String sixMonth;

	private String add_detials_where_column;// 从表额外条件

	// 频率
	private String date_str;
	private String user_type;
	private String operate_type;
	private String sort_type;

	public String getMain_table_name() {
		return main_table_name;
	}

	public void setMain_table_name(String main_table_name) {
		this.main_table_name = main_table_name;
	}

	public String getWhere_column() {
		return where_column;
	}

	public void setWhere_column(String where_column) {
		this.where_column = where_column;
	}

	public String getDate_formate() {
		return date_formate;
	}

	public void setDate_formate(String date_formate) {
		this.date_formate = date_formate;
	}

	public String getDate_type() {
		return date_type;
	}

	public void setDate_type(String date_type) {
		this.date_type = date_type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getAdd_where_column() {
		return add_where_column;
	}

	public void setAdd_where_column(String add_where_column) {
		this.add_where_column = add_where_column;
	}

	public String getMain_filter() {
		return main_filter;
	}

	public void setMain_filter(String main_filter) {
		this.main_filter = main_filter;
	}

	public List<T> getParamsList() {
		return paramsList;
	}

	public void setParamsList(List<T> paramsList) {
		this.paramsList = paramsList;
	}

	public String getDetails_table_name() {
		return details_table_name;
	}

	public void setDetails_table_name(String details_table_name) {
		this.details_table_name = details_table_name;
	}

	public String getOn_condition() {
		return on_condition;
	}

	public void setOn_condition(String on_condition) {
		this.on_condition = on_condition;
	}

	public String getAdd_detials_where_column() {
		return add_detials_where_column;
	}

	public void setAdd_detials_where_column(String add_detials_where_column) {
		this.add_detials_where_column = add_detials_where_column;
	}

	public String getStatus_column() {
		return status_column;
	}

	public void setStatus_column(String status_column) {
		this.status_column = status_column;
	}

	public String getFirstMonth() {
		return firstMonth;
	}

	public void setFirstMonth(String firstMonth) {
		this.firstMonth = firstMonth;
	}

	public String getSecondMonth() {
		return secondMonth;
	}

	public void setSecondMonth(String secondMonth) {
		this.secondMonth = secondMonth;
	}

	public String getThirdMonth() {
		return thirdMonth;
	}

	public void setThirdMonth(String thirdMonth) {
		this.thirdMonth = thirdMonth;
	}

	public String getFourMonth() {
		return fourMonth;
	}

	public void setFourMonth(String fourMonth) {
		this.fourMonth = fourMonth;
	}

	public String getFiveMonth() {
		return fiveMonth;
	}

	public void setFiveMonth(String fiveMonth) {
		this.fiveMonth = fiveMonth;
	}

	public String getSixMonth() {
		return sixMonth;
	}

	public void setSixMonth(String sixMonth) {
		this.sixMonth = sixMonth;
	}

	public String getDate_str() {
		return date_str;
	}

	public void setDate_str(String date_str) {
		this.date_str = date_str;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getOperate_type() {
		return operate_type;
	}

	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}

	public String getSort_type() {
		return sort_type;
	}

	public void setSort_type(String sort_type) {
		this.sort_type = sort_type;
	}
}
