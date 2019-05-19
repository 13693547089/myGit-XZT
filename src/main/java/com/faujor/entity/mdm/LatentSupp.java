package com.faujor.entity.mdm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class LatentSupp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5794209297304567954L;
	/**
	 * 潜在供应商
	 */
	private String suppId; //VARCHAR2(50) NOT NULL,--供应商编码
	private String sapId; //VARCHAR2(50),          --sap编码
	private String srmId; //VARCHAR2(50),           --srm编码
	private String oaId; //VARCHAR2(50),           --oa编码
	private String categoryId; //VARCHAR2(50),     --供应商类型编码
	private String category; //VARCHAR2(30),        --供应商类型
	private String subGroup; //VARCHAR2(30),       --科目组名称
	private String suppName; //VARCHAR2(50),       --供应商名称
	private String suppAbbre;//VARCHAR2(20),      --供应商简称
	private String provCode; //VARCHAR2(10),       --省码
	private String provName ;//VARCHAR2(20),       --省名称
	private String  cityCode ;//VARCHAR2(40),       --市码
	private String cityName; //VARCHAR2(40),       --市名称
	private String postcode; //VARCHAR2(50),        --邮编
	private String address; //VARCHAR2(50),         --地址
	private String contacts; //VARCHAR2(20),        --联系人
	private String phone; //VARCHAR2(20),           --联系电话
	private String bankCode; //VARCHAR2(20),       --银行编号
	private String bankAbbre ;//varchar2(20),      --银行简称
	private String bankName;//VARCHAR2(60),       --银行名称
	private String bankAccount ;//VARCHAR2(60),    --银行账号
	private String accountHolder; //VARCHAR2(50),  --账户持有人
	private String holderPhone; //VARCHAR2(20),    --户主电话
	private String faxNumber; //VARCHAR2(50),      --户主传真号
	private String email; //VARCHAR2(50),           --邮箱
	private String payClauseId; //VARCHAR2(50),   --付款条件编号
	private String currencyId; //VARCHAR2(50),     --惯用币种编号
	private String taxeKindId; //VARCHAR2(50),    --惯用税种编号
	private String remark; //VARCHAR2(300),         --备注说明
	private String busiLicense; //VARCHAR2(50),    --责任税务部门的税收编号 （营业执照）
	private String clientId; //VARCHAR2(50),       --客户编号
	private String isDelete; //VARCHAR2(10),       --删除标识
	private String compCode; //VARCHAR2(20),       --公司代码
	private String akont; //VARCHAR2(20),           --总帐中的统驭科目
	private String proGroup ;//VARCHAR2(20),       --计划组
	private String status ;//VARCHAR2(10),          --供应商的状态
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date registerTime; //DATE,           --供应商登记时间
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date auditTime; //DATE,              --OA审核时间
	private String auditStatus; //VARCHAR2(10),    --OA审核状态
	private Integer firstTrialId;                     //--采购员编号
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date buyerAuditTime;                 //--采购员审核时间
	private Integer miniId;                //--采购部长的编号
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date  subOATime;                    //--采购部长提交OA的时间
	private String suppInfo;   //存储供应商编码或供应商名称
	
	public LatentSupp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LatentSupp(String suppId, String sapId, String srmId, String oaId, String categoryId, String category,
			String subGroup, String suppName, String suppAbbre, String provCode, String provName, String cityCode,
			String cityName, String postcode, String address, String contacts, String phone, String bankCode,
			String bankAbbre, String bankName, String bankAccount, String accountHolder, String holderPhone,
			String faxNumber, String email, String payClauseId, String currencyId, String taxeKindId, String remark,
			String busiLicense, String clientId, String isDelete, String compCode, String akont, String proGroup,
			String status, Date registerTime, Date auditTime, String auditStatus, Integer firstTrialId, Date buyerAuditTime,
			Integer miniId, Date subOATime, String suppInfo) {
		super();
		this.suppId = suppId;
		this.sapId = sapId;
		this.srmId = srmId;
		this.oaId = oaId;
		this.categoryId = categoryId;
		this.category = category;
		this.subGroup = subGroup;
		this.suppName = suppName;
		this.suppAbbre = suppAbbre;
		this.provCode = provCode;
		this.provName = provName;
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.postcode = postcode;
		this.address = address;
		this.contacts = contacts;
		this.phone = phone;
		this.bankCode = bankCode;
		this.bankAbbre = bankAbbre;
		this.bankName = bankName;
		this.bankAccount = bankAccount;
		this.accountHolder = accountHolder;
		this.holderPhone = holderPhone;
		this.faxNumber = faxNumber;
		this.email = email;
		this.payClauseId = payClauseId;
		this.currencyId = currencyId;
		this.taxeKindId = taxeKindId;
		this.remark = remark;
		this.busiLicense = busiLicense;
		this.clientId = clientId;
		this.isDelete = isDelete;
		this.compCode = compCode;
		this.akont = akont;
		this.proGroup = proGroup;
		this.status = status;
		this.registerTime = registerTime;
		this.auditTime = auditTime;
		this.auditStatus = auditStatus;
		this.firstTrialId = firstTrialId;
		this.buyerAuditTime = buyerAuditTime;
		this.miniId = miniId;
		this.subOATime = subOATime;
		this.suppInfo = suppInfo;
	}

	public String getSuppId() {
		return suppId;
	}

	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public String getSrmId() {
		return srmId;
	}

	public void setSrmId(String srmId) {
		this.srmId = srmId;
	}

	public String getOaId() {
		return oaId;
	}

	public void setOaId(String oaId) {
		this.oaId = oaId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getSuppAbbre() {
		return suppAbbre;
	}

	public void setSuppAbbre(String suppAbbre) {
		this.suppAbbre = suppAbbre;
	}

	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAbbre() {
		return bankAbbre;
	}

	public void setBankAbbre(String bankAbbre) {
		this.bankAbbre = bankAbbre;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getHolderPhone() {
		return holderPhone;
	}

	public void setHolderPhone(String holderPhone) {
		this.holderPhone = holderPhone;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPayClauseId() {
		return payClauseId;
	}

	public void setPayClauseId(String payClauseId) {
		this.payClauseId = payClauseId;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getTaxeKindId() {
		return taxeKindId;
	}

	public void setTaxeKindId(String taxeKindId) {
		this.taxeKindId = taxeKindId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBusiLicense() {
		return busiLicense;
	}

	public void setBusiLicense(String busiLicense) {
		this.busiLicense = busiLicense;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getAkont() {
		return akont;
	}

	public void setAkont(String akont) {
		this.akont = akont;
	}

	public String getProGroup() {
		return proGroup;
	}

	public void setProGroup(String proGroup) {
		this.proGroup = proGroup;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getSuppInfo() {
		return suppInfo;
	}



	public void setSuppInfo(String suppInfo) {
		this.suppInfo = suppInfo;
	}

	

	public Integer getFirstTrialId() {
		return firstTrialId;
	}

	public void setFirstTrialId(Integer firstTrialId) {
		this.firstTrialId = firstTrialId;
	}

	

	public Integer getMiniId() {
		return miniId;
	}

	public void setMiniId(Integer miniId) {
		this.miniId = miniId;
	}

	public Date getBuyerAuditTime() {
		return buyerAuditTime;
	}

	public void setBuyerAuditTime(Date buyerAuditTime) {
		this.buyerAuditTime = buyerAuditTime;
	}


	public Date getSubOATime() {
		return subOATime;
	}

	public void setSubOATime(Date subOATime) {
		this.subOATime = subOATime;
	}

	@Override
	public String toString() {
		return "LatentSupp [suppId=" + suppId + ", sapId=" + sapId + ", srmId=" + srmId + ", oaId=" + oaId
				+ ", categoryId=" + categoryId + ", category=" + category + ", subGroup=" + subGroup + ", suppName="
				+ suppName + ", suppAbbre=" + suppAbbre + ", provCode=" + provCode + ", provName=" + provName
				+ ", cityCode=" + cityCode + ", cityName=" + cityName + ", postcode=" + postcode + ", address="
				+ address + ", contacts=" + contacts + ", phone=" + phone + ", bankCode=" + bankCode + ", bankAbbre="
				+ bankAbbre + ", bankName=" + bankName + ", bankAccount=" + bankAccount + ", accountHolder="
				+ accountHolder + ", holderPhone=" + holderPhone + ", faxNumber=" + faxNumber + ", email=" + email
				+ ", payClauseId=" + payClauseId + ", currencyId=" + currencyId + ", taxeKindId=" + taxeKindId
				+ ", remark=" + remark + ", busiLicense=" + busiLicense + ", clientId=" + clientId + ", isDelete="
				+ isDelete + ", compCode=" + compCode + ", akont=" + akont + ", proGroup=" + proGroup + ", status="
				+ status + ", registerTime=" + registerTime + ", auditTime=" + auditTime + ", auditStatus="
				+ auditStatus + ", firstTrialId=" + firstTrialId + ", buyerAuditTime=" + buyerAuditTime + ", miniId="
				+ miniId + ", subOATime=" + subOATime + ", suppInfo=" + suppInfo + "]";
	}


}
