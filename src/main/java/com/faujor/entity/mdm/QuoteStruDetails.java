package com.faujor.entity.mdm;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteStruDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String menuId;// 参考的模板详情的id
	private String struId;
	private String segmName;
	private String segmCode;
	private String asseName;
	private String asseCode;
	private String mateCode;
	private String detailsNum;
	private String unit;
	private String standard;
	private String material;
	private String remark;
	private String parentId;
	private String nodeKind;
	private Date createTime;
	private String creator;
	private String creatorName;
	private Date modifyTime;
	private String modifier;
	private String modifierName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getStruId() {
		return struId;
	}

	public void setStruId(String struId) {
		this.struId = struId;
	}

	public String getSegmName() {
		return segmName;
	}

	public void setSegmName(String segmName) {
		this.segmName = segmName;
	}

	public String getSegmCode() {
		return segmCode;
	}

	public void setSegmCode(String segmCode) {
		this.segmCode = segmCode;
	}

	public String getAsseName() {
		return asseName;
	}

	public void setAsseName(String asseName) {
		this.asseName = asseName;
	}

	public String getAsseCode() {
		return asseCode;
	}

	public void setAsseCode(String asseCode) {
		this.asseCode = asseCode;
	}

	public String getMateCode() {
		return mateCode;
	}

	public void setMateCode(String mateCode) {
		this.mateCode = mateCode;
	}

	public String getDetailsNum() {
		return detailsNum;
	}

	public void setDetailsNum(String detailsNum) {
		this.detailsNum = detailsNum;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNodeKind() {
		return nodeKind;
	}

	public void setNodeKind(String nodeKind) {
		this.nodeKind = nodeKind;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
}
