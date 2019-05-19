package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * PS_CAP_REP_ORDER
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CapRepOrder implements Serializable {
    private String id;

    private String mainId;

    private String mateCode;

    private String mateDesc;

    private String version;

    private String batchNo;

    private BigDecimal orderNum;

    private BigDecimal finishedNum;

    private String orderNo;

    private String orderSide;

    private Date expectCompleteTime;

    private String orderAttach;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(String orderSide) {
        this.orderSide = orderSide;
    }

    public Date getExpectCompleteTime() {
        return expectCompleteTime;
    }

    public void setExpectCompleteTime(Date expectCompleteTime) {
        this.expectCompleteTime = expectCompleteTime;
    }

    public String getOrderAttach() {
        return orderAttach;
    }

    public void setOrderAttach(String orderAttach) {
        this.orderAttach = orderAttach;
    }
}