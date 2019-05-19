package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：PdrDetail
 * @tableName PS_PDR_DETAIL
 * @tableDesc 生产日报明细表
 * @author Vincent
 * @date 2018-03-01
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PdrDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer sn;
	private String mainId;	// 主表ID
	private String matCode;	// 物料编码
	private String matName;	// 物料名称
	private String matUnit;	// 物料单位
	private String prodSeries;	// 产品系列
	private Float pdcPlanQty;	// 本月生产计划
	private Float currNeedQty; 	// 目前应完成
	private Float preCompQty; 	// 之前已完成
	private Float currCompQty; 	// 目前已完成
	private String compScale;	// 生产达成比
	private Float actPdcQty;	// 实际生产
	private Float actDevQty;	// 实际交货
	private Float stockQty;		// 库存数量
	private String batch;		// 生产批
	private String remark;		// 备注
	private Float qcStock;		// 已检库存数量
	private Float unQcStock;	// 未检库存数量
	
	private Float beginStock; // 期初库存(上个月期末库存)
	private Float theoryStock;// 理论库存
	private Float diffStock;  // 差异库存
	
	private Float preSumDev;  // 之前总的交货数量
	
	private Float devPlanQty; // 本月交货计划
	private String devCompScale;	// 交货达成比
	private Float currSumDev;	// 目前交货数量
	
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
	public Float getPdcPlanQty() {
		return pdcPlanQty;
	}
	public void setPdcPlanQty(Float pdcPlanQty) {
		this.pdcPlanQty = pdcPlanQty;
	}
	public Float getCurrNeedQty() {
		return currNeedQty;
	}
	public void setCurrNeedQty(Float currNeedQty) {
		this.currNeedQty = currNeedQty;
	}
	public Float getPreCompQty() {
		return preCompQty;
	}
	public void setPreCompQty(Float preCompQty) {
		this.preCompQty = preCompQty;
	}
	public Float getCurrCompQty() {
		return currCompQty;
	}
	public void setCurrCompQty(Float currCompQty) {
		this.currCompQty = currCompQty;
	}
	public String getCompScale() {
		return compScale;
	}
	public void setCompScale(String compScale) {
		this.compScale = compScale;
	}
	public Float getActPdcQty() {
		return actPdcQty;
	}
	public void setActPdcQty(Float actPdcQty) {
		this.actPdcQty = actPdcQty;
	}
	public Float getActDevQty() {
		return actDevQty;
	}
	public void setActDevQty(Float actDevQty) {
		this.actDevQty = actDevQty;
	}
	public Float getStockQty() {
		return stockQty;
	}
	public void setStockQty(Float stockQty) {
		this.stockQty = stockQty;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Float getQcStock() {
		return qcStock;
	}
	public void setQcStock(Float qcStock) {
		this.qcStock = qcStock;
	}
	public Float getUnQcStock() {
		return unQcStock;
	}
	public void setUnQcStock(Float unQcStock) {
		this.unQcStock = unQcStock;
	}
	public Float getBeginStock() {
		return beginStock;
	}
	public void setBeginStock(Float beginStock) {
		this.beginStock = beginStock;
	}
	public Float getTheoryStock() {
		return theoryStock;
	}
	public void setTheoryStock(Float theoryStock) {
		this.theoryStock = theoryStock;
	}
	public Float getDiffStock() {
		return diffStock;
	}
	public void setDiffStock(Float diffStock) {
		this.diffStock = diffStock;
	}
	public Float getPreSumDev() {
		return preSumDev;
	}
	public void setPreSumDev(Float preSumDev) {
		this.preSumDev = preSumDev;
	}
	public Float getDevPlanQty() {
		return devPlanQty;
	}
	public void setDevPlanQty(Float devPlanQty) {
		this.devPlanQty = devPlanQty;
	}
	public String getDevCompScale() {
		return devCompScale;
	}
	public void setDevCompScale(String devCompScale) {
		this.devCompScale = devCompScale;
	}
	public Float getCurrSumDev() {
		return currSumDev;
	}
	public void setCurrSumDev(Float currSumDev) {
		this.currSumDev = currSumDev;
	}
}
