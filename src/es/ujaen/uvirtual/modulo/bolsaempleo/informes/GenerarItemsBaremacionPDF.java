package es.ujaen.uvirtual.modulo.bolsaempleo.informes;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfWriter;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

public class GenerarItemsBaremacionPDF extends BolsaEmpleoPDFGenerator {
	
	private static final String NOMBREDEESTACLASE = GenerarResultadosPDF.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String PDF_NOMBRE = "Listado_Items";
	private static final String PDF_TITULO = "Listado de Items";
	
	
	private InputStream generarPDFItemsBaremacion(Usuario usu) throws UVException, SQLException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		List<ItemBaremacion> listaItems = modelo.listaItemBaremacion();
		
		try (Document document = new Document()) {
			// create a PDF writer instance and pass output stream
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(usu.getApellidosYNombre());
			document.addTitle(PDF_NOMBRE);
			document.addCreationDate();

			document.add(new Paragraph(new Chunk(PDF_TITULO, fontTitle)));
				        
			this.generarPDFTable(listaItems, document);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			throw new UVException(MENSAJE_ERROR_GENERANDO_PDF + " " + e);
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private void generarPDFTable(List<ItemBaremacion> listaItems, Document document) throws SQLException, UVException {
		Table table = new Table(PDF_TABLE_COLUMNS_5, listaItems.size());
		ModeloBaremacionBloques modeloBloque = ModeloBaremacionBloques.obtenerInstancia();
		ModeloBaremacionApartados modeloApartado = ModeloBaremacionApartados.obtenerInstancia();
		List<BloqueBaremacion> listaBloques = modeloBloque.listaBloqueBaremacion();
		List<ApartadoBaremacion> listaApartados = modeloApartado.listaApartadoBaremacionActivosOrdenadosPorCodigo();

		this.generarPDFTableHeader(table);
		
		for (ApartadoBaremacion apa: listaApartados) {
			Cell cell = new Cell(new Paragraph(
					"Apartado " + apa.getCodigo() + " - " + apa.getNombre(), new Font(Font.HELVETICA, SIZE_8)));
			cell.setBackgroundColor(new Color(COLOR_112, COLOR_112, COLOR_112));
			cell.setColspan(COLSPAN_5);
			cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.addCell(cell);
			for (BloqueBaremacion bloq: listaBloques) {
				if (bloq.getApartadoBaremacion().getCodNum().equals(apa.getCodNum())) {
					cell = new Cell(new Paragraph(
							"Bloque " + bloq.getCodigo() + " - " + bloq.getNombre(), new Font(Font.HELVETICA, SIZE_8)));
					cell.setBackgroundColor(new Color(COLOR_185, COLOR_185, COLOR_185));
					cell.setColspan(COLSPAN_5);
					cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
					table.addCell(cell);
					for (ItemBaremacion item: listaItems) {
						if (item.getBloqueBaremacion().getCodNum().equals(bloq.getCodNum())) {
							String codigoItem = item.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
									+ item.getBloqueBaremacion().getCodigo() + "." 
									+ item.getCodigo();
							
							cell = new Cell(new Paragraph(
									codigoItem, new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
							
							String nombre = item.getNombre();
							if (item.getDescripcion() != null) {
								nombre = item.getNombre() + " ( " + item.getDescripcion() + " )";
							}
							
							cell = new Cell(new Paragraph(nombre, new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							table.addCell(cell);
							cell = new Cell(new Paragraph(
									item.getValor().toString(), new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph(item.getAfinidad() != null ? item.getAfinidad().toString() : item.getAfinidad(),
									new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph(item.getIndividualizado() ? "Si" : "No",
									new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
						}
					}
				}
			}
		}
		document.add(table);
	}
	
	private void generarPDFTableHeader(Table table) {
		table.setBorderWidth(1);
		table.setBorderColor(new Color(0, 0, 0));
		table.setPadding(PDF_TABLE_PADDING);
		table.setWidth(SIZE_100_WIDTH);
		table.setWidths(SIZE_100_WIDTHS);

		Font font = new Font(Font.HELVETICA, SIZE_10);
		font.setColor(new Color(0, COLOR_51, COLOR_153));

		Phrase phrase = new Phrase("Código", font);
		Cell cell = new Cell(phrase);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase2 = new Phrase("Nombre", font);
		cell = new Cell(phrase2);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase3 = new Phrase("Valor Unitario", font);
		cell = new Cell(phrase3);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase4 = new Phrase("Afinidad", font);
		cell = new Cell(phrase4);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);
		table.endHeaders();
		
		Phrase phrase5 = new Phrase("Individualizado", font);
		cell = new Cell(phrase5);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);
		table.endHeaders();
	}
}
