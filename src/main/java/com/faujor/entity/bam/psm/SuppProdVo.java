package com.faujor.entity.bam.psm;

import java.math.BigDecimal;
import java.util.Date;

import com.faujor.utils.RestCode;

public class SuppProdVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// id
	private String id;
	// 备货计划子表id
	private String mainId;
//	PROD_SERIES_CODE
	private String prodSeriesCode;
//	产品系列标书
	private String prodSeriesDesc;
	
	private String planDetailId;
	
	private RestCode restCode=RestCode.noUpdate();
	
	private BigDecimal nationStock;
	
	private String status;
	
	private Double rowNum;
//	品项编码
	private String itemCode;
//	品项描述
	private String itemName;
//	物料名称
	private String mateCode;
//	物料描述
	private String mateDesc;
//	供应商编码
	private String suppNo;
//	供应商名称
	private String suppName;
//	排名
	private String rank;
//	计划月份
	private Date planMonth; 
//	生产计划
	private BigDecimal planPrdNum; 
//	交货计划
	private BigDecimal planDlvNum;
//	箱入数
	private Integer boxNumber;
//	包入数
	private Integer packNumber;
//	期初订单
	private BigDecimal beginOrder;
//	期初库存
	private BigDecimal beginStock;
//	期初可生产订单
	private BigDecimal beginEnableOrder;
//	期末预计库存
	private BigDecimal endStock;
	
	private String safeScale;
	
	//当月后一个月的生产计划
	private BigDecimal addOnePlanPrdNum;
	//当月后一个月的交货计划
	private BigDecimal addOnePlanDlvNum;
	//当月后一个月的期末库存
	private BigDecimal addOnePlanEndStock;
	//当月后一个月的安全库存率
	private String addOneSafeScale;
	
	
	//当月后二个月的生产计划
	private BigDecimal addTwoPlanPrdNum;
	//当月后二个月的交货计划
	private BigDecimal addTwoPlanDlvNum;
	//当月后二个月的期末库存
	private BigDecimal addTwoPlanEndStock;
	//当月后2个月的安全库存率
	private String addTwoSafeScale;	
	
	//当月后三个月的生产计划
	private BigDecimal addThreePlanPrdNum;
	//当月后三个月的交货计划
	private BigDecimal addThreePlanDlvNum;
	//当月后三个月的期末库存
	private BigDecimal addThreePlanEndStock;
	//当月后3个月的安全库存率
	private String addThreeSafeScale;	
		
	//当月后四个月的生产计划
	private BigDecimal addFourPlanPrdNum;
	//当月后四个月的交货计划
	private BigDecimal addFourPlanDlvNum;
	//当月后四个月的期末库存
	private BigDecimal addFourPlanEndStock;
	//当月后4个月的安全库存率
	private String addFourSafeScale;		
	
	//当月后五个月的生产计划
	private BigDecimal addFivePlanPrdNum;
	//当月后五个月的交货计划
	private BigDecimal addFivePlanDlvNum;
	//当月后五个月的期末库存
	private BigDecimal addFivePlanEndStock;
	//当月后5个月的安全库存率
	private String addFiveSafeScale;	
	
	//当月后六个月的生产计划
	private BigDecimal addSixPlanPrdNum;
	//当月后六个月的交货计划
	private BigDecimal addSixPlanDlvNum;
	//当月后六个月的期末库存
	private BigDecimal addSixPlanEndStock;
	//当月后6个月的安全库存率
	private String addSixSafeScale;	
	
	//当月后七个月的生产计划
	private BigDecimal addSevenPlanPrdNum;
	//当月后七个月的交货计划
	private BigDecimal addSevenPlanDlvNum;
	//当月后七个月的期末库存
	private BigDecimal addSevenPlanEndStock;
	//当月后7个月的安全库存率
	private String addSevenSafeScale;	
	
	
	//当月后八个月的生产计划
	private BigDecimal addEightPlanPrdNum;
	//当月后八个月的交货计划
	private BigDecimal addEightPlanDlvNum;
	//当月后八个月的期末库存
	private BigDecimal addEightPlanEndStock;
	//当月后8个月的安全库存率
	private String addEightSafeScale;
	
	
	//当月后九个月的生产计划
	private BigDecimal addNinePlanPrdNum;
	//当月后九个月的交货计划
	private BigDecimal addNinePlanDlvNum;
	//当月后九个月的期末库存
	private BigDecimal addNinePlanEndStock;
	//当月后9个月的安全库存率
	private String addNineSafeScale;
	
	//当月后十个月的生产计划
	private BigDecimal addTenPlanPrdNum;
	//当月后十个月的交货计划
	private BigDecimal addTenPlanDlvNum;
	//当月后十个月的期末库存
	private BigDecimal addTenPlanEndStock;
	//当月后10个月的安全库存率
	private String addTenSafeScale;
	
	//当月后十一个月的生产计划
	private BigDecimal addElevenPlanPrdNum;
	//当月后十一个月的交货计划
	private BigDecimal addElevenPlanDlvNum;
	//当月后十一个月的期末库存
	private BigDecimal addElevenPlanEndStock;
	//当月后11个月的安全库存率
	private String addElevenSafeScale;
	
	//当月后十二个月的生产计划
	private BigDecimal addTwelvePlanPrdNum;
	//当月后十二个月的交货计划
	private BigDecimal addTwelvePlanDlvNum;
	//当月后十二个月的期末库存
	private BigDecimal addTwelvePlanEndStock;
	//当月后12个月的安全库存率
	private String addTwelveSafeScale;
	
	//当月后13个月的交货计划
	private BigDecimal addThirteenPlanDlvNum;
	
	//当月后十二个月的生产计划
	private BigDecimal sumPlanPrdNum;
	//当月后十二个月的交货计划
	private BigDecimal sumPlanDlvNum;
	//当月后十二个月的期末库存
	private BigDecimal maxPlanEndStock;
	
	
	

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

	public String getProdSeriesCode() {
		return prodSeriesCode;
	}

	public void setProdSeriesCode(String prodSeriesCode) {
		this.prodSeriesCode = prodSeriesCode;
	}

	public String getProdSeriesDesc() {
		return prodSeriesDesc;
	}

	public void setProdSeriesDesc(String prodSeriesDesc) {
		this.prodSeriesDesc = prodSeriesDesc;
	}

	public String getPlanDetailId() {
		return planDetailId;
	}

	public void setPlanDetailId(String planDetailId) {
		this.planDetailId = planDetailId;
	}

	public RestCode getRestCode() {
		return restCode;
	}

	public void setRestCode(RestCode restCode) {
		this.restCode = restCode;
	}

	public BigDecimal getNationStock() {
		return nationStock;
	}

	public void setNationStock(BigDecimal nationStock) {
		this.nationStock = nationStock;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getRowNum() {
		return rowNum;
	}
	public void setRowNum(Double rowNum) {
		this.rowNum = rowNum;
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

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Date getPlanMonth() {
		return planMonth;
	}

	public void setPlanMonth(Date planMonth) {
		this.planMonth = planMonth;
	}

	public BigDecimal getPlanPrdNum() {
		return planPrdNum;
	}

	public void setPlanPrdNum(BigDecimal planPrdNum) {
		this.planPrdNum = planPrdNum;
	}

	public BigDecimal getPlanDlvNum() {
		return planDlvNum;
	}

	public void setPlanDlvNum(BigDecimal planDlvNum) {
		this.planDlvNum = planDlvNum;
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

	public BigDecimal getBeginOrder() {
		return beginOrder;
	}

	public void setBeginOrder(BigDecimal beginOrder) {
		this.beginOrder = beginOrder;
	}

	public BigDecimal getBeginStock() {
		return beginStock;
	}

	public void setBeginStock(BigDecimal beginStock) {
		this.beginStock = beginStock;
	}

	public BigDecimal getBeginEnableOrder() {
		return beginEnableOrder;
	}

	public void setBeginEnableOrder(BigDecimal beginEnableOrder) {
		this.beginEnableOrder = beginEnableOrder;
	}

	public BigDecimal getEndStock() {
		return endStock;
	}

	public void setEndStock(BigDecimal endStock) {
		this.endStock = endStock;
	}

	public String getSafeScale() {
		return safeScale;
	}

	public void setSafeScale(String safeScale) {
		this.safeScale = safeScale;
	}

	public BigDecimal getAddOnePlanPrdNum() {
		return addOnePlanPrdNum;
	}

	public void setAddOnePlanPrdNum(BigDecimal addOnePlanPrdNum) {
		this.addOnePlanPrdNum = addOnePlanPrdNum;
	}

	public BigDecimal getAddOnePlanDlvNum() {
		return addOnePlanDlvNum;
	}

	public void setAddOnePlanDlvNum(BigDecimal addOnePlanDlvNum) {
		this.addOnePlanDlvNum = addOnePlanDlvNum;
	}

	public BigDecimal getAddOnePlanEndStock() {
		return addOnePlanEndStock;
	}

	public void setAddOnePlanEndStock(BigDecimal addOnePlanEndStock) {
		this.addOnePlanEndStock = addOnePlanEndStock;
	}

	public String getAddOneSafeScale() {
		return addOneSafeScale;
	}

	public void setAddOneSafeScale(String addOneSafeScale) {
		this.addOneSafeScale = addOneSafeScale;
	}

	public BigDecimal getAddTwoPlanPrdNum() {
		return addTwoPlanPrdNum;
	}

	public void setAddTwoPlanPrdNum(BigDecimal addTwoPlanPrdNum) {
		this.addTwoPlanPrdNum = addTwoPlanPrdNum;
	}

	public BigDecimal getAddTwoPlanDlvNum() {
		return addTwoPlanDlvNum;
	}

	public void setAddTwoPlanDlvNum(BigDecimal addTwoPlanDlvNum) {
		this.addTwoPlanDlvNum = addTwoPlanDlvNum;
	}

	public BigDecimal getAddTwoPlanEndStock() {
		return addTwoPlanEndStock;
	}

	public void setAddTwoPlanEndStock(BigDecimal addTwoPlanEndStock) {
		this.addTwoPlanEndStock = addTwoPlanEndStock;
	}

	public String getAddTwoSafeScale() {
		return addTwoSafeScale;
	}

	public void setAddTwoSafeScale(String addTwoSafeScale) {
		this.addTwoSafeScale = addTwoSafeScale;
	}

	public BigDecimal getAddThreePlanPrdNum() {
		return addThreePlanPrdNum;
	}

	public void setAddThreePlanPrdNum(BigDecimal addThreePlanPrdNum) {
		this.addThreePlanPrdNum = addThreePlanPrdNum;
	}

	public BigDecimal getAddThreePlanDlvNum() {
		return addThreePlanDlvNum;
	}

	public void setAddThreePlanDlvNum(BigDecimal addThreePlanDlvNum) {
		this.addThreePlanDlvNum = addThreePlanDlvNum;
	}

	public BigDecimal getAddThreePlanEndStock() {
		return addThreePlanEndStock;
	}

	public void setAddThreePlanEndStock(BigDecimal addThreePlanEndStock) {
		this.addThreePlanEndStock = addThreePlanEndStock;
	}

	public String getAddThreeSafeScale() {
		return addThreeSafeScale;
	}

	public void setAddThreeSafeScale(String addThreeSafeScale) {
		this.addThreeSafeScale = addThreeSafeScale;
	}

	public BigDecimal getAddFourPlanPrdNum() {
		return addFourPlanPrdNum;
	}

	public void setAddFourPlanPrdNum(BigDecimal addFourPlanPrdNum) {
		this.addFourPlanPrdNum = addFourPlanPrdNum;
	}

	public BigDecimal getAddFourPlanDlvNum() {
		return addFourPlanDlvNum;
	}

	public void setAddFourPlanDlvNum(BigDecimal addFourPlanDlvNum) {
		this.addFourPlanDlvNum = addFourPlanDlvNum;
	}

	public BigDecimal getAddFourPlanEndStock() {
		return addFourPlanEndStock;
	}

	public void setAddFourPlanEndStock(BigDecimal addFourPlanEndStock) {
		this.addFourPlanEndStock = addFourPlanEndStock;
	}

	public String getAddFourSafeScale() {
		return addFourSafeScale;
	}

	public void setAddFourSafeScale(String addFourSafeScale) {
		this.addFourSafeScale = addFourSafeScale;
	}

	public BigDecimal getAddFivePlanPrdNum() {
		return addFivePlanPrdNum;
	}

	public void setAddFivePlanPrdNum(BigDecimal addFivePlanPrdNum) {
		this.addFivePlanPrdNum = addFivePlanPrdNum;
	}

	public BigDecimal getAddFivePlanDlvNum() {
		return addFivePlanDlvNum;
	}

	public void setAddFivePlanDlvNum(BigDecimal addFivePlanDlvNum) {
		this.addFivePlanDlvNum = addFivePlanDlvNum;
	}

	public BigDecimal getAddFivePlanEndStock() {
		return addFivePlanEndStock;
	}

	public void setAddFivePlanEndStock(BigDecimal addFivePlanEndStock) {
		this.addFivePlanEndStock = addFivePlanEndStock;
	}

	public String getAddFiveSafeScale() {
		return addFiveSafeScale;
	}

	public void setAddFiveSafeScale(String addFiveSafeScale) {
		this.addFiveSafeScale = addFiveSafeScale;
	}

	public BigDecimal getAddSixPlanPrdNum() {
		return addSixPlanPrdNum;
	}

	public void setAddSixPlanPrdNum(BigDecimal addSixPlanPrdNum) {
		this.addSixPlanPrdNum = addSixPlanPrdNum;
	}

	public BigDecimal getAddSixPlanDlvNum() {
		return addSixPlanDlvNum;
	}

	public void setAddSixPlanDlvNum(BigDecimal addSixPlanDlvNum) {
		this.addSixPlanDlvNum = addSixPlanDlvNum;
	}

	public BigDecimal getAddSixPlanEndStock() {
		return addSixPlanEndStock;
	}

	public void setAddSixPlanEndStock(BigDecimal addSixPlanEndStock) {
		this.addSixPlanEndStock = addSixPlanEndStock;
	}

	public String getAddSixSafeScale() {
		return addSixSafeScale;
	}

	public void setAddSixSafeScale(String addSixSafeScale) {
		this.addSixSafeScale = addSixSafeScale;
	}

	public BigDecimal getAddSevenPlanPrdNum() {
		return addSevenPlanPrdNum;
	}

	public void setAddSevenPlanPrdNum(BigDecimal addSevenPlanPrdNum) {
		this.addSevenPlanPrdNum = addSevenPlanPrdNum;
	}

	public BigDecimal getAddSevenPlanDlvNum() {
		return addSevenPlanDlvNum;
	}

	public void setAddSevenPlanDlvNum(BigDecimal addSevenPlanDlvNum) {
		this.addSevenPlanDlvNum = addSevenPlanDlvNum;
	}

	public BigDecimal getAddSevenPlanEndStock() {
		return addSevenPlanEndStock;
	}

	public void setAddSevenPlanEndStock(BigDecimal addSevenPlanEndStock) {
		this.addSevenPlanEndStock = addSevenPlanEndStock;
	}

	public String getAddSevenSafeScale() {
		return addSevenSafeScale;
	}

	public void setAddSevenSafeScale(String addSevenSafeScale) {
		this.addSevenSafeScale = addSevenSafeScale;
	}

	public BigDecimal getAddEightPlanPrdNum() {
		return addEightPlanPrdNum;
	}

	public void setAddEightPlanPrdNum(BigDecimal addEightPlanPrdNum) {
		this.addEightPlanPrdNum = addEightPlanPrdNum;
	}

	public BigDecimal getAddEightPlanDlvNum() {
		return addEightPlanDlvNum;
	}

	public void setAddEightPlanDlvNum(BigDecimal addEightPlanDlvNum) {
		this.addEightPlanDlvNum = addEightPlanDlvNum;
	}

	public BigDecimal getAddEightPlanEndStock() {
		return addEightPlanEndStock;
	}

	public void setAddEightPlanEndStock(BigDecimal addEightPlanEndStock) {
		this.addEightPlanEndStock = addEightPlanEndStock;
	}

	public String getAddEightSafeScale() {
		return addEightSafeScale;
	}

	public void setAddEightSafeScale(String addEightSafeScale) {
		this.addEightSafeScale = addEightSafeScale;
	}

	public BigDecimal getAddNinePlanPrdNum() {
		return addNinePlanPrdNum;
	}

	public void setAddNinePlanPrdNum(BigDecimal addNinePlanPrdNum) {
		this.addNinePlanPrdNum = addNinePlanPrdNum;
	}

	public BigDecimal getAddNinePlanDlvNum() {
		return addNinePlanDlvNum;
	}

	public void setAddNinePlanDlvNum(BigDecimal addNinePlanDlvNum) {
		this.addNinePlanDlvNum = addNinePlanDlvNum;
	}

	public BigDecimal getAddNinePlanEndStock() {
		return addNinePlanEndStock;
	}

	public void setAddNinePlanEndStock(BigDecimal addNinePlanEndStock) {
		this.addNinePlanEndStock = addNinePlanEndStock;
	}

	public String getAddNineSafeScale() {
		return addNineSafeScale;
	}

	public void setAddNineSafeScale(String addNineSafeScale) {
		this.addNineSafeScale = addNineSafeScale;
	}

	public BigDecimal getAddTenPlanPrdNum() {
		return addTenPlanPrdNum;
	}

	public void setAddTenPlanPrdNum(BigDecimal addTenPlanPrdNum) {
		this.addTenPlanPrdNum = addTenPlanPrdNum;
	}

	public BigDecimal getAddTenPlanDlvNum() {
		return addTenPlanDlvNum;
	}

	public void setAddTenPlanDlvNum(BigDecimal addTenPlanDlvNum) {
		this.addTenPlanDlvNum = addTenPlanDlvNum;
	}

	public BigDecimal getAddTenPlanEndStock() {
		return addTenPlanEndStock;
	}

	public void setAddTenPlanEndStock(BigDecimal addTenPlanEndStock) {
		this.addTenPlanEndStock = addTenPlanEndStock;
	}

	public String getAddTenSafeScale() {
		return addTenSafeScale;
	}

	public void setAddTenSafeScale(String addTenSafeScale) {
		this.addTenSafeScale = addTenSafeScale;
	}

	public BigDecimal getAddElevenPlanPrdNum() {
		return addElevenPlanPrdNum;
	}

	public void setAddElevenPlanPrdNum(BigDecimal addElevenPlanPrdNum) {
		this.addElevenPlanPrdNum = addElevenPlanPrdNum;
	}

	public BigDecimal getAddElevenPlanDlvNum() {
		return addElevenPlanDlvNum;
	}

	public void setAddElevenPlanDlvNum(BigDecimal addElevenPlanDlvNum) {
		this.addElevenPlanDlvNum = addElevenPlanDlvNum;
	}

	public BigDecimal getAddElevenPlanEndStock() {
		return addElevenPlanEndStock;
	}

	public void setAddElevenPlanEndStock(BigDecimal addElevenPlanEndStock) {
		this.addElevenPlanEndStock = addElevenPlanEndStock;
	}

	public String getAddElevenSafeScale() {
		return addElevenSafeScale;
	}

	public void setAddElevenSafeScale(String addElevenSafeScale) {
		this.addElevenSafeScale = addElevenSafeScale;
	}

	public BigDecimal getAddTwelvePlanPrdNum() {
		return addTwelvePlanPrdNum;
	}

	public void setAddTwelvePlanPrdNum(BigDecimal addTwelvePlanPrdNum) {
		this.addTwelvePlanPrdNum = addTwelvePlanPrdNum;
	}

	public BigDecimal getAddTwelvePlanDlvNum() {
		return addTwelvePlanDlvNum;
	}

	public void setAddTwelvePlanDlvNum(BigDecimal addTwelvePlanDlvNum) {
		this.addTwelvePlanDlvNum = addTwelvePlanDlvNum;
	}

	public BigDecimal getAddTwelvePlanEndStock() {
		return addTwelvePlanEndStock;
	}

	public void setAddTwelvePlanEndStock(BigDecimal addTwelvePlanEndStock) {
		this.addTwelvePlanEndStock = addTwelvePlanEndStock;
	}

	public String getAddTwelveSafeScale() {
		return addTwelveSafeScale;
	}

	public void setAddTwelveSafeScale(String addTwelveSafeScale) {
		this.addTwelveSafeScale = addTwelveSafeScale;
	}

	public BigDecimal getAddThirteenPlanDlvNum() {
		return addThirteenPlanDlvNum;
	}

	public void setAddThirteenPlanDlvNum(BigDecimal addThirteenPlanDlvNum) {
		this.addThirteenPlanDlvNum = addThirteenPlanDlvNum;
	}

	public BigDecimal getSumPlanPrdNum() {
		return sumPlanPrdNum;
	}

	public void setSumPlanPrdNum(BigDecimal sumPlanPrdNum) {
		this.sumPlanPrdNum = sumPlanPrdNum;
	}

	public BigDecimal getSumPlanDlvNum() {
		return sumPlanDlvNum;
	}

	public void setSumPlanDlvNum(BigDecimal sumPlanDlvNum) {
		this.sumPlanDlvNum = sumPlanDlvNum;
	}

	public BigDecimal getMaxPlanEndStock() {
		return maxPlanEndStock;
	}

	public void setMaxPlanEndStock(BigDecimal maxPlanEndStock) {
		this.maxPlanEndStock = maxPlanEndStock;
	}
}
