package com.faujor.entity.privileges;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;

public class OrgDo extends SerializableSerializer {
	private static final long serialVersionUID = 1L;
	private Long menuId;// 实际上是sid，这里只是为了对应js中的树型结构
	private String sname;
	private String scode;// 编码
	private String suppNo;// 内部员工编号或者是供应商编码
	private String sfname;// 全路径带后缀(.ogn)
	private String sfid;
	private String sfcode;
	private String sfnames;// 全路径不带后缀
	private String sfids;
	private String sfcodes;
	private String stype;
	private long parentId;
	private String snodeKind;
	private long spersonId;
	private Date screateTime;
	private String screator;
	private String screateName;
	private Date supdateTime;
	private String smodifier;
	private String smodifyName;
	// 角色
	private List<Long> roleIds;

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getSuppNo() {
		return suppNo;
	}

	public void setSuppNo(String suppNo) {
		this.suppNo = suppNo;
	}

	public String getSfname() {
		return sfname;
	}

	public void setSfname(String sfname) {
		this.sfname = sfname;
	}

	public String getSfid() {
		return sfid;
	}

	public void setSfid(String sfid) {
		this.sfid = sfid;
	}

	public String getSfcode() {
		return sfcode;
	}

	public void setSfcode(String sfcode) {
		this.sfcode = sfcode;
	}

	public String getSfnames() {
		return sfnames;
	}

	public void setSfnames(String sfnames) {
		this.sfnames = sfnames;
	}

	public String getSfids() {
		return sfids;
	}

	public void setSfids(String sfids) {
		this.sfids = sfids;
	}

	public String getSfcodes() {
		return sfcodes;
	}

	public void setSfcodes(String sfcodes) {
		this.sfcodes = sfcodes;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getSnodeKind() {
		return snodeKind;
	}

	public void setSnodeKind(String snodeKind) {
		this.snodeKind = snodeKind;
	}

	public long getSpersonId() {
		return spersonId;
	}

	public void setSpersonId(long spersonId) {
		this.spersonId = spersonId;
	}

	public Date getScreateTime() {
		return screateTime;
	}

	public void setScreateTime(Date screateTime) {
		this.screateTime = screateTime;
	}

	public String getScreator() {
		return screator;
	}

	public void setScreator(String screator) {
		this.screator = screator;
	}

	public String getScreateName() {
		return screateName;
	}

	public void setScreateName(String screateName) {
		this.screateName = screateName;
	}

	public Date getSupdateTime() {
		return supdateTime;
	}

	public void setSupdateTime(Date supdateTime) {
		this.supdateTime = supdateTime;
	}

	public String getSmodifier() {
		return smodifier;
	}

	public void setSmodifier(String smodifier) {
		this.smodifier = smodifier;
	}

	public String getSmodifyName() {
		return smodifyName;
	}

	public void setSmodifyName(String smodifyName) {
		this.smodifyName = smodifyName;
	}

}
