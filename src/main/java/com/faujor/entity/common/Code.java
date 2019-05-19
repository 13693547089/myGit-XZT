package com.faujor.entity.common;

import java.util.Date;

public class Code {
	private String Id;
	private String codeType;
	private String prefix;
	private Date lastTime;
	private Integer suffixLength;
	private Integer suffixNum;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public Integer getSuffixLength() {
		return suffixLength;
	}
	public void setSuffixLength(Integer suffixLength) {
		this.suffixLength = suffixLength;
	}
	public Integer getSuffixNum() {
		return suffixNum;
	}
	public void setSuffixNum(Integer suffixNum) {
		this.suffixNum = suffixNum;
	}
	public Code() {
		super();
	}
	public Code(String id, String codeType, String prefix, Date lastTime, Integer suffixLength, Integer suffixNum) {
		super();
		Id = id;
		this.codeType = codeType;
		this.prefix = prefix;
		this.lastTime = lastTime;
		this.suffixLength = suffixLength;
		this.suffixNum = suffixNum;
	}
	@Override
	public String toString() {
		return "Code [Id=" + Id + ", codeType=" + codeType + ", prefix=" + prefix + ", lastTime=" + lastTime
				+ ", suffixLength=" + suffixLength + ", suffixNum=" + suffixNum + "]";
	}
}
