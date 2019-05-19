package com.faujor.entity.mdm;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Material implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mateId;// VARCHAR2(50) NOT NULL, --编号
	private String mateCode;// VARCHAR2(20), --物料编码
	private String skuTiptop;// VARCHAR2(50), --SKU(原tiptop料号)
	private String skuEnglish;// VARCHAR2(50), --SKU英文
	private String chinName;// VARCHAR2(50), --料件（中文名称）
	private String mateType;// VARCHAR2(20), --物料类型
	private String mateTypeExpl;// VARCHAR2(20), --物料类型说明
	private String mateGroupCode;// VARCHAR2(20), --物料组代码
	private String mateGroupExpl;// VARCHAR2(20), --物料组说明
	private String barCode;// VARCHAR2(20), --条码
	private String barCodeEngl;// VARCHAR2(50), --条码英文简称
	private String barCodeChin;// VARCHAR2(50), --条码中文简称
	private String repeUnit;// VARCHAR2(10), --基本计量单位（库存单位）
	private Integer boxNumber;// INT, --箱入数
	private String finProMate;// VARCHAR2(20), --成品料
	private String finProSku;// VARCHAR2(50), --成品SKU
	private String abcIden;// VARCHAR2(10), --ABC标识
	private String busiCode;// VARCHAR2(10), --事业部代码
	private String busiExpl;// VARCHAR2(50), --事业部说明
	private String cateCode;// VARCHAR2(10), --品类代码
	private String cateExpl;// VARCHAR2(50), --品类说明
	private String bigCate;// VARCHAR2(10), --大类
	private String bigCateName;// VARCHAR2(50), --大类名称
	private String seriesCode;// VARCHAR2(10), --系列代码
	private String seriesExpl;// VARCHAR2(50), --系列说明
	private String bigItemCode;// VARCHAR2(10), --大品项代码
	private String bigItemExpl;// VARCHAR2(50), --大品项说明
	private String smallItemCode;// VARCHAR2(10), --小品项代码
	private String smallItemExpl;// VARCHAR2(50), --小品项说明
	private String mainProfCode;// VARCHAR2(10), --主规格代码
	private String mainProfExpl;// VARCHAR2(50), --主规格说明
	private String nextProfCode;// VARCHAR2(10), --次规格代码
	private String nextProfExpl;// VARCHAR2(50), --次规格说明
	private String boxCode;// VARCHAR2(20), --箱条码
	private String isGift;// VARCHAR2(10), --是否礼包
	private String brandCode;// VARCHAR2(10), --品牌代码
	private String brandExpl;// VARCHAR2(50), --品牌说明
	private String childBrandCode;// VARCHAR2(10), --子品牌代码
	private String childBrandExpl;// VARCHAR2(50), --子品牌说明
	private String produCode;// VARCHAR2(10), --生产划分代码
	private String produExpl;// VARCHAR2(50), --生产划分说明
	private String chanCode;// VARCHAR2(10), --渠道代码（业本给出）
	private String chanExpl;// VARCHAR2(50), --渠道说明（业本给出）
	private String useChin;// VARCHAR2(50), --用途中文（箱唛，仅辅助物）
	private String useEngl;// VARCHAR2(50), --用途英文（箱唛，仅辅助物）
	private String mateStatus;// VARCHAR2(10), --物料状态(生命周期）
	private String procUnit;// VARCHAR2(10), --采购单位
	private String boxUnit;// VARCHAR2(10), --箱单位
	private String branchUnit;// VARCHAR2(10), --零支单位(借用成本单位的栏位）
	private String produUnit;// VARCHAR2(10), --生产单位
	private Integer packNumber;// INT, --包入数(借用成本单位换算率）
	private String dimension;// VARCHAR2(50), --量纲
	private String produAbc;// VARCHAR2(10), --产品ABC
	private String barCodeAbc;// VARCHAR2(10), --条码ABC
	private String supply;// VARCHAR2(10), --货源
	private String reveCateCode;// VARCHAR2(30), --税收分类编码
	private String reveCateName;// VARCHAR2(60), --税收分类名称
	private String expiDate;// VARCHAR2(10), --保质期
	private String guarUnit;// VARCHAR2(10), --保质单位
	private String nationKey;// VARCHAR2(10), --国家键值
	private String nationName;// VARCHAR2(30), --国家名称
	private String provCode;// VARCHAR2(10), --行政省编码
	private String provName;// VARCHAR2(30), --行政省名称
	private String cityCode;// VARCHAR2(10), --行政市编码
	private String cityName;// VARCHAR2(30), --行政市名称
	private Float mateLength;// NUMBER(13,3), --长（整箱，单位分米）
	private Float mateWidth;// NUMBER(13,3), --宽（整箱，单位分米）
	private Float mateHigh;// NUMBER(13,3), --高（整箱，单位分米）
	private Float mateBulk;// NUMBER(13,3), --体积（整箱，单位立方分米）
	private Float mateWeight;// NUMBER(13,3), --重量（整箱，单位立方分米）
	private String status;// VARCHAR2(10), --状态（更新否）
	private String isDelete;// VARCHAR2(10), --物料状态(区分物料是否做删除标记)
	private String repeatedly;// VARCHAR2(10), --批次管理
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;// DATE, --更新详细日期
	private String finMateId;// VARCHAR2(50), --成品物料编码
	private String mateName;// VARCHAR2(20), --物料名称
	private String basicUnit;// VARCHAR2(10), --基本单位
	private String isQuote;// varcher2(10) --是否有报价结构 0:表示没有，1:表示有

	private String mateInfo;
	private String mateGroupInfo;
	private String mateTypeInfo;
	private String seriesInfo;
	private String isMaintenance; // 是否维护基本信息

	private boolean LAY_CHECKED;

	public Material() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Material(String mateId, String mateCode, String skuTiptop, String skuEnglish, String chinName,
			String mateType, String mateTypeExpl, String mateGroupCode, String mateGroupExpl, String barCode,
			String barCodeEngl, String barCodeChin, String repeUnit, Integer boxNumber, String finProMate,
			String finProSku, String abcIden, String busiCode, String busiExpl, String cateCode, String cateExpl,
			String bigCate, String bigCateName, String seriesCode, String seriesExpl, String bigItemCode,
			String bigItemExpl, String smallItemCode, String smallItemExpl, String mainProfCode, String mainProfExpl,
			String nextProfCode, String nextProfExpl, String boxCode, String isGift, String brandCode, String brandExpl,
			String childBrandCode, String childBrandExpl, String produCode, String produExpl, String chanCode,
			String chanExpl, String useChin, String useEngl, String mateStatus, String procUnit, String boxUnit,
			String branchUnit, String produUnit, Integer packNumber, String dimension, String produAbc,
			String barCodeAbc, String supply, String reveCateCode, String reveCateName, String expiDate,
			String guarUnit, String nationKey, String nationName, String provCode, String provName, String cityCode,
			String cityName, Float mateLength, Float mateWidth, Float mateHigh, Float mateBulk, Float mateWeight,
			String status, String isDelete, String repeatedly, Date updateDate, String finMateId, String mateName,
			String basicUnit, String isQuote, String mateInfo, String mateGroupInfo, String mateTypeInfo,
			String seriesInfo, String isMaintenance, boolean lAY_CHECKED) {
		super();
		this.mateId = mateId;
		this.mateCode = mateCode;
		this.skuTiptop = skuTiptop;
		this.skuEnglish = skuEnglish;
		this.chinName = chinName;
		this.mateType = mateType;
		this.mateTypeExpl = mateTypeExpl;
		this.mateGroupCode = mateGroupCode;
		this.mateGroupExpl = mateGroupExpl;
		this.barCode = barCode;
		this.barCodeEngl = barCodeEngl;
		this.barCodeChin = barCodeChin;
		this.repeUnit = repeUnit;
		this.boxNumber = boxNumber;
		this.finProMate = finProMate;
		this.finProSku = finProSku;
		this.abcIden = abcIden;
		this.busiCode = busiCode;
		this.busiExpl = busiExpl;
		this.cateCode = cateCode;
		this.cateExpl = cateExpl;
		this.bigCate = bigCate;
		this.bigCateName = bigCateName;
		this.seriesCode = seriesCode;
		this.seriesExpl = seriesExpl;
		this.bigItemCode = bigItemCode;
		this.bigItemExpl = bigItemExpl;
		this.smallItemCode = smallItemCode;
		this.smallItemExpl = smallItemExpl;
		this.mainProfCode = mainProfCode;
		this.mainProfExpl = mainProfExpl;
		this.nextProfCode = nextProfCode;
		this.nextProfExpl = nextProfExpl;
		this.boxCode = boxCode;
		this.isGift = isGift;
		this.brandCode = brandCode;
		this.brandExpl = brandExpl;
		this.childBrandCode = childBrandCode;
		this.childBrandExpl = childBrandExpl;
		this.produCode = produCode;
		this.produExpl = produExpl;
		this.chanCode = chanCode;
		this.chanExpl = chanExpl;
		this.useChin = useChin;
		this.useEngl = useEngl;
		this.mateStatus = mateStatus;
		this.procUnit = procUnit;
		this.boxUnit = boxUnit;
		this.branchUnit = branchUnit;
		this.produUnit = produUnit;
		this.packNumber = packNumber;
		this.dimension = dimension;
		this.produAbc = produAbc;
		this.barCodeAbc = barCodeAbc;
		this.supply = supply;
		this.reveCateCode = reveCateCode;
		this.reveCateName = reveCateName;
		this.expiDate = expiDate;
		this.guarUnit = guarUnit;
		this.nationKey = nationKey;
		this.nationName = nationName;
		this.provCode = provCode;
		this.provName = provName;
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.mateLength = mateLength;
		this.mateWidth = mateWidth;
		this.mateHigh = mateHigh;
		this.mateBulk = mateBulk;
		this.mateWeight = mateWeight;
		this.status = status;
		this.isDelete = isDelete;
		this.repeatedly = repeatedly;
		this.updateDate = updateDate;
		this.finMateId = finMateId;
		this.mateName = mateName;
		this.basicUnit = basicUnit;
		this.isQuote = isQuote;
		this.mateInfo = mateInfo;
		this.mateGroupInfo = mateGroupInfo;
		this.mateTypeInfo = mateTypeInfo;
		this.seriesInfo = seriesInfo;
		this.isMaintenance = isMaintenance;
		LAY_CHECKED = lAY_CHECKED;
	}



	/**
	 * @return the seriesInfo
	 */
	public String getSeriesInfo() {
		return seriesInfo;
	}



	/**
	 * @param seriesInfo the seriesInfo to set
	 */
	public void setSeriesInfo(String seriesInfo) {
		this.seriesInfo = seriesInfo;
	}



	/**
	 * @return the lAY_CHECKED
	 */
	public boolean isLAY_CHECKED() {
		return LAY_CHECKED;
	}

	/**
	 * @param lAY_CHECKED
	 *            the lAY_CHECKED to set
	 */
	public void setLAY_CHECKED(boolean lAY_CHECKED) {
		LAY_CHECKED = lAY_CHECKED;
	}

	/**
	 * @return the isQuote
	 */
	public String getIsQuote() {
		return isQuote;
	}

	/**
	 * @param isQuote
	 *            the isQuote to set
	 */
	public void setIsQuote(String isQuote) {
		this.isQuote = isQuote;
	}

	public String getMateId() {
		return mateId;
	}

	public void setMateId(String mateId) {
		this.mateId = mateId;
	}

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}

	public String getSkuTiptop() {
		return skuTiptop;
	}

	public void setSkuTiptop(String skuTiptop) {
		this.skuTiptop = skuTiptop;
	}

	public String getSkuEnglish() {
		return skuEnglish;
	}

	public void setSkuEnglish(String skuEnglish) {
		this.skuEnglish = skuEnglish;
	}

	public String getChinName() {
		return chinName;
	}

	public void setChinName(String chinName) {
		this.chinName = chinName;
	}

	public String getMateType() {
		return mateType;
	}

	public void setMateType(String mateType) {
		this.mateType = mateType;
	}

	public String getMateTypeExpl() {
		return mateTypeExpl;
	}

	public void setMateTypeExpl(String mateTypeExpl) {
		this.mateTypeExpl = mateTypeExpl;
	}

	public String getMateGroupCode() {
		return mateGroupCode;
	}

	public void setMateGroupCode(String mateGroupCode) {
		this.mateGroupCode = mateGroupCode;
	}

	public String getMateGroupExpl() {
		return mateGroupExpl;
	}

	public void setMateGroupExpl(String mateGroupExpl) {
		this.mateGroupExpl = mateGroupExpl;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBarCodeEngl() {
		return barCodeEngl;
	}

	public void setBarCodeEngl(String barCodeEngl) {
		this.barCodeEngl = barCodeEngl;
	}

	public String getBarCodeChin() {
		return barCodeChin;
	}

	public void setBarCodeChin(String barCodeChin) {
		this.barCodeChin = barCodeChin;
	}

	public String getRepeUnit() {
		return repeUnit;
	}

	public void setRepeUnit(String repeUnit) {
		this.repeUnit = repeUnit;
	}

	public Integer getBoxNumber() {
		return boxNumber;
	}

	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
	}

	public String getFinProMate() {
		return finProMate;
	}

	public void setFinProMate(String finProMate) {
		this.finProMate = finProMate;
	}

	public String getFinProSku() {
		return finProSku;
	}

	public void setFinProSku(String finProSku) {
		this.finProSku = finProSku;
	}

	public String getAbcIden() {
		return abcIden;
	}

	public void setAbcIden(String abcIden) {
		this.abcIden = abcIden;
	}

	public String getBusiCode() {
		return busiCode;
	}

	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}

	public String getBusiExpl() {
		return busiExpl;
	}

	public void setBusiExpl(String busiExpl) {
		this.busiExpl = busiExpl;
	}

	public String getCateCode() {
		return cateCode;
	}

	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}

	public String getCateExpl() {
		return cateExpl;
	}

	public void setCateExpl(String cateExpl) {
		this.cateExpl = cateExpl;
	}

	public String getBigCate() {
		return bigCate;
	}

	public void setBigCate(String bigCate) {
		this.bigCate = bigCate;
	}

	public String getBigCateName() {
		return bigCateName;
	}

	public void setBigCateName(String bigCateName) {
		this.bigCateName = bigCateName;
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public String getSeriesExpl() {
		return seriesExpl;
	}

	public void setSeriesExpl(String seriesExpl) {
		this.seriesExpl = seriesExpl;
	}

	public String getBigItemCode() {
		return bigItemCode;
	}

	public void setBigItemCode(String bigItemCode) {
		this.bigItemCode = bigItemCode;
	}

	public String getBigItemExpl() {
		return bigItemExpl;
	}

	public void setBigItemExpl(String bigItemExpl) {
		this.bigItemExpl = bigItemExpl;
	}

	public String getSmallItemCode() {
		return smallItemCode;
	}

	public void setSmallItemCode(String smallItemCode) {
		this.smallItemCode = smallItemCode;
	}

	public String getSmallItemExpl() {
		return smallItemExpl;
	}

	public void setSmallItemExpl(String smallItemExpl) {
		this.smallItemExpl = smallItemExpl;
	}

	public String getMainProfCode() {
		return mainProfCode;
	}

	public void setMainProfCode(String mainProfCode) {
		this.mainProfCode = mainProfCode;
	}

	public String getMainProfExpl() {
		return mainProfExpl;
	}

	public void setMainProfExpl(String mainProfExpl) {
		this.mainProfExpl = mainProfExpl;
	}

	public String getNextProfCode() {
		return nextProfCode;
	}

	public void setNextProfCode(String nextProfCode) {
		this.nextProfCode = nextProfCode;
	}

	public String getNextProfExpl() {
		return nextProfExpl;
	}

	public void setNextProfExpl(String nextProfExpl) {
		this.nextProfExpl = nextProfExpl;
	}

	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}

	public String getIsGift() {
		return isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandExpl() {
		return brandExpl;
	}

	public void setBrandExpl(String brandExpl) {
		this.brandExpl = brandExpl;
	}

	public String getChildBrandCode() {
		return childBrandCode;
	}

	public void setChildBrandCode(String childBrandCode) {
		this.childBrandCode = childBrandCode;
	}

	public String getChildBrandExpl() {
		return childBrandExpl;
	}

	public void setChildBrandExpl(String childBrandExpl) {
		this.childBrandExpl = childBrandExpl;
	}

	public String getProduCode() {
		return produCode;
	}

	public void setProduCode(String produCode) {
		this.produCode = produCode;
	}

	public String getProduExpl() {
		return produExpl;
	}

	public void setProduExpl(String produExpl) {
		this.produExpl = produExpl;
	}

	public String getChanCode() {
		return chanCode;
	}

	public void setChanCode(String chanCode) {
		this.chanCode = chanCode;
	}

	public String getChanExpl() {
		return chanExpl;
	}

	public void setChanExpl(String chanExpl) {
		this.chanExpl = chanExpl;
	}

	public String getUseChin() {
		return useChin;
	}

	public void setUseChin(String useChin) {
		this.useChin = useChin;
	}

	public String getUseEngl() {
		return useEngl;
	}

	public void setUseEngl(String useEngl) {
		this.useEngl = useEngl;
	}

	public String getMateStatus() {
		return mateStatus;
	}

	public void setMateStatus(String mateStatus) {
		this.mateStatus = mateStatus;
	}

	public String getProcUnit() {
		return procUnit;
	}

	public void setProcUnit(String procUnit) {
		this.procUnit = procUnit;
	}

	public String getBoxUnit() {
		return boxUnit;
	}

	public void setBoxUnit(String boxUnit) {
		this.boxUnit = boxUnit;
	}

	public String getBranchUnit() {
		return branchUnit;
	}

	public void setBranchUnit(String branchUnit) {
		this.branchUnit = branchUnit;
	}

	public String getProduUnit() {
		return produUnit;
	}

	public void setProduUnit(String produUnit) {
		this.produUnit = produUnit;
	}

	public Integer getPackNumber() {
		return packNumber;
	}

	public void setPackNumber(Integer packNumber) {
		this.packNumber = packNumber;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getProduAbc() {
		return produAbc;
	}

	public void setProduAbc(String produAbc) {
		this.produAbc = produAbc;
	}

	public String getBarCodeAbc() {
		return barCodeAbc;
	}

	public void setBarCodeAbc(String barCodeAbc) {
		this.barCodeAbc = barCodeAbc;
	}

	public String getSupply() {
		return supply;
	}

	public void setSupply(String supply) {
		this.supply = supply;
	}

	public String getReveCateCode() {
		return reveCateCode;
	}

	public void setReveCateCode(String reveCateCode) {
		this.reveCateCode = reveCateCode;
	}

	public String getReveCateName() {
		return reveCateName;
	}

	public void setReveCateName(String reveCateName) {
		this.reveCateName = reveCateName;
	}

	public String getExpiDate() {
		return expiDate;
	}

	public void setExpiDate(String expiDate) {
		this.expiDate = expiDate;
	}

	public String getGuarUnit() {
		return guarUnit;
	}

	public void setGuarUnit(String guarUnit) {
		this.guarUnit = guarUnit;
	}

	public String getNationKey() {
		return nationKey;
	}

	public void setNationKey(String nationKey) {
		this.nationKey = nationKey;
	}

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	

	/**
	 * @return the mateLength
	 */
	public Float getMateLength() {
		return mateLength;
	}

	/**
	 * @param mateLength the mateLength to set
	 */
	public void setMateLength(Float mateLength) {
		this.mateLength = mateLength;
	}

	/**
	 * @return the mateWidth
	 */
	public Float getMateWidth() {
		return mateWidth;
	}

	/**
	 * @param mateWidth the mateWidth to set
	 */
	public void setMateWidth(Float mateWidth) {
		this.mateWidth = mateWidth;
	}

	/**
	 * @return the mateHigh
	 */
	public Float getMateHigh() {
		return mateHigh;
	}

	/**
	 * @param mateHigh the mateHigh to set
	 */
	public void setMateHigh(Float mateHigh) {
		this.mateHigh = mateHigh;
	}

	/**
	 * @return the mateBulk
	 */
	public Float getMateBulk() {
		return mateBulk;
	}

	/**
	 * @param mateBulk the mateBulk to set
	 */
	public void setMateBulk(Float mateBulk) {
		this.mateBulk = mateBulk;
	}

	/**
	 * @return the mateWeight
	 */
	public Float getMateWeight() {
		return mateWeight;
	}

	/**
	 * @param mateWeight the mateWeight to set
	 */
	public void setMateWeight(Float mateWeight) {
		this.mateWeight = mateWeight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getRepeatedly() {
		return repeatedly;
	}

	public void setRepeatedly(String repeatedly) {
		this.repeatedly = repeatedly;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getFinMateId() {
		return finMateId;
	}

	public void setFinMateId(String finMateId) {
		this.finMateId = finMateId;
	}

	public String getMateName() {
		return mateName;
	}

	public void setMateName(String mateName) {
		this.mateName = mateName;
	}

	public String getBasicUnit() {
		return basicUnit;
	}

	public void setBasicUnit(String basicUnit) {
		this.basicUnit = basicUnit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the mateInfo
	 */
	public String getMateInfo() {
		return mateInfo;
	}

	/**
	 * @param mateInfo
	 *            the mateInfo to set
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
	 * @param mateGroupInfo
	 *            the mateGroupInfo to set
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
	 * @param mateTypeInfo
	 *            the mateTypeInfo to set
	 */
	public void setMateTypeInfo(String mateTypeInfo) {
		this.mateTypeInfo = mateTypeInfo;
	}
	
	
	public String getIsMaintenance() {
		return isMaintenance;
	}

	public void setIsMaintenance(String isMaintenance) {
		this.isMaintenance = isMaintenance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Material [mateId=" + mateId + ", mateCode=" + mateCode + ", skuTiptop=" + skuTiptop + ", skuEnglish="
				+ skuEnglish + ", chinName=" + chinName + ", mateType=" + mateType + ", mateTypeExpl=" + mateTypeExpl
				+ ", mateGroupCode=" + mateGroupCode + ", mateGroupExpl=" + mateGroupExpl + ", barCode=" + barCode
				+ ", barCodeEngl=" + barCodeEngl + ", barCodeChin=" + barCodeChin + ", repeUnit=" + repeUnit
				+ ", boxNumber=" + boxNumber + ", finProMate=" + finProMate + ", finProSku=" + finProSku + ", abcIden="
				+ abcIden + ", busiCode=" + busiCode + ", busiExpl=" + busiExpl + ", cateCode=" + cateCode
				+ ", cateExpl=" + cateExpl + ", bigCate=" + bigCate + ", bigCateName=" + bigCateName + ", seriesCode="
				+ seriesCode + ", seriesExpl=" + seriesExpl + ", bigItemCode=" + bigItemCode + ", bigItemExpl="
				+ bigItemExpl + ", smallItemCode=" + smallItemCode + ", smallItemExpl=" + smallItemExpl
				+ ", mainProfCode=" + mainProfCode + ", mainProfExpl=" + mainProfExpl + ", nextProfCode=" + nextProfCode
				+ ", nextProfExpl=" + nextProfExpl + ", boxCode=" + boxCode + ", isGift=" + isGift + ", brandCode="
				+ brandCode + ", brandExpl=" + brandExpl + ", childBrandCode=" + childBrandCode + ", childBrandExpl="
				+ childBrandExpl + ", produCode=" + produCode + ", produExpl=" + produExpl + ", chanCode=" + chanCode
				+ ", chanExpl=" + chanExpl + ", useChin=" + useChin + ", useEngl=" + useEngl + ", mateStatus="
				+ mateStatus + ", procUnit=" + procUnit + ", boxUnit=" + boxUnit + ", branchUnit=" + branchUnit
				+ ", produUnit=" + produUnit + ", packNumber=" + packNumber + ", dimension=" + dimension + ", produAbc="
				+ produAbc + ", barCodeAbc=" + barCodeAbc + ", supply=" + supply + ", reveCateCode=" + reveCateCode
				+ ", reveCateName=" + reveCateName + ", expiDate=" + expiDate + ", guarUnit=" + guarUnit
				+ ", nationKey=" + nationKey + ", nationName=" + nationName + ", provCode=" + provCode + ", provName="
				+ provName + ", cityCode=" + cityCode + ", cityName=" + cityName + ", mateLength=" + mateLength
				+ ", mateWidth=" + mateWidth + ", mateHigh=" + mateHigh + ", mateBulk=" + mateBulk + ", mateWeight="
				+ mateWeight + ", status=" + status + ", isDelete=" + isDelete + ", repeatedly=" + repeatedly
				+ ", updateDate=" + updateDate + ", finMateId=" + finMateId + ", mateName=" + mateName + ", basicUnit="
				+ basicUnit + ", isQuote=" + isQuote + ", mateInfo=" + mateInfo + ", mateGroupInfo=" + mateGroupInfo
				+ ", mateTypeInfo=" + mateTypeInfo + ", seriesInfo=" + seriesInfo + ", isMaintenance=" + isMaintenance
				+ ", LAY_CHECKED=" + LAY_CHECKED + "]";
	}




	

}
