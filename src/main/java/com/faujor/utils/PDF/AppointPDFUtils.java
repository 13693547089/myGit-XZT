package com.faujor.utils.PDF;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 预约单PDF生成类
 * 
 * @author martian
 *
 */
public class AppointPDFUtils {
	float flat1 = 18f;
	static Font FontChinese;
	static Font font26;
	static Font font24;
	static Font font18;
	static Font font16;
	static Font font15;
	static Font font14;
	static Font font11;
	static Font font11_bold;
	static Font font12_bold;
	static Font font10;
	static Font font10_bold;
	static Font font;
	static Font font7_bold;
	static Font font8;
	static Font font7_5;
	static Font font6_5_bold;
	static Font font5_5;
	static Font fontSpace;
	Boolean isCenter = true, isBorder = true, isHeight = true;
	int widthPercentage = 100;
	float lineHeight = (float) 13.5;

	static {
		BaseFont bfChinese;
		try {
			bfChinese = BaseFont.createFont("/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			FontChinese = new Font(bfChinese, 14, Font.NORMAL);
			font26 = new Font(bfChinese, 26, Font.BOLD);
			font24 = new Font(bfChinese, 26, Font.BOLD);
			font18 = new Font(bfChinese, 18, Font.NORMAL);
			font16 = new Font(bfChinese, 16, Font.NORMAL);
			font15 = new Font(bfChinese, 15, Font.NORMAL);
			font14 = new Font(bfChinese, 14, Font.NORMAL);
			font12_bold = new Font(bfChinese, 12, Font.BOLD);
			font11 = new Font(bfChinese, 11, Font.NORMAL);
			font11_bold = new Font(bfChinese, 11, Font.BOLD);
			font10 = new Font(bfChinese, 10, Font.NORMAL);
			font10_bold = new Font(bfChinese, 10, Font.BOLD);
			fontSpace = new Font(bfChinese, 2, Font.NORMAL);
			font5_5 = new Font(bfChinese, (float) 7.5, Font.NORMAL);
			font8 = new Font(bfChinese, (float) 8, Font.NORMAL);
			font7_bold = new Font(bfChinese, 7, Font.BOLD);
			font7_5 = new Font(bfChinese, (float) 7.5, Font.NORMAL);
			font6_5_bold = new Font(bfChinese, (float) 7.5, Font.BOLD);
			font5_5 = new Font(bfChinese, (float) 5.5, Font.NORMAL);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			JSONArray jsonarray = new JSONArray();

			JSONObject jsonObj = new JSONObject();

			String[] arr = { "BKREF", "PERNR", "NACHN", "BET1100", "BET1500", "BET3004", "BET3003", "BET3002",
					"BET3009", "BET3055", "BET3034", "BET3098", "BET4009", "BET4010", "BET4013", "BET4096", "BET101",
					"BETYLSY", "BET333", "BET2071", "BET362", "BETYFHJ", "BET313", "BET323", "BET2098", "BET4098",
					"BET3035", "BET401", "BET403", "BET559", "BETGZXJ", "BET4099", "BET4003" };

			int length = arr.length;
			for (int k = 0; k < 30; k++) {
				for (int j = 0; j < length; j++) {
					jsonObj.put(arr[j], "6120.89");
				}
				jsonObj.put("NACHN", "张三李四");
				jsonObj.put("BKREF", "12345678901234567890");

				jsonObj.put("KK", "6120.89");
				jsonObj.put("ZYGXZ", "03");
				jsonObj.put("ZYGXZWB", (k % 2) + "用工");

				jsonarray.add(jsonObj);
			}

			ByteArrayOutputStream generationPDF = new AppointPDFUtils().generationPDF(jsonarray, "人事劳动部", "2015-12");

			FileOutputStream stream = new FileOutputStream("c:\\e\\" + "b.pdf");
			stream.write(generationPDF.toByteArray());
			stream.close();
			System.out.println("b" + ".pdf 文件生成成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ByteArrayOutputStream generationPDF(JSONArray jsonarray, String OBJID_TEXT, String DATA) throws Exception {
		Rectangle re = PageSize.A4;
		Document document = new Document(re, 0, 0, 0, 0);
		// 实例化文档对象
		Rectangle A42 = new RectangleReadOnly(842, 595);
		// re = A42;
		// Document document = new Document(re, 0, 0, 0, 0);
		// //页边空白
		document.setMargins(30, 10, 0, 10);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建 PdfWriter 对象
		PdfWriter writer = PdfWriter.getInstance(document, stream);
		// 以下页设置为横向A4大小
		document.setPageSize(new RectangleReadOnly(842, 595));

		document.open();

		// 考勤明细表
		int size = jsonarray.size();

		int rows = 29;
		int begin = 0;
		int end = 0;
		int pages = 0;
		Map<String, String> mapText = new HashMap<String, String>();
		Map<String, JSONArray> mapJson = new HashMap<String, JSONArray>();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = jsonarray.getJSONObject(i);
			jsonObject.put("ZYGXZWB", ((String) jsonObject.get("ZYGXZWB")).replace("协议农合工", "农合工"));
			jsonObject.put("ZYGXZ", ((String) jsonObject.get("ZYGXZ")).replace("04", "03"));

			// 工别 文本

			String ZYGXZ = (String) jsonObject.get("ZYGXZ");
			String ZYGXZWB = (String) jsonObject.get("ZYGXZWB");
			if (mapJson.containsKey(ZYGXZ)) {
				mapJson.get(ZYGXZ).add(jsonObject);
			} else {
				mapText.put(ZYGXZ, ZYGXZWB);
				JSONArray jsonArrayObj = new JSONArray();
				jsonArrayObj.add(jsonObject);
				mapJson.put(ZYGXZ, jsonArrayObj);
			}
		}
		String[] split = { "KK", "BET1100", "BET1500", "BET3004", "BET3003", "BET3002", "BET3009", "BET3055", "BET3034",
				"BET3098", "BET4009", "BET4010", "BET4013", "BET4096", "BET101", "BETYLSY", "BET333", "BET2071",
				"BET362", "BETYFHJ", "BET313", "BET323", "BET2098", "BET4098", "BET3035", "BET401", "BET403", "BET559",
				"BETGZXJ", "BET4099", "BET2200", "DFJNBI", "BET4003" };
		String str = "";
		int length = split.length;
		Set<String> keySet = mapJson.keySet();
		int size_keyset = keySet.size();
		Iterator<String> iterator1 = keySet.iterator();
		JSONArray jsonArray_new = new JSONArray();

		while (iterator1.hasNext()) {
			String ZYGXZ = iterator1.next();
			JSONArray jsonArray2 = mapJson.get(ZYGXZ);

			size = jsonArray2.size();

			for (int i = 0; i < size; i++) {
				jsonArray_new.add(jsonArray2.get(i));
			}
			JSONObject obj_hj = new JSONObject();

			for (int j = 0; j < length; j++) {
				str = split[j];
				obj_hj.put(str, Double.valueOf(0));
			}
			int rs = 0;
			for (int k = 0; k < size; k++) {
				rs++;
				for (int j = 0; j < length; j++) {
					JSONObject jsonObj = jsonArray2.getJSONObject(k);
					str = split[j];
					if (jsonObj.containsKey(str)) {
						obj_hj.put(str, Double.valueOf(obj_hj.getString(str)) + Double.valueOf(jsonObj.getString(str)));
					} else {
					}
				}
			}

			for (int j = 0; j < length; j++) {

				BigDecimal b = new BigDecimal(obj_hj.getDouble(split[j]));
				b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
				obj_hj.put(split[j], b.stripTrailingZeros().toPlainString());
				if (obj_hj.getString(split[j]).equals("0.00")) {
					obj_hj.put(split[j], "0");
				}
			}

			obj_hj.put("BKREF", mapText.get(ZYGXZ) + " 小计");
			obj_hj.put("NACHN", rs + "");

			jsonArray_new.add(obj_hj);
		}

		begin = 0;
		end = 0;

		size = jsonArray_new.size();

		if ((size) % rows == 0)
			pages = (size) / rows;
		else
			pages = (size) / rows + 1;

		for (int i = 0; i < pages; i++) {
			this.addPageDetailHead(document, writer, OBJID_TEXT, DATA);
			if (begin + rows > size) {
				end = size;
			} else {
				end = begin + rows;
			}

			this.addPageDetailList(document, jsonArray_new, begin, end, size, size_keyset);

			begin = (i + 1) * rows;
		}

		document.close();// 关闭

		return stream;

	}

	private void addPageDetailHead(Document document, PdfWriter writer, String OBJID_TEXT, String DATA)
			throws Exception {
		// 新建页
		document.newPage();

		// lineHeight = lineHeight2;
		this.isBorder = false;

		Paragraph paragraph_0 = new Paragraph("牛栏山酒厂" + OBJID_TEXT + "工资表", font15);
		paragraph_0.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph_0);

		PdfPTable titleTable = new PdfPTable(2);

		// titleTable.setSpacingBefore(25);//段前间距
		titleTable.setWidths(new float[] { 54, 46 });
		titleTable.setWidthPercentage(90);
		// 11.75

		// this.font = font18;
		this.isBorder = false;
		font = font7_5;
		PdfPCell pdfPCell = this.getPdfPCellLeft(" " + DATA.substring(0, 4) + " 年 "
				+ (DATA.substring(5, 7).startsWith("0") ? " " + DATA.substring(6, 7) : DATA.substring(5, 7)) + " 月 ");
		pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPCell.setFixedHeight((float) 1.75);
		titleTable.addCell(pdfPCell);

		pdfPCell = this.getPdfPCellLeft("单位:元               ");
		pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

		titleTable.addCell(pdfPCell);

		document.add(titleTable);

		// document.add(new Paragraph(" ", fontSpace));

		/************************************************/
		this.isBorder = true;
		document.add(new Paragraph(" ", fontSpace));
		document.add(new Paragraph(" ", fontSpace));

		// PdfPTable headerTable = new PdfPTable(21);
		PdfPTable headerTable = new PdfPTable(10);
		float width_0 = Float.valueOf("8");
		float width_1 = Float.valueOf("3.75");
		double[] arr = { 0, 6.5, 5.50, 5.75, 5.75, 3.63, 4.13, 4.88, 3.88, 6.88, 3.75, 5.50, 7.00, 4.13, 6.25, 2.75,
				4.88, 7.25, 7.13, 6.25, 7.38, 7.00, 7.00 };

		headerTable.setWidths(new float[] { (float) arr[1], (float) arr[2],
				(float) (arr[3] + arr[4] + arr[5] + arr[6] + arr[7] + arr[8] + arr[9] + arr[10] + arr[10] + arr[10]
						+ arr[11] + arr[11] + arr[12]),
				(float) (arr[13] + arr[14] + arr[15] + arr[16]), (float) arr[17], (float) arr[18], (float) arr[19],
				(float) arr[20], (float) arr[21], (float) arr[22] });

		headerTable.setWidthPercentage(widthPercentage);
		this.isBorder = true;
		font = font6_5_bold;

		lineHeight = (float) 13.75;
		this.isBorder = true;

		headerTable.setWidthPercentage(widthPercentage);

		headerTable.addCell(this.getPdfPCell("帐号"));

		headerTable.addCell(this.getPdfPCell("姓名"));

		PdfPTable iTable = new PdfPTable(13);
		PdfPCell iCell = this.getPdfPCell("应付工资");
		iTable.setWidths(new float[] { (float) arr[3], (float) arr[4], (float) arr[5], (float) arr[6], (float) arr[7],
				(float) arr[8], (float) arr[9], (float) arr[10], (float) arr[10], (float) arr[10], (float) arr[11],
				(float) arr[11], (float) arr[12] });
		// x
		iCell.setColspan(13);
		iTable.addCell(iCell);
		iTable.addCell(this.getPdfPCell("岗位工资"));
		iTable.addCell(this.getPdfPCell("效益工资"));
		iTable.addCell(this.getPdfPCell("值班"));
		iTable.addCell(this.getPdfPCell("加班"));
		iTable.addCell(this.getPdfPCell("其他补款"));

		iTable.addCell(this.getPdfPCell("扣款"));

		iTable.addCell(this.getPdfPCell("工资小计"));

		PdfPCell pdfPCell_glyf = this.getPdfPCell("工龄  药费");
		pdfPCell_glyf.setPaddingTop((float) 6.0);
		pdfPCell_glyf.setVerticalAlignment(Element.ALIGN_TOP);
		iTable.addCell(pdfPCell_glyf);

		PdfPCell pdfPCell_xlbt = this.getPdfPCell("学历  补贴");
		pdfPCell_xlbt.setPaddingTop((float) 6.0);
		pdfPCell_xlbt.setVerticalAlignment(Element.ALIGN_TOP);
		iTable.addCell(pdfPCell_xlbt);

		iTable.addCell(this.getPdfPCell("其他"));

		iTable.addCell(this.getPdfPCell("手机费"));

		iTable.addCell(this.getPdfPCell("岗位津贴"));

		iTable.addCell(this.getPdfPCell("应付工资小计"));
		// headerTable.addCell(this.getPdfPCell(""));

		PdfPCell cell = new PdfPCell(iTable);
		cell.setPadding(0);
		headerTable.addCell(cell);

		PdfPTable iTable_1 = new PdfPTable(3);
		iTable_1.setWidths(new float[] { (float) arr[13], (float) (arr[14] + arr[15]), (float) arr[16] });
		PdfPCell iCell_1 = this.getPdfPCell("应扣工资");
		iCell_1.setColspan(3);
		iTable_1.addCell(iCell_1);
		PdfPCell pdfPCell2 = this.getPdfPCell("养老及失业保险6.5%");
		pdfPCell2.setPaddingTop((float) 2.5);
		pdfPCell2.setVerticalAlignment(Element.ALIGN_TOP);
		iTable_1.addCell(pdfPCell2);

		PdfPTable iTable_2 = new PdfPTable(2);
		iTable_2.setWidths(new float[] { (float) arr[14], (float) arr[15] });
		PdfPCell iCell_2 = this.getPdfPCell("个人大病统筹");
		iCell_2.setColspan(2);
		iTable_2.addCell(iCell_2);
		iTable_2.addCell(this.getPdfPCell("2%基金"));
		iTable_2.addCell(this.getPdfPCell("3元"));

		PdfPCell cell_2 = new PdfPCell(iTable_2);
		cell_2.setPadding(0);
		iTable_1.addCell(cell_2);

		iTable_1.addCell(this.getPdfPCell("住房基金"));
		PdfPCell cell_1 = new PdfPCell(iTable_1);
		cell_1.setPadding(0);
		headerTable.addCell(cell_1);

		headerTable.addCell(this.getPdfPCell("应付工资     合计"));
		headerTable.addCell(this.getPdfPCell("应税工资     合计"));
		headerTable.addCell(this.getPdfPCell("扣个人所得税"));
		headerTable.addCell(this.getPdfPCell("税后扣款"));
//		headerTable.addCell(this.getPdfPCell("实际应缴党费"));
		headerTable.addCell(this.getPdfPCell("实付工资"));

		headerTable.addCell(this.getPdfPCell("签字"));

		document.add(headerTable);

	}

	private void addPageDetailList(Document document, JSONArray jsonarray, int begin, int end, int size,
			int size_keyset) throws Exception {
		float lineHeight2 = (float) 13.5;
		this.isBorder = true;
		font = font11;
		float width_0 = Float.valueOf("8");
		float width_1 = Float.valueOf("3.75");

		double[] arr = { 0, 6.5, 5.50, 5.75, 5.75, 3.63, 4.13, 4.88, 3.88, 6.88, 3.75, 5.50, 7.00, 4.13, 6.25, 2.75,
				4.88, 7.25, 7.13, 6.25, 7.38, 7.00, 7.00 };

		PdfPTable headerTable = new PdfPTable(10);
		headerTable.setWidths(new float[] { (float) arr[1], (float) arr[2],
				(float) (arr[3] + arr[4] + arr[5] + arr[6] + arr[7] + arr[8] + arr[9] + arr[10] + arr[10] + arr[10]
						+ arr[11] + arr[11] + arr[12]),
				(float) (arr[13] + arr[14] + arr[15] + arr[16]), (float) arr[17], (float) arr[18], (float) arr[19],
				(float) arr[20], (float) arr[21], (float) arr[22] });

		headerTable.setWidthPercentage(widthPercentage);
		this.isBorder = true;

		headerTable.setWidthPercentage(widthPercentage);
		for (int k = begin; k < end; k++) {
			JSONObject jsonObject = jsonarray.getJSONObject(k);
			font = font5_5;
			float lineHeight1 = (float) 15.5;

			lineHeight = lineHeight1;
			headerTable.addCell(this.getPdfPCell(jsonObject.getString("BKREF")));
			font = font8;
			headerTable.addCell(this.getPdfPCell(jsonObject.getString("NACHN")));

			PdfPTable iTable = new PdfPTable(13);
			iTable.setWidths(new float[] { (float) arr[3], (float) arr[4], (float) arr[5], (float) arr[6],
					(float) arr[7], (float) arr[8], (float) arr[9], (float) arr[10], (float) arr[10], (float) arr[10],
					(float) arr[11], (float) arr[11], (float) arr[12] });
			// x
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET1100")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET1500")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3004")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3003")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3098")));

			iTable.addCell(this.getPdfPCell(jsonObject.getString("KK")));// 扣款

			iTable.addCell(this.getPdfPCell(jsonObject.getString("BETGZXJ")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3002")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3009")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3055")));
			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3034")));

			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3035")));
			// iTable.addCell(this.getPdfPCell(jsonObject.getString("BET3098")));

			iTable.addCell(this.getPdfPCell(jsonObject.getString("BET101")));

			PdfPCell cell = new PdfPCell(iTable);
			cell.setPadding(0);
			headerTable.addCell(cell);

			PdfPTable iTable_1 = new PdfPTable(3);

			iTable_1.setWidths(new float[] { (float) arr[13], (float) (arr[14] + arr[15]), (float) arr[16] });
			PdfPCell pdfPCell2 = this.getPdfPCell(jsonObject.getString("BETYLSY"));
			iTable_1.addCell(pdfPCell2);

			PdfPTable iTable_2 = new PdfPTable(2);
			iTable_2.setWidths(new float[] { (float) arr[14], (float) arr[15] });
			iTable_2.addCell(this.getPdfPCell(jsonObject.getString("BET333")));
			iTable_2.addCell(this.getPdfPCell(jsonObject.getString("BET2071")));

			PdfPCell cell_2 = new PdfPCell(iTable_2);
			cell_2.setPadding(0);
			iTable_1.addCell(cell_2);

			iTable_1.addCell(this.getPdfPCell(jsonObject.getString("BET362")));
			PdfPCell cell_1 = new PdfPCell(iTable_1);
			cell_1.setPadding(0);
			headerTable.addCell(cell_1);

			headerTable.addCell(this.getPdfPCell(jsonObject.getString("BETYFHJ")));
			headerTable.addCell(this.getPdfPCell(jsonObject.getString("BET401")));
			headerTable.addCell(this.getPdfPCell(jsonObject.getString("BET403")));
			headerTable.addCell(this.getPdfPCell(jsonObject.getString("BET4099")));
//			headerTable.addCell(this
//					.getPdfPCell(jsonObject.getString("BET4003")));

			headerTable.addCell(this.getPdfPCell(jsonObject.getString("BET559")));
			headerTable.addCell(this.getPdfPCell(""));

		}

		JSONObject obj_hj = new JSONObject();

		String[] split = { "KK", "BET1100", "BET1500", "BET3004", "BET3003", "BET3002", "BET3009", "BET3055", "BET3034",
				"BET3098", "BET4009", "BET4010", "BET4013", "BET4096", "BET101", "BETYLSY", "BET333", "BET2071",
				"BET362", "BETYFHJ", "BET313", "BET323", "BET2098", "BET4098", "BET3035", "BET401", "BET403", "BET559",
				"BETGZXJ", "BET4099", "BET4003" };
		int length = split.length;
		// length=18;

		font = font7_bold;

		if (end == size) {
			obj_hj = new JSONObject();
			String str = "";
			for (int j = 0; j < length; j++) {
				str = split[j];
				obj_hj.put(str, Double.valueOf(0));
			}
			int rs = 0;
			for (int k = 0; k < size; k++) {

				rs++;
				for (int j = 0; j < length; j++) {
					JSONObject jsonObj = jsonarray.getJSONObject(k);
					str = split[j];
					if (((String) jsonObj.getString("BKREF")).contains("小计")) {

					} else {
						if (jsonObj.containsKey(str)) {
							obj_hj.put(str,
									Double.valueOf(obj_hj.getString(str)) + Double.valueOf(jsonObj.getString(str)));
						} else {
						}
					}
				}
			}

			PdfPCell pdfPCell = this.getPdfPCell("合计");
			headerTable.addCell(pdfPCell);//

			font = font8;
			pdfPCell = this.getPdfPCell((rs - size_keyset) + "");
			headerTable.addCell(pdfPCell);//

			for (int j = 0; j < length; j++) {

				BigDecimal b = new BigDecimal(obj_hj.getDouble(split[j]));
				b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
				obj_hj.put(split[j], b.stripTrailingZeros().toPlainString());
				if (obj_hj.getString(split[j]).equals("0.00")) {
					obj_hj.put(split[j], "0");
				}
			}

			PdfPTable iTable = new PdfPTable(13);
			iTable.setWidths(new float[] { (float) arr[3], (float) arr[4], (float) arr[5], (float) arr[6],
					(float) arr[7], (float) arr[8], (float) arr[9], (float) arr[10], (float) arr[10], (float) arr[10],
					(float) arr[11], (float) arr[11], (float) arr[12] });
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET1100")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET1500")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3004")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3003")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3098")));

			iTable.addCell(this.getPdfPCell(obj_hj.getString("KK")));// 扣款

			iTable.addCell(this.getPdfPCell(obj_hj.getString("BETGZXJ")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3002")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3009")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3055")));
			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3034")));

			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3035")));
			// iTable.addCell(this.getPdfPCell(obj_hj.getString("BET3098")));

			iTable.addCell(this.getPdfPCell(obj_hj.getString("BET101")));

			PdfPCell cell = new PdfPCell(iTable);
			cell.setPadding(0);
			headerTable.addCell(cell);

			PdfPTable iTable_1 = new PdfPTable(3);
			iTable_1.setWidths(new float[] { (float) arr[13], (float) (arr[14] + arr[15]), (float) arr[16] });
			// PdfPCell iCell_1 = this.getPdfPCell("应扣工资");
			// iCell_1.setColspan(3);
			// iTable_1.addCell(iCell_1);
			PdfPCell pdfPCell2 = this.getPdfPCell(obj_hj.getString("BETYLSY"));
			// pdfPCell2.setVerticalAlignment(Element.ALIGN_TOP);
			iTable_1.addCell(pdfPCell2);

			PdfPTable iTable_2 = new PdfPTable(2);
			iTable_2.setWidths(new float[] { (float) arr[14], (float) arr[15] });
			// PdfPCell iCell_2 = this.getPdfPCell("个人大病统筹");
			// iCell_2.setColspan(2);
			// iTable_2.addCell(iCell_2);
			// obj_hj.getString("")
			// obj_hj.getString("")
			iTable_2.addCell(this.getPdfPCell(obj_hj.getString("BET333")));
			iTable_2.addCell(this.getPdfPCell(obj_hj.getString("BET2071")));

			PdfPCell cell_2 = new PdfPCell(iTable_2);
			cell_2.setPadding(0);
			iTable_1.addCell(cell_2);

			iTable_1.addCell(this.getPdfPCell(obj_hj.getString("BET362")));
			PdfPCell cell_1 = new PdfPCell(iTable_1);
			cell_1.setPadding(0);
			headerTable.addCell(cell_1);

			headerTable.addCell(this.getPdfPCell(obj_hj.getString("BETYFHJ")));

			headerTable.addCell(this.getPdfPCell(obj_hj.getString("BET401")));
			headerTable.addCell(this.getPdfPCell(obj_hj.getString("BET403")));
			headerTable.addCell(this.getPdfPCell(obj_hj.getString("BET4099")));
//			headerTable.addCell(this.getPdfPCell(obj_hj.getString("BET4003")));
			headerTable.addCell(this.getPdfPCell(obj_hj.getString("BET559")));

			pdfPCell = this.getPdfPCell("");
			headerTable.addCell(pdfPCell);//

		}
		document.add(headerTable);

	}

	public PdfPCell getPdfPCell(String cellName) {
		if (font == null) {
			font = FontChinese;
		}
		PdfPCell pdfPCell = MyPdfCell.getMyPdfCell(new Paragraph(cellName, font));
		if (isHeight) {
			pdfPCell.setMinimumHeight(25f);
		}
		if (isCenter) {
			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		}
		if (!isBorder) {
			// pdfPCell.setBorderWidth(arg0);
			pdfPCell.setBorder(0);
		}
		pdfPCell.setFixedHeight(lineHeight);
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pdfPCell.setPadding(0);
		return pdfPCell;
	}

	public PdfPCell getPdfPCellLeft(String cellName) throws DocumentException, IOException {
		if (font == null) {
			font = FontChinese;
		}
		PdfPCell pdfPCell = MyPdfCell.getMyPdfCell(new Paragraph(cellName, font));
		if (isHeight) {
			pdfPCell.setMinimumHeight(25f);
		}
		pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		if (!isBorder) {
			pdfPCell.setBorder(0);
		}
		pdfPCell.setPadding(0);
		return pdfPCell;
	}

}
