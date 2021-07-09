package es.ujaen.uvirtual.modulo.bolsaempleo.informes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para generar pdfs de ítems baremación .
 * 
 * @author ATISoluciones
 */
public class GenerarItemsBaremacionPDF extends BolsaEmpleoPDFGenerator {
	
	private static final String NOMBREDEESTACLASE = GenerarResultadosPDF.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String PDF_NOMBRE = "Listado_Items";
	private static final String PDF_TITULO = "Listado de Items";
	
	
	/** Generar PDF de ítems de baremación .
	 * @return InputStream .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public static InputStream generarPDF() throws UVException, SQLException {
		initPDFProperties();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		List<ItemBaremacion> listaItems = modelo.listaItemBaremacion();
		
		try (Document document = new Document()) {
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(PDF_AUTHOR);
			document.addTitle(PDF_NOMBRE);
			document.addCreationDate();

			document.add(new Paragraph(new Chunk(PDF_TITULO, fontTitle2)));
				        
			generarPDFTable(listaItems, document);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			throw new UVException(MENSAJE_ERROR_GENERANDO_PDF + " " + e);
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private static void generarPDFTable(List<ItemBaremacion> listaItems, Document document) throws SQLException, UVException {
		Table table = generarTable(PDF_TABLE_COLUMNS_5, listaItems.size());
		
		LinkedHashMap<String, Float> headerColumns = new LinkedHashMap<String, Float>();
		headerColumns.put("Código", (float) SIZE_10);
		headerColumns.put("Nombre", (float) SIZE_60);
		headerColumns.put("Valor Unitario", (float) SIZE_10);
		headerColumns.put("Afinidad", (float) SIZE_10);
		headerColumns.put("Individualizado", (float) SIZE_10);
		generarTableHeader(table, headerColumns, fontH10);
		
		LinkedHashMap<String, LinkedHashMap<String, List<String[]>>> data = new LinkedHashMap<>();
		
		List<BloqueBaremacion> listaBloques = ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion();
		List<ApartadoBaremacion> listaApartados = ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacionActivosOrdenadosPorCodigo();
		
		for (ApartadoBaremacion apa: listaApartados) {
			String apartado = "Apartado " + apa.getCodigo() + " - " + apa.getNombre();
			LinkedHashMap<String, List<String[]>> apartadoData = new LinkedHashMap<>();
			
			for (BloqueBaremacion bloq: listaBloques) {
				if (bloq.getApartadoBaremacion().getCodNum().equals(apa.getCodNum())) {
					String bloque = "Bloque " + bloq.getCodigo() + " - " + bloq.getNombre();
					List<String[]> items = new ArrayList<String[]>();
					
					for (ItemBaremacion item: listaItems) {
						if (item.getBloqueBaremacion().getCodNum().equals(bloq.getCodNum())) {
							String codigoItem = item.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
									+ item.getBloqueBaremacion().getCodigo() + "." 
									+ item.getCodigo();
							String nombre = item.getNombre();
							if (item.getDescripcion() != null) {
								nombre = item.getNombre() + " ( " + item.getDescripcion() + " )";
							}
							
							items.add(new String[] {
									codigoItem,
									nombre,
									item.getValor().toString(),
									item.getAfinidad() != null ? item.getAfinidad().toString() : item.getAfinidad(),
									item.getIndividualizado() ? "Si" : "No",
								}
							);
						}
					}
					
					apartadoData.put(bloque, items);
				}
			}
			
			data.put(apartado, apartadoData);
		}
		
		generarTableEncapsulatedRow(table, data);
		
		document.add(table);
	}	
}
