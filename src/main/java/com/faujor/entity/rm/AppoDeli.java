package com.faujor.entity.rm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppoDeli implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//预约单信息
	public String sapId;//供应商的sap编码
	public String suppName;//供应商名称
	public String suppRange;//供应商子范围编码
	public String suppRangeDesc;//供应商子范围描述
	public String appoCode;//预约单号、直发通知单号
	public String requCode;//调拨单号（只有直发通知有）
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date appoDate;//预约单的预约日、直发通知单的到货日期，
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date createDate;//预约单号，直发通知单的创建日期
	public String mateCode;//物料编码
	public String mateName;//物料名称
	public String opId;//采购订单号（这个只有直发通知单有）
	public Double mateNumber;//预约数量
	public String appoStatus;//预约状态，直发通知单状态
	public String appoType ; //appo:表示预约单，stra:表示直发通知单
	//送货单信息
	public String status;//送货单的状态
	public String deliCode;//送货单号
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date deliDate;//送货时间
	public String deliCreateDate;//送货单创建时间
	public String orderId;//采购订单号 
	public String frequency;//项次
	public Double deliNumber;//实际送货数量
	public Double appoNumber;//预约数量
	public Double unpaNumber;//订单未交数量
	public Double calculNumber;//订单计算未交量（或订单可用量）
	public String receUnit;//送货单的送货地址
	//收货单信息
	public String receCode;//收货单号
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date receDate;//收货时间 
	public String receCreateDate;//收货单创建日期
	public Double receNumber;//实收数量
	public String inboDeliCode;//内向交货单号
	public String isOccupy;//是否占用
	public String receStatus;//收货单状态
	
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date appoStartDate;//预约日期起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date appoEndDate;//预约日期结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date deliStartDate;//送货日期起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date deliEndDate;//送货日期结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date receStartDate;//收货日期起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date receEndDate;//收货日期结束日期
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date appoCreStartDate;//预约日期起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date appoCreEndDate;//预约日期结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date deliCreStartDate;//送货日期起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date deliCreEndDate;//送货日期结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date receCreStartDate;//收货日期起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date receCreEndDate;//收货日期结束日期
	
	/**
	 * @return the suppRange
	 */
	public String getSuppRange() {
		return suppRange;
	}
	/**
	 * @param suppRange the suppRange to set
	 */
	public void setSuppRange(String suppRange) {
		this.suppRange = suppRange;
	}
	/**
	 * @return the suppRangeDesc
	 */
	public String getSuppRangeDesc() {
		return suppRangeDesc;
	}
	/**
	 * @param suppRangeDesc the suppRangeDesc to set
	 */
	public void setSuppRangeDesc(String suppRangeDesc) {
		this.suppRangeDesc = suppRangeDesc;
	}
	/**
	 * @return the sapId
	 */
	public String getSapId() {
		return sapId;
	}
	/**
	 * @param sapId the sapId to set
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}
	/**
	 * @return the suppName
	 */
	public String getSuppName() {
		return suppName;
	}
	/**
	 * @param suppName the suppName to set
	 */
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	/**
	 * @return the appoCode
	 */
	public String getAppoCode() {
		return appoCode;
	}
	/**
	 * @param appoCode the appoCode to set
	 */
	public void setAppoCode(String appoCode) {
		this.appoCode = appoCode;
	}
	/**
	 * @return the requCode
	 */
	public String getRequCode() {
		return requCode;
	}
	/**
	 * @param requCode the requCode to set
	 */
	public void setRequCode(String requCode) {
		this.requCode = requCode;
	}
	/**
	 * @return the appoDate
	 */
	public Date getAppoDate() {
		return appoDate;
	}
	/**
	 * @param appoDate the appoDate to set
	 */
	public void setAppoDate(Date appoDate) {
		this.appoDate = appoDate;
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
	 * @return the opId
	 */
	public String getOpId() {
		return opId;
	}
	/**
	 * @param opId the opId to set
	 */
	public void setOpId(String opId) {
		this.opId = opId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the deliCode
	 */
	public String getDeliCode() {
		return deliCode;
	}
	/**
	 * @param deliCode the deliCode to set
	 */
	public void setDeliCode(String deliCode) {
		this.deliCode = deliCode;
	}
	/**
	 * @return the deliDate
	 */
	public Date getDeliDate() {
		return deliDate;
	}
	/**
	 * @param deliDate the deliDate to set
	 */
	public void setDeliDate(Date deliDate) {
		this.deliDate = deliDate;
	}
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	 * @return the deliNumber
	 */
	public Double getDeliNumber() {
		return deliNumber;
	}
	/**
	 * @param deliNumber the deliNumber to set
	 */
	public void setDeliNumber(Double deliNumber) {
		this.deliNumber = deliNumber;
	}
	/**
	 * @return the appoNumber
	 */
	public Double getAppoNumber() {
		return appoNumber;
	}
	/**
	 * @param appoNumber the appoNumber to set
	 */
	public void setAppoNumber(Double appoNumber) {
		this.appoNumber = appoNumber;
	}
	/**
	 * @return the unpaNumber
	 */
	public Double getUnpaNumber() {
		return unpaNumber;
	}
	/**
	 * @param unpaNumber the unpaNumber to set
	 */
	public void setUnpaNumber(Double unpaNumber) {
		this.unpaNumber = unpaNumber;
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
	 * @return the receCode
	 */
	public String getReceCode() {
		return receCode;
	}
	/**
	 * @param receCode the receCode to set
	 */
	public void setReceCode(String receCode) {
		this.receCode = receCode;
	}
	/**
	 * @return the receDate
	 */
	public Date getReceDate() {
		return receDate;
	}
	/**
	 * @param receDate the receDate to set
	 */
	public void setReceDate(Date receDate) {
		this.receDate = receDate;
	}
	
	/**
	 * @return the deliCreateDate
	 */
	public String getDeliCreateDate() {
		return deliCreateDate;
	}
	/**
	 * @param deliCreateDate the deliCreateDate to set
	 */
	public void setDeliCreateDate(String deliCreateDate) {
		this.deliCreateDate = deliCreateDate;
	}
	/**
	 * @return the receCreateDate
	 */
	public String getReceCreateDate() {
		return receCreateDate;
	}
	/**
	 * @param receCreateDate the receCreateDate to set
	 */
	public void setReceCreateDate(String receCreateDate) {
		this.receCreateDate = receCreateDate;
	}
	/**
	 * @return the receNumber
	 */
	public Double getReceNumber() {
		return receNumber;
	}
	/**
	 * @param receNumber the receNumber to set
	 */
	public void setReceNumber(Double receNumber) {
		this.receNumber = receNumber;
	}
	/**
	 * @return the inboDeliCode
	 */
	public String getInboDeliCode() {
		return inboDeliCode;
	}
	/**
	 * @param inboDeliCode the inboDeliCode to set
	 */
	public void setInboDeliCode(String inboDeliCode) {
		this.inboDeliCode = inboDeliCode;
	}
	/**
	 * @return the mateNumber
	 */
	public Double getMateNumber() {
		return mateNumber;
	}
	/**
	 * @param mateNumber the mateNumber to set
	 */
	public void setMateNumber(Double mateNumber) {
		this.mateNumber = mateNumber;
	}
	/**
	 * @return the appoStatus
	 */
	public String getAppoStatus() {
		return appoStatus;
	}
	/**
	 * @param appoStatus the appoStatus to set
	 */
	public void setAppoStatus(String appoStatus) {
		this.appoStatus = appoStatus;
	}
	/**
	 * @return the appoType
	 */
	public String getAppoType() {
		return appoType;
	}
	/**
	 * @param appoType the appoType to set
	 */
	public void setAppoType(String appoType) {
		this.appoType = appoType;
	}
	/**
	 * @return the appoStartDate
	 */
	public Date getAppoStartDate() {
		return appoStartDate;
	}
	/**
	 * @param appoStartDate the appoStartDate to set
	 */
	public void setAppoStartDate(Date appoStartDate) {
		this.appoStartDate = appoStartDate;
	}
	/**
	 * @return the appoEndDate
	 */
	public Date getAppoEndDate() {
		return appoEndDate;
	}
	/**
	 * @param appoEndDate the appoEndDate to set
	 */
	public void setAppoEndDate(Date appoEndDate) {
		this.appoEndDate = appoEndDate;
	}
	/**
	 * @return the deliStartDate
	 */
	public Date getDeliStartDate() {
		return deliStartDate;
	}
	/**
	 * @param deliStartDate the deliStartDate to set
	 */
	public void setDeliStartDate(Date deliStartDate) {
		this.deliStartDate = deliStartDate;
	}
	/**
	 * @return the deliEndDate
	 */
	public Date getDeliEndDate() {
		return deliEndDate;
	}
	/**
	 * @param deliEndDate the deliEndDate to set
	 */
	public void setDeliEndDate(Date deliEndDate) {
		this.deliEndDate = deliEndDate;
	}
	/**
	 * @return the receStartDate
	 */
	public Date getReceStartDate() {
		return receStartDate;
	}
	/**
	 * @param receStartDate the receStartDate to set
	 */
	public void setReceStartDate(Date receStartDate) {
		this.receStartDate = receStartDate;
	}
	/**
	 * @return the receEndDate
	 */
	public Date getReceEndDate() {
		return receEndDate;
	}
	/**
	 * @param receEndDate the receEndDate to set
	 */
	public void setReceEndDate(Date receEndDate) {
		this.receEndDate = receEndDate;
	}
	/**
	 * @return the isOccupy
	 */
	public String getIsOccupy() {
		return isOccupy;
	}
	/**
	 * @param isOccupy the isOccupy to set
	 */
	public void setIsOccupy(String isOccupy) {
		this.isOccupy = isOccupy;
	}
	
	
	/**
	 * @return the appoCreStartDate
	 */
	public Date getAppoCreStartDate() {
		return appoCreStartDate;
	}
	/**
	 * @param appoCreStartDate the appoCreStartDate to set
	 */
	public void setAppoCreStartDate(Date appoCreStartDate) {
		this.appoCreStartDate = appoCreStartDate;
	}
	/**
	 * @return the appoCreEndDate
	 */
	public Date getAppoCreEndDate() {
		return appoCreEndDate;
	}
	/**
	 * @param appoCreEndDate the appoCreEndDate to set
	 */
	public void setAppoCreEndDate(Date appoCreEndDate) {
		this.appoCreEndDate = appoCreEndDate;
	}
	/**
	 * @return the deliCreStartDate
	 */
	public Date getDeliCreStartDate() {
		return deliCreStartDate;
	}
	/**
	 * @param deliCreStartDate the deliCreStartDate to set
	 */
	public void setDeliCreStartDate(Date deliCreStartDate) {
		this.deliCreStartDate = deliCreStartDate;
	}
	/**
	 * @return the deliCreEndDate
	 */
	public Date getDeliCreEndDate() {
		return deliCreEndDate;
	}
	/**
	 * @param deliCreEndDate the deliCreEndDate to set
	 */
	public void setDeliCreEndDate(Date deliCreEndDate) {
		this.deliCreEndDate = deliCreEndDate;
	}
	/**
	 * @return the receCreStartDate
	 */
	public Date getReceCreStartDate() {
		return receCreStartDate;
	}
	/**
	 * @param receCreStartDate the receCreStartDate to set
	 */
	public void setReceCreStartDate(Date receCreStartDate) {
		this.receCreStartDate = receCreStartDate;
	}
	/**
	 * @return the receCreEndDate
	 */
	public Date getReceCreEndDate() {
		return receCreEndDate;
	}
	/**
	 * @param receCreEndDate the receCreEndDate to set
	 */
	public void setReceCreEndDate(Date receCreEndDate) {
		this.receCreEndDate = receCreEndDate;
	}
	
	/**
	 * @return the receUnit
	 */
	public String getReceUnit() {
		return receUnit;
	}
	/**
	 * @param receUnit the receUnit to set
	 */
	public void setReceUnit(String receUnit) {
		this.receUnit = receUnit;
	}
	/**
	 * @return the receStatus
	 */
	public String getReceStatus() {
		return receStatus;
	}
	/**
	 * @param receStatus the receStatus to set
	 */
	public void setReceStatus(String receStatus) {
		this.receStatus = receStatus;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppoDeli [sapId=" + sapId + ", suppName=" + suppName + ", appoCode=" + appoCode + ", requCode="
				+ requCode + ", appoDate=" + appoDate + ", createDate=" + createDate + ", mateCode=" + mateCode
				+ ", mateName=" + mateName + ", opId=" + opId + ", mateNumber=" + mateNumber + ", appoStatus="
				+ appoStatus + ", appoType=" + appoType + ", status=" + status + ", deliCode=" + deliCode
				+ ", deliDate=" + deliDate + ", deliCreateDate=" + deliCreateDate + ", orderId=" + orderId
				+ ", frequency=" + frequency + ", deliNumber=" + deliNumber + ", appoNumber=" + appoNumber
				+ ", unpaNumber=" + unpaNumber + ", calculNumber=" + calculNumber + ", receUnit=" + receUnit
				+ ", receCode=" + receCode + ", receDate=" + receDate + ", receCreateDate=" + receCreateDate
				+ ", receNumber=" + receNumber + ", inboDeliCode=" + inboDeliCode + ", isOccupy=" + isOccupy
				+ ", receStatus=" + receStatus + ", appoStartDate=" + appoStartDate + ", appoEndDate=" + appoEndDate
				+ ", deliStartDate=" + deliStartDate + ", deliEndDate=" + deliEndDate + ", receStartDate="
				+ receStartDate + ", receEndDate=" + receEndDate + ", appoCreStartDate=" + appoCreStartDate
				+ ", appoCreEndDate=" + appoCreEndDate + ", deliCreStartDate=" + deliCreStartDate + ", deliCreEndDate="
				+ deliCreEndDate + ", receCreStartDate=" + receCreStartDate + ", receCreEndDate=" + receCreEndDate
				+ "]";
	}
	
	
	
	
	
}
