package com.faujor.entity.mdm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MateBasicInfo implements Serializable {
	private static final long serialVersionUID = -5885652251884684143L;

	private String id; // 主键id
	private String mateId; // 物料表的主键
	private String basicName;// 名称
	private String standard;// 规格
	private String unit;// 单位

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMateId() {
		return mateId;
	}

	public void setMateId(String mateId) {
		this.mateId = mateId;
	}

	public String getBasicName() {
		return basicName;
	}

	public void setBasicName(String basicName) {
		this.basicName = basicName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
