package com.TheJavaCooker.CookingWithJavaInternalService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

            Document document = new Document(50, 50, 60, 60);

            simplePrint(nombreReceta + "\n" + tipo + "\n" + duracion + "\n" + nombre_creador + "\n" , document);

            simplePrint("\n\nIngredientes:\n", document);
            for (int i=0; i<nIngredientes; i++) {
                Paragraph paragraph = new Paragraph();
                paragraph.addText(ingredientes.get(i), 20, PDType1Font.HELVETICA);
                document.add(paragraph);
            }

            simplePrint("\n\nUtensilios:\n", document);
            for (int i=0; i<nUtensilios; i++) {
                Paragraph paragraph = new Paragraph();
                paragraph.addText(utensilios.get(i), 20, PDType1Font.HELVETICA);
                document.add(paragraph);
            }

            simplePrint("\n\nPasos:\n", document);
            for (int i=0; i<nPasos; i++) {
                Paragraph paragraph = new Paragraph();
                paragraph.addText("\n" + i + ":\n" + pasos.get(i) + "\n", 20, PDType1Font.HELVETICA);
                document.add(paragraph);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);

            return out.toByteArray();
        } catch (Exception e) {
            PersonalDebug.imprimir("ERROR creando PDF:" + e.toString());
            return new byte[0];
        }
    }

    private static void simplePrint(String text, Document document) throws IOException {
        Paragraph paragraph = new Paragraph();
        paragraph.addText(text, 20, PDType1Font.HELVETICA);
        document.add(paragraph);
    }
}
