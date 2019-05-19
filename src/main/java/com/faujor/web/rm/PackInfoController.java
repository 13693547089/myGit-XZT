package com.faujor.web.rm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.rm.CutMatePackForm;
import com.faujor.entity.rm.PackOrderForm;
import com.faujor.entity.rm.PackOrderVO;
import com.faujor.entity.rm.SemiMatePack;
import com.faujor.entity.rm.SemiMatePackForm;
import com.faujor.service.rm.PackInfoService;
import com.faujor.utils.ExportExcel;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class PackInfoController {

	@Autowired
	private PackInfoService packInfoService;
	
	/**
	 * 跳转到半成品包材统计表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSemiMatePackHtml")
	public String getSemiMatePackHtml(Model model) {
		
		return "rm/pack/semiMatePackList";
	}
	
	/**
	 * 获取半成品包材统计表
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value ="获取半成品包材统计表")
	@ResponseBody
	@RequestMapping("/getSemiMatePackList")
	public Map<String, Object> getSemiMatePackList(SemiMatePackForm semiMate,Integer limit,Integer page) {
		if(limit == null){limit=10;}
		if(page == null){page=1;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("semiMate", semiMate);
		return packInfoService.getSemiMatePackList(map);
	}
	
	
	/**
	 * 导出数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportSemiMatePackList")
	public String exportSemiMatePackList(String objjson,
			HttpServletRequest req, HttpServletResponse res){
		ServletOutputStream os = null;
		try {
			// 获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String time = sdf.format(date);
			// 文件名从称
			SysUserDO user = UserCommon.getUser();
			String userType = user.getUserType();
			String username = user.getUsername();
			if (!StringUtils.isEmpty(userType) && userType.equals("supplier")) {
				username = user.getSuppNo();
			}
			//导出excel的文件名称
			String fileName = username + "-半成品包材统计表-" + time + ".xlsx";
			//查询数据
			Map<String, Object> map = new HashMap<String, Object>();
			SemiMatePackForm semiForm = JsonUtils.jsonToPojo(objjson, SemiMatePackForm.class);
			map.put("semiMate", semiForm);
			List<SemiMatePack> list = packInfoService.getSemimatePacks(map);
			//导出数据
			Workbook wb = ExportExcel.exportSemiMatePackList(list, req, res);
			ExportExcel.setAttachmentFileName(req, res, fileName);
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
	
	
	/**
	 * 跳转到打切包材统计表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCutMatePackHtml")
	public String getCutMatePackHtml(Model model) {
		
		return "rm/pack/cutMatePackList";
	}
	
	/**
	 * 获取抬头字段信息
	 * @param cutMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCutMatePackListFields")
	public String getCutMatePackListFields(String cutMonth) {
		
		return packInfoService.getCutMatePackListFields(cutMonth);
	}
	
	/**
	 * 获取打切包材统计表
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value ="获取打切包材统计表")
	@ResponseBody
	@RequestMapping("/getCutMatePackList")
	public Map<String, Object> getCutMatePackList(CutMatePackForm cutMateForm,Integer limit,Integer page) {
		if(limit == null){limit=10;}
		if(page == null){page=1;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("cutMateForm", cutMateForm);
		return packInfoService.getCutMatePackListByPage(map);
	}
	
	

	/**
	 * 导出数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportCutMatePackList")
	public String exportCutMatePackList(String objjson,
			HttpServletRequest req, HttpServletResponse res){
		ServletOutputStream os = null;
		try {
			// 获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String time = sdf.format(date);
			// 文件名从称
			SysUserDO user = UserCommon.getUser();
			String userType = user.getUserType();
			String username = user.getUsername();
			if (!StringUtils.isEmpty(userType) && userType.equals("supplier")) {
				username = user.getSuppNo();
			}
			//导出excel的文件名称
			String fileName = username + "-打切包材统计表-" + time + ".xlsx";
			//查询数据
			Map<String, Object> map = new HashMap<String, Object>();
			CutMatePackForm cutMatePackForm = JsonUtils.jsonToPojo(objjson, CutMatePackForm.class);
			map.put("cutMateForm", cutMatePackForm);
			Map<String, Object> result = packInfoService.getCutMatePacks(map);
			//导出数据
			Workbook wb = ExportExcel.exportCutMatePackList(result, req, res);
			ExportExcel.setAttachmentFileName(req, res, fileName);
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
	
	
	/**
	 * 跳转到包材订单执行情况统计表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/getPackOrderListHtml")
	public String getPackOrderListHtml(Model model) {
		
		return "rm/pack/packOrderList";
	}
	
	/**
	 * 包材订单执行情况统计表
	 * @param packOrderForm
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value ="包材订单执行情况统计表")
	@ResponseBody
	@RequestMapping("/getPackOrderListByPage")
	public Map<String, Object> getPackOrderListByPage(PackOrderForm packOrderForm,Integer limit,Integer page) {
		if(limit == null){limit=10;}
		if(page == null){page=1;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("packOrderForm", packOrderForm);
		return packInfoService.getPackOrderListByPage(map);
	}
	

	/**
	 * 导出数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportPackOrderList")
	public String exportPackOrderList(String objjson,
			HttpServletRequest req, HttpServletResponse res){
		ServletOutputStream os = null;
		try {
			// 获取时间年月日时分秒拼接作为文件名
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String time = sdf.format(date);
			// 文件名从称
			SysUserDO user = UserCommon.getUser();
			String userType = user.getUserType();
			String username = user.getUsername();
			if (!StringUtils.isEmpty(userType) && userType.equals("supplier")) {
				username = user.getSuppNo();
			}
			//导出excel的文件名称
			String fileName = username + "-包材订单执行情况统计表-" + time + ".xlsx";
			//查询数据
			Map<String, Object> map = new HashMap<String, Object>();
			PackOrderForm packOrderForm = JsonUtils.jsonToPojo(objjson, PackOrderForm.class);
			if(packOrderForm != null) {
				Date startDate = packOrderForm.getStartDate();
				Date endDate = packOrderForm.getEndDate();
				if(startDate != null && endDate!= null) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String formatStart = simpleDateFormat.format(startDate);
					String formatEnd = simpleDateFormat.format(endDate);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parseStart = format.parse(formatStart+" 00:00:00");
					Date parseEnd = format.parse(formatEnd+" 00:00:00");
					packOrderForm.setStartDate(parseStart);
					packOrderForm.setEndDate(parseEnd);
				}
				
			}
			map.put("packOrderForm", packOrderForm);
			List<PackOrderVO> list = packInfoService.getPackOrderList(map);
			//导出数据
			Workbook wb = ExportExcel.exportPackOrderList(list, req, res);
			ExportExcel.setAttachmentFileName(req, res, fileName);
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
