package com.faujor.entity.mdm;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteStru implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String mateId;
	private String quoteTempId;
	private String quoteUnit;
	private String remark;
	private Date createTime;
	private String creator;
	private String creatorName;
	private Date modifyTime;
	private String modifier;
	private String modifierName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMateId() {
		return mateId;
	}

	public void setMateId(String mateId) {
		this.mateId = mateId;
	}

	public String getQuoteTempId() {
		return quoteTempId;
	}

	public void setQuoteTempId(String quoteTempId) {
		this.quoteTempId = quoteTempId;
	}

	public String getQuoteUnit() {
		return quoteUnit;
	}

	public void setQuoteUnit(String quoteUnit) {
		this.quoteUnit = quoteUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
}
