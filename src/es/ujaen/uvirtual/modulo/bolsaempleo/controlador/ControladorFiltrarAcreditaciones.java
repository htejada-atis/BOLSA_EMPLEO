package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoAcreditacionesTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFiltrarAcreditaciones;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase controlador para filtrar candidatos por acreditaciones .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.filtraracreditaciones", 
	description = "Filtrar candidatos de las bolsas por acreditaciones", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/filtraracreditaciones", 
			"/srv/en/informacionadministrativa/bolsaempleo/filtraracreditaciones",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/filtraracreditaciones", 
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/filtraracreditaciones"
	})
public class ControladorFiltrarAcreditaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorFiltrarAcreditaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_ACREDITACION_DESELECCIONADA = "acreditaciondeseleccionada";
	public static final String ACCION_ACREDITACION_SELECCIONADA = "acreditacionseleccionada";
	public static final String ACCION_CANDIDATO_SELECCIONADO = "candidatoseleccionado";
	public static final String ACCION_DATATABLE_ACREDITACIONES_CANDIDATO = "datatableacreditacionescandidato";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_INDEX = "listar";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACREDITACION = "acreditacion";
	public static final String PARAM_CANDIDATO = "candidato";
	
	// ruta vistas
	public static final String RUTA_BEP_FILACRE = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/filtraracreditaciones/";
	public static final String JSP_INDEX = RUTA_BEP_FILACRE + "index.jsp";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/filtraracreditaciones";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaFiltrarAcreditaciones bean = new VistaFiltrarAcreditaciones();		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			if (!init(bean, datos, request, response)) {
				return;
			}
			switch (nombreAccion) {
				case ACCION_ACREDITACION_DESELECCIONADA:
				case ACCION_ACREDITACION_SELECCIONADA:
				case ACCION_CANDIDATO_SELECCIONADO:
				case ACCION_DATATABLE_ACREDITACIONES_CANDIDATO:
					candidatoSeleccionado(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DATATABLE_CANDIDATOS:
					listadoCandidatos(bean, datos, request, response);
					break;
				case ACCION_INDEX:
					break;
				default:
					errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} finally {
			datos.getVistas().put(bean.getClass().getName(), bean);
			datos.getFicherosJSP().add(bean.getVista());
			datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
			datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
			datos.getFicherosJS().add("/js/jquery.ui.datepicker-es.js");
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private boolean init(VistaFiltrarAcreditaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));
			
			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private void errorFatal(VistaFiltrarAcreditaciones bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	private void candidatoSeleccionado(VistaFiltrarAcreditaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
		bean.setCandidato(candidato);
		
		switch (nombreAccion) {
			case ACCION_ACREDITACION_DESELECCIONADA:
				seleccionarAcreditacion(bean, datos, request, response, false);
				break;
			case ACCION_ACREDITACION_SELECCIONADA:
				seleccionarAcreditacion(bean, datos, request, response, true);
				break;
			case ACCION_CANDIDATO_SELECCIONADO:
				obtenerCodigoPadreAcreditacion(bean, request);
				break;
			case ACCION_DATATABLE_ACREDITACIONES_CANDIDATO:
				listadoAcreditacionesCandidato(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
		
	}
	
	private void obtenerCodigoPadreAcreditacion(VistaFiltrarAcreditaciones bean, HttpServletRequest request) throws SQLException, UVException {
		ParametrosConfiguracion config = ModeloParametrosConfiguracion.obtenerInstancia().getParametroByNombre("bolsaempleo.local.codMeritoPreferente");
		bean.setCodigoPadreMeritoPreferente(config.getValor());
		
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
		List<MeritoPreferenteUsuario> validados = ModeloMeritosPreferentesCandidato.obtenerInstancia().listaMeritosPreferentesUsuarioPorPosesionValidados(candidato);
		bean.setValidadas(validados);
	}
	
	/** Selecciona una acreditación para validarla o no .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param seleccionada .
	 * @throws IOException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarAcreditacion(VistaFiltrarAcreditaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, boolean seleccionada)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer acreditacionId = Formateador.leeParametroInteger(request.getParameter(PARAM_ACREDITACION));
				ModeloMeritosPreferentesCandidato modeloAcreditacion = ModeloMeritosPreferentesCandidato.obtenerInstancia();
				MeritoPreferenteUsuario acreditacion = modeloAcreditacion.getMeritoPreferenteUsuarioById(acreditacionId);
				bean.setAcreditacion(acreditacion);
				
				if (seleccionada) {
					modeloAcreditacion.validaAcreditacion(acreditacion, bean.getCandidato(), bean.getUsuarioLogeado());
				} else {
					modeloAcreditacion.desvalidaAcreditacion(acreditacion, bean.getCandidato(), bean.getUsuarioLogeado());
				}
				
				CodigoDescripcion mensaje = new CodigoDescripcion("ok", seleccionada ? "acreditación validada" : "acreditación no validada");
				writer.write(new Gson().toJson(mensaje));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * Lista de candidatos datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 */
	private void listadoCandidatos(VistaFiltrarAcreditaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloCandidato modeloCandidato = ModeloCandidato.obtenerInstancia();
				
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoAcreditacionesTable> dataTable = modeloCandidato.listaCandidatosAcreditacionesDatatable(request.getParameterMap());
				bean.setDatatableCandidatos(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
					LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
					LOGGER.log(Level.SEVERE, e.toString());
				} else {
					LOGGER.log(Level.WARNING, e.toString());
				}
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * Lista de acreditaciones candidato datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 */
	private void listadoAcreditacionesCandidato(VistaFiltrarAcreditaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable = ModeloMeritosPreferentesCandidato.obtenerInstancia().
						listaMeritosCandidatoDatatable(request.getParameterMap(), bean.getCandidato());
				bean.setDataTableAcreditaciones(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
					LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
					LOGGER.log(Level.SEVERE, e.toString());
				} else {
					LOGGER.log(Level.WARNING, e.toString());
				}
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
}
