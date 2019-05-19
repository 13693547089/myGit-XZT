package com.faujor.entity.bam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.faujor.entity.mdm.Material;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class CutProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String prodId;// VARCHAR2(50) NOT NULL,
	private String mateId;// VARCHAR2(50),
	private String createId;// VARCHAR2(50),
	private String creator;// VARCHAR2(50),
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;// DATE
	private String version;//版本
	private String cutAim;//打切目的
	private String mainStru;//主包材
	private String isSpecial;//是否为特殊自制品
	private String isHaveSeim;//是否有半成品
	public CutProduct() {
		super();
		// TODO Auto-generated constructor stub
	}

	



	public CutProduct(String prodId, String mateId, String createId, String creator, Date createDate, String version,
			String cutAim, String mainStru, String isSpecial, String isHaveSeim) {
		super();
		this.prodId = prodId;
		this.mateId = mateId;
		this.createId = createId;
		this.creator = creator;
		this.createDate = createDate;
		this.version = version;
		this.cutAim = cutAim;
		this.mainStru = mainStru;
		this.isSpecial = isSpecial;
		this.isHaveSeim = isHaveSeim;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutProduct [prodId=" + prodId + ", mateId=" + mateId + ", createId=" + createId + ", creator=" + creator
				+ ", createDate=" + createDate + ", version=" + version + ", cutAim=" + cutAim + ", mainStru="
				+ mainStru + ", isSpecial=" + isSpecial + ", isHaveSeim=" + isHaveSeim + "]";
	}

	
	
}
