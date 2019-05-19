package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 旺季备货表
 * @author hql
 *
 */
public class BusyStock  implements Serializable{

	private static final long serialVersionUID = 1L;
	private static BigDecimal bg0=new BigDecimal(0);
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
	
	private String safeScale="";
	
	//当月后一个月的生产计划
	private BigDecimal addOnePlanPrdNum=bg0;
	//当月后一个月的交货计划
	private BigDecimal addOnePlanDlvNum=bg0;
	//当月后一个月的期末库存
	private BigDecimal addOnePlanEndStock=bg0;
	//当月后一个月的安全库存率
	private String addOneSafeScale="";
	
	
	//当月后二个月的生产计划
	private BigDecimal addTwoPlanPrdNum=bg0;
	//当月后二个月的交货计划
	private BigDecimal addTwoPlanDlvNum=bg0;
	//当月后二个月的期末库存
	private BigDecimal addTwoPlanEndStock=bg0;
	//当月后2个月的安全库存率
	private String addTwoSafeScale;	
	
	//当月后三个月的生产计划
	private BigDecimal addThreePlanPrdNum=bg0;
	//当月后三个月的交货计划
	private BigDecimal addThreePlanDlvNum=bg0;
	//当月后三个月的期末库存
	private BigDecimal addThreePlanEndStock=bg0;
	//当月后3个月的安全库存率
	private String addThreeSafeScale="";	
		
	//当月后四个月的生产计划
	private BigDecimal addFourPlanPrdNum=bg0;
	//当月后四个月的交货计划
	private BigDecimal addFourPlanDlvNum=bg0;
	//当月后四个月的期末库存
	private BigDecimal addFourPlanEndStock=bg0;
	//当月后4个月的安全库存率
	private String addFourSafeScale="";		
	
	//当月后五个月的生产计划
	private BigDecimal addFivePlanPrdNum=bg0;
	//当月后五个月的交货计划
	private BigDecimal addFivePlanDlvNum=bg0;
	//当月后五个月的期末库存
	private BigDecimal addFivePlanEndStock=bg0;
	//当月后5个月的安全库存率
	private String addFiveSafeScale="";	
	
	//当月后六个月的生产计划
	private BigDecimal addSixPlanPrdNum=bg0;
	//当月后六个月的交货计划
	private BigDecimal addSixPlanDlvNum=bg0;
	//当月后六个月的期末库存
	private BigDecimal addSixPlanEndStock=bg0;
	//当月后6个月的安全库存率
	private String addSixSafeScale="";	
	
	//当月后七个月的生产计划
	private BigDecimal addSevenPlanPrdNum=bg0;
	//当月后七个月的交货计划
	private BigDecimal addSevenPlanDlvNum=bg0;
	//当月后七个月的期末库存
	private BigDecimal addSevenPlanEndStock=bg0;
	//当月后7个月的安全库存率
	private String addSevenSafeScale="";	
	
	
	//当月后八个月的生产计划
	private BigDecimal addEightPlanPrdNum=bg0;
	//当月后八个月的交货计划
	private BigDecimal addEightPlanDlvNum=bg0;
	//当月后八个月的期末库存
	private BigDecimal addEightPlanEndStock=bg0;
	//当月后8个月的安全库存率
	private String addEightSafeScale="";
	
	
	//当月后九个月的生产计划
	private BigDecimal addNinePlanPrdNum=bg0;
	//当月后九个月的交货计划
	private BigDecimal addNinePlanDlvNum=bg0;
	//当月后九个月的期末库存
	private BigDecimal addNinePlanEndStock=bg0;
	//当月后9个月的安全库存率
	private String addNineSafeScale="";
	
	//当月后十个月的生产计划
	private BigDecimal addTenPlanPrdNum=bg0;
	//当月后十个月的交货计划
	private BigDecimal addTenPlanDlvNum=bg0;
	//当月后十个月的期末库存
	private BigDecimal addTenPlanEndStock=bg0;
	//当月后10个月的安全库存率
	private String addTenSafeScale="";
	
	//当月后十一个月的生产计划
	private BigDecimal addElevenPlanPrdNum=bg0;
	//当月后十一个月的交货计划
	private BigDecimal addElevenPlanDlvNum=bg0;
	//当月后十一个月的期末库存
	private BigDecimal addElevenPlanEndStock=bg0;
	//当月后11个月的安全库存率
	private String addElevenSafeScale="";
	
	//当月后十二个月的生产计划
	private BigDecimal addTwelvePlanPrdNum=bg0;
	//当月后十二个月的交货计划
	private BigDecimal addTwelvePlanDlvNum=bg0;
	//当月后十二个月的期末库存
	private BigDecimal addTwelvePlanEndStock=bg0;
	//当月后12个月的安全库存率
	private String addTwelveSafeScale="";
	
	//当月后13个月的交货计划
	private BigDecimal addThirteenPlanDlvNum=bg0;
	
	//当月后十二个月的生产计划
	private BigDecimal sumPlanPrdNum=bg0;
	//当月后十二个月的交货计划
	private BigDecimal sumPlanDlvNum=bg0;
	//当月后十二个月的期末库存
	private BigDecimal maxPlanEndStock=bg0;
	
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
	public void setAddEightPlanEndStock(BigDecimal addEigthPlanEndStock) {
		this.addEightPlanEndStock = addEigthPlanEndStock;
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
	public Integer getPackNumber() {
		return packNumber;
	}
	public void setPackNumber(Integer packNumber) {
		this.packNumber = packNumber;
	}
	public String getSafeScale() {
		return safeScale;
	}
	public void setSafeScale(String safeScale) {
		this.safeScale = safeScale;
	}
	public String getAddOneSafeScale() {
		return addOneSafeScale;
	}
	public void setAddOneSafeScale(String addOneSafeScale) {
		this.addOneSafeScale = addOneSafeScale;
	}
	public String getAddTwoSafeScale() {
		return addTwoSafeScale;
	}
	public void setAddTwoSafeScale(String addTwoSafeScale) {
		this.addTwoSafeScale = addTwoSafeScale;
	}
	public String getAddThreeSafeScale() {
		return addThreeSafeScale;
	}
	public void setAddThreeSafeScale(String addThreeSafeScale) {
		this.addThreeSafeScale = addThreeSafeScale;
	}
	public String getAddFourSafeScale() {
		return addFourSafeScale;
	}
	public void setAddFourSafeScale(String addFourSafeScale) {
		this.addFourSafeScale = addFourSafeScale;
	}
	public String getAddFiveSafeScale() {
		return addFiveSafeScale;
	}
	public void setAddFiveSafeScale(String addFiveSafeScale) {
		this.addFiveSafeScale = addFiveSafeScale;
	}
	public String getAddSixSafeScale() {
		return addSixSafeScale;
	}
	public void setAddSixSafeScale(String addSixSafeScale) {
		this.addSixSafeScale = addSixSafeScale;
	}
	public String getAddSevenSafeScale() {
		return addSevenSafeScale;
	}
	public void setAddSevenSafeScale(String addSevenSafeScale) {
		this.addSevenSafeScale = addSevenSafeScale;
	}
	public String getAddEightSafeScale() {
		return addEightSafeScale;
	}
	public void setAddEightSafeScale(String addEightSafeScale) {
		this.addEightSafeScale = addEightSafeScale;
	}
	public String getAddNineSafeScale() {
		return addNineSafeScale;
	}
	public void setAddNineSafeScale(String addNineSafeScale) {
		this.addNineSafeScale = addNineSafeScale;
	}
	public String getAddTenSafeScale() {
		return addTenSafeScale;
	}
	public void setAddTenSafeScale(String addTenSafeScale) {
		this.addTenSafeScale = addTenSafeScale;
	}
	public String getAddElevenSafeScale() {
		return addElevenSafeScale;
	}
	public void setAddElevenSafeScale(String addElevenSafeScale) {
		this.addElevenSafeScale = addElevenSafeScale;
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
	
}
