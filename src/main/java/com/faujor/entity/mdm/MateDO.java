package com.faujor.entity.mdm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：物料表mdm_mate
 * @update Vincent
 * @upateDate 2018-04-12
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MateDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String finMateId;// VARCHAR2(50), --成品物料编码
	private String mateCode;// VARCHAR2(20), --物料编码
	private String mateName;// VARCHAR2(20), --物料名称
	private String mateType;
	private String mateTypeExpl;// VARCHAR2(20), --物料类型说明
	private String basicUnit;// VARCHAR2(10), --基本单位
	private String mateGroup;
	private String mateGroupExpl;// VARCHAR2(20), --物料组说明
	private String procUnit;// VARCHAR2(10), --采购单位
	private String mateId;// VARCHAR2(50) NOT NULL, --编号
	private Integer boxNumber;// INT, --箱入数
	private String skuEnglish;
	private String chinName;
	private String seriesCode;// VARCHAR2(10), --系列代码
	private String seriesExpl;// VARCHAR2(50), --系列说明
	private String isMaintenance; // 是否维护
	private String mateBulk;//物料的提交
	
	
	private String LAY_CHECKED;

	private String itemCode; // 项次编码
	private String itemName;// 项次名称

	private String board;// 机台编码
	private String boardName;// 机台

	private String bigItemCode;	// 大品项编码
	private String bigItemExpl;	// 大品项
	
	private String isSpecial;//是否为特殊自制品
	private String isHaveSeim;//是否有半成品
	/**
	 * @return the boxNumber
	 */
	public Integer getBoxNumber() {
		return boxNumber;
	}

	/**
	 * @param boxNumber
	 *            the boxNumber to set
	 */
	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
	}

	/**
	 * @return the seriesCode
	 */
	public String getSeriesCode() {
		return seriesCode;
	}

	/**
	 * @return the lAY_CHECKED
	 */
	public String getLAY_CHECKED() {
		return LAY_CHECKED;
	}

	/**
	 * @param lAY_CHECKED
	 *            the lAY_CHECKED to set
	 */
	public void setLAY_CHECKED(String lAY_CHECKED) {
		LAY_CHECKED = lAY_CHECKED;
	}

	/**
	 * @param seriesCode
	 *            the seriesCode to set
	 */
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	/**
	 * @return the seriesExpl
	 */
	public String getSeriesExpl() {
		return seriesExpl;
	}

	/**
	 * @param seriesExpl
	 *            the seriesExpl to set
	 */
	public void setSeriesExpl(String seriesExpl) {
		this.seriesExpl = seriesExpl;
	}

	/**
	 * @return the mateType
	 */
	public String getMateType() {
		return mateType;
	}

	/**
	 * @param mateType
	 *            the mateType to set
	 */
	public void setMateType(String mateType) {
		this.mateType = mateType;
	}

	/**
	 * @return the skuEnglish
	 */
	public String getSkuEnglish() {
		return skuEnglish;
	}

	/**
	 * @param skuEnglish
	 *            the skuEnglish to set
	 */
	public void setSkuEnglish(String skuEnglish) {
		this.skuEnglish = skuEnglish;
	}

	public String getChinName() {
		return chinName;
	}

	public void setChinName(String chinName) {
		this.chinName = chinName;
	}

	public String getFinMateId() {
		return finMateId;
	}

	public void setFinMateId(String finMateId) {
		this.finMateId = finMateId;
	}

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}

	public String getMateName() {
		return mateName;
	}

	public void setMateName(String mateName) {
		this.mateName = mateName;
	}

	public String getMateTypeExpl() {
		return mateTypeExpl;
	}

	public void setMateTypeExpl(String mateTypeExpl) {
		this.mateTypeExpl = mateTypeExpl;
	}

	public String getBasicUnit() {
		return basicUnit;
	}

	public void setBasicUnit(String basicUnit) {
		this.basicUnit = basicUnit;
	}

	public String getMateGroup() {
		return mateGroup;
	}

	public void setMateGroup(String mateGroup) {
		this.mateGroup = mateGroup;
	}

	public String getMateGroupExpl() {
		return mateGroupExpl;
	}

	public void setMateGroupExpl(String mateGroupExpl) {
		this.mateGroupExpl = mateGroupExpl;
	}

	public String getProcUnit() {
		return procUnit;
	}

	public void setProcUnit(String procUnit) {
		this.procUnit = procUnit;
	}

	public String getMateId() {
		return mateId;
	}

	public void setMateId(String mateId) {
		this.mateId = mateId;
	}

	public String getIsMaintenance() {
		return isMaintenance;
	}

	public void setIsMaintenance(String isMaintenance) {
		this.isMaintenance = isMaintenance;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	/**
	 * @return the mateBulk
	 */
	public String getMateBulk() {
		return mateBulk;
	}

	/**
	 * @param mateBulk the mateBulk to set
	 */
	public void setMateBulk(String mateBulk) {
		this.mateBulk = mateBulk;
	}

	public String getBigItemCode() {
		return bigItemCode;
	}

	public void setBigItemCode(String bigItemCode) {
		this.bigItemCode = bigItemCode;
	}

	public String getBigItemExpl() {
		return bigItemExpl;
	}

	public void setBigItemExpl(String bigItemExpl) {
		this.bigItemExpl = bigItemExpl;
	}

	/**
	 * @return the isSpecial
	 */
	public String getIsSpecial() {
		return isSpecial;
	}

	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	/**
	 * @return the isHaveSeim
	 */
	public String getIsHaveSeim() {
		return isHaveSeim;
	}

	/**
	 * @param isHaveSeim the isHaveSeim to set
	 */
	public void setIsHaveSeim(String isHaveSeim) {
		this.isHaveSeim = isHaveSeim;
	}
	
	
}
