package com.TheJavaCooker.CookingWithJavaInternalService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;


public class PDFCreator {
    // Método para crear el PDF
    public static byte[] createPDF(String[] args) {
        try {
            int pos = 0;

            String nombreReceta = args[0];
            String tipo = args[1];
            String duracion = args[2];
            String nombre_creador = args[3];
            int nIngredientes = 0;
            int nUtensilios = 0;
            int nPasos = 0;
            String[] ingredientes;
            String[] utensilios;
            String[] pasos;

            ingredientes = new String[nIngredientes];
            for (int i=0; i<nIngredientes; i++) {
                ingredientes[i] = args[i+pos];
            }

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();

            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 26);
            content.moveTextPositionByAmount(250, 730);
            content.drawString(nombreReceta);
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.moveTextPositionByAmount(10, 675);
            content.drawString("Paso 1: ");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.moveTextPositionByAmount(10, 650);
            content.drawString("Paso 2: ");
            content.endText();

            content.close();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out);
            doc.close();

            return out.toByteArray();
        } catch (Exception e) {
            PersonalDebug.imprimir("ERROR creando PDF:" + e.toString());
            return new byte[0];
        }
    }
}
