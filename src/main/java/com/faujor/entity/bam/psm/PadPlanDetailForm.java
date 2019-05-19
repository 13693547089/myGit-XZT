package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PadPlanDetailForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String matCode;	// 物料编码
	private String matName;	// 物料名称
	private String prodSeriesCode;	// 产品系列编码
	private String prodSeries;	// 产品系列
	private String startVersion;//起始版本
	private String endVersion;//截止版本
	private String bigItemCode;	// 大品项编码
	private String bigItemExpl;	// 大品项	
	private String produCode;//产能划分编码
	private String produExpl;//产能划分
	private String planMonth;//计划月份
	/**
	 * @return the matCode
	 */
	public String getMatCode() {
		return matCode;
	}
	/**
	 * @param matCode the matCode to set
	 */
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	/**
	 * @return the matName
	 */
	public String getMatName() {
		return matName;
	}
	/**
	 * @param matName the matName to set
	 */
	public void setMatName(String matName) {
		this.matName = matName;
	}
	/**
	 * @return the prodSeriesCode
	 */
	public String getProdSeriesCode() {
		return prodSeriesCode;
	}
	/**
	 * @param prodSeriesCode the prodSeriesCode to set
	 */
	public void setProdSeriesCode(String prodSeriesCode) {
		this.prodSeriesCode = prodSeriesCode;
	}
	/**
	 * @return the prodSeries
	 */
	public String getProdSeries() {
		return prodSeries;
	}
	/**
	 * @param prodSeries the prodSeries to set
	 */
	public void setProdSeries(String prodSeries) {
		this.prodSeries = prodSeries;
	}
	/**
	 * @return the startVersion
	 */
	public String getStartVersion() {
		return startVersion;
	}
	/**
	 * @param startVersion the startVersion to set
	 */
	public void setStartVersion(String startVersion) {
		this.startVersion = startVersion;
	}
	/**
	 * @return the endVersion
	 */
	public String getEndVersion() {
		return endVersion;
	}
	/**
	 * @param endVersion the endVersion to set
	 */
	public void setEndVersion(String endVersion) {
		this.endVersion = endVersion;
	}
	/**
	 * @return the bigItemCode
	 */
	public String getBigItemCode() {
		return bigItemCode;
	}
	/**
	 * @param bigItemCode the bigItemCode to set
	 */
	public void setBigItemCode(String bigItemCode) {
		this.bigItemCode = bigItemCode;
	}
	/**
	 * @return the bigItemExpl
	 */
	public String getBigItemExpl() {
		return bigItemExpl;
	}
	/**
	 * @param bigItemExpl the bigItemExpl to set
	 */
	public void setBigItemExpl(String bigItemExpl) {
		this.bigItemExpl = bigItemExpl;
	}
	/**
	 * @return the produCode
	 */
	public String getProduCode() {
		return produCode;
	}
	/**
	 * @param produCode the produCode to set
	 */
	public void setProduCode(String produCode) {
		this.produCode = produCode;
	}
	/**
	 * @return the produExpl
	 */
	public String getProduExpl() {
		return produExpl;
	}
	/**
	 * @param produExpl the produExpl to set
	 */
	public void setProduExpl(String produExpl) {
		this.produExpl = produExpl;
	}
	/**
	 * @return the planMonth
	 */
	public String getPlanMonth() {
		return planMonth;
	}
	/**
	 * @param planMonth the planMonth to set
	 */
	public void setPlanMonth(String planMonth) {
		this.planMonth = planMonth;
	}
	
}
