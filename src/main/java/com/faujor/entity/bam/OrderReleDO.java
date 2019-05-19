package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderReleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fid;
	private String contOrdeNumb;
	private String status;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date subeDate;
	private String suppName;
	private String suppNumb;
	private String buyer;
	private String department;
	private String ortype;
	private String remarks;
	private String purcType;
	private String zzoem;
	private String deliType;
	private String purchOrg;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private String publishName;

	/**
	 * @return the zzoem
	 */
	public String getZzoem() {
		return zzoem;
	}

	public void setZzoem(String zzoem) {
		this.zzoem = zzoem;
	}

	public String getDeliType() {
		return deliType;
	}

	public void setDeliType(String deliType) {
		this.deliType = deliType;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getContOrdeNumb() {
		return contOrdeNumb;
	}

	public void setContOrdeNumb(String contOrdeNumb) {
		this.contOrdeNumb = contOrdeNumb;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSubeDate() {
		return subeDate;
	}

	public void setSubeDate(Date subeDate) {
		this.subeDate = subeDate;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getSuppNumb() {
		return suppNumb;
	}

	public void setSuppNumb(String suppNumb) {
		this.suppNumb = suppNumb;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getOrtype() {
		return ortype;
	}

	public void setOrtype(String ortype) {
		this.ortype = ortype;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setPurcType(String purcType) {
		this.purcType = purcType;
	}

	public String getPurcType() {
		return purcType;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPurchOrg() {
		return purchOrg;
	}

	public void setPurchOrg(String purchOrg) {
		this.purchOrg = purchOrg;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
}
