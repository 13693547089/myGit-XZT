package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * PS_CAP_REP
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CapRep implements Serializable {
    private String id;

    private String capCode;

    private String status;
    
    private String repMonth;

    private String suppNo;

    private String suppName;

    private String creater;

    private String createrName;

    private Date createTime;

    private String modifier;

    private String modifierName;

    private Date modifyTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCapCode() {
        return capCode;
    }

    public void setCapCode(String capCode) {
        this.capCode = capCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepMonth() {
        return repMonth;
    }

    public void setRepMonth(String repMonth) {
        this.repMonth = repMonth;
    }

    public String getSuppNo() {
        return suppNo;
    }

    public void setSuppNo(String suppNo) {
        this.suppNo = suppNo;
    }

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}