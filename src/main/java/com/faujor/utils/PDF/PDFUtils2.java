package com.faujor.utils.PDF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.AppointMail;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFUtils2 {
	Boolean isCenter = true, isBorder = true, isHeight = true;
	int widthPercentage = 100;
	float lineHeight = (float) 13.5;

	public String generationPDF(AppointMail appoint, List<AppoMate> list) {
		try {
			// 模板路径
			// 生成的新文件路径
//			String templatePath = new ClassPathResource("appoint.pdf").getFile().getAbsolutePath();
			String fileName = appoint.getAppointCode() + ".pdf";
//			String newPath = templatePath.replace("target/classes/appoint.pdf", fileName);
			String newPath = "D:/Apache/PDF/" + fileName;
			File file = new File(newPath);
			if (!file.exists()) {
				FileOutputStream fos = new FileOutputStream(newPath);
				ByteArrayOutputStream generationPDF1 = this.generationPDF1(appoint, list);
				fos.write(generationPDF1.toByteArray());
				fos.close();
			}
			return newPath;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public ByteArrayOutputStream generationPDF1(AppointMail appoint, List<AppoMate> list) throws Exception {
		Rectangle re = PageSize.A4;
		Document document = new Document(re, 30, 30, 30, 10);// left right up down
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// 必不可少
		PdfWriter.getInstance(document, stream);
		document.open();
//		String fontPath = new ClassPathResource("yehei.ttf").getFile().getAbsolutePath();
		String fontPath = "./yahei.ttf";
//		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(bf, 9, Font.NORMAL);

		Font boldFont = new Font(bf, 12, Font.BOLD);

		Paragraph headParagraph = new Paragraph("TOP公司提/发货通知单", boldFont);
		headParagraph.setAlignment(Element.ALIGN_CENTER);
		document.add(headParagraph);

		PdfPTable title = new PdfPTable(1);
		title.setWidthPercentage(100);
		title.setSpacingBefore(12f);
		String to = "TO:" + appoint.getSupplierName();
//		PdfPCell pdfPCell = new PdfPCell(new Phrase("TO:四川宏浩纸塑有限公司（FAX:0838-5656500）", font));
		PdfPCell pdfPCell = new PdfPCell(new Phrase(to, font));
		pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPCell.setBorder(Rectangle.NO_BORDER);
		pdfPCell.setFixedHeight(20f);
		title.addCell(pdfPCell);
		PdfPCell pdfPCell2 = new PdfPCell(new Phrase("FR:脱普日用化学品(中国)有限公司采购部 021-62261100", font));
		pdfPCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPCell2.setBorder(Rectangle.NO_BORDER);
		pdfPCell2.setFixedHeight(20f);
		title.addCell(pdfPCell2);
		PdfPCell pdfPCell3 = new PdfPCell(new Phrase("Subject OEM发货预约通知", font));
		pdfPCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPCell3.setBorder(Rectangle.NO_BORDER);
		pdfPCell3.setFixedHeight(20f);
		title.addCell(pdfPCell3);
		PdfPCell pdfPCell5 = new PdfPCell(new Phrase("", font));
		pdfPCell5.setBorder(Rectangle.BOTTOM);
		title.addCell(pdfPCell5);
		String head = "请贵公司于" + appoint.getDeliveryDate() + "向我公司制定地点送货,具体发货明细如下:";
		PdfPCell pdfPCell4 = new PdfPCell(new Phrase(head, font));

		pdfPCell4.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPCell4.setBorder(Rectangle.NO_BORDER);
		pdfPCell4.setFixedHeight(18f);
		pdfPCell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
		title.addCell(pdfPCell4);

		document.add(title);

		this.setTableHeaderInfo(document, font);
		int tableSize = list.size() + 1;
		this.setTableDataInfo(document, font, appoint, list);
		// 25 极限
		// 计算尾部和中间部分的间隔
		int range = 500 - 18 * tableSize;
		// 添加尾部信息
		this.setFooterInfo(document, range, font, appoint);
		document.close();
		return stream;
	}

	// 添加表格头部信息
	private void setTableHeaderInfo(Document document, Font font) throws DocumentException {
		PdfPTable headTable = new PdfPTable(6);
		headTable.setWidthPercentage(100);
		headTable.setSpacingBefore(6f);
		headTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		Phrase phrase = new Phrase("仓库", font);
		PdfPCell pdfPCell6 = new PdfPCell(phrase);
		pdfPCell6.setHorizontalAlignment(Element.ALIGN_CENTER);

		headTable.addCell(pdfPCell6);
		int align = Element.ALIGN_CENTER;
		headTable.addCell(this.setCellStyle("品项", font, align));
		headTable.addCell(this.setCellStyle("数量", font, align));
		headTable.addCell(this.setCellStyle("方量", font, align));
		headTable.addCell(this.setCellStyle("重量（KG）", font, align));
		headTable.addCell(this.setCellStyle("预约单", font, align));
		document.add(headTable);
	}

	// 添加表格信息
	private void setTableDataInfo(Document document, Font font, AppointMail appoint, List<AppoMate> list)
			throws DocumentException {
		int align = Element.ALIGN_CENTER;
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		this.isBorder = true;
		String warehouse = appoint.getWarehouse();
		String appointCode = appoint.getAppointCode();
		BigDecimal t_mateNumber = BigDecimal.ZERO;
		BigDecimal t_mateAmount = BigDecimal.ZERO;
		for (AppoMate am : list) {
			table.addCell(this.setCellStyle(warehouse, font, align));
			table.addCell(this.setCellStyle(am.getMateName(), font, align));
			Double mateNumber = am.getMateNumber();
			BigDecimal mn = new BigDecimal(mateNumber.toString());
			t_mateNumber = t_mateNumber.add(mn);
			table.addCell(this.setCellStyle(am.getMateNumber().toString(), font, align));
			Double mateAmount = am.getMateAmount();
			BigDecimal ma = new BigDecimal(mateAmount.toString());
			t_mateAmount = t_mateAmount.add(ma);

			table.addCell(this.setCellStyle(am.getMateAmount().toString(), font, align));
			table.addCell(this.setCellStyle("", font, align));
			table.addCell(this.setCellStyle(appointCode, font, align));
		}
		table.addCell(this.setCellStyle("", font, align));
		table.addCell(this.setCellStyle("合计", font, align));
		table.addCell(this.setCellStyle(t_mateNumber.toString(), font, align));
		table.addCell(this.setCellStyle(t_mateAmount.toString(), font, align));
		table.addCell(this.setCellStyle("", font, align));
		table.addCell(this.setCellStyle("", font, align));
		document.add(table);
	}

	// 添加尾部信息
	private void setFooterInfo(Document document, int range, Font font, AppointMail appoint) throws DocumentException {
		// 尾部信息
		int left = Element.ALIGN_LEFT;
		PdfPTable pdfPTable = new PdfPTable(1);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setSpacingBefore(range);

		this.isBorder = false;
		pdfPTable.addCell(this.setCellStyle("备注：", font, left));
		document.add(pdfPTable);

		PdfPTable pdfPTable2 = new PdfPTable(6);
		pdfPTable2.setWidthPercentage(100);
		String carNum = "车辆数：" + appoint.getCarNum();
		pdfPTable2.addCell(this.setCellStyle(carNum, font, left));
		String carType = "车型：" + appoint.getCarType();
		pdfPTable2.addCell(this.setCellStyle(carType, font, left));
		pdfPTable2.addCell(this.setCellStyle("", font, left));
		pdfPTable2.addCell(this.setCellStyle("", font, left));
		pdfPTable2.addCell(this.setCellStyle("", font, left));
		pdfPTable2.addCell(this.setCellStyle("", font, left));
		document.add(pdfPTable2);

		PdfPTable pdfPTable3 = new PdfPTable(1);
		pdfPTable3.setWidthPercentage(100);
		pdfPTable3.setSpacingBefore(6f);
		pdfPTable3.addCell(this.setCellStyle("以上产品运至:", font, left));
//		pdfPTable3.addCell(this.setCellStyle("无锡市新区梅村镇群兴路59号", font, left));
		pdfPTable3.addCell(this.setCellStyle(appoint.getDestination(), font, left));

		String telphone = "联系电话：" + appoint.getTelphone();
		pdfPTable3.addCell(this.setCellStyle(telphone, font, left));
		String contact = "联系人：" + appoint.getContact();
		pdfPTable3.addCell(this.setCellStyle(contact, font, left));
		document.add(pdfPTable3);
	}

	public PdfPCell setCellStyle(String cellVal, Font font1, int align) {
		Phrase phrase = new Phrase(cellVal, font1);
		PdfPCell cell = new PdfPCell(phrase);
//		cell.setFixedHeight(20f);
		cell.setVerticalAlignment(align);
		cell.setHorizontalAlignment(align);
		if (!isBorder)
			cell.setBorder(Rectangle.NO_BORDER);

//		cell.setFixedHeight(15f);
		return cell;
	}
}
