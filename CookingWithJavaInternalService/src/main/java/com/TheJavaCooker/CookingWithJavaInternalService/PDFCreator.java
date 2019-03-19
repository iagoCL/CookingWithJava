package com.TheJavaCooker.CookingWithJavaInternalService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class PDFCreator {
    // Método para crear el PDF
    public static byte[] createPDF(List<String> args) {
        try {
            int pos = 0;

            String nombreReceta = args.get(pos++);
            String tipo = args.get(pos++);
            String duracion = args.get(pos++);
            String nombre_creador = args.get(pos++);
            int nIngredientes = Integer.parseInt(args.get(pos++));
            int nUtensilios = Integer.parseInt(args.get(pos++));
            int nPasos = Integer.parseInt(args.get(pos++));
            List<String> ingredientes = new ArrayList<>(nIngredientes);
            List<String> utensilios = new ArrayList<>(nUtensilios);
            List<String> pasos = new ArrayList<>(nPasos);

            for (int i=0; i<nIngredientes; i++) {
                ingredientes.add(args.get(pos++));
            }
            for (int i=0; i<nUtensilios; i++) {
                utensilios.add(args.get(pos++));
            }
            for (int i=0; i<nPasos; i++) {
                pasos.add(args.get(pos++));
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
