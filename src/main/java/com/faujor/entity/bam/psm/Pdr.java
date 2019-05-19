package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：Pdr
 * @tableName PS_PDR
 * @tableDesc 生产日报主表
 * @author Vincent
 * @date 2018-03-01
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pdr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer sn;
	private String pdrCode;	// 生产日报编码
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date produceDate;	// 生产日期
	private String suppName;	// 供应商
	private String suppCode;	// 供应商编码
	private String status;		// 状态
	private String crtDate;
	private String crtUser;
	private String uptDate;
	private String uptUser;
	private String remark;		// 备注
	
	private String syncFlag;  // 同步标志 1：已同步 0：未同步，查询为0标志的用于同步
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getPdrCode() {
		return pdrCode;
	}
	public void setPdrCode(String pdrCode) {
		this.pdrCode = pdrCode;
	}
	public Date getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}
	public String getSuppName() {
		return suppName;
	}
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	public String getSuppCode() {
		return suppCode;
	}
	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public String getCrtUser() {
		return crtUser;
	}
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	public String getUptDate() {
		return uptDate;
	}
	public void setUptDate(String uptDate) {
		this.uptDate = uptDate;
	}
	public String getUptUser() {
		return uptUser;
	}
	public void setUptUser(String uptUser) {
		this.uptUser = uptUser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
}
