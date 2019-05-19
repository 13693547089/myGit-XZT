package com.faujor.entity.mdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OemPackSupp {
	private String id;
	private String oemSuppCode;
	private String oemSuppName;
	private String packSuppCode;
	private String packSuppName;
	private String mateCode;
	private String mateName;

	private String selected_col;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOemSuppCode() {
		return oemSuppCode;
	}

	public void setOemSuppCode(String oemSuppCode) {
		this.oemSuppCode = oemSuppCode;
	}

	public String getOemSuppName() {
		return oemSuppName;
	}

	public void setOemSuppName(String oemSuppName) {
		this.oemSuppName = oemSuppName;
	}

	public String getPackSuppCode() {
		return packSuppCode;
	}

	public void setPackSuppCode(String packSuppCode) {
		this.packSuppCode = packSuppCode;
	}

	public String getPackSuppName() {
		return packSuppName;
	}

	public void setPackSuppName(String packSuppName) {
		this.packSuppName = packSuppName;
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

	public String getSelected_col() {
		return selected_col;
	}

	public void setSelected_col(String selected_col) {
		this.selected_col = selected_col;
	}
}
