package com.faujor.entity.bam.delivery;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliverySignDO implements Serializable {
	private static final long serialVersionUID = 5011764807310166358L;
	private String id; // 主键ID
	private String suppName;// 供应商名称
	private String deliCode;// 送货单编码
	private Date signTime; // 签到时间
	private String signStatus; // 签到状态
	private String remark; // 备注
	private Date createTime;// 创建时间
	private String creator; // 创建人
	private String creatorName; // 创建人名称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getDeliCode() {
		return deliCode;
	}

	public void setDeliCode(String deliCode) {
		this.deliCode = deliCode;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
}
