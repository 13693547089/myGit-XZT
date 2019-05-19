package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：PdrStockReport
 * @tableDesc 产能上报供应商库存报表
 * @author Vincent
 * @date 2018-08-02
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PdrStockReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String suppCode;	// 供应商编码
	private String suppName;	// 供应商
	private String produceDate;	// 生产日期
	private String series;	// 系列名称	
	private String matCode;	// 物料编码
	private String matName;	// 物料名称
	private String productType;		// 产品类别
	private String batchNo; // 批次号
	private Float qty; // 库存数量
	private Float qcQty; 	// 已检数量
	private Float unQcQty;  // 未检数量
	private String remark;		// 备注
	
	public String getSuppCode() {
		return suppCode;
	}
	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}
	public String getSuppName() {
		return suppName;
	}
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	public String getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
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
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Float getQty() {
		return qty;
	}
	public void setQty(Float qty) {
		this.qty = qty;
	}
	public Float getQcQty() {
		return qcQty;
	}
	public void setQcQty(Float qcQty) {
		this.qcQty = qcQty;
	}
	public Float getUnQcQty() {
		return unQcQty;
	}
	public void setUnQcQty(Float unQcQty) {
		this.unQcQty = unQcQty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
