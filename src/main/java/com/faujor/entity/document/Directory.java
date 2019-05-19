package com.faujor.entity.document;

import java.util.Date;

/**
 * 文档目录
 * @author hql
 *
 */
public class Directory {
//	id
	private String Id;
//	路径编码
	private String direCode;
//	路径名称
	private String direName;
//	全编码
	private String direFcode;
//	路径全名称
	private String direFname;
//	父节点
	private String parentId;
//	创建人
	private String createUser;
//	创建时间
	private Date createTime;
//	修改人
	private String modifyUser;
//	修改时间
	private Date modifyTime;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getDireCode() {
		return direCode;
	}
	public void setDireCode(String direCode) {
		this.direCode = direCode;
	}
	public String getDireName() {
		return direName;
	}
	public void setDireName(String direName) {
		this.direName = direName;
	}
	public String getDireFcode() {
		return direFcode;
	}
	public void setDireFcode(String direFcode) {
		this.direFcode = direFcode;
	}
	public String getDireFname() {
		return direFname;
	}
	public void setDireFname(String direFname) {
		this.direFname = direFname;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
