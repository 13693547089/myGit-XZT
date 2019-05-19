package com.faujor.entity.cluster.mdm;

import java.io.Serializable;

public class MaterialUnitDO implements Serializable {
	private static final long serialVersionUID = -2540696571566218392L;
	private String id;// VARCHAR2(50) NOT NULL, --编码
	private String mateNumber;// VARCHAR2(20), --物料号
	private String mateId;// VARCHAR2(50), --物料编号
	private String unitTypeId;// VARCHAR2(50), --单位类型编号
	private String unitTypeName;// VARCHAR2(20), --单位类型名称
	private String unit;// VARCHAR2(50), --单位
	private String mole;// VARCHAR2(10), --转换分子
	private String deno;// VARCHAR2(10), --转换分母
	private Float mateLength;// NUMBER(13,3), --长（整箱，单位分米）
	private Float mateWidth;// NUMBER(13,3), --宽（整箱，单位分米）
	private Float mateHigh;// NUMBER(13,3), --高（整箱，单位分米）
	private Float mateBulk;// NUMBER(13,3), --体积（整箱，单位立方分米）
	private Float mateWeight;// NUMBER(13,3), --重量（整箱，单位立方分米）
	private String lwhUnit;// VARCHAR2(10), --长宽高单位
	private String weightUnit;// VARCHAR2(10), --重量单位
	private String bulkUnit;// VARCHAR2(10), --体重单位

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMateNumber() {
		return mateNumber;
	}

	public void setMateNumber(String mateNumber) {
		this.mateNumber = mateNumber;
	}

	public String getMateId() {
		return mateId;
	}

	public void setMateId(String mateId) {
		this.mateId = mateId;
	}

	public String getUnitTypeId() {
		return unitTypeId;
	}

	public void setUnitTypeId(String unitTypeId) {
		this.unitTypeId = unitTypeId;
	}

	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMole() {
		return mole;
	}

	public void setMole(String mole) {
		this.mole = mole;
	}

	public String getDeno() {
		return deno;
	}

	public void setDeno(String deno) {
		this.deno = deno;
	}

	public Float getMateLength() {
		return mateLength;
	}

	public void setMateLength(Float mateLength) {
		this.mateLength = mateLength;
	}

	public Float getMateWidth() {
		return mateWidth;
	}

	public void setMateWidth(Float mateWidth) {
		this.mateWidth = mateWidth;
	}

	public Float getMateHigh() {
		return mateHigh;
	}

	public void setMateHigh(Float mateHigh) {
		this.mateHigh = mateHigh;
	}

	public Float getMateBulk() {
		return mateBulk;
	}

	public void setMateBulk(Float mateBulk) {
		this.mateBulk = mateBulk;
	}

	public Float getMateWeight() {
		return mateWeight;
	}

	public void setMateWeight(Float mateWeight) {
		this.mateWeight = mateWeight;
	}

	public String getLwhUnit() {
		return lwhUnit;
	}

	public void setLwhUnit(String lwhUnit) {
		this.lwhUnit = lwhUnit;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getBulkUnit() {
		return bulkUnit;
	}

	public void setBulkUnit(String bulkUnit) {
		this.bulkUnit = bulkUnit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
