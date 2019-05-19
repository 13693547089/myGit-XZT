package com.faujor.entity.bam;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CutPlanMate implements Serializable {

	private static final long serialVersionUID = 1L;
	//	打切计划物料主键
	private String planMateId;	
//	打切计划主键
	private String planId;	
//	物料编号
	private String mateId;
//	物料名称
	private String mateName;
//	打切月份月底成品库存+在途
	private BigDecimal nowInve;
//	打切月份月底在外订单
	private BigDecimal outInve;
//	打切月份加一预测
	private BigDecimal addOne;
//	打切月份加二预测
	private BigDecimal addTwo;
//	打切月份加三预测
	private BigDecimal addThree;
//	打切月份加四预测
	private BigDecimal addFour;
//	打切月份加五预测
	private BigDecimal addFive;
//	打切月份加六预测
	private BigDecimal addSix;
//	剩余量
	private BigDecimal residue;
//	预计替换时间
	private String replaceDate;
//	打切品的打切目的
	private String cutGoal;
//	打切进度
	private String cutSche;
//	备注
	private String remark;
	//打切品的版本
	private String mateVersion;
	//主包材数
	private BigDecimal mainStruNum;
	//供应商成品库存
	private BigDecimal inveNum;
	//打切品的主包材  类号+ 类名
	private String mainStru;//这个是打切品中物料对应的主包材
	//字段+属性值
	private String fields;
	private String isSpecial;//是否是特殊打切品
	
	private String bigItemCode;//大品项代码
	private String bigItemExpl;//大品相说明
	
	private String mainStru2;//打切联络单中物料对应的主包材
	private String isChange;//比较打切品中物料对应的主包材和打切联络单中物料对应的主包材，相同为：same,不相同为：different
	/**
	 * @return the mainStru
	 */
	public String getMainStru() {
		return mainStru;
	}
	/**
	 * @param mainStru the mainStru to set
	 */
	public void setMainStru(String mainStru) {
		this.mainStru = mainStru;
	}
	/**
	 * @return the fields
	 */
	public String getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}
	/**
	 * @return the mateVersion
	 */
	public String getMateVersion() {
		return mateVersion;
	}
	/**
	 * @param mateVersion the mateVersion to set
	 */
	public void setMateVersion(String mateVersion) {
		this.mateVersion = mateVersion;
	}
	/**
	 * @return the mainStruNum
	 */
	public BigDecimal getMainStruNum() {
		return mainStruNum;
	}
	/**
	 * @param mainStruNum the mainStruNum to set
	 */
	public void setMainStruNum(BigDecimal mainStruNum) {
		this.mainStruNum = mainStruNum;
	}
	/**
	 * @return the inveNum
	 */
	public BigDecimal getInveNum() {
		return inveNum;
	}
	/**
	 * @param inveNum the inveNum to set
	 */
	public void setInveNum(BigDecimal inveNum) {
		this.inveNum = inveNum;
	}
	public String getPlanMateId() {
		return planMateId;
	}
	public void setPlanMateId(String planMateId) {
		this.planMateId = planMateId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getMateId() {
		return mateId;
	}
	public void setMateId(String mateId) {
		this.mateId = mateId;
	}
	public String getMateName() {
		return mateName;
	}
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	public BigDecimal getNowInve() {
		return nowInve;
	}
	public void setNowInve(BigDecimal nowInve) {
		this.nowInve = nowInve;
	}
	public BigDecimal getOutInve() {
		return outInve;
	}
	public void setOutInve(BigDecimal outInve) {
		this.outInve = outInve;
	}
	public BigDecimal getAddOne() {
		return addOne;
	}
	public void setAddOne(BigDecimal addOne) {
		this.addOne = addOne;
	}
	public BigDecimal getAddTwo() {
		return addTwo;
	}
	public void setAddTwo(BigDecimal addTwo) {
		this.addTwo = addTwo;
	}
	public BigDecimal getAddThree() {
		return addThree;
	}
	public void setAddThree(BigDecimal addThree) {
		this.addThree = addThree;
	}
	public BigDecimal getAddFour() {
		return addFour;
	}
	public void setAddFour(BigDecimal addFour) {
		this.addFour = addFour;
	}
	public BigDecimal getAddFive() {
		return addFive;
	}
	public void setAddFive(BigDecimal addFive) {
		this.addFive = addFive;
	}
	public BigDecimal getAddSix() {
		return addSix;
	}
	public void setAddSix(BigDecimal addSix) {
		this.addSix = addSix;
	}
	public BigDecimal getResidue() {
		return residue;
	}
	public void setResidue(BigDecimal residue) {
		this.residue = residue;
	}
	public String getReplaceDate() {
		return replaceDate;
	}
	public void setReplaceDate(String replaceDate) {
		this.replaceDate = replaceDate;
	}
	public String getCutGoal() {
		return cutGoal;
	}
	public void setCutGoal(String cutGoal) {
		this.cutGoal = cutGoal;
	}
	public String getCutSche() {
		return cutSche;
	}
	public void setCutSche(String cutSche) {
		this.cutSche = cutSche;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the isSpecial
	 */
	public String getIsSpecial() {
		return isSpecial;
	}
	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}
	
	
	/**
	 * @return the bigItemCode
	 */
	public String getBigItemCode() {
		return bigItemCode;
	}
	/**
	 * @param bigItemCode the bigItemCode to set
	 */
	public void setBigItemCode(String bigItemCode) {
		this.bigItemCode = bigItemCode;
	}
	/**
	 * @return the bigItemExpl
	 */
	public String getBigItemExpl() {
		return bigItemExpl;
	}
	/**
	 * @param bigItemExpl the bigItemExpl to set
	 */
	public void setBigItemExpl(String bigItemExpl) {
		this.bigItemExpl = bigItemExpl;
	}
	/**
	 * @return the mainStru2
	 */
	public String getMainStru2() {
		return mainStru2;
	}
	/**
	 * @param mainStru2 the mainStru2 to set
	 */
	public void setMainStru2(String mainStru2) {
		this.mainStru2 = mainStru2;
	}
	/**
	 * @return the isChange
	 */
	public String getIsChange() {
		return isChange;
	}
	/**
	 * @param isChange the isChange to set
	 */
	public void setIsChange(String isChange) {
		this.isChange = isChange;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutPlanMate [planMateId=" + planMateId + ", planId=" + planId + ", mateId=" + mateId + ", mateName="
				+ mateName + ", nowInve=" + nowInve + ", outInve=" + outInve + ", addOne=" + addOne + ", addTwo="
				+ addTwo + ", addThree=" + addThree + ", addFour=" + addFour + ", addFive=" + addFive + ", addSix="
				+ addSix + ", residue=" + residue + ", replaceDate=" + replaceDate + ", cutGoal=" + cutGoal
				+ ", cutSche=" + cutSche + ", remark=" + remark + ", mateVersion=" + mateVersion + ", mainStruNum="
				+ mainStruNum + ", inveNum=" + inveNum + ", mainStru=" + mainStru + ", fields=" + fields
				+ ", isSpecial=" + isSpecial + ", bigItemCode=" + bigItemCode + ", bigItemExpl=" + bigItemExpl
				+ ", mainStru2=" + mainStru2 + ", isChange=" + isChange + "]";
	}
	
	
}
