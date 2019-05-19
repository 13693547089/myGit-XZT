package com.faujor.entity.fam;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 采购地订单发票信息
 * 
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchReconInvoce implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	// 发票号
	private String invoceNo;
	// 总金额
	private BigDecimal totalMoney;
	// 税金
	private BigDecimal taxMoney;
	
	private BigDecimal noTaxMoney;

	// 附件信息
	private String attFile;
	// 对账单号
	private String reconCode;

	public String getReconCode() {
		return reconCode;
	}

	public void setReconCode(String reconCode) {
		this.reconCode = reconCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoceNo() {
		return invoceNo;
	}

	public void setInvoceNo(String invoceNo) {
		this.invoceNo = invoceNo;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public BigDecimal getTaxMoney() {
		return taxMoney;
	}

	public void setTaxMoney(BigDecimal taxMoney) {
		this.taxMoney = taxMoney;
	}

	public String getAttFile() {
		return attFile;
	}

	public void setAttFile(String attFile) {
		this.attFile = attFile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getNoTaxMoney() {
		return noTaxMoney;
	}

	public void setNoTaxMoney(BigDecimal noTaxMoney) {
		this.noTaxMoney = noTaxMoney;
	}
	
}
