package com.faujor.entity.common;

import java.io.Serializable;



import org.springframework.stereotype.Component;

/**
 * Token 的 Model 类，可以增加字段提高安全性，例如url 签名
 * @author 
 * @date 2015/7/31.
 */
@Component
public class User implements Serializable{
	private static final long serialVersionUID = -6315750265007224061L;
	// 用户 id
    private String id;
    // 用户名
    private String username;
    // password
    private String password;
    
    public User() {
    	super();
    }
    public User(String username,String password) {
    	super();
    	this.username = username;
		this.password = password;
    }
    public User(String id,String username, String password) {
    	super();
    	this.id = id;
    	this.username = username;
    	this.password = password;
    }
    public User(String id) {
		super();
		this.id = id;
	}
	public String getUsername() {
    	return username;
    }
    public void setUsername(String username) {
    	this.username = username;
    }
    public String getPassword() {
    	return password;
    }
    public void setPassword(String password) {
    	this.password = password;
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    
}