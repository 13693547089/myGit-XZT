package com.faujor.entity.sapcenter.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 实体类：OraCxjhEntity
 * @tableName T_ORA_CXJH
 * @tableDesc 物料信息中间表
 * @author Vincent
 * @date 2018-03-10
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OraCxjhEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String year;	//年份
	private String month;	//月
	private String matnr;	//物料编码
	private String zabc;	//月销售ABC
	private float zxspj;	//前三月平均销售量
	private float zsntq;	//上年度同期销量
	private String zsykc;	//上月结账库存
	private String zbyjh;	//本月交货计划
	private String zsjsc;	//本月实际生产
	private String zbyxl;	//本月销量
	private float zyjrk;	//本月预计入库
	private float zygxl;	//本月预估销量
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getZabc() {
		return zabc;
	}
	public void setZabc(String zabc) {
		this.zabc = zabc;
	}
	public float getZxspj() {
		return zxspj;
	}
	public void setZxspj(float zxspj) {
		this.zxspj = zxspj;
	}
	public float getZsntq() {
		return zsntq;
	}
	public void setZsntq(float zsntq) {
		this.zsntq = zsntq;
	}
	public String getZsykc() {
		return zsykc;
	}
	public void setZsykc(String zsykc) {
		this.zsykc = zsykc;
	}
	public String getZbyjh() {
		return zbyjh;
	}
	public void setZbyjh(String zbyjh) {
		this.zbyjh = zbyjh;
	}
	public String getZsjsc() {
		return zsjsc;
	}
	public void setZsjsc(String zsjsc) {
		this.zsjsc = zsjsc;
	}
	public String getZbyxl() {
		return zbyxl;
	}
	public void setZbyxl(String zbyxl) {
		this.zbyxl = zbyxl;
	}
	public float getZyjrk() {
		return zyjrk;
	}
	public void setZyjrk(float zyjrk) {
		this.zyjrk = zyjrk;
	}
	public float getZygxl() {
		return zygxl;
	}
	public void setZygxl(float zygxl) {
		this.zygxl = zygxl;
	}
}
