package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class OrderPackForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;	//varcher2	50	编号
	private String oemOrderCode;	//	varcher2	100	关联OEM采购订单编号
	private String status;	//	varchar2	20	状态
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createTime;	//	date		创建时间
	private String creator;	//	varchar2	100	创建人编号
	private String create_name;	//	varchar2	200	创建人名称
	private String modifieId;	//	varcher2	50	修改人
	private String modifier;	//	date	100	修改人名称
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date udpateDate;	//	date		修改时间
	private String suppCode;	//	varcher2	100	OEM供应商编码
	private String suppName;	//	varcher2	100	OEM供应商名称
	private String purchOrg;	//	varcher2	50	销售组织
	private String limitThan;	//	varcher2	50	下单限比
	private String remark;	//	varcher2	4000	备注

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
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	 * @return the create_name
	 */
	public String getCreate_name() {
		return create_name;
	}
	/**
	 * @param create_name the create_name to set
	 */
	public void setCreate_name(String create_name) {
		this.create_name = create_name;
	}
	/**
	 * @return the modifieId
	 */
	public String getModifieId() {
		return modifieId;
	}
	/**
	 * @param modifieId the modifieId to set
	 */
	public void setModifieId(String modifieId) {
		this.modifieId = modifieId;
	}
	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}
	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	/**
	 * @return the udpateDate
	 */
	public Date getUdpateDate() {
		return udpateDate;
	}
	/**
	 * @param udpateDate the udpateDate to set
	 */
	public void setUdpateDate(Date udpateDate) {
		this.udpateDate = udpateDate;
	}


	public String getSuppCode() {
		return suppCode;
	}

	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getPurchOrg() {
		return purchOrg;
	}

	public void setPurchOrg(String purchOrg) {
		this.purchOrg = purchOrg;
	}

	public String getLimitThan() {
		return limitThan;
	}

	public void setLimitThan(String limitThan) {
		this.limitThan = limitThan;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
