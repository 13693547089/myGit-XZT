package com.faujor.dao.master.common;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.common.AsyncContentLogDO;
import com.faujor.entity.common.AsyncLog;

public interface AsyncLogMapper {

	/**
	 * 插入同步记录
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
	 * 同步记录列表
	 * 
	 * @param al
	 * @param rb
	 * @return
	 */
	List<AsyncLog> findAsyncLogList(AsyncLog al, RowBounds rb);

	int countAsyncLog(AsyncLog al);

	/**
	 * 保存同步内容
	 * 
	 * @param content
	 * @return
	 */
	int saveAsyncContent(AsyncContentLogDO content);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	AsyncContentLogDO findAsyncContentById(String id);

}
