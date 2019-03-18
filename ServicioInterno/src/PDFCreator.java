import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PDFCreator {
    // Método para crear el PDF
    public static byte[] createPDF(String nombreReceta, String paso1, String paso2) {

        byte[] pdf = null;

        try {
            String fileName = nombreReceta + ".pdf";

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
            content.drawString("Paso 1: " + paso1);
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.moveTextPositionByAmount(10, 650);
            content.drawString("Paso 2: " + paso2);
            content.endText();

            content.close();
            doc.save(fileName);
            doc.close();

            String location = System.getProperty("user.dir") + "\\" + nombreReceta + ".pdf";
            Path pdfPath = Paths.get(location);
            pdf = Files.readAllBytes(pdfPath);
            System.out.println("File created in: " + location);
        } catch(IOException | COSVisitorException e) {
            System.out.println(e.getMessage());
        }

        return pdf;
    }
}
