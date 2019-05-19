package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class OrderMate implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// private String
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
	private String mone;
	private Date deliDate;
	private String taxAmou;
	private Double mateTex;
	private Integer multRate;
	private String factoryAddr;
	private String librPosi;
	private double mateVolum;
	private String asseCode;
	private String asseName;
	private String suppRange;//供应商子范围编码
	private String suppRangeDesc;//供应商子范围描述

	public String getFactoryAddr() {
		return factoryAddr;
	}

	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}

	public String getLibrPosi() {
		return librPosi;
	}

	public void setLibrPosi(String librPosi) {
		this.librPosi = librPosi;
	}

	public Integer getMultRate() {
		return multRate;
	}

	public void setMultRate(Integer multRate) {
		this.multRate = multRate;
	}

	public Double getMateTex() {
		return mateTex;
	}

	public void setMateTex(Double mateTex) {
		this.mateTex = mateTex;
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

	public String getMone() {
		return mone;
	}

	public void setMone(String mone) {
		this.mone = mone;
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

	public double getMateVolum() {
		return mateVolum;
	}

	public void setMateVolum(double mateVolum) {
		this.mateVolum = mateVolum;
	}

	public String getAsseCode() {
		return asseCode;
	}

	public void setAsseCode(String asseCode) {
		this.asseCode = asseCode;
	}

	public String getAsseName() {
		return asseName;
	}

	public void setAsseName(String asseName) {
		this.asseName = asseName;
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
