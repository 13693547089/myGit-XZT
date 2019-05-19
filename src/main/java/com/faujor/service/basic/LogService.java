package com.faujor.service.basic;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.basic.LogDO;

public interface LogService {

	/**
	 * 获取日志列表数据
	 * 
	 * @param logDO
	 * @param rb
	 * @return
	 */
	Map<String, Object> getLogPage(LogDO logDO, RowBounds rb);

	/**
	 * 根据用户登录名获取登录信息
	 * 
	 * @param userName
	 * @return
	 */
	LogDO getLogInfoByUserName(String userName);

	/**
	 * 获取用户的所有登录记录
	 * @param rb 
	 * 
	 * @param userName
	 * @return
	 */
	Map<String, Object> findLogDetailsByUserName(RowBounds rb, String userName);

}
