package com.faujor.entity.rm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CutMatePack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cutMonth;		//打切月份
	private String mateCode;		//物料编码--料号
	private String mateName;		//物料名称--品名
	private String version;		    //物料版本
	private Integer boxNumber;		//物料箱入数
	private Double sumOutNum;//总订单在外量
	private Double outNum;//订单打切在外量
	private Double sumInveNum;//总成品库存
	private Double inveNum;//打切成品库存
	private String fields;//OEM打切联络单填写的物料具体数据
	private String headerFields;//OEM打切联络单物料列的抬头信息
	private String baoFields;//包材打切联络单填写的物料具体数据
	private String oemSuppId;		//OEM供应商编码
	private String oemSapId;		//OEM供应商SAP编码
	private String oemSuppName;		//OEM供应商名称
	private String liaiId;		//OEM打切联络单的主键
	private String baoLiaiId;		//包材打切联络单的主键
	private String baoSuppId;	//	包材供应商编码
	private String baoSapId;	//	包材供应商SAP编码
	private String baoSuppName;	//	包材供应商名称
	/**
	 * @return the cutMonth
	 */
	public String getCutMonth() {
		return cutMonth;
	}
	/**
	 * @param cutMonth the cutMonth to set
	 */
	public void setCutMonth(String cutMonth) {
		this.cutMonth = cutMonth;
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the boxNumber
	 */
	public Integer getBoxNumber() {
		return boxNumber;
	}
	/**
	 * @param boxNumber the boxNumber to set
	 */
	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
	}
	/**
	 * @return the sumOutNum
	 */
	public Double getSumOutNum() {
		return sumOutNum;
	}
	/**
	 * @param sumOutNum the sumOutNum to set
	 */
	public void setSumOutNum(Double sumOutNum) {
		this.sumOutNum = sumOutNum;
	}
	/**
	 * @return the outNum
	 */
	public Double getOutNum() {
		return outNum;
	}
	/**
	 * @param outNum the outNum to set
	 */
	public void setOutNum(Double outNum) {
		this.outNum = outNum;
	}
	/**
	 * @return the sumInveNum
	 */
	public Double getSumInveNum() {
		return sumInveNum;
	}
	/**
	 * @param sumInveNum the sumInveNum to set
	 */
	public void setSumInveNum(Double sumInveNum) {
		this.sumInveNum = sumInveNum;
	}
	/**
	 * @return the inveNum
	 */
	public Double getInveNum() {
		return inveNum;
	}
	/**
	 * @param inveNum the inveNum to set
	 */
	public void setInveNum(Double inveNum) {
		this.inveNum = inveNum;
	}
	/**
	 * @return the fields
	 */
	public String getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}
	/**
	 * @return the headerFields
	 */
	public String getHeaderFields() {
		return headerFields;
	}
	/**
	 * @param headerFields the headerFields to set
	 */
	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}
	/**
	 * @return the baoFields
	 */
	public String getBaoFields() {
		return baoFields;
	}
	/**
	 * @param baoFields the baoFields to set
	 */
	public void setBaoFields(String baoFields) {
		this.baoFields = baoFields;
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
	 * @return the liaiId
	 */
	public String getLiaiId() {
		return liaiId;
	}
	/**
	 * @param liaiId the liaiId to set
	 */
	public void setLiaiId(String liaiId) {
		this.liaiId = liaiId;
	}
	/**
	 * @return the baoLiaiId
	 */
	public String getBaoLiaiId() {
		return baoLiaiId;
	}
	/**
	 * @param baoLiaiId the baoLiaiId to set
	 */
	public void setBaoLiaiId(String baoLiaiId) {
		this.baoLiaiId = baoLiaiId;
	}
	
	
	
	

	
}
