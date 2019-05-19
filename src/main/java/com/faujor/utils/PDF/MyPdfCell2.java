package com.faujor.utils.PDF;

import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;

public class MyPdfCell2 extends PdfPCell {

	public static PdfPCell getMyPdfCell(Phrase phrase) {
		PdfPCell pdfPCell = new PdfPCell(phrase);
		pdfPCell.setUseAscender(true);
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return pdfPCell;
	}

}