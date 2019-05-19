package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.faujor.entity.mdm.Material;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class CutProductDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String prodId;// VARCHAR2(50) NOT NULL,
	private String createId;// VARCHAR2(50),
	private String creator;// VARCHAR2(50),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;// DATE
	private String version;//版本
	private String cutAim;//打切目的
	private String mainStru;//主包材
	private String mateId;// VARCHAR2(50),
	private String mateCode;//成品物料名称（打切品中的物料是成品物料，mateCode就是成品物料编码）
	private String mateName;//物料名称
	private String mateGroupExpl;//物料组说明
	private String mateType;//物料类型
	private String procUnit;//采购单位
	private String basicUnit;//基本单位
	private String isSpecial;//是否为特殊自制品
	private String isHaveSeim;//是否有半成品
	
	public CutProductDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public CutProductDO(String prodId, String createId, String creator, Date createDate, String version, String cutAim,
			String mainStru, String mateId, String mateCode, String mateName, String mateGroupExpl, String mateType,
			String procUnit, String basicUnit, String isSpecial, String isHaveSeim) {
		super();
		this.prodId = prodId;
		this.createId = createId;
		this.creator = creator;
		this.createDate = createDate;
		this.version = version;
		this.cutAim = cutAim;
		this.mainStru = mainStru;
		this.mateId = mateId;
		this.mateCode = mateCode;
		this.mateName = mateName;
		this.mateGroupExpl = mateGroupExpl;
		this.mateType = mateType;
		this.procUnit = procUnit;
		this.basicUnit = basicUnit;
		this.isSpecial = isSpecial;
		this.isHaveSeim = isHaveSeim;
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
	 * @return the mateId
	 */
	public String getMateId() {
		return mateId;
	}
	/**
	 * @param mateId the mateId to set
	 */
	public void setMateId(String mateId) {
		this.mateId = mateId;
	}
	/**
	 * @return the createId
	 */
	public String getCreateId() {
		return createId;
	}
	/**
	 * @param createId the createId to set
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @return the mateGroupExpl
	 */
	public String getMateGroupExpl() {
		return mateGroupExpl;
	}

	/**
	 * @param mateGroupExpl the mateGroupExpl to set
	 */
	public void setMateGroupExpl(String mateGroupExpl) {
		this.mateGroupExpl = mateGroupExpl;
	}

	/**
	 * @return the mateType
	 */
	public String getMateType() {
		return mateType;
	}

	/**
	 * @param mateType the mateType to set
	 */
	public void setMateType(String mateType) {
		this.mateType = mateType;
	}

	/**
	 * @return the procUnit
	 */
	public String getProcUnit() {
		return procUnit;
	}

	/**
	 * @param procUnit the procUnit to set
	 */
	public void setProcUnit(String procUnit) {
		this.procUnit = procUnit;
	}

	/**
	 * @return the basicUnit
	 */
	public String getBasicUnit() {
		return basicUnit;
	}

	/**
	 * @param basicUnit the basicUnit to set
	 */
	public void setBasicUnit(String basicUnit) {
		this.basicUnit = basicUnit;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutProductDO [prodId=" + prodId + ", createId=" + createId + ", creator=" + creator + ", createDate="
				+ createDate + ", version=" + version + ", cutAim=" + cutAim + ", mainStru=" + mainStru + ", mateId="
				+ mateId + ", mateCode=" + mateCode + ", mateName=" + mateName + ", mateGroupExpl=" + mateGroupExpl
				+ ", mateType=" + mateType + ", procUnit=" + procUnit + ", basicUnit=" + basicUnit + ", isSpecial="
				+ isSpecial + ", isHaveSeim=" + isHaveSeim + "]";
	}

	

	
}
