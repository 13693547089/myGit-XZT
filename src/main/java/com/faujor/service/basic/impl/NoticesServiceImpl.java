package com.faujor.service.basic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.basic.NoticesMapper;
import com.faujor.entity.basic.NoticesDO;
import com.faujor.service.basic.NoticesService;

@Service("noticesService")
public class NoticesServiceImpl implements NoticesService {

	@Autowired
	private NoticesMapper noticesMapper;

	@Override
	public Map<String, Object> findNoticesList(NoticesDO notices, RowBounds rb) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<NoticesDO> list = noticesMapper.findNoticesForPage(notices, rb);
		result.put("data", list);
		result.put("count", 0);
		result.put("msg", "0");
		result.put("code", "0");
		return result;
	}

	@Override
	public NoticesDO findNoticeById(String id) {
		return noticesMapper.findNoticeById(id);
	}

	@Override
	public int saveNotice(NoticesDO notice) {
		notice.setSuccessNum(0);
		notice.setReadNum(0);
		return noticesMapper.saveNotice(notice);
	}

	@Override
	public void updateSuccessNum(String id) {
		noticesMapper.updateSuccessNum(id);
	}

	@Override
	public int updateReadNum(String id) {
		return noticesMapper.updateReadNum(id);
	}

}
