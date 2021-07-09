package es.ujaen.uvirtual.modulo.bolsaempleo.informes;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;

/**
 * Clase para generar pdfs de la bolsa de empleo.
 * @author ATISoluciones
 */
public class BolsaEmpleoPDFGenerator {
	
	protected static final String PDF_AUTHOR = "UJA";
		
	// pdf properties
	protected static final int PDF_ANCHO = 4;
	protected static final int PDF_ALTO = 4;
	protected static final int PDF_FORMATO = 4;
	protected static final int PDF_TABLE_BORDER_WIDTH = 1;
	protected static final int PDF_TABLE_COLUMNS_2 = 2;
	protected static final int PDF_TABLE_COLUMNS_3 = 3;
	protected static final int PDF_TABLE_COLUMNS_5 = 5;
	protected static final int PDF_TABLE_COLUMNS_6 = 6;
	protected static final int PDF_TABLE_PADDING = 5;
	protected static final int PDF_TABLE_ROWS_2 = 2;
	protected static final int PDF_TABLE_ROWS_3 = 3;
	
	protected static final int SIZE_8 = 8;
	protected static final int SIZE_10 = 10;
	protected static final int SIZE_11 = 11;
	protected static final int SIZE_12 = 12;
	protected static final int SIZE_18 = 18;
	protected static final int SIZE_20 = 20;
	protected static final int SIZE_23 = 23;
	protected static final int SIZE_30 = 30;
	protected static final int SIZE_40 = 40;
	protected static final int SIZE_60 = 60;
	protected static final int SIZE_65 = 65;
	protected static final int SIZE_100 = 100;
	protected static final int SIZE_100_WIDTH = 100;
	
	protected static final int COLOR_51 = 51;
	protected static final int COLOR_112 = 112;
	protected static final int COLOR_153 = 153;
	protected static final int COLOR_185 = 185;
	protected static final int COLOR_201 = 201;
	protected static final int COLOR_241 = 241;
	protected static final int COLOR_254 = 254;
	
	public static final int COLSPAN_5 = 5;
	
	public static final int NUMBER_100 = 100;
	
	protected static Color whiteColor = new Color(0, 0, 0);
	protected static Color lightBlueColor = new Color(COLOR_185, COLOR_201, COLOR_254);
	protected static Color darkBlueColor = new Color(0, COLOR_51, COLOR_153);
	protected static Color cellBackgroundColor = new Color(COLOR_241, COLOR_241, COLOR_241);
	protected static Color lightGreyColor = new Color(COLOR_185, COLOR_185, COLOR_185);
	protected static Color lightGreyColor2 = new Color(COLOR_241, COLOR_241, COLOR_241);
	protected static Color greyColor = new Color(COLOR_112, COLOR_112, COLOR_112);
	
	protected static Font fontBlue = new Font();
	protected static Font fontBold = new Font(Font.BOLD);
	protected static Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA, SIZE_30, Font.BOLDITALIC);
	protected static Font fontTitle2 = FontFactory.getFont(FontFactory.HELVETICA, SIZE_40, Font.BOLDITALIC);
	protected static Font fontH8 = new Font(Font.HELVETICA, SIZE_8);
	protected static Font fontH10 = new Font(Font.HELVETICA, SIZE_10);
	
	// mensajes
	public static final String MENSAJE_ERROR_GENERANDO_PDF = "Error generando pdf, consulte con los administradores";
	
	
	protected static void initPDFProperties() {
		fontBlue.setColor(darkBlueColor);
		fontBold.setStyle("bold");
		fontH10.setColor(new Color(0, COLOR_51, COLOR_153));
	}
	
	protected static Table generarTable(int columns, int rows) {
		Table table = new Table(columns, rows);
		table.setBorderWidth(PDF_TABLE_BORDER_WIDTH);
		table.setBorderColor(whiteColor);
		table.setPadding(PDF_TABLE_PADDING);
		table.setWidth(SIZE_100);
		return table;
	}
	
	protected static void generarTableHeader(Table table, LinkedHashMap<String, Float> columnHeaders) {
		float[] columnWidths = new float[columnHeaders.size()];
		
		int counter = 0;
		for (String title: columnHeaders.keySet()) {
			Phrase phrase = new Phrase(title, fontBlue);
			Cell cell = new Cell(phrase);
			cell.setHeader(true);
			cell.setBackgroundColor(lightBlueColor);
			table.addCell(cell);
			columnWidths[counter] = columnHeaders.get(title);
			counter++;
		}
		
		table.setWidths(columnWidths);
	}
	
	protected static void generarTableHeader(Table table, LinkedHashMap<String, Float> columnHeaders, Font font) {
		float[] columnWidths = new float[columnHeaders.size()];
		
		int counter = 0;
		for (String title: columnHeaders.keySet()) {
			Phrase phrase = new Phrase(title, font);
			Cell cell = new Cell(phrase);
			cell.setHeader(true);
			cell.setBackgroundColor(lightBlueColor);
			table.addCell(cell);
			columnWidths[counter] = columnHeaders.get(title);
			counter++;
		}
		
		table.endHeaders();
		table.setWidths(columnWidths);
	}
	
	protected static void generarTableRow(Table table, String[] columns) {
		for (String column: columns) {
			Cell cell = new Cell(column);
			cell.setBackgroundColor(cellBackgroundColor);
			table.addCell(cell);
		}
	}
	
	protected static void generarTableEncapsulatedRow(Table table, LinkedHashMap<String, LinkedHashMap<String, List<String[]>>> data) {
		for (Entry<String, LinkedHashMap<String, List<String[]>>> apartado: data.entrySet()) {
			Cell cell = new Cell(new Paragraph(apartado.getKey(), fontH8));
			cell.setBackgroundColor(greyColor);
			cell.setColspan(COLSPAN_5);
			cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.addCell(cell);
			
			for (Entry<String, List<String[]>> bloque: apartado.getValue().entrySet()) {
				cell = new Cell(new Paragraph(bloque.getKey(), fontH8));
				cell.setBackgroundColor(lightGreyColor);
				cell.setColspan(COLSPAN_5);
				cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
				table.addCell(cell);
				
				for (String[] item: bloque.getValue()) {
					for (int i = 0; i < item.length; i++) {
						cell = new Cell(new Paragraph(item[i], fontH8));
						cell.setBackgroundColor(lightGreyColor2);
						if (i != 1) {
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
						}
						table.addCell(cell);
					}
				}
			}
		}
	}
	
}
