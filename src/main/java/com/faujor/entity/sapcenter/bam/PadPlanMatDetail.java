package com.faujor.entity.sapcenter.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：PadPlanMatDetail
 * @tableDesc 生产/交货计划
 * @author Vincent
 * @date 2018-03-01
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PadPlanMatDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer sn;
	private String mainId;	// 主表ID
	private String matCode;	// 物料编码
	private String matName;	// 物料名称
	private String prodSeriesCode;	// 产品系列编码
	private String prodSeries;	// 产品系列
	private String rank;		// 排名
	private Float threeAvgSales;// 前三个月平均销量
	private Float nationStock1; // XX月全国库存
	private Float nationStock2; // YY月全国库存
	private Float nationStock3;	// ZZ月全国库存
	private Float padPlanQty;	// XX月生产交货计划
	private Float saleForeQty;	// XX月销售预测
	private Float turnOverDays;	// 周转天数
	private Float nextSaleForeQty;	// 下月销售预测
	private Float estDeliQty;	// XX月预计交货数量
	private Float estSaleQty;	// XX月预计销售数量
	private Float actSaleQty;	// XX月实际销售数量
	private Float actDeliQty;	// XX月实际生产数量
	private Float actTurnOverDays;	// 实际周转天数
	private String bigItemCode;	// 大品项编码
	private String bigItemExpl;	// 大品项
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}
	public String getProdSeriesCode() {
		return prodSeriesCode;
	}
	public void setProdSeriesCode(String prodSeriesCode) {
		this.prodSeriesCode = prodSeriesCode;
	}
	public String getProdSeries() {
		return prodSeries;
	}
	public void setProdSeries(String prodSeries) {
		this.prodSeries = prodSeries;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Float getThreeAvgSales() {
		return threeAvgSales;
	}
	public void setThreeAvgSales(Float threeAvgSales) {
		this.threeAvgSales = threeAvgSales;
	}
	public Float getNationStock1() {
		return nationStock1;
	}
	public void setNationStock1(Float nationStock1) {
		this.nationStock1 = nationStock1;
	}
	public Float getNationStock2() {
		return nationStock2;
	}
	public void setNationStock2(Float nationStock2) {
		this.nationStock2 = nationStock2;
	}
	public Float getNationStock3() {
		return nationStock3;
	}
	public void setNationStock3(Float nationStock3) {
		this.nationStock3 = nationStock3;
	}
	public Float getPadPlanQty() {
		return padPlanQty;
	}
	public void setPadPlanQty(Float padPlanQty) {
		this.padPlanQty = padPlanQty;
	}
	public Float getSaleForeQty() {
		return saleForeQty;
	}
	public void setSaleForeQty(Float saleForeQty) {
		this.saleForeQty = saleForeQty;
	}
	public Float getTurnOverDays() {
		return turnOverDays;
	}
	public void setTurnOverDays(Float turnOverDays) {
		this.turnOverDays = turnOverDays;
	}
	public Float getNextSaleForeQty() {
		return nextSaleForeQty;
	}
	public void setNextSaleForeQty(Float nextSaleForeQty) {
		this.nextSaleForeQty = nextSaleForeQty;
	}
	public Float getEstDeliQty() {
		return estDeliQty;
	}
	public void setEstDeliQty(Float estDeliQty) {
		this.estDeliQty = estDeliQty;
	}
	public Float getEstSaleQty() {
		return estSaleQty;
	}
	public void setEstSaleQty(Float estSaleQty) {
		this.estSaleQty = estSaleQty;
	}
	public Float getActSaleQty() {
		return actSaleQty;
	}
	public void setActSaleQty(Float actSaleQty) {
		this.actSaleQty = actSaleQty;
	}
	public Float getActDeliQty() {
		return actDeliQty;
	}
	public void setActDeliQty(Float actDeliQty) {
		this.actDeliQty = actDeliQty;
	}
	public Float getActTurnOverDays() {
		return actTurnOverDays;
	}
	public void setActTurnOverDays(Float actTurnOverDays) {
		this.actTurnOverDays = actTurnOverDays;
	}
	public String getBigItemCode() {
		return bigItemCode;
	}
	public void setBigItemCode(String bigItemCode) {
		this.bigItemCode = bigItemCode;
	}
	public String getBigItemExpl() {
		return bigItemExpl;
	}
	public void setBigItemExpl(String bigItemExpl) {
		this.bigItemExpl = bigItemExpl;
	}
}
