//package com.faujor.utils.PDF;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.core.io.ClassPathResource;
//
//import com.alibaba.fastjson.JSONArray;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.AcroFields;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfStamper;
//import com.itextpdf.text.pdf.PdfWriter;
//
//public class PDFUtils {
//	public static void main(String[] args) {
//
//	}
//
//	float flat1 = 18f;
//	static Font FontChinese;
//	static Font font26;
//	static Font font24;
//	static Font font18;
//	static Font font16;
//	static Font font15;
//	static Font font14;
//	static Font font11;
//	static Font font11_bold;
//	static Font font12_bold;
//	static Font font10;
//	static Font font10_bold;
//	static Font font;
//	static Font font7_bold;
//	static Font font8;
//	static Font font7_5;
//	static Font font6_5_bold;
//	static Font font5_5;
//	static Font fontSpace;
//	Boolean isCenter = true, isBorder = true, isHeight = true;
//	int widthPercentage = 100;
//	float lineHeight = (float) 13.5;
//
//	public void generationPDF() throws IOException, DocumentException {
//		// 模板路径
//		// 生成的新文件路径
//		String templatePath = new ClassPathResource("appoint.pdf").getFile().getAbsolutePath();
//		String newPath = templatePath.replace("target/classes/appoint.pdf", "test1.pdf");
//
//		PdfReader reader = new PdfReader(templatePath);// 读取pdf模板
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//		// 目标PDF文件名称
//		PdfStamper stamper = new PdfStamper(reader, bos);
//		PdfContentByte under = stamper.getUnderContent(1);
//
//		// 使用中文字体
//		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//		ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
//		fontList.add(bf);
//		AcroFields fields = stamper.getAcroFields();
//		fillData(fields, data());
////		java.util.Iterator<String> it = fields.getFields().keySet().iterator();
////		while (it.hasNext()) {
////			String name = it.next().toString();
////			// 填写内容部分
////			form.setField(name, "2465465145648456");
////			//
////		}
//		stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
//		stamper.close();
//
//		FileOutputStream fos = new FileOutputStream(newPath);
//		fos.write(bos.toByteArray());
//		fos.flush();
//		fos.close();
//		bos.close();
//
////		Document doc = new Document();
//
////		PdfCopy copy = new PdfCopy(doc, fos);
////		doc.open();
////		PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
////		copy.addPage(importPage);
////		doc.close();
//	}
//
//	/**
//	 *     * 填充模板     *
//	 */
//	public static void fillData(AcroFields fields, Map<String, String> data) throws IOException, DocumentException {
//		for (String key : data.keySet()) {
//			String value = data.get(key);
//			fields.setField(key, value);// 为字段赋值,注意字段名称是区分大小写的  
//		}
//	}
//
//	/**
//	 *     * 填充数据源     * 其中data存放的key值与pdf模板中的文本域值相对应     *
//	 */
//	public static Map<String, String> data() {
//		Map<String, String> data = new HashMap<String, String>();
//		data.put("TARGET", "李磊");
//		return data;
//	}
//
//	private void addPageDetailHead(Document document, PdfWriter writer, String OBJID_TEXT, String DATA)
//			throws Exception {
//		// 新建页
//		document.newPage();
//		this.isBorder = false;
//
//		Paragraph headParagraph = new Paragraph("TOP 公司提/发货通知单");
//		headParagraph.setAlignment(Element.ALIGN_CENTER);
//		document.add(headParagraph);
//		PdfPTable title = new PdfPTable(1);
//		title.setWidthPercentage(100);
//		this.isBorder = false;
//		PdfPCell pdfPCell = this.getPdfPCellLeft("TO:111");
//		pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		title.addCell(pdfPCell);
//		PdfPCell pdfPCell2 = this.getPdfPCellLeft("FR:脱普日用化学品(中国)有限公司采购部 021-62261100");
//		pdfPCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		title.addCell(pdfPCell2);
//		PdfPCell pdfPCell3 = this.getPdfPCellLeft("Subject OEM 发货预约通知");
//		pdfPCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		title.addCell(pdfPCell3);
//
//		PdfPCell pdfPCell4 = this.getPdfPCellLeft("请贵公司于3月3号向我公司制定地点送货，具体发货明细如下:");
//		pdfPCell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		title.addCell(pdfPCell4);
//		
//		document.add(title);
//		
//		PdfPTable headTable = new PdfPTable(6);
//		headTable.setWidthPercentage(100);
//		this.isBorder = true;
//		headTable.addCell(this.getPdfPCell("仓库"));
//		headTable.addCell(this.getPdfPCell("品项"));
//		headTable.addCell(this.getPdfPCell("数量"));
//		headTable.addCell(this.getPdfPCell("方量"));
//		headTable.addCell(this.getPdfPCell("重量（KG）"));
//		headTable.addCell(this.getPdfPCell("预约单"));
//		document.add(headTable);
//		
//	}
//
//	private void insertTable(Document document, AcroFields fields, PdfStamper ps) throws DocumentException {
////		List<AcroFields.FieldPosition> list = fields.getFieldPositions("CONNECT_NAME");
//		PdfPTable table = new PdfPTable(6);
//		table.setWidthPercentage(100);
//		this.isBorder = true;
//		for (int i = 0; i < 4; i++) {
//			table.addCell(this.getPdfPCell("网易"));
//			table.addCell(this.getPdfPCell("云"));
//			table.addCell(this.getPdfPCell("音乐"));
//		}
//		document.add(table);
//		
//		// 尾部信息
//		PdfPTable pdfPTable = new PdfPTable(1);
//		pdfPTable.addCell(this.getPdfPCell("备注：第三方的是否定是"));
//		document.add(pdfPTable);
//		
//		PdfPTable pdfPTable2 = new PdfPTable(2);
//	}
//
////	/**
////	 * 创建Chunk
////	 * 
////	 * @return
////	 * @author WangMeng
////	 * @date 2016年6月16日
////	 */
////	public static Chunk CreateChunk() {
////		BaseFont bfChinese;
////		Chunk chunk = null;
////		try {
////			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
////			Font fontChinese = new Font(bfChinese, 10 * 4 / 3);
////			chunk = new Chunk("张三", fontChinese);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////		return chunk;
////	}
//
//	public PdfPCell getPdfPCell(String cellName) {
//		if (font == null) {
//			font = FontChinese;
//		}
//		PdfPCell pdfPCell = MyPdfCell.getMyPdfCell(new Paragraph(cellName, font));
//		if (isHeight) {
//			pdfPCell.setMinimumHeight(25f);
//		}
//		if (isCenter) {
//			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		}
//		if (!isBorder) {
//			// pdfPCell.setBorderWidth(arg0);
//			pdfPCell.setBorder(0);
//		}
//		pdfPCell.setFixedHeight(lineHeight);
//		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		pdfPCell.setPadding(0);
//		return pdfPCell;
//	}
//
//	public PdfPCell getPdfPCellLeft(String cellName) throws DocumentException, IOException {
//		if (font == null) {
//			font = FontChinese;
//		}
//		PdfPCell pdfPCell = MyPdfCell.getMyPdfCell(new Paragraph(cellName, font));
//		if (isHeight) {
//			pdfPCell.setMinimumHeight(25f);
//		}
//		pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//		if (!isBorder) {
//			pdfPCell.setBorder(0);
//		}
//		pdfPCell.setPadding(0);
//		return pdfPCell;
//	}
//}
