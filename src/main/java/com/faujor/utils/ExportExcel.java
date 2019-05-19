package com.faujor.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.entity.bam.CutLiaiField;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.bam.CutPlan;
import com.faujor.entity.bam.CutPlanMate;
import com.faujor.entity.bam.CutProductDO;
import com.faujor.entity.bam.psm.PadPlanDetailForm;
import com.faujor.entity.bam.psm.PadPlanRecord;
import com.faujor.entity.rm.AppoDeli;
import com.faujor.entity.rm.CutMatePack;
import com.faujor.entity.rm.PackOrderVO;
import com.faujor.entity.rm.SemiMatePack;
import com.faujor.entity.rm.UserSuppMate;

public class ExportExcel {
	
	/**
	 * 导出打切联络单
	 * @param cutLiai
	 * @param ja
	 * @param sheetForAuth
	 */
	public static void createExcelOfCutLiai(JSONArray jay,CutLiaison cutLiai,JSONArray ja,
			HSSFWorkbook wb) {
		   //新建工作表
		   HSSFSheet sheetForAuth = wb.createSheet();
		   //打切联络单的表头   
		   //创建一行作为表头
//			HSSFRow rowHead = sheetForAuth.createRow(0);
			//设置列宽
		    sheetForAuth.setColumnWidth(0, 7000);
		    sheetForAuth.setColumnWidth(1, 7000);
		    sheetForAuth.setColumnWidth(2, 7000);
//		    sheetForAuth.setColumnWidth(3, 7000);
//		    sheetForAuth.setColumnWidth(4, 7000);
//		    sheetForAuth.setColumnWidth(5, 7000);
//		    sheetForAuth.setColumnWidth(6, 7000);
//		    sheetForAuth.setColumnWidth(7, 7000);
//		    sheetForAuth.setColumnWidth(8, 7000);
		    //填写表头信息
//			rowHead.createCell(0).setCellValue("编码");
//			rowHead.createCell(1).setCellValue("打切月份");
//			rowHead.createCell(2).setCellValue("状态");
//			rowHead.createCell(3).setCellValue("打切联络单号");
//			rowHead.createCell(4).setCellValue("供应商编码");
//			rowHead.createCell(5).setCellValue("供应商名称");
//			rowHead.createCell(6).setCellValue("创建人编码");
//			rowHead.createCell(7).setCellValue("创建人名称");
//			rowHead.createCell(8).setCellValue("创建时间");
			
//			String liaiId = cutLiai.getLiaiId();
//			if(liaiId != null && liaiId.equals("null")){
//				liaiId="";
//			}
			String cutMonth = cutLiai.getCutMonth();
			if(cutMonth != null && cutMonth.equals("null")){
				cutMonth="";
			}
			Date date = DateUtils.parse(cutMonth, "yyyy-MM");
			String format = DateUtils.format(date, "yyyy年MM月");
//			String status = cutLiai.getStatus();
//			if(status != null && status.equals("null")){
//				status="";
//			}
//			String liaiCode = cutLiai.getLiaiCode();
//			if(liaiCode != null && liaiCode.equals("null")){
//				liaiCode="";
//			}
//			String suppId = cutLiai.getSuppId();
//			if(suppId != null && suppId.equals("null")){
//				suppId="";
//			}
			String suppName = cutLiai.getSuppName();
			if(suppName != null && suppName.equals("null")){
				suppName="";
			}
//			String createId = cutLiai.getCreateId();
//			if(createId != null && createId.equals("null")){
//				createId="";
//			}
//			String creator = cutLiai.getCreator();
//			if(creator != null && creator.equals("null")){
//				creator="";
//			}
//			Date createDate = cutLiai.getCreateDate();
//			String date =  DateUtils.format(createDate);
//			if(date != null && date.equals("null")){
//				date="";
//			}
			// 创建新的一行
			HSSFRow row = sheetForAuth.createRow(0);
			//给这一行的每列赋值
			row.createCell(0).setCellValue(format);
			row.createCell(1).setCellValue(suppName);
			row.createCell(2).setCellValue("打切・改良品・新製品連絡表");
//			row.createCell(3).setCellValue(liaiCode);
//			row.createCell(4).setCellValue(suppId);
//			row.createCell(5).setCellValue(suppName);
//			row.createCell(6).setCellValue(createId);
//			row.createCell(7).setCellValue(creator);
//			row.createCell(8).setCellValue(date);
		
//{"cutAim":"5555","boxNumber":1,"mateCode":"0402.90.239","C301":23,"mateName":"MOTC","version":"1.0","B303":2,"id":"1c14534faea84244ae3e002da3d4b7ad","mainStru":"40 外箱","A303":"2","inveNum":22,"A302":"21","B302":21,"outNum":22,"prodNum":0}
			//String fields = cutLiai.getFields();
			//JSONArray jay = JSONArray.parseArray(fields);
			//打切联络单下物料的表头
			//创建一行作为表头
 			HSSFRow rowHead2 = sheetForAuth.createRow(2);
 			for(int i =1;i<jay.size();i++){
 				//设置列宽
 				sheetForAuth.setColumnWidth(i-1, 7000);
 				CutLiaiField field2 = jay.getObject(i, CutLiaiField.class);
 				//填写表头信息
				rowHead2.createCell(i-1).setCellValue(field2.getTitle());
			}
 		
 			for(int k =0;k<ja.size();k++){
 				//得到一个对象
 				JSONObject jo = (JSONObject) ja.get(k);
 				System.out.println(jo);
 				System.out.println(jo.get("mateCode"));
 				// 创建新的一行
 				HSSFRow row2 = sheetForAuth.createRow(k + 3);
 				for(int i =1;i<jay.size();i++){
 					CutLiaiField field2 = jay.getObject(i, CutLiaiField.class);
 					Object ob = jo.get(field2.getField());
 					String str="";
 					if(ob != null){
 						if (ob instanceof Integer) {
 						    int value = ((Integer) ob).intValue();
 						   //给这一行的每列赋值
 						    if(value < 0) {
								HSSFCellStyle createCellStyle = wb.createCellStyle();
								HSSFFont font = wb.createFont();
								font.setColor(HSSFColor.RED.index);
								createCellStyle.setFont(font);
								//如果值小于加上红色字体
 						    	//Font font = wb.createFont();
 								//font.setColor("红色");
 								row2.createCell(i-1).setCellStyle(createCellStyle);
 								
 						    }
 	 						row2.createCell(i-1).setCellValue(value);
 						} else if (ob instanceof String) {
 						    String s = (String) ob;
 						    if(s.contains("<")) {
 						    	int index1 = s.indexOf(">");
 						    	int index2 = s.indexOf("<", index1);
 						    	s = s.substring(index1+1, index2);
 						    	HSSFCellStyle createCellStyle = wb.createCellStyle();
								HSSFFont font = wb.createFont();
								font.setColor(HSSFColor.RED.index);
								font.setBold(true);
								font.setFontName("黑体");
								createCellStyle.setFont(font);
								//如果值小于加上红色字体
 						    	//Font font = wb.createFont();
 								//font.setColor("红色");
 								row2.createCell(i-1).setCellStyle(createCellStyle);
 						    }
 						    //给这一行的每列赋值
 	 						row2.createCell(i-1).setCellValue(s);
 						} 
 					}else{
 						//给这一行的每列赋值
 						row2.createCell(i-1).setCellValue(str);
 					}
 				}
 			}
	}
	/**
	 * 设置下载文件编码
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 */
	public final static void setAttachmentFileName(HttpServletRequest request,
			HttpServletResponse response, String fileName)
			throws UnsupportedEncodingException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/octet-stream;charset=UTF-8");

		fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
		response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
	}
	
	/**
	 * 导出打切计划
	 * @param cutPlan
	 * @param list
	 * @param sheetForAuth
	 */
	public static void createExcelOfCutPlan(CutPlan cutPlan, List<CutPlanMate> list, HSSFSheet sheetForAuth) {
		   //打切联络单的表头   
		    //创建一行作为表头
			HSSFRow rowHead = sheetForAuth.createRow(0);
			//设置列宽
		    //sheetForAuth.setColumnWidth(0, 7000);
		    sheetForAuth.setColumnWidth(1, 7000);
		    sheetForAuth.setColumnWidth(2, 7000);
		    sheetForAuth.setColumnWidth(3, 7000);
		    sheetForAuth.setColumnWidth(4, 4000);
		    sheetForAuth.setColumnWidth(5, 7000);
		    sheetForAuth.setColumnWidth(6, 7000);
		    sheetForAuth.setColumnWidth(7, 7000);
		    sheetForAuth.setColumnWidth(8, 7000);
		    sheetForAuth.setColumnWidth(9, 7000);
		    sheetForAuth.setColumnWidth(10, 7000);
		    sheetForAuth.setColumnWidth(11, 7000);
		    sheetForAuth.setColumnWidth(12, 7000);
		    sheetForAuth.setColumnWidth(13, 7000);
		    sheetForAuth.setColumnWidth(14, 7000);
		    sheetForAuth.setColumnWidth(15, 7000);
		    sheetForAuth.setColumnWidth(16, 7000);
		    sheetForAuth.setColumnWidth(17, 7000);
		    sheetForAuth.setColumnWidth(18, 7000);
		    sheetForAuth.setColumnWidth(19, 7000);
		    //填写表头信息
			//rowHead.createCell(0).setCellValue("编码");
			rowHead.createCell(1).setCellValue("打切月份");
			rowHead.createCell(2).setCellValue("状态");
			rowHead.createCell(3).setCellValue("打切计划号");
			rowHead.createCell(4).setCellValue("计划名称");
			rowHead.createCell(5).setCellValue("创建人编码");
			rowHead.createCell(6).setCellValue("创建人名称");
			rowHead.createCell(7).setCellValue("创建时间");
			
			String planId = cutPlan.getPlanId();
			if(planId != null && planId.equals("null")){
				planId="";
			}
			String cutMonth = cutPlan.getCutMonth();
			if(cutMonth != null && cutMonth.equals("null")){
				cutMonth="";
			}
			String status = cutPlan.getStatus();
			if(status != null && status.equals("null")){
				status="";
			}
			String cutPlanCode = cutPlan.getCutPlanCode();
			if(cutPlanCode != null && cutPlanCode.equals("null")){
				cutPlanCode="";
			}
			String planName = cutPlan.getPlanName();
			if(planName != null && planName.equals("null")){
				planName="";
			}
			
			String createId = cutPlan.getCreateId();
			if(createId != null && createId.equals("null")){
				createId="";
			}
			String creator = cutPlan.getCreater();
			if(creator != null && creator.equals("null")){
				creator="";
			}
			Date createDate = cutPlan.getCreateDate();
			String date =  DateUtils.format(createDate);
			if(date != null && date.equals("null")){
				date="";
			}
			// 创建新的一行
			HSSFRow row = sheetForAuth.createRow(1);
			//给这一行的每列赋值
			//row.createCell(0).setCellValue(planId);
			row.createCell(1).setCellValue(cutMonth);
			row.createCell(2).setCellValue(status);
			row.createCell(3).setCellValue(cutPlanCode);
			row.createCell(4).setCellValue(planName);
			row.createCell(5).setCellValue(createId);
			row.createCell(6).setCellValue(creator);
			row.createCell(7).setCellValue(date);
			
			//创建一行作为表头
 			HSSFRow rowHead2 = sheetForAuth.createRow(4);
 			String str = cutMonth.substring(5);
 			int i = Integer.parseInt(str);
 			int h = i-1;
 			if(h==0){
 				h=12;
 			}
 			int addone = i;
 			int addtwo = i+1 >12 ? (i-11):(i+1);
 			int addthree = i+2 >12 ? (i-10):(i+2);
 			int addfour = i+3 >12 ? (i-9):(i+3);
 			int addfive= i+4 >12 ? (i-8):(i+4);
 			int addsix = i+5 >12 ? (i-7):(i+5);
 			//填写表头信息
			//rowHead2.createCell(0).setCellValue("编码");
			//rowHead2.createCell(1).setCellValue("打切计划主键");
			rowHead2.createCell(1).setCellValue("物料编码");
			rowHead2.createCell(2).setCellValue("物料名称");
			rowHead2.createCell(3).setCellValue("物料版本");
			rowHead2.createCell(4).setCellValue("大品项");
			rowHead2.createCell(5).setCellValue(h+"月底成品库存+在途");
			rowHead2.createCell(6).setCellValue(h+"月底在外订单");
			rowHead2.createCell(7).setCellValue("供应商成品库存量");
			rowHead2.createCell(8).setCellValue("主包材箱数");
			rowHead2.createCell(9).setCellValue(addone+"月预测");
			rowHead2.createCell(10).setCellValue(addtwo+"月预测");
			rowHead2.createCell(11).setCellValue(addthree+"月预测");
			rowHead2.createCell(12).setCellValue(addfour+"月预测");
			rowHead2.createCell(13).setCellValue(addfive+"月预测");
			rowHead2.createCell(14).setCellValue(addsix+"月预测");
			rowHead2.createCell(15).setCellValue("剩余量");
			rowHead2.createCell(16).setCellValue("预计替换时间");
			rowHead2.createCell(17).setCellValue("打切目的");
			rowHead2.createCell(18).setCellValue("打切进度");
			rowHead2.createCell(19).setCellValue("备注");
			 BigDecimal zero = new BigDecimal(0);
			 CutPlanMate cp = new CutPlanMate();
			for (int k=0;k<list.size();k++) {
				cp=list.get(k);
				String planMateId = cp.getPlanMateId();
				if(planMateId != null && planMateId.equals("null")){
					planMateId="";
				}
				String planId2 = cp.getPlanId();
				if(planId2 != null && planId2.equals("null")){
					planId2="";
				}
				String mateId = cp.getMateId();
				if(mateId != null && mateId.equals("null")){
					mateId="";
				}
				String mateName = cp.getMateName();
				if(mateName != null && mateName.equals("null")){
					mateName="";
				}
				String mateVersion = cp.getMateVersion();
				if(mateVersion != null && mateVersion.equals("null")){
					mateVersion="";
				}
				String bigItemExpl = cp.getBigItemExpl();
				if(bigItemExpl != null && bigItemExpl.equals("null")){
					bigItemExpl="";
				}
				 BigDecimal nowInve = cp.getNowInve();
				 if(nowInve == null){
					 nowInve =zero;
				  }
				 String nowInve2 = nowInve.toString();
				 BigDecimal outInve = cp.getOutInve();
				 if(outInve == null){
					 outInve=zero;
				  }
				  String outInve2 = outInve.toString();
				  BigDecimal inveNum = cp.getInveNum();
				  if(inveNum == null){
					  inveNum=zero;
					  }
				  String inveNum2 = inveNum.toString();
				  BigDecimal mainStruNum = cp.getMainStruNum();
				  if(mainStruNum == null){
					  mainStruNum=zero;
					  }
				  String mainStruNum2 = mainStruNum.toString();
				 BigDecimal addOne2 = cp.getAddOne();
				 if(addOne2 == null){
					 addOne2=zero;
				  }
				  String addone3 = addOne2.toString();
				  
				BigDecimal addTwo2 = cp.getAddTwo();
				if(addTwo2 == null){
					addTwo2=zero;
				  }
				  String addTwo3 = addTwo2.toString();
				  
				BigDecimal addThree2 = cp.getAddThree();
				if(addThree2 == null){
					addThree2=zero;
				  }
				  String addThree3 = addThree2.toString();
				  
				BigDecimal addFour2 = cp.getAddFour();
				if(addFour2 == null){
					addFour2=zero;
				  }
				  String addFour3 = addFour2.toString();
				    
				BigDecimal addFive2 = cp.getAddFive();
				if(addFive2 == null){
					addFive2=zero;
				  }
				  String addFive3 = addFive2.toString();
				  
				BigDecimal addSix2 = cp.getAddSix();
				if(addSix2 == null){
					addSix2=zero;
				  }
					String addSix3 = addSix2.toString();
				  
				BigDecimal residue = cp.getResidue();
				if(residue == null){
					residue=zero;
				  }
					String residue2 = residue.toString();
				  
				String replaceDate = cp.getReplaceDate();
				if(replaceDate != null && replaceDate.equals("null")){
					replaceDate="";
				}
				String cutGoal = cp.getCutGoal();
				if(cutGoal != null && cutGoal.equals("null")){
					cutGoal="";
				}
				String cutSche = cp.getCutSche();
				if(cutSche != null && cutSche.equals("null")){
					cutSche="";
				}
				String remark = cp.getRemark();
				if(remark != null && remark.equals("null")){
					remark="";
				}
				//给这一行的每列赋值
				// 创建新的一行
 				HSSFRow row2 = sheetForAuth.createRow(k + 5);
				//row2.createCell(0).setCellValue(planMateId);
				//row2.createCell(1).setCellValue(planId2);
				row2.createCell(1).setCellValue(mateId);
				row2.createCell(2).setCellValue(mateName);
				row2.createCell(3).setCellValue(mateVersion);
				row2.createCell(4).setCellValue(bigItemExpl);
				row2.createCell(5).setCellValue(nowInve2);
				row2.createCell(6).setCellValue(outInve2);
				row2.createCell(7).setCellValue(inveNum2);
				row2.createCell(8).setCellValue(mainStruNum2);
				row2.createCell(9).setCellValue(addone3);
				row2.createCell(10).setCellValue(addTwo3);
				row2.createCell(11).setCellValue(addThree3);
				row2.createCell(12).setCellValue(addFour3);
				row2.createCell(13).setCellValue(addFive3);
				row2.createCell(14).setCellValue(addSix3);
				row2.createCell(15).setCellValue(residue2);
				row2.createCell(16).setCellValue(replaceDate);
				row2.createCell(17).setCellValue(cutGoal);
				row2.createCell(18).setCellValue(cutSche);
				row2.createCell(19).setCellValue(remark);
				
			}
	}
	/**
	 * 导出打切维护品
	 * @param cpdoList
	 * @param sheetForAuth
	 */
	public static void createExcelOfCutProduct(List<CutProductDO> cpdoList, HSSFSheet sheetForAuth) {
		//创建一行作为表头
		HSSFRow rowHead = sheetForAuth.createRow(0);
		//设置列宽
	    //sheetForAuth.setColumnWidth(0, 7000);
	    sheetForAuth.setColumnWidth(1, 7000);
	    sheetForAuth.setColumnWidth(2, 7000);
	    sheetForAuth.setColumnWidth(3, 3500);
	    sheetForAuth.setColumnWidth(4, 3500);
	    sheetForAuth.setColumnWidth(5, 3000);
	    sheetForAuth.setColumnWidth(6, 3000);
	    sheetForAuth.setColumnWidth(7, 3000);
	    sheetForAuth.setColumnWidth(8, 3000);
	    sheetForAuth.setColumnWidth(9, 7000);
	    sheetForAuth.setColumnWidth(10, 7000);
	    //填写表头信息
		//rowHead.createCell(0).setCellValue("编码");
		rowHead.createCell(1).setCellValue("成品物料编码");
		rowHead.createCell(2).setCellValue("物料名称");
		rowHead.createCell(3).setCellValue("版本");
		rowHead.createCell(4).setCellValue("物料组说明");
		rowHead.createCell(5).setCellValue("物料类型");
		rowHead.createCell(6).setCellValue("基本单位");
		rowHead.createCell(7).setCellValue("采购单位");
		rowHead.createCell(8).setCellValue("打切品类型");
		rowHead.createCell(9).setCellValue("主包材");
		rowHead.createCell(10).setCellValue("打切目的");
		int size = cpdoList.size();
		CutProductDO cp = new CutProductDO();
		for(int i =0;i<size;i++){
			cp = cpdoList.get(i);
			String mateCode = cp.getMateCode();
			if(mateCode != null && mateCode.equals("null")){
				mateCode="";
			}
			String mateName = cp.getMateName();
			if(mateName != null && mateName.equals("null")){
				mateName="";
			}
			String version = cp.getVersion();
			if(version != null && version.equals("null")){
				version="";
			}
			String mategroup = cp.getMateGroupExpl();
			if(mategroup != null && mategroup.equals("null")){
				mategroup="";
			}
			String mateType = cp.getMateType();
			if(mateType != null && mateType.equals("null")){
				mateType="";
			}
			String basicUnit = cp.getBasicUnit();
			if(basicUnit != null && basicUnit.equals("null")){
				basicUnit="";
			}
			String proUnit = cp.getProcUnit();
			if(proUnit != null && proUnit.equals("null")){
				proUnit="";
			}
			String isSpecial = cp.getIsSpecial();
			if(isSpecial != null && isSpecial.equals("null")){
				isSpecial="";
			}
			if("YES".equals(isSpecial)){
				isSpecial = "自制打切";
			}else if("NO".equals(isSpecial)){
				isSpecial = "OEM打切";
			}
			String mainStru = cp.getMainStru();
			if(mainStru != null && mainStru.equals("null")){
				mainStru="";
			}
			String cutAim = cp.getCutAim();
			if(cutAim != null && cutAim.equals("null")){
				cutAim="";
			}
			
			// 创建新的一行
			HSSFRow row = sheetForAuth.createRow(i+1);
			//给这一行的每列赋值
			//row.createCell(0).setCellValue(planId);
			row.createCell(1).setCellValue(mateCode);
			row.createCell(2).setCellValue(mateName);
			row.createCell(3).setCellValue(version);
			row.createCell(4).setCellValue(mategroup);
			row.createCell(5).setCellValue(mateType);
			row.createCell(6).setCellValue(basicUnit);
			row.createCell(7).setCellValue(proUnit);
			row.createCell(8).setCellValue(isSpecial);
			row.createCell(9).setCellValue(mainStru);
			row.createCell(10).setCellValue(cutAim);
			
		}
		
	}
	/**
	 * 导出预约送货一览列表中的数据，按模板导出
	 * @param appoDeliList
	 * @param req
	 * @param res
	 * @return
	 */
	public static Workbook exportAppoDeliList(List<AppoDeli> appoDeliList, HttpServletRequest req,
			HttpServletResponse res) {
		Workbook wb = null;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\预约送货一览表.xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			Sheet sheetForAuth = wb.getSheetAt(0);
			double appoNum = 0;
			double deliNum = 0;
			double receNum = 0;
			int moneLength = 2;
			if (appoDeliList != null) {
				int listSize = appoDeliList.size();
				moneLength = moneLength +listSize+5;
				for (int i = 0; i < listSize; i++) {
					AppoDeli appoDeli = appoDeliList.get(i);
					String sapId = appoDeli.getSapId();
					if (sapId != null && sapId.equals("null")) {
						sapId = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 0, sapId, null);
					String suppName = appoDeli.getSuppName();
					if (suppName != null && suppName.equals("null")) {
						suppName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 1, suppName, null);
					String suppRange = appoDeli.getSuppRange();
					if (suppRange != null && suppRange.equals("null")) {
						suppRange = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 2, suppRange, null);
					String suppRangeDesc = appoDeli.getSuppRangeDesc();
					if (suppRangeDesc != null && suppRangeDesc.equals("null")) {
						suppRangeDesc = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 3, suppRangeDesc, null);
					String appoType = appoDeli.getAppoType();
					if (appoType != null && appoType.equals("null")) {
						appoType = "";
					}else{
						if("appo".equals(appoType)){
							appoType = "预约单";
						}else if("stra".equals(appoType)){
							appoType = "直发单";
						}
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 4, appoType, null);
					String appoCode = appoDeli.getAppoCode();
					if (appoCode != null && appoCode.equals("null")) {
						appoCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 5, appoCode, null);
					String appoStatus = appoDeli.getAppoStatus();
					if (appoStatus != null && appoStatus.equals("null")) {
						appoStatus = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 6, appoStatus, null);
					String receUnit = appoDeli.getReceUnit();
					if (receUnit != null && receUnit.equals("null")) {
						receUnit = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 7, receUnit, null);
					String requCode = appoDeli.getRequCode();
					if (requCode != null && requCode.equals("null")) {
						requCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 8, requCode, null);
					String mateName = appoDeli.getMateName();
					if (mateName != null && mateName.equals("null")) {
						mateName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 9, mateName, null);
					Double mateNumber = appoDeli.getMateNumber();
					if (mateNumber == null) {
						mateNumber = 0D;
					}
					appoNum+=mateNumber;
					ExcelUtil.setValue(sheetForAuth, i+2, 10, mateNumber, null);
					String deliCode = appoDeli.getDeliCode();
					if (deliCode != null && deliCode.equals("null")) {
						deliCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 11, deliCode, null);
					String status = appoDeli.getStatus();
					if (status != null && status.equals("null")) {
						status = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 12, status, null);
					String frequency = appoDeli.getFrequency();
					if (frequency != null && frequency.equals("null")) {
						frequency = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 13, frequency, null);
					String orderId = appoDeli.getOrderId();
					if (orderId != null && orderId.equals("null")) {
						orderId = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 14, orderId, null);
					Double unpaNumber = appoDeli.getUnpaNumber();
					if (unpaNumber == null) {
						unpaNumber = 0D;
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 15, unpaNumber, null);
					Double calculNumber = appoDeli.getCalculNumber();
					if (calculNumber == null) {
						calculNumber = 0D;
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 16, calculNumber, null);
					Double deliNumber = appoDeli.getDeliNumber();
					if (deliNumber == null) {
						deliNumber = 0D;
					}
					deliNum+=deliNumber;
					ExcelUtil.setValue(sheetForAuth, i+2, 17, deliNumber, null);
					String receCode = appoDeli.getReceCode();
					if (receCode != null && receCode.equals("null")) {
						receCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 18, receCode, null);
					String receStatus = appoDeli.getReceStatus();
					if (receStatus != null && receStatus.equals("null")) {
						receStatus = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 19, receStatus, null);
					Double receNumber = appoDeli.getReceNumber();
					if (receNumber == null) {
						receNumber = 0D;
					}
					receNum+=receNumber;
					ExcelUtil.setValue(sheetForAuth, i+2, 20, receNumber, null);
					String inboDeliCode = appoDeli.getInboDeliCode();
					if (inboDeliCode != null && inboDeliCode.equals("null")) {
						inboDeliCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 21, inboDeliCode, null);
					String isOccupy = appoDeli.getIsOccupy();
					if (isOccupy != null && isOccupy.equals("null")) {
						isOccupy = "";
					}else{
						if("yes".equals(isOccupy)){
							isOccupy = "是";
						}else if("no".equals(isOccupy)){
							isOccupy = "否";
						}
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 22, isOccupy, null);
					String mateCode = appoDeli.getMateCode();
					if (mateCode != null && mateCode.equals("null")) {
						mateCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 23, mateCode, null);
					Date AppoDate = appoDeli.getAppoDate();
					String format = DateUtils.format(AppoDate, "yyyy-MM-dd");
					if (format != null && format.equals("null")) {
						format = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 24, format, null);
					Date createDate = appoDeli.getCreateDate();
					String formatCreateDate = DateUtils.format(createDate, "yyyy-MM-dd");
					if (formatCreateDate != null && formatCreateDate.equals("null")) {
						formatCreateDate = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 25, formatCreateDate, null);
					Date deliDate = appoDeli.getDeliDate();
					String formatdeliDate = DateUtils.format(deliDate, "yyyy-MM-dd");
					if (formatdeliDate != null && formatdeliDate.equals("null")) {
						formatdeliDate = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 26, formatdeliDate, null);
					String deliCreDate = appoDeli.getDeliCreateDate();
					//String formatdeliCreDate = DateUtils.format(deliCreDate, "yyyy-MM-dd");
					if(deliCreDate != null && deliCreDate !=""){
						Date parsedeliCreDate = DateUtils.parse(deliCreDate, "yyyy-MM-dd");
						String format2 = DateUtils.format(parsedeliCreDate, "yyyy-MM-dd");
						ExcelUtil.setValue(sheetForAuth, i+2, 27, format2, null);
					}else{
						ExcelUtil.setValue(sheetForAuth, i+2, 27, deliCreDate, null);
					}
					
					Date receDate = appoDeli.getReceDate();
					String formatreceDate = DateUtils.format(receDate, "yyyy-MM-dd");
					if (formatreceDate != null && formatreceDate.equals("null")) {
						formatreceDate = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 28, formatreceDate, null);
					String receCreDate = appoDeli.getReceCreateDate();
					//String formatdeliCreDate = DateUtils.format(deliCreDate, "yyyy-MM-dd");
					if (receCreDate != null && receCreDate != "") {
						Date parsereceCreDate = DateUtils.parse(receCreDate, "yyyy-MM-dd");
						String format3 = DateUtils.format(parsereceCreDate, "yyyy-MM-dd");
						ExcelUtil.setValue(sheetForAuth, i+2, 29, format3, null);
					}else{
						ExcelUtil.setValue(sheetForAuth, i+2, 29, receCreDate, null);
					}
				}
				/*if (listSize > 20) {
					moneLength = moneLength + 1;
				} else {
					moneLength = 21;
				}*/
				Font font = wb.createFont();
				font.setBold(true);
				font.setFontName("微软雅黑");
				CellStyle cellStyle = ExcelUtil.setCellStyle(false, false, 2, 2, font, wb);
				ExcelUtil.setValue(sheetForAuth, moneLength, 0, "合计", null);
				ExcelUtil.setValue(sheetForAuth, moneLength, 9, "预约总数", null);
				ExcelUtil.setValue(sheetForAuth, moneLength, 10, appoNum, cellStyle);
				ExcelUtil.setValue(sheetForAuth, moneLength, 16, "实际送货总数", null);
				ExcelUtil.setValue(sheetForAuth, moneLength, 17, deliNum, cellStyle);
				ExcelUtil.setValue(sheetForAuth, moneLength, 19, "实收总数", null);
				ExcelUtil.setValue(sheetForAuth, moneLength, 20, receNum, cellStyle);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	//导出采购货源一览表数据
	public static Workbook exportUserSuppMateList(List<UserSuppMate> list, HttpServletRequest req,
			HttpServletResponse res) {
		Workbook wb = null;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\采购货源一览表 .xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			Sheet sheetForAuth = wb.getSheetAt(0);
			if (list != null && list.size()>0) {
				int listSize = list.size();
				for (int i = 0; i < listSize; i++) {
					UserSuppMate usm = list.get(i);
					String userId = usm.getUserId();
					if (userId != null && userId.equals("null")) {
						userId = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 0, userId, null);
					String name = usm.getName();
					if (name != null && name.equals("null")) {
						name = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 1, name, null);
					String suppName = usm.getSuppName();
					if (suppName != null && suppName.equals("null")) {
						suppName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 2, suppName, null);
					String sapId = usm.getSapId();
					if (sapId != null && sapId.equals("null")) {
						sapId = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 3, sapId, null);
					String mateCode = usm.getMateCode();
					if (mateCode != null && mateCode.equals("null")) {
						mateCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 4, mateCode, null);
					String mateName = usm.getMateName();
					if (mateName != null && mateName.equals("null")) {
						mateName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 5, mateName, null);
					String mateGroupExpl = usm.getMateGroupExpl();
					if (mateGroupExpl != null && mateGroupExpl.equals("null")) {
						mateGroupExpl = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 6, mateGroupExpl, null);
					String mateType = usm.getMateType();
					if (mateType != null && mateType.equals("null")) {
						mateType = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 7, mateType, null);
					String basicUnit = usm.getBasicUnit();
					if (basicUnit != null && basicUnit.equals("null")) {
						basicUnit = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 8, basicUnit, null);
					String procUnit = usm.getProcUnit();
					if (procUnit != null && procUnit.equals("null")) {
						procUnit = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+1, 9, procUnit, null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	/**
	 * 导出半成品包材统计数据
	 * @param list
	 * @param req
	 * @param res
	 * @return
	 */
	public static Workbook exportSemiMatePackList(List<SemiMatePack> list, HttpServletRequest req,
			HttpServletResponse res) {
		Workbook wb = null;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\半成品包材统计表.xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			Sheet sheetForAuth = wb.getSheetAt(0);
			if (list != null && list.size()>0) {
				int listSize = list.size();
				for (int i = 0; i < listSize; i++) {
					SemiMatePack semiMatePack = list.get(i);
					String oemOrderCode = semiMatePack.getOemOrderCode();
					if (oemOrderCode != null && oemOrderCode.equals("null")) {
						oemOrderCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 0, oemOrderCode, null);
					Date orderDate = semiMatePack.getOrderDate();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String format2 = format.format(orderDate);
					if (format2 != null && format2.equals("null")) {
						format2 = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 1, format2, null);
					String oemSuppName = semiMatePack.getOemSuppName();
					if (oemSuppName != null && oemSuppName.equals("null")) {
						oemSuppName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 2, oemSuppName, null);
					String mateName = semiMatePack.getMateName();
					if (mateName != null && mateName.equals("null")) {
						mateName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 3, mateName, null);
					Double purcQuan = semiMatePack.getPurcQuan();//采购数量
					if(purcQuan == null ) {
						purcQuan = 0D;
					}
					String purcQuanStr = purcQuan.toString();
					if (purcQuanStr != null && purcQuanStr.equals("null")) {
						purcQuanStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 4, purcQuanStr, null);
					Double quanMate = semiMatePack.getQuanMate();//已交数量
					if(quanMate == null) {
						quanMate = 0D;
					}
					String quanMateStr = quanMate.toString();
					if (quanMateStr != null && quanMateStr.equals("null")) {
						quanMateStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 5, quanMateStr, null);
					Double unpaQuan = semiMatePack.getUnpaQuan();//未交数量
					if(unpaQuan == null) {
						unpaQuan = 0D;
					}
					String unpaQuanStr = unpaQuan.toString();
					if (unpaQuanStr != null && unpaQuanStr.equals("null")) {
						unpaQuanStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 6, unpaQuanStr, null);
					String packName = semiMatePack.getPackName();
					if (packName != null && packName.equals("null")) {
						packName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 7, packName, null);
					Double packTotalNum = semiMatePack.getPackTotalNum();//包材总量
					if(packTotalNum == null ) {
						packTotalNum = 0D;
					}
					String packTotalNumStr = packTotalNum.toString();
					if (packTotalNumStr != null && packTotalNumStr.equals("null")) {
						packTotalNumStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 8, packTotalNumStr, null);
					Double placedNum = semiMatePack.getPlacedNum();//已下单数量
					if(placedNum == null ) {
						placedNum = 0D;
					}
					String placedNumStr = placedNum.toString();
					if (placedNumStr != null && placedNumStr.equals("null")) {
						placedNumStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 9, placedNumStr, null);
					Double residueNum = semiMatePack.getResidueNum();//剩余下单数量
					if(residueNum == null ) {
						residueNum = 0D;
					}
					String residueNumStr = residueNum.toString();
					if (residueNumStr != null && residueNumStr.equals("null")) {
						residueNumStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 10, residueNumStr, null);
					String oemSapId = semiMatePack.getOemSapId();
					if (oemSapId != null && oemSapId.equals("null")) {
						oemSapId = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 11, oemSapId, null);
					String mateCode = semiMatePack.getMateCode();
					if (mateCode != null && mateCode.equals("null")) {
						mateCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 12, mateCode, null);
					String packCode = semiMatePack.getPackCode();
					if (packCode != null && packCode.equals("null")) {
						packCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 13, packCode, null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	public static Workbook exportCutMatePackList(Map<String, Object> result, HttpServletRequest req,
			HttpServletResponse res) {
		Workbook wb = null;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\打切包材统计表.xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			Sheet sheetForAuth = wb.getSheetAt(0);
			//列表的头部信息
			JSONArray headerJA = (JSONArray) result.get("jsonArray");
			//列表的数据信息
			JSONArray JAData = (JSONArray) result.get("data");
			//OEM打切联络单列头样式
			CellStyle oemFieldStyle = ExcelUtil.getCellStyle(sheetForAuth, 1, 8);
			//包材打切联络单列头样式
			CellStyle baoFieldStyle = ExcelUtil.getCellStyle(sheetForAuth, 1, 9);
			//物料列头样式
			CellStyle mateFieldStyle = ExcelUtil.getCellStyle(sheetForAuth, 1, 0);
			//数据样式
			CellStyle dataStyle = ExcelUtil.getCellStyle(sheetForAuth, 2, 0);
			//获取列宽
			int columnWidth = sheetForAuth.getColumnWidth(6);
			//------表头设置起始位置-------
			ExcelUtil.setValue(sheetForAuth, 2, 0, "mateCode", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 1, "mateName", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 2, "version", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 3, "boxNumber", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 4, "oemSuppName", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 5, "sumOutNum", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 6, "outNum", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 7, "sumInveNum", dataStyle);
			ExcelUtil.setValue(sheetForAuth, 2, 8, "inveNum", dataStyle);
			sheetForAuth.setColumnWidth(4, 7000);
			int count=0;
			Row row1 = sheetForAuth.getRow(1);
			for (int i = 0; i < headerJA.size(); i++) {
				CutLiaiField f = headerJA.getObject(i, CutLiaiField.class);
				String field = f.getField();
				String title = f.getTitle();
				//找到OEM动态列表
				if(field != null ) {
					if( field.startsWith("A") || field.startsWith("B")) {
						if(title.contains("OEM")) {
							Cell createCell = row1.createCell(9+count);
							createCell.setCellValue(title);
							createCell.setCellStyle(oemFieldStyle);
							sheetForAuth.setColumnWidth(9+count, 5000);
							ExcelUtil.setValue(sheetForAuth, 2, 9+count, field, dataStyle);
							count ++;
						}
					}
				}
			}
			int total = 0;
			for (int i = 0; i < headerJA.size(); i++) {
				CutLiaiField f = headerJA.getObject(i, CutLiaiField.class);
				String field = f.getField();
				String title = f.getTitle();
				//找到包材动态列表
				if(field != null) {
					if(field.contains("Out") || field.contains("OutCut") || field.contains("StockPack")) {
						if(field.contains("OutNum")) {
							continue;
						}
						if(total==0) {
							Cell createCell = row1.createCell(9+count);
							createCell.setCellValue("包材供应商");
							createCell.setCellStyle(baoFieldStyle);
							sheetForAuth.setColumnWidth(9+count, 7000);
							ExcelUtil.setValue(sheetForAuth, 2, 9+count, "baoSuppName", dataStyle);
						}
						Cell createCell = row1.createCell(9+count+1+total);
						createCell.setCellValue(title);
						createCell.setCellStyle(baoFieldStyle);
						sheetForAuth.setColumnWidth(9+count+1+total, 5000);
						ExcelUtil.setValue(sheetForAuth, 2, 9+count+1+total, field, dataStyle);
						total++;
					}
				}
			}		
			Cell createCell1 = row1.createCell(9+count+1+total);
			createCell1.setCellValue("OEM供应商编号");
			createCell1.setCellStyle(mateFieldStyle);
			sheetForAuth.setColumnWidth(9+count+1+total, 5000);
			ExcelUtil.setValue(sheetForAuth, 2, 9+count+1+total, "oemSapId", dataStyle);
			Cell createCell2 = row1.createCell(9+count+2+total);
			createCell2.setCellValue("包材供应商编号");
			createCell2.setCellStyle(mateFieldStyle);
			sheetForAuth.setColumnWidth(9+count+2+total, 5000);
			ExcelUtil.setValue(sheetForAuth, 2, 9+count+2+total, "baoSapId", dataStyle);
			//------表头设置截止位置-------
			//------写入数据------
			int cellNumber = 9+count+2+total;
			for (int i = 0; i < JAData.size(); i++) {
				JSONObject object = (JSONObject) JAData.get(i);
				for (int j = 0; j <= cellNumber; j++) {
					String field = ExcelUtil.getCellValue(sheetForAuth, 2, j);
					Object ob = object.get(field);
					if(ob != null) {
						if (ob instanceof Integer) {
							int value = ((Integer) ob).intValue();
							ExcelUtil.setValue(sheetForAuth, i+3, j, value, dataStyle);
						} else if (ob instanceof String) {
							String str = (String) ob;
							ExcelUtil.setValue(sheetForAuth, i+3, j, str, dataStyle);
						}else if (ob instanceof Double) {
							Double d = (Double) ob;
							
							ExcelUtil.setValue(sheetForAuth, i+3, j, d.toString(), dataStyle);
						}
					}else {
						ExcelUtil.setValue(sheetForAuth, i+3, j, "", dataStyle);
					}
				}
			}
			Row row2 = sheetForAuth.getRow(2);
			sheetForAuth.removeRow(row2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
		
	}
	/**
	 * 导出生产交货计划记录
	 * @param list
	 * @param map
	 * @param req
	 * @param res
	 * @return
	 */
	public static Workbook exportPadPlanMateList(List<PadPlanRecord> list, Map<String, Object> map,
			HttpServletRequest req, HttpServletResponse res) {
		Workbook wb = null;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\生产交货计划记录表.xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			Sheet sheetForAuth = wb.getSheetAt(0);
			PadPlanDetailForm paForm = (PadPlanDetailForm) map.get("paForm");
			CellStyle cellStyle = ExcelUtil.getCellStyle(sheetForAuth, 0, 0);
			ExcelUtil.setValue(sheetForAuth, 0, 0, paForm.getPlanMonth()+"生产交货计划记录表", cellStyle);
			//设置表头信息
			String[] array = new String[]{"one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve"};
			for (int i = 0; i < array.length; i++) {
				String number = array[i];
				String month = (String) map.get(number);
				String versionDesc = month+"版本";
				ExcelUtil.setValue(sheetForAuth, 1, 4+i, versionDesc, null);
			}
			if (list != null && list.size()>0) {
				int listSize = list.size();
				for (int i = 0; i < listSize; i++) {
					PadPlanRecord record = list.get(i);
					//排名
					String rank = record.getRank();
					if (rank != null && rank.equals("null")) {
						rank = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 0, rank, null);
					//产品系列
					String prodSeries = record.getProdSeries();
					if (prodSeries != null && prodSeries.equals("null")) {
						prodSeries = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 1, prodSeries, null);
					//大品项
					String bigItemExpl = record.getBigItemExpl();
					if (bigItemExpl != null && bigItemExpl.equals("null")) {
						bigItemExpl = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 2, bigItemExpl, null);
					//物料名称
					String matName = record.getMatName();
					if (matName != null && matName.equals("null")) {
						matName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 3, matName, null);
					Float qtyOne = record.getQtyOne();
					if(qtyOne == null) {
						qtyOne = 0F;
					}
					String qtyOneStr = Float.toString(qtyOne);
					if (qtyOneStr != null && qtyOneStr.equals("null")) {
						qtyOneStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 4, qtyOneStr, null);
					Float qtyTwo = record.getQtyTwo();
					if(qtyTwo == null) {
						qtyTwo = 0F;
					}
					String qtyTwoStr = Float.toString(qtyTwo);
					if (qtyTwoStr != null && qtyTwoStr.equals("null")) {
						qtyTwoStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 5, qtyTwoStr, null);
					Float qtyThree = record.getQtyThree();
					if(qtyThree == null) {
						qtyThree = 0F;
					}
					String qtyThreeStr = Float.toString(qtyThree);
					if (qtyThreeStr != null && qtyThreeStr.equals("null")) {
						qtyThreeStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 6, qtyThreeStr, null);
					Float qtyFour = record.getQtyFour();
					if(qtyFour == null) {
						qtyFour = 0F;
					}
					String qtyFourStr = Float.toString(qtyFour);
					if (qtyFourStr != null && qtyFourStr.equals("null")) {
						qtyFourStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 7, qtyFourStr, null);
					Float qtyFive = record.getQtyFive();
					if(qtyFive == null) {
						qtyFive = 0F;
					}
					String qtyFiveStr = Float.toString(qtyFive);
					if (qtyFiveStr != null && qtyFiveStr.equals("null")) {
						qtyFiveStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 8, qtyFiveStr, null);
					Float qtySix = record.getQtySix();
					if(qtySix == null) {
						qtySix = 0F;
					}
					String qtySixStr = Float.toString(qtySix);
					if (qtySixStr != null && qtySixStr.equals("null")) {
						qtySixStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 9, qtySixStr, null);
					Float qtySeven = record.getQtySeven();
					if(qtySeven == null) {
						qtySeven = 0F;
					}
					String qtySevenStr = Float.toString(qtySeven);
					if (qtySevenStr != null && qtySevenStr.equals("null")) {
						qtySevenStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 10, qtySevenStr, null);
					Float qtyEight = record.getQtyEight();
					if(qtyEight == null) {
						qtyEight = 0F;
					}
					String qtyEightStr = Float.toString(qtyEight);
					if (qtyEightStr != null && qtyEightStr.equals("null")) {
						qtyEightStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 11, qtyEightStr, null);
					Float qtyNine = record.getQtyNine();
					if(qtyNine == null) {
						qtyNine = 0F;
					}
					String qtyNineStr = Float.toString(qtyNine);
					if (qtyNineStr != null && qtyNineStr.equals("null")) {
						qtyNineStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 12, qtyNineStr, null);
					Float qtyTen = record.getQtyTen();
					if(qtyTen == null) {
						qtyTen = 0F;
					}
					String qtyTenStr = Float.toString(qtyTen);
					if (qtyTenStr != null && qtyTenStr.equals("null")) {
						qtyTenStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 13, qtyTenStr, null);
					Float qtyEleven = record.getQtyEleven();
					if(qtyEleven == null) {
						qtyEleven = 0F;
					}
					String qtyElevenStr = Float.toString(qtyEleven);
					if (qtyElevenStr != null && qtyElevenStr.equals("null")) {
						qtyElevenStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 14, qtyElevenStr, null);
					Float qtyTwelve = record.getQtyTwelve();
					if(qtyTwelve == null) {
						qtyTwelve = 0F;
					}
					String qtyTwelveStr = Float.toString(qtyTwelve);
					if (qtyTwelveStr != null && qtyTwelveStr.equals("null")) {
						qtyTwelveStr = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 15, qtyTwelveStr, null);
					String matCode = record.getMatCode();
					if (matCode != null && matCode.equals("null")) {
						matCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 16, matCode, null);
					String produExpl = record.getProduExpl();
					if (produExpl != null && produExpl.equals("null")) {
						produExpl = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 17, produExpl, null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	public static Workbook exportPackOrderList(List<PackOrderVO> list, HttpServletRequest req,
			HttpServletResponse res) {
		Workbook wb = null;
		try {
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\包材订单执行情况统计表.xlsx";
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			Sheet sheetForAuth = wb.getSheetAt(0);
			if (list != null && list.size()>0) {
				int listSize = list.size();
				for (int i = 0; i < listSize; i++) {
					PackOrderVO packOrderVO = list.get(i);
					//订单日期
					Date orderDate = packOrderVO.getOrderDate();
					String format = "";
					if(orderDate != null) {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						format = simpleDateFormat.format(orderDate);
					}
					if (format != null && format.equals("null")) {
						format = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 0, format, null);
					//包材订单编号
					String packOrderCode = packOrderVO.getPackOrderCode();
					if (packOrderCode != null && packOrderCode.equals("null")) {
						packOrderCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 1, packOrderCode, null);
					//OEM供应商
					String oemSuppName = packOrderVO.getOemSuppName();
					if (oemSuppName != null && oemSuppName.equals("null")) {
						oemSuppName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 2, oemSuppName, null);
					//物料编号
					String mateCode = packOrderVO.getMateCode();
					if (mateCode != null && mateCode.equals("null")) {
						mateCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 3, mateCode, null);
					//物料名称
					String mateName = packOrderVO.getMateName();
					if (mateName != null && mateName.equals("null")) {
						mateName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 4, mateName, null);
					//包材编号
					String packCode = packOrderVO.getPackCode();
					if (packCode != null && packCode.equals("null")) {
						packCode = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 5, packCode, null);
					//包材名称
					String packName = packOrderVO.getPackName();
					if (packName != null && packName.equals("null")) {
						packName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 6, packName, null);
					//采购数量
					Float pruchNum = packOrderVO.getPruchNum();
					if(pruchNum == null) {
						pruchNum = 0F;
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 7, pruchNum.toString(), null);
					//包材供应商
					String baoSuppName = packOrderVO.getBaoSuppName();
					if (baoSuppName != null && baoSuppName.equals("null")) {
						baoSuppName = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 8, baoSuppName, null);
					
					//包材订单数量
					Float packOrderNum = packOrderVO.getPackOrderNum();
					if(packOrderNum == null) {
						packOrderNum = 0F;
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 9, packOrderNum.toString(), null);
					//当前已完成数量
					Float completeNum = packOrderVO.getCompleteNum();
					if(completeNum == null) {
						completeNum = 0F;
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 10, completeNum.toString(), null);
					//剩余未完成数量
					Float residueNum = packOrderVO.getResidueNum();
					if(residueNum == null) {
						residueNum = 0F;
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 11, residueNum.toString(), null);
					//库存数量
					Float invenNum = packOrderVO.getInvenNum();
					if(invenNum == null) {
						invenNum = 0F;
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 12, invenNum.toString(), null);
					//OEM供应商编码
					String oemSapId = packOrderVO.getOemSapId();
					if (oemSapId != null && oemSapId.equals("null")) {
						oemSapId = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 13, oemSapId, null);
					//包材供应商编码
					String baoSapId = packOrderVO.getBaoSapId();
					if (baoSapId != null && baoSapId.equals("null")) {
						baoSapId = "";
					}
					ExcelUtil.setValue(sheetForAuth, i+2, 14, baoSapId, null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	
	
	/*public Object tranObject(Object param){
		if (param instanceof Integer) {
		    int value = ((Integer) param).intValue();
		    prepStatement.setInt(i + 1, value);
		   } else if (param instanceof String) {
		    String s = (String) param;
		    prepStatement.setString(i + 1, s);
		   } else if (param instanceof Double) {
		    double d = ((Double) param).doubleValue();
		    prepStatement.setDouble(i + 1, d);
		   } else if (param instanceof Float) {
		    float f = ((Float) param).floatValue();
		    prepStatement.setFloat(i + 1, f);
		   } else if (param instanceof Long) {
		    long l = ((Long) param).longValue();
		    prepStatement.setLong(i + 1, l);
		   } else if (param instanceof Boolean) {
		    boolean b = ((Boolean) param).booleanValue();
		    prepStatement.setBoolean(i + 1, b);
		   } else if (param instanceof Date) {
		    Date d = (Date) param;
		    prepStatement.setDate(i + 1, (Date) param);
		   } 
	}*/
	
}
