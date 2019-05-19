package com.faujor.service.sync.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.faujor.common.ftp.FtpUtil;
import com.faujor.dao.master.bam.PadPlanMapper;
import com.faujor.dao.master.bam.PdrMapper;
import com.faujor.dao.master.bam.SaleForecastMapper;
import com.faujor.dao.master.sync.SyncMasterMapper;
import com.faujor.dao.sapcenter.bam.SapPdrMapper;
import com.faujor.dao.sapcenter.sync.SyncMapper;
import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;
import com.faujor.entity.bam.psm.Psi;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.document.Directory;
import com.faujor.entity.document.Document;
import com.faujor.entity.sapcenter.bam.OraCxjhEntity;
import com.faujor.service.bam.PadPlanService;
import com.faujor.service.document.DirectoryService;
import com.faujor.service.document.DocumentService;
import com.faujor.service.sapcenter.bam.SapPadPlanService;
import com.faujor.service.sapcenter.bam.SapPdrService;
import com.faujor.service.sync.SyncService;
import com.faujor.utils.CommonUtils;
import com.faujor.utils.DateUtils;
import com.faujor.utils.ExcelUtil;
import com.faujor.utils.UUIDUtil;

/**
 * 中间库数据同步 实现类
 * @author Vincent
 *
 */
@Service
public class SyncServiceImpl implements SyncService{

	@Autowired
	private SyncMapper syncMapper;
	
	@Autowired
	private SyncMasterMapper syncMasterMapper;

	@Autowired
	private DirectoryService directoryService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private PadPlanService padPlanService;
	@Autowired
	private PadPlanMapper padPlanMapper;
	@Autowired
	private SaleForecastMapper saleFcstMapper;
	@Autowired
	private PdrMapper pdrMapper;
	@Autowired
	private SapPdrService sapPdrService;
	@Autowired
	private SapPadPlanService sapPadPlanService;
	
	@Value("${ftp_base_path}")
	private  String ftpBasePath ;
	/**
	 * 获取中间库T_ORA_CXJH表数据
	 * @param map
	 * @return
	 */
	@Override
	public List<OraCxjhEntity> getMatSyncInfoByCondition(Map<String, Object> map) {
		return syncMapper.getMatSyncInfoByCondition(map);
	}

	/**
	 * 中间库T_ORA_CXJH表数据插入至本库中
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void saveCxjhMatList(Map<String, Object> map) throws Exception{
		
		Object year = map.get("year");
		if(year != null && !year.equals("")){
			// 删除原来月份的数据
			Map<String,Object> parmMap = new HashMap<String,Object>();
			parmMap.put("year", year.toString());
			parmMap.put("month", map.get("month").toString());
			syncMasterMapper.delCxjhMatByCondition(parmMap);
		}
		
		List<OraCxjhEntity> list = (List<OraCxjhEntity>)map.get("list");
		
		if(list != null && list.size()>0){

			List<List<?>> tempList = CommonUtils.splitList(list, 1000);
			
			int size = tempList.size();
			for(int i=0;i<size;i++){
				map.put("list", tempList.get(i));
				syncMasterMapper.saveCxjhMatList(map);
			}
		}
		
		// 同步本月数据的情况下，对已有的生产交货单数据进行更新
		if(year != null && !year.equals("")){
			String cMonth = map.get("month").toString();
			String planMonth = year.toString()+"-"+cMonth;
			
			//***************** 添加缺少的物料
			// 获取本月份ID
			Map<String, Object> nextMap=new HashMap<String,Object>();
			nextMap.put("planMonth", planMonth);
			PadPlan plan = padPlanMapper.getPadPlanByMap(nextMap);
			// 获取月份的id
			String currId = "";
			if(plan != null){
				currId = plan.getId();
			}
			
			// 获取激活状态下的销售预测的字段
			Map<String, Object> sMap = new HashMap<String, Object>();
			sMap.put("status", "激活");
			List<SaleForecast> saleList = saleFcstMapper.getSaleFcstByCondition(sMap);
			String saleId = "";
			String columnName = "'0'";
			String nextColumnName = "'0'";
			
			if(saleList.size()>0){
				SaleForecast saleFore = saleList.get(0);
				saleId = saleFore.getId();
				// 预测期间
				String saleYm = saleFore.getFsctYear();
				String[] yearArr = saleYm.split("~");
				String sYm = yearArr[0].trim();
				String eYm = yearArr[1].trim();
				
				String[] sYmArr = sYm.split("-");
				String sYear = sYmArr[0];
				String sMonth = sYmArr[1];
				int sY = Integer.parseInt(sYear);
				int sM = Integer.parseInt(sMonth);
				
				String[] eYmArr = eYm.split("-");
				String eYear = eYmArr[0];
				String eMonth = eYmArr[1];
				int eM = Integer.parseInt(eMonth);
				
				// 同步的年、月
				int mYear = Integer.parseInt(year.toString());
				int mMonth = Integer.parseInt(cMonth);
				
				// 计算与初始的年月相差月份 (对12取模是另一种方式)
				int qty = (mYear-sY)*12+mMonth-sM;
				if(qty>=0 && qty<24){
					if(qty>=12){
						columnName = "sale_Fore"+(qty-11);
						if(qty-10>12){
							// 当前月份为最后一月
							nextColumnName = "sale_Fore1";
						}else{
							nextColumnName = "sale_Fore"+(qty-10);
						}
					}else{
						columnName = "sale_Fore_Qty"+(qty+1);
						if(qty+2>12){
							nextColumnName = "sale_Fore1";
						}else{
							nextColumnName = "sale_Fore_Qty"+(qty+2);
						}
					}
				}else{
					columnName = "'0'";
					nextColumnName = "'0'";
				}
				
				// 获取汇总的字段
				String sumC1 = "";
				String sumC2 = "";
				String sumC3 = "";
				
				if(sMonth.equals("01")){
					sumC1 = "(a.SALE_FORE_QTY1+a.SALE_FORE_QTY2+a.SALE_FORE_QTY3+a.SALE_FORE_QTY4+"+
			"a.SALE_FORE_QTY5+a.SALE_FORE_QTY6+a.SALE_FORE_QTY7+a.SALE_FORE_QTY8+"+
			"a.SALE_FORE_QTY9+a.SALE_FORE_QTY10+a.SALE_FORE_QTY11+a.SALE_FORE_QTY12) ";
					sumC2 = " (a.SALE_FORE1+a.SALE_FORE2+a.SALE_FORE3+a.SALE_FORE4+"+
			"a.SALE_FORE5+a.SALE_FORE6+a.SALE_FORE7+a.SALE_FORE8+"+
			"a.SALE_FORE9+a.SALE_FORE10+a.SALE_FORE11+a.SALE_FORE12) ";
					sumC3 = "0";
				}else{
					for(int i=0;i<=12-sM;i++){
						sumC1 += "a.SALE_FORE_QTY"+(i+1)+"+";
					}
					sumC1 = "("+sumC1.substring(0, sumC1.length()-1)+") ";
					
					for(int i=12;i>12-eM;i--){
						sumC2 += "a.SALE_FORE_QTY"+i+"+";
					}
					for(int i=1;i<=12-eM;i++){
						sumC2 += "a.SALE_FORE"+i+"+";
					}
					sumC2 = "("+sumC2.substring(0, sumC2.length()-1)+") ";
					
					for(int i=12;i>12-eM;i--){
						sumC3 += "a.SALE_FORE"+i+"+";
					}
					sumC3 = "("+sumC3.substring(0, sumC3.length()-1)+") ";
				}
				
				sMap.put("sum1", sumC1);
				sMap.put("sum2", sumC2);
				sMap.put("sum3", sumC3);
				sMap.put("mainId", saleId);
			}
			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("padId", currId);
			aMap.put("columnName", columnName);
			aMap.put("nextColumnName", nextColumnName);
			aMap.put("saleId", saleId);
			aMap.put("year", year.toString());
			aMap.put("month", cMonth);
			
			if(!currId.equals("")){
				// 保存额外数据至当前月计划中
				padPlanMapper.saveExtraMaterialInPadPlan(aMap);
				
				//***************** 更新同步月份的数据
				Map<String, Object> pMap =new HashMap<String,Object>();
				pMap.put("year", year.toString());
				pMap.put("month", cMonth);
				pMap.put("ym", year.toString()+"-"+cMonth);
				padPlanService.updateMonthPadDetail(pMap);
			}
			
			Object isLastDay = map.get("isChecked");
			// 1: 月末最后一天  0: 不是最后一天
			String isLastDayFlag = "0";
			if(isLastDay != null){
				isLastDayFlag = isLastDay.toString();
			}
			// 月底最后一天，更新销售预测为实际销售
			if(DateUtils.isLastDayOfMonth(new Date()) || isLastDayFlag.equals("1")){
				
				if(!currId.equals("")){
					// 修改生产交货计划中的本月销售预测
					padPlanMapper.updatePadPlanSaleForeByCxjh(aMap);
				}
				
				// 修改销售预测中的对应月份的数据，及汇总的重新计算
				if(!columnName.equals("'0'")){
					// 修改对应月份数据
					aMap.put("saleForeColumn", "a."+columnName);
					saleFcstMapper.updateSaleForeByCxjh(aMap);
					
					// 销售预测汇总重新计算
					saleFcstMapper.updateSaleFcstDetailSum(sMap);
				}
			}
			
			//************ 修改后续月份的计算列
			// 当前年月
			String currYm = DateUtils.format(new Date(),DateUtils.DATE_YM_PATTERN);
			
			int mNum = DateUtils.calcYmDec(currYm, planMonth);
			// 同步月份为当前月份时
			if(mNum == 0){
				if(currId.equals("")){
					return;
				}
				// 上个月计划id
				String preId = currId;
				// 获取最大的后续月份
				String maxMonth = padPlanMapper.getMaxPadPlanMonth();
				
				int decMonths = DateUtils.calcYmDec(planMonth, maxMonth);
				for(int i=1;i<=decMonths;i++){
					// 获取下个月的计划
					String nextMonth = DateUtils.dateMonthCalc(planMonth, i);
					nextMap.put("planMonth", nextMonth);
					PadPlan nextPadPlan = padPlanMapper.getPadPlanByMap(nextMap);
					
					// 下个月份 ID
					String nextId = nextPadPlan.getId();
					
					Map<String, Object> dealMap=new HashMap<String,Object>();
					// 未来月起，下个月中的上个月全国库存获取
					dealMap.put("nextId", nextId);
					dealMap.put("preId", preId);
					padPlanMapper.updateNextPlanPreStock(dealMap);
					
					// 修改后续月份的计算列
					padPlanMapper.updateFutureMonthCalc(dealMap);
					
					// 保存上个月计划id
					preId = nextId;
				}
			}
		}
	}
	
	/**
	 * 获取同步的中间库表的数量
	 * @param map
	 * @return
	 */
	public int getCxjhMatCount(Map<String, Object> map){
		return syncMasterMapper.getCxjhMatCount(map);
	}
	
	/**
	 * 根据条件删除中间库表的数据
	 * @param map
	 */
	public void delCxjhMatByCondition(Map<String, Object> map){
		syncMasterMapper.delCxjhMatByCondition(map);
	}

	/**
	 * 同步生产交货计划至FTP
	 */
	@Override
	public void syncPadPlanReport() {
		Workbook wb;
		try {
			// 当前日期为每月一号，需要取上一天
			
			Date calcDate = DateUtils.addDate(new Date(), -1);
			// 获取报表数据
			String currYm = DateUtils.format(calcDate, DateUtils.DATE_YM_PATTERN);

			String currDate = DateUtils.format(calcDate, DateUtils.DATE_PATTERN_TWO);

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("planMonth", currYm);

			// 处理后续月份条件
			// 当前年份
			String currYy = currYm.split("-")[0];
			String currMstr = currYm.split("-")[1];
			// 当前月份
			int currM = Integer.parseInt(currMstr);
			// 去年
			String preY = String.valueOf(Integer.valueOf(currYy) - 1);
			String preYy = preY.substring(preY.length() - 2, preY.length());
			// 明年
			String nextYy = String.valueOf(Integer.valueOf(currYy) + 1);
			String nextY = nextYy.substring(nextYy.length() - 2, nextYy.length());

			// 参数
			paramMap.put("preYear", preY);
			paramMap.put("currYear", currYy);
			paramMap.put("currMonth", currMstr);

			String months = "";
			String pMonths = "";
			int j = 0;
			for (int i = currM + 1; i <= 12; i++) {
				j = j + 1;
				String m = "";
				if (i < 10) {
					m = "0" + i;
				} else {
					m = String.valueOf(i);
				}
				months += "'" + currYy + "-" + m + "' as p" + j + ",";
			}
			for (int i = 1; i <= currM; i++) {
				j = j + 1;
				String m = "";
				if (i < 10) {
					m = "0" + i;
				} else {
					m = String.valueOf(i);
				}
				months += "'" + nextYy + "-" + m + "' as p" + j + ",";
			}
			
			j=0;
			for(int i=currM;i<=12;i++){
				j = j+1;
				String m = "";
				if(i<10){
					m = "0"+i;
				}else{
					m = String.valueOf(i);
				}
				pMonths += "'"+currYy+"-"+m+"' as p"+j+",";
			}
			for(int i=1;i<currM;i++){
				j = j+1;
				String m = "";
				if(i<10){
					m = "0"+i;
				}else{
					m = String.valueOf(i);
				}
				pMonths += "'"+nextYy+"-"+m+"' as p"+j+",";
			}
			
			months = months.substring(0, months.length() - 1);
			pMonths = pMonths.substring(0, pMonths.length()-1);
			paramMap.put("months", months);
			paramMap.put("pMonths", pMonths);

			// String basepath =
			// request.getSession().getServletContext().getRealPath("/").toString();

			// InputStream stream =
			// getClass().getClassLoader().getResourceAsStream("templates\\excelTemp\\psiTemp.xls");
			//// File targetFile = new File("xxx.pdf");
			//// FileUtils.copyInputStreamToFile(stream, targetFile);

			// wb = ExcelUtil.getWorkBook(stream,".xls");

			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\psiTemp3.xls";
			File file = new File(xlsTemplatePath);
			if (!file.exists()) {
				xlsTemplatePath = "C:\\TOP_SRM\\excelTemp\\psiTemp3.xls";
			}
			wb = ExcelUtil.getWorkBook(xlsTemplatePath);

			String fileName = "进销存报表" + currDate;
			Sheet sheet = wb.getSheetAt(0);
			
			// sheet 名称改变
			wb.setSheetName(0, currDate);
			
			// 设置标题
			ExcelUtil.setValue(sheet, 0, 0, currYy+"年家用生产进销存计划表-"+currDate, null);
			
			//***** 设置列头 *****
			// 设置去年列头
			for(int i=1;i<=12;i++){
				ExcelUtil.setValue(sheet, 1, 5+i, preYy+"年"+i+"月实际销售数量", null);
			}
			
			// 获取当前年份1月至当前月份之间的标题的样式
			//获取第2行第19列的样式
			CellStyle cellStyle1 = ExcelUtil.getCellStyle(sheet, 1, 18);
			//获取第3行第19列的样式
			CellStyle cellStyle2 = ExcelUtil.getCellStyle(sheet, 2, 18);
			//获取第4行第19列的样式
			CellStyle cellStyle3 = ExcelUtil.getCellStyle(sheet, 3, 18);
			
			// 上月库存样式样式
			//获取第2行第20列的样式
			CellStyle currStyle1 = ExcelUtil.getCellStyle(sheet, 1, 19);
			//获取第3行第20列的样式
			CellStyle currStyle2 = ExcelUtil.getCellStyle(sheet, 2, 19);
			//获取第4行第20列的样式
			CellStyle currStyle3 = ExcelUtil.getCellStyle(sheet, 3, 19);
			
			// 本月生产交货计划
			//获取第2行第21列的样式
			CellStyle currStyle11 = ExcelUtil.getCellStyle(sheet, 1, 20);
			//获取第3行第21列的样式
			CellStyle currStyle21 = ExcelUtil.getCellStyle(sheet, 2, 20);
			//获取第4行第21列的样式
			CellStyle currStyle31 = ExcelUtil.getCellStyle(sheet, 3, 20);
			
			// 第25列样式
			CellStyle currActStyle1 = ExcelUtil.getCellStyle(sheet, 1, 24);
			CellStyle currActStyle2 = ExcelUtil.getCellStyle(sheet, 2, 24);
			CellStyle currActStyle3 = ExcelUtil.getCellStyle(sheet, 3, 24);
			
			// 获取后续月份的样式
			CellStyle nextPdcStyle1 = ExcelUtil.getCellStyle(sheet, 1, 30);
			CellStyle nextPdcStyle2 = ExcelUtil.getCellStyle(sheet, 2, 30);
			CellStyle nextPdcStyle3 = ExcelUtil.getCellStyle(sheet, 3, 30);

			CellStyle nextSaleStyle1 = ExcelUtil.getCellStyle(sheet, 1, 31);
			CellStyle nextSaleStyle2 = ExcelUtil.getCellStyle(sheet, 2, 31);
			CellStyle nextSaleStyle3 = ExcelUtil.getCellStyle(sheet, 3, 31);

			CellStyle preStockStyle1 = ExcelUtil.getCellStyle(sheet, 1, 32);
			CellStyle preStockStyle2 = ExcelUtil.getCellStyle(sheet, 2, 32);
			CellStyle preStockStyle3 = ExcelUtil.getCellStyle(sheet, 3, 32);

			CellStyle zzStyle1 = ExcelUtil.getCellStyle(sheet, 1, 33);
			CellStyle zzStyle2 = ExcelUtil.getCellStyle(sheet, 2, 33);
			CellStyle zzStyle3 = ExcelUtil.getCellStyle(sheet, 3, 33);
			
			// 获取列宽
			int columnWidth1 = sheet.getColumnWidth(18); // 11.7
			int columnWidth2 = sheet.getColumnWidth(19); // 17.5
			int columnWidth3 = sheet.getColumnWidth(32); // 9.2
			int columnWidth4 = sheet.getColumnWidth(33); // 7.5
			
			
			String currY = currYy.substring(currYy.length()-2,currYy.length());
			
			// 設置今年本月以前的銷售
			int startCol1 = 17;
			for(int i=1;i<=currM-1;i++){
				ExcelUtil.setValue(sheet, 1, startCol1+i, currY+"年"+i+"月实际销售数量", cellStyle1);
				ExcelUtil.setValue(sheet, 2, startCol1+i, null, cellStyle2);
				ExcelUtil.setValue(sheet, 3, startCol1+i, null, cellStyle3);
				// 设置列宽
				sheet.setColumnWidth(startCol1+i, columnWidth1);
			}

			startCol1 = startCol1+currM-1;
			
			int xx=1;
			// 上个月月底库存等
			if(currM == 1){
				ExcelUtil.setValue(sheet, 1, startCol1+xx, preYy+"年12月底全国库存", currStyle1);
				ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
				ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			}else{
				ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+(currM-1)+"月底全国库存", currStyle1);
				ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
				ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			}
			// 设置列宽
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			// 设置当前月份数据
			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"月生产/交货计划", currStyle11);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle21);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle31);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, "实际生产", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "预计生产", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, "达成率", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth3);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"销售预测", currActStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currActStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currActStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"月实际销售数量", currActStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currActStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currActStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth1);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "达成率", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth3);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "本月预计销售", currActStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currActStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currActStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth3);
			xx=xx+1;

			ExcelUtil.setValue(sheet, 1, startCol1+xx, currY+"年"+currM+"月底全国库存", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth2);
			xx=xx+1;
			
			ExcelUtil.setValue(sheet, 1, startCol1+xx, "周转天数", currStyle1);
			ExcelUtil.setValue(sheet, 2, startCol1+xx, null, currStyle2);
			ExcelUtil.setValue(sheet, 3, startCol1+xx, null, currStyle3);
			sheet.setColumnWidth(startCol1+xx, columnWidth4);
			
			// 后续列起始
			int nextMCol = startCol1+xx;
			int startCol2 = startCol1+xx;
			
			for(int i=1;i<=12-currM;i++){
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+1, currY+"年"+(currM+i)+"月生产/交货计划", nextPdcStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+1, null, nextPdcStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+1, null, nextPdcStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+1, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+2, currY+"年"+(currM+i)+"月销售预测", nextSaleStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+2, null, nextSaleStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+2, null, nextSaleStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+2, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+3, currY+"年"+(currM+i)+"月底全国库存", preStockStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+3, null, preStockStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+3, null, preStockStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+3, columnWidth3);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+4, "周转天数", zzStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+4, null, zzStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+4, null, zzStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+4, columnWidth4);
			}
			
			startCol2 = startCol2+(12-currM)*4;
			
			for(int i=1;i<=currM;i++){
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+1, nextY+"年"+i+"月生产/交货计划", nextPdcStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+1, null, nextPdcStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+1, null, nextPdcStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+1, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+2, nextY+"年"+i+"月销售预测", nextSaleStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+2, null, nextSaleStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+2, null, nextSaleStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+2, columnWidth1);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+3, nextY+"年"+i+"月底全国库存", preStockStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+3, null, preStockStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+3, null, preStockStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+3, columnWidth3);
				
				ExcelUtil.setValue(sheet, 1, startCol2+(i-1)*4+4, "周转天数", zzStyle1);
				ExcelUtil.setValue(sheet, 2, startCol2+(i-1)*4+4, null, zzStyle2);
				ExcelUtil.setValue(sheet, 3, startCol2+(i-1)*4+4, null, zzStyle3);
				sheet.setColumnWidth(startCol2+(i-1)*4+4, columnWidth4);
			}
			//------------- 暂时添加物料编码------------------
			/*ExcelUtil.setValue(sheet, 1, startCol2+(currM-1)*4+5, "物料编码", zzStyle1);
			ExcelUtil.setValue(sheet, 2, startCol2+(currM-1)*4+5, null, zzStyle2);
			ExcelUtil.setValue(sheet, 3, startCol2+(currM-1)*4+5, null, zzStyle3);
			sheet.setColumnWidth(startCol2+(currM-1)*4+5, columnWidth4);*/
			
			// 统计行字体处理
			Font sumFont = wb.createFont();
			sumFont.setFontName("微软雅黑");    
			sumFont.setFontHeightInPoints((short) 10);//设置字体大小
			sumFont.setBold(true);
			
			// 获取导出数据
			List<Psi> psiList = padPlanService.getPsiInfoByMap(paramMap);
			int listSize = psiList.size();
			
			// 获取导出大品项统计
			List<Psi> sumList = padPlanService.getPsiSumByMap(paramMap);
			int sumListSize = sumList.size();
			
			//******* 数据填充  ******
			
			// 插入行数,listSize:数据行数（模板中自带一行），sumListSize：小计行数
			ExcelUtil.insertRow(sheet, 2, listSize-1+sumListSize);
			int startRow = 2;
			
			Class<? extends Psi> psiClass = Psi.class;
			// 调用get方法
			Method method;
			
			// 汇总数据存储
			HashMap<Integer,Object> sumMap = new HashMap<Integer,Object>();
			// 周转列保存
			List<Integer> turnColList = new ArrayList<Integer>();
			DecimalFormat df = (DecimalFormat)NumberFormat.getPercentInstance();
			
			// 大品项统计起始
			int sumIndex = 0;
			// 汇总行所在行下标，初始
			int sumC = sumList.get(0).getSumnum();
			
			// 设置大品项统计行的样式
			// 系列
			CellStyle sumCellStyle0 = wb.createCellStyle();
			CellStyle sumStyle0 = ExcelUtil.getCellStyle(sheet, 2, 2);
			sumCellStyle0.cloneStyleFrom(sumStyle0);
			sumCellStyle0.setFont(sumFont);
			// 简称
			CellStyle sumCellStyle1 = wb.createCellStyle();
			CellStyle sumStyle1 = ExcelUtil.getCellStyle(sheet, 2, 4);
			sumCellStyle1.cloneStyleFrom(sumStyle1);
			sumCellStyle1.setFont(sumFont);
			// 三个月平均
			CellStyle sumCellStyle2 = wb.createCellStyle();
			CellStyle sumStyle2 = ExcelUtil.getCellStyle(sheet, 2, 5);
			sumCellStyle2.cloneStyleFrom(sumStyle2);
			sumCellStyle2.setFont(sumFont);
			
			// 设置统计行样式
			Map<Integer,CellStyle> sumStyleMap = new HashMap<Integer,CellStyle>();
			for(int xy=6;xy<=nextMCol+11*4+4;xy++){
				CellStyle sumCellStyle = wb.createCellStyle();
				CellStyle sumStyle = ExcelUtil.getCellStyle(sheet, 2, xy);
				sumCellStyle.cloneStyleFrom(sumStyle);
				sumCellStyle.setFont(sumFont);
				
				sumStyleMap.put(xy, sumCellStyle);
			}
			
			for(int i=0;i<listSize+sumListSize;i++){
				
				//*** 大品项汇总小计行添加  ***
				if(sumC == i){
					// 需要添加大品项汇总行
					Psi sumItem = sumList.get(sumIndex);
					
					ExcelUtil.setValue(sheet, startRow+i, 2, sumItem.getProdSeriesCode(), sumCellStyle0);
					ExcelUtil.setValue(sheet, startRow+i, 3, sumItem.getBigItemExpl(), sumCellStyle0);
					ExcelUtil.setValue(sheet, startRow+i, 4, "小计", sumCellStyle1);

					ExcelUtil.setValue(sheet, startRow+i, 5, sumItem.getThreeAvgSales(), sumCellStyle2);
					
					// 大品项汇总行处理
					SumItemDeal(sheet,startRow,sumItem,i,startCol1,nextMCol,currM);
					
					// 设置统计行样式
					for(int xy=5;xy<=nextMCol+11*4+4;xy++){
						ExcelUtil.setCellStyle(sheet, startRow+i, xy, sumStyleMap.get(xy));
					}
					
					// 处理下次统计的行信息
					if(sumIndex<sumListSize-1){
						// 下一个汇总行
						sumIndex = sumIndex+1;
						// 最后行下标计算完成，不需要再计算下个汇总行下标
						// 下一个大品项统计行下标
						int itemSumCount = sumList.get(sumIndex).getSumnum();
						sumC += itemSumCount;
						sumC += 1;
					}
					
					continue;
				}
				
				Psi item = psiList.get(i-sumIndex);// 如有汇总行，则 sumIndex 已加一
				ExcelUtil.setValue(sheet, startRow+i, 0, item.getRank(), null);
				ExcelUtil.setValue(sheet, startRow+i, 1, item.getProduExpl(), null);//产能划分
				ExcelUtil.setValue(sheet, startRow+i, 2, item.getProdSeriesCode(), null);
				ExcelUtil.setValue(sheet, startRow+i, 3, item.getBigItemExpl(), null);
				ExcelUtil.setValue(sheet, startRow+i, 4, item.getMatShort(), null);
				ExcelUtil.setValue(sheet, startRow+i, 5, item.getThreeAvgSales(), null);
				
				int ssIndex = 5;
				
				// 计算列总计
				int key = 5;
				float val = item.getThreeAvgSales();
				sumMap = summaryCalc(sumMap,key,val);
				
				try {
					// 去年一整年的实际销售
					for(int z=1;z<=12;z++){
							method = psiClass.getMethod("getPreY" + z +"mSales");
							// 得到值
							Object invoke = method.invoke(item);
							// 计算统计值
							key = ssIndex+z;
							val = Float.parseFloat(invoke.toString());
							sumMap = summaryCalc(sumMap,key,val);
							
							ExcelUtil.setValue(sheet, startRow+i, ssIndex+z, invoke, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 今年本月之前月份的实际销售
				int sIndex = ssIndex+12;
				try {
					for(int z=1;z<=currM-1;z++){
							method = psiClass.getMethod("getCurrY" + z +"mSales");
							// 得到值
							Object invoke = method.invoke(item);
							
							// 计算统计值
							key = (sIndex+z);
							val = Float.parseFloat(invoke.toString());
							sumMap = summaryCalc(sumMap,key,val);
							
							ExcelUtil.setValue(sheet, startRow+i, sIndex+z, invoke, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 上个月的全国库存
				ExcelUtil.setValue(sheet, startRow+i, startCol1+1, item.getNationStock2(), null);
				// 计算统计值
				key = startCol1+1;
				val = item.getNationStock2();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 生产交货计划数量
				ExcelUtil.setValue(sheet, startRow+i, startCol1+2, item.getPadPlanQty(), null);
				// 计算统计值
				key = startCol1+2;
				val = item.getPadPlanQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 实际生产
				ExcelUtil.setValue(sheet, startRow+i, startCol1+3, item.getPadActQty(), null);
				// 计算统计值
				key = startCol1+3;
				val = item.getPadActQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 预计生产
				ExcelUtil.setValue(sheet, startRow+i, startCol1+4, item.getEstDeliQty(), null);
				// 计算统计值
				key = startCol1+4;
				val = item.getEstDeliQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 达成率
				if(item.getPadPlanQty() != 0){
					float calcNum = (item.getPadActQty()/item.getPadPlanQty());
					String per = df.format(calcNum);
					
					ExcelUtil.setValue(sheet, startRow+i, startCol1+5, per, null);
				}else{
					ExcelUtil.setValue(sheet, startRow+i, startCol1+5, "-", null);
				}
				
				// 本月销售预测
				ExcelUtil.setValue(sheet, startRow+i, startCol1+6, item.getSaleForeQty(), null);
				// 计算统计值
				key = startCol1+6;
				val = item.getSaleForeQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 实际销售
				ExcelUtil.setValue(sheet, startRow+i, startCol1+7, item.getSaleForeActQty(), null);
				// 计算统计值
				key = startCol1+7;
				val = item.getSaleForeActQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 达成率
				if(item.getSaleForeQty() != 0){
					float calcNum = (item.getSaleForeActQty()/item.getSaleForeQty());
					String per = df.format(calcNum);
					
					ExcelUtil.setValue(sheet, startRow+i, startCol1+8, per, null);
				}else{
					ExcelUtil.setValue(sheet, startRow+i, startCol1+8, "-", null);
				}
				// 本月预计销售
				ExcelUtil.setValue(sheet, startRow+i, startCol1+9, item.getEstSaleQty(), null);
				// 计算统计值
				key = startCol1+9;
				val = item.getEstSaleQty();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 全国库存
				ExcelUtil.setValue(sheet, startRow+i, startCol1+10, item.getNationStock1(), null);
				// 计算统计值
				key = startCol1+10;
				val = item.getNationStock1();
				sumMap = summaryCalc(sumMap,key,val);
				
				// 周转天数
				ExcelUtil.setValue(sheet, startRow+i, startCol1+11, item.getTurnOverDays(), null);
				// 计算统计值
				key = startCol1+11;
				// 记录周转天数列
				turnColList.add(key);
				
				//----------------------- 后续月份数据填充 ------------------------------
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1, item.getNext1Plan(), null);
				// 计算统计值
				key = nextMCol+1;
				val = item.getNext1Plan();
				sumMap = summaryCalc(sumMap,key,val);
				
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2, item.getNext1SalesF(), null);
				// 计算统计值
				key = nextMCol+2;
				val = item.getNext1SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				float stock1 = item.getNationStock1()+(item.getNext1Plan()==null?0:item.getNext1Plan())
						-(item.getNext1SalesF()==null?0:item.getNext1SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3, stock1, null);
				// 计算统计值
				key = nextMCol+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				
				// 周转天数
				float tod1 = 0;
				float nextSalesF = item.getNext1SalesF()==null?0:item.getNext1SalesF();
				if(nextSalesF==0){
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+4;
				// 记录周转天数列
				turnColList.add(key);
				
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+1, item.getNext2Plan(), null);
				// 计算统计值
				key = nextMCol+1*4+1;
				val = item.getNext2Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+2, item.getNext2SalesF(), null);
				// 计算统计值
				key = nextMCol+1*4+2;
				val = item.getNext2SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext2Plan()==null?0:item.getNext2Plan())
						-(item.getNext2SalesF()==null?0:item.getNext2SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+1*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext2SalesF()==null?0:item.getNext2SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+1*4+4;
				// 记录周转天数列
				turnColList.add(key);
				
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+1, item.getNext3Plan(), null);
				// 计算统计值
				key = nextMCol+2*4+1;
				val = item.getNext3Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+2, item.getNext3SalesF(), null);
				// 计算统计值
				key = nextMCol+2*4+2;
				val = item.getNext3SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext3Plan()==null?0:item.getNext3Plan())
						-(item.getNext3SalesF()==null?0:item.getNext3SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+2*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext3SalesF()==null?0:item.getNext3SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+2*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+1, item.getNext4Plan(), null);
				// 计算统计值
				key = nextMCol+3*4+1;
				val = item.getNext4Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+2, item.getNext4SalesF(), null);
				// 计算统计值
				key = nextMCol+3*4+2;
				val = item.getNext4SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext4Plan()==null?0:item.getNext4Plan())
						-(item.getNext4SalesF()==null?0:item.getNext4SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+3*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext4SalesF()==null?0:item.getNext4SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+3*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+1, item.getNext5Plan(), null);
				// 计算统计值
				key = nextMCol+4*4+1;
				val = item.getNext5Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+2, item.getNext5SalesF(), null);
				// 计算统计值
				key = nextMCol+4*4+2;
				val = item.getNext5SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext5Plan()==null?0:item.getNext5Plan())
						-(item.getNext5SalesF()==null?0:item.getNext5SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+4*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext5SalesF()==null?0:item.getNext5SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+4*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+1, item.getNext6Plan(), null);
				// 计算统计值
				key = nextMCol+5*4+1;
				val = item.getNext6Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+2, item.getNext6SalesF(), null);
				// 计算统计值
				key = nextMCol+5*4+2;
				val = item.getNext6SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext6Plan()==null?0:item.getNext6Plan())
						-(item.getNext6SalesF()==null?0:item.getNext6SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+5*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext6SalesF()==null?0:item.getNext6SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+5*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+1, item.getNext7Plan(), null);
				// 计算统计值
				key = nextMCol+6*4+1;
				val = item.getNext7Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+2, item.getNext7SalesF(), null);
				// 计算统计值
				key = nextMCol+6*4+2;
				val = item.getNext7SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext7Plan()==null?0:item.getNext7Plan())
						-(item.getNext7SalesF()==null?0:item.getNext7SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+6*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext7SalesF()==null?0:item.getNext7SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+6*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+1, item.getNext8Plan(), null);
				// 计算统计值
				key = nextMCol+7*4+1;
				val = item.getNext8Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+2, item.getNext8SalesF(), null);
				// 计算统计值
				key = nextMCol+7*4+2;
				val = item.getNext8SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext8Plan()==null?0:item.getNext8Plan())
						-(item.getNext8SalesF()==null?0:item.getNext8SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+7*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext8SalesF()==null?0:item.getNext8SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+7*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+1, item.getNext9Plan(), null);
				// 计算统计值
				key = nextMCol+8*4+1;
				val = item.getNext9Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+2, item.getNext9SalesF(), null);
				// 计算统计值
				key = nextMCol+8*4+2;
				val = item.getNext9SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext9Plan()==null?0:item.getNext9Plan())
						-(item.getNext9SalesF()==null?0:item.getNext9SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+8*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext9SalesF()==null?0:item.getNext9SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+8*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+1, item.getNext10Plan(), null);
				// 计算统计值
				key = nextMCol+9*4+1;
				val = item.getNext10Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+2, item.getNext10SalesF(), null);
				// 计算统计值
				key = nextMCol+9*4+2;
				val = item.getNext10SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext10Plan()==null?0:item.getNext10Plan())
						-(item.getNext10SalesF()==null?0:item.getNext10SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+9*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext10SalesF()==null?0:item.getNext10SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+9*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+1, item.getNext11Plan(), null);
				// 计算统计值
				key = nextMCol+10*4+1;
				val = item.getNext11Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+2, item.getNext11SalesF(), null);
				// 计算统计值
				key = nextMCol+10*4+2;
				val = item.getNext11SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext11Plan()==null?0:item.getNext11Plan())
						-(item.getNext11SalesF()==null?0:item.getNext11SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+10*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext11SalesF()==null?0:item.getNext11SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+10*4+4;
				// 记录周转天数列
				turnColList.add(key);

				ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+1, item.getNext12Plan(), null);
				// 计算统计值
				key = nextMCol+11*4+1;
				val = item.getNext12Plan();
				sumMap = summaryCalc(sumMap,key,val);
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+2, item.getNext12SalesF(), null);
				// 计算统计值
				key = nextMCol+11*4+2;
				val = item.getNext12SalesF();
				sumMap = summaryCalc(sumMap,key,val);
				// 全国库存
				stock1 = stock1+(item.getNext12Plan()==null?0:item.getNext12Plan())
						-(item.getNext12SalesF()==null?0:item.getNext12SalesF());
				ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+3, stock1, null);
				// 计算统计值
				key = nextMCol+11*4+3;
				val = stock1;
				sumMap = summaryCalc(sumMap,key,val);
				// 周转天数
				nextSalesF = item.getNext12SalesF()==null?0:item.getNext12SalesF();
				if(nextSalesF==0){
					tod1 = 0;
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, "-", null);
				}else{
					tod1 = Math.round((stock1/nextSalesF)*30);
					ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, tod1, null);
				}
				// 计算统计值
				key = nextMCol+11*4+4;
				// 记录周转天数列
				turnColList.add(key);

				// ---------------------物料编码赋值-----------------------
				//ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+5, item.getMatCode(), null);
			}
			
			// 周转天数处理
			for(int i=0;i<turnColList.size();i++){
				int colIx = turnColList.get(i);
				float stock = Float.parseFloat(sumMap.get(colIx-1).toString());
				float sale = Float.parseFloat(sumMap.get(colIx-2).toString());
				if(sale != 0){
					sumMap.put(colIx, Math.round((stock/sale)*30));
				}else{
					sumMap.put(colIx, 0);
				}
			}
			
			// 统计行赋值
			Iterator iter = sumMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int key = Integer.parseInt(entry.getKey().toString());
				Object val = entry.getValue();
				
				ExcelUtil.setValue(sheet, startRow+listSize+sumListSize, key, val, null);
			}
			/*
			 * // 添加合计的公式 int cellNumSum = nextMCol+11*4+4; // 行 int rowSum =
			 * listSize+2; // 设置计算的第一列公式 ExcelUtil.setFormula(sheet, rowSum, 2,
			 * "sum(C3:C"+rowSum+")", null); for(int i=3;i<=cellNumSum;i++){
			 * if(i == startCol1+4 ||i == startCol1+7){ continue; }
			 * ExcelUtil.setFormula(sheet, rowSum, i,
			 * ExcelUtil.getCellFormula(sheet, rowSum, 2), null); }
			 */

			// 上传至ftp
			uploadExcelFile(wb, fileName + ".xls");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 大品项汇总行处理
	 * @param sheet
	 * @param startRow
	 * @param item
	 * @param i
	 * @param startCol1
	 * @param nextMCol
	 * @param currM
	 */
	private void SumItemDeal(Sheet sheet,int startRow,Psi item,int i,int startCol1,int nextMCol,int currM){
		int ssIndex = 5;

		Class<? extends Psi> psiClass = Psi.class;
		// 调用get方法
		Method method;
		DecimalFormat df = (DecimalFormat)NumberFormat.getPercentInstance();
		try {
			// 去年一整年的实际销售
			for(int z=1;z<=12;z++){
					method = psiClass.getMethod("getPreY" + z +"mSales");
					// 得到值
					Object invoke = method.invoke(item);
					
					ExcelUtil.setValue(sheet, startRow+i, ssIndex+z, invoke, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 今年本月之前月份的实际销售
		int sIndex = ssIndex+12;
		try {
			for(int z=1;z<=currM-1;z++){
					method = psiClass.getMethod("getCurrY" + z +"mSales");
					// 得到值
					Object invoke = method.invoke(item);
					
					ExcelUtil.setValue(sheet, startRow+i, sIndex+z, invoke, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 上个月的全国库存
		ExcelUtil.setValue(sheet, startRow+i, startCol1+1, item.getNationStock2(), null);
		
		// 生产交货计划数量
		ExcelUtil.setValue(sheet, startRow+i, startCol1+2, item.getPadPlanQty(), null);
		
		// 实际生产
		ExcelUtil.setValue(sheet, startRow+i, startCol1+3, item.getPadActQty(), null);
		
		// 预计生产
		ExcelUtil.setValue(sheet, startRow+i, startCol1+4, item.getEstDeliQty(), null);
		
		// 达成率
		if(item.getPadPlanQty() != 0){
			float calcNum = (item.getPadActQty()/item.getPadPlanQty());
			String per = df.format(calcNum);
			
			ExcelUtil.setValue(sheet, startRow+i, startCol1+5, per, null);
		}else{
			ExcelUtil.setValue(sheet, startRow+i, startCol1+5, "-", null);
		}
		
		// 本月销售预测
		ExcelUtil.setValue(sheet, startRow+i, startCol1+6, item.getSaleForeQty(), null);
		
		// 实际销售
		ExcelUtil.setValue(sheet, startRow+i, startCol1+7, item.getSaleForeActQty(), null);
		
		// 达成率
		if(item.getSaleForeQty() != 0){
			float calcNum = (item.getSaleForeActQty()/item.getSaleForeQty());
			String per = df.format(calcNum);
			
			ExcelUtil.setValue(sheet, startRow+i, startCol1+8, per, null);
		}else{
			ExcelUtil.setValue(sheet, startRow+i, startCol1+8, "-", null);
		}
		// 本月预计销售
		ExcelUtil.setValue(sheet, startRow+i, startCol1+9, item.getEstSaleQty(), null);
		
		// 全国库存
		ExcelUtil.setValue(sheet, startRow+i, startCol1+10, item.getNationStock1(), null);
		
		// 周转天数
		ExcelUtil.setValue(sheet, startRow+i, startCol1+11, item.getTurnOverDays(), null);
		
		//----------------------- 后续月份数据填充 ------------------------------
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1, item.getNext1Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2, item.getNext1SalesF(), null);
		// 全国库存
		float stock1 = item.getNationStock1()+(item.getNext1Plan()==null?0:item.getNext1Plan())
				-(item.getNext1SalesF()==null?0:item.getNext1SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3, stock1, null);
		// 周转天数
		float tod1 = 0;
		float nextSalesF = item.getNext1SalesF()==null?0:item.getNext1SalesF();
		if(nextSalesF==0){
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4, tod1, null);
		}
		
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+1, item.getNext2Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+2, item.getNext2SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext2Plan()==null?0:item.getNext2Plan())
				-(item.getNext2SalesF()==null?0:item.getNext2SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext2SalesF()==null?0:item.getNext2SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+1*4+4, tod1, null);
		}
		
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+1, item.getNext3Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+2, item.getNext3SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext3Plan()==null?0:item.getNext3Plan())
				-(item.getNext3SalesF()==null?0:item.getNext3SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext3SalesF()==null?0:item.getNext3SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+2*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+1, item.getNext4Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+2, item.getNext4SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext4Plan()==null?0:item.getNext4Plan())
				-(item.getNext4SalesF()==null?0:item.getNext4SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext4SalesF()==null?0:item.getNext4SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+3*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+1, item.getNext5Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+2, item.getNext5SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext5Plan()==null?0:item.getNext5Plan())
				-(item.getNext5SalesF()==null?0:item.getNext5SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext5SalesF()==null?0:item.getNext5SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+4*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+1, item.getNext6Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+2, item.getNext6SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext6Plan()==null?0:item.getNext6Plan())
				-(item.getNext6SalesF()==null?0:item.getNext6SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext6SalesF()==null?0:item.getNext6SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+5*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+1, item.getNext7Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+2, item.getNext7SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext7Plan()==null?0:item.getNext7Plan())
				-(item.getNext7SalesF()==null?0:item.getNext7SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext7SalesF()==null?0:item.getNext7SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+6*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+1, item.getNext8Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+2, item.getNext8SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext8Plan()==null?0:item.getNext8Plan())
				-(item.getNext8SalesF()==null?0:item.getNext8SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext8SalesF()==null?0:item.getNext8SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+7*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+1, item.getNext9Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+2, item.getNext9SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext9Plan()==null?0:item.getNext9Plan())
				-(item.getNext9SalesF()==null?0:item.getNext9SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext9SalesF()==null?0:item.getNext9SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+8*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+1, item.getNext10Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+2, item.getNext10SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext10Plan()==null?0:item.getNext10Plan())
				-(item.getNext10SalesF()==null?0:item.getNext10SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext10SalesF()==null?0:item.getNext10SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+9*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+1, item.getNext11Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+2, item.getNext11SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext11Plan()==null?0:item.getNext11Plan())
				-(item.getNext11SalesF()==null?0:item.getNext11SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext11SalesF()==null?0:item.getNext11SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+10*4+4, tod1, null);
		}

		ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+1, item.getNext12Plan(), null);
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+2, item.getNext12SalesF(), null);
		// 全国库存
		stock1 = stock1+(item.getNext12Plan()==null?0:item.getNext12Plan())
				-(item.getNext12SalesF()==null?0:item.getNext12SalesF());
		ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+3, stock1, null);
		// 周转天数
		nextSalesF = item.getNext12SalesF()==null?0:item.getNext12SalesF();
		if(nextSalesF==0){
			tod1 = 0;
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, "-", null);
		}else{
			tod1 = Math.round((stock1/nextSalesF)*30);
			ExcelUtil.setValue(sheet, startRow+i, nextMCol+11*4+4, tod1, null);
		}
	}
		
	/**
	 * 上传导出的excel
	 * @param wb
	 * @param fileName
	 * @return
	 */
	private String uploadExcelFile(Workbook wb,String fileName){
        // 保存至进销存报表目录中
		String direCode = "JXCBB";
        Directory dire = directoryService.getDireByCode(direCode);
		if(dire==null){
			return "请选择正确的文件路径编码！"; 
		}

		boolean uploadFlag=true;
		try {
			// 获取workbook的inputstream
	        InputStream is = ExcelUtil.getInputStreamData(wb);
			String newName=UUIDUtil.getUUID()+fileName;
			String realPath=ftpBasePath.concat(dire.getDireFcode());
			
			uploadFlag = FtpUtil.uploadFile(realPath, newName, is);

			List<Document> docs=new ArrayList<Document>();
			if(uploadFlag){
				Document doc = new Document();
				doc.setId(UUIDUtil.getUUID());
				doc.setDireCode(direCode);
				doc.setFileUrl(realPath);
				doc.setFileName(newName);
				doc.setRealName(fileName);
				
				String currYm = DateUtils.format(DateUtils.addDate(new Date(), -1), DateUtils.DATE_YM_PATTERN);
				doc.setLinkNo(currYm);
				//doc.setLinkId(linkId);
				//doc.setDocCate(docCate);
				doc.setCreateUser("admin_auto");
				doc.setCreateTime(new Date());
				
				//doc.setDocSize(fc.size()/1024+"Kb");
				String[] split = fileName.split(".");
				if(split.length>0){
					doc.setDocType(split[split.length-1]);
				}
				docs.add(doc);
				
				documentService.saveDocs(docs);
			}
		}catch (Exception e) {
			return e.getMessage();
		}
		return "";
	}
	
	private HashMap<Integer,Object> summaryCalc(HashMap<Integer,Object> sumMap,int key,float val){
		
		if(sumMap.containsKey(key)){
			float sum = Float.parseFloat(sumMap.get(key).toString());
			sum+=val;
			sumMap.put(key, sum);
		}else{
			sumMap.put(key, val);
		}
		return sumMap;
	}

	/**
	 * 同步产能上报信息
	 */
	@Override
	@Transactional
	public void syncPdrInfo() {
		// 提交后未成功同步至中间库的产能上报 （状态：已提交，同步状态：0）
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("syncFlag","0");
		map.put("status","已提交");
		List<Pdr> pdrList = pdrMapper.getPdrByMap(map);
		int size = pdrList.size();
		if(size>0){
			for(int i=0;i<size;i++){
				Pdr pdr = pdrList.get(i);
				String id = pdr.getId();
				List<PdrDetail> detailList = pdrMapper.getPdrDetailListByMainId(id);
				map.put("mainId", id);
				List<PdrItem> itemList = pdrMapper.getPdrItemListByMainId(map);
				
				try {
					sapPdrService.saveSapPdrInfo(pdr, detailList, itemList);
					// 修改同步状态为1
					pdrMapper.updatePdrSyncFlag("1", id);
				} catch (Exception e) {
				}
			}
		}

		// 退回后未成功删除中间库数据的产能上报 （状态：已保存，同步状态：1）
		map.put("syncFlag","1");
		map.put("status","已保存");
		pdrList = pdrMapper.getPdrByMap(map);
		size = pdrList.size();
		if(size>0){
			for(int i=0;i<size;i++){
				Pdr pdr = pdrList.get(i);
				String id = pdr.getId();
				try {
					sapPdrService.delSapPdrInfo(id);
					// 修改同步状态为0
					pdrMapper.updatePdrSyncFlag("0", id);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 同步生产交货计划至sap中间库中
	 */
	@Override
	public void syncPadPlanInfo() {
		
		// 获取需要同步的计划
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("syncFlag", "0");
		map.put("status", "已提交");
		List<PadPlan> planList = padPlanMapper.getPadPlanListByMap(map);
		if(planList.size()>0){
			for(int i=0;i<planList.size();i++){
				PadPlan padPlan = planList.get(i);
				// 同步计划至sap中间库
				syncPadPlanInfoToSapDB(padPlan);
			}
		}
	}
	
	@Transactional
	public void syncPadPlanInfoToSapDB(PadPlan padPlan){
		String id = padPlan.getId();
		// 获取明细数据
		List<PadPlanDetail> detailList = padPlanMapper.getPadPlanDetailListByMainId(id);
		
		try {
			int res = sapPadPlanService.saveSapPadPlanInfo(padPlan, detailList);
			if(res>0){
				// 修改同步状态 1: 已同步
				padPlanService.updatePadPlanSyncFlag("1",id);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
