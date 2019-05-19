package com.faujor.entity.sapcenter.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：TranPlanMatDetail
 * @tableDesc 调拨计划物料明细
 * @author Vincent
 * @date 2018-03-01
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranPlanMatDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer sn;
	private String mainId;	// 主表ID
	private String matCode;	// 物料编码
	private String matName;	// 物料名称
	private String rank;	// 排名
	private String matUnit;	// 物料单位
	private String prodSeries;	// 产品系列
	private Float saleForeQty;	// 销售预测
	private Float saleQty;		// 已销售
	private String saleScale;	// 销售达成率
	private Float planDevQty; 	// 交货计划
	private Float actDevQty;	// 已交量
	private String devScale;	// 交货达成率
	private Float unDevQty; 	// 未交量
	private Float monQty;		// 周一调拨量
	private Float tueQty;		// 周二调拨量
	private Float wedQty;		// 周三调拨量
	private Float thuQty;		// 周四调拨量
	private Float friQty;		// 周五调拨量
	private Float satQty;		// 周六调拨量
	private Float sunQty;		// 周日调拨量
	
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
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getMatUnit() {
		return matUnit;
	}
	public void setMatUnit(String matUnit) {
		this.matUnit = matUnit;
	}
	public String getProdSeries() {
		return prodSeries;
	}
	public void setProdSeries(String prodSeries) {
		this.prodSeries = prodSeries;
	}
	public Float getSaleForeQty() {
		return saleForeQty;
	}
	public void setSaleForeQty(Float saleForeQty) {
		this.saleForeQty = saleForeQty;
	}
	public Float getSaleQty() {
		return saleQty;
	}
	public void setSaleQty(Float saleQty) {
		this.saleQty = saleQty;
	}
	public String getSaleScale() {
		return saleScale;
	}
	public void setSaleScale(String saleScale) {
		this.saleScale = saleScale;
	}
	public Float getPlanDevQty() {
		return planDevQty;
	}
	public void setPlanDevQty(Float planDevQty) {
		this.planDevQty = planDevQty;
	}
	public Float getActDevQty() {
		return actDevQty;
	}
	public void setActDevQty(Float actDevQty) {
		this.actDevQty = actDevQty;
	}
	public String getDevScale() {
		return devScale;
	}
	public void setDevScale(String devScale) {
		this.devScale = devScale;
	}
	public Float getUnDevQty() {
		return unDevQty;
	}
	public void setUnDevQty(Float unDevQty) {
		this.unDevQty = unDevQty;
	}
	public Float getMonQty() {
		return monQty;
	}
	public void setMonQty(Float monQty) {
		this.monQty = monQty;
	}
	public Float getTueQty() {
		return tueQty;
	}
	public void setTueQty(Float tueQty) {
		this.tueQty = tueQty;
	}
	public Float getWedQty() {
		return wedQty;
	}
	public void setWedQty(Float wedQty) {
		this.wedQty = wedQty;
	}
	public Float getThuQty() {
		return thuQty;
	}
	public void setThuQty(Float thuQty) {
		this.thuQty = thuQty;
	}
	public Float getFriQty() {
		return friQty;
	}
	public void setFriQty(Float friQty) {
		this.friQty = friQty;
	}
	public Float getSatQty() {
		return satQty;
	}
	public void setSatQty(Float satQty) {
		this.satQty = satQty;
	}
	public Float getSunQty() {
		return sunQty;
	}
	public void setSunQty(Float sunQty) {
		this.sunQty = sunQty;
	}
}
