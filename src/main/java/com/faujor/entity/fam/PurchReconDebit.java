package com.faujor.entity.fam;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 采购订单扣款信息
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PurchReconDebit implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	//对账单号
	private String reconCode;
	//物料名称
	private String mateDesc;
	//物料编码
	private String mateCode;
	//未税金额
	private BigDecimal amount;
	//扣款原因
	private String debitReason;
	//备注
	private String remark;
	
	private String attFile;
	
	public String getAttFile() {
		return attFile;
	}
	public void setAttFile(String attFile) {
		this.attFile = attFile;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReconCode() {
		return reconCode;
	}
	public void setReconCode(String reconCode) {
		this.reconCode = reconCode;
	}
	public String getMateDesc() {
		return mateDesc;
	}
	public void setMateDesc(String mateDesc) {
		this.mateDesc = mateDesc;
	}
	public String getMateCode() {
		return mateCode;
	}
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDebitReason() {
		return debitReason;
	}
	public void setDebitReason(String debitReason) {
		this.debitReason = debitReason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
