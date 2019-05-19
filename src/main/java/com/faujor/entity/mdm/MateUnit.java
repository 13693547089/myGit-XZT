package com.faujor.entity.mdm;

import java.io.Serializable;

public class MateUnit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id ;//VARCHAR2(50) NOT NULL,      --编码
	private String mateNumber;// VARCHAR2(20),      --物料号
	private String mateId ;//VARCHAR2(50),          --物料编号
	private String unitTypeId;// VARCHAR2(50),     --单位类型编号
	private String unitTypeName;// VARCHAR2(20),   --单位类型名称
	private String unit ;//VARCHAR2(50),             --单位
	private String mole ;//VARCHAR2(10),             --转换分子
	private String deno;// VARCHAR2(10),             --转换分母
	private Float mateLength;// NUMBER(13,3),           --长
	private Float mateWidth;// NUMBER(13,3),            --宽
	private Float mateHigh;// NUMBER(13,3),             --高
	private String lwhUnit;// VARCHAR2(10),         --长宽高单位
	private Float mateWeight ;//NUMBER(13,3),           --重量
	private String weightUnit;// VARCHAR2(10),      --重量单位
	private Float mateBulk ;//NUMBER(13,3),             --体积
	private String bulkUnit;// VARCHAR2(10),        --体重单位
	public MateUnit() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MateUnit(String id, String mateNumber, String mateId, String unitTypeId, String unitTypeName, String unit,
			String mole, String deno, Float mateLength, Float mateWidth, Float mateHigh, String lwhUnit,
			Float mateWeight, String weightUnit, Float mateBulk, String bulkUnit) {
		super();
		this.id = id;
		this.mateNumber = mateNumber;
		this.mateId = mateId;
		this.unitTypeId = unitTypeId;
		this.unitTypeName = unitTypeName;
		this.unit = unit;
		this.mole = mole;
		this.deno = deno;
		this.mateLength = mateLength;
		this.mateWidth = mateWidth;
		this.mateHigh = mateHigh;
		this.lwhUnit = lwhUnit;
		this.mateWeight = mateWeight;
		this.weightUnit = weightUnit;
		this.mateBulk = mateBulk;
		this.bulkUnit = bulkUnit;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the mateNumber
	 */
	public String getMateNumber() {
		return mateNumber;
	}
	/**
	 * @param mateNumber the mateNumber to set
	 */
	public void setMateNumber(String mateNumber) {
		this.mateNumber = mateNumber;
	}
	/**
	 * @return the mateId
	 */
	public String getMateId() {
		return mateId;
	}
	/**
	 * @param mateId the mateId to set
	 */
	public void setMateId(String mateId) {
		this.mateId = mateId;
	}
	/**
	 * @return the unitTypeId
	 */
	public String getUnitTypeId() {
		return unitTypeId;
	}
	/**
	 * @param unitTypeId the unitTypeId to set
	 */
	public void setUnitTypeId(String unitTypeId) {
		this.unitTypeId = unitTypeId;
	}
	/**
	 * @return the unitTypeName
	 */
	public String getUnitTypeName() {
		return unitTypeName;
	}
	/**
	 * @param unitTypeName the unitTypeName to set
	 */
	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the mole
	 */
	public String getMole() {
		return mole;
	}
	/**
	 * @param mole the mole to set
	 */
	public void setMole(String mole) {
		this.mole = mole;
	}
	/**
	 * @return the deno
	 */
	public String getDeno() {
		return deno;
	}
	/**
	 * @param deno the deno to set
	 */
	public void setDeno(String deno) {
		this.deno = deno;
	}
	
	/**
	 * @return the mateLength
	 */
	public Float getMateLength() {
		return mateLength;
	}
	/**
	 * @param mateLength the mateLength to set
	 */
	public void setMateLength(Float mateLength) {
		this.mateLength = mateLength;
	}
	/**
	 * @return the mateWidth
	 */
	public Float getMateWidth() {
		return mateWidth;
	}
	/**
	 * @param mateWidth the mateWidth to set
	 */
	public void setMateWidth(Float mateWidth) {
		this.mateWidth = mateWidth;
	}
	/**
	 * @return the mateHigh
	 */
	public Float getMateHigh() {
		return mateHigh;
	}
	/**
	 * @param mateHigh the mateHigh to set
	 */
	public void setMateHigh(Float mateHigh) {
		this.mateHigh = mateHigh;
	}
	/**
	 * @return the lwhUnit
	 */
	public String getLwhUnit() {
		return lwhUnit;
	}
	/**
	 * @param lwhUnit the lwhUnit to set
	 */
	public void setLwhUnit(String lwhUnit) {
		this.lwhUnit = lwhUnit;
	}
	
	/**
	 * @return the mateWeight
	 */
	public Float getMateWeight() {
		return mateWeight;
	}

	/**
	 * @param mateWeight the mateWeight to set
	 */
	public void setMateWeight(Float mateWeight) {
		this.mateWeight = mateWeight;
	}

	/**
	 * @return the weightUnit
	 */
	public String getWeightUnit() {
		return weightUnit;
	}
	/**
	 * @param weightUnit the weightUnit to set
	 */
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	
	/**
	 * @return the mateBulk
	 */
	public Float getMateBulk() {
		return mateBulk;
	}
	/**
	 * @param mateBulk the mateBulk to set
	 */
	public void setMateBulk(Float mateBulk) {
		this.mateBulk = mateBulk;
	}
	/**
	 * @return the bulkUnit
	 */
	public String getBulkUnit() {
		return bulkUnit;
	}
	/**
	 * @param bulkUnit the bulkUnit to set
	 */
	public void setBulkUnit(String bulkUnit) {
		this.bulkUnit = bulkUnit;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MateUnit [id=" + id + ", mateNumber=" + mateNumber + ", mateId=" + mateId + ", unitTypeId=" + unitTypeId
				+ ", unitTypeName=" + unitTypeName + ", unit=" + unit + ", mole=" + mole + ", deno=" + deno
				+ ", mateLength=" + mateLength + ", mateWidth=" + mateWidth + ", mateHigh=" + mateHigh + ", lwhUnit="
				+ lwhUnit + ", mateWeight=" + mateWeight + ", weightUnit=" + weightUnit + ", mateBulk=" + mateBulk
				+ ", bulkUnit=" + bulkUnit + "]";
	}

	
	
	
	
	
}
