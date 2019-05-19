package com.faujor.web.bam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.dao.master.bam.AppointMapper;
import com.faujor.entity.bam.AppoQueryDO;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.ReceiveMessage;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.TaskDO;
import com.faujor.service.bam.AppointService;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExportAppointList;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class AppointIssueController {

	@Autowired
	private AppointService appointService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ReceiveMessageService receiveMessageService;
	@Autowired
	private AppointMapper AppointMapper;

	/**
	 * 跳转到预约发布页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppointIssueListHtml")
	public String getAppointIssueListHtml(Model model) {
		List<Dic> priorityList = basicService.findDicListByCategoryCode("YXJ");
		List<Dic> expectlist = basicService.findDicListByCategoryCode("QWSHSJ");
		List<Dic> list = basicService.findDicListByCategoryCode("YYZT");
		List<Dic> appoStatusList = new ArrayList<Dic>();
		for (Dic d : list) {
			String status = d.getDicName();
			if ("已确认".equals(status) || "已发布".equals(status) || "待发货".equals(status) || "已发货".equals(status)) {
				appoStatusList.add(d);
			}
		}
		model.addAttribute("expectlist", expectlist);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("appoStatusList", appoStatusList);
		return "bam/appoIssue/appointIssueList";
	}

	/**
	 * 预约发布分页展示数据
	 *
	 * @param appo
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value = "获取预约发布列表")
	@ResponseBody
	@RequestMapping("/queryAppointForIssueByPage")
	public Map<String, Object> queryAppointForIssueByPage(String statusJson, Appoint appo, Integer limit,
			Integer page) {
		if (limit == null) {
			limit = 10;
		}
		if (page == null) {
			page = 1;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		if (statusJson != null) {
			List<String> statusList = JsonUtils.jsonToList(statusJson, String.class);
			if (statusList.size() > 0) {
				map.put("statusList", statusList);
			}
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = format.format(date);
		appo.setCreateDateStart(strDate + " 00:00:00");
		appo.setCreateDateEnd(strDate + " 23:59:59");
		SysUserDO user = UserCommon.getUser();
		map.put("post", user.getUserId().toString());
		map.put("start", start);
		map.put("end", end);
		map.put("appo", appo);
		Map<String, Object> page2 = appointService.queryAppointForIssueByPage(map);
		return page2;
	}

	/**
	 * 跳转到预约发布页面
	 *
	 * @return
	 */
	@RequestMapping("/getAppointIssueHtml")
	public String getAppointIssueHtml(Model model, String appoId, String type) {
		TaskDO task = taskService.getTask(appoId);
		model.addAttribute("task", task);
		List<Dic> expectlist = basicService.findDicListByCategoryCode("QWSHSJ");
		List<Dic> affirmtlist = basicService.findDicListByCategoryCode("QWSHSJ");
		List<Dic> priorityList = basicService.findDicListByCategoryCode("YXJ");
		Appoint appo = appointService.queryAppointByAppoId(appoId);
		// 查询预约日期当日已发布的预约在每个时间段的状况
		List<Appoint> list = appointService.queryAppointForIssueByAppoDate(appo.getAppoDate());
		// 8:00-10:00
		int eightCount = 0;
		// 10:00-13:00
		int tenCount = 0;
		// 13:00-15:00
		int thirCount = 0;
		// 15:00-16:00
		int fiftCount = 0;
		// 16:00-
		int sixCount = 0;
		for (Appoint a : list) {
			if ("8:00-10:00".equals(a.getAffirmDate())) {
				eightCount++;
			} else if ("10:00-13:00".equals(a.getAffirmDate())) {
				tenCount++;
			} else if ("13:00-15:00".equals(a.getAffirmDate())) {
				thirCount++;
			} else if ("15:00-16:00".equals(a.getAffirmDate())) {
				fiftCount++;
			} else if ("16:00-".equals(a.getAffirmDate())) {
				sixCount++;
			}
		}
		for (Dic d : affirmtlist) {
			if ("8:00-10:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + eightCount);
			} else if ("10:00-13:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + tenCount);
			} else if ("13:00-15:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + thirCount);
			} else if ("15:00-16:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + fiftCount);
			} else if ("16:00-".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + sixCount);
			}

		}
		for (Dic d : affirmtlist) {
			if ("8:00-10:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + eightCount);
			} else if ("10:00-13:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + tenCount);
			} else if ("13:00-15:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + thirCount);
			} else if ("15:00-16:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + fiftCount);
			} else if ("16:00-".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + sixCount);
			}

		}

		model.addAttribute("type", type);
		model.addAttribute("appo", appo);
		model.addAttribute("affirmtlist", affirmtlist);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("expectlist", expectlist);
		return "bam/appoIssue/appointIssue";
	}

	/**
	 * type=2：预约发布和type=1:预约保存
	 *
	 * @param appo
	 * @param type
	 * @return
	 */
	@Log(value = "发布/保存预约申请")
	@ResponseBody
	@RequestMapping("/updateAffirmDate")
	public Map<String, Object> updateAffirmDate(Appoint appo, String funtype) {
		SysUserDO user = UserCommon.getUser();
		String affirmDate = appo.getAffirmDate();
		int i = affirmDate.indexOf(" ");
		String affirmDate2 = affirmDate.substring(0, i);
		appo.setAffirmDate(affirmDate2);
		if ("2".equals(funtype)) {// 发布
			appo.setAppoStatus("已发布");
			appo.setCdcPublId(user.getUserId().toString());
			appo.setCdcPublDate(new Date());
			appo.setCdcPublStatus("同意");
		}
		return appointService.updateAffirmDate(appo,funtype);
	}

	/**
	 * 预约发布弹出框
	 *
	 * @return
	 */
	@RequestMapping("/getAppoIssuePopHtml")
	public String getAppoIssuePopHtml() {
		return "bam/appoIssue/appoIssuePop";
	}

	/**
	 * 预约发布列表上的发布与保存
	 *
	 * @param dateJson
	 * @param type
	 * @return
	 */
	@Log(value = "发布/保存预约申请")
	@ResponseBody
	@RequestMapping("/updateAffirmDateByAppoId")
	public boolean updateAffirmDateByAppoId(String dateJson, String type) {
		List<Appoint> list = JsonUtils.jsonToList(dateJson, Appoint.class);
		boolean b = appointService.updateAffirmDateByAppoId(list, type);
		return b;
	}

	/**
	 * 预约查询弹出框
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppoQueryPopHtml")
	public String getAppoQueryPopHtml(Model model, String isCdc) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		// calendar.add(calendar.DATE,-1);//-1:把日期往前减少一天，表示昨天
		calendar.add(calendar.DATE, 1);// 1:把日期向后推一天,表示明天
		date = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		String strDate = str+" - "+str;
		List<ReceiveMessage> list = receiveMessageService.findReceiveAddr();
		model.addAttribute("cdcAddrList", list);
		// List<Appoint> list = appointService.queryAppoStatByAppoDate(str);
		if ("yes".equals(isCdc)) {
			SysUserDO user = UserCommon.getUser();
			model.addAttribute("cdc", user.getUserId());
		} else {
			model.addAttribute("cdc", "");
		}
		model.addAttribute("tomorrow", strDate);
		return "bam/appoIssue/appoQueryPop";
	}

	/**
	 * 根据日期搜索
	 *
	 * @param appo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAppointStatByAppoDate")
	public Map<String, Object> queryAppointStatByAppoDate(String cdc, String appoDate) {
		// SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		// String str = format.format(appoDate);
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(appoDate)){
			map.put("dateStr", appoDate);
		}else{
			String[] split = appoDate.split(" - ");
			if(split[0].equals(split[1])){//查询一天的数据
				map.put("dateStr", split[0]);
			}else{//查询某一时间范围的数据
				map.put("startDate", split[0]);
				map.put("endDate", split[1]);
			}
		}
		map.put("cdc", cdc);
		List<AppoQueryDO> list = appointService.queryAppoReportByParams(map);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("count", 0);
		result.put("code", 0);
		result.put("msg", "");
		return result;
	}

	/**
	 * 导出
	 *
	 */

	@RequestMapping("/exportAppointList")
	public String exportAppointList(String objjson, HttpServletRequest req, HttpServletResponse res) {
		ServletOutputStream os = null;
		try {
			// 获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String time = sdf.format(date);
			// 文件名从称
			SysUserDO user = UserCommon.getUser();
			String userType = user.getUserType();
			String username = user.getUsername();
			if (!StringUtils.isEmpty(userType) && userType.equals("supplier")) {
				username = user.getSuppNo();
			}
			String fileName = username + "-预约发布信息-" + time + ".xlsx";
			// List<String> list = JsonUtils.jsonToList(Fids, String.class);
			// 根据费用编号的查询费用信息，返回一个List集合
			Appoint appoint = JsonUtils.jsonToPojo(objjson, Appoint.class);
			if (appoint != null) {
				String startstr = DateUtils.format(appoint.getStartDate(), "yyyy/MM/dd");
				String endstr = DateUtils.format(appoint.getEndDate(), "yyyy/MM/dd");
				appoint.setReceUnit(startstr);
				appoint.setReceAddr(endstr);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appo", appoint);
			map.put("post", user.getUserId().toString());
			List<Appoint> appList = AppointMapper.queryManyAppointByList(map);

			/*
			 * String sheetName = new
			 * SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			 *
			 * // 新建文件 HSSFWorkbook wb = new HSSFWorkbook(); // 设置列头样式
			 * HSSFCellStyle columnHeadStyle = wb.createCellStyle();
			 * columnHeadStyle.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.
			 * index); // 新建工作表 HSSFSheet sheet = wb.createSheet(sheetName);
			 */
			// 获取表头，例如：无锡CDC送货预约表
			String receUnit = receiveMessageService.queryReceUnitbyPost(user.getUserId());
			receUnit = receUnit + "送货预约表";
			Workbook wb = ExportAppointList.exportAppointList(receUnit, appList, req, res);
			ExportAppointList.setAttachmentFileName(req, res, fileName);
			os = res.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
