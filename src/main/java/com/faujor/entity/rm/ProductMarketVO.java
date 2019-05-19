package com.faujor.entity.rm;

import java.math.BigDecimal;

/**
 * 产销管理报表
 * 
 * @author martian
 *
 */
public class ProductMarketVO {

	private BigDecimal pad_count;
	private BigDecimal sip_count;
	private BigDecimal pdr_count;

	public BigDecimal getPad_count() {
		return pad_count;
	}

	public void setPad_count(BigDecimal pad_count) {
		this.pad_count = pad_count;
	}

	public BigDecimal getSip_count() {
		return sip_count;
	}

	public void setSip_count(BigDecimal sip_count) {
		this.sip_count = sip_count;
	}

	public BigDecimal getPdr_count() {
		return pdr_count;
	}

	public void setPdr_count(BigDecimal pdr_count) {
		this.pdr_count = pdr_count;
	}
}
