package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

public class OrderMate1 implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fid;
	private String mainId;
	private String frequency;
	private String mateNumb;
	private String prodName;
	private Integer boxEntrNumb;
	private Double purcQuan;
	private Double quanMate;
	private Double unpaQuan;
	private String company;
	private String unitPric;
	private String NETWR;
	private Date deliDate;
	private String taxAmou;
	private Double menge;
	private Double zwmenge;
	private String elikz;
	private Double mateTax;
	private Integer peinh;
	private String werks; // 工厂
	private String lgort; // 库位
	private double mateVolum;
	private String suppRange; // 供应商子范围编码
	private String suppRangeDesc; // 供应商子范围描述

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public Integer getPeinh() {
		return peinh;
	}

	public void setPeinh(Integer peinh) {
		this.peinh = peinh;
	}

	public Double getMateTax() {
		return mateTax;
	}

	public void setMateTax(Double mateTax) {
		this.mateTax = mateTax;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getMateNumb() {
		return mateNumb;
	}

	public void setMateNumb(String mateNumb) {
		this.mateNumb = mateNumb;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public Integer getBoxEntrNumb() {
		return boxEntrNumb;
	}

	public void setBoxEntrNumb(Integer boxEntrNumb) {
		this.boxEntrNumb = boxEntrNumb;
	}

	public Double getPurcQuan() {
		return purcQuan;
	}

	public void setPurcQuan(Double purcQuan) {
		this.purcQuan = purcQuan;
	}

	public Double getQuanMate() {
		return quanMate;
	}

	public void setQuanMate(Double quanMate) {
		this.quanMate = quanMate;
	}

	public Double getUnpaQuan() {
		return unpaQuan;
	}

	public void setUnpaQuan(Double unpaQuan) {
		this.unpaQuan = unpaQuan;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getUnitPric() {
		return unitPric;
	}

	public void setUnitPric(String unitPric) {
		this.unitPric = unitPric;
	}

	public String getNETWR() {
		return NETWR;
	}

	public void setNETWR(String nETWR) {
		NETWR = nETWR;
	}

	public Date getDeliDate() {
		return deliDate;
	}

	public void setDeliDate(Date deliDate) {
		this.deliDate = deliDate;
	}

	public String getTaxAmou() {
		return taxAmou;
	}

	public void setTaxAmou(String taxAmou) {
		this.taxAmou = taxAmou;
	}

	public Double getMenge() {
		return menge;
	}

	public void setMenge(Double menge) {
		this.menge = menge;
	}

	public Double getZwmenge() {
		return zwmenge;
	}

	public void setZwmenge(Double zwmenge) {
		this.zwmenge = zwmenge;
	}

	public String getElikz() {
		return elikz;
	}

	public void setElikz(String elikz) {
		this.elikz = elikz;
	}

	public double getMateVolum() {
		return mateVolum;
	}

	public void setMateVolum(double mateVolum) {
		this.mateVolum = mateVolum;
	}

	/**
	 * @return the suppRange
	 */
	public String getSuppRange() {
		return suppRange;
	}

	/**
	 * @param suppRange the suppRange to set
	 */
	public void setSuppRange(String suppRange) {
		this.suppRange = suppRange;
	}

	/**
	 * @return the suppRangeDesc
	 */
	public String getSuppRangeDesc() {
		return suppRangeDesc;
	}

	/**
	 * @param suppRangeDesc the suppRangeDesc to set
	 */
	public void setSuppRangeDesc(String suppRangeDesc) {
		this.suppRangeDesc = suppRangeDesc;
	}
	
}
