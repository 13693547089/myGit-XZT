package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PadPlanRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rank;		// 排名
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
	private Float qtyOne;//2019-01版本（例如）
	private Float qtyTwo;//2019-02版本
	private Float qtyThree;//2019-03版本
	private Float qtyFour;//2019-04版本
	private Float qtyFive;//2019-05版本
	private Float qtySix;//2019-06版本
	private Float qtySeven;//2019-07版本
	private Float qtyEight;//2019-08版本
	private Float qtyNine;//2019-09版本
	private Float qtyTen;//2019-10版本
	private Float qtyEleven;//2019-11版本
	private Float qtyTwelve;//2019-12版本
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
	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}
	/**
	 * @return the qtyOne
	 */
	public Float getQtyOne() {
		return qtyOne;
	}
	/**
	 * @param qtyOne the qtyOne to set
	 */
	public void setQtyOne(Float qtyOne) {
		this.qtyOne = qtyOne;
	}
	/**
	 * @return the qtyTwo
	 */
	public Float getQtyTwo() {
		return qtyTwo;
	}
	/**
	 * @param qtyTwo the qtyTwo to set
	 */
	public void setQtyTwo(Float qtyTwo) {
		this.qtyTwo = qtyTwo;
	}
	/**
	 * @return the qtyThree
	 */
	public Float getQtyThree() {
		return qtyThree;
	}
	/**
	 * @param qtyThree the qtyThree to set
	 */
	public void setQtyThree(Float qtyThree) {
		this.qtyThree = qtyThree;
	}
	/**
	 * @return the qtyFour
	 */
	public Float getQtyFour() {
		return qtyFour;
	}
	/**
	 * @param qtyFour the qtyFour to set
	 */
	public void setQtyFour(Float qtyFour) {
		this.qtyFour = qtyFour;
	}
	/**
	 * @return the qtyFive
	 */
	public Float getQtyFive() {
		return qtyFive;
	}
	/**
	 * @param qtyFive the qtyFive to set
	 */
	public void setQtyFive(Float qtyFive) {
		this.qtyFive = qtyFive;
	}
	/**
	 * @return the qtySix
	 */
	public Float getQtySix() {
		return qtySix;
	}
	/**
	 * @param qtySix the qtySix to set
	 */
	public void setQtySix(Float qtySix) {
		this.qtySix = qtySix;
	}
	/**
	 * @return the qtySeven
	 */
	public Float getQtySeven() {
		return qtySeven;
	}
	/**
	 * @param qtySeven the qtySeven to set
	 */
	public void setQtySeven(Float qtySeven) {
		this.qtySeven = qtySeven;
	}
	/**
	 * @return the qtyEight
	 */
	public Float getQtyEight() {
		return qtyEight;
	}
	/**
	 * @param qtyEight the qtyEight to set
	 */
	public void setQtyEight(Float qtyEight) {
		this.qtyEight = qtyEight;
	}
	/**
	 * @return the qtyNine
	 */
	public Float getQtyNine() {
		return qtyNine;
	}
	/**
	 * @param qtyNine the qtyNine to set
	 */
	public void setQtyNine(Float qtyNine) {
		this.qtyNine = qtyNine;
	}
	/**
	 * @return the qtyTen
	 */
	public Float getQtyTen() {
		return qtyTen;
	}
	/**
	 * @param qtyTen the qtyTen to set
	 */
	public void setQtyTen(Float qtyTen) {
		this.qtyTen = qtyTen;
	}
	/**
	 * @return the qtyEleven
	 */
	public Float getQtyEleven() {
		return qtyEleven;
	}
	/**
	 * @param qtyEleven the qtyEleven to set
	 */
	public void setQtyEleven(Float qtyEleven) {
		this.qtyEleven = qtyEleven;
	}
	/**
	 * @return the qtyTwelve
	 */
	public Float getQtyTwelve() {
		return qtyTwelve;
	}
	/**
	 * @param qtyTwelve the qtyTwelve to set
	 */
	public void setQtyTwelve(Float qtyTwelve) {
		this.qtyTwelve = qtyTwelve;
	}
	
	
	
	
	
}
