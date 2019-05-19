package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fid; 
	private String contOrdeNumb;
	private Date subeDate;
	private String mateNumb;
	private Double unpaQuan;// 订单上的原始未交数量
	private Double calculNumber; // 计算得到的未交数量
	private String company;
	private String frequency;
	public OrderDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public OrderDO(String fid, String contOrdeNumb, Date subeDate, String mateNumb, Double unpaQuan,
			Double calculNumber, String company, String frequency) {
		super();
		this.fid = fid;
		this.contOrdeNumb = contOrdeNumb;
		this.subeDate = subeDate;
		this.mateNumb = mateNumb;
		this.unpaQuan = unpaQuan;
		this.calculNumber = calculNumber;
		this.company = company;
		this.frequency = frequency;
	}


	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the fid
	 */
	public String getFid() {
		return fid;
	}
	/**
	 * @param fid the fid to set
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}
	/**
	 * @return the contOrdeNumb
	 */
	public String getContOrdeNumb() {
		return contOrdeNumb;
	}
	/**
	 * @param contOrdeNumb the contOrdeNumb to set
	 */
	public void setContOrdeNumb(String contOrdeNumb) {
		this.contOrdeNumb = contOrdeNumb;
	}
	/**
	 * @return the subeDate
	 */
	public Date getSubeDate() {
		return subeDate;
	}
	/**
	 * @param subeDate the subeDate to set
	 */
	public void setSubeDate(Date subeDate) {
		this.subeDate = subeDate;
	}
	/**
	 * @return the mateNumb
	 */
	public String getMateNumb() {
		return mateNumb;
	}
	/**
	 * @param mateNumb the mateNumb to set
	 */
	public void setMateNumb(String mateNumb) {
		this.mateNumb = mateNumb;
	}
	
	/**
	 * @return the unpaQuan
	 */
	public Double getUnpaQuan() {
		return unpaQuan;
	}

	/**
	 * @param unpaQuan the unpaQuan to set
	 */
	public void setUnpaQuan(Double unpaQuan) {
		this.unpaQuan = unpaQuan;
	}

	/**
	 * @return the calculNumber
	 */
	public Double getCalculNumber() {
		return calculNumber;
	}

	/**
	 * @param calculNumber the calculNumber to set
	 */
	public void setCalculNumber(Double calculNumber) {
		this.calculNumber = calculNumber;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
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
		return "OrderDO [fid=" + fid + ", contOrdeNumb=" + contOrdeNumb + ", subeDate=" + subeDate + ", mateNumb="
				+ mateNumb + ", unpaQuan=" + unpaQuan + ", calculNumber=" + calculNumber + ", company=" + company
				+ ", frequency=" + frequency + "]";
	}
	
	
}
