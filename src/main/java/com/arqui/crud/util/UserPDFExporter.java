package com.arqui.crud.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.arqui.crud.entity.User;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Phrase;
import java.awt.Color;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase utilitaria encargada de la generación de reportes en formato PDF.
 * <p>
 * Esta clase utiliza la librería <b>OpenPDF</b> (basada en iText) para construir
 * un documento visualmente estilizado con formato corporativo.
 * </p>
 * * <b>Características del diseño</b>
 * <ul>
 * <li>Paleta de colores personalizada (Azul oscuro, Azul brillante, Gris suave).</li>
 * <li>Tabla con anchos de columna dinámicos.</li>
 * <li>Filas con colores alternados ("Zebra Striping") para facilitar la lectura.</li>
 * </ul>
 * @author Braulio Cuevas
 * @author Mauricio Dzay
 * @author Jareth Moo
 * @version 2.0
 */
public class UserPDFExporter {
    /** Lista de usuarios que serán renderizados en la tabla del PDF. */
    private List<User> listUsers;

    /** Color para el título principal (Azul oscuro). */
    private static final Color DARK_BLUE = new Color(44, 62, 80);
    /** Color de fondo para los encabezados de la tabla (Azul brillante). */
    private static final Color HEADER_BG = new Color(52, 152, 219);
    /** Color de fondo para las filas pares de la tabla (Gris muy claro). */
    private static final Color LIGHT_GRAY = new Color(240, 240, 240);

    /**
     * Constructor de la clase exportadora.
     *
     * @param listUsers La lista de objetos {@link User} recuperada de la base de datos
     * que se desea imprimir en el reporte.
     */
    public UserPDFExporter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    /**
     * Método principal que orquesta la creación y escritura del documento PDF.
     * <p>
     * Define la estructura del documento en el siguiente orden:
     * </p>
     * <ol>
     * <li>Título del reporte (Centrado y grande).</li>
     * <li>Fecha y hora de generación (Subtítulo gris).</li>
     * <li>Tabla de datos con 5 columnas y anchos relativos definidos.</li>
     * </ol>
     * <p>
     * Finalmente, escribe los bytes resultantes directamente en el flujo de salida de la respuesta HTTP.
     * </p>
     *
     * @param response Objeto {@link HttpServletResponse} donde se escribirá el archivo PDF.
     * @throws IOException Si ocurre un error al escribir en el Output Stream.
     */
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        fontTitle.setColor(DARK_BLUE);

        Paragraph paragraph = new Paragraph("Listado de Usuarios", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA);
        fontDate.setSize(10);
        fontDate.setColor(Color.GRAY);

        Paragraph dateInfo = new Paragraph("Generado el: " + currentDateTime, fontDate);
        dateInfo.setAlignment(Paragraph.ALIGN_CENTER);
        dateInfo.setSpacingAfter(30);
        document.add(dateInfo);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 3.0f, 3.5f, 2.5f, 1.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }

    /**
     * Construye la fila de encabezados de la tabla.
     * <p>
     * Aplica el color de fondo {@code HEADER_BG}, texto blanco y bordes blancos
     * para un aspecto moderno y limpio.
     * </p>
     *
     * @param table La tabla PDF a la que se agregarán las celdas de encabezado.
     */
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(HEADER_BG);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(Color.WHITE);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(java.awt.Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Nombre", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Teléfono", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Edad", font));
        table.addCell(cell);
    }

    /**
     * Itera sobre la lista de usuarios y llena la tabla con los datos.
     * <p>
     * Implementa lógica de <b>Zebra Striping</b>: alterna el color de fondo de las filas
     * entre blanco y {@code LIGHT_GRAY} usando una variable booleana {@code isGray}.
     * </p>
     *
     * @param table La tabla PDF donde se insertarán las filas de datos.
     */
    private void writeTableData(PdfPTable table) {
        Font fontData = FontFactory.getFont(FontFactory.HELVETICA);
        fontData.setSize(11);
        fontData.setColor(Color.BLACK);

        boolean isGray = false; // Variable para alternar colores

        for (User user : listUsers) {
            Color bgColor = isGray ? LIGHT_GRAY : Color.WHITE;
            isGray = !isGray;

            addDataCell(table, String.valueOf(user.getId()), bgColor, fontData, Element.ALIGN_CENTER);
            addDataCell(table, user.getName(), bgColor, fontData, Element.ALIGN_LEFT);
            addDataCell(table, user.getEmail(), bgColor, fontData, Element.ALIGN_LEFT);
            addDataCell(table, user.getPhone(), bgColor, fontData, Element.ALIGN_CENTER);
            
            String ageStr = (user.getAge() != null) ? String.valueOf(user.getAge()) : "-";
            addDataCell(table, ageStr, bgColor, fontData, Element.ALIGN_CENTER);
        }
    }

    /**
     * Método auxiliar para crear y añadir una celda de datos con estilo específico.
     * <p>
     * Centraliza la configuración de padding, bordes y alineación vertical para evitar
     * código repetitivo en el bucle principal.
     * </p>
     *
     * @param table     La tabla destino.
     * @param text      El contenido textual de la celda.
     * @param bgColor   El color de fondo (calculado dinámicamente para el efecto cebra).
     * @param font      La fuente a utilizar.
     * @param alignment La alineación horizontal (Izquierda, Centro, Derecha).
     */
    private void addDataCell(PdfPTable table, String text, Color bgColor, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(6); 
        cell.setHorizontalAlignment(alignment); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(new Color(220, 220, 220)); 
        table.addCell(cell);
    }
}