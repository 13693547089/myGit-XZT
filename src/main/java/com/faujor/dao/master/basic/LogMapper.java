package com.faujor.dao.master.basic;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.basic.LogDO;

public interface LogMapper {

	/**
	 * 根据logdo获取日志list
	 * 
	 * @param rb
	 * @param logDO
	 * @return
	 */
	List<LogDO> findLogListByLogDO(RowBounds rb, LogDO logDO);

	int countLogListByLogDO(LogDO logDO);

	/**
	 * 保存系统日志
	 * 
	 * @param sysLog
	 * @return
	 */
	int saveSysLog(LogDO sysLog);

	/**
	 * 根据用户登录名获取用户登录信息
	 * 
	 * @param userName
	 * @return
	 */
	LogDO findLogInfoByUserName(String userName);

	/**
	 * 获取用户的所有登录信息
	 * 
	 * @param rb
	 * @param userName
	 * @return
	 */
	List<LogDO> findLogDetailsByUserName(RowBounds rb, String userName);

	int countLogDetailsByUserName(String userName);

}
