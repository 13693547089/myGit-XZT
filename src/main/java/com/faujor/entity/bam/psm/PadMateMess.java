package com.faujor.entity.bam.psm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PadMateMess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String planCode;
	private String planMonth;
	private Float padPlanQty; //交货计划值
	private Float turnOverDays;	// 周转天数
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
	 * @return the planCode
	 */
	public String getPlanCode() {
		return planCode;
	}
	/**
	 * @param planCode the planCode to set
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	/**
	 * @return the planMonth
	 */
	public String getPlanMonth() {
		return planMonth;
	}
	/**
	 * @param planMonth the planMonth to set
	 */
	public void setPlanMonth(String planMonth) {
		this.planMonth = planMonth;
	}
	/**
	 * @return the padPlanQty
	 */
	public Float getPadPlanQty() {
		return padPlanQty;
	}
	/**
	 * @param padPlanQty the padPlanQty to set
	 */
	public void setPadPlanQty(Float padPlanQty) {
		this.padPlanQty = padPlanQty;
	}
	/**
	 * @return the turnOverDays
	 */
	public Float getTurnOverDays() {
		return turnOverDays;
	}
	/**
	 * @param turnOverDays the turnOverDays to set
	 */
	public void setTurnOverDays(Float turnOverDays) {
		this.turnOverDays = turnOverDays;
	}
	
}
