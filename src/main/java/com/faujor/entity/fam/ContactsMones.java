package com.faujor.entity.fam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 对账分支
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactsMones implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String fid;
	private String mainID;
	private String mone;
	private String remarks;
	private String tempEsti;

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

	public String getMone()
	{
		return mone;
	}

	public void setMone(String mone)
	{
		this.mone = mone;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public String getTempEsti()
	{
		return tempEsti;
	}

	public void setTempEsti(String tempEsti)
	{
		this.tempEsti = tempEsti;
	}
}
