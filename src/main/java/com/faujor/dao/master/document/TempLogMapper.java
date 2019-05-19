package com.faujor.dao.master.document;

import java.util.List;

import com.faujor.entity.document.TempLog;

public interface TempLogMapper {
	/**
	 * 添加模板操作的日志
	 * @param log
	 */
	void saveTempLog(TempLog log);
	/**
	 * 获取模板的日志列表
	 * @param tempNo
	 * @return
	 */
	List<TempLog> getTempLogByTempNo(String tempNo);
	/**
	 * 根据模板编号删除模板日志记录
	 * @param tempNo
	 */
	void delLogByTempNo(String tempNo);
}
