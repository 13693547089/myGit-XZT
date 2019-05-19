package com.faujor.entity.bam;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 报价单物料报价信息
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class QuotePrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	主键
	private String id;
//	报价单号
	private String quoteCode;
//	报价单下的物料Id
	private String quoteMateId;
//	报价单下的物料编码
	private String mateNo;
//	段号
	private String segmCode;
//	段名
	private String segmName;
//	组件编号
	private String asseCode;
//	组件名称
	private String asseName;
//	物料编号
	private String mateCode;
//	价格
	private BigDecimal detailPrice;
//	数量
	private BigDecimal detailsNum;
//	单位
	private String detailUnit;
//	规格
	private String standard;
//	材料
	private String material;
//	备注
	private String remark;
	
	//--------
//	单价
	private BigDecimal unitPrice;
//	原材料价格
	private BigDecimal matePrice;
//	原材料单位
	private String mateUnit;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public String getQuoteMateId() {
		return quoteMateId;
	}
	public void setQuoteMateId(String quoteMateId) {
		this.quoteMateId = quoteMateId;
	}
	public String getMateNo() {
		return mateNo;
	}
	public void setMateNo(String mateNo) {
		this.mateNo = mateNo;
	}
	public String getSegmCode() {
		return segmCode;
	}
	public void setSegmCode(String segmCode) {
		this.segmCode = segmCode;
	}
	public String getSegmName() {
		return segmName;
	}
	public void setSegmName(String segmName) {
		this.segmName = segmName;
	}
	public String getAsseCode() {
		return asseCode;
	}
	public void setAsseCode(String asseCode) {
		this.asseCode = asseCode;
	}
	public String getAsseName() {
		return asseName;
	}
	public void setAsseName(String asseName) {
		this.asseName = asseName;
	}
	public String getMateCode() {
		return mateCode;
	}
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	public BigDecimal getDetailPrice() {
		return detailPrice;
	}
	public void setDetailPrice(BigDecimal detailPrice) {
		this.detailPrice = detailPrice;
	}
	public BigDecimal getDetailsNum() {
		return detailsNum;
	}
	public void setDetailsNum(BigDecimal detailsNum) {
		this.detailsNum = detailsNum;
	}
	public String getDetailUnit() {
		return detailUnit;
	}
	public void setDetailUnit(String detailUnit) {
		this.detailUnit = detailUnit;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getMatePrice() {
		return matePrice;
	}
	public void setMatePrice(BigDecimal matePrice) {
		this.matePrice = matePrice;
	}
	public String getMateUnit() {
		return mateUnit;
	}
	public void setMateUnit(String mateUnit) {
		this.mateUnit = mateUnit;
	}
}
