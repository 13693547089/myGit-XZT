package com.faujor.entity.privileges;

import java.io.Serializable;

public class UserDO implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String userName;
	private String password;
	private String email;
	private String mobile;
	private int status;
	private String name;
	private long leader;
	private String sfcodes;
	private String plainCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLeader() {
		return leader;
	}

	public void setLeader(long leader) {
		this.leader = leader;
	}

	public String getSfcodes() {
		return sfcodes;
	}

	public void setSfcodes(String sfcodes) {
		this.sfcodes = sfcodes;
	}

	public String getPlainCode() {
		return plainCode;
	}

	public void setPlainCode(String plainCode) {
		this.plainCode = plainCode;
	}
}
