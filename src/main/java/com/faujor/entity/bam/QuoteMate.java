package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 报价单物料列表
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class QuoteMate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Id
	private String id;
//	报价单号
	private String quoteCode;
//	物料编码
	private String mateNo;
//	供应商范围
	private String suppScope;
//	备注
	private String remark;
	//物料的SAP编码
	private String mateCode;
//	有效开始时间
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GTM+8")
	private Date startDate;
//	有效结束时间
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GTM+8")
	private Date endDate;
	//原因
	private String mateReason;
	
	
	private String mateName;
	
	private List<QuotePrice> priceList;
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
	public String getMateNo() {
		return mateNo;
	}
	public void setMateNo(String mateNo) {
		this.mateNo = mateNo;
	}
	public String getSuppScope() {
		return suppScope;
	}
	public void setSuppScope(String suppScope) {
		this.suppScope = suppScope;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<QuotePrice> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<QuotePrice> priceList) {
		this.priceList = priceList;
	}
	public String getMateCode() {
		return mateCode;
	}
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the mateReason
	 */
	public String getMateReason() {
		return mateReason;
	}
	/**
	 * @param mateReason the mateReason to set
	 */
	public void setMateReason(String mateReason) {
		this.mateReason = mateReason;
	}
	public String getMateName() {
		return mateName;
	}
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	
	
}
