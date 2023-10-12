package com.example.itextpdf5andicepdfviewertest;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.io.OutputStream;

public class Temp {
//            pdfWriter.setPageEvent(new CustomPageEventHelper());
//            pagePortraitFooter(outputStream.toByteArray(), outputStream, pdfWriter.getPageSize().getWidth() - 35f, 15f);




    public void pagePortraitFooter(byte[] bs, OutputStream os, float x, float y) throws IOException, DocumentException {
//        SQL sql = new SQL(model);

        PdfReader reader = new PdfReader(bs);
        int n = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, os);
        PdfContentByte pageContent;

        for (int i = 0; i < n; ) {
            pageContent = stamper.getOverContent(++i);

            ColumnText.showTextAligned(pageContent,
                    Element.ALIGN_BOTTOM, new Phrase("sql.getDateFormat(LocalDate.now())"),
                    50f, 15f, 0);

//            ImagesPath images = new ImagesPath();
//            PdfContentByte canvas = pageContent;
//            canvas.moveTo(28, 33);
//            canvas.lineTo(570, 33);
//            canvas.closePathStroke();

//            Image image4 = Image.getInstance(images.footerImage);
//            image4.scaleAbsolute(320f, 20f);
//            image4.setAbsolutePosition(150, 10);
//            image4.setAlignment(Element.ALIGN_CENTER);
//            pageContent.addImage(image4);

            ColumnText.showTextAligned(pageContent,
                    Element.ALIGN_RIGHT, new Phrase(String.format("Page %s of %s", i, n)),
                    x, y, 0);
        }
        System.out.println(n);

        stamper.close();
        reader.close();
    }
}

class FooterEvent extends PdfPageEventHelper {
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(String.format("Page %d of", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8));
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, (document.right() + document.left()) / 2, document.bottom() - 10, 0);
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        int totalPageCount = writer.getPageNumber() - 1;
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(String.valueOf(totalPageCount), new Font(Font.FontFamily.HELVETICA, 8)), (document.right() + document.left()) / 2, document.bottom() - 10, 0);
    }
}

class CustomPageEventHelper extends PdfPageEventHelper {
    private int totalPages;

    public void onOpenDocument(PdfWriter writer, Document document) {
        totalPages = 0;
    }

    public void onStartPage(PdfWriter writer, Document document) {
        totalPages++;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("Page " + writer.getPageNumber() + " of " + totalPages,
                new Font(Font.FontFamily.HELVETICA, 8));

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
                (document.right() + document.left()) / 2, document.bottom() - 10, 0);
    }
}
