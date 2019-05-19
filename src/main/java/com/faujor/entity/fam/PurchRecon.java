package com.faujor.entity.fam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 采购对账单
 * 
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchRecon implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	// 对账单号
	private String reconCode;
	// 对账日期
	private Date createDate;
	// 对账状态编码
	private String reconStatus;
	// 对账单状态描述
	private String reconStatusDesc;
	// 供应商编码
	private String suppNo;
	// 供应商描述
	private String suppName;
	// 对账日期开始
	private Date startDate;
	// 对账日期结束
	private Date endDate;
	// 工厂编码
	private String plantCode;
	// 工厂描述
	private String plantDesc;
	// 含税金额
	private BigDecimal sumAmount;
	// 创建人
	private String createUser;
	// 创建时间
	private Date createTime;
	// 修改人
	private String modifyUser;
	// 修改时间
	private Date modifyTime;

	private String creater;

	private String modifier;

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(String reconStatus) {
		this.reconStatus = reconStatus;
	}

	public String getReconStatusDesc() {
		return reconStatusDesc;
	}

	public void setReconStatusDesc(String reconStatusDesc) {
		this.reconStatusDesc = reconStatusDesc;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getPlantDesc() {
		return plantDesc;
	}

	public void setPlantDesc(String plantDesc) {
		this.plantDesc = plantDesc;
	}

	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
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

}
