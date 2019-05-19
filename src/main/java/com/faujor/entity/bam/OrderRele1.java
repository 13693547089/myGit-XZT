package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class OrderRele1 implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fid;
	private String contOrdeNumb; // 采购订单编号
	private String status;
	private Date subeDate;
	private String suppName;
	private String lifnr;// 存储供应商编码 Z001,Z009
	private String reswk;// 存储供应商编码 Z002,Z006,Z008
	private String suppNumb;
	private String buyer;
	private String department;
	private String BSART; // 类型
	private String remarks;
	private String purcType;
	private String zzoem; // oem供应商
	private String releNumb;
	private String deliType;
	private Double mateVolum;// 调拨体积
	private String purchOrg; // 采购组织
	private List<OrderMate> mates;
	private List<OrderEnclosure> Enclosure;

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

	public String getZzoem() {
		return zzoem;
	}

	public void setZzoem(String zzoem) {
		this.zzoem = zzoem;
	}

	public List<OrderEnclosure> getEnclosure() {
		return Enclosure;
	}

	public String getLifnr() {
		return lifnr;
	}

	public void setLifnr(String lifnr) {
		this.lifnr = lifnr;
	}

	public String getReswk() {
		return reswk;
	}

	public void setReswk(String reswk) {
		this.reswk = reswk;
	}

	public void setEnclosure(List<OrderEnclosure> enclosure) {
		Enclosure = enclosure;
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

	public String getBSART() {
		return BSART;
	}

	public void setBSART(String BSART) {
		this.BSART = BSART;
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

	public Double getMateVolum() {
		return mateVolum;
	}

	public void setMateVolum(Double mateVolum) {
		this.mateVolum = mateVolum;
	}

	public String getPurchOrg() {
		return purchOrg;
	}

	public void setPurchOrg(String purchOrg) {
		this.purchOrg = purchOrg;
	}

}
