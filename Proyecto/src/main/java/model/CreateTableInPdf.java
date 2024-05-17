package model;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;

public class CreateTableInPdf {
    public void generatePdfs(String nombre, ObservableList<Task> tasks){
        String dest = nombre+".pdf";
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("payrolls/"+dest));
            document.open();

            Paragraph titulo = new Paragraph(nombre);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);

            float[] columnWidths = {4, 2, 2, 2};
            table.setWidths(columnWidths);

            table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph("Categoría")));
            table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph("Tiempo")));
            table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph("Dinero")));
            table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph("Total")));

            for(Task task : tasks){
                table.addCell(task.getCategoria());
                table.addCell(task.getTiempo()+"");
                table.addCell("80€/h");
                table.addCell(task.getTiempo()*80+"€");
            }
            document.add(table);
            document.close();
            System.out.println("Table PDF created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}