package com.faujor.entity.bam.psm;

public class SuppVo {

	private String suppNo;
	private String suppName;
	private String itemCode;
	private String itemName;
	public String getSuppNo() {
		return suppNo;
	}
	public void setSuppNo(String suppNo) {
		this.suppNo = suppNo;
	}
	public String getSuppName() {
		return suppName;
	}
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemCode == null) ? 0 : itemCode.hashCode());
		result = prime * result + ((suppNo == null) ? 0 : suppNo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuppVo other = (SuppVo) obj;
		if (itemCode == null) {
			if (other.itemCode != null)
				return false;
		} else if (!itemCode.equals(other.itemCode))
			return false;
		if (suppNo == null) {
			if (other.suppNo != null)
				return false;
		} else if (!suppNo.equals(other.suppNo))
			return false;
		return true;
	}	
	
	
}
