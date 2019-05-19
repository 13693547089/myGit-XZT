package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.util.Date;

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
public class PdrDetailVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mateCode;	// 物料编码
	private String mateName;	// 物料名称
	private String suppNo;//供应商编码
	private String suppName;//供应商名称
	private Date produceDate;	// 生产日期
	private Float actPdcQty;	// 实际生产
	private Float actDevQty;	// 实际交货
	private Float stockQty;		// 库存数量
	public String getMateCode() {
		return mateCode;
	}
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	public String getMateName() {
		return mateName;
	}
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	public String getSuppNo() {
		return suppNo;
	}
	public void setSuppNo(String suppNo) {
		this.suppNo = suppNo;
	}
	public String getSuppName() {
		return suppName;
	}
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	public Date getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
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
}
