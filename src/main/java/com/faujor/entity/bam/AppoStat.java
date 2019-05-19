package com.faujor.entity.bam;

import java.io.Serializable;

public class AppoStat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer appoNumber;            //本日预约数
	private String appoAmount;            //本日预约方量
	private Integer confAppoNum;			//已确认预约数
	private Integer unconfAppoNum;		   //未确认预约数
	private String confAppoAmo;			//已确认预约数的总方量
	private String unconfAppoAmo;		   //未确认预约数的总方量
	private Integer publAppoNum;			//已发布预约数
	private Integer unpublAppoNum;			//未发布预约数
	private String publAppoAmo;		    //已发布预约数的总方量
	private String unpublAppoAmo;			//未发布预约数的总方量
	private Integer eighAppoNum;			//8:00 - 10:00 时段的预约数
	private String eighAppoAmo;		    //8:00 - 10:00 时段的预约数的总方量
	private Integer tenAppoNum;			    //10:00 - 13:00 时段的预约数
	private String tenAppoAmo;			    //10:00 - 13:00 时段的预约数的总方量
	private Integer thirAppoNum;			//13:00 - 15:00 时段的预约数
	private String thirAppoAmo;			//13:00 - 15:00 时段的预约数的总方量
	private Integer fiftAppoNum;			//15:00 - 16:00 时段的预约数
	private String fiftAppoAmo;			//15:00 - 16:00 时段的预约数的总方量
	private Integer sixAppoNum;			   //16:00 -时段的预约数
	private String sixAppoAmo;			   //16:00 -时段的预约数的总方量
	
	private String confAppo;
	private String unconfAppo;
	private String publAppo;
	private String unpublAppo;
	
	public AppoStat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppoStat(Integer appoNumber, String appoAmount, Integer confAppoNum, Integer unconfAppoNum,
			String confAppoAmo, String unconfAppoAmo, Integer publAppoNum, Integer unpublAppoNum, String publAppoAmo,
			String unpublAppoAmo, Integer eighAppoNum, String eighAppoAmo, Integer tenAppoNum, String tenAppoAmo,
			Integer thirAppoNum, String thirAppoAmo, Integer fiftAppoNum, String fiftAppoAmo, Integer sixAppoNum,
			String sixAppoAmo, String confAppo, String unconfAppo, String publAppo, String unpublAppo) {
		super();
		this.appoNumber = appoNumber;
		this.appoAmount = appoAmount;
		this.confAppoNum = confAppoNum;
		this.unconfAppoNum = unconfAppoNum;
		this.confAppoAmo = confAppoAmo;
		this.unconfAppoAmo = unconfAppoAmo;
		this.publAppoNum = publAppoNum;
		this.unpublAppoNum = unpublAppoNum;
		this.publAppoAmo = publAppoAmo;
		this.unpublAppoAmo = unpublAppoAmo;
		this.eighAppoNum = eighAppoNum;
		this.eighAppoAmo = eighAppoAmo;
		this.tenAppoNum = tenAppoNum;
		this.tenAppoAmo = tenAppoAmo;
		this.thirAppoNum = thirAppoNum;
		this.thirAppoAmo = thirAppoAmo;
		this.fiftAppoNum = fiftAppoNum;
		this.fiftAppoAmo = fiftAppoAmo;
		this.sixAppoNum = sixAppoNum;
		this.sixAppoAmo = sixAppoAmo;
		this.confAppo = confAppo;
		this.unconfAppo = unconfAppo;
		this.publAppo = publAppo;
		this.unpublAppo = unpublAppo;
	}

	/**
	 * @return the appoNumber
	 */
	public Integer getAppoNumber() {
		return appoNumber;
	}

	/**
	 * @param appoNumber the appoNumber to set
	 */
	public void setAppoNumber(Integer appoNumber) {
		this.appoNumber = appoNumber;
	}

	/**
	 * @return the appoAmount
	 */
	public String getAppoAmount() {
		return appoAmount;
	}

	/**
	 * @param appoAmount the appoAmount to set
	 */
	public void setAppoAmount(String appoAmount) {
		this.appoAmount = appoAmount;
	}

	/**
	 * @return the confAppoNum
	 */
	public Integer getConfAppoNum() {
		return confAppoNum;
	}

	/**
	 * @param confAppoNum the confAppoNum to set
	 */
	public void setConfAppoNum(Integer confAppoNum) {
		this.confAppoNum = confAppoNum;
	}

	/**
	 * @return the unconfAppoNum
	 */
	public Integer getUnconfAppoNum() {
		return unconfAppoNum;
	}

	/**
	 * @param unconfAppoNum the unconfAppoNum to set
	 */
	public void setUnconfAppoNum(Integer unconfAppoNum) {
		this.unconfAppoNum = unconfAppoNum;
	}

	/**
	 * @return the confAppoAmo
	 */
	public String getConfAppoAmo() {
		return confAppoAmo;
	}

	/**
	 * @param confAppoAmo the confAppoAmo to set
	 */
	public void setConfAppoAmo(String confAppoAmo) {
		this.confAppoAmo = confAppoAmo;
	}

	/**
	 * @return the unconfAppoAmo
	 */
	public String getUnconfAppoAmo() {
		return unconfAppoAmo;
	}

	/**
	 * @param unconfAppoAmo the unconfAppoAmo to set
	 */
	public void setUnconfAppoAmo(String unconfAppoAmo) {
		this.unconfAppoAmo = unconfAppoAmo;
	}

	/**
	 * @return the publAppoNum
	 */
	public Integer getPublAppoNum() {
		return publAppoNum;
	}

	/**
	 * @param publAppoNum the publAppoNum to set
	 */
	public void setPublAppoNum(Integer publAppoNum) {
		this.publAppoNum = publAppoNum;
	}

	/**
	 * @return the unpublAppoNum
	 */
	public Integer getUnpublAppoNum() {
		return unpublAppoNum;
	}

	/**
	 * @param unpublAppoNum the unpublAppoNum to set
	 */
	public void setUnpublAppoNum(Integer unpublAppoNum) {
		this.unpublAppoNum = unpublAppoNum;
	}

	/**
	 * @return the publAppoAmo
	 */
	public String getPublAppoAmo() {
		return publAppoAmo;
	}

	/**
	 * @param publAppoAmo the publAppoAmo to set
	 */
	public void setPublAppoAmo(String publAppoAmo) {
		this.publAppoAmo = publAppoAmo;
	}

	/**
	 * @return the unpublAppoAmo
	 */
	public String getUnpublAppoAmo() {
		return unpublAppoAmo;
	}

	/**
	 * @param unpublAppoAmo the unpublAppoAmo to set
	 */
	public void setUnpublAppoAmo(String unpublAppoAmo) {
		this.unpublAppoAmo = unpublAppoAmo;
	}

	/**
	 * @return the eighAppoNum
	 */
	public Integer getEighAppoNum() {
		return eighAppoNum;
	}

	/**
	 * @param eighAppoNum the eighAppoNum to set
	 */
	public void setEighAppoNum(Integer eighAppoNum) {
		this.eighAppoNum = eighAppoNum;
	}

	/**
	 * @return the eighAppoAmo
	 */
	public String getEighAppoAmo() {
		return eighAppoAmo;
	}

	/**
	 * @param eighAppoAmo the eighAppoAmo to set
	 */
	public void setEighAppoAmo(String eighAppoAmo) {
		this.eighAppoAmo = eighAppoAmo;
	}

	/**
	 * @return the tenAppoNum
	 */
	public Integer getTenAppoNum() {
		return tenAppoNum;
	}

	/**
	 * @param tenAppoNum the tenAppoNum to set
	 */
	public void setTenAppoNum(Integer tenAppoNum) {
		this.tenAppoNum = tenAppoNum;
	}

	/**
	 * @return the tenAppoAmo
	 */
	public String getTenAppoAmo() {
		return tenAppoAmo;
	}

	/**
	 * @param tenAppoAmo the tenAppoAmo to set
	 */
	public void setTenAppoAmo(String tenAppoAmo) {
		this.tenAppoAmo = tenAppoAmo;
	}

	/**
	 * @return the thirAppoNum
	 */
	public Integer getThirAppoNum() {
		return thirAppoNum;
	}

	/**
	 * @param thirAppoNum the thirAppoNum to set
	 */
	public void setThirAppoNum(Integer thirAppoNum) {
		this.thirAppoNum = thirAppoNum;
	}

	/**
	 * @return the thirAppoAmo
	 */
	public String getThirAppoAmo() {
		return thirAppoAmo;
	}

	/**
	 * @param thirAppoAmo the thirAppoAmo to set
	 */
	public void setThirAppoAmo(String thirAppoAmo) {
		this.thirAppoAmo = thirAppoAmo;
	}

	/**
	 * @return the fiftAppoNum
	 */
	public Integer getFiftAppoNum() {
		return fiftAppoNum;
	}

	/**
	 * @param fiftAppoNum the fiftAppoNum to set
	 */
	public void setFiftAppoNum(Integer fiftAppoNum) {
		this.fiftAppoNum = fiftAppoNum;
	}

	/**
	 * @return the fiftAppoAmo
	 */
	public String getFiftAppoAmo() {
		return fiftAppoAmo;
	}

	/**
	 * @param fiftAppoAmo the fiftAppoAmo to set
	 */
	public void setFiftAppoAmo(String fiftAppoAmo) {
		this.fiftAppoAmo = fiftAppoAmo;
	}

	/**
	 * @return the sixAppoNum
	 */
	public Integer getSixAppoNum() {
		return sixAppoNum;
	}

	/**
	 * @param sixAppoNum the sixAppoNum to set
	 */
	public void setSixAppoNum(Integer sixAppoNum) {
		this.sixAppoNum = sixAppoNum;
	}

	/**
	 * @return the sixAppoAmo
	 */
	public String getSixAppoAmo() {
		return sixAppoAmo;
	}

	/**
	 * @param sixAppoAmo the sixAppoAmo to set
	 */
	public void setSixAppoAmo(String sixAppoAmo) {
		this.sixAppoAmo = sixAppoAmo;
	}

	/**
	 * @return the confAppo
	 */
	public String getConfAppo() {
		return confAppo;
	}

	/**
	 * @param confAppo the confAppo to set
	 */
	public void setConfAppo(String confAppo) {
		this.confAppo = confAppo;
	}

	/**
	 * @return the unconfAppo
	 */
	public String getUnconfAppo() {
		return unconfAppo;
	}

	/**
	 * @param unconfAppo the unconfAppo to set
	 */
	public void setUnconfAppo(String unconfAppo) {
		this.unconfAppo = unconfAppo;
	}

	/**
	 * @return the publAppo
	 */
	public String getPublAppo() {
		return publAppo;
	}

	/**
	 * @param publAppo the publAppo to set
	 */
	public void setPublAppo(String publAppo) {
		this.publAppo = publAppo;
	}

	/**
	 * @return the unpublAppo
	 */
	public String getUnpublAppo() {
		return unpublAppo;
	}

	/**
	 * @param unpublAppo the unpublAppo to set
	 */
	public void setUnpublAppo(String unpublAppo) {
		this.unpublAppo = unpublAppo;
	}

}
