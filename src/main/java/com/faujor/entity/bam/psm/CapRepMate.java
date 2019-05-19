package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * PS_CAP_REP_MATE
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CapRepMate implements Serializable {
    private String id;

    private String mainId;

    private String mateCode;

    private String mateDesc;

    private String unitCode;

    private String unitName;

    private BigDecimal orderNum;

    private BigDecimal finishedNum;

    private BigDecimal unfinishedNum;

    private Date expectCompleteTime;

    private BigDecimal stockNum;

    private static final long serialVersionUID = 1L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}

	public String getMateDesc() {
		return mateDesc;
	}

	public void setMateDesc(String mateDesc) {
		this.mateDesc = mateDesc;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public BigDecimal getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(BigDecimal orderNum) {
		this.orderNum = orderNum;
	}

	public BigDecimal getFinishedNum() {
		return finishedNum;
	}

	public void setFinishedNum(BigDecimal finishedNum) {
		this.finishedNum = finishedNum;
	}

	public BigDecimal getUnfinishedNum() {
		return unfinishedNum;
	}

	public void setUnfinishedNum(BigDecimal unfinishedNum) {
		this.unfinishedNum = unfinishedNum;
	}

	public Date getExpectCompleteTime() {
		return expectCompleteTime;
	}

	public void setExpectCompleteTime(Date expectCompleteTime) {
		this.expectCompleteTime = expectCompleteTime;
	}

	public BigDecimal getStockNum() {
		return stockNum;
	}

	public void setStockNum(BigDecimal stockNum) {
		this.stockNum = stockNum;
	}

   
}