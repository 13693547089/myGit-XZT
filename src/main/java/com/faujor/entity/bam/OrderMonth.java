package com.faujor.entity.bam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * BAM_ORDER_MONTH
 * @author 
 */
public class OrderMonth implements Serializable {
    private String id;

    private String orderNo;

    private String suppNo;

    private String mateCode;

    private BigDecimal undeliveredNum;

    private Date month;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSuppNo() {
        return suppNo;
    }

    public void setSuppNo(String suppNo) {
        this.suppNo = suppNo;
    }

    public String getMateCode() {
        return mateCode;
    }

    public void setMateCode(String mateCode) {
        this.mateCode = mateCode;
    }

    public BigDecimal getUndeliveredNum() {
        return undeliveredNum;
    }

    public void setUndeliveredNum(BigDecimal undeliveredNum) {
        this.undeliveredNum = undeliveredNum;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }
}