package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class OrderRele implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fid;
	private String contOrdeNumb;
	private String status;
	private Date subeDate;
	private String suppName;
	private String suppNumb;
	private String buyer;
	private String department;
	private String ortype;
	private String remarks;
	private String purcType;
	private String zzoem;
	private String enclLeng;
	private String releNumb;
	private String deliType;
	private String allo_status;
	private double mateVolum; // 体积
	private String purchOrg; // 采购组织
	private long publishId; // 发布人ID
	private String publishName;// 发布人名称

	private Double limitThan; // 下单限比
	private List<OrderMate> mates;

	// private List<OrderEnclosure> Enclosure;
	public double getMateVolum() {
		return mateVolum;
	}

	public void setMateVolum(double mateVolum) {
		this.mateVolum = mateVolum;
	}

	public String getAllo_status() {
		return allo_status;
	}

	public void setAllo_status(String allo_status) {
		this.allo_status = allo_status;
	}

	public String getReleNumb() {
		return releNumb;
	}

	public void setReleNumb(String releNumb) {
		this.releNumb = releNumb;
	}

	public String getDeliType() {
		return deliType;
	}

	public void setDeliType(String deliType) {
		this.deliType = deliType;
	}

	public String getEnclLeng() {
		return enclLeng;
	}

	public void setEnclLeng(String enclLeng) {
		this.enclLeng = enclLeng;
	}

	public String getZzoem() {
		return zzoem;
	}

	public void setZzoem(String zzoem) {
		this.zzoem = zzoem;
	}

	// public List<OrderEnclosure> getEnclosure() {
	// return Enclosure;
	// }
	//
	// public void setEnclosure(List<OrderEnclosure> enclosure) {
	// Enclosure = enclosure;
	// }

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

	public List<OrderMate> getMates() {
		return mates;
	}

	public void setMates(List<OrderMate> mates) {
		this.mates = mates;
	}

	public String getPurchOrg() {
		return purchOrg;
	}

	public void setPurchOrg(String purchOrg) {
		this.purchOrg = purchOrg;
	}

	public long getPublishId() {
		return publishId;
	}

	public void setPublishId(long publishId) {
		this.publishId = publishId;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	/**
	 * @return the limitThan
	 */
	public Double getLimitThan() {
		return limitThan;
	}

	/**
	 * @param limitThan the limitThan to set
	 */
	public void setLimitThan(Double limitThan) {
		this.limitThan = limitThan;
	}

	
	
}
