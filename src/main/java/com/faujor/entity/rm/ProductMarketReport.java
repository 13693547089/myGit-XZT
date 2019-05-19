package com.faujor.entity.rm;

public class ProductMarketReport {
	private String month_date;

	// 计数
	private Integer pad_count;
	private Integer sip_count;
	private Integer pdr_count;

	// 环比
	private Float pad_chain_ratio;
	private Float sip_chain_ratio;
	private Float pdr_chain_ratio;
	// 同比
	private Float pad_yoy_ratio;
	private Float sip_yoy_ratio;
	private Float pdr_yoy_ratio;

	public String getMonth_date() {
		return month_date;
	}

	public void setMonth_date(String month_date) {
		this.month_date = month_date;
	}

	public Integer getPad_count() {
		return pad_count;
	}

	public void setPad_count(Integer pad_count) {
		this.pad_count = pad_count;
	}

	public Integer getSip_count() {
		return sip_count;
	}

	public void setSip_count(Integer sip_count) {
		this.sip_count = sip_count;
	}

	public Integer getPdr_count() {
		return pdr_count;
	}

	public void setPdr_count(Integer pdr_count) {
		this.pdr_count = pdr_count;
	}

	public Float getPad_chain_ratio() {
		return pad_chain_ratio;
	}

	public void setPad_chain_ratio(Float pad_chain_ratio) {
		this.pad_chain_ratio = pad_chain_ratio;
	}

	public Float getSip_chain_ratio() {
		return sip_chain_ratio;
	}

	public void setSip_chain_ratio(Float sip_chain_ratio) {
		this.sip_chain_ratio = sip_chain_ratio;
	}

	public Float getPdr_chain_ratio() {
		return pdr_chain_ratio;
	}

	public void setPdr_chain_ratio(Float pdr_chain_ratio) {
		this.pdr_chain_ratio = pdr_chain_ratio;
	}

	public Float getPad_yoy_ratio() {
		return pad_yoy_ratio;
	}

	public void setPad_yoy_ratio(Float pad_yoy_ratio) {
		this.pad_yoy_ratio = pad_yoy_ratio;
	}

	public Float getSip_yoy_ratio() {
		return sip_yoy_ratio;
	}

	public void setSip_yoy_ratio(Float sip_yoy_ratio) {
		this.sip_yoy_ratio = sip_yoy_ratio;
	}

	public Float getPdr_yoy_ratio() {
		return pdr_yoy_ratio;
	}

	public void setPdr_yoy_ratio(Float pdr_yoy_ratio) {
		this.pdr_yoy_ratio = pdr_yoy_ratio;
	}
}
