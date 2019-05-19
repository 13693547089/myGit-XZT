package com.faujor.entity.fam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 对账金额
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactsMone implements Serializable
{

	private static final long serialVersionUID = -8415940388811746058L;
	private String fid;
	private String mainID;
	private String amouPaya;
	private String amouRece;
	private String warrMone;
	private String remarks;
	private String unseAcco;
	private String prepAcco;

	public String getFid()
	{
		return fid;
	}

	public void setFid(String fid)
	{
		this.fid = fid;
	}

	public String getMainID()
	{
		return mainID;
	}

	public void setMainID(String mainID)
	{
		this.mainID = mainID;
	}

	public String getPrepAcco()
	{
		return prepAcco;
	}

	public void setPrepAcco(String prepAcco)
	{
		this.prepAcco = prepAcco;
	}

	public String getAmouPaya()
	{
		return amouPaya;
	}

	public void setAmouPaya(String amouPaya)
	{
		this.amouPaya = amouPaya;
	}

	public String getAmouRece()
	{
		return amouRece;
	}

	public void setAmouRece(String amouRece)
	{
		this.amouRece = amouRece;
	}

	public String getWarrMone()
	{
		return warrMone;
	}

	public void setWarrMone(String warrMone)
	{
		this.warrMone = warrMone;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public String getUnseAcco()
	{
		return unseAcco;
	}

	public void setUnseAcco(String unseAcco)
	{
		this.unseAcco = unseAcco;
	}

}

