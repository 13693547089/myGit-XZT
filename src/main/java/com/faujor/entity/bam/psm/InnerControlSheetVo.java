package com.faujor.entity.bam.psm;

import java.util.ArrayList;
import java.util.List;
/**
 * 内部管控表sheet数据
 * @author 
 * 2018年9月7日 上午10:42:14
 * InnerControlSheetVo.java
 */
public class InnerControlSheetVo {

	private List<InnerControl> suppList=new ArrayList<>();
	private List<InnerControl> mateList=new ArrayList<>();
	private String prodSeriesCode;
	private String prodSeriesDesc;
	public List<InnerControl> getSuppList() {
		return suppList;
	}
	public void setSuppList(List<InnerControl> suppList) {
		this.suppList = suppList;
	}
	public List<InnerControl> getMateList() {
		return mateList;
	}
	public void setMateList(List<InnerControl> mateList) {
		this.mateList = mateList;
	}
	public String getProdSeriesCode() {
		return prodSeriesCode;
	}
	public void setProdSeriesCode(String prodSeriesCode) {
		this.prodSeriesCode = prodSeriesCode;
	}
	public String getProdSeriesDesc() {
		return prodSeriesDesc;
	}
	public void setProdSeriesDesc(String prodSeriesDesc) {
		this.prodSeriesDesc = prodSeriesDesc;
	}

}
