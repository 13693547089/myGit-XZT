package com.faujor.utils.PDF;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;

public class MyPdfCell extends PdfPCell {

	public static PdfPCell getMyPdfCell(Phrase phrase) {
		PdfPCell pdfPCell = new PdfPCell(phrase);
		pdfPCell.setUseAscender(true);
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return pdfPCell;
	}

}