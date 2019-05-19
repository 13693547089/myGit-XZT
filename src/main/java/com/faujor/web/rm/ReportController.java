package com.faujor.web.rm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.rm.BillReport;
import com.faujor.entity.rm.BillReportDetails;
import com.faujor.entity.rm.LoginOperateVO;
import com.faujor.entity.rm.LoginReport;
import com.faujor.entity.rm.SqlParams;
import com.faujor.service.privileges.OrgService;
import com.faujor.service.rm.ReportService;
import com.faujor.utils.PageUtils;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportService reportService;
	@Autowired
	private OrgService orgService;

	/**
	 * 单据记录统计
	 * 
	 * @return
	 */
	@RequestMapping("/bill_report")
	public String billReport(Model model) {
		// 上月的
		Date current = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(current);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		Date time = calendar.getTime();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(time);
		String dateStr = format + " - " + format;
		model.addAttribute("currentDate", sd.format(current));
		model.addAttribute("dateStr", dateStr);
		model.addAttribute("br", new BillReport());
		return "/rm/report/billReport";
	}

	/**
	 * 局部刷新单记录统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param model
	 * @return
	 */
	@RequestMapping("/refresh_data")
	public String refreshData(String startDate, String endDate, Model model) {
		BillReport br = reportService.getBillReport(startDate, endDate);
		model.addAttribute("br", br);
		return "/rm/report/billReport::data_refresh";
	}

	/**
	 * 单记录详情信息
	 * 
	 * @param dateStr
	 * @param model
	 * @return
	 */
	@RequestMapping("/bill_report_details")
	public String billReportDetails(String dateStr, Model model) {
		Date current = new Date();
		String format = getLastMonth(null, "month");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		model.addAttribute("currentDate", sd.format(current));
		model.addAttribute("dateStr", format);
		return "/rm/report/billReportDetails";
	}

	/**
	 * 单记录详情的数据
	 * 
	 * @param dateStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bill_report_details_data")
	public Map<String, Object> detailsData(String dateStr) {
		String firstMonth = StringUtils.isEmpty(dateStr) ? getLastMonth(dateStr, "month") : dateStr;
		String secondMonth = getLastMonth(firstMonth, "month");
		String thirdMonth = getLastMonth(secondMonth, "month");
		List<BillReportDetails> list = reportService.findDetailsData(firstMonth, secondMonth, thirdMonth);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		String[] split = firstMonth.split("-");
		map.put("month", Integer.parseInt(split[1]));
		return map;
	}

	private String getLastMonth(String dateStr, String type) {
		Date date = null;
		if (StringUtils.isEmpty(dateStr)) {
			date = new Date();
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 上月的
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String format = "";
		if ("month".equals(type)) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
			Date time = calendar.getTime();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
			format = sd.format(time);
		} else if ("day".equals(type)) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
			Date time = calendar.getTime();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			format = sd.format(time);
		}
		return format;
	}

	/**
	 * 登录操作统计报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/login_report")
	public String loginReport(Model model) {
		Date current = new Date();
		String format = getLastMonth(null, "month");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		model.addAttribute("currentDate", sd.format(current));
		model.addAttribute("dateStr", format);
		model.addAttribute("lr", new LoginReport());
		return "/rm/report/loginReport";
	}

	/**
	 * 登录，操作次数 环比
	 * 
	 * @param dateStr
	 * @return
	 */
	@RequestMapping("/login_report_ratio")
	public String loginReportRatio(String dateStr, Model model) {
		SqlParams sqlParams = new SqlParams();
		dateStr = StringUtils.isEmpty(dateStr) ? getLastMonth(null, "month") : dateStr;
		sqlParams.setFirstMonth(dateStr);
		String secondMonth = getLastMonth(dateStr, "month");
		sqlParams.setSecondMonth(secondMonth);
		String thirdMonth = getLastMonth(secondMonth, "month");
		sqlParams.setThirdMonth(thirdMonth);
		String fourMonth = getLastMonth(thirdMonth, "month");
		sqlParams.setFourMonth(fourMonth);
		LoginReport lr = reportService.findLoginReportRatio(sqlParams);
		String[] split = dateStr.split("-");
		lr.setFirst_login_month(split[1]);
		lr.setFirst_operate_month(split[1]);
		String[] split2 = secondMonth.split("-");
		lr.setSecond_login_month(split2[1]);
		lr.setSecond_operate_month(split2[1]);
		String[] split3 = thirdMonth.split("-");
		lr.setThird_login_month(split3[1]);
		lr.setThird_operate_month(split3[1]);
		model.addAttribute("lr", lr);
		return "/rm/report/loginReport::ratio_div";
	}

	/**
	 * 登录，操作次数
	 * 
	 * @param dateStr
	 * @param model
	 * @return
	 */
	@RequestMapping("/login_operate_frequence")
	@ResponseBody
	public List<List<Object>> loginOperateFrequence(String dateStr) {
		SqlParams sqlParams = new SqlParams();
		String firstMonth = StringUtils.isEmpty(dateStr) ? getLastMonth(null, "month") : dateStr;
		sqlParams.setFirstMonth(firstMonth);
		String secondMonth = getLastMonth(firstMonth, "month");
		sqlParams.setSecondMonth(secondMonth);
		String thirdMonth = getLastMonth(secondMonth, "month");
		sqlParams.setThirdMonth(thirdMonth);
		String fourMonth = getLastMonth(thirdMonth, "month");
		sqlParams.setFourMonth(fourMonth);
		String fiveMonth = getLastMonth(fourMonth, "month");
		sqlParams.setFiveMonth(fiveMonth);
		String sixMonth = getLastMonth(fiveMonth, "month");
		sqlParams.setSixMonth(sixMonth);
		List<List<Object>> list = reportService.findLoginOperateFrequence(sqlParams);
		return list;
	}

	/**
	 * top 10
	 * 
	 * @param sqlParams
	 * @return
	 */
	@RequestMapping("/login_operate_top_ten")
	@ResponseBody
	public List<List<Object>> loginOperateTopTen(SqlParams sqlParams) {
		String dateStr = sqlParams.getDate_str();
		dateStr = StringUtils.isEmpty(dateStr) ? getLastMonth(null, "month") : dateStr;
		sqlParams.setDate_str(dateStr);
		List<List<Object>> list = reportService.findTopTenInfo(sqlParams);
		return list;
	}

	/**
	 * 采购订单报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/purchase_order")
	public String purchaseOrder(Model model) {
		Date current = new Date();
		String format = getLastMonth(null, "month");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		model.addAttribute("currentDate", sd.format(current));
		model.addAttribute("dateStr", format);
		return "/rm/report/purchaseOrderReport";
	}

	/**
	 * 采购订单报表数据
	 * 
	 * @param dateStr
	 * @param model
	 * @return
	 */
	@RequestMapping("/purchase_order_data")
	@ResponseBody
	public List<List<Object>> purchaseOrderData(String dateStr) {

		List<List<Object>> list = reportService.findPurchaseOrderData(dateStr);
		return list;
	}

	/**
	 * 产销管理报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/product_market")
	public String productMarket(Model model) {
		Date current = new Date();
		String format = getLastMonth(null, "month");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		model.addAttribute("currentDate", sd.format(current));
		model.addAttribute("dateStr", format);
		return "/rm/report/productMarketReport";
	}

	/**
	 * 产销管理报表数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/product_market_data")
	@ResponseBody
	public List<List<Object>> productMarketData(String dateStr, Model model) {
		List<List<Object>> list = reportService.findProductMarketData(dateStr);
		return list;
	}

	/**
	 * 登录操作 日志
	 * 
	 * @return
	 */
	@RequestMapping("/login_index")
	public String loginIndex(Model model) {
		Date date = new Date();
		String lastDay = getLastMonth(null, "day");
		model.addAttribute("defaultDay", lastDay + " - " + lastDay);
		model.addAttribute("currentDay", date);
		List<OrgDo> list = orgService.findOrgsByOrgCode("dept");
		model.addAttribute("deptList", list);
		return "/rm/report/loginLog";
	}

	/**
	 * 用户 登录操作 详情
	 * 
	 * @param day_date
	 * @param user_type
	 * @param user_dept
	 * @param username
	 * @return
	 */
	@RequestMapping("/login_list")
	@ResponseBody
	public PageUtils<LoginOperateVO> loginList(String day_date, String user_type, String user_dept, String username) {
		String startDay, endDay;
		if (StringUtils.isEmpty(day_date)) {
			startDay = endDay = getLastMonth(null, "day");
		} else {
			String[] split = day_date.split(" - ");
			startDay = split[0];
			endDay = split[1];
		}
		return reportService.findLoginList(startDay, endDay, user_type, user_dept, username);
	}

	/**
	 * 登录操作明细
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/login_details_index")
	public String loginDetailsIndex(Model model, String username, String day_date, Long userId) {
		
		model.addAttribute("username", username);
		model.addAttribute("day_date", day_date);
		model.addAttribute("userId", userId);
		return "/rm/report/loginLogDetails";
	}
	
	@RequestMapping("/login_details")
	@ResponseBody
	public PageUtils<LoginOperateVO> loginDetails(String username, String day_date, Long userId) {
		String startDay, endDay;
		if (StringUtils.isEmpty(day_date)) {
			startDay = endDay = getLastMonth(null, "day");
		} else {
			String[] split = day_date.split(" - ");
			startDay = split[0];
			endDay = split[1];
		}
		return reportService.findLoginDetails(startDay, endDay, username, userId);
	}

}
