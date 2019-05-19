package com.faujor.service.common;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.common.AsyncLog;

public interface AsyncLogService {
	/**
	 * 保存同步日志
	 * 
	 * @param al
	 * @return
	 */
	int saveAsyncLog(AsyncLog al);

	/**
	 * 更新同步记录
	 * 
	 * @param al
	 * @return
	 */
	int updateAsyncLog(AsyncLog al);
	
	/**
	 * 获取同步日志
	 * @param al
	 * @param rb 
	 * @return
	 */
	Map<String, Object> findAsyncLogList(AsyncLog al, RowBounds rb);

	/**
	 * 保存同步日志2
	 * @param al
	 */
	public void saveAsyncLog2(AsyncLog al);
}
