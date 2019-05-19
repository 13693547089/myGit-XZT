package com.faujor.entity.cluster.mdm;

import java.io.Serializable;

public class SuppPurcDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id; // 唯一标识
	private String suppId;// 合格供应商主表id
	private String sapId;// 供应商或债权人的帐号
	private String purcOrga;// 采购组织
	private String purcOrgaDesc;
	private String suppRange;// 子范围
	private String suppRangeDesc;
	private String payClause;// 付款条件
	private String payClauseDesc;
	private String currCode;// 货币码
	private String abcIden;// ABC标识
	private String suppGroup;// 供应商方案组

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPurcOrga() {
		return purcOrga;
	}

	public void setPurcOrga(String purcOrga) {
		this.purcOrga = purcOrga;
	}

	public String getPurcOrgaDesc() {
		return purcOrgaDesc;
	}

	public void setPurcOrgaDesc(String purcOrgaDesc) {
		this.purcOrgaDesc = purcOrgaDesc;
	}

	public String getSuppRange() {
		return suppRange;
	}

	public void setSuppRange(String suppRange) {
		this.suppRange = suppRange;
	}

	public String getSuppRangeDesc() {
		return suppRangeDesc;
	}

	public void setSuppRangeDesc(String suppRangeDesc) {
		this.suppRangeDesc = suppRangeDesc;
	}

	public String getPayClause() {
		return payClause;
	}

	public void setPayClause(String payClause) {
		this.payClause = payClause;
	}

	public String getPayClauseDesc() {
		return payClauseDesc;
	}

	public void setPayClauseDesc(String payClauseDesc) {
		this.payClauseDesc = payClauseDesc;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public String getAbcIden() {
		return abcIden;
	}

	public void setAbcIden(String abcIden) {
		this.abcIden = abcIden;
	}

	public String getSuppGroup() {
		return suppGroup;
	}

	public void setSuppGroup(String suppGroup) {
		this.suppGroup = suppGroup;
	}
}
