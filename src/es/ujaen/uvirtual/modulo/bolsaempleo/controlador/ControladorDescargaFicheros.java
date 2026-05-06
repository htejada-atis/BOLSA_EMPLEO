package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.informes.GenerarAlegacionesPDF;
import es.ujaen.uvirtual.modulo.bolsaempleo.informes.GenerarItemsBaremacionPDF;
import es.ujaen.uvirtual.modulo.bolsaempleo.informes.GenerarResultadosPDF;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDescargaFichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloFichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaDescargaFicheros;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados;
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
	public static final String ACCION_DESCARGAR_ADJUNTO_MENSAJE_PERSONAL = "descargaradjuntomensajepersonal";
	public static final String ACCION_DESCARGAR_DOCUMENTO = "descargardocumento";
	public static final String ACCION_DESCARGAR_HORARIO_CANDIDATO = "descargarhorariocandidato";
	public static final String ACCION_DESCARGAR_HORARIO_DIRECTOR = "descargarhorariodirector";
	public static final String ACCION_DESCARGAR_HORARIO_PERSONAL = "descargarhorariopersonal";
	public static final String ACCION_DESCARGAR_MERITO_CANDIDATO = "descargarmeritocandidato";
	public static final String ACCION_DESCARGAR_ALEGACION_MERITO = "descargaralegacionmerito";
	public static final String ACCION_DESCARGAR_ALEGACION_MERITO_POR_PERSONAL = "descargaralegacionmeritoporpersonal";
	public static final String ACCION_DESCARGAR_ALEGACION_MERITO_POR_DIRECTOR = "descargaralegacionmeritopordirector";
	public static final String ACCION_DESCARGAR_MERITO_PERSONAL = "descargarmeritopersonal";
	public static final String ACCION_DESCARGAR_MERITO_EVALUADOR = "descargarmeritoevaluador";
	public static final String ACCION_DESCARGAR_NRI_PERSONAL = "descargarnripersonal";
	public static final String ACCION_DESCARGAR_RESULTADOS_SOLICITUD_CANDIDATO = "descargarresultadossolicitudcandidato";
	public static final String ACCION_DESCARGAR_ALEGACIONES_RESULTADOS_SOLICITUD_CANDIDATO = "descargaralegacionesresultadossolicitudcandidato";
	public static final String ACCION_DESCARGAR_RESOLUCION_ALEGACION_RESULTADOS_SOLICITUD_CANDIDATO = "descargarresolucionalegacionresultadossolicitudcandidato";
	public static final String ACCION_DESCARGAR_RESULTADOS_SOLICITUD_PERSONAL = "descargarresultadossolicitudpersonal";
	public static final String ACCION_DESCARGAR_RESUMEN_ITEM_BAREMACION = "descargarresumenitemsbaremacion";
	public static final String ACCION_DESCARGAR_SOLICITUD = "descargarsolicitud";
	public static final String ACCION_DESCARGAR_SOLICITUD_PERSONAL = "descargarsolicitudpersonal";
	public static final String ACCION_DESCARGAR_TITULACION_CANDIDATO = "descargartitulacioncandidato";
	public static final String ACCION_DESCARGAR_TITULACION_PERSONAL = "descargartitulacionpersonal";

	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACREDITACION = "acreditacion";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_CONVOCATORIA = "convocatoria";
	public static final String PARAM_MENSAJE = "mensaje";
	public static final String PARAM_MERITO = "merito";
	public static final String PARAM_ALEGACION = "alegacion";
	public static final String PARAM_PLAZA_OFERTADA = "plazaofertada";
	public static final String PARAM_SOLICITUD = "solicitud";
	public static final String PARAM_TITULACION = "titulacion";

	// Mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_GENERANDO_PDF_SOLICITUD = "Error al generar pdf de la solicitud";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso para acceder a este archivo";

	// Urls
	public static final String URL_DESCARGA_FICHEROS = "/srv/es/informacionadministrativa/bolsaempleo/descargaficheros";
	public static final String URL_DESCARGA_FICHEROS_PUBLICA = "/pub/es/informacionadministrativa/bolsaempleo/descargaficheros";

	/** do get.
	 * @param request  peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		response.setContentType("application/pdf");

		VistaDescargaFicheros bean = new VistaDescargaFicheros();
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));

		try {
			if (!init(bean, datos, request, response)) {
				return;
			}
			switch (nombreAccion) {
				case ACCION_DESCARGAR_ACREDITACION_CANDIDATO:
				case ACCION_DESCARGAR_HORARIO_CANDIDATO:
				case ACCION_DESCARGAR_MERITO_CANDIDATO:
				case ACCION_DESCARGAR_TITULACION_CANDIDATO:
				case ACCION_DESCARGAR_RESULTADOS_SOLICITUD_CANDIDATO:
				case ACCION_DESCARGAR_SOLICITUD:
				case ACCION_DESCARGAR_ALEGACION_MERITO:
				case ACCION_DESCARGAR_ALEGACIONES_RESULTADOS_SOLICITUD_CANDIDATO:
				case ACCION_DESCARGAR_RESOLUCION_ALEGACION_RESULTADOS_SOLICITUD_CANDIDATO:
					accionesFicherosCandidatos(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DESCARGAR_HORARIO_DIRECTOR:
				case ACCION_DESCARGAR_ALEGACION_MERITO_POR_DIRECTOR:
					accionesFicherosDirectores(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DESCARGAR_MERITO_EVALUADOR:
					accionesFicherosEvaluador(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DESCARGAR_ALEGACION_MERITO_POR_PERSONAL:
				case ACCION_DESCARGAR_ACREDITACION_PERSONAL:
				case ACCION_DESCARGAR_ADJUNTO_MENSAJE_PERSONAL:
				case ACCION_DESCARGAR_HORARIO_PERSONAL:
				case ACCION_DESCARGAR_MERITO_PERSONAL:
				case ACCION_DESCARGAR_NRI_PERSONAL:
				case ACCION_DESCARGAR_TITULACION_PERSONAL:
				case ACCION_DESCARGAR_RESULTADOS_SOLICITUD_PERSONAL:
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
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

	private boolean init(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		BolsaEmpleoUtils.readMensajeSession(bean, request);

		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());

			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}

		return true;
	}

	private void accionesFicherosCandidatos(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {

		if (!bean.getUsuarioLogeado().isCandidato()) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		switch (nombreAccion) {
			case ACCION_DESCARGAR_ACREDITACION_CANDIDATO:
				descargaAcreditacionCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_HORARIO_CANDIDATO:
				descargaHorarioPlazaCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_MERITO_CANDIDATO:
				descargaMeritoCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_TITULACION_CANDIDATO:
				descargaTitulacionCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_RESULTADOS_SOLICITUD_CANDIDATO:
				descargaResultadosSolicitudCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_ALEGACIONES_RESULTADOS_SOLICITUD_CANDIDATO:
				descargaAlegacionesResultadosSolicitudCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_RESOLUCION_ALEGACION_RESULTADOS_SOLICITUD_CANDIDATO:
				descargaResolucionAlegacionResultadosSolicitudCandidato(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_SOLICITUD:
				descargaSolicitud(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_ALEGACION_MERITO:
				descargaAlegacionMerito(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, datos, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}

	}

	private void accionesFicherosDirectores(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {

		if (!bean.getUsuarioLogeado().isDirectorDepartamento()) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		switch (nombreAccion) {
			case ACCION_DESCARGAR_HORARIO_DIRECTOR:
				descargaHorarioPlazaDirector(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_ALEGACION_MERITO_POR_DIRECTOR:
				descargaAlegacionMeritoDirector(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, datos, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}

	private void accionesFicherosEvaluador(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {

		if (!bean.getUsuarioLogeado().isMiembroComision() && !bean.getUsuarioLogeado().isDirectorDepartamento()) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		switch (nombreAccion) {
			case ACCION_DESCARGAR_MERITO_EVALUADOR:
				descargaMeritoEvaluador(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, datos, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}

	@SuppressWarnings({"checkstyle:CyclomaticComplexity"})
	private void accionesFicherosPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {

		if (!bean.getUsuarioLogeado().isServicioPersonal()) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		switch (nombreAccion) {
			case ACCION_DESCARGAR_ACREDITACION_PERSONAL:
				descargaAcreditacionPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_ADJUNTO_MENSAJE_PERSONAL:
				descargaAdjuntoMensajePersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_HORARIO_PERSONAL:
				descargaHorarioPlazaPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_MERITO_PERSONAL:
				descargaMeritoPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_NRI_PERSONAL:
				descargaNRIPlazaPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_TITULACION_PERSONAL:
				descargaTitulacionPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_RESULTADOS_SOLICITUD_PERSONAL:
				descargaResultadosSolicitudPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_RESUMEN_ITEM_BAREMACION:
				descargaResumenItemsBaremacion(datos, response);
				break;
			case ACCION_DESCARGAR_SOLICITUD_PERSONAL:
				descargaSolicitudPersonal(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_ALEGACION_MERITO_POR_PERSONAL:
				descargaAlegacionMerito(bean, datos, request, response);
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
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
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
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		} else {
			bean.setFichero(fichero);
			descargarPDF(datos, response, fichero.getArchivo());
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ITEMS DE BAREMACION
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void descargaResumenItemsBaremacion(UVDatos datos, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		try (InputStream archivo = GenerarItemsBaremacionPDF.generarPDF()) {
			descargarPDF(datos, response, archivo);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MENSAJES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void descargaAdjuntoMensajePersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idMensaje = Formateador.leeParametroInteger(request.getParameter(PARAM_MENSAJE));
		Mensaje mensaje = ModeloMensajes.obtenerInstancia().getMensajeById(idMensaje, true);
		bean.setMensaje(mensaje);
		descargarPDF(datos, response, mensaje.getAdjunto());
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
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
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
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
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
	// ALEGACIONES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void descargaAlegacionMerito(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws SQLException, UVException, IOException {
		Integer idAlegacionMerito = Formateador.leeParametroInteger(request.getParameter(PARAM_ALEGACION));
		UsuarioBolsaEmpleo usuario = bean.getUsuarioLogeado();
		boolean isPersonal = usuario.getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL); 
		ModeloAlegaciones modeloAlegaciones = ModeloAlegaciones.obtenerInstancia();

		if (!isPersonal) {
			// Comprobamos si le pertence la alegación
			Alegacion alegacion = modeloAlegaciones.getAlegacionByArchivoId(idAlegacionMerito);
		
			if (alegacion == null || !alegacion.getCandidato().getCodNum().equals(usuario.getCodNum())) {
				BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
				return;
			}
		}

		InputStream archivo = modeloAlegaciones.getFicheroAlegacionMerito(idAlegacionMerito);
		if (archivo == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		} else {
			descargarPDF(datos, response, archivo);
		}
	}

	private void descargaAlegacionMeritoDirector(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws SQLException, UVException, IOException {

		// PARAM_ALEGACION contiene el ID del archivo (TBEP_SOL_MER_BOL_ALE_FILE.CODNUM), no el CodNum de la alegación
		Integer idArchivoAlegacion = Formateador.leeParametroInteger(request.getParameter(PARAM_ALEGACION));

		// Obtener la alegación desde el ID del archivo para validar permisos
		ModeloAlegaciones modeloAlegaciones = ModeloAlegaciones.obtenerInstancia();
		Alegacion alegacion = modeloAlegaciones.getAlegacionByArchivoId(idArchivoAlegacion);

		if (alegacion == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		// Verificar que el director sea evaluador en el área de la alegación
		ModeloEvaluador modeloEvaluador = ModeloEvaluador.obtenerInstancia();
		Evaluador evaluador = modeloEvaluador.getEvaluadorById(bean.getUsuarioLogeado().getCodNum(), alegacion.getArea().getCodNum());

		if (evaluador == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		// Si el director tiene permisos, descargar el fichero usando el ID del archivo
		InputStream archivo = modeloAlegaciones.getFicheroAlegacionMerito(idArchivoAlegacion);
		if (archivo == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		} else {
			descargarPDF(datos, response, archivo);
		}
	}

	private void descargaAlegacionesResultadosSolicitudCandidato(VistaDescargaFicheros bean, UVDatos datos,
			HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idBolsa = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));
		Integer idConvocatoria = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA));

		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(idBolsa);
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(idConvocatoria);

		// VALIDACIÓN: Verificar que el usuario tiene una solicitud en esta bolsa y convocatoria
		BolsaResultado bolsaResultado = ModeloResultados.obtenerInstancia().getBolsaResultado(bolsa, bean.getUsuarioLogeado(), convocatoria);
		if (bolsaResultado == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		bean.setBolsaResultado(bolsaResultado);

		VistaMisResultados beanResultados = new VistaMisResultados();
		beanResultados.setBolsa(bolsa);
		beanResultados.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convocatoria.getCodNum()));
		beanResultados.setBolsaResultado(bolsaResultado);
		beanResultados.setUsuarioLogeado(bean.getUsuarioLogeado());

		ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia();
		modelo.establecerDescripcionesSiExistenAlegacionesEnLosMeritos(beanResultados, convocatoria, bean.getUsuarioLogeado(), bolsa.getCodNum());
		SolMerBolAlegacion solMerBolAlegacion = modelo.obtenerSolMerBolAlegacionExistente(bolsa, convocatoria, bean.getUsuarioLogeado(), null);
		modelo.asignarArchivosAlegacion(beanResultados, solMerBolAlegacion);

		Alegacion alegacion = modelo.getAlegacionBySolicitudBolsa(beanResultados, convocatoria, bean.getUsuarioLogeado());
		descargarPDF(datos, response, GenerarAlegacionesPDF.generarPDFAlegaciones(bean.getUsuarioLogeado(), bolsaResultado, convocatoria, solMerBolAlegacion, alegacion));
	}

	public void descargaResolucionAlegacionResultadosSolicitudCandidato(VistaDescargaFicheros bean, UVDatos datos,
			HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idBolsa = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));
		Integer idConvocatoria = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA));

		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(idBolsa);
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(idConvocatoria);

		// VALIDACIÓN: Verificar que el usuario tiene una solicitud en esta bolsa y convocatoria
		BolsaResultado bolsaResultado = ModeloResultados.obtenerInstancia().getBolsaResultado(bolsa, bean.getUsuarioLogeado(), convocatoria);
		if (bolsaResultado == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		bean.setBolsaResultado(bolsaResultado);

		VistaMisResultados beanResultados = new VistaMisResultados();
		beanResultados.setBolsa(bolsa);
		beanResultados.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convocatoria.getCodNum()));
		beanResultados.setBolsaResultado(bolsaResultado);
		beanResultados.setUsuarioLogeado(bean.getUsuarioLogeado());

		ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia();
		modelo.establecerDescripcionesSiExistenAlegacionesEnLosMeritos(beanResultados, convocatoria, bean.getUsuarioLogeado(), bolsa.getCodNum());

		SolMerBolAlegacion solMerBolAlegacion = modelo.obtenerSolMerBolAlegacionExistente(bolsa, convocatoria, bean.getUsuarioLogeado(), null);
		modelo.asignarArchivosAlegacion(beanResultados, solMerBolAlegacion);

		Alegacion alegacion = modelo.getAlegacionBySolicitudBolsa(beanResultados, convocatoria, bean.getUsuarioLogeado());
		if (alegacion == null || !ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION.equals(alegacion.getEstado())) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
			return;
		}

		descargarPDF(datos, response, GenerarAlegacionesPDF.generarPDFResolucionAlegacion(bean.getUsuarioLogeado(), bolsaResultado, convocatoria, solMerBolAlegacion, alegacion));
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PLAZAS OFERTADAS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void descargaHorarioPlazaCandidato(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia();

		Integer idPlazaOfertada = Formateador.leeParametroInteger(request.getParameter(PARAM_PLAZA_OFERTADA));
		PlazaOfertada plaza = ModeloDescargaFichero.obtenerInstancia().compruebaPlazaOfertadaCandidato(idPlazaOfertada, bean.getUsuarioLogeado(),
				modeloConvocatoria.getUltimaConvocatoriaFinalizada());
		bean.setPlazaOfertada(plaza);
		if (plaza == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		} else {
			descargarPDF(datos, response, plaza.getHorario());
		}
	}

	private void descargaHorarioPlazaDirector(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idPlazaOfertada = Formateador.leeParametroInteger(request.getParameter(PARAM_PLAZA_OFERTADA));
		PlazaOfertada plaza = ModeloDescargaFichero.obtenerInstancia().compruebaPlazaOfertadaDirector(idPlazaOfertada, bean.getUsuarioLogeado());
		bean.setPlazaOfertada(plaza);
		if (plaza == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		} else {
			descargarPDF(datos, response, plaza.getHorario());
		}
	}

	private void descargaHorarioPlazaPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		Integer idPlazaOfertada = Formateador.leeParametroInteger(request.getParameter(PARAM_PLAZA_OFERTADA));
		PlazaOfertada plaza = ModeloPlazaOfertada.obtenerInstancia().getPlazaOfertadaById(idPlazaOfertada);
		bean.setPlazaOfertada(plaza);
		descargarPDF(datos, response, plaza.getHorario());
	}

	private void descargaNRIPlazaPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		Integer idPlazaOfertada = Formateador.leeParametroInteger(request.getParameter(PARAM_PLAZA_OFERTADA));
		PlazaOfertada plaza = ModeloPlazaOfertada.obtenerInstancia().getPlazaOfertadaById(idPlazaOfertada);
		bean.setPlazaOfertada(plaza);
		descargarPDF(datos, response, plaza.getNri());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// RESULTADOS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void descargaResultadosSolicitudCandidato(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA)));

		BolsaResultado bolsaResultado = ModeloResultados.obtenerInstancia().getBolsaResultado(bolsa, bean.getUsuarioLogeado(), convocatoria);
		bean.setBolsaResultado(bolsaResultado);

		descargarPDF(datos, response, GenerarResultadosPDF.generarPDF(bean.getUsuarioLogeado(), bolsaResultado, convocatoria));
	}

	private void descargaResultadosSolicitudPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA)));

		BolsaResultado bolsaResultado = ModeloResultados.obtenerInstancia().getBolsaResultado(bolsa, candidato, convocatoria);
		bean.setBolsaResultado(bolsaResultado);

		descargarPDF(datos, response, GenerarResultadosPDF.generarPDF(candidato, bolsaResultado, convocatoria));
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SOLICITUD
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void descargaSolicitud(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer idSolicitud = Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD));
		Solicitud solicitud = ModeloDescargaFichero.obtenerInstancia().compruebaSolicitudCandidato(idSolicitud, bean.getUsuarioLogeado());
		if (solicitud == null) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
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
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
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
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		} else {
			descargarPDF(datos, response, titulacion.getArchivo());
		}
	}

	private void descargaTitulacionPersonal(VistaDescargaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException {
		Integer idTitulacion = Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION));
		TitulacionUsuario titulacion = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(idTitulacion, true);
		bean.setTitulacion(titulacion);
		descargarPDF(datos, response, titulacion.getArchivo());
	}



}
