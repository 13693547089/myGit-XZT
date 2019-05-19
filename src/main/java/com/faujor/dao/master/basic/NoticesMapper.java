package com.faujor.dao.master.basic;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.basic.NoticesDO;

public interface NoticesMapper {

	/**
	 * 查询获取通知数据
	 * 
	 * @param notices
	 * @param rb
	 * @return
	 */
	List<NoticesDO> findNoticesForPage(NoticesDO notices, RowBounds rb);

	/**
	 * 根据ID获取通知信息
	 * 
	 * @param id
	 * @return
	 */
	NoticesDO findNoticeById(String id);

	/**
	 * 保存通知信息
	 * 
	 * @param notice
	 * @return
	 */
	int saveNotice(NoticesDO notice);

	/**
	 * 更新成功通知的数量
	 * 
	 * @param id
	 */
	int updateSuccessNum(String id);

	/**
	 * 更新已读的数量
	 * 
	 * @param id
	 */
	int updateReadNum(String id);

}
