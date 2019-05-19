package com.faujor.entity.bam;

public class AppointMail {
	private String supplierName;// 供应商名称（来自供应商主数据，由两个字段拼接而成，供应商名称和FAX）
	private String deliveryDate;// 送货日期
	private String supplierEmail;// 供应商邮箱（来自供应商主数据）

	private String appointCode;// 预约单号
	private String warehouse;// 仓库

	private String remark;// 备注
	private String carNum;// 车数
	private String carType;// 车型
	private String destination;// 目的地
	private String telphone;// 电话
	private String contact;// 联系人
	
	private String status;// 状态：区别发布和作废功能

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public String getAppointCode() {
		return appointCode;
	}

	public void setAppointCode(String appointCode) {
		this.appointCode = appointCode;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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
	
	
}
