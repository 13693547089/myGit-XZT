package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 报价单主表
 * @author hql
 *
 */
public class Quote implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	主键
	private String id;
//	报价单编号
	private String quoteCode;
//	报价日期
	private Date quoteDate;
//	报价单状态
	private String status;
//	供应商编码
	private String suppNo;
//	供应商名称
	private String suppName;
//	报价单类型
	private String quoteType;
//	报价单类型描述
	private String quoteTypeDesc;
//	报价单依据
	private String quoteBase;
	private String quoteBaseTwo;
//	依据的名称
	private String quoteBaseName;
	private String quoteBaseTwoName;
	
//	备注
	private String remark;
//	创建人
	private String createUser;
//	创建时间
	private Date createTime;
//	修改人
	private String modifyUser;
//	修改时间
	private Date modifyTime;
	//创建人 修改人名称
	private String creater;
	private String modifier;
	//初审核人 初审核时间
	private String firstAuditor;
	private Date firstAuditDate;
	//oa审核通过的时间
	private Date oaAuditDate;
	//创建人类型（供应商、采购员）
	private String userType;
	
	//有效起止日期
	private Date validStart;
	private Date validEnd;
	
	
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	private List<QuoteMate> mateList;
	
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
	public Date getQuoteDate() {
		return quoteDate;
	}
	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getQuoteType() {
		return quoteType;
	}
	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}
	public String getQuoteTypeDesc() {
		return quoteTypeDesc;
	}
	public void setQuoteTypeDesc(String quoteTypeDesc) {
		this.quoteTypeDesc = quoteTypeDesc;
	}
	public String getQuoteBase() {
		return quoteBase;
	}
	public void setQuoteBase(String quoteBase) {
		this.quoteBase = quoteBase;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public List<QuoteMate> getMateList() {
		return mateList;
	}
	public void setMateList(List<QuoteMate> mateList) {
		this.mateList = mateList;
	}
	public String getQuoteBaseName() {
		return quoteBaseName;
	}
	public void setQuoteBaseName(String quoteBaseName) {
		this.quoteBaseName = quoteBaseName;
	}
	public String getFirstAuditor() {
		return firstAuditor;
	}
	public void setFirstAuditor(String firstAuditor) {
		this.firstAuditor = firstAuditor;
	}
	public Date getFirstAuditDate() {
		return firstAuditDate;
	}
	public void setFirstAuditDate(Date firstAuditDate) {
		this.firstAuditDate = firstAuditDate;
	}
	public Date getOaAuditDate() {
		return oaAuditDate;
	}
	public void setOaAuditDate(Date oaAuditDate) {
		this.oaAuditDate = oaAuditDate;
	}
	public Date getValidStart() {
		return validStart;
	}
	public void setValidStart(Date validStart) {
		this.validStart = validStart;
	}
	public Date getValidEnd() {
		return validEnd;
	}
	public void setValidEnd(Date validEnd) {
		this.validEnd = validEnd;
	}
	/**
	 * @return the quoteBaseTwo
	 */
	public String getQuoteBaseTwo() {
		return quoteBaseTwo;
	}
	/**
	 * @param quoteBaseTwo the quoteBaseTwo to set
	 */
	public void setQuoteBaseTwo(String quoteBaseTwo) {
		this.quoteBaseTwo = quoteBaseTwo;
	}
	/**
	 * @return the quoteBaseTwoName
	 */
	public String getQuoteBaseTwoName() {
		return quoteBaseTwoName;
	}
	/**
	 * @param quoteBaseTwoName the quoteBaseTwoName to set
	 */
	public void setQuoteBaseTwoName(String quoteBaseTwoName) {
		this.quoteBaseTwoName = quoteBaseTwoName;
	}
	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}
