package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfWriter;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDescargaFichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloFichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaDescargaFicheros;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase controlador para mostrar errores .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.descargaficheros",
	displayName = "bolsaempleo.descargaficheros",
	description = "Controlador para la descarga de ficheros de bolsa empleo", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/descargaficheros", 
			"/srv/en/informacionadministrativa/bolsaempleo/descargaficheros",
			"/pub/es/informacionadministrativa/bolsaempleo/descargaficheros",
			"/pub/en/informacionadministrativa/bolsaempleo/descargaficheros"
	})
public class ControladorDescargaFicheros extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorDescargaFicheros.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
    
	// Acciones
	public static final String ACCION_DESCARGAR_ACREDITACION_CANDIDATO = "descargaracreditacioncandidato";
	public static final String ACCION_DESCARGAR_ACREDITACION_PERSONAL = "descargaracreditacionpersonal";
	public static final String ACCION_DESCARGAR_DOCUMENTO = "descargardocumento";
	public static final String ACCION_DESCARGAR_MERITO_CANDIDATO = "descargarmeritocandidato";
	public static final String ACCION_DESCARGAR_MERITO_PERSONAL = "descargarmeritopersonal";
	public static final String ACCION_DESCARGAR_MERITO_COMISION = "descargarmeritocomision";
	public static final String ACCION_DESCARGAR_RESUMEN_ITEM_BAREMACION = "descargarresumenitemsbaremacion";
	public static final String ACCION_DESCARGAR_SOLICITUD = "descargarsolicitud";
	public static final String ACCION_DESCARGAR_SOLICITUD_PERSONAL = "descargarsolicitudpersonal";
	public static final String ACCION_DESCARGAR_TITULACION_CANDIDATO = "descargartitulacioncandidato";
	public static final String ACCION_DESCARGAR_TITULACION_PERSONAL = "descargartitulacionpersonal";	
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACREDITACION = "acreditacion";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_MERITO = "merito";
	public static final String PARAM_SOLICITUD = "solicitud";
	public static final String PARAM_TITULACION = "titulacion";
	
	// Mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_GENERANDO_PDF_SOLICITUD = "Error al generar pdf de la solicitud";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso para acceder a este archivo";
	
	// pdf items baremacion
	public static final int PDF_ANCHO = 4;
	public static final int PDF_ALTO = 4;
	public static final int PDF_FORMATO = 4;
	public static final int PDF_TABLE_COLUMNS = 5;
	public static final int PDF_TABLE_PADDING = 5;
	public static final int SIZE_8 = 8;
	public static final int SIZE_10 = 10;
	public static final int SIZE_40 = 40;
	public static final int SIZE_100_WIDTH = 100;	
	public static final int[] SIZE_100 = {10, 60, 10, 10, 10};	
	
	public static final int COLOR_51 = 51;
	public static final int COLOR_112 = 112;
	public static final int COLOR_153 = 153;
	public static final int COLOR_185 = 185;
	public static final int COLOR_201 = 201;
	public static final int COLOR_241 = 241;
	public static final int COLOR_254 = 254;
	public static final int COLSPAN = 5;
	public static final int INDENTATION_LIST = 20;
	
	// Urls
	public static final String URL_DESCARGA_FICHEROS = "/srv/es/informacionadministrativa/bolsaempleo/descargaficheros";
	public static final String URL_DESCARGA_FICHEROS_PUBLICA = "/pub/es/informacionadministrativa/bolsaempleo/descargaficheros";
		
	/** do get.
	 * @param request  peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		response.setContentType("application/pdf");

		VistaDescargaFicheros bean = new VistaDescargaFicheros();
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_DESCARGAR_ACREDITACION_CANDIDATO:
				case ACCION_DESCARGAR_MERITO_CANDIDATO:
				case ACCION_DESCARGAR_TITULACION_CANDIDATO:
				case ACCION_DESCARGAR_SOLICITUD:
					accionesFicherosCandidatos(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DESCARGAR_MERITO_COMISION:
					accionesFicherosComision(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DESCARGAR_ACREDITACION_PERSONAL:
				case ACCION_DESCARGAR_MERITO_PERSONAL:
				case ACCION_DESCARGAR_TITULACION_PERSONAL:
				case ACCION_DESCARGAR_RESUMEN_ITEM_BAREMACION:
				case ACCION_DESCARGAR_SOLICITUD_PERSONAL:
					accionesFicherosPersonal(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DESCARGAR_DOCUMENTO:
					descargaArchivoFichero(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, datos, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
			}
		} catch (Exception ex) {
			errorFatal(bean, datos, ex.getMessage() != null ? ex.getMessage() : ex.toString());
		}
	}

	/** do post.
	 * @param request  peticion
	 * @param response respuesta
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Devuelve la url de descarga con su acción teniendo en cuenta si es público o privado.
	 * 
	 * @param usuario .
	 * @param action .
	 * @return .
	 */
	public static String getUrl(UsuarioBolsaEmpleo usuario, String action) {
		return (usuario == null ? URL_DESCARGA_FICHEROS_PUBLICA : URL_DESCARGA_FICHEROS) + "?" + ControladorDescargaFicheros.PARAM_ACCION + "=" + action;
	}
	
	private void errorFatal(VistaDescargaFicheros bean, UVDatos datos, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		datos.getVistas().put(bean.getClass().getName(), bean);
		datos.getFicherosJSP().add(bean.getVista());
		datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
		datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
		datos.getFicherosCSS().add("/css/intranet.css");
		datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
	}

	private void init(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void accionesFicherosCandidatos(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		
		if (!bean.getUsuarioLogeado().isCandidato()) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
			return;
		}
		
		switch (nombreAccion) {
			case ACCION_DESCARGAR_ACREDITACION_CANDIDATO:
				descargaAcreditacionCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_MERITO_CANDIDATO:
				descargaMeritoCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_TITULACION_CANDIDATO:
				descargaTitulacionCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_SOLICITUD:
				descargaSolicitud(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, datos, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
		
	}
	
	private void accionesFicherosComision(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		
		if (!bean.getUsuarioLogeado().isMiembroComision()) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
			return;
		}
		
		switch (nombreAccion) {
			case ACCION_DESCARGAR_MERITO_COMISION:
				descargaMeritoEvaluador(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, datos, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void accionesFicherosPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		
		if (!bean.getUsuarioLogeado().isServicioPersonal()) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
			return;
		}
		
		switch (nombreAccion) {
			case ACCION_DESCARGAR_ACREDITACION_PERSONAL:
				descargaAcreditacionPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_MERITO_PERSONAL:
				descargaMeritoPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_TITULACION_PERSONAL:
				descargaTitulacionPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_RESUMEN_ITEM_BAREMACION:
				descargaResumenItemsBaremacion(datos, response);
				break;
			case ACCION_DESCARGAR_SOLICITUD_PERSONAL:
				descargaSolicitudPersonal(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, datos, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void descargarPDF(UVDatos datos, HttpServletResponse response, InputStream archivo) throws UVException {
		datos.setRespuestaEnviada(true);
        
		try (ServletOutputStream stream = response.getOutputStream(); BufferedInputStream buf = new BufferedInputStream(archivo)) {
			int readBytes = 0;
			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
            }
			stream.flush();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(ex));
			LOGGER.log(Level.SEVERE, ex.toString());
			throw new UVException(ex.getMessage());
        }
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ACREDITACIONES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void descargaAcreditacionCandidato(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idAcreditacion = Formateador.leeParametroInteger(request.getParameter(PARAM_ACREDITACION));
		MeritoPreferenteUsuario acreditacion = ModeloDescargaFichero.obtenerInstancia().compruebaAcreditacionCandidato(idAcreditacion, bean.getUsuarioLogeado());
		bean.setAcreditacion(acreditacion);
		if (acreditacion == null) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
		} else {
			descargarPDF(datos, response, acreditacion.getArchivo());
		}
	}
	
	private void descargaAcreditacionPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		Integer idAcreditacion = Formateador.leeParametroInteger(request.getParameter(PARAM_ACREDITACION));
		MeritoPreferenteUsuario acreditacion = ModeloMeritosPreferentesCandidato.obtenerInstancia().getMeritoPreferenteUsuarioById(idAcreditacion);
		bean.setAcreditacion(acreditacion);
		descargarPDF(datos, response, acreditacion.getArchivo());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// DOCUMENTOS DEL SISTEMA (FICHEROS)
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void descargaArchivoFichero(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idArchivo = Formateador.leeParametroInteger(request.getParameter(PARAM_ARCHIVO));
		Fichero fichero = ModeloFichero.obtenerInstancia().listaFichero(idArchivo);
		if (!fichero.isPublico() && bean.getUsuarioLogeado() == null) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
		} else {
			bean.setFichero(fichero);
			descargarPDF(datos, response, fichero.getArchivo());
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MÉRITOS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void descargaMeritoCandidato(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idMerito = Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO));
		Merito merito = ModeloDescargaFichero.obtenerInstancia().compruebaMeritoCandidato(idMerito, bean.getUsuarioLogeado());
		bean.setMerito(merito);
		if (merito == null) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
		} else {
			descargarPDF(datos, response, merito.getArchivo());
		}
	}
	
	private void descargaMeritoEvaluador(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idMerito = Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO));
		Merito merito = ModeloDescargaFichero.obtenerInstancia().compruebaMeritoEvaluador(idMerito, bean.getUsuarioLogeado());
		bean.setMerito(merito);
		if (merito == null) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
		} else {
			descargarPDF(datos, response, merito.getArchivo());
		}
	}
	
	private void descargaMeritoPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		Integer idMerito = Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO));
		Merito merito = ModeloMerito.obtenerInstancia().listaMerito(idMerito);
		bean.setMerito(merito);
		descargarPDF(datos, response, merito.getArchivo());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SOLICITUD
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void descargaSolicitud(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idSolicitud = Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD));
		Solicitud solicitud = ModeloDescargaFichero.obtenerInstancia().compruebaSolicitudCandidato(idSolicitud, bean.getUsuarioLogeado());
		if (solicitud == null) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
		} else {
			if (solicitud.getArchivo() == null) {
				throw new UVException(MENSAJE_ERROR_GENERANDO_PDF_SOLICITUD);
			}
			
			descargarPDF(datos, response, solicitud.getArchivo());
		}
	}
	
	private void descargaSolicitudPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idSolicitud = Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD));
		Solicitud solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudByIdArchivo(idSolicitud);
		if (solicitud == null) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
		} else {
			if (solicitud.getArchivo() == null) {
				throw new UVException(MENSAJE_ERROR_GENERANDO_PDF_SOLICITUD);
			}
			
			descargarPDF(datos, response, solicitud.getArchivo());
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TITULACIONES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void descargaTitulacionCandidato(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idTitulacion = Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION));
		TitulacionUsuario titulacion = ModeloDescargaFichero.obtenerInstancia().compruebaTitulacionCandidato(idTitulacion, bean.getUsuarioLogeado());
		bean.setTitulacion(titulacion);
		if (titulacion == null) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_PERMISO, bean, request);
			response.sendRedirect(ControladorErrorBolsaEmpleo.URL_ERROR);
		} else {
			descargarPDF(datos, response, titulacion.getArchivo());
		}
	}
	
	private void descargaTitulacionPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		Integer idTitulacion = Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION));
		TitulacionUsuario titulacion = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(idTitulacion);
		bean.setTitulacion(titulacion);
		descargarPDF(datos, response, titulacion.getArchivo());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ITEMS DE BAREMACION
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void descargaResumenItemsBaremacion(UVDatos datos, HttpServletResponse response)
			throws SQLException, UVException {
		descargarPDF(datos, response, generarPDFItemsBaremacion(datos.getUsuario()));
	}
	
	private InputStream generarPDFItemsBaremacion(Usuario usu) throws UVException, SQLException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		List<ItemBaremacion> listaItems = modelo.listaItemBaremacion();
		
		try (Document document = new Document()) {
			// create a PDF writer instance and pass output stream
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(usu.getApellidosYNombre());
			document.addTitle("Listado_Items");
			document.addCreationDate();

			document.add(new Paragraph(new Chunk("Listado de Items", FontFactory.getFont(FontFactory.HELVETICA, SIZE_40, Font.BOLDITALIC))));
				        
			this.generarPDFTable(listaItems, document);
		} catch (Exception exp) {
			throw new UVException("Error generando pdf, consulte con los administradores" + exp);
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private void generarPDFTable(List<ItemBaremacion> listaItems, Document document) throws SQLException, UVException {
		Table table = new Table(PDF_TABLE_COLUMNS, listaItems.size());
		ModeloBaremacionBloques modeloBloque = ModeloBaremacionBloques.obtenerInstancia();
		ModeloBaremacionApartados modeloApartado = ModeloBaremacionApartados.obtenerInstancia();
		List<BloqueBaremacion> listaBloques = modeloBloque.listaBloqueBaremacion();
		List<ApartadoBaremacion> listaApartados = modeloApartado.listaApartadoBaremacionActivosOrdenadosPorCodigo();

		this.generarPDFTableHeader(table);
		
		for (ApartadoBaremacion apa: listaApartados) {
			Cell cell = new Cell(new Paragraph(
					"Apartado " + apa.getCodigo() + " - " + apa.getNombre(), new Font(Font.HELVETICA, SIZE_8)));
			cell.setBackgroundColor(new Color(COLOR_112, COLOR_112, COLOR_112));
			cell.setColspan(COLSPAN);
			cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.addCell(cell);
			for (BloqueBaremacion bloq: listaBloques) {
				if (bloq.getApartadoBaremacion().getCodNum().equals(apa.getCodNum())) {
					cell = new Cell(new Paragraph(
							"Bloque " + bloq.getCodigo() + " - " + bloq.getNombre(), new Font(Font.HELVETICA, SIZE_8)));
					cell.setBackgroundColor(new Color(COLOR_185, COLOR_185, COLOR_185));
					cell.setColspan(COLSPAN);
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
		table.setWidths(SIZE_100);

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
