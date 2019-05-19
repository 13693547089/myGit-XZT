package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class CutLiaiMate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String liaiMateId;// VARCHAR2(50) NOT NULL,
	private String  liaiId;// VARCHAR2(50),
	private String  mateCode;// VARCHAR2(50),
	private String mateName;// VARCHAR2(100),
	private Integer outNum;// NUMBER(10),订单在外量
	private Integer inveNum;// NUMBER(10),成品库存
	private Integer prodNum;// NUMBER(10),可生产订单
	private String  fields ;//VARCHAR2(4000)
	private Integer  boxNumber ;
	private String version;//物料版本
	private String cutAim;//物料的打切目的
	private String mainStru;//主包材
	private String isSpecial;//是否是特殊打切品
	private Double mainStruNum;//主包材数量
	private Double sumOutNum;//总订单在外量
	private Double sumInveNum;//总成品库存
	private Double sumProdNum;//总可生产订单
	private String  seriesCode;//系列编码
	private String  seriesExpl;//系列说明
	private Integer lastProdNum;//上月打切可生产订单
	private String remark;//备注
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
	 * @return the mainStruNum
	 */
	public Double getMainStruNum() {
		return mainStruNum;
	}

	/**
	 * @param mainStruNum the mainStruNum to set
	 */
	public void setMainStruNum(Double mainStruNum) {
		this.mainStruNum = mainStruNum;
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
	 * @return the liaiMateId
	 */
	public String getLiaiMateId() {
		return liaiMateId;
	}
	/**
	 * @param liaiMateId the liaiMateId to set
	 */
	public void setLiaiMateId(String liaiMateId) {
		this.liaiMateId = liaiMateId;
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
	 * @return the outNum
	 */
	public Integer getOutNum() {
		return outNum;
	}
	/**
	 * @param outNum the outNum to set
	 */
	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
	}
	/**
	 * @return the inveNum
	 */
	public Integer getInveNum() {
		return inveNum;
	}
	/**
	 * @param inveNum the inveNum to set
	 */
	public void setInveNum(Integer inveNum) {
		this.inveNum = inveNum;
	}
	/**
	 * @return the prodNum
	 */
	public Integer getProdNum() {
		return prodNum;
	}
	/**
	 * @param prodNum the prodNum to set
	 */
	public void setProdNum(Integer prodNum) {
		this.prodNum = prodNum;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutLiaiMate [liaiMateId=" + liaiMateId + ", liaiId=" + liaiId + ", mateCode=" + mateCode + ", mateName="
				+ mateName + ", outNum=" + outNum + ", inveNum=" + inveNum + ", prodNum=" + prodNum + ", fields="
				+ fields + ", boxNumber=" + boxNumber + ", version=" + version + ", cutAim=" + cutAim + ", mainStru="
				+ mainStru + ", isSpecial=" + isSpecial + ", mainStruNum=" + mainStruNum + ", sumOutNum=" + sumOutNum
				+ ", sumInveNum=" + sumInveNum + ", sumProdNum=" + sumProdNum + ", seriesCode=" + seriesCode
				+ ", seriesExpl=" + seriesExpl + ", lastProdNum=" + lastProdNum + ", remark=" + remark + "]";
	}





	

	

	
}
