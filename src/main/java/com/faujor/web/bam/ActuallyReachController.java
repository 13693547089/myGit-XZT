package com.faujor.web.bam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.psm.ActuallyReach;
import com.faujor.entity.bam.psm.BusyStock;
import com.faujor.entity.bam.psm.InnerControl;
import com.faujor.entity.bam.psm.InnerControlSheetVo;
import com.faujor.entity.bam.psm.PdrDetailVo;
import com.faujor.entity.bam.psm.SuppVo;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.bam.ActReachService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.BigDecimalUtil;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.IoUtil;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UserCommon;

/**
 * 实际达成
 * @author hql
 *
 */
@Controller
@RequestMapping("/actReach")
public class ActuallyReachController {
	
	@Autowired
	private ActReachService actReachService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private MaterialService materialService;
	/**
	 * 获取实际达成的页面
	 * @return
	 */
	@RequestMapping("/getActReachPage")
	public String getActReachPage(){
		return "bam/ActuallyReach/actuallyReach";
	}
	/**
	 * 获取内部管控供应商弹框页面
	 * @param actReachJson
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSuppReachDg")
	public String getSuppReachDg(String mateCode,String mateDesc,String planMonth,BigDecimal nextDevNum,Model model,String endDate){
		Material mate = materialService.queryMaterialByMateCode(mateCode);
		model.addAttribute("mateCode", mateCode);
		model.addAttribute("mateDesc", mate.getSkuEnglish());
		model.addAttribute("planMonth", planMonth);
		model.addAttribute("nextDevNum", nextDevNum);
		model.addAttribute("endDate", endDate);
		return "bam/ActuallyReach/suppReachDg";
	}
	/**
	 * 查看产能上报的对话框
	 * @param mateCode
	 * @param mateDesc
	 * @param planMonth
	 * @param nextDevNum
	 * @param model
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/getSuppDlvDg")
	public String getSuppDlvDg(String mateCode,String mateDesc,String planMonth,BigDecimal nextDevNum,Model model,String endDate){
		Material mate = materialService.queryMaterialByMateCode(mateCode);
		model.addAttribute("mateCode", mateCode);
		model.addAttribute("mateDesc", mate.getSkuEnglish());
		model.addAttribute("planMonth", planMonth);
		model.addAttribute("nextDevNum", nextDevNum);
		model.addAttribute("endDate", endDate);
		Calendar cal=Calendar.getInstance();
		cal.setTime(DateUtils.parse(endDate, "yyyy-MM-dd"));
		cal.set(Calendar.DAY_OF_MONTH, 1) ;
		model.addAttribute("startDate", DateUtils.format(cal.getTime(),  "yyyy-MM-dd"));
		return "bam/ActuallyReach/suppDlvDg";
	}
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	/**
	 * 分页获取实际达成数据
	 * @param page
	 * @param mateDesc
	 * @param endDate
	 * @return
	 */
	@Log("查看实际达成报表")
	@ResponseBody
	@RequestMapping("/getActReachByPage")
	public LayuiPage<ActuallyReach> getActReachByPage(LayuiPage<ActuallyReach>  page,String mateDesc,Date endDate){
		Map<String, Object> map=new HashMap<String,Object>();
		Calendar cal=Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		Date nextMonth = cal.getTime();
		map.put("nextMonth",nextMonth);
		map.put("endDate",endDate );
		map.put("startDate",startDate );
		String planMonth=DateUtils.format(endDate, "yyyy-MM");
		map.put("planMonth", planMonth);
		
		//获取所有的管理人员
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		if(mateDesc!=null && !"".equals(mateDesc)){
			map.put("mateDesc", mateDesc);
		}
		String sfCode = user.getSfCode();
		if(sfCode!=null && sfCode.indexOf("PURCHAROR")!=-1){
			Map<String , Object> params=new HashMap<>();
			params.put("ownId", userId);
			params.put("isContainOwn", true);
			List<UserDO> users = orgService.manageSubordinateUsers(params);
			List<Long> userIds=new ArrayList<Long>();
			for (UserDO userDo : users) {
				userIds.add(userDo.getId());
			}
			map.put("userIds", userIds);
		}
		//获取所有的物料管理的所有物料
		page.calculatePage();
		map.put("page", page);
		LayuiPage<ActuallyReach> returnPage = actReachService.getActReachByPage(map);
		return returnPage;
	}
	/**
	 * 获取某个物料的供应商实际达成
	 * @param mateCode
	 * @param planMonth
	 * @return
	 */
	@Log("查看实际达成报表详情")
	@ResponseBody
	@RequestMapping("/getSuppActReach")
	public List<ActuallyReach> getSuppActReach(String mateCode,String planMonth,Date endDate){
		Map<String, Object> map=new HashMap<>();
		Calendar cal=Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		Date nextMonth = cal.getTime();
		map.put("endDate",endDate );
		map.put("startDate",startDate );
		map.put("planMonth", startDate);
		map.put("mateCode", mateCode);
		map.put("nextMonth", nextMonth);
		List<ActuallyReach> actReachList=new ArrayList<>();
//		List<QualSupp> supps = qualSuppService.queryQualSuppOfMateByMateCode(mateCode);
		List<QualSupp> supps = actReachService.getSuppByMateMonth(map);
		map.put("planMonth", planMonth);
		for (QualSupp qualSupp : supps) {
			ActuallyReach actReach=new ActuallyReach();
			actReach.setSuppNo(qualSupp.getSapId());
			actReach.setSuppName(qualSupp.getSuppName());
			actReachList.add(actReach);
		}
		map.put("list", actReachList);
		List<ActuallyReach> list = actReachService.getMateSuppActReach(map);
		return list;
	}
	/**
	 * 查看产能上报的数据
	 * @param mateCode
	 * @param suppName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPdrData")
	public List<PdrDetailVo> getPdrData(String mateCode,String suppName,Date startDate,Date endDate){
		Map<String, Object> map =new HashMap<>();
		map.put("mateCode", mateCode);
		map.put("suppName", suppName);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<PdrDetailVo> list = actReachService.getPrdDetailByMap(map);
		return list;
	}
	
	
	/**
	 * 导出实际达成报表
	 * @param request
	 * @param response
	 * @param endDate
	 * @param mateCode
	 * @return
	 */
	@RequestMapping("/exportActReach")
	public String exportActReach(HttpServletRequest request,HttpServletResponse response,Date endDate,String mateDesc){
		Map<String, Object> map=new HashMap<>();
		Calendar cal=Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		Date nextMonth = cal.getTime();
		map.put("endDate",endDate );
		map.put("startDate",startDate );
		map.put("planMonth", startDate);
		map.put("nextMonth", nextMonth);
		
		//获取所有的管理人员
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		if(mateDesc!=null && !"".equals(mateDesc)){
			map.put("mateDesc", mateDesc);
		}
		String sfCode = user.getSfCode();
		if(sfCode!=null && sfCode.indexOf("PURCHAROR")!=-1){
			Map<String , Object> params=new HashMap<>();
			params.put("ownId", userId);
			params.put("isContainOwn", true);
			List<UserDO> users = orgService.manageSubordinateUsers(params);
			List<Long> userIds=new ArrayList<Long>();
			for (UserDO userDo : users) {
				userIds.add(userDo.getId());
			}
			map.put("userIds", userIds);
		}
		List<ActuallyReach> data = actReachService.getExportActReach(map);
		String realName="实际达成报表.xls";
		//创建HSSFWorkbook对象(excel的文档对象)
	     HSSFWorkbook wb=null;
	     OutputStream os=null;
	     FileInputStream fis=null;
	     try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String realPath = filePath+"templates\\excelTemp\\实际达成报表模板.xls";
			fis=new FileInputStream(realPath);
			wb=new HSSFWorkbook(fis);
			//建立新的sheet对象（excel的表单）
			HSSFSheet sheet=wb.getSheetAt(0);						
			//获取第2行第16列的样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 1, 0);
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 1, 1);
			CellStyle cellStyle3 = ExcelUtil.getCellStyle(sheet, 1, 2);
			CellStyle cellStyle4 = ExcelUtil.getCellStyle(sheet, 1, 3);
			CellStyle cellStyle5 = ExcelUtil.getCellStyle(sheet, 1, 4);
			CellStyle cellStyle6 = ExcelUtil.getCellStyle(sheet, 1, 5);
			CellStyle cellStyle7 = ExcelUtil.getCellStyle(sheet, 1, 6);
			CellStyle cellStyle8 = ExcelUtil.getCellStyle(sheet, 1, 7);
			CellStyle cellStyle9 = ExcelUtil.getCellStyle(sheet, 1, 8);
			CellStyle cellStyle10 = ExcelUtil.getCellStyle(sheet, 1, 9);
			CellStyle cellStyle11 = ExcelUtil.getCellStyle(sheet, 1, 10);
			CellStyle cellStyle12 = ExcelUtil.getCellStyle(sheet, 1, 11);
			CellStyle cellStyle13 = ExcelUtil.getCellStyle(sheet, 1, 12);
			CellStyle cellStyle14 = ExcelUtil.getCellStyle(sheet, 1, 13);
			CellStyle cellStyle15 = ExcelUtil.getCellStyle(sheet, 1, 14);
			CellStyle cellStyle16 = ExcelUtil.getCellStyle(sheet, 1, 15);
			CellStyle cellStyle17 = ExcelUtil.getCellStyle(sheet, 1, 16);
			for (int i = 0; i < data.size(); i++) {
			  ActuallyReach actReach =data.get(i);
			  int rowNum=i+1;
			  ExcelUtil.setValue(sheet,  rowNum, 0, rowNum, cellStyle1);
			  ExcelUtil.setValue(sheet,  rowNum, 1, actReach.getProdSeriesDesc(), cellStyle2);
    		  ExcelUtil.setValue(sheet,  rowNum, 2,actReach.getItemName(), cellStyle3);
    		  ExcelUtil.setValue(sheet,  rowNum, 3, actReach.getSuppName(), cellStyle4);
    		  ExcelUtil.setValue(sheet,  rowNum, 4, actReach.getMateDesc(), cellStyle5);
    		  ExcelUtil.setValue(sheet,  rowNum, 5, actReach.getRank(), cellStyle6);
    		  ExcelUtil.setValue(sheet,  rowNum, 6, actReach.getPlanMonth(), cellStyle7);
    		  ExcelUtil.setValue(sheet,  rowNum, 7, BigDecimalUtil.getDoubleVal(actReach.getPlanPrdNum()), cellStyle8);
    		  ExcelUtil.setValue(sheet,  rowNum, 8, BigDecimalUtil.getDoubleVal(actReach.getActPrdNum()), cellStyle9);
    		  ExcelUtil.setValue(sheet,  rowNum, 9, actReach.getPudReachScale(), cellStyle10);
    		  ExcelUtil.setValue(sheet,  rowNum, 10,BigDecimalUtil.getDoubleVal(actReach.getPlanDlvNum()), cellStyle11);
    		  ExcelUtil.setValue(sheet,  rowNum, 11, BigDecimalUtil.getDoubleVal(actReach.getActDlvNum()), cellStyle12);
    		  ExcelUtil.setValue(sheet,  rowNum, 12, actReach.getDlvReachScale(), cellStyle13);
    		  ExcelUtil.setValue(sheet,  rowNum, 13, BigDecimalUtil.getDoubleVal(actReach.getSuppActNum()), cellStyle14);
    		  ExcelUtil.setValue(sheet,  rowNum, 14, actReach.getSafeScale(), cellStyle15);
    		  ExcelUtil.setValue(sheet,  rowNum, 15, actReach.getMateCode(), cellStyle16);
    		  ExcelUtil.setValue(sheet,  rowNum, 16, actReach.getSuppNo(), cellStyle17);
			}
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/x-msdownlocad");
			realName=URLEncoder.encode(realName, "utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+realName);
			os = response.getOutputStream();
			wb.write(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IoUtil.closeIo(os,fis,wb);
		}
		return null;
	}
	
	
	@Log("导出内部管控表")
	@RequestMapping("/exportInnerControl")
	public String exportInnerControl(HttpServletRequest request,HttpServletResponse response,String reconCode,Date endDate,String invenCode,String wFlag){
		//获取查询的开始日期和结束日期
		Map<String, Object> map=new HashMap<>();
		Calendar cal=Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		int maxNum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, maxNum);
		endDate = cal.getTime();
		map.put("endDate", endDate);
		map.put("startDate", startDate);
		map.put("planMonth", startDate);
		map.put("invenCode", invenCode);
		String planMonthStr = DateUtils.format(endDate, "yyyyMM");
		
		String realName="内部管控表"+planMonthStr+".xlsx";
		//获取内部管控表数据
		//获取所有的管理人员
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		
		String sfCode = user.getSfCode();
		if(sfCode!=null && sfCode.indexOf("PURCHAROR")!=-1){
			Map<String , Object> params=new HashMap<>();
			params.put("ownId", userId);
			params.put("isContainOwn", true);
			List<UserDO> users = orgService.manageSubordinateUsers(params);
			List<Long> userIds=new ArrayList<Long>();
			for (UserDO userDo : users) {
				userIds.add(userDo.getId());
			}
			map.put("userIds", userIds);
		}
		List<InnerControl> innerControls = actReachService.getSuppInnerControl(map);
		map.put("endDate", endDate);
		map.put("startDate", startDate);
		map.put("planMonth", startDate);
		List<InnerControl> mateInnerControls = actReachService.getMateInnerControl(map);
		//内部管控表sheet页展示-----------------------------开始
		List<InnerControlSheetVo> sheetDataList=new ArrayList<>();
		//第一个sheet页的汇总数据
		InnerControlSheetVo sheetData0=new InnerControlSheetVo();
		sheetData0.setMateList(mateInnerControls);
		sheetData0.setSuppList(innerControls);
		sheetData0.setProdSeriesDesc("汇总");
		sheetData0.setProdSeriesCode("汇总");
		sheetDataList.add(sheetData0);
		
		//处理供应商级别的数据
		Map<String, List<SuppVo>> suppSubMap=new HashMap<>();
		for (InnerControl innerControl:innerControls) {
			String prodSeriesCode = innerControl.getProdSeriesCode();
			String prodSeriesDesc = innerControl.getProdSeriesDesc();
			boolean flag=false;//sheet中不包含改系列
			for (InnerControlSheetVo innerControlSheetVo : sheetDataList) {
				String prodSeriesCode2 = innerControlSheetVo.getProdSeriesCode();
				if(StringUtil.equals(prodSeriesCode, prodSeriesCode2)){
					flag=true;
					innerControlSheetVo.getSuppList().add(innerControl);
				}
			}
			if(!flag){
				InnerControlSheetVo innerControlSheetVo=new InnerControlSheetVo();
				innerControlSheetVo.setProdSeriesCode(prodSeriesCode);
				innerControlSheetVo.setProdSeriesDesc(prodSeriesDesc);
				innerControlSheetVo.getSuppList().add(innerControl);
				sheetDataList.add(innerControlSheetVo);
			}
		}
//		处理物料级别的数据
		for (InnerControl innerControl:mateInnerControls) {
			String prodSeriesCode = innerControl.getProdSeriesCode();
			for (InnerControlSheetVo innerControlSheetVo : sheetDataList) {
				String prodSeriesCode2 = innerControlSheetVo.getProdSeriesCode();
				if(StringUtil.equals(prodSeriesCode, prodSeriesCode2)){
					innerControlSheetVo.getMateList().add(innerControl);
				}
			}
		}

		//内部管控表sheet页展示-----------------------------结束
		//创建HSSFWorkbook对象(excel的文档对象)
	     Workbook wb=null;
	     OutputStream os=null;
	     FileInputStream fis=null;
		try {
			
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String realPath = filePath+"templates\\excelTemp\\内部管控表样式.xlsx";
			fis=new FileInputStream(realPath);
			wb = new XSSFWorkbook(fis);
			//建立新的sheet对象（excel的表单）
			Sheet sheet=wb.getSheetAt(0);	
	  		Font font = wb.createFont();
	  		font.setBold(true);
			font.setFontName("微软雅黑");
			font.setFontHeightInPoints((short)9);
	
		    
			//获取第2行第16列的样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 3, 0);
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 3, 1);
			CellStyle cellStyle3 = ExcelUtil.getCellStyle(sheet, 3, 2);
			CellStyle cellStyle4 = ExcelUtil.getCellStyle(sheet, 3, 3);
			CellStyle cellStyle5 = ExcelUtil.getCellStyle(sheet, 3, 4);
			CellStyle cellStyle6 = ExcelUtil.getCellStyle(sheet, 3, 5);
			CellStyle cellStyle7 = ExcelUtil.getCellStyle(sheet, 3, 6);
			CellStyle cellStyle8 = ExcelUtil.getCellStyle(sheet, 3, 7);
			CellStyle cellStyle9 = ExcelUtil.getCellStyle(sheet, 3, 8);
			CellStyle cellStyle10 = ExcelUtil.getCellStyle(sheet, 3, 9);
			CellStyle cellStyle11 = ExcelUtil.getCellStyle(sheet, 3, 10);
			CellStyle cellStyle12 = ExcelUtil.getCellStyle(sheet, 3, 11);
			CellStyle cellStyle13 = ExcelUtil.getCellStyle(sheet, 3, 12);
			CellStyle cellStyle14 = ExcelUtil.getCellStyle(sheet, 3, 13);
			CellStyle cellStyle15 = ExcelUtil.getCellStyle(sheet, 3, 14);
			CellStyle cellStyle16 = ExcelUtil.getCellStyle(sheet, 3, 15);
			CellStyle cellStyle17 = ExcelUtil.getCellStyle(sheet, 3, 16);
			CellStyle cellStyle18 = ExcelUtil.getCellStyle(sheet, 3, 17);
			CellStyle cellStyle19 = ExcelUtil.getCellStyle(sheet, 3, 18);
			CellStyle cellStyle20 = ExcelUtil.getCellStyle(sheet, 3, 19);
			CellStyle cellStyle21 = ExcelUtil.getCellStyle(sheet, 3, 20);
			CellStyle cellStyle22 = ExcelUtil.getCellStyle(sheet, 3, 21);
			CellStyle cellStyle23 = ExcelUtil.getCellStyle(sheet, 3, 22);
			CellStyle cellStyle24 = ExcelUtil.getCellStyle(sheet, 3, 23);
			CellStyle cellStyle25 = ExcelUtil.getCellStyle(sheet, 3, 24);
			CellStyle cellStyle26 = ExcelUtil.getCellStyle(sheet, 3, 25);
			
			CellStyle cellStyle70 = ExcelUtil.getCellStyle(sheet, 3, 69);
			CellStyle cellStyle71 = ExcelUtil.getCellStyle(sheet, 3, 70);
			CellStyle cellStyle72 = ExcelUtil.getCellStyle(sheet, 3, 71);


			CellStyle cellStyleT1 = ExcelUtil.getCellStyle(sheet, 4, 0);
			CellStyle cellStyleT2 = ExcelUtil.getCellStyle(sheet, 4, 1);
			CellStyle cellStyleT3 = ExcelUtil.getCellStyle(sheet, 4, 2);
			CellStyle cellStyleT4 = ExcelUtil.getCellStyle(sheet, 4, 3);
			CellStyle cellStyleT5 = ExcelUtil.getCellStyle(sheet, 4, 4);
			CellStyle cellStyleT6 = ExcelUtil.getCellStyle(sheet, 4, 5);
			CellStyle cellStyleT7 = ExcelUtil.getCellStyle(sheet, 4, 6);
			CellStyle cellStyleT8 = ExcelUtil.getCellStyle(sheet, 4, 7);
			CellStyle cellStyleT9 = ExcelUtil.getCellStyle(sheet, 4, 8);
			CellStyle cellStyleT10 = ExcelUtil.getCellStyle(sheet,4, 9);
			CellStyle cellStyleT11 = ExcelUtil.getCellStyle(sheet, 4, 10);
			CellStyle cellStyleT12 = ExcelUtil.getCellStyle(sheet, 4, 11);
			CellStyle cellStyleT13 = ExcelUtil.getCellStyle(sheet, 4, 12);
			CellStyle cellStyleT14 = ExcelUtil.getCellStyle(sheet, 4, 13);
			CellStyle cellStyleT15 = ExcelUtil.getCellStyle(sheet, 4, 14);
			CellStyle cellStyleT16 = ExcelUtil.getCellStyle(sheet, 4, 15);
			CellStyle cellStyleT17 = ExcelUtil.getCellStyle(sheet, 4, 16);
			CellStyle cellStyleT18 = ExcelUtil.getCellStyle(sheet, 4, 17);
			CellStyle cellStyleT19 = ExcelUtil.getCellStyle(sheet, 4, 18);
			CellStyle cellStyleT20 = ExcelUtil.getCellStyle(sheet, 4, 19);
			CellStyle cellStyleT21 = ExcelUtil.getCellStyle(sheet, 4, 20);
			CellStyle cellStyleT22 = ExcelUtil.getCellStyle(sheet, 4, 21);
			CellStyle cellStyleT23 = ExcelUtil.getCellStyle(sheet, 4, 22);
			CellStyle cellStyleT24 = ExcelUtil.getCellStyle(sheet, 4, 23);
			CellStyle cellStyleT25 = ExcelUtil.getCellStyle(sheet, 4, 24);
			CellStyle cellStyleT26 = ExcelUtil.getCellStyle(sheet, 4, 25);
			
			CellStyle cellStyleT70 = ExcelUtil.getCellStyle(sheet, 4, 69);

			
			sheet.getRow(0).getCell(0).setCellValue("内部管控表"+planMonthStr);
			sheet.getRow(1).getCell(8).setCellValue(planMonthStr);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(22).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(26).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(30).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(34).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(38).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(42).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(46).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(50).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(54).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(58).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(62).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			cal.add(Calendar.MONTH,1);
			sheet.getRow(1).getCell(66).setCellValue(DateUtils.format(cal.getTime(), "yyyyMM"));
			for (int i = 0; i < sheetDataList.size(); i++) {
		    	 suppSubMap=new HashMap<>();

				InnerControlSheetVo sheetData = sheetDataList.get(i);
				List<InnerControl> suppList = sheetData.getSuppList();
				List<InnerControl> mateList = sheetData.getMateList();
				String prodSeriesDesc = sheetData.getProdSeriesDesc();
				if(prodSeriesDesc==null){
					prodSeriesDesc="未知系列";
				}
				Sheet dSheet =wb.getSheetAt(0);
				if(i!=0){
					dSheet = wb.createSheet(prodSeriesDesc);
					ExcelUtil.copyRows(sheet, dSheet, 1, 3, 0);
				}
				int size = suppList.size();
			      for (int j=0;j<size;j++) {
			    	  
			    	  InnerControl con=suppList.get(j);
			    	  String suppNo = con.getSuppNo();
						//------供应商合计相关数据(存放供应商对应的系列)开始
			    	    String itemCode = con.getItemCode();
						String itemName2 = con.getItemName();
						String suppName = con.getSuppName();
						List<SuppVo> list = suppSubMap.get(suppNo);
						if(list==null){
							list=new ArrayList<>();
						}
						SuppVo supp=new SuppVo();
						supp.setSuppNo(suppNo);
						supp.setSuppName(suppName);
						supp.setItemCode(itemCode);
						supp.setItemName(itemName2);
						if(!list.contains(supp)){
							list.add(supp);
						}
						suppSubMap.put(suppNo, list);
						//------供应商合计相关数据(存放供应商对应的系列)结束
			    
			    	  
			    	  int rowNum=3+j;
			    	  //公共和当月数据
			    	  if(j==size-1){
			    		  ExcelUtil.setValue(dSheet,  rowNum, 0, j+1, cellStyleT1);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 1, con.getSuppName(), cellStyleT2);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 2, con.getRank(), cellStyleT3);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 3, con.getMateDesc(), cellStyleT4);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 4, con.getProdSeriesDesc(), cellStyleT5);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 5, con.getItemName(), cellStyleT6);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 6, con.getBoxNumber(), cellStyleT7);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 7, con.getPackNumber(), cellStyleT8);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 8, BigDecimalUtil.getDoubleVal(con.getBeginOrder()), cellStyleT9);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 9, BigDecimalUtil.getDoubleVal(con.getBeginStock()), cellStyleT10);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 10, BigDecimalUtil.getDoubleVal(con.getBeginEnableOrder()), cellStyleT11);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 11, BigDecimalUtil.getDoubleVal(con.getPlanPrdNum()), cellStyleT12);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 12, BigDecimalUtil.getDoubleVal(con.getActPrdNum()), cellStyleT13);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 13, StringUtil.getDoubleValue(con.getPudReachScale()), cellStyleT14);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 14, BigDecimalUtil.getDoubleVal(con.getPlanDlvNum()), cellStyleT15);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 15, BigDecimalUtil.getDoubleVal(con.getActDlvNum()), cellStyleT16);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 16, StringUtil.getDoubleValue(con.getDlvReachScale()), cellStyleT17);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 17, BigDecimalUtil.getDoubleVal(con.getSuppDlvNum()), cellStyleT18);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 18, BigDecimalUtil.getDoubleVal(con.getSuppActNum()), cellStyleT19);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 19, StringUtil.getDoubleValue(con.getSafeScale()), cellStyleT20);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 20, BigDecimalUtil.getDoubleVal(con.getEndStock()), cellStyleT21);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 21, StringUtil.getDoubleValue(con.getPlanSafeScale()), cellStyleT22);
			    		  //推迟12个月的数据
			    		  //推迟12个月的数据(1)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 22, BigDecimalUtil.getDoubleVal(con.getAddOnePlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 23, BigDecimalUtil.getDoubleVal(con.getAddOnePlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 24, BigDecimalUtil.getDoubleVal(con.getAddOnePlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 25, StringUtil.getDoubleValue(con.getAddOneSafeScale()), cellStyleT26);
			    		  //推迟12个月的数据(2)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 26, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 27, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 28, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 29, StringUtil.getDoubleValue(con.getAddTwoSafeScale()), cellStyleT26);
			    		  //推迟12个月的数据(3)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 30, BigDecimalUtil.getDoubleVal(con.getAddThreePlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 31, BigDecimalUtil.getDoubleVal(con.getAddThreePlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 32, BigDecimalUtil.getDoubleVal(con.getAddThreePlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 33, StringUtil.getDoubleValue(con.getAddThreeSafeScale()), cellStyleT26);
			    		  //推迟12个月的数据(4)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 34, BigDecimalUtil.getDoubleVal(con.getAddFourPlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 35, BigDecimalUtil.getDoubleVal(con.getAddFourPlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 36, BigDecimalUtil.getDoubleVal(con.getAddFourPlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 37, StringUtil.getDoubleValue(con.getAddFourSafeScale()), cellStyleT26);
			    		  //推迟12个月的数据(5)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 38, BigDecimalUtil.getDoubleVal(con.getAddFivePlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 39, BigDecimalUtil.getDoubleVal(con.getAddFivePlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 40, BigDecimalUtil.getDoubleVal(con.getAddFivePlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 41, StringUtil.getDoubleValue(con.getAddFiveSafeScale()), cellStyleT26);	
			    		  //推迟12个月的数据(6)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 42, BigDecimalUtil.getDoubleVal(con.getAddSixPlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 43, BigDecimalUtil.getDoubleVal(con.getAddSixPlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 44, BigDecimalUtil.getDoubleVal(con.getAddSixPlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 45, StringUtil.getDoubleValue(con.getAddSixSafeScale()), cellStyleT26);
			    		  //推迟12个月的数据(7)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 46, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 47, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 48, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 49, StringUtil.getDoubleValue(con.getAddSevenSafeScale()), cellStyleT26);
			    		  //推迟12个月的数据(8)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 50, BigDecimalUtil.getDoubleVal(con.getAddEightPlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 51, BigDecimalUtil.getDoubleVal(con.getAddEightPlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 52, BigDecimalUtil.getDoubleVal(con.getAddEightPlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 53, StringUtil.getDoubleValue(con.getAddEightSafeScale()), cellStyleT26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 54, BigDecimalUtil.getDoubleVal(con.getAddNinePlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 55, BigDecimalUtil.getDoubleVal(con.getAddNinePlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 56, BigDecimalUtil.getDoubleVal(con.getAddNinePlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 57, StringUtil.getDoubleValue(con.getAddNineSafeScale()), cellStyleT26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 58, BigDecimalUtil.getDoubleVal(con.getAddTenPlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 59, BigDecimalUtil.getDoubleVal(con.getAddTenPlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 60, BigDecimalUtil.getDoubleVal(con.getAddTenPlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 61, StringUtil.getDoubleValue(con.getAddTenSafeScale()), cellStyleT26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 62, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 63, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 64, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 65, StringUtil.getDoubleValue(con.getAddElevenSafeScale()), cellStyleT26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 66, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanPrdNum()), cellStyleT23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 67, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanDlvNum()), cellStyleT24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 68, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanEndStock()), cellStyleT25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 69, StringUtil.getDoubleValue(con.getAddTwelveSafeScale()), cellStyleT70);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 71, con.getSuppNo(), cellStyle71);
			    		  
			    	  }else{
			    		  ExcelUtil.setValue(dSheet,  rowNum, 0, j+1, cellStyle1);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 1, con.getSuppName(), cellStyle2);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 2, con.getRank(), cellStyle3);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 3, con.getMateDesc(), cellStyle4);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 4, con.getProdSeriesDesc(), cellStyle5);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 5, con.getItemName(), cellStyle6);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 6, con.getBoxNumber(), cellStyle7);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 7, con.getPackNumber(), cellStyle8);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 8, BigDecimalUtil.getDoubleVal(con.getBeginOrder()), cellStyle9);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 9, BigDecimalUtil.getDoubleVal(con.getBeginStock()), cellStyle10);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 10, BigDecimalUtil.getDoubleVal(con.getBeginEnableOrder()), cellStyle11);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 11, BigDecimalUtil.getDoubleVal(con.getPlanPrdNum()), cellStyle12);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 12, BigDecimalUtil.getDoubleVal(con.getActPrdNum()), cellStyle13);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 13, StringUtil.getDoubleValue(con.getPudReachScale()), cellStyle14);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 14, BigDecimalUtil.getDoubleVal(con.getPlanDlvNum()), cellStyle15);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 15, BigDecimalUtil.getDoubleVal(con.getActDlvNum()), cellStyle16);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 16, StringUtil.getDoubleValue(con.getDlvReachScale()), cellStyle17);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 17, BigDecimalUtil.getDoubleVal(con.getSuppDlvNum()), cellStyle18);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 18, BigDecimalUtil.getDoubleVal(con.getSuppActNum()), cellStyle19);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 19, StringUtil.getDoubleValue(con.getSafeScale()), cellStyle20);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 20, BigDecimalUtil.getDoubleVal(con.getEndStock()), cellStyle21);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 21, StringUtil.getDoubleValue( con.getPlanSafeScale()), cellStyle22);
			    		  //推迟12个月的数据(1)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 22, BigDecimalUtil.getDoubleVal(con.getAddOnePlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 23, BigDecimalUtil.getDoubleVal(con.getAddOnePlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 24, BigDecimalUtil.getDoubleVal(con.getAddOnePlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 25, StringUtil.getDoubleValue(con.getAddOneSafeScale()), cellStyle26);
			    		  //推迟12个月的数据(2)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 26, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 27, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 28, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 29, StringUtil.getDoubleValue(con.getAddTwoSafeScale()), cellStyle26);
			    		  //推迟12个月的数据(3)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 30, BigDecimalUtil.getDoubleVal(con.getAddThreePlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 31, BigDecimalUtil.getDoubleVal(con.getAddThreePlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 32, BigDecimalUtil.getDoubleVal(con.getAddThreePlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 33, StringUtil.getDoubleValue(con.getAddThreeSafeScale()), cellStyle26);
			    		  //推迟12个月的数据(4)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 34, BigDecimalUtil.getDoubleVal(con.getAddFourPlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 35, BigDecimalUtil.getDoubleVal(con.getAddFourPlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 36, BigDecimalUtil.getDoubleVal(con.getAddFourPlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 37, StringUtil.getDoubleValue(con.getAddFourSafeScale()), cellStyle26);
			    		  //推迟12个月的数据(5)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 38, BigDecimalUtil.getDoubleVal(con.getAddFivePlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 39, BigDecimalUtil.getDoubleVal(con.getAddFivePlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 40, BigDecimalUtil.getDoubleVal(con.getAddFivePlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 41, StringUtil.getDoubleValue(con.getAddFiveSafeScale()), cellStyle26);	
			    		  //推迟12个月的数据(6)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 42, BigDecimalUtil.getDoubleVal(con.getAddSixPlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 43, BigDecimalUtil.getDoubleVal(con.getAddSixPlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 44, BigDecimalUtil.getDoubleVal(con.getAddSixPlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 45, StringUtil.getDoubleValue(con.getAddSixSafeScale()), cellStyle26);
			    		  //推迟12个月的数据(7)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 46, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 47, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 48, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 49, StringUtil.getDoubleValue(con.getAddSevenSafeScale()), cellStyle26);
			    		  //推迟12个月的数据(8)
			    		  ExcelUtil.setValue(dSheet,  rowNum, 50, BigDecimalUtil.getDoubleVal(con.getAddEightPlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 51, BigDecimalUtil.getDoubleVal(con.getAddEightPlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 52, BigDecimalUtil.getDoubleVal(con.getAddEightPlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 53, StringUtil.getDoubleValue(con.getAddEightSafeScale()), cellStyle26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 54, BigDecimalUtil.getDoubleVal(con.getAddNinePlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 55, BigDecimalUtil.getDoubleVal(con.getAddNinePlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 56, BigDecimalUtil.getDoubleVal(con.getAddNinePlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 57, StringUtil.getDoubleValue(con.getAddNineSafeScale()), cellStyle26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 58, BigDecimalUtil.getDoubleVal(con.getAddTenPlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 59, BigDecimalUtil.getDoubleVal(con.getAddTenPlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 60, BigDecimalUtil.getDoubleVal(con.getAddTenPlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 61, StringUtil.getDoubleValue(con.getAddTenSafeScale()), cellStyle26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 62, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 63, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 64, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 65, StringUtil.getDoubleValue(con.getAddElevenSafeScale()), cellStyle26);
			    		  
			    		  ExcelUtil.setValue(dSheet,  rowNum, 66, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanPrdNum()), cellStyle23);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 67, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanDlvNum()), cellStyle24);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 68, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanEndStock()), cellStyle25);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 69, StringUtil.getDoubleValue(con.getAddTwelveSafeScale()), cellStyle70);
			    		  ExcelUtil.setValue(dSheet,  rowNum, 71, con.getSuppNo(), cellStyle71);

			    	  }
				}
			      for (int j = 0; j < 68; j++) {
			    	  ExcelUtil.setValue(dSheet,  3+size, j, "-  ", cellStyle71);
			      }
			     int mateIndex=0;
			     int mateBegin=4+size+1;
			     for (int j=0;j<mateList.size();j++) {
				      int rowNum=4+size+mateIndex;
				      mateIndex++;
			    	  InnerControl con=mateList.get(j);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 0, mateIndex, cellStyle1);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 1, con.getItemName(), cellStyle2);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 2, con.getRank(), cellStyle3);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 3, con.getMateDesc(), cellStyle4);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 4, con.getProdSeriesDesc(), cellStyle5);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 5, con.getItemName(), cellStyle6);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 6, con.getBoxNumber(), cellStyle7);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 7, con.getPackNumber(), cellStyle8);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 8, BigDecimalUtil.getDoubleVal(con.getBeginOrder()), cellStyle9);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 9, BigDecimalUtil.getDoubleVal(con.getBeginStock()), cellStyle10);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 10, BigDecimalUtil.getDoubleVal(con.getBeginEnableOrder()), cellStyle11);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 11, BigDecimalUtil.getDoubleVal(con.getPlanPrdNum()), cellStyle12);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 12, BigDecimalUtil.getDoubleVal(con.getActPrdNum()), cellStyle13);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 13, StringUtil.getDoubleValue(con.getPudReachScale()), cellStyle14);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 14, BigDecimalUtil.getDoubleVal(con.getPlanDlvNum()), cellStyle15);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 15, BigDecimalUtil.getDoubleVal(con.getActDlvNum()), cellStyle16);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 16, StringUtil.getDoubleValue(con.getDlvReachScale()), cellStyle17);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 17, BigDecimalUtil.getDoubleVal(con.getSuppDlvNum()), cellStyle18);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 18, BigDecimalUtil.getDoubleVal(con.getSuppActNum()), cellStyle19);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 19, StringUtil.getDoubleValue(con.getSafeScale()), cellStyle20);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 20, BigDecimalUtil.getDoubleVal(con.getEndStock()), cellStyle21);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 21, StringUtil.getDoubleValue( con.getPlanSafeScale()), cellStyle22);
		    		  //推迟12个月的数据(1)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 22, BigDecimalUtil.getDoubleVal(con.getAddOnePlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 23, BigDecimalUtil.getDoubleVal(con.getAddOnePlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 24, BigDecimalUtil.getDoubleVal(con.getAddOnePlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 25, StringUtil.getDoubleValue(con.getAddOneSafeScale()), cellStyle26);
		    		  //推迟12个月的数据(2)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 26, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 27, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 28, BigDecimalUtil.getDoubleVal(con.getAddTwoPlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 29, StringUtil.getDoubleValue(con.getAddTwoSafeScale()), cellStyle26);
		    		  //推迟12个月的数据(3)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 30, BigDecimalUtil.getDoubleVal(con.getAddThreePlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 31, BigDecimalUtil.getDoubleVal(con.getAddThreePlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 32, BigDecimalUtil.getDoubleVal(con.getAddThreePlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 33, StringUtil.getDoubleValue(con.getAddThreeSafeScale()), cellStyle26);
		    		  //推迟12个月的数据(4)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 34, BigDecimalUtil.getDoubleVal(con.getAddFourPlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 35, BigDecimalUtil.getDoubleVal(con.getAddFourPlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 36, BigDecimalUtil.getDoubleVal(con.getAddFourPlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 37, StringUtil.getDoubleValue(con.getAddFourSafeScale()), cellStyle26);
		    		  //推迟12个月的数据(5)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 38, BigDecimalUtil.getDoubleVal(con.getAddFivePlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 39, BigDecimalUtil.getDoubleVal(con.getAddFivePlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 40, BigDecimalUtil.getDoubleVal(con.getAddFivePlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 41, StringUtil.getDoubleValue(con.getAddFiveSafeScale()), cellStyle26);	
		    		  //推迟12个月的数据(6)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 42, BigDecimalUtil.getDoubleVal(con.getAddSixPlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 43, BigDecimalUtil.getDoubleVal(con.getAddSixPlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 44, BigDecimalUtil.getDoubleVal(con.getAddSixPlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 45, StringUtil.getDoubleValue(con.getAddSixSafeScale()), cellStyle26);
		    		  //推迟12个月的数据(7)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 46, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 47, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 48, BigDecimalUtil.getDoubleVal(con.getAddSevenPlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 49, StringUtil.getDoubleValue(con.getAddSevenSafeScale()), cellStyle26);
		    		  //推迟12个月的数据(8)
		    		  ExcelUtil.setValue(dSheet,  rowNum, 50, BigDecimalUtil.getDoubleVal(con.getAddEightPlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 51, BigDecimalUtil.getDoubleVal(con.getAddEightPlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 52, BigDecimalUtil.getDoubleVal(con.getAddEightPlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 53, StringUtil.getDoubleValue(con.getAddEightSafeScale()), cellStyle26);
		    		  
		    		  ExcelUtil.setValue(dSheet,  rowNum, 54, BigDecimalUtil.getDoubleVal(con.getAddNinePlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 55, BigDecimalUtil.getDoubleVal(con.getAddNinePlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 56, BigDecimalUtil.getDoubleVal(con.getAddNinePlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 57, StringUtil.getDoubleValue(con.getAddNineSafeScale()), cellStyle26);
		    		  
		    		  ExcelUtil.setValue(dSheet,  rowNum, 58, BigDecimalUtil.getDoubleVal(con.getAddTenPlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 59, BigDecimalUtil.getDoubleVal(con.getAddTenPlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 60, BigDecimalUtil.getDoubleVal(con.getAddTenPlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 61, StringUtil.getDoubleValue(con.getAddTenSafeScale()), cellStyle26);
		    		  
		    		  ExcelUtil.setValue(dSheet,  rowNum, 62, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 63, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 64, BigDecimalUtil.getDoubleVal(con.getAddElevenPlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 65, StringUtil.getDoubleValue(con.getAddElevenSafeScale()), cellStyle26);
		    		  
		    		  ExcelUtil.setValue(dSheet,  rowNum, 66, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanPrdNum()), cellStyle23);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 67, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanDlvNum()), cellStyle24);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 68, BigDecimalUtil.getDoubleVal(con.getAddTwelvePlanEndStock()), cellStyle25);
		    		  ExcelUtil.setValue(dSheet,  rowNum, 69, StringUtil.getDoubleValue(con.getAddTwelveSafeScale()), cellStyle26);
		    		  String mateDesc = con.getMateDesc();
		    		  if(mateDesc!=null && mateDesc.indexOf("合计")>0){
		    			  setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
		    			  //是否需要计算万片合计
		    			  if(!StringUtil.isNullOrEmpty(wFlag) && "w".equals(wFlag)){
		    				  rowNum++;
		    				  mateIndex++;
				    		  ExcelUtil.setValue(dSheet,  rowNum, 0, mateIndex, cellStyle1);
				    		  ExcelUtil.setValue(dSheet,  rowNum, 1, con.getItemName(), cellStyle2);
				    		  ExcelUtil.setValue(dSheet,  rowNum, 3, "万片小计", cellStyle4);
				    		  ExcelUtil.setValue(dSheet,  rowNum, 4, con.getProdSeriesDesc(), cellStyle5);
				    		  ExcelUtil.setValue(dSheet,  rowNum, 5, con.getItemName(), cellStyle6);
				    		  //推迟12个月的数据(1)
				    		  ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
						    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
						    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
					    		 for (int m = 8; m <=69; m++) {
									if(m>=21 && m%4==1){
								    	 ExcelUtil.setFormula(dSheet,  rowNum, m, StringUtil.getExcelStrByIndex(m-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(m+2)+(rowNum+1), cellStyle26);
									}else if(m!=13 && m!=16 && m!=19){
										 String str = StringUtil.getExcelStrByIndex(m);
										 ExcelUtil.setFormula(dSheet,  rowNum, m, "SUMPRODUCT(($G$"+mateBegin+":$G$"+(rowNum-1)+")*($H$"+mateBegin+":$H$"+(rowNum-1)+")*("+str+"$"+mateBegin+":"+str+"$"+(rowNum-1)+"))/10000", cellStyle23);
									}
								}
							  setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.TAN.index);
		    			  }
		    			  mateBegin=rowNum+2;
		    		  }
		    	 }
	    		//箱合计
			    int rowNum=4+size+mateIndex;
				mateIndex++;
	    		int start=4 ;
	    		int end=suppList.size()+3;
	    		 
		    	ExcelUtil.setValue(dSheet,  rowNum, 0, mateIndex, cellStyle1);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
	    		ExcelUtil.setValue(dSheet,  rowNum, 71, "", cellStyle72);
	    		for (int j = 8; j <=69; j++) {
					if(j>=21 && j%4==1){
				    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
					}else if(j!=13 && j!=16 && j!=19){
						 String str = StringUtil.getExcelStrByIndex(j);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUM("+str+start+":"+str+end+")", cellStyle23);
					}
				}
		    	ExcelUtil.setValue(dSheet,  rowNum, 3, "物料箱合计", cellStyle2);
		    	ExcelUtil.setValue(dSheet,  rowNum, 5, "物料箱合计", cellStyle2);
    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
			     
   			  //是否需要计算万片合计
			   if(!StringUtil.isNullOrEmpty(wFlag) && "w".equals(wFlag)){
   				  rowNum++;
   				  mateIndex++;
	    		  ExcelUtil.setValue(dSheet,  rowNum, 0, mateIndex, cellStyle1);
	    		  //推迟12个月的数据(1)
	    		  ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
		    		 for (int m = 8; m <=69; m++) {
						if(m>=21 && m%4==1){
					    	 ExcelUtil.setFormula(dSheet,  rowNum, m, StringUtil.getExcelStrByIndex(m-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(m+2)+(rowNum+1), cellStyle26);
						}else if(m!=13 && m!=16 && m!=19){
							 String str = StringUtil.getExcelStrByIndex(m);
							 ExcelUtil.setFormula(dSheet,  rowNum, m, "SUMPRODUCT(($G$"+start+":$G$"+end+")*($H$"+start+":$H$"+end+")*("+str+"$"+start+":"+str+"$"+end+"))/10000", cellStyle23);
						}
					}
		    	  ExcelUtil.setValue(dSheet,  rowNum, 3, "物料万片合计", cellStyle2);
		    	  ExcelUtil.setValue(dSheet,  rowNum, 5, "物料万片合计", cellStyle2);
				  setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.TAN.index);
   			  	}	
			    for (int j = 0; j < 68; j++) {
			    	  ExcelUtil.setValue(dSheet,  mateIndex+4+suppList.size(), j, "-  ", cellStyle71);
			    }	
			     //开始供应商的合计计算
	    		 rowNum=mateIndex+4+size;
	    		 int index=0;
	    		 Set<String> keySet = suppSubMap.keySet();
	    			for (String suppNo : keySet) {
	    				String suppName="";
    				 List<SuppVo> list = suppSubMap.get(suppNo);
					 for (SuppVo suppVo : list) {
						 suppName = suppVo.getSuppName();
						 String itemName = suppVo.getItemName();
				    	 //供应商合计
				    	 rowNum++;
				    	 index++;
				    	 ExcelUtil.setValue(dSheet,  rowNum, 0, index, cellStyle1);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
			    		 ExcelUtil.setValue(dSheet,  rowNum, 71, suppNo, cellStyle72);
			    		 for (int j = 8; j <=69; j++) {
							if(j>=21 && j%4==1){
						    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
							}else if(j!=13 && j!=16 && j!=19){
								 String str = StringUtil.getExcelStrByIndex(j);
						    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMIFS("+str+"$"+start+":"+str+"$"+end+",$F$"+start+":$F$"+end+",$F"+(rowNum+1)+"&\"\",$BT$"+start+":$BT$"+end+",$BT"+(rowNum+1)+")", cellStyle23);
							}
						}
//				    	ExcelUtil.setFormula(dSheet,  rowNum, 1, "VLOOKUP(BT"+(rowNum+1)+",IF({1,0},$BT$4:OFFSET($BT$4,MATCH(1,$A$5:$A$65536,0)-2,),$B$4:OFFSET($B$4,MATCH(1,$A$5:$A$65536,0)-2,)),2,0)", cellStyle2);
				    	ExcelUtil.setValue(dSheet,  rowNum, 3, suppName, cellStyle2);
				    	ExcelUtil.setValue(dSheet,  rowNum, 5, itemName, cellStyle2);
		    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
		    			 //供应商万片合计
		    			 if(!StringUtil.isNullOrEmpty(wFlag) && "w".equals(wFlag)){
					    	 rowNum++;
					    	 index++;
					    	 ExcelUtil.setValue(dSheet,  rowNum, 0, index, cellStyle1);
					    	 ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
					    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
					    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
				    		 ExcelUtil.setValue(dSheet,  rowNum, 71, suppNo, cellStyle72);
				    		 for (int j = 8; j <=69; j++) {
								if(j>=21 && j%4==1){
							    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
								}else if(j!=13 && j!=16 && j!=19){
									 String str = StringUtil.getExcelStrByIndex(j);
	//						    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMPRODUCT(($G$"+start+":$G$"+end+")*($H$"+start+":$H$"+end+")*("+str+"$"+start+":"+str+"$"+end+"))/10000", cellStyle23);
							    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMPRODUCT(($F$"+start+":$F$"+end+"=$F"+(rowNum+1)+")*($BT$"+start+":$BT$"+end+"=$BT"+(rowNum+1)+")*($H$"+start+":$H$"+end+")*($G$"+start+":$G$"+end+")*("+str+"$"+start+":"+str+"$"+end+"))/10000", cellStyle23);
								}
							}
						    ExcelUtil.setValue(dSheet,  rowNum, 3, suppName, cellStyle2);
					    	ExcelUtil.setValue(dSheet,  rowNum, 5, itemName, cellStyle2);
	//				    	ExcelUtil.setFormula(dSheet,  rowNum, 1, "VLOOKUP(BT"+(rowNum+1)+",IF({1,0},$BT$4:OFFSET($BT$4,MATCH(1,$A$5:$A$65536,0)-2,),$B$4:OFFSET($B$4,MATCH(1,$A$5:$A$65536,0)-2,)),2,0)", cellStyle2);
			    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.TAN.index);
		    			 }
					 }
					 
					 //---------------供应商箱小计与万片小计
			    	 rowNum++;
			    	 index++;
			    	 ExcelUtil.setValue(dSheet,  rowNum, 0, index, cellStyle1);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
		    		 ExcelUtil.setValue(dSheet,  rowNum, 71, suppNo, cellStyle72);
		    		 for (int j = 8; j <=69; j++) {
						if(j>=21 && j%4==1){
					    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
						}else if(j!=13 && j!=16 && j!=19){
							 String str = StringUtil.getExcelStrByIndex(j);
					    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMIFS("+str+"$"+start+":"+str+"$"+end+",$BT$"+start+":$BT$"+end+",$BT"+(rowNum+1)+")", cellStyle23);
						}
					}
//			    	ExcelUtil.setFormula(dSheet,  rowNum, 1, "VLOOKUP(BT"+(rowNum+1)+",IF({1,0},$BT$4:OFFSET($BT$4,MATCH(1,$A$5:$A$65536,0)-2,),$B$4:OFFSET($B$4,MATCH(1,$A$5:$A$65536,0)-2,)),2,0)", cellStyle2);
			    	ExcelUtil.setValue(dSheet,  rowNum, 3, suppName, cellStyle2);
			    	ExcelUtil.setValue(dSheet,  rowNum, 5, "箱小计", cellStyle2);
	    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
	    			 //供应商万片合计
	    			 if(!StringUtil.isNullOrEmpty(wFlag) && "w".equals(wFlag)){
				    	 rowNum++;
				    	 index++;
				    	 ExcelUtil.setValue(dSheet,  rowNum, 0, index, cellStyle1);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
			    		 ExcelUtil.setValue(dSheet,  rowNum, 71, suppNo, cellStyle72);
			    		 for (int j = 8; j <=69; j++) {
							if(j>=21 && j%4==1){
						    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
							}else if(j!=13 && j!=16 && j!=19){
								 String str = StringUtil.getExcelStrByIndex(j);
//						    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMPRODUCT(($G$"+start+":$G$"+end+")*($H$"+start+":$H$"+end+")*("+str+"$"+start+":"+str+"$"+end+"))/10000", cellStyle23);
						    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMPRODUCT(($BT$"+start+":$BT$"+end+"=$BT"+(rowNum+1)+")*($H$"+start+":$H$"+end+")*($G$"+start+":$G$"+end+")*("+str+"$"+start+":"+str+"$"+end+"))/10000", cellStyle23);
							}
						}
					    ExcelUtil.setValue(dSheet,  rowNum, 3, suppName, cellStyle2);
				    	ExcelUtil.setValue(dSheet,  rowNum, 5, "万片小计", cellStyle2);
		    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.TAN.index);
	    			 }
					 
				}
	    		//供应商箱合计
		    	rowNum++;
		    	index++;
		    	ExcelUtil.setValue(dSheet,  rowNum, 0, index, cellStyle1);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
	    		ExcelUtil.setValue(dSheet,  rowNum, 71, "", cellStyle72);
	    		for (int j = 8; j <=69; j++) {
					if(j>=21 && j%4==1){
				    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
					}else if(j!=13 && j!=16 && j!=19){
						 String str = StringUtil.getExcelStrByIndex(j);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUM("+str+start+":"+str+end+")", cellStyle23);
					}
				}
		    	ExcelUtil.setValue(dSheet,  rowNum, 3, "供应商箱合计", cellStyle2);
		    	ExcelUtil.setValue(dSheet,  rowNum, 5, "供应商箱合计", cellStyle2);
    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
   			 //供应商万片合计
    			if(!StringUtil.isNullOrEmpty(wFlag) && "w".equals(wFlag)){
			    	 rowNum++;
			    	 index++;
			    	 ExcelUtil.setValue(dSheet,  rowNum, 0, index, cellStyle1);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
			    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
		    		 for (int j = 8; j <=69; j++) {
						if(j>=21 && j%4==1){
					    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
						}else if(j!=13 && j!=16 && j!=19){
							 String str = StringUtil.getExcelStrByIndex(j);
					    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMPRODUCT(($BT$"+start+":$BT$"+end+"=$BT"+(rowNum+1)+")*($H$"+start+":$H$"+end+")*($G$"+start+":$G$"+end+")*("+str+"$"+start+":"+str+"$"+end+"))/10000", cellStyle23);
						}
					}
				    ExcelUtil.setValue(dSheet,  rowNum, 3, "供应商万片合计", cellStyle2);
			    	ExcelUtil.setValue(dSheet,  rowNum, 5, "供应商万片合计", cellStyle2);
	    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.TAN.index);
   			 	}
		    	rowNum++;
			    for (int j = 0; j < 68; j++) {
			    	  ExcelUtil.setValue(dSheet,  rowNum, j, "-  ", cellStyle71);
			    }	
	    		//箱合计
		    	rowNum++;
		    	index++;
		    	ExcelUtil.setValue(dSheet,  rowNum, 0, 1, cellStyle1);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
		    	ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
	    		ExcelUtil.setValue(dSheet,  rowNum, 71, "", cellStyle72);
	    		for (int j = 8; j <=69; j++) {
					if(j>=21 && j%4==1){
				    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
					}else if(j!=13 && j!=16 && j!=19){
						 String str = StringUtil.getExcelStrByIndex(j);
				    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUM("+str+start+":"+str+end+")", cellStyle23);
					}
				}
		    	ExcelUtil.setValue(dSheet,  rowNum, 3, "箱合计", cellStyle2);
		    	ExcelUtil.setValue(dSheet,  rowNum, 5, "箱合计", cellStyle2);
    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.LIGHT_CORNFLOWER_BLUE.index);    			
      			 //万片合计
      			 if(!StringUtil.isNullOrEmpty(wFlag) && "w".equals(wFlag)){
   			    	 rowNum++;
   			    	 index++;
   			    	 ExcelUtil.setValue(dSheet,  rowNum, 0, 2, cellStyle1);
   			    	 ExcelUtil.setFormula(dSheet,  rowNum, 13, "M"+(rowNum+1)+"/L"+(rowNum+1), cellStyle14);
   			    	 ExcelUtil.setFormula(dSheet,  rowNum, 16, "P"+(rowNum+1)+"/O"+(rowNum+1), cellStyle14);
   			    	 ExcelUtil.setFormula(dSheet,  rowNum, 19, "S"+(rowNum+1)+"/X"+(rowNum+1), cellStyle14);
   		    		 for (int j = 8; j <=69; j++) {
   						if(j>=21 && j%4==1){
   					    	 ExcelUtil.setFormula(dSheet,  rowNum, j, StringUtil.getExcelStrByIndex(j-1)+(rowNum+1)+"/"+StringUtil.getExcelStrByIndex(j+2)+(rowNum+1), cellStyle26);
   						}else if(j!=13 && j!=16 && j!=19){
   							 String str = StringUtil.getExcelStrByIndex(j);
   					    	 ExcelUtil.setFormula(dSheet,  rowNum, j, "SUMPRODUCT(($BT$"+start+":$BT$"+end+"=$BT"+(rowNum+1)+")*($H$"+start+":$H$"+end+")*($G$"+start+":$G$"+end+")*("+str+"$"+start+":"+str+"$"+end+"))/10000", cellStyle23);
   						}
   					}
   				    ExcelUtil.setValue(dSheet,  rowNum, 3, "万片合计", cellStyle2);
   			    	ExcelUtil.setValue(dSheet,  rowNum, 5, "万片合计", cellStyle2);
   	    			setFontAndBgColor(0, 69, rowNum, wb, font, dSheet, IndexedColors.TAN.index);
      			 }
//		        dSheet.autoSizeColumn((short)1); //调整第二列宽度
//		        dSheet.autoSizeColumn((short)3); //调整第四列宽度
//		        dSheet.autoSizeColumn((short)8); //调整第四列宽度
//		        dSheet.autoSizeColumn((short)9); //调整第四列宽度
//		        dSheet.autoSizeColumn((short)16); //调整第四列宽度
//		        dSheet.autoSizeColumn((short)17); //调整第四列宽度
			}
			wb.setForceFormulaRecalculation(true);
			try {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				realName=URLEncoder.encode(realName, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename="+realName);
				os = response.getOutputStream();
				ExcelUtil.exportExcel(wb, os);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			IoUtil.closeIo(os,fis,wb);
		}
		return null;
	}
	
	@Log("导出旺季备货表")
	@RequestMapping("/exportBusyStock")
	public String exportBusyStock(HttpServletResponse response,String reconCode,Date endDate,String invenCode){
		//获取查询的开始日期和结束日期
		Map<String, Object> map=new HashMap<>();
		Calendar cal=Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = cal.getTime();
		int maxNum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, maxNum);
		endDate = cal.getTime();
		map.put("endDate", endDate);
		map.put("startDate", startDate);
		map.put("planMonth", startDate);
		map.put("invenCode", invenCode);
		String planMonthStr = DateUtils.format(endDate, "yyyyMM");
		String realName="旺季备货表"+planMonthStr+".xls";
		//获取内部管控表数据
		//获取所有的管理人员
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		
		String sfCode = user.getSfCode();
		if(sfCode!=null && sfCode.indexOf("PURCHAROR")!=-1){
			Map<String , Object> params=new HashMap<>();
			params.put("ownId", userId);
			params.put("isContainOwn", true);
			List<UserDO> users = orgService.manageSubordinateUsers(params);
			List<Long> userIds=new ArrayList<Long>();
			for (UserDO userDo : users) {
				userIds.add(userDo.getId());
			}
			map.put("userIds", userIds);
		}
		List<BusyStock> busyStocks = actReachService.getSuppBusyStock(map);
		map.put("endDate", endDate);
		map.put("startDate", startDate);
		map.put("planMonth", startDate);
		List<BusyStock> mateBusyStocks = actReachService.getMateBusyStock(map);
		//创建HSSFWorkbook对象(excel的文档对象)
	     HSSFWorkbook wb=null;
	     OutputStream os=null;
	     FileInputStream fis=null;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String realPath = filePath+"templates\\excelTemp\\旺季备货表样式.xls";
			fis=new FileInputStream(realPath);
			wb = new HSSFWorkbook(fis);
			//建立新的sheet对象（excel的表单）
			HSSFSheet sheet=wb.getSheetAt(0);
			
			
//			获取样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 3, 0);
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 3, 1);
			CellStyle cellStyle3 = ExcelUtil.getCellStyle(sheet, 3, 2);
			CellStyle cellStyle4 = ExcelUtil.getCellStyle(sheet, 3, 3);
			CellStyle cellStyle5 = ExcelUtil.getCellStyle(sheet, 3, 4);
			CellStyle cellStyle6 = ExcelUtil.getCellStyle(sheet, 3, 5);
			CellStyle cellStyle7 = ExcelUtil.getCellStyle(sheet, 3, 6);
			CellStyle cellStyle8 = ExcelUtil.getCellStyle(sheet, 3, 7);
			CellStyle cellStyle9 = ExcelUtil.getCellStyle(sheet, 3, 8);
			CellStyle cellStyle10 = ExcelUtil.getCellStyle(sheet, 3, 9);
			CellStyle cellStyle11 = ExcelUtil.getCellStyle(sheet, 3, 10);
			CellStyle cellStyle12 = ExcelUtil.getCellStyle(sheet, 3, 11);
			CellStyle cellStyle13 = ExcelUtil.getCellStyle(sheet, 3, 12);
			CellStyle cellStyle14 = ExcelUtil.getCellStyle(sheet, 3, 13);
			CellStyle cellStyle15 = ExcelUtil.getCellStyle(sheet, 3, 14);
			CellStyle cellStyle16 = ExcelUtil.getCellStyle(sheet, 3, 15);
			CellStyle cellStyle17 = ExcelUtil.getCellStyle(sheet, 3, 16);


			CellStyle cellStyle61 = ExcelUtil.getCellStyle(sheet, 3, 60);
			
			CellStyle cellStyle63 = ExcelUtil.getCellStyle(sheet, 3, 62);
			CellStyle cellStyle64 = ExcelUtil.getCellStyle(sheet, 3, 63);
			CellStyle cellStyle65 = ExcelUtil.getCellStyle(sheet, 3, 64);
			CellStyle cellStyle66 = ExcelUtil.getCellStyle(sheet, 3, 65);


			CellStyle cellStyleT1 = ExcelUtil.getCellStyle(sheet, 4, 0);
			CellStyle cellStyleT2 = ExcelUtil.getCellStyle(sheet, 4, 1);
			CellStyle cellStyleT3 = ExcelUtil.getCellStyle(sheet, 4, 2);
			CellStyle cellStyleT4 = ExcelUtil.getCellStyle(sheet, 4, 3);
			CellStyle cellStyleT5 = ExcelUtil.getCellStyle(sheet, 4, 4);
			CellStyle cellStyleT6 = ExcelUtil.getCellStyle(sheet, 4, 5);
			CellStyle cellStyleT7 = ExcelUtil.getCellStyle(sheet, 4, 6);
			CellStyle cellStyleT8 = ExcelUtil.getCellStyle(sheet, 4, 7);
			CellStyle cellStyleT9 = ExcelUtil.getCellStyle(sheet, 4, 8);
			CellStyle cellStyleT10 = ExcelUtil.getCellStyle(sheet,4, 9);
			CellStyle cellStyleT11 = ExcelUtil.getCellStyle(sheet, 4, 10);
			CellStyle cellStyleT12 = ExcelUtil.getCellStyle(sheet, 4, 11);
			CellStyle cellStyleT13 = ExcelUtil.getCellStyle(sheet, 4, 12);
			CellStyle cellStyleT14 = ExcelUtil.getCellStyle(sheet, 4, 13);
			CellStyle cellStyleT15 = ExcelUtil.getCellStyle(sheet, 4, 14);
			CellStyle cellStyleT16 = ExcelUtil.getCellStyle(sheet, 4, 15);
			CellStyle cellStyleT17 = ExcelUtil.getCellStyle(sheet, 4, 16);

			CellStyle cellStyleT61 = ExcelUtil.getCellStyle(sheet, 4, 60);
			
			CellStyle cellStyleT63 = ExcelUtil.getCellStyle(sheet, 4, 62);
			CellStyle cellStyleT64 = ExcelUtil.getCellStyle(sheet, 4, 63);
			CellStyle cellStyleT65 = ExcelUtil.getCellStyle(sheet, 4, 64);
			

			
			sheet.getRow(0).getCell(0).setCellValue("旺季备货表"+planMonthStr);;
			HSSFRow row2=sheet.getRow(1);  
			 //创建单元格并设置单元格内容
		      //当月数据
		      row2.getCell(6).setCellValue(planMonthStr);
		      //下1个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(13).setCellValue(planMonthStr);

		      //下2个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(17).setCellValue(planMonthStr);
		      //下3个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(21).setCellValue(planMonthStr);

		      //下4个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(25).setCellValue(planMonthStr);

		      //下5个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(29).setCellValue(planMonthStr);

		      //下6个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(33).setCellValue(planMonthStr);

		      //下7个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(37).setCellValue(planMonthStr);
		      //下8个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(41).setCellValue(planMonthStr);

		      //下9个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(45).setCellValue(planMonthStr);

		      //下10个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(49).setCellValue(planMonthStr);

		      //下11个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(53).setCellValue(planMonthStr);

		      //下12个月生产交货预测
		      cal.add(Calendar.MONTH, 1);
		      planMonthStr=DateUtils.format(cal.getTime(), "yyyyMM");
		      row2.getCell(57).setCellValue(planMonthStr);
		      int suppBusyStockSize = busyStocks.size();
		      //在sheet里创建第三行
		      for (int i=0;i<suppBusyStockSize;i++) {
		    	  BusyStock busyStock=busyStocks.get(i);
		    	  int rowNum=3+i;
		    	  if(i==suppBusyStockSize-1){
			    	  //公共和当月数据
			    	  ExcelUtil.setValue(sheet, rowNum, 0, i+1, cellStyleT1);
			    	  ExcelUtil.setValue(sheet, rowNum, 1, busyStock.getSuppName(), cellStyleT2);
			    	  ExcelUtil.setValue(sheet, rowNum, 2, busyStock.getRank(), cellStyleT3);
			    	  ExcelUtil.setValue(sheet, rowNum, 3, busyStock.getMateDesc(), cellStyleT4);
			    	  ExcelUtil.setValue(sheet, rowNum, 4, busyStock.getBoxNumber(), cellStyleT5);
			    	  ExcelUtil.setValue(sheet, rowNum, 5, busyStock.getPackNumber(), cellStyleT6);
			    	  ExcelUtil.setValue(sheet, rowNum, 6, BigDecimalUtil.getDoubleVal(busyStock.getBeginOrder()), cellStyleT7);
			    	  ExcelUtil.setValue(sheet, rowNum, 7, BigDecimalUtil.getDoubleVal(busyStock.getBeginStock()), cellStyleT8);
			    	  ExcelUtil.setValue(sheet, rowNum, 8, BigDecimalUtil.getDoubleVal(busyStock.getBeginEnableOrder()), cellStyleT9);
			    	  ExcelUtil.setValue(sheet, rowNum, 9, BigDecimalUtil.getDoubleVal(busyStock.getPlanPrdNum()), cellStyleT10);
			    	  ExcelUtil.setValue(sheet, rowNum, 10, BigDecimalUtil.getDoubleVal(busyStock.getPlanDlvNum()), cellStyleT11);
			    	  ExcelUtil.setValue(sheet, rowNum, 11, BigDecimalUtil.getDoubleVal(busyStock.getEndStock()), cellStyleT12);
			    	  ExcelUtil.setValue(sheet, rowNum, 12, busyStock.getSafeScale(), cellStyleT13);

				     //推迟1个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 13, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 14, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 15, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 16, busyStock.getAddOneSafeScale(), cellStyleT17);

				      //推迟2个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 17, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 18, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 19, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 20, busyStock.getAddTwoSafeScale(), cellStyleT17);
				      //推迟3个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 21, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 22, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 23, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 24, busyStock.getAddThreeSafeScale(), cellStyleT17);
				      //推迟4个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 25, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 26, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 27, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 28, busyStock.getAddFourSafeScale(), cellStyleT17);	  
				      //推迟5个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 29, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 30, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 31, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 32, busyStock.getAddFiveSafeScale(), cellStyleT17); 
				      //推迟6个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 33, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 34, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 35, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 36, busyStock.getAddSixSafeScale(), cellStyleT17);				  
				      //推迟7个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 37, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 38, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 39, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 40, busyStock.getAddSevenSafeScale(), cellStyleT17);		  
				      //推迟8个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 41, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 42, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 43, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 44, busyStock.getAddEightSafeScale(), cellStyleT17);  
				      //推迟9个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 45, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 46, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 47, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 48, busyStock.getAddNineSafeScale(), cellStyleT17);
				      //推迟10个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 49, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 50, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 51, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 52, busyStock.getAddTenSafeScale(), cellStyleT17); 
				      //推迟11个月预测------------
			    	  ExcelUtil.setValue(sheet, rowNum, 53, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 54, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 55, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 56, busyStock.getAddElevenSafeScale(), cellStyleT17); 
				      //推迟12个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 57, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanPrdNum()), cellStyleT14);
					  ExcelUtil.setValue(sheet, rowNum, 58, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanDlvNum()), cellStyleT15);
					  ExcelUtil.setValue(sheet, rowNum, 59, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanEndStock()), cellStyleT16);
					  ExcelUtil.setValue(sheet, rowNum, 60, busyStock.getAddTwelveSafeScale(), cellStyleT61);
				      //生产交货计划求和 期末库存最大值
					  ExcelUtil.setValue(sheet, rowNum, 62,BigDecimalUtil.getDoubleVal(busyStock.getSumPlanPrdNum()), cellStyleT63);
					  ExcelUtil.setValue(sheet, rowNum, 63,BigDecimalUtil.getDoubleVal(busyStock.getSumPlanDlvNum()), cellStyleT64);
					  ExcelUtil.setValue(sheet, rowNum, 64,BigDecimalUtil.getDoubleVal(busyStock.getMaxPlanEndStock()), cellStyleT65);
		    	  }else{
			    	  //公共和当月数据
			    	  ExcelUtil.setValue(sheet, rowNum, 0, i+1, cellStyle1);
			    	  ExcelUtil.setValue(sheet, rowNum, 1, busyStock.getSuppName(), cellStyle2);
			    	  ExcelUtil.setValue(sheet, rowNum, 2, busyStock.getRank(), cellStyle3);
			    	  ExcelUtil.setValue(sheet, rowNum, 3, busyStock.getMateDesc(), cellStyle4);
			    	  ExcelUtil.setValue(sheet, rowNum, 4, busyStock.getBoxNumber(), cellStyle5);
			    	  ExcelUtil.setValue(sheet, rowNum, 5, busyStock.getPackNumber(), cellStyle6);
			    	  ExcelUtil.setValue(sheet, rowNum, 6, BigDecimalUtil.getDoubleVal(busyStock.getBeginOrder()), cellStyle7);
			    	  ExcelUtil.setValue(sheet, rowNum, 7, BigDecimalUtil.getDoubleVal(busyStock.getBeginStock()), cellStyle8);
			    	  ExcelUtil.setValue(sheet, rowNum, 8, BigDecimalUtil.getDoubleVal(busyStock.getBeginEnableOrder()), cellStyle9);
			    	  ExcelUtil.setValue(sheet, rowNum, 9, BigDecimalUtil.getDoubleVal(busyStock.getPlanPrdNum()), cellStyle10);
			    	  ExcelUtil.setValue(sheet, rowNum, 10, BigDecimalUtil.getDoubleVal(busyStock.getPlanDlvNum()), cellStyle11);
			    	  ExcelUtil.setValue(sheet, rowNum, 11, BigDecimalUtil.getDoubleVal(busyStock.getEndStock()), cellStyle12);
			    	  ExcelUtil.setValue(sheet, rowNum, 12, busyStock.getSafeScale(), cellStyle13);

				     //推迟1个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 13, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 14, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 15, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 16, busyStock.getAddOneSafeScale(), cellStyle17);

				      //推迟2个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 17, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 18, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 19, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 20, busyStock.getAddTwoSafeScale(), cellStyle17);
				      //推迟3个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 21, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 22, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 23, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 24, busyStock.getAddThreeSafeScale(), cellStyle17);
				      //推迟4个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 25, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 26, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 27, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 28, busyStock.getAddFourSafeScale(), cellStyle17);	  
				      //推迟5个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 29, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 30, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 31, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 32, busyStock.getAddFiveSafeScale(), cellStyle17); 
				      //推迟6个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 33, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 34, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 35, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 36, busyStock.getAddSixSafeScale(), cellStyle17);				  
				      //推迟7个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 37, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 38, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 39, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 40, busyStock.getAddSevenSafeScale(), cellStyle17);		  
				      //推迟8个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 41, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 42, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 43, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 44, busyStock.getAddEightSafeScale(), cellStyle17);  
				      //推迟9个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 45, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 46, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 47, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 48, busyStock.getAddNineSafeScale(), cellStyle17);
				      //推迟10个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 49, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 50, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 51, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 52, busyStock.getAddTenSafeScale(), cellStyle17); 
				      //推迟11个月预测------------
			    	  ExcelUtil.setValue(sheet, rowNum, 53, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 54, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 55, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 56, busyStock.getAddElevenSafeScale(), cellStyle17); 
				      //推迟12个月预测
			    	  ExcelUtil.setValue(sheet, rowNum, 57, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanPrdNum()), cellStyle14);
					  ExcelUtil.setValue(sheet, rowNum, 58, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanDlvNum()), cellStyle15);
					  ExcelUtil.setValue(sheet, rowNum, 59, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanEndStock()), cellStyle16);
					  ExcelUtil.setValue(sheet, rowNum, 60, busyStock.getAddTwelveSafeScale(), cellStyle61);
				      //生产交货计划求和 期末库存最大值
					  ExcelUtil.setValue(sheet, rowNum, 62,BigDecimalUtil.getDoubleVal(busyStock.getSumPlanPrdNum()), cellStyle63);
					  ExcelUtil.setValue(sheet, rowNum, 63,BigDecimalUtil.getDoubleVal(busyStock.getSumPlanDlvNum()), cellStyle64);
					  ExcelUtil.setValue(sheet, rowNum, 64,BigDecimalUtil.getDoubleVal(busyStock.getMaxPlanEndStock()), cellStyle65);
		    	  }
			}
		      for (int i = 0; i < 65; i++) {
		    	  ExcelUtil.setValue(sheet,  3+suppBusyStockSize, i, "-  ", cellStyle66);
		      }
		      //在sheet里创建第三行
		      for (int i=0;i<mateBusyStocks.size();i++) {
		    	  BusyStock busyStock=mateBusyStocks.get(i);
		    	  //公共和当月数
		    	  int rowNum=4+i+suppBusyStockSize;
		    	  ExcelUtil.setValue(sheet, rowNum, 0, i+1, cellStyle1);
		    	  ExcelUtil.setValue(sheet, rowNum, 1, busyStock.getItemName(), cellStyle2);
		    	  ExcelUtil.setValue(sheet, rowNum, 2, busyStock.getRank(), cellStyle3);
		    	  ExcelUtil.setValue(sheet, rowNum, 3, busyStock.getMateDesc(), cellStyle4);
		    	  ExcelUtil.setValue(sheet, rowNum, 4, busyStock.getBoxNumber(), cellStyle5);
		    	  ExcelUtil.setValue(sheet, rowNum, 5, busyStock.getPackNumber(), cellStyle6);
		    	  ExcelUtil.setValue(sheet, rowNum, 6, BigDecimalUtil.getDoubleVal(busyStock.getBeginOrder()), cellStyle7);
		    	  ExcelUtil.setValue(sheet, rowNum, 7, BigDecimalUtil.getDoubleVal(busyStock.getBeginStock()), cellStyle8);
		    	  ExcelUtil.setValue(sheet, rowNum, 8, BigDecimalUtil.getDoubleVal(busyStock.getBeginEnableOrder()), cellStyle9);
		    	  ExcelUtil.setValue(sheet, rowNum, 9, BigDecimalUtil.getDoubleVal(busyStock.getPlanPrdNum()), cellStyle10);
		    	  ExcelUtil.setValue(sheet, rowNum, 10, BigDecimalUtil.getDoubleVal(busyStock.getPlanDlvNum()), cellStyle11);
		    	  ExcelUtil.setValue(sheet, rowNum, 11, BigDecimalUtil.getDoubleVal(busyStock.getEndStock()), cellStyle12);
		    	  ExcelUtil.setValue(sheet, rowNum, 12, busyStock.getSafeScale(), cellStyle13);

			     //推迟1个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 13, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 14, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 15, BigDecimalUtil.getDoubleVal(busyStock.getAddOnePlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 16, busyStock.getAddOneSafeScale(), cellStyle17);

			      //推迟2个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 17, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 18, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 19, BigDecimalUtil.getDoubleVal(busyStock.getAddTwoPlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 20, busyStock.getAddTwoSafeScale(), cellStyle17);
			      //推迟3个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 21, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 22, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 23, BigDecimalUtil.getDoubleVal(busyStock.getAddThreePlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 24, busyStock.getAddThreeSafeScale(), cellStyle17);
			      //推迟4个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 25, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 26, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 27, BigDecimalUtil.getDoubleVal(busyStock.getAddFourPlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 28, busyStock.getAddFourSafeScale(), cellStyle17);	  
			      //推迟5个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 29, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 30, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 31, BigDecimalUtil.getDoubleVal(busyStock.getAddFivePlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 32, busyStock.getAddFiveSafeScale(), cellStyle17); 
			      //推迟6个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 33, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 34, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 35, BigDecimalUtil.getDoubleVal(busyStock.getAddSixPlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 36, busyStock.getAddSixSafeScale(), cellStyle17);				  
			      //推迟7个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 37, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 38, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 39, BigDecimalUtil.getDoubleVal(busyStock.getAddSevenPlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 40, busyStock.getAddSevenSafeScale(), cellStyle17);		  
			      //推迟8个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 41, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 42, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 43, BigDecimalUtil.getDoubleVal(busyStock.getAddEightPlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 44, busyStock.getAddEightSafeScale(), cellStyle17);  
			      //推迟9个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 45, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 46, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 47, BigDecimalUtil.getDoubleVal(busyStock.getAddNinePlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 48, busyStock.getAddNineSafeScale(), cellStyle17);
			      //推迟10个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 49, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 50, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 51, BigDecimalUtil.getDoubleVal(busyStock.getAddTenPlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 52, busyStock.getAddTenSafeScale(), cellStyle17); 
			      //推迟11个月预测------------
		    	  ExcelUtil.setValue(sheet, rowNum, 53, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 54, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 55, BigDecimalUtil.getDoubleVal(busyStock.getAddElevenPlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 56, busyStock.getAddElevenSafeScale(), cellStyle17); 
			      //推迟12个月预测
		    	  ExcelUtil.setValue(sheet, rowNum, 57, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanPrdNum()), cellStyle14);
				  ExcelUtil.setValue(sheet, rowNum, 58, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanDlvNum()), cellStyle15);
				  ExcelUtil.setValue(sheet, rowNum, 59, BigDecimalUtil.getDoubleVal(busyStock.getAddTwelvePlanEndStock()), cellStyle16);
				  ExcelUtil.setValue(sheet, rowNum, 60, busyStock.getAddTwelveSafeScale(), cellStyle17);

			}
	        sheet.autoSizeColumn((short)1); //调整第二列宽度
	        sheet.autoSizeColumn((short)3); //调整第四列宽度
			try {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownlocad");
				realName=URLEncoder.encode(realName, "utf-8");
				response.setHeader("Content-Disposition", "attachment;filename="+realName);
				os = response.getOutputStream();
				wb.write(os);
				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			IoUtil.closeIo(os,fis,wb);
		}
		return null;
	}
	//----------------------------设置背景色------------------------
	public void setFontAndBgColor(int begin,int end ,int rowNum,Workbook wb,Font font,Sheet sheet,short color){
		  for (int j = begin;j<=end; j++) {
  			  CellStyle cellStyle = wb.createCellStyle();
			  CellStyle cellStyle0 = ExcelUtil.getCellStyle(sheet, rowNum, j);
			  cellStyle.cloneStyleFrom(cellStyle0);
			  cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			  cellStyle.setFillForegroundColor(color);
			  cellStyle.setFont(font);
			  ExcelUtil.setCellStyle(sheet, rowNum,j, cellStyle);
		  }
	}
	//----------------------------设置背景色------------------------
}
