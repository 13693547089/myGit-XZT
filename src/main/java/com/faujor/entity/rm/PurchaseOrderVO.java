package com.faujor.entity.rm;

import java.math.BigDecimal;

public class PurchaseOrderVO {

	private String monthDate;
	private BigDecimal poCount;
	private BigDecimal poItemCount;

	public String getMonthDate() {
		return monthDate;
	}

	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}

	public BigDecimal getPoCount() {
		return poCount;
	}

	public void setPoCount(BigDecimal poCount) {
		this.poCount = poCount;
	}

	public BigDecimal getPoItemCount() {
		return poItemCount;
	}

	public void setPoItemCount(BigDecimal poItemCount) {
		this.poItemCount = poItemCount;
	}
}
