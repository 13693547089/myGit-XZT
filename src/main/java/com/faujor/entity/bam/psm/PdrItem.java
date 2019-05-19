package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：PdrItem
 * @tableName PS_PDR_ITEM
 * @tableDesc 生产日报项次表
 * @author Vincent
 * @date 2018-03-30
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PdrItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer sn;
	private String mainId;	// 主表ID
	private String matCode;	// 物料编码
	private String matName;	// 物料名称
	private String itemType;	// 品项类别
	private String batchNo;		// 批次号
	private Float qty; // 数量
	private String remark;		// 备注
	private Float qcQty; 	// 已检数量
	private Float unQcQty;  // 未检数量
	
	// 数据库中不存在字段
	private String prodSeries; // 系列
	private String boardName; // 类别
	
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
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getProdSeries() {
		return prodSeries;
	}
	public void setProdSeries(String prodSeries) {
		this.prodSeries = prodSeries;
	}
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
}
