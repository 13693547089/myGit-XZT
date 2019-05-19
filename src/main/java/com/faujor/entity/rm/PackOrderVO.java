package com.faujor.entity.rm;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackOrderVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date orderDate;//订单日期
	private String packOrderCode;//包材订单编号
	private String oemSapId;//OEM供应商SAP编码
	private String oemSuppId;//OEM供应商编码
	private String oemSuppName;//OEM供应商名称
	private String mateCode;//半成品物料编码
	private String mateName;//半成品物料名称
	private String packCode;//包材编码
	private String packName;//包材名称
	private Float pruchNum;//采购数量
	
	
	private String baoSapId;//包材供应商SAP编码
	private String baoSuppId;//包材供应商编码
	private String baoSuppName;//包材供应商名称
	private Float packOrderNum;//包材订单数量
	private Float completeNum;//已完成数量
	private Float residueNum;//剩余数量
	private Float invenNum;//库存数量
	
	/**
	 * @return the packOrderCode
	 */
	public String getPackOrderCode() {
		return packOrderCode;
	}
	/**
	 * @param packOrderCode the packOrderCode to set
	 */
	public void setPackOrderCode(String packOrderCode) {
		this.packOrderCode = packOrderCode;
	}
	/**
	 * @return the mateCode
	 */
	public String getMateCode() {
		return mateCode;
	}
	/**
	 * @param mateCode the mateCode to set
	 */
	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}
	/**
	 * @return the mateName
	 */
	public String getMateName() {
		return mateName;
	}
	/**
	 * @param mateName the mateName to set
	 */
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	
	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the packCode
	 */
	public String getPackCode() {
		return packCode;
	}
	/**
	 * @param packCode the packCode to set
	 */
	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	/**
	 * @return the packName
	 */
	public String getPackName() {
		return packName;
	}
	/**
	 * @param packName the packName to set
	 */
	public void setPackName(String packName) {
		this.packName = packName;
	}
	/**
	 * @return the oemSapId
	 */
	public String getOemSapId() {
		return oemSapId;
	}
	/**
	 * @param oemSapId the oemSapId to set
	 */
	public void setOemSapId(String oemSapId) {
		this.oemSapId = oemSapId;
	}
	/**
	 * @return the oemSuppName
	 */
	public String getOemSuppName() {
		return oemSuppName;
	}
	/**
	 * @param oemSuppName the oemSuppName to set
	 */
	public void setOemSuppName(String oemSuppName) {
		this.oemSuppName = oemSuppName;
	}
	/**
	 * @return the baoSapId
	 */
	public String getBaoSapId() {
		return baoSapId;
	}
	/**
	 * @param baoSapId the baoSapId to set
	 */
	public void setBaoSapId(String baoSapId) {
		this.baoSapId = baoSapId;
	}
	/**
	 * @return the baoSuppName
	 */
	public String getBaoSuppName() {
		return baoSuppName;
	}
	/**
	 * @param baoSuppName the baoSuppName to set
	 */
	public void setBaoSuppName(String baoSuppName) {
		this.baoSuppName = baoSuppName;
	}
	/**
	 * @return the oemSuppId
	 */
	public String getOemSuppId() {
		return oemSuppId;
	}
	/**
	 * @param oemSuppId the oemSuppId to set
	 */
	public void setOemSuppId(String oemSuppId) {
		this.oemSuppId = oemSuppId;
	}
	/**
	 * @return the pruchNum
	 */
	public Float getPruchNum() {
		return pruchNum;
	}
	/**
	 * @param pruchNum the pruchNum to set
	 */
	public void setPruchNum(Float pruchNum) {
		this.pruchNum = pruchNum;
	}
	/**
	 * @return the baoSuppId
	 */
	public String getBaoSuppId() {
		return baoSuppId;
	}
	/**
	 * @param baoSuppId the baoSuppId to set
	 */
	public void setBaoSuppId(String baoSuppId) {
		this.baoSuppId = baoSuppId;
	}
	/**
	 * @return the packOrderNum
	 */
	public Float getPackOrderNum() {
		return packOrderNum;
	}
	/**
	 * @param packOrderNum the packOrderNum to set
	 */
	public void setPackOrderNum(Float packOrderNum) {
		this.packOrderNum = packOrderNum;
	}
	/**
	 * @return the completeNum
	 */
	public Float getCompleteNum() {
		return completeNum;
	}
	/**
	 * @param completeNum the completeNum to set
	 */
	public void setCompleteNum(Float completeNum) {
		this.completeNum = completeNum;
	}
	/**
	 * @return the residueNum
	 */
	public Float getResidueNum() {
		return residueNum;
	}
	/**
	 * @param residueNum the residueNum to set
	 */
	public void setResidueNum(Float residueNum) {
		this.residueNum = residueNum;
	}
	/**
	 * @return the invenNum
	 */
	public Float getInvenNum() {
		return invenNum;
	}
	/**
	 * @param invenNum the invenNum to set
	 */
	public void setInvenNum(Float invenNum) {
		this.invenNum = invenNum;
	}
	
	
	
	
	
}
