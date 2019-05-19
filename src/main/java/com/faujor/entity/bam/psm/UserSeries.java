package com.faujor.entity.bam.psm;

import java.io.Serializable;

/**
 *  实体类：PadPlanDetail
 * @tableName mdm_user_series
 * @ClassName:   UserSeries
 * @Instructions:
 * @author: Vincent
 * @date:   2018年8月31日 下午2:09:51
 *
 */
public class UserSeries implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userCode;
	private String userName;
	private String userId;
	private String seriesCode;
	private String seriesExpl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSeriesCode() {
		return seriesCode;
	}
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public String getSeriesExpl() {
		return seriesExpl;
	}
	public void setSeriesExpl(String seriesExpl) {
		this.seriesExpl = seriesExpl;
	}
	
}
