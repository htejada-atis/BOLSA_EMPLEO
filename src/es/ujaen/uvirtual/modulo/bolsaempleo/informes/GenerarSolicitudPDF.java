package es.ujaen.uvirtual.modulo.bolsaempleo.informes;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para generar pdfs de solicitudes .
 * 
 * @author ATISoluciones
 */
public class GenerarSolicitudPDF extends BolsaEmpleoPDFGenerator {
	
	private static final String NOMBREDEESTACLASE = GenerarResultadosPDF.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String PDF_NOMBRE = "S%sC%s U%s";
	private static final String PDF_TITULO = "Solicitud";
	
	public static final String MENSAJE_AREA_SIN_MERITOS = "No hay méritos asignados a éste área.";
	
	
	/** Generar PDF de la solicitud .
	 * @param solicitud .
	 * @param bolsasSolicitud .
	 * @return InputStream .
	 * @throws UVException .
	 */
	public static InputStream generarPDF(Solicitud solicitud, List<BolsaSolicitud> bolsasSolicitud) throws UVException {
		initPDFProperties();
		
		UsuarioBolsaEmpleo candidato = solicitud.getUsuario();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try (Document document = new Document()) {
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(PDF_AUTHOR);
			document.addTitle(String.format(PDF_NOMBRE, solicitud.getCodNum(), solicitud.getConvocatoria().getCodNum(), candidato.getPrsNif()));
			document.addCreationDate();

			document.add(new Paragraph(new Chunk(PDF_TITULO, fontTitle)));
			
			document.add(new Paragraph("\n"));
			
			document.add(new Paragraph("Convocatoria: " + solicitud.getConvocatoria().getDescripcion(), fontBold));
			document.add(new Paragraph("Fecha confirmación de solicitud: "
					+ Formateador.formatoFecha(solicitud.getFechaConfirmacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS), fontBold));
			
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
			
			for (BolsaSolicitud bolsa: bolsasSolicitud) {
				generarAreaPDF(bolsa, bolsasSolicitud, document);
			}
			
			generarTitulaciones(solicitud.getUsuario(), document);
			generarAcreditaciones(solicitud.getUsuario(), document);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			throw new UVException(MENSAJE_ERROR_GENERANDO_PDF);
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private static void generarAreaPDF(BolsaSolicitud bolsa, List<BolsaSolicitud> bolsasSolicitud, Document document) {
		LinkedHashMap<String, Float> headerColumns = new LinkedHashMap<String, Float>();
		headerColumns.put("Cod. Mérito", (float) SIZE_10);
		headerColumns.put("Mérito", (float) SIZE_30);
		headerColumns.put("Valor", (float) SIZE_10);
		headerColumns.put("Descripción", (float) SIZE_30);
		headerColumns.put("Afinidad", (float) SIZE_20);
		
		Table table = generarTable(PDF_TABLE_COLUMNS_5, bolsasSolicitud.size());
		generarTableHeader(table, headerColumns);

		if (bolsa.getNumeroMeritos() != null && bolsa.getNumeroMeritos() > 0) {
			for (MeritoSolicitudTable merito: bolsa.getListaMeritos()) {
				String codigoItem = merito.getCodNum() + " " 
						+ merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
						+ merito.getMerito().getItemBaremacion().getBloqueBaremacion().getCodigo() + "." 
						+ merito.getMerito().getItemBaremacion().getCodigo();
				
				String afinidad = "";
				
				if (merito.getCodNum() != null && merito.getMeritoSolicitud() != null && merito.getMerito().getItemBaremacion().getAfinidad() != null
						&& merito.getValoraciones().size() > 0) {
					if (merito.getMerito().getItemBaremacion().getIndividualizado()) {
						afinidad = merito.getValoraciones().get(0).getAfinidad().getCodigo() + " " 
								+ merito.getValoraciones().get(0).getAfinidad().getModulacion() * NUMBER_100 + "%";
					} else {
						for (MeritoSolicitudValoracion valoracion: merito.getValoraciones()) {
							afinidad += valoracion.getValor() + " - " + valoracion.getAfinidad().getCodigo() + " "
									+ valoracion.getAfinidad().getModulacion() * NUMBER_100 + "%\n";
						}
					}
				}
				
				generarTableRow(table, new String[] {
						codigoItem,
						merito.getMerito().getItemBaremacion().getNombre(),
						merito.getMerito().getValor().toString(),
						merito.getMerito().getDescripcion(),
						afinidad,
					}
				);	
			}
		} else {
			Cell cell = new Cell(MENSAJE_AREA_SIN_MERITOS);
			cell.setColspan(COLSPAN_5);
			cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
			table.addCell(cell);
		}
		
		document.add(new Paragraph("Área - " + bolsa.getArea().getDescripcion(), fontBold));
		document.add(table);
		document.add(new Paragraph("\n"));
	}
	
	private static void generarTitulaciones(UsuarioBolsaEmpleo candidato, Document document) throws DocumentException, SQLException, UVException {		
		document.add(new Paragraph("\n"));
		
		ModeloTitulacion modeloTitulacion = ModeloTitulacion.obtenerInstancia();
		List<TitulacionUsuario> listaTitulaciones = modeloTitulacion.listaTitulacionesCandidato(candidato.getCodNum(), false);
		
		document.add(new Paragraph("Titulaciones", fontBold));
		
		if (listaTitulaciones.size() > 0) {
			LinkedHashMap<String, Float> headerColumns = new LinkedHashMap<String, Float>();
			
			headerColumns.put("Id. Titulación", (float) SIZE_10);
			headerColumns.put("Titulación", (float) SIZE_40);
			
			Table table = generarTable(PDF_TABLE_COLUMNS_2, listaTitulaciones.size());
			
			generarTableHeader(table, headerColumns);
			
			for (TitulacionUsuario t: listaTitulaciones) {
				generarTableRow(table, new String[] {
						t.getCodNum().toString(),
						t.getTitulacion() != null ? t.getTitulacion().getNombre() : ("Otra titulación: " + t.getOtraTitulacion()),
					}
				);
			}
			
			document.add(table);
		} else {
			document.add(new Paragraph("Sin titulaciones"));
		}
	}
	
	private static void generarAcreditaciones(UsuarioBolsaEmpleo candidato, Document document) throws DocumentException, SQLException, UVException {
		document.add(new Paragraph("\n"));
		
		ModeloMeritosPreferentesCandidato modeloAcreditaciones = ModeloMeritosPreferentesCandidato.obtenerInstancia();
		List<MeritoPreferenteUsuario> listaAcreditaciones = modeloAcreditaciones.listaMeritosPreferentesUsuarioPorPosesion(candidato);
		
		document.add(new Paragraph("Acreditaciones", fontBold));
		
		if (listaAcreditaciones.size() > 0) {
			LinkedHashMap<String, Float> headerColumns = new LinkedHashMap<String, Float>();
			
			headerColumns.put("Id. Acreditación", (float) SIZE_12);
			headerColumns.put("Código", (float) SIZE_12);
			headerColumns.put("Acreditación", (float) SIZE_40);
			
			Table table = generarTable(PDF_TABLE_COLUMNS_3, listaAcreditaciones.size());
			
			generarTableHeader(table, headerColumns);
			
			for (MeritoPreferenteUsuario m : listaAcreditaciones) {
				ParametrosConfiguracion config = ModeloParametrosConfiguracion.obtenerInstancia().getParametroByNombre("bolsaempleo.local.codMeritoPreferente");
				generarTableRow(table, new String[] {
						m.getCodNum().toString(),
						config.getValor() + "." + m.getMeritoPreferente().getCodigo(),
						m.getMeritoPreferente().getNombre() + " " 
						+ (m.getMeritoPreferenteOpcion() != null ? m.getMeritoPreferenteOpcion().getNombre() : ""),
					}
				);
			}
			
			document.add(table);
		} else {
			document.add(new Paragraph("Sin acreditaciones"));
		}
	}
	
}
