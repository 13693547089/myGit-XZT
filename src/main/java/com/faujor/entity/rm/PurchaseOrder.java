package com.faujor.entity.rm;

public class PurchaseOrder {

	private String month_date;

	private int po_count;
	private int po_item_count;

	private Float po_chain_ratio; // 环比
	private Float po_item_chain_ratio;// 行项目环比

	private Float po_YOY_ratio; // 同比
	private Float po_item_YOY_ratio; // 行项目同比

	public String getMonth_date() {
		return month_date;
	}

	public void setMonth_date(String month_date) {
		this.month_date = month_date;
	}

	public int getPo_count() {
		return po_count;
	}

	public void setPo_count(int po_count) {
		this.po_count = po_count;
	}

	public int getPo_item_count() {
		return po_item_count;
	}

	public void setPo_item_count(int po_item_count) {
		this.po_item_count = po_item_count;
	}

	public Float getPo_chain_ratio() {
		return po_chain_ratio;
	}

	public void setPo_chain_ratio(Float po_chain_ratio) {
		this.po_chain_ratio = po_chain_ratio;
	}

	public Float getPo_item_chain_ratio() {
		return po_item_chain_ratio;
	}

	public void setPo_item_chain_ratio(Float po_item_chain_ratio) {
		this.po_item_chain_ratio = po_item_chain_ratio;
	}

	public Float getPo_YOY_ratio() {
		return po_YOY_ratio;
	}

	public void setPo_YOY_ratio(Float po_YOY_ratio) {
		this.po_YOY_ratio = po_YOY_ratio;
	}

	public Float getPo_item_YOY_ratio() {
		return po_item_YOY_ratio;
	}

	public void setPo_item_YOY_ratio(Float po_item_YOY_ratio) {
		this.po_item_YOY_ratio = po_item_YOY_ratio;
	}
}
