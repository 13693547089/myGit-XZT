package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true) ///将这个注解写在类上之后，就会忽略类中不存在的字段
public class OrderEnclosure implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String fid;
	private String mainId;
	private String appeType;
	private String appeName;
	private String appeFile;
	private String newName;
	private String fileId;
	private String fileUrl;


	public OrderEnclosure(String fid, String mainId, String appeType, String appeName, String appeFile, String newName,
			String fileId, String fileUrl)
	{
		super();
		this.fid = fid;
		this.mainId = mainId;
		this.appeType = appeType;
		this.appeName = appeName;
		this.appeFile = appeFile;
		this.newName = newName;
		this.fileId = fileId;
		this.fileUrl = fileUrl;
	}

	public String getNewName()
	{
		return newName;
	}

	public void setNewName(String newName)
	{
		this.newName = newName;
	}

	public String getFileId()
	{
		return fileId;
	}

	public void setFileId(String fileId)
	{
		this.fileId = fileId;
	}

	public String getFileUrl()
	{
		return fileUrl;
	}

	public void setFileUrl(String fileUrl)
	{
		this.fileUrl = fileUrl;
	}

	public OrderEnclosure()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFid()
	{
		return fid;
	}

	public void setFid(String fid)
	{
		this.fid = fid;
	}

	public String getMainId()
	{
		return mainId;
	}

	public void setMainId(String mainId)
	{
		this.mainId = mainId;
	}

	public String getAppeType()
	{
		return appeType;
	}

	public void setAppeType(String appeType)
	{
		this.appeType = appeType;
	}

	public String getAppeName()
	{
		return appeName;
	}

	public void setAppeName(String appeName)
	{
		this.appeName = appeName;
	}

	public String getAppeFile()
	{
		return appeFile;
	}

	public void setAppeFile(String appeFile)
	{
		this.appeFile = appeFile;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public String toString()
	{
		return "OrderEnclosure [fid=" + fid + ", mainId=" + mainId + ", appeType=" + appeType + ", appeName=" + appeName
				+ ", appeFile=" + appeFile + ", newName=" + newName + ", fileId=" + fileId + ", fileUrl=" + fileUrl + "]";
	}









}
