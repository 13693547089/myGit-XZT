package com.faujor.entity.bam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
public class BaoCaiInfo implements Serializable {

	private String fid;
	private String frequency;
	private String mateNumb;
	private String prodName;
	private String segmName;
	private String asseCode;
	private String asseName;
	private String mateCode;
	private BigDecimal detailsNum;
	private BigDecimal sumQuan;
	private BigDecimal orderedNum;
	private BigDecimal noOrderNum;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
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

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}

	public String getAsseName() {
		return asseName;
	}

	public void setAsseName(String asseName) {
		this.asseName = asseName;
	}

	public BigDecimal getDetailsNum() {
		return detailsNum;
	}

	public void setDetailsNum(BigDecimal detailsNum) {
		this.detailsNum = detailsNum;
	}

	public BigDecimal getSumQuan() {
		return sumQuan;
	}

	public void setSumQuan(BigDecimal sumQuan) {
		this.sumQuan = sumQuan;
	}

	public String getSegmName() {
		return segmName;
	}

	public void setSegmName(String segmName) {
		this.segmName = segmName;
	}

	public String getAsseCode() {
		return asseCode;
	}

	public void setAsseCode(String asseCode) {
		this.asseCode = asseCode;
	}

	public BigDecimal getOrderedNum() {
		return orderedNum;
	}

	public void setOrderedNum(BigDecimal orderedNum) {
		this.orderedNum = orderedNum;
	}

	public BigDecimal getNoOrderNum() {
		return noOrderNum;
	}

	public void setNoOrderNum(BigDecimal noOrderNum) {
		this.noOrderNum = noOrderNum;
	}
}
