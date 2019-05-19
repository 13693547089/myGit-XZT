package com.faujor.entity.fam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactsMain implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String fid;
	private String status;
	private String recoNumb;
	private String suppName;
	private String suppNumb;
	private String founder;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date creatTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date closDate;
	private String currBankPaym;
	private String inteAmou;
	private String bankTota;
	private String advaInteAmou;
	private String dataText;
	private List<ContactsMone> mone;
	private List<ContactsMones> mones;

	public String getFid()
	{
		return fid;
	}

	public void setFid(String fid)
	{
		this.fid = fid;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getRecoNumb()
	{
		return recoNumb;
	}

	public void setRecoNumb(String recoNumb)
	{
		this.recoNumb = recoNumb;
	}

	public String getSuppName()
	{
		return suppName;
	}

	public void setSuppName(String suppName)
	{
		this.suppName = suppName;
	}

	public String getSuppNumb()
	{
		return suppNumb;
	}

	public void setSuppNumb(String suppNumb)
	{
		this.suppNumb = suppNumb;
	}

	public String getFounder()
	{
		return founder;
	}

	public void setFounder(String founder)
	{
		this.founder = founder;
	}

	public Date getCreatTime()
	{
		return creatTime;
	}

	public void setCreatTime(Date creatTime)
	{
		this.creatTime = creatTime;
	}

	public Date getClosDate()
	{
		return closDate;
	}

	public void setClosDate(Date closDate)
	{
		this.closDate = closDate;
	}

	public String getCurrBankPaym()
	{
		return currBankPaym;
	}

	public void setCurrBankPaym(String currBankPaym)
	{
		this.currBankPaym = currBankPaym;
	}

	public String getInteAmou()
	{
		return inteAmou;
	}

	public void setInteAmou(String inteAmou)
	{
		this.inteAmou = inteAmou;
	}

	public String getBankTota()
	{
		return bankTota;
	}

	public void setBankTota(String bankTota)
	{
		this.bankTota = bankTota;
	}

	public String getAdvaInteAmou()
	{
		return advaInteAmou;
	}

	public String getDataText()
	{
		return dataText;
	}

	public void setDataText(String dataText)
	{
		this.dataText = dataText;
	}

	public void setAdvaInteAmou(String advaInteAmou)
	{
		this.advaInteAmou = advaInteAmou;
	}

	public List<ContactsMone> getMone()
	{
		return mone;
	}

	public void setMone(List<ContactsMone> mone)
	{
		this.mone = mone;
	}

	public List<ContactsMones> getMones()
	{
		return mones;
	}

	public void setMones(List<ContactsMones> mones)
	{
		this.mones = mones;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public String toString()
	{
		return "ContactsMain [fid=" + fid + ", status=" + status + ", recoNumb=" + recoNumb + ", suppName=" + suppName
				+ ", suppNumb=" + suppNumb + ", founder=" + founder + ", creatTime=" + creatTime + ", closDate=" + closDate
				+ ", currBankPaym=" + currBankPaym + ", inteAmou=" + inteAmou + ", bankTota=" + bankTota + ", advaInteAmou="
				+ advaInteAmou + ", mone=" + mone + ", mones=" + mones + "]";
	}



}
