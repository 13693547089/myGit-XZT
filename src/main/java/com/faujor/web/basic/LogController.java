package com.faujor.web.basic;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.basic.LogDO;
import com.faujor.service.basic.LogService;

@Controller
@RequestMapping("/basic")
public class LogController {

	@Autowired
	private LogService logService;

	@RequestMapping("/logIndex")
	public String logInxex() {

		return "basic/log/logList";
	}

	/**
	 * 获取日志列表的信息
	 * 
	 * @return
	 */
	@RequestMapping("/getLogList")
	@ResponseBody
	public Map<String, Object> getLogList(LogDO logDO, Integer limit, Integer page) {
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return logService.getLogPage(logDO, rb);
	}

	/**
	 * 获取改用户的详细操作信息
	 * 
	 * @param model
	 * @param userName
	 * @return
	 */
	@RequestMapping("/getLogDetails")
	public String getLogDetails(Model model, String userName) {
		LogDO logDO = logService.getLogInfoByUserName(userName);
		model.addAttribute("logDO", logDO);
		return "basic/log/logDetails";
	}

	/**
	 * 获取登录详情
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping("/getLogDetailsByUserName")
	@ResponseBody
	public Map<String, Object> getLogDetailsByUserName(Integer limit, Integer page, String userName) {
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return logService.findLogDetailsByUserName(rb, userName);
	}

	/**
	 * 报表首页
	 * 
	 * @return
	 */
	@RequestMapping("/reportIndex")
	public String reportIndex() {

		return "basic/report/reportIndex";
	}

}
