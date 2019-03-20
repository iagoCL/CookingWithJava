package com.TheJavaCooker.CookingWithJavaInternalService;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.render.ColumnLayout;
import rst.pdfbox.layout.elements.render.VerticalLayout;
import rst.pdfbox.layout.text.Alignment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PDFCreator {
    // Método para crear el PDF
    public static byte[] createPDF(List<String> args) {
        try {
            // Lectura e interpretación de argumentos
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

            // Creación de documento
            Document document = new Document(50, 50, 60, 60);

            // Cabecera
            Paragraph paragraph = new Paragraph();
            paragraph.addText(nombreReceta + "\n", 50, PDType1Font.HELVETICA_BOLD);
            paragraph.addText("Creado por " + nombre_creador + "\n", 30, PDType1Font.HELVETICA);
            paragraph.addText(tipo + "\n", 20, PDType1Font.HELVETICA);
            paragraph.addText(duracion + "\n", 20, PDType1Font.HELVETICA);
            paragraph.addText("--------------------------------------------------\n", 20, PDType1Font.HELVETICA);
            paragraph.setAlignment(Alignment.Center);
            document.add(paragraph);

            // Ingredientes en columna izquierda
            document.add(new ColumnLayout(2, 5));
            Paragraph left = new Paragraph();
            left.addText("Ingredientes:\n", 20, PDType1Font.HELVETICA_BOLD);
            document.add(left);
            document.add(loopPrint(ingredientes));

            // Utensilios en columna derecha
            document.add(ColumnLayout.NEWCOLUMN);
            Paragraph right = new Paragraph();
            right.addText("Utensilios:\n", 20, PDType1Font.HELVETICA_BOLD);
            document.add(right);
            document.add(loopPrint(utensilios));

            document.add(new VerticalLayout());
            paragraph = new Paragraph();
            paragraph.addText("\n--------------------------------------------------\n", 20, PDType1Font.HELVETICA);
            paragraph.setAlignment(Alignment.Center);
            document.add(paragraph);

            // Lista de pasos
            paragraph = new Paragraph();
            paragraph.addText("\nPasos:\n", 20, PDType1Font.HELVETICA_BOLD);
            document.add(paragraph);
            for (int i=0; i<nPasos; i++) {
                Paragraph parrafoPaso = new Paragraph();
                parrafoPaso.addText("\n" + (i + 1) + ". " + pasos.get(i) + "\n", 13, PDType1Font.HELVETICA);
                document.add(parrafoPaso);
            }

            // Envía el PDF en un array de bytes
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            PersonalDebug.imprimir("ERROR creando PDF:" + e.toString());
            return new byte[0];
        }
    }

    // Imprime en el PDF una lista
    private static Paragraph loopPrint(List<String> lista) throws IOException {
        Paragraph paragraph = new Paragraph();
        for (int i=0; i<lista.size(); i++) {
            paragraph.addText("- " + lista.get(i) + "\n", 13, PDType1Font.HELVETICA);
        }
        return paragraph;
    }
}
