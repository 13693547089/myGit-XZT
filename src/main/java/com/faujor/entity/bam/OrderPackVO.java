package com.faujor.entity.bam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class OrderPackVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;	//varcher2	50	编号
	private String oemOrderCode;	//	varcher2	100	关联OEM采购订单编号
	private String oemSapId;//OEM供应商sap编码
	private String oemSuppName;//OEM供应商名称
	private String status;	//	varchar2	20	状态
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date subeDate;//订单日期
	private String purchOrg;//PURCH_ORG 采购组织
	private String creator;//创建人编号
	private String createName;//创建人名称
	private BigDecimal limitThan;//下单限比
	private String remarks;
	private BigDecimal subtotal;//小计
	private BigDecimal total;//合计
	private BigDecimal taxNumber;//税额
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;//订单起始日期
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;//订单结束日期
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the oemOrderCode
	 */
	public String getOemOrderCode() {
		return oemOrderCode;
	}
	/**
	 * @param oemOrderCode the oemOrderCode to set
	 */
	public void setOemOrderCode(String oemOrderCode) {
		this.oemOrderCode = oemOrderCode;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the oemSuppName
	 */
	public String getOemSuppName() {
		return oemSuppName;
	}
	/**
	 * @param oemSuppName the oemSuppName to set
	 */
	public void setOemSuppName(String oemSuppName) {
		this.oemSuppName = oemSuppName;
	}
	/**
	 * @return the subeDate
	 */
	public Date getSubeDate() {
		return subeDate;
	}
	/**
	 * @param subeDate the subeDate to set
	 */
	public void setSubeDate(Date subeDate) {
		this.subeDate = subeDate;
	}
	/**
	 * @return the purchOrg
	 */
	public String getPurchOrg() {
		return purchOrg;
	}
	/**
	 * @param purchOrg the purchOrg to set
	 */
	public void setPurchOrg(String purchOrg) {
		this.purchOrg = purchOrg;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the createName
	 */
	public String getCreateName() {
		return createName;
	}
	/**
	 * @param createName the createName to set
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	/**
	 * @return the oemSapId
	 */
	public String getOemSapId() {
		return oemSapId;
	}
	/**
	 * @param oemSapId the oemSapId to set
	 */
	public void setOemSapId(String oemSapId) {
		this.oemSapId = oemSapId;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the subtotal
	 */
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	/**
	 * @return the taxNumber
	 */
	public BigDecimal getTaxNumber() {
		return taxNumber;
	}
	/**
	 * @param taxNumber the taxNumber to set
	 */
	public void setTaxNumber(BigDecimal taxNumber) {
		this.taxNumber = taxNumber;
	}


	public BigDecimal getLimitThan() {
		return limitThan;
	}

	public void setLimitThan(BigDecimal limitThan) {
		this.limitThan = limitThan;
	}
}
