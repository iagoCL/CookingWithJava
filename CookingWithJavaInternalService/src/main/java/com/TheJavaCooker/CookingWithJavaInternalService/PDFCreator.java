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
    // Method to create the PDF
    public static byte[] createPDF(String recipeName, String foodType, String totalDuration, String creatorName,
            List<String> ingredientsName, List<String> toolsName, List<String> stepsDescriptions) {
        Document document;
        try {
            // Document creation
            document = new Document(50, 50, 60, 60);
        } catch (Exception exception) {
            PersonalDebug.printMsg("ERROR creating the PDF Document:" + exception.toString());
            return new byte[0];
        }
        try {
            // Header
            Paragraph paragraph = new Paragraph();
            paragraph.addText(recipeName + "\n", 50, PDType1Font.HELVETICA_BOLD);
            paragraph.addText("Created by " + creatorName + "\n", 30, PDType1Font.HELVETICA);
            paragraph.addText(foodType + "\n", 20, PDType1Font.HELVETICA);
            paragraph.addText(totalDuration + "\n", 20, PDType1Font.HELVETICA);
            paragraph.addText("--------------------------------------------------\n", 20, PDType1Font.HELVETICA);
            paragraph.setAlignment(Alignment.Center);
            document.add(paragraph);
        } catch (Exception exception) {
            PersonalDebug.printMsg("ERROR creating the PDF Header:" + exception.toString());
            return new byte[0];
        }
        try {
            // Ingredients on left column
            document.add(new ColumnLayout(2, 5));
            Paragraph left = new Paragraph();
            left.addText("Ingredients:\n", 20, PDType1Font.HELVETICA_BOLD);
            document.add(left);
            document.add(loopPrint(ingredientsName));
        } catch (Exception exception) {
            PersonalDebug.printMsg("ERROR creating the PDF Ingredients:" + exception.toString());
            return new byte[0];
        }
        try {
            // Tools on the right
            document.add(ColumnLayout.NEWCOLUMN);
            Paragraph right = new Paragraph();
            right.addText("Tools:\n", 20, PDType1Font.HELVETICA_BOLD);
            document.add(right);
            document.add(loopPrint(toolsName));
        } catch (Exception exception) {
            PersonalDebug.printMsg("ERROR creating the PDF tools:" + exception.toString());
            return new byte[0];
        }
        try {
            document.add(new VerticalLayout());
            Paragraph paragraph = new Paragraph();
            paragraph.addText("\n--------------------------------------------------\n", 20, PDType1Font.HELVETICA);
            paragraph.setAlignment(Alignment.Center);
            document.add(paragraph);

            // Steps list
            paragraph = new Paragraph();
            paragraph.addText("\nNumber of Steps:\n", 20, PDType1Font.HELVETICA_BOLD);
            document.add(paragraph);
            for (int i = 0; i < stepsDescriptions.size(); i++) {
                Paragraph stepParagraph = new Paragraph();
                stepParagraph.addText("\n" + (i + 1) + ". " + stepsDescriptions.get(i) + "\n", 13,
                        PDType1Font.HELVETICA);
                document.add(stepParagraph);
            }
        } catch (Exception exception) {
            PersonalDebug.printMsg("ERROR creating the PDF steps:" + exception.toString());
            return new byte[0];
        }
        try {
            // Sends the PDF in a byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (Exception exception) {
            PersonalDebug.printMsg("ERROR saving the PDF:" + exception.toString());
            return new byte[0];
        }
    }

    // Print the PDF in a list
    private static Paragraph loopPrint(List<String> list) throws IOException {
        Paragraph paragraph = new Paragraph();
        for (int i = 0; i < list.size(); i++) {
            paragraph.addText("- " + list.get(i) + "\n", 13, PDType1Font.HELVETICA);
        }
        return paragraph;
    }
}