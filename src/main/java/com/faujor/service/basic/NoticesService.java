package com.faujor.service.basic;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.basic.NoticesDO;

public interface NoticesService {

	/**
	 * 查询列表信息
	 * 
	 * @param notices
	 * @param rb
	 * @return
	 */
	Map<String, Object> findNoticesList(NoticesDO notices, RowBounds rb);

	/**
	 * 根据id获取通知信息
	 * 
	 * @param id
	 * @return
	 */
	NoticesDO findNoticeById(String id);

	/**
	 * 保存通知
	 * 
	 * @param notice
	 * @return
	 */
	int saveNotice(NoticesDO notice);

	/**
	 * 更新成功数据
	 * 
	 * @param id
	 */
	void updateSuccessNum(String id);

	/**
	 * 更新已读数据
	 * 
	 * @param id
	 * @return
	 */
	int updateReadNum(String id);

}
