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
    public String generatePdfs(String nombre, ObservableList<Task> tasks,String dateSelected){
        String dest = "payrolls/" + nombre + ".pdf";
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();

            Paragraph titulo = new Paragraph("Payroll of "+nombre+ " in "+dateSelected);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            float[] columnWidths = {2,3, 2, 2, 2};
            table.setWidths(columnWidths);

            table.addCell(new PdfPCell(new Paragraph("Fecha")));
            table.addCell(new PdfPCell(new Paragraph("Categoría")));
            table.addCell(new PdfPCell(new Paragraph("Tiempo")));
            table.addCell(new PdfPCell(new Paragraph("Dinero")));
            table.addCell(new PdfPCell(new Paragraph("Total")));

            for(Task task : tasks){
                table.addCell(task.getFechaInicio());
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
        return dest;
    }
}