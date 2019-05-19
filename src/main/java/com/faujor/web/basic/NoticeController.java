package com.faujor.web.basic;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.basic.NoticesDO;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.basic.NoticesService;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/notices")
public class NoticeController {

	@Autowired
	private NoticesService noticesService;

	@RequestMapping("/notice")
	public String noticeList() {

		return "/basic/notice/notice";
	}

	/**
	 * 通知历史列表
	 * 
	 * @param notices
	 * @param limit
	 * @param page
	 * @return
	 */
	@RequestMapping("/findNoticesList")
	@ResponseBody
	public Map<String, Object> findNoticesList(NoticesDO notices, Integer limit, Integer page) {
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return noticesService.findNoticesList(notices, rb);
	}

	/**
	 * 进入通知页面或者创建通知
	 * 
	 * @param model
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping("/noticeInfo")
	public String noticeInfo(Model model, String id, String type) {
		NoticesDO notice = new NoticesDO();
		if ("create".equals(type)) {
			id = UUIDUtil.getUUID();
			notice.setId(id);
			notice.setNoticeTime(new Date());
			SysUserDO user = UserCommon.getUser();
			notice.setUserName(user.getUsername());
			notice.setName(user.getName());
		} else {
			// read 的状态下，则计算已经发送到了在线的数量
			noticesService.updateSuccessNum(id);
			notice = noticesService.findNoticeById(id);
		}
		model.addAttribute("notice", notice);
		model.addAttribute("type", type);
		return "/basic/notice/noticeDialog";
	}

	@RequestMapping("/saveNotice")
	@ResponseBody
	public RestCode saveNotice(NoticesDO notice) {
		int i = noticesService.saveNotice(notice);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@RequestMapping("/readNotice")
	@ResponseBody
	public RestCode readNotice(String id) {
		int i = noticesService.updateReadNum(id);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

}
