package com.faujor.entity.common;

import java.io.Serializable;

public class KvSequenceDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String keyName;
	private long valName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public long getValName() {
		return valName;
	}
	public void setValName(long valName) {
		this.valName = valName;
	}
}
