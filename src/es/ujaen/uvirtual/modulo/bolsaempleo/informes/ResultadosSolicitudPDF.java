package es.ujaen.uvirtual.modulo.bolsaempleo.informes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para generar pdfs de la bolsa de empleo.
 * 
 * @author ATISoluciones
 */
public class ResultadosSolicitudPDF extends BolsaEmpleoPDFGenerator {
	
	private static final String NOMBREDEESTACLASE = ResultadosSolicitudPDF.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String PDF_NOMBRE = "S%sC%s P%s";
	private static final String PDF_TITULO = "Informe de baremación del área: %s";
	
	
	/** Generar PDF de los resultados de una solicitud para una bolsa .
	 * @param solicitud .
	 * @param bolsa .
	 * @param fechaActual .
	 * @return InputStream .
	 * @throws UVException .
	 */
	public static InputStream generarResultadosSolicitudPDF(Solicitud solicitud, BolsaResultado bolsa, Date fechaActual) throws UVException {
		initPDFProperties();
		
		UsuarioBolsaEmpleo candidato = solicitud.getUsuario();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try (Document document = new Document()) {
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(candidato.getNombre() + " " + candidato.getPrimerApellido() + " " 
					+ candidato.getSegundoApellido());
			document.addTitle(String.format(PDF_NOMBRE, solicitud.getCodNum(), solicitud.getConvocatoria().getCodNum(), candidato.getPrsNif()));
			document.addCreationDate();
			
			document.add(new Paragraph(new Chunk(String.format(PDF_TITULO, bolsa.getArea().getDescripcion()), fontTitle)));
			
			document.add(new Paragraph("\n"));
			
			document.add(new Paragraph("Convocatoria: " + solicitud.getConvocatoria().getDescripcion(), fontBold));
			document.add(new Paragraph("Actualizado a fecha de: "
					+ Formateador.formatoFecha(fechaActual, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS), fontBold));
			
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
			
			generarMeritosEvaluadosPDF(bolsa, document);
			generarMeritosExcluidosPDF(bolsa, document);
			generarMeritosNoEvaluadosPDF(bolsa, document);
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
			headerColumns.put("Desglose", (float) SIZE_12);
			headerColumns.put("Resultado", (float) SIZE_11);
			headerColumns.put("Observación", (float) SIZE_18);
			
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
			table2.setWidths(new float[] {SIZE_65, SIZE_18});
			
			generarTableRow(table2, new String[] {
					"Total sin aplicar el máximo valor de los méritos preferentes y acreditaciones:",
					bolsa.getTotalSinAplicar().toString(),
				}
			);
			
			if (bolsa.getDesgloseTotal() != null) {
				generarTableRow(table2, new String[] {
						bolsa.getDesgloseDescripcion(),
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
			headerColumns.put("Valor", (float) SIZE_23);
			headerColumns.put("Observación", (float) SIZE_18);
			
			Table table = new Table(PDF_TABLE_COLUMNS_5, bolsa.getListaMeritos().size());
			
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
			headerColumns.put("Valor", (float) SIZE_23);
			headerColumns.put("Observación", (float) SIZE_18);
			
			Table table = new Table(PDF_TABLE_COLUMNS_5, bolsa.getListaMeritos().size());
			
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
	
}
