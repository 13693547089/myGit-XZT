package com.faujor.entity.cluster.mdm;

import java.io.Serializable;
import java.util.List;

public class SupplierDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String suppId;
	private String sapId;// sap编码
	private String srmId;// srm编码
	private String oaId;// oa编码
	private String category;// 供应商类型
	private String subGroup;// 科目组名称
	private String suppName;// 供应商全称
	private String suppAbbre;// 简称
	private String provCode;// 省
	private String provName;
	private String cityCode;// 地级市代码
	private String cityName; // 地级市
	private String address;// 地址
	private String holderPhone;// 电话
	private String faxNumber;// 传真
	private String email;// 电子邮件
	private String bankCode;// 银行编号
	private String accountHolder;// 开户人（含公司）
	private String bankAccount;// 银行账号
	private String bankName;// 银行
	private String busiLicense; // 营业执照号
	private String clientId;
	private String contacts;// 联系人
	private String phone;// 联系电话
	private String isDelete;// 删除标识
	private String compCode;// 公司代码
	private String akont;// 总帐中的统驭科目
	private String proGroup;// 计划组

	// 证照信息
	private String pmc12;
	private String pmc13;
	private String pmc14;
	private String pmc15;
	private String pmc16;
	private String pmc17;
	private String pmc18;
	private String pmc19;
	private String pmc20;
	private String pmc21;
	private String pmc22;
	private String pmc23;
	private String taPmc016;
	private String pmc24;
	private String pmc25;
	private String pmc26;
	private String pmc27;
	private String pmc28;
	private String pmc29;
	private String pmc30;
	private String pmc31;
	private String pmc32;
	private String pmc33;
	private String pmc34;
	private String pmc35;
	private String pmc36;
	private String pmc37;

	// 采购数据
	private String PMC43;
	private String PMC44;
	private String PMC45;
	private String PMC46;
	private String PMC47;

	// 新版采购数据
	private List<SuppPurcDO> suppPurcList;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getPmc12() {
		return pmc12;
	}

	public void setPmc12(String pmc12) {
		this.pmc12 = pmc12;
	}

	public String getPmc13() {
		return pmc13;
	}

	public void setPmc13(String pmc13) {
		this.pmc13 = pmc13;
	}

	public String getPmc14() {
		return pmc14;
	}

	public void setPmc14(String pmc14) {
		this.pmc14 = pmc14;
	}

	public String getPmc15() {
		return pmc15;
	}

	public void setPmc15(String pmc15) {
		this.pmc15 = pmc15;
	}

	public String getPmc16() {
		return pmc16;
	}

	public void setPmc16(String pmc16) {
		this.pmc16 = pmc16;
	}

	public String getPmc17() {
		return pmc17;
	}

	public void setPmc17(String pmc17) {
		this.pmc17 = pmc17;
	}

	public String getPmc18() {
		return pmc18;
	}

	public void setPmc18(String pmc18) {
		this.pmc18 = pmc18;
	}

	public String getPmc19() {
		return pmc19;
	}

	public void setPmc19(String pmc19) {
		this.pmc19 = pmc19;
	}

	public String getPmc20() {
		return pmc20;
	}

	public void setPmc20(String pmc20) {
		this.pmc20 = pmc20;
	}

	public String getPmc21() {
		return pmc21;
	}

	public void setPmc21(String pmc21) {
		this.pmc21 = pmc21;
	}

	public String getPmc22() {
		return pmc22;
	}

	public void setPmc22(String pmc22) {
		this.pmc22 = pmc22;
	}

	public String getPmc23() {
		return pmc23;
	}

	public void setPmc23(String pmc23) {
		this.pmc23 = pmc23;
	}

	public String getTaPmc016() {
		return taPmc016;
	}

	public void setTaPmc016(String taPmc016) {
		this.taPmc016 = taPmc016;
	}

	public String getPmc24() {
		return pmc24;
	}

	public void setPmc24(String pmc24) {
		this.pmc24 = pmc24;
	}

	public String getPmc25() {
		return pmc25;
	}

	public void setPmc25(String pmc25) {
		this.pmc25 = pmc25;
	}

	public String getPmc26() {
		return pmc26;
	}

	public void setPmc26(String pmc26) {
		this.pmc26 = pmc26;
	}

	public String getPmc27() {
		return pmc27;
	}

	public void setPmc27(String pmc27) {
		this.pmc27 = pmc27;
	}

	public String getPmc28() {
		return pmc28;
	}

	public void setPmc28(String pmc28) {
		this.pmc28 = pmc28;
	}

	public String getPmc29() {
		return pmc29;
	}

	public void setPmc29(String pmc29) {
		this.pmc29 = pmc29;
	}

	public String getPmc30() {
		return pmc30;
	}

	public void setPmc30(String pmc30) {
		this.pmc30 = pmc30;
	}

	public String getPmc31() {
		return pmc31;
	}

	public void setPmc31(String pmc31) {
		this.pmc31 = pmc31;
	}

	public String getPmc32() {
		return pmc32;
	}

	public void setPmc32(String pmc32) {
		this.pmc32 = pmc32;
	}

	public String getPmc33() {
		return pmc33;
	}

	public void setPmc33(String pmc33) {
		this.pmc33 = pmc33;
	}

	public String getPmc34() {
		return pmc34;
	}

	public void setPmc34(String pmc34) {
		this.pmc34 = pmc34;
	}

	public String getPmc35() {
		return pmc35;
	}

	public void setPmc35(String pmc35) {
		this.pmc35 = pmc35;
	}

	public String getPmc36() {
		return pmc36;
	}

	public void setPmc36(String pmc36) {
		this.pmc36 = pmc36;
	}

	public String getPmc37() {
		return pmc37;
	}

	public void setPmc37(String pmc37) {
		this.pmc37 = pmc37;
	}

	public String getPMC43() {
		return PMC43;
	}

	public void setPMC43(String pMC43) {
		PMC43 = pMC43;
	}

	public String getPMC44() {
		return PMC44;
	}

	public void setPMC44(String pMC44) {
		PMC44 = pMC44;
	}

	public String getPMC45() {
		return PMC45;
	}

	public void setPMC45(String pMC45) {
		PMC45 = pMC45;
	}

	public String getPMC46() {
		return PMC46;
	}

	public void setPMC46(String pMC46) {
		PMC46 = pMC46;
	}

	public String getPMC47() {
		return PMC47;
	}

	public void setPMC47(String pMC47) {
		PMC47 = pMC47;
	}

	public List<SuppPurcDO> getSuppPurcList() {
		return suppPurcList;
	}

	public void setSuppPurcList(List<SuppPurcDO> suppPurcList) {
		this.suppPurcList = suppPurcList;
	}
}
