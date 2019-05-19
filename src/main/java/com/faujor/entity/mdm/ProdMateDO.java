package com.faujor.entity.mdm;

import java.io.Serializable;
import java.util.List;

import com.faujor.entity.bam.CutBaoCaiMate;

public class ProdMateDO implements Serializable {
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
	private String version; //打切品版本
	private String prodId;
	private String cutAim;//打切目的
	private String mainStru;//主包材
	private String isSpecial;//是否为特殊自制品
	private String isHaveSeim;//是否有半成品
	private String mateInfo;
	private String mateGroupInfo;
	private String mateTypeInfo;
	private Double sumOutNum;//总订单在外量
	private Double sumInveNum;//总成品库存
	private Double sumProdNum;//总可生产订单
	private String seriesCode;//系列编码
	private String seriesExpl;//系列名称
	private Integer lastProdNum;//上月打切可生产订单
	private String suppCode;//供应商编码
	private String suppName;//供应商名称
	private String sapCode;//sap编码

	private List<CutBaoCaiMate> mateList;
	
	
	/**
	 * @return the mateList
	 */
	public List<CutBaoCaiMate> getMateList() {
		return mateList;
	}


	/**
	 * @param mateList the mateList to set
	 */
	public void setMateList(List<CutBaoCaiMate> mateList) {
		this.mateList = mateList;
	}

	/**
	 * @return the mainStru
	 */
	public String getMainStru() {
		return mainStru;
	}

	/**
	 * @param mainStru the mainStru to set
	 */
	public void setMainStru(String mainStru) {
		this.mainStru = mainStru;
	}

	/**
	 * @return the cutAim
	 */
	public String getCutAim() {
		return cutAim;
	}

	/**
	 * @param cutAim the cutAim to set
	 */
	public void setCutAim(String cutAim) {
		this.cutAim = cutAim;
	}

	/**
	 * @return the prodId
	 */
	public String getProdId() {
		return prodId;
	}

	/**
	 * @param prodId the prodId to set
	 */
	public void setProdId(String prodId) {
		this.prodId = prodId;
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
	 * @param boxNumber
	 *            the boxNumber to set
	 */
	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
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

	/**
	 * @return the mateInfo
	 */
	public String getMateInfo() {
		return mateInfo;
	}

	/**
	 * @param mateInfo the mateInfo to set
	 */
	public void setMateInfo(String mateInfo) {
		this.mateInfo = mateInfo;
	}

	/**
	 * @return the mateGroupInfo
	 */
	public String getMateGroupInfo() {
		return mateGroupInfo;
	}

	/**
	 * @param mateGroupInfo the mateGroupInfo to set
	 */
	public void setMateGroupInfo(String mateGroupInfo) {
		this.mateGroupInfo = mateGroupInfo;
	}

	/**
	 * @return the mateTypeInfo
	 */
	public String getMateTypeInfo() {
		return mateTypeInfo;
	}

	/**
	 * @param mateTypeInfo the mateTypeInfo to set
	 */
	public void setMateTypeInfo(String mateTypeInfo) {
		this.mateTypeInfo = mateTypeInfo;
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
	 * @return the sumProdNum
	 */
	public Double getSumProdNum() {
		return sumProdNum;
	}

	/**
	 * @param sumProdNum the sumProdNum to set
	 */
	public void setSumProdNum(Double sumProdNum) {
		this.sumProdNum = sumProdNum;
	}

	/**
	 * @return the seriesCode
	 */
	public String getSeriesCode() {
		return seriesCode;
	}

	/**
	 * @param seriesCode the seriesCode to set
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
	 * @param seriesExpl the seriesExpl to set
	 */
	public void setSeriesExpl(String seriesExpl) {
		this.seriesExpl = seriesExpl;
	}

	/**
	 * @return the lastProdNum
	 */
	public Integer getLastProdNum() {
		return lastProdNum;
	}

	/**
	 * @param lastProdNum the lastProdNum to set
	 */
	public void setLastProdNum(Integer lastProdNum) {
		this.lastProdNum = lastProdNum;
	}


	public String getSuppCode() {
		return suppCode;
	}

	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	
	
	
}
