package com.faujor.entity.document;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 文件上传的文件信息
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Document implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
//	文件路径
	private String fileUrl;
//	文件名称
	private String fileName;
//	文件原始名称
	private String realName;
//	目录编码
	private String direCode;
//	文件类型
	private String docType;
//	文件大小
	private String docSize;
//	文件类型(关联模块)
	private String docCate;
//	业务关联字段
	private String linkNo;
//	关联单据ID
	private String linkId;
//	创建人
	private String createUser;
//	创建人真是姓名
	private String creater;
//	创建时间
	private Date createTime;
//	修改人
	private String modifyUser;
//	修改时间
	private Date modifyTime;
//	附件类型  文本  合同等
	private String  attachType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getDireCode() {
		return direCode;
	}
	public void setDireCode(String direCode) {
		this.direCode = direCode;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getDocSize() {
		return docSize;
	}
	public void setDocSize(String docSize) {
		this.docSize = docSize;
	}
	public String getDocCate() {
		return docCate;
	}
	public void setDocCate(String docCate) {
		this.docCate = docCate;
	}
	public String getLinkNo() {
		return linkNo;
	}
	public void setLinkNo(String linkNo) {
		this.linkNo = linkNo;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAttachType() {
		return attachType;
	}
	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	
}
