package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：Psi
 * @tableDesc 进销存报表实体类
 * @author Vincent
 * @date 2018-03-28
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Psi implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer sumnum;// 汇总数量（大品项汇总）
	private String rank;// 排名
	private String produExpl;// 排名
	private String seriesCode;// 系列编码
	private String matCode;// 物料编码
	private String matShort;// 物料简称
	private Float threeAvgSales;// 前三个月平均销量
	private Float preY1mSales;// 去年1月实际销售量
	private Float preY2mSales;// 去年2月实际销售量
	private Float preY3mSales;// 去年3月实际销售量
	private Float preY4mSales;// 去年4月实际销售量
	private Float preY5mSales;// 去年5月实际销售量
	private Float preY6mSales;// 去年6月实际销售量
	private Float preY7mSales;// 去年7月实际销售量
	private Float preY8mSales;// 去年8月实际销售量
	private Float preY9mSales;// 去年9月实际销售量
	private Float preY10mSales;// 去年10月实际销售量
	private Float preY11mSales;// 去年11月实际销售量
	private Float preY12mSales;// 去年12月实际销售量

	private Float currY1mSales;// 当年1月实际销售量
	private Float currY2mSales;// 当年2月实际销售量
	private Float currY3mSales;// 当年3月实际销售量
	private Float currY4mSales;// 当年4月实际销售量
	private Float currY5mSales;// 当年5月实际销售量
	private Float currY6mSales;// 当年6月实际销售量
	private Float currY7mSales;// 当年7月实际销售量
	private Float currY8mSales;// 当年8月实际销售量
	private Float currY9mSales;// 当年9月实际销售量
	private Float currY10mSales;// 当年10月实际销售量
	private Float currY11mSales;// 当年11月实际销售量
	private Float currY12mSales;// 当年12月实际销售量
	
	private Float nationStock1; // XX月全国库存
	private Float nationStock2; // YY月全国库存
	private Float padPlanQty;	// XX月生产交货计划
	private Float padActQty;	// XX月实际生产数量
	private Float saleForeQty;	// XX月销售预测
	private Float saleForeActQty;	// XX月实际销售数量
	private Float turnOverDays;	// 周转天数
	private String procAchRate;	// 生产达成率
	private String salesAchRate;	// 销售达成率
	private Float estDeliQty;	// XX月预计交货数量
	private Float estSaleQty;	// XX月预计销售数量
	private Float actSaleQty;	// XX月实际销售数量
	private String prodSeriesCode;	// 系列
	private String bigItemExpl;	// 大品項
	
	private Float next1Plan;// 后续12个月的生产交货计划
	private Float next2Plan;
	private Float next3Plan;
	private Float next4Plan;
	private Float next5Plan;
	private Float next6Plan;
	private Float next7Plan;
	private Float next8Plan;
	private Float next9Plan;
	private Float next10Plan;
	private Float next11Plan;
	private Float next12Plan;
	
	private Float next1SalesF;// 后续12个月的销售预测
	private Float next2SalesF;
	private Float next3SalesF;
	private Float next4SalesF;
	private Float next5SalesF;
	private Float next6SalesF;
	private Float next7SalesF;
	private Float next8SalesF;
	private Float next9SalesF;
	private Float next10SalesF;
	private Float next11SalesF;
	private Float next12SalesF;
	
	private Float pre1Stock;// 后续12个月对应去年的库存
	private Float pre2Stock;
	private Float pre3Stock;
	private Float pre4Stock;
	private Float pre5Stock;
	private Float pre6Stock;
	private Float pre7Stock;
	private Float pre8Stock;
	private Float pre9Stock;
	private Float pre10Stock;
	private Float pre11Stock;
	private Float pre12Stock;
	
	private Float tod1;// 后续12个月周转天数
	private Float tod2;
	private Float tod3;
	private Float tod4;
	private Float tod5;
	private Float tod6;
	private Float tod7;
	private Float tod8;
	private Float tod9;
	private Float tod10;
	private Float tod11;
	private Float tod12;
	
	public Integer getSumnum() {
		return sumnum;
	}
	public void setSumnum(Integer sumnum) {
		this.sumnum = sumnum;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	/**
	 * @return the produExpl
	 */
	public String getProduExpl() {
		return produExpl;
	}
	/**
	 * @param produExpl the produExpl to set
	 */
	public void setProduExpl(String produExpl) {
		this.produExpl = produExpl;
	}
	public String getSeriesCode() {
		return seriesCode;
	}
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	public String getMatShort() {
		return matShort;
	}
	public void setMatShort(String matShort) {
		this.matShort = matShort;
	}
	public Float getThreeAvgSales() {
		return threeAvgSales;
	}
	public void setThreeAvgSales(Float threeAvgSales) {
		this.threeAvgSales = threeAvgSales;
	}
	public Float getPreY1mSales() {
		return preY1mSales;
	}
	public void setPreY1mSales(Float preY1mSales) {
		this.preY1mSales = preY1mSales;
	}
	public Float getPreY2mSales() {
		return preY2mSales;
	}
	public void setPreY2mSales(Float preY2mSales) {
		this.preY2mSales = preY2mSales;
	}
	public Float getPreY3mSales() {
		return preY3mSales;
	}
	public void setPreY3mSales(Float preY3mSales) {
		this.preY3mSales = preY3mSales;
	}
	public Float getPreY4mSales() {
		return preY4mSales;
	}
	public void setPreY4mSales(Float preY4mSales) {
		this.preY4mSales = preY4mSales;
	}
	public Float getPreY5mSales() {
		return preY5mSales;
	}
	public void setPreY5mSales(Float preY5mSales) {
		this.preY5mSales = preY5mSales;
	}
	public Float getPreY6mSales() {
		return preY6mSales;
	}
	public void setPreY6mSales(Float preY6mSales) {
		this.preY6mSales = preY6mSales;
	}
	public Float getPreY7mSales() {
		return preY7mSales;
	}
	public void setPreY7mSales(Float preY7mSales) {
		this.preY7mSales = preY7mSales;
	}
	public Float getPreY8mSales() {
		return preY8mSales;
	}
	public void setPreY8mSales(Float preY8mSales) {
		this.preY8mSales = preY8mSales;
	}
	public Float getPreY9mSales() {
		return preY9mSales;
	}
	public void setPreY9mSales(Float preY9mSales) {
		this.preY9mSales = preY9mSales;
	}
	public Float getPreY10mSales() {
		return preY10mSales;
	}
	public void setPreY10mSales(Float preY10mSales) {
		this.preY10mSales = preY10mSales;
	}
	public Float getPreY11mSales() {
		return preY11mSales;
	}
	public void setPreY11mSales(Float preY11mSales) {
		this.preY11mSales = preY11mSales;
	}
	public Float getPreY12mSales() {
		return preY12mSales;
	}
	public void setPreY12mSales(Float preY12mSales) {
		this.preY12mSales = preY12mSales;
	}
	public Float getCurrY1mSales() {
		return currY1mSales;
	}
	public void setCurrY1mSales(Float currY1mSales) {
		this.currY1mSales = currY1mSales;
	}
	public Float getCurrY2mSales() {
		return currY2mSales;
	}
	public void setCurrY2mSales(Float currY2mSales) {
		this.currY2mSales = currY2mSales;
	}
	public Float getCurrY3mSales() {
		return currY3mSales;
	}
	public void setCurrY3mSales(Float currY3mSales) {
		this.currY3mSales = currY3mSales;
	}
	public Float getCurrY4mSales() {
		return currY4mSales;
	}
	public void setCurrY4mSales(Float currY4mSales) {
		this.currY4mSales = currY4mSales;
	}
	public Float getCurrY5mSales() {
		return currY5mSales;
	}
	public void setCurrY5mSales(Float currY5mSales) {
		this.currY5mSales = currY5mSales;
	}
	public Float getCurrY6mSales() {
		return currY6mSales;
	}
	public void setCurrY6mSales(Float currY6mSales) {
		this.currY6mSales = currY6mSales;
	}
	public Float getCurrY7mSales() {
		return currY7mSales;
	}
	public void setCurrY7mSales(Float currY7mSales) {
		this.currY7mSales = currY7mSales;
	}
	public Float getCurrY8mSales() {
		return currY8mSales;
	}
	public void setCurrY8mSales(Float currY8mSales) {
		this.currY8mSales = currY8mSales;
	}
	public Float getCurrY9mSales() {
		return currY9mSales;
	}
	public void setCurrY9mSales(Float currY9mSales) {
		this.currY9mSales = currY9mSales;
	}
	public Float getCurrY10mSales() {
		return currY10mSales;
	}
	public void setCurrY10mSales(Float currY10mSales) {
		this.currY10mSales = currY10mSales;
	}
	public Float getCurrY11mSales() {
		return currY11mSales;
	}
	public void setCurrY11mSales(Float currY11mSales) {
		this.currY11mSales = currY11mSales;
	}
	public Float getCurrY12mSales() {
		return currY12mSales;
	}
	public void setCurrY12mSales(Float currY12mSales) {
		this.currY12mSales = currY12mSales;
	}
	public Float getNationStock1() {
		return nationStock1;
	}
	public void setNationStock1(Float nationStock1) {
		this.nationStock1 = nationStock1;
	}
	public Float getNationStock2() {
		return nationStock2;
	}
	public void setNationStock2(Float nationStock2) {
		this.nationStock2 = nationStock2;
	}
	public Float getPadPlanQty() {
		return padPlanQty;
	}
	public void setPadPlanQty(Float padPlanQty) {
		this.padPlanQty = padPlanQty;
	}
	public Float getPadActQty() {
		return padActQty;
	}
	public void setPadActQty(Float padActQty) {
		this.padActQty = padActQty;
	}
	public Float getSaleForeQty() {
		return saleForeQty;
	}
	public void setSaleForeQty(Float saleForeQty) {
		this.saleForeQty = saleForeQty;
	}
	public Float getSaleForeActQty() {
		return saleForeActQty;
	}
	public void setSaleForeActQty(Float saleForeActQty) {
		this.saleForeActQty = saleForeActQty;
	}
	public Float getTurnOverDays() {
		return turnOverDays;
	}
	public void setTurnOverDays(Float turnOverDays) {
		this.turnOverDays = turnOverDays;
	}
	public String getProcAchRate() {
		return procAchRate;
	}
	public void setProcAchRate(String procAchRate) {
		this.procAchRate = procAchRate;
	}
	public String getSalesAchRate() {
		return salesAchRate;
	}
	public void setSalesAchRate(String salesAchRate) {
		this.salesAchRate = salesAchRate;
	}
	public String getProdSeriesCode() {
		return prodSeriesCode;
	}
	public void setProdSeriesCode(String prodSeriesCode) {
		this.prodSeriesCode = prodSeriesCode;
	}
	public String getBigItemExpl() {
		return bigItemExpl;
	}
	public void setBigItemExpl(String bigItemExpl) {
		this.bigItemExpl = bigItemExpl;
	}
	public Float getNext1Plan() {
		return next1Plan;
	}
	public void setNext1Plan(Float next1Plan) {
		this.next1Plan = next1Plan;
	}
	public Float getNext2Plan() {
		return next2Plan;
	}
	public void setNext2Plan(Float next2Plan) {
		this.next2Plan = next2Plan;
	}
	public Float getNext3Plan() {
		return next3Plan;
	}
	public void setNext3Plan(Float next3Plan) {
		this.next3Plan = next3Plan;
	}
	public Float getNext4Plan() {
		return next4Plan;
	}
	public void setNext4Plan(Float next4Plan) {
		this.next4Plan = next4Plan;
	}
	public Float getNext5Plan() {
		return next5Plan;
	}
	public void setNext5Plan(Float next5Plan) {
		this.next5Plan = next5Plan;
	}
	public Float getNext6Plan() {
		return next6Plan;
	}
	public void setNext6Plan(Float next6Plan) {
		this.next6Plan = next6Plan;
	}
	public Float getNext7Plan() {
		return next7Plan;
	}
	public void setNext7Plan(Float next7Plan) {
		this.next7Plan = next7Plan;
	}
	public Float getNext8Plan() {
		return next8Plan;
	}
	public void setNext8Plan(Float next8Plan) {
		this.next8Plan = next8Plan;
	}
	public Float getNext9Plan() {
		return next9Plan;
	}
	public void setNext9Plan(Float next9Plan) {
		this.next9Plan = next9Plan;
	}
	public Float getNext10Plan() {
		return next10Plan;
	}
	public void setNext10Plan(Float next10Plan) {
		this.next10Plan = next10Plan;
	}
	public Float getNext11Plan() {
		return next11Plan;
	}
	public void setNext11Plan(Float next11Plan) {
		this.next11Plan = next11Plan;
	}
	public Float getNext12Plan() {
		return next12Plan;
	}
	public void setNext12Plan(Float next12Plan) {
		this.next12Plan = next12Plan;
	}
	public Float getNext1SalesF() {
		return next1SalesF;
	}
	public void setNext1SalesF(Float next1SalesF) {
		this.next1SalesF = next1SalesF;
	}
	public Float getNext2SalesF() {
		return next2SalesF;
	}
	public void setNext2SalesF(Float next2SalesF) {
		this.next2SalesF = next2SalesF;
	}
	public Float getNext3SalesF() {
		return next3SalesF;
	}
	public void setNext3SalesF(Float next3SalesF) {
		this.next3SalesF = next3SalesF;
	}
	public Float getNext4SalesF() {
		return next4SalesF;
	}
	public void setNext4SalesF(Float next4SalesF) {
		this.next4SalesF = next4SalesF;
	}
	public Float getNext5SalesF() {
		return next5SalesF;
	}
	public void setNext5SalesF(Float next5SalesF) {
		this.next5SalesF = next5SalesF;
	}
	public Float getNext6SalesF() {
		return next6SalesF;
	}
	public void setNext6SalesF(Float next6SalesF) {
		this.next6SalesF = next6SalesF;
	}
	public Float getNext7SalesF() {
		return next7SalesF;
	}
	public void setNext7SalesF(Float next7SalesF) {
		this.next7SalesF = next7SalesF;
	}
	public Float getNext8SalesF() {
		return next8SalesF;
	}
	public void setNext8SalesF(Float next8SalesF) {
		this.next8SalesF = next8SalesF;
	}
	public Float getNext9SalesF() {
		return next9SalesF;
	}
	public void setNext9SalesF(Float next9SalesF) {
		this.next9SalesF = next9SalesF;
	}
	public Float getNext10SalesF() {
		return next10SalesF;
	}
	public void setNext10SalesF(Float next10SalesF) {
		this.next10SalesF = next10SalesF;
	}
	public Float getNext11SalesF() {
		return next11SalesF;
	}
	public void setNext11SalesF(Float next11SalesF) {
		this.next11SalesF = next11SalesF;
	}
	public Float getNext12SalesF() {
		return next12SalesF;
	}
	public void setNext12SalesF(Float next12SalesF) {
		this.next12SalesF = next12SalesF;
	}
	public Float getPre1Stock() {
		return pre1Stock;
	}
	public void setPre1Stock(Float pre1Stock) {
		this.pre1Stock = pre1Stock;
	}
	public Float getPre2Stock() {
		return pre2Stock;
	}
	public void setPre2Stock(Float pre2Stock) {
		this.pre2Stock = pre2Stock;
	}
	public Float getPre3Stock() {
		return pre3Stock;
	}
	public void setPre3Stock(Float pre3Stock) {
		this.pre3Stock = pre3Stock;
	}
	public Float getPre4Stock() {
		return pre4Stock;
	}
	public void setPre4Stock(Float pre4Stock) {
		this.pre4Stock = pre4Stock;
	}
	public Float getPre5Stock() {
		return pre5Stock;
	}
	public void setPre5Stock(Float pre5Stock) {
		this.pre5Stock = pre5Stock;
	}
	public Float getPre6Stock() {
		return pre6Stock;
	}
	public void setPre6Stock(Float pre6Stock) {
		this.pre6Stock = pre6Stock;
	}
	public Float getPre7Stock() {
		return pre7Stock;
	}
	public void setPre7Stock(Float pre7Stock) {
		this.pre7Stock = pre7Stock;
	}
	public Float getPre8Stock() {
		return pre8Stock;
	}
	public void setPre8Stock(Float pre8Stock) {
		this.pre8Stock = pre8Stock;
	}
	public Float getPre9Stock() {
		return pre9Stock;
	}
	public void setPre9Stock(Float pre9Stock) {
		this.pre9Stock = pre9Stock;
	}
	public Float getPre10Stock() {
		return pre10Stock;
	}
	public void setPre10Stock(Float pre10Stock) {
		this.pre10Stock = pre10Stock;
	}
	public Float getPre11Stock() {
		return pre11Stock;
	}
	public void setPre11Stock(Float pre11Stock) {
		this.pre11Stock = pre11Stock;
	}
	public Float getPre12Stock() {
		return pre12Stock;
	}
	public void setPre12Stock(Float pre12Stock) {
		this.pre12Stock = pre12Stock;
	}
	public Float getTod1() {
		return tod1;
	}
	public void setTod1(Float tod1) {
		this.tod1 = tod1;
	}
	public Float getTod2() {
		return tod2;
	}
	public void setTod2(Float tod2) {
		this.tod2 = tod2;
	}
	public Float getTod3() {
		return tod3;
	}
	public void setTod3(Float tod3) {
		this.tod3 = tod3;
	}
	public Float getTod4() {
		return tod4;
	}
	public void setTod4(Float tod4) {
		this.tod4 = tod4;
	}
	public Float getTod5() {
		return tod5;
	}
	public void setTod5(Float tod5) {
		this.tod5 = tod5;
	}
	public Float getTod6() {
		return tod6;
	}
	public void setTod6(Float tod6) {
		this.tod6 = tod6;
	}
	public Float getTod7() {
		return tod7;
	}
	public void setTod7(Float tod7) {
		this.tod7 = tod7;
	}
	public Float getTod8() {
		return tod8;
	}
	public void setTod8(Float tod8) {
		this.tod8 = tod8;
	}
	public Float getTod9() {
		return tod9;
	}
	public void setTod9(Float tod9) {
		this.tod9 = tod9;
	}
	public Float getTod10() {
		return tod10;
	}
	public void setTod10(Float tod10) {
		this.tod10 = tod10;
	}
	public Float getTod11() {
		return tod11;
	}
	public void setTod11(Float tod11) {
		this.tod11 = tod11;
	}
	public Float getTod12() {
		return tod12;
	}
	public void setTod12(Float tod12) {
		this.tod12 = tod12;
	}
	public Float getEstDeliQty() {
		return estDeliQty;
	}
	public void setEstDeliQty(Float estDeliQty) {
		this.estDeliQty = estDeliQty;
	}
	public Float getEstSaleQty() {
		return estSaleQty;
	}
	public void setEstSaleQty(Float estSaleQty) {
		this.estSaleQty = estSaleQty;
	}
	public Float getActSaleQty() {
		return actSaleQty;
	}
	public void setActSaleQty(Float actSaleQty) {
		this.actSaleQty = actSaleQty;
	}
}
