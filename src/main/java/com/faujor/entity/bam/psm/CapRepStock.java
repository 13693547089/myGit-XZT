package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * PS_CAP_REP_STOCK
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CapRepStock implements Serializable {
    private String id;

    private String mainId;

    private String mateCode;

    private String mateDesc;

    private String version;

    private String batchNo;

    private String orderSide;

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

    public String getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(String orderSide) {
        this.orderSide = orderSide;
    }

    public BigDecimal getStockNum() {
        return stockNum;
    }

    public void setStockNum(BigDecimal stockNum) {
        this.stockNum = stockNum;
    }

  
}