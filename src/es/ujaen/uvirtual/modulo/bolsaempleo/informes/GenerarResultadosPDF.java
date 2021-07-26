package es.ujaen.uvirtual.modulo.bolsaempleo.informes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para generar pdfs de resultados .
 * 
 * @author ATISoluciones
 */
public class GenerarResultadosPDF extends BolsaEmpleoPDFGenerator {
	
	private static final String NOMBREDEESTACLASE = GenerarResultadosPDF.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String PDF_NOMBRE = "RS%sC%s U%s";
	private static final String PDF_TITULO = "Informe de baremación del área: %s";
	
	
	/** Generar PDF de los resultados de una solicitud para una bolsa .
	 * @param solicitud .
	 * @param bolsa .
	 * @param fechaActual .
	 * @return InputStream .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public static InputStream generarPDF(UsuarioBolsaEmpleo candidato, BolsaResultado bolsa, Convocatoria convocatoria) throws UVException, SQLException {
		initPDFProperties();
		
		MeritoPreferente meritoPreferente = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteTipoMerito();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try (Document document = new Document()) {
			PdfWriter.getInstance(document, out);
			
			document.open();
			document.addAuthor(PDF_AUTHOR);
			document.addTitle(String.format(PDF_NOMBRE, candidato.getCodNum(), convocatoria.getCodNum(), candidato.getPrsNif()));
			document.addCreationDate();
			
			document.add(new Paragraph(new Chunk(String.format(PDF_TITULO, bolsa.getArea().getDescripcion()), fontTitle)));
			
			document.add(new Paragraph("\n"));
			
			document.add(new Paragraph("Convocatoria: " + convocatoria.getDescripcion(), fontBold));
			document.add(new Paragraph("Actualizado a fecha de: "
					+ Formateador.formatoFecha(bolsa.getFechaBaremacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS), fontBold));
			
			document.add(new Paragraph("Usuario: "
					+ Formateador.leeParametroString(candidato.getPrsNif()) + " "
					+ Formateador.leeParametroString(candidato.getNombre()) + " "
					+ Formateador.leeParametroString(candidato.getPrimerApellido()) + " "
					+ Formateador.leeParametroString(candidato.getSegundoApellido()), fontBold));
			
			document.add(new Paragraph("Dirección: "
					+ Formateador.leeParametroString(candidato.getDireccion()) + " "
					+ Formateador.leeParametroString(candidato.getCodigoPostal()) + " "
					+ Formateador.leeParametroString(candidato.getLocalidad()) + " "
					+ Formateador.leeParametroString(candidato.getProvincia()) + " "
					+ Formateador.leeParametroString(candidato.getTelefono()), fontBold));
			
			document.add(new Paragraph("\n"));
			
			document.add(new Paragraph("Leyenda del campo \"Desglose\":", fontBold));
			
			com.lowagie.text.List lista = new com.lowagie.text.List();
			lista.setListSymbol("• ");
			lista.add("Méritos desagregables: (D) (valor1 * afinidad1 + valor2 * afinidad2 + valor3 * afinidad3 + valor4 * afinidad4)"
					+ " * Valor unitario * Peso Bloque");
			lista.add("Méritos con bonificación por bloque "
					+ meritoPreferente.getAplicableApartadoBaremacion().getCodigo() + " - "
					+ meritoPreferente.getAplicableApartadoBaremacion().getNombre() + ":"
					+ " Valor * Afinidad * Valor unitario * Peso Bloque * (" + meritoPreferente.getPrefijoInforme() + ") Factor Mérito Preferente");
			lista.add("Resto de méritos: Valor * Afinidad * Valor unitario * Peso Bloque");

			document.add(lista);
			
			generarMeritosEvaluadosPDF(bolsa, document);
			generarMeritosExcluidosPDF(bolsa, document);
			generarMeritosNoEvaluadosPDF(bolsa, document);
			
			document.add(new Paragraph("\n"));
			
			generarTitulacionesValidadas(bolsa, document);
			generarAcreditacionesValidadas(bolsa, document);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			throw new UVException(MENSAJE_ERROR_GENERANDO_PDF);
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private static void generarMeritosEvaluadosPDF(BolsaResultado bolsa, Document document) {
		document.add(new Paragraph("\n"));
		document.add(new Paragraph(ModeloResultados.MERITOS_VALIDADOS, fontBold));
		
		if (bolsa.getListaMeritos().size() > 0) {
			LinkedHashMap<String, Float> headerColumns = new LinkedHashMap<String, Float>();
			headerColumns.put("Id. Mérito", (float) SIZE_10);
			headerColumns.put("Cod. Mérito", (float) SIZE_12);
			headerColumns.put("Tipo de Mérito", (float) SIZE_20);
			headerColumns.put("Desglose", (float) SIZE_16);
			headerColumns.put("Resultado", (float) SIZE_11);
			headerColumns.put("Observación", (float) SIZE_16);
			
			Table table = generarTable(PDF_TABLE_COLUMNS_6, bolsa.getListaMeritos().size());
			generarTableHeader(table, headerColumns);
			
			for (MeritoResultado merito: bolsa.getListaMeritos()) {
				String codigoItem = merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
						+ merito.getItemBaremacion().getBloqueBaremacion().getCodigo() + "." 
						+ merito.getItemBaremacion().getCodigo();
				
				generarTableRow(table, new String[] {
						merito.getCodNum().toString(),
						codigoItem,
						merito.getItemBaremacion().getNombre(),
						merito.getDesglose(),
						merito.getResultado().toString(),
						merito.getObservacionCandidato(),
					}
				);
			}
			
			Table table2 = generarTable(PDF_TABLE_COLUMNS_2, bolsa.getDesgloseTotal() != null ? PDF_TABLE_ROWS_3 : PDF_TABLE_ROWS_2);
			table2.setWidths(new float[] {SIZE_69, SIZE_16});
			
			generarTableRow(table2, new String[] {
					"Total sin aplicar el máximo valor de las titulaciones preferentes y acreditaciones:",
					bolsa.getTotalSinAplicar().toString(),
				}
			);
			
			if (bolsa.getDesgloseTotal() != null) {
				generarTableRow(table2, new String[] {
						"Cálculo final: " + bolsa.getDesgloseDescripcion(),
						bolsa.getDesgloseTotal(),
					}
				);
			}
			
			generarTableRow(table2, new String[] {
					"Total: ",
					bolsa.getTotal().toString(),
				}
			);
			
			document.add(table);
			document.add(table2);
		} else {
			document.add(new Paragraph(ModeloResultados.MENSAJE_SIN_MERITOS_EVALUADOS));
		}
	}
	
	private static void generarMeritosExcluidosPDF(BolsaResultado bolsa, Document document) {
		document.add(new Paragraph("\n"));
		
		document.add(new Paragraph(ModeloResultados.MERITOS_EXCLUIDOS, fontBold));
		
		if (bolsa.getListaMeritosExcluidos().size() > 0) {
			LinkedHashMap<String, Float> headerColumns = new LinkedHashMap<String, Float>();
			
			headerColumns.put("Id. Mérito", (float) SIZE_10);
			headerColumns.put("Cod. Mérito", (float) SIZE_12);
			headerColumns.put("Tipo de Mérito", (float) SIZE_20);
			headerColumns.put("Valor", (float) SIZE_27);
			headerColumns.put("Observación", (float) SIZE_16);
			
			Table table = generarTable(PDF_TABLE_COLUMNS_5, bolsa.getListaMeritos().size());
			
			generarTableHeader(table, headerColumns);
			
			for (MeritoResultado merito: bolsa.getListaMeritosExcluidos()) {
				String codigoItem = merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
						+ merito.getItemBaremacion().getBloqueBaremacion().getCodigo() + "." 
						+ merito.getItemBaremacion().getCodigo();
				
				generarTableRow(table, new String[] {
						merito.getCodNum().toString(),
						codigoItem,
						merito.getItemBaremacion().getNombre(),
						merito.getValorMeritoSolicitud() != null && merito.getValorMeritoSolicitud() > 0 
						? merito.getValorMeritoSolicitud().toString() : merito.getValor().toString(),
						merito.getObservacionCandidato(),
					}
				);
			}
			
			document.add(table);
		} else {
			document.add(new Paragraph(ModeloResultados.MENSAJE_SIN_MERITOS_EXCLUIDOS));
		}
	}
	
	private static void generarMeritosNoEvaluadosPDF(BolsaResultado bolsa, Document document) {
		document.add(new Paragraph("\n"));
		
		document.add(new Paragraph(ModeloResultados.MERITOS_NO_EVALUADOS, fontBold));
		
		if (bolsa.getListaMeritosNoEvaluados().size() > 0) {
			LinkedHashMap<String, Float> headerColumns = new LinkedHashMap<String, Float>();
			
			headerColumns.put("Id. Mérito", (float) SIZE_10);
			headerColumns.put("Cod. Mérito", (float) SIZE_12);
			headerColumns.put("Tipo de Mérito", (float) SIZE_20);
			headerColumns.put("Valor", (float) SIZE_27);
			headerColumns.put("Observación", (float) SIZE_16);
			
			Table table = generarTable(PDF_TABLE_COLUMNS_5, bolsa.getListaMeritos().size());
			
			generarTableHeader(table, headerColumns);
			
			for (MeritoResultado merito: bolsa.getListaMeritosNoEvaluados()) {
				String codigoItem = merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
						+ merito.getItemBaremacion().getBloqueBaremacion().getCodigo() + "." 
						+ merito.getItemBaremacion().getCodigo();
				
				generarTableRow(table, new String[] {
						merito.getCodNum().toString(),
						codigoItem,
						merito.getItemBaremacion().getNombre(),
						merito.getValorMeritoSolicitud() != null && merito.getValorMeritoSolicitud() > 0
						? merito.getValorMeritoSolicitud().toString() : merito.getValor().toString(),
						merito.getObservacionCandidato(),
					}
				);
			}
			
			document.add(table);
		} else {
			document.add(new Paragraph(ModeloResultados.MENSAJE_SIN_MERITOS_NO_EVALUADOS));
		}
	}
	
	private static void generarTitulacionesValidadas(BolsaResultado bolsa, Document document) throws DocumentException, SQLException, UVException, IOException {		
		document.add(new Paragraph("\n"));
		
		document.add(new Paragraph("Titulaciones validadas", fontBold));
		
		String titulaciones = BolsaEmpleoUtils.clobToString(bolsa.getTitulacionesValidadas());
		document.add(new Paragraph(titulaciones.isEmpty() ? "No hay titulaciones validadas" : titulaciones));
	}
	
	private static void generarAcreditacionesValidadas(BolsaResultado bolsa, Document document) throws DocumentException, SQLException, UVException, IOException {
		document.add(new Paragraph("\n"));
		
		document.add(new Paragraph("Acreditaciones validadas", fontBold));
		
		String acreditaciones = BolsaEmpleoUtils.clobToString(bolsa.getAcreditacionesValidadas());
		document.add(new Paragraph(acreditaciones.isEmpty() ? "No hay acreditaciones validadas" : acreditaciones));
	}
	
}
