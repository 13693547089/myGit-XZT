package com.faujor.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.faujor.entity.fam.ContactsMain;
import com.faujor.entity.fam.ContactsMone;
import com.faujor.entity.fam.ContactsMones;




public class ExportExcelForCostInfo
{


	public static void createExcelOfCostInfo(ContactsMain ContactsMain, HSSFSheet sheetForAuth, Workbook wb)
	{
		//设置列宽
		sheetForAuth.setColumnWidth(0, 9000);
		sheetForAuth.setColumnWidth(1, 10000);
		sheetForAuth.setColumnWidth(2, 10000);
		sheetForAuth.setColumnWidth(3, 10000);
		sheetForAuth.setColumnWidth(4, 10000);
		sheetForAuth.setColumnWidth(5, 10000);

		//创建一行作为表头
		//HSSFRow rowHead = sheetForAuth.createRow(0);
		//设置标题字体大小
		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 14);
		CellStyle titleType = ExcelUtil.setCellStyle(false, true, 3, 3, titleFont, wb);
		//设置数据字体大小
		Font dataFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 14);
		CellStyle dataType = ExcelUtil.setCellStyle(false, true, 3, 3, dataFont, wb);

		//填写表头信息
		/*
		 * rowHead.createCell(0).setCellValue("财务对账单号"); rowHead.createCell(1).setCellValue("状态");
		 * rowHead.createCell(2).setCellValue("填单日期"); rowHead.createCell(3).setCellValue("供应商");
		 * rowHead.createCell(4).setCellValue("账款截止日期");
		 */

		ExcelUtil.setValue(sheetForAuth, 0, 0, "财务对账单号", titleType);
		ExcelUtil.setValue(sheetForAuth, 0, 1, "状态", titleType);
		ExcelUtil.setValue(sheetForAuth, 0, 2, "填单日期", titleType);
		ExcelUtil.setValue(sheetForAuth, 0, 3, "供应商", titleType);
		ExcelUtil.setValue(sheetForAuth, 0, 4, "账款截止日期", titleType);
		ExcelUtil.setValue(sheetForAuth, 1, 5, "", dataType);
		//合并行
		sheetForAuth.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));


		if (ContactsMain != null)
		{

			String RecoNumb = ContactsMain.getRecoNumb();
			if (RecoNumb != null && RecoNumb.equals("null"))
			{
				RecoNumb = "";
			}
			String Status = ContactsMain.getStatus();
			if (Status != null && Status.equals("null"))
			{
				Status = "";
			}
			Date CreatTime = ContactsMain.getCreatTime();
			if (CreatTime != null && CreatTime.equals("null"))
			{
				CreatTime = null;
			}
			String SuppName = ContactsMain.getSuppName();
			if (SuppName != null && SuppName.equals("null"))
			{
				SuppName = "";
			}
			Date ClosDate = ContactsMain.getClosDate();
			if (ClosDate != null && ClosDate.equals("null"))
			{
				ClosDate = null;
			}
			// 创建新的一行
			//			HSSFRow row = sheetForAuth.createRow(1);
			//			//给这一行的每列赋值
			//			row.createCell(0).setCellValue(RecoNumb);
			//			row.createCell(1).setCellValue(Status);
			//			row.createCell(2).setCellValue(DateUtils.format(CreatTime));
			//			row.createCell(3).setCellValue(SuppName);
			//			row.createCell(4).setCellValue(DateUtils.format(ClosDate));
			ExcelUtil.setValue(sheetForAuth, 1, 0, RecoNumb, dataType);
			ExcelUtil.setValue(sheetForAuth, 1, 1, Status, dataType);
			ExcelUtil.setValue(sheetForAuth, 1, 2, DateUtils.format(CreatTime), dataType);
			ExcelUtil.setValue(sheetForAuth, 1, 3, SuppName, dataType);
			ExcelUtil.setValue(sheetForAuth, 1, 4, DateUtils.format(ClosDate), dataType);
			ExcelUtil.setValue(sheetForAuth, 1, 5, "", dataType);
			sheetForAuth.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));

		}
		//插入mone表数据
		List<ContactsMone> mone = ContactsMain.getMone();
		//		//创建一行作为表头
		//		HSSFRow rowMone = sheetForAuth.createRow(2);
		//		//填写表头信息
		//		rowMone.createCell(0).setCellValue("账款未结清期间");
		//		rowMone.createCell(1).setCellValue("当期采购应付帐款金额");
		//		rowMone.createCell(2).setCellValue("当期物料销售应收帐款金额");
		//		rowMone.createCell(3).setCellValue("预付账款金额");
		//		rowMone.createCell(4).setCellValue("质保金（其他应付款）");
		//		rowMone.createCell(5).setCellValue("备注");

		ExcelUtil.setValue(sheetForAuth, 2, 0, "账款未结清期间", titleType);
		ExcelUtil.setValue(sheetForAuth, 2, 1, "当期采购应付帐款金额", titleType);
		ExcelUtil.setValue(sheetForAuth, 2, 2, "当期物料销售应收帐款金额", titleType);
		ExcelUtil.setValue(sheetForAuth, 2, 3, "预付账款金额", titleType);
		ExcelUtil.setValue(sheetForAuth, 2, 4, "质保金（其他应付款）", titleType);
		ExcelUtil.setValue(sheetForAuth, 2, 5, "备注", titleType);
		int moneLength = 2;
		if (mone != null)
		{
			for (int i = 0; i < mone.size(); i++)
			{
				moneLength++;
				String AmouPaya = mone.get(i).getAmouPaya();
				if (AmouPaya != null && AmouPaya.equals("null"))
				{
					AmouPaya = "";
				}
				String AmouRece = mone.get(i).getAmouRece();
				if (AmouRece != null && AmouRece.equals("null"))
				{
					AmouRece = "";
				}
				String PrepAcco = mone.get(i).getPrepAcco();
				if (PrepAcco != null && PrepAcco.equals("null"))
				{
					PrepAcco = "";
				}
				String UnseAcco = mone.get(i).getUnseAcco();
				if (UnseAcco != null && UnseAcco.equals("null"))
				{
					UnseAcco = "";
				}
				String WarrMone = mone.get(i).getWarrMone();
				if (WarrMone != null && WarrMone.equals("null"))
				{
					WarrMone = "";
				}
				String Remarks = mone.get(i).getRemarks();
				if (Remarks != null && Remarks.equals("null"))
				{
					Remarks = "";
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

				ExcelUtil.setValue(sheetForAuth, moneLength, 0, UnseAcco, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 1, AmouPaya, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 2, AmouRece, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 3, PrepAcco, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 4, WarrMone, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 5, Remarks, dataType);

			}
		}
		//插入mones表
		List<ContactsMones> mones = ContactsMain.getMones();
		//创建一行作为表头
		moneLength++;
		//		HSSFRow rowMones = sheetForAuth.createRow(moneLength);
		//		//填写表头信息
		//		rowMones.createCell(0).setCellValue("应付暂估款期间");
		//		rowMones.createCell(1).setCellValue("金额");
		//		rowMones.createCell(2).setCellValue("备注");

		ExcelUtil.setValue(sheetForAuth, moneLength, 0, "应付暂估款期间", titleType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 1, "金额", titleType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 2, "备注", titleType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 3, "", dataType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 4, "", dataType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 5, "", dataType);
		sheetForAuth.addMergedRegion(new CellRangeAddress(moneLength, moneLength, 2, 5));
		if (mones != null)
		{
			for (int i = 0; i < mones.size(); i++)
			{
				moneLength++;
				String TempEsti = mones.get(i).getTempEsti();
				if (TempEsti != null && TempEsti.equals("null"))
				{
					TempEsti = "";
				}
				String Mones = mones.get(i).getMone();
				if (Mones != null && Mones.equals("null"))
				{
					Mones = "";
				}
				String Remarks = mones.get(i).getRemarks();
				if (Remarks != null && Remarks.equals("null"))
				{
					Remarks = "";
				}
				//				// 创建新的一行
				//				HSSFRow row = sheetForAuth.createRow(moneLength);
				//				//给这一行的每列赋值
				//				row.createCell(0).setCellValue(TempEsti);
				//				row.createCell(1).setCellValue(Mones);
				//				row.createCell(2).setCellValue(Remarks);
				ExcelUtil.setValue(sheetForAuth, moneLength, 0, TempEsti, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 1, Mones, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 2, Remarks, dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 3, "", dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 4, "", dataType);
				ExcelUtil.setValue(sheetForAuth, moneLength, 5, "", dataType);

				sheetForAuth.addMergedRegion(new CellRangeAddress(moneLength, moneLength, 2, 5));

			}

		}
		//创建一行作为表头
		moneLength++;
		//		HSSFRow rowMonesSum = sheetForAuth.createRow(moneLength);
		//		//填写表头信息
		//		rowMonesSum.createCell(0).setCellValue("当期银行付款合计");
		//		rowMonesSum.createCell(1).setCellValue("其中因贵司申请需要提前支付货款收取贵公司利息金额");

		ExcelUtil.setValue(sheetForAuth, moneLength, 0, "当期银行付款合计", titleType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 1, "贵司申请提前支付货款收取贵司利息金额", titleType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 2, "确认数据", titleType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 3, "备注", titleType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 4, "", dataType);
		ExcelUtil.setValue(sheetForAuth, moneLength, 5, "", dataType);
		sheetForAuth.addMergedRegion(new CellRangeAddress(moneLength, moneLength, 3, 5));
		if (ContactsMain != null)
		{
			moneLength++;
			String BankTota = ContactsMain.getBankTota();
			if (BankTota != null && BankTota.equals("null"))
			{
				BankTota = "";
			}
			String AdvaInteAmou = ContactsMain.getAdvaInteAmou();
			if (AdvaInteAmou != null && AdvaInteAmou.equals("null"))
			{
				AdvaInteAmou = "";
			}
			String InteAmou = ContactsMain.getInteAmou();
			if (InteAmou != null && InteAmou.equals("null"))
			{
				InteAmou = "";
			}
			String DataText = ContactsMain.getDataText();
			if (DataText != null && DataText.equals("null"))
			{
				DataText = "";
			}
			//			HSSFRow rowMonesSumData = sheetForAuth.createRow(moneLength);
			//			//填写表头信息
			//			rowMonesSumData.createCell(0).setCellValue(BankTota);
			//			rowMonesSumData.createCell(1).setCellValue(AdvaInteAmou);
			sheetForAuth.addMergedRegion(new CellRangeAddress(moneLength, moneLength, 3, 5));
			ExcelUtil.setValue(sheetForAuth, moneLength, 0, BankTota, dataType);
			ExcelUtil.setValue(sheetForAuth, moneLength, 1, AdvaInteAmou, dataType);
			ExcelUtil.setValue(sheetForAuth, moneLength, 2, InteAmou, dataType);
			ExcelUtil.setValue(sheetForAuth, moneLength, 3, DataText, dataType);
			ExcelUtil.setValue(sheetForAuth, moneLength, 4, "", dataType);
			ExcelUtil.setValue(sheetForAuth, moneLength, 5, "", dataType);
		}
		//创建一行作为表头
		//		moneLength++;
		//		HSSFRow rowRemarks = sheetForAuth.createRow(moneLength);
		//		//填写表头信息
		//		rowRemarks.createCell(0).setCellValue("确认数据");
		//		rowRemarks.createCell(1).setCellValue("备注");
		//		if (ContactsMain != null)
		//		{
		//			moneLength++;
		//			String InteAmou = ContactsMain.getInteAmou();
		//			if (InteAmou != null && InteAmou.equals("null"))
		//			{
		//				InteAmou = "";
		//			}
		//			String DataText = ContactsMain.getDataText();
		//			if (DataText != null && DataText.equals("null"))
		//			{
		//				DataText = "";
		//			}
		//			HSSFRow rowRemarksData = sheetForAuth.createRow(moneLength);
		//			//填写表头信息
		//			rowRemarksData.createCell(0).setCellValue(InteAmou);
		//			rowRemarksData.createCell(1).setCellValue(DataText);
		//		}
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
