package com.faujor.entity.bam.psm;

import java.math.BigDecimal;

/**
 * 备货计划与生产交货计划差异比较
 * @author 
 * 2018年10月31日 下午2:07:02
 * InvenPadCompare.java
 */
public class InvenPadCompare {
//	排名
	private String ranking ;
//	物料编码
	private String mateCode;
//	物料描述
	private String mateDesc;
//	交货计划物料编码
	private String padMateCode;
//	交货计划物料描述
	private String padMateDesc;
//	系列编码
	private String seriesCode;
//	系列描述
	private String seriesDesc;
//	类别编码
	private String itemCode;
//	类别描述
	private String itemName;
//	箱入数
	private Integer boxNumber;
//	包入数
	private Integer packNumber;
//	当月交货计划数据
	private BigDecimal invenDlvNum;
	private BigDecimal padDlvNum;
//	推迟1个月交货计划数据
	private BigDecimal addOneInvenDlvNum;
	private BigDecimal addOnePadDlvNum;
//	推迟2个月交货计划数据
	private BigDecimal addTwoInvenDlvNum;
	private BigDecimal addTwoPadDlvNum;
	//	推迟3个月交货计划数据
	private BigDecimal addThreeInvenDlvNum;
	private BigDecimal addThreePadDlvNum;
	//	推迟4个月交货计划数据
	private BigDecimal addFourInvenDlvNum;
	private BigDecimal addFourPadDlvNum;
	//	推迟5个月交货计划数据
	private BigDecimal addFiveInvenDlvNum;
	private BigDecimal addFivePadDlvNum;
	//	推迟6个月交货计划数据
	private BigDecimal addSixInvenDlvNum;
	private BigDecimal addSixPadDlvNum;
	//	推迟7个月交货计划数据
	private BigDecimal addSevenInvenDlvNum;
	private BigDecimal addSevenPadDlvNum;
	//	推迟8个月交货计划数据
	private BigDecimal addEightInvenDlvNum;
	private BigDecimal addEightPadDlvNum;
	//	推迟9个月交货计划数据
	private BigDecimal addNineInvenDlvNum;
	private BigDecimal addNinePadDlvNum;
	//	推迟10个月交货计划数据
	private BigDecimal addTenInvenDlvNum;
	private BigDecimal addTenPadDlvNum;
	//	推迟11个月交货计划数据
	private BigDecimal addElevenInvenDlvNum;
	private BigDecimal addElevenPadDlvNum;
	//	推迟12个月交货计划数据
	private BigDecimal addTwelveInvenDlvNum;
	private BigDecimal addTwelvePadDlvNum;
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
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
	public String getPadMateCode() {
		return padMateCode;
	}
	public void setPadMateCode(String padMateCode) {
		this.padMateCode = padMateCode;
	}
	public String getPadMateDesc() {
		return padMateDesc;
	}
	public void setPadMateDesc(String padMateDesc) {
		this.padMateDesc = padMateDesc;
	}
	public String getSeriesCode() {
		return seriesCode;
	}
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public String getSeriesDesc() {
		return seriesDesc;
	}
	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
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
	public Integer getBoxNumber() {
		return boxNumber;
	}
	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
	}
	public Integer getPackNumber() {
		return packNumber;
	}
	public void setPackNumber(Integer packNumber) {
		this.packNumber = packNumber;
	}
	public BigDecimal getInvenDlvNum() {
		return invenDlvNum;
	}
	public void setInvenDlvNum(BigDecimal invenDlvNum) {
		this.invenDlvNum = invenDlvNum;
	}
	public BigDecimal getPadDlvNum() {
		return padDlvNum;
	}
	public void setPadDlvNum(BigDecimal padDlvNum) {
		this.padDlvNum = padDlvNum;
	}
	public BigDecimal getAddOneInvenDlvNum() {
		return addOneInvenDlvNum;
	}
	public void setAddOneInvenDlvNum(BigDecimal addOneInvenDlvNum) {
		this.addOneInvenDlvNum = addOneInvenDlvNum;
	}
	public BigDecimal getAddOnePadDlvNum() {
		return addOnePadDlvNum;
	}
	public void setAddOnePadDlvNum(BigDecimal addOnePadDlvNum) {
		this.addOnePadDlvNum = addOnePadDlvNum;
	}
	public BigDecimal getAddTwoInvenDlvNum() {
		return addTwoInvenDlvNum;
	}
	public void setAddTwoInvenDlvNum(BigDecimal addTwoInvenDlvNum) {
		this.addTwoInvenDlvNum = addTwoInvenDlvNum;
	}
	public BigDecimal getAddTwoPadDlvNum() {
		return addTwoPadDlvNum;
	}
	public void setAddTwoPadDlvNum(BigDecimal addTwoPadDlvNum) {
		this.addTwoPadDlvNum = addTwoPadDlvNum;
	}
	public BigDecimal getAddThreeInvenDlvNum() {
		return addThreeInvenDlvNum;
	}
	public void setAddThreeInvenDlvNum(BigDecimal addThreeInvenDlvNum) {
		this.addThreeInvenDlvNum = addThreeInvenDlvNum;
	}
	public BigDecimal getAddThreePadDlvNum() {
		return addThreePadDlvNum;
	}
	public void setAddThreePadDlvNum(BigDecimal addThreePadDlvNum) {
		this.addThreePadDlvNum = addThreePadDlvNum;
	}
	public BigDecimal getAddFourInvenDlvNum() {
		return addFourInvenDlvNum;
	}
	public void setAddFourInvenDlvNum(BigDecimal addFourInvenDlvNum) {
		this.addFourInvenDlvNum = addFourInvenDlvNum;
	}
	public BigDecimal getAddFourPadDlvNum() {
		return addFourPadDlvNum;
	}
	public void setAddFourPadDlvNum(BigDecimal addFourPadDlvNum) {
		this.addFourPadDlvNum = addFourPadDlvNum;
	}
	public BigDecimal getAddFiveInvenDlvNum() {
		return addFiveInvenDlvNum;
	}
	public void setAddFiveInvenDlvNum(BigDecimal addFiveInvenDlvNum) {
		this.addFiveInvenDlvNum = addFiveInvenDlvNum;
	}
	public BigDecimal getAddFivePadDlvNum() {
		return addFivePadDlvNum;
	}
	public void setAddFivePadDlvNum(BigDecimal addFivePadDlvNum) {
		this.addFivePadDlvNum = addFivePadDlvNum;
	}
	public BigDecimal getAddSixInvenDlvNum() {
		return addSixInvenDlvNum;
	}
	public void setAddSixInvenDlvNum(BigDecimal addSixInvenDlvNum) {
		this.addSixInvenDlvNum = addSixInvenDlvNum;
	}
	public BigDecimal getAddSixPadDlvNum() {
		return addSixPadDlvNum;
	}
	public void setAddSixPadDlvNum(BigDecimal addSixPadDlvNum) {
		this.addSixPadDlvNum = addSixPadDlvNum;
	}
	public BigDecimal getAddSevenInvenDlvNum() {
		return addSevenInvenDlvNum;
	}
	public void setAddSevenInvenDlvNum(BigDecimal addSevenInvenDlvNum) {
		this.addSevenInvenDlvNum = addSevenInvenDlvNum;
	}
	public BigDecimal getAddSevenPadDlvNum() {
		return addSevenPadDlvNum;
	}
	public void setAddSevenPadDlvNum(BigDecimal addSevenPadDlvNum) {
		this.addSevenPadDlvNum = addSevenPadDlvNum;
	}
	public BigDecimal getAddEightInvenDlvNum() {
		return addEightInvenDlvNum;
	}
	public void setAddEightInvenDlvNum(BigDecimal addEightInvenDlvNum) {
		this.addEightInvenDlvNum = addEightInvenDlvNum;
	}
	public BigDecimal getAddEightPadDlvNum() {
		return addEightPadDlvNum;
	}
	public void setAddEightPadDlvNum(BigDecimal addEightPadDlvNum) {
		this.addEightPadDlvNum = addEightPadDlvNum;
	}
	public BigDecimal getAddNineInvenDlvNum() {
		return addNineInvenDlvNum;
	}
	public void setAddNineInvenDlvNum(BigDecimal addNineInvenDlvNum) {
		this.addNineInvenDlvNum = addNineInvenDlvNum;
	}
	public BigDecimal getAddNinePadDlvNum() {
		return addNinePadDlvNum;
	}
	public void setAddNinePadDlvNum(BigDecimal addNinePadDlvNum) {
		this.addNinePadDlvNum = addNinePadDlvNum;
	}
	public BigDecimal getAddTenInvenDlvNum() {
		return addTenInvenDlvNum;
	}
	public void setAddTenInvenDlvNum(BigDecimal addTenInvenDlvNum) {
		this.addTenInvenDlvNum = addTenInvenDlvNum;
	}
	public BigDecimal getAddTenPadDlvNum() {
		return addTenPadDlvNum;
	}
	public void setAddTenPadDlvNum(BigDecimal addTenPadDlvNum) {
		this.addTenPadDlvNum = addTenPadDlvNum;
	}
	public BigDecimal getAddElevenInvenDlvNum() {
		return addElevenInvenDlvNum;
	}
	public void setAddElevenInvenDlvNum(BigDecimal addElevenInvenDlvNum) {
		this.addElevenInvenDlvNum = addElevenInvenDlvNum;
	}
	public BigDecimal getAddElevenPadDlvNum() {
		return addElevenPadDlvNum;
	}
	public void setAddElevenPadDlvNum(BigDecimal addElevenPadDlvNum) {
		this.addElevenPadDlvNum = addElevenPadDlvNum;
	}
	public BigDecimal getAddTwelveInvenDlvNum() {
		return addTwelveInvenDlvNum;
	}
	public void setAddTwelveInvenDlvNum(BigDecimal addTwelveInvenDlvNum) {
		this.addTwelveInvenDlvNum = addTwelveInvenDlvNum;
	}
	public BigDecimal getAddTwelvePadDlvNum() {
		return addTwelvePadDlvNum;
	}
	public void setAddTwelvePadDlvNum(BigDecimal addTwelvePadDlvNum) {
		this.addTwelvePadDlvNum = addTwelvePadDlvNum;
	}	
}
