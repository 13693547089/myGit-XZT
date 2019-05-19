package com.faujor.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.ResourceUtils;

import com.faujor.entity.bam.Appoint;


public class ExportAppointList
{
	//	public static void exportAppointList(List<Appoint> appList, HSSFSheet sheetForAuth, Workbook wb)
	//	{
	//		//设置列宽
	//		sheetForAuth.setColumnWidth(0, 6000);
	//		sheetForAuth.setColumnWidth(1, 6000);
	//		sheetForAuth.setColumnWidth(2, 10000);
	//		sheetForAuth.setColumnWidth(3, 3000);
	//		sheetForAuth.setColumnWidth(4, 3000);
	//
	//
	//		//创建一行作为表头
	//		//HSSFRow rowHead = sheetForAuth.createRow(0);
	//		//设置标题字体大小
	//		Font titleFont = wb.createFont();
	//		titleFont.setFontHeightInPoints((short) 14);
	//		CellStyle titleType = ExcelUtil.setCellStyle(false, true, 3, 3, titleFont, wb);
	//		//titleType.setFillBackgroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
	//		//titleType.setFillPattern(FillPatternType.NO_FILL);
	//
	//		//设置数据字体大小
	//		Font dataFont = wb.createFont();
	//		titleFont.setFontHeightInPoints((short) 14);
	//		CellStyle dataType = ExcelUtil.setCellStyle(false, true, 3, 3, dataFont, wb);
	//
	//		ExcelUtil.setValue(sheetForAuth, 0, 0, "预约日期", titleType);
	//		ExcelUtil.setValue(sheetForAuth, 0, 1, "实际送货时间", titleType);
	//		ExcelUtil.setValue(sheetForAuth, 0, 2, "供应商", titleType);
	//		ExcelUtil.setValue(sheetForAuth, 0, 3, "车次", titleType);
	//		ExcelUtil.setValue(sheetForAuth, 0, 4, "方量", titleType);
	//		int sumCar = 0;
	//		int sumFang = 0;
	//		int moneLength = 0;
	//
	//		if (appList != null)
	//		{
	//			for (int i = 0; i < appList.size(); i++)
	//			{
	//				moneLength++;
	//				String SuppName = appList.get(i).getSuppName();
	//				if (SuppName != null && SuppName.equals("null"))
	//				{
	//					SuppName = "";
	//				}
	//				String AffirmDat = appList.get(i).getAffirmDate();
	//				if (AffirmDat != null && AffirmDat.equals("null"))
	//				{
	//					AffirmDat = "";
	//				}
	//				Date AppoDate = appList.get(i).getAppoDate();
	//				String format = DateUtils.format(AppoDate, "yyyy年MM月dd日");
	//				if (format != null && format.equals("null"))
	//				{
	//					format = "";
	//				}
	//				Integer truckNum = appList.get(i).getTruckNum();
	//				if (truckNum == null && truckNum.equals("null"))
	//				{
	//					truckNum = 0;
	//				}
	//				else
	//				{
	//					sumCar += truckNum;
	//				}
	//				Integer mateAmount = appList.get(i).getMateAmount();
	//				if (mateAmount == null && mateAmount.equals("null"))
	//				{
	//					mateAmount = 0;
	//				}
	//				else
	//				{
	//					sumFang += mateAmount;
	//				}
	//
	//
	//				// 创建新的一行
	//				//				HSSFRow row = sheetForAuth.createRow(moneLength);
	//				//				//给这一行的每列赋值
	//				//				row.createCell(0).setCellValue(UnseAcco);
	//				//				row.createCell(1).setCellValue(AmouPaya);
	//				//				row.createCell(2).setCellValue(AmouRece);
	//				//				row.createCell(3).setCellValue(PrepAcco);
	//				//				row.createCell(4).setCellValue(WarrMone);
	//				//				row.createCell(5).setCellValue(Remarks);
	//
	//				ExcelUtil.setValue(sheetForAuth, moneLength, 0, format, dataType);
	//				ExcelUtil.setValue(sheetForAuth, moneLength, 1, AffirmDat, dataType);
	//				ExcelUtil.setValue(sheetForAuth, moneLength, 2, SuppName, dataType);
	//				ExcelUtil.setValue(sheetForAuth, moneLength, 3, truckNum, dataType);
	//				ExcelUtil.setValue(sheetForAuth, moneLength, 4, mateAmount, dataType);
	//
	//			}
	//		}
	//		moneLength++;
	//		ExcelUtil.setValue(sheetForAuth, moneLength, 0, "合计", titleType);
	//		ExcelUtil.setValue(sheetForAuth, moneLength, 3, sumCar, titleType);
	//		ExcelUtil.setValue(sheetForAuth, moneLength, 4, sumFang, titleType);
	//
	//
	//	}

	public static Workbook exportAppointList(String receUnit,List<Appoint> appList, HttpServletRequest request, HttpServletResponse response)
	{
		//response.setContentType("text/html");
		Workbook wb = null;
		try
		{
			String filePath = ResourceUtils.getURL("classpath:").getPath();
			// 模板路径
			String xlsTemplatePath = filePath + "templates\\excelTemp\\预约发布信息样式2.xlsx";

			wb = ExcelUtil.getWorkBook(xlsTemplatePath);
			String currDate = DateUtils.format(new Date(), DateUtils.DATE_PATTERN_TWO);
			/*
			 * String fileName = "预约发布信息" + currDate; response.setContentType("application/vnd.ms-excel;charset=utf-8");
			 * response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"),
			 * "ISO-8859-1") + ".xlsx");
			 */

			Sheet sheetForAuth = wb.getSheetAt(0);
			int sumCar = 0;
			int sumFang = 0;
			int moneLength = 1;

			if (appList != null)
			{
				int listSize = appList.size();
				if (listSize > 20)
				{
					// 插入行数
					ExcelUtil.insertRow(sheetForAuth, 2, listSize - 20);
				}
				ExcelUtil.setValue(sheetForAuth, 0, 0, receUnit, null);
				for (int i = 0; i < appList.size(); i++)
				{
					moneLength++;
					String SuppName = appList.get(i).getSuppName();
					if (SuppName != null && SuppName.equals("null"))
					{
						SuppName = "";
					}
					String AffirmDat = appList.get(i).getAffirmDate();
					if (AffirmDat != null && AffirmDat.equals("null"))
					{
						AffirmDat = "";
					}
					Date AppoDate = appList.get(i).getAppoDate();
					String format = DateUtils.format(AppoDate, "yyyy年MM月dd日");
					if (format != null && format.equals("null"))
					{
						format = "";
					}
					Integer truckNum = appList.get(i).getTruckNum();
					if (truckNum == null && truckNum.equals("null"))
					{
						truckNum = 0;
					}
					else
					{
						sumCar += truckNum;
					}
					Double mateAmount = appList.get(i).getMateAmount();
					if (mateAmount == null && mateAmount.equals("null"))
					{
						mateAmount = 0D;
					}
					else
					{
						sumFang += mateAmount;
					}


					// 创建新的一行
					//				HSSFRow row = sheetForAuth.createRow(moneLength);
					//				//给这一行的每列赋值
					//				row.createCell(0).setCellValue(UnseAcco);
					//				row.createCell(1).setCellValue(AmouPaya);
					//				row.createCell(2).setCellValue(AmouRece);
					//				row.createCell(3).setCellValue(PrepAcco);
					//				row.createCell(4).setCellValue(WarrMone);
					//				row.createCell(5).setCellValue(Remarks);

					ExcelUtil.setValue(sheetForAuth, moneLength, 0, format, null);
					ExcelUtil.setValue(sheetForAuth, moneLength, 1, AffirmDat, null);
					ExcelUtil.setValue(sheetForAuth, moneLength, 2, SuppName, null);
					ExcelUtil.setValue(sheetForAuth, moneLength, 3, truckNum, null);
					ExcelUtil.setValue(sheetForAuth, moneLength, 4, mateAmount, null);

				}
				if (listSize > 20)
				{
					moneLength = moneLength + 1;
				}
				else
				{
					moneLength = 21;
				}
				ExcelUtil.setValue(sheetForAuth, moneLength, 0, "合计", null);
				ExcelUtil.setValue(sheetForAuth, moneLength, 3, sumCar, null);
				ExcelUtil.setValue(sheetForAuth, moneLength, 4, sumFang, null);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 设置下载文件编码
	 *
	 * @param request
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 */
	public final static void setAttachmentFileName(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws UnsupportedEncodingException
	{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/octet-stream;charset=UTF-8");

		fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
	}
}
