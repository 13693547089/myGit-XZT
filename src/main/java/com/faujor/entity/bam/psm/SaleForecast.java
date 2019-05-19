package com.faujor.entity.bam.psm;

import java.io.Serializable;

/**
 * 实体类：SaleForecast
 * @tableName PS_SALE_FORECAST
 * @tableDesc 销售预测表
 * @author Vincent
 * @date 2018-06-04
 *
 */
public class SaleForecast implements Serializable {
	// ID
    private String id;
    // 预测编码
    private String fsctCode;
    // 预测名称
    private String fsctName;
    // 预测年度
    private String fsctYear;
    // 状态
    private String status;
    // 备注
    private String remark;
    // 创建日期
    private String crtDate;
    // 创建用户
    private String crtUser;
    // 更新日期
    private String uptDate;
    // 更新用户
    private String uptUser;
    // 创建用户编码
    private String crtUserCode;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFsctCode() {
        return fsctCode;
    }

    public void setFsctCode(String fsctCode) {
        this.fsctCode = fsctCode;
    }

    public String getFsctName() {
        return fsctName;
    }

    public void setFsctName(String fsctName) {
        this.fsctName = fsctName;
    }

    public String getFsctYear() {
        return fsctYear;
    }

    public void setFsctYear(String fsctYear) {
        this.fsctYear = fsctYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public String getUptDate() {
        return uptDate;
    }

    public void setUptDate(String uptDate) {
        this.uptDate = uptDate;
    }

    public String getUptUser() {
        return uptUser;
    }

    public void setUptUser(String uptUser) {
        this.uptUser = uptUser;
    }

    public String getCrtUserCode() {
		return crtUserCode;
	}

	public void setCrtUserCode(String crtUserCode) {
		this.crtUserCode = crtUserCode;
	}
}