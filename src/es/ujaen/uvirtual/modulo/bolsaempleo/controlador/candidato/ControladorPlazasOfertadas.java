package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloContratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloOfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlazasOfertadas;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Servlet implementation class ControladorPlazasOfertadas.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.plazasofertadas",
	description = "Plazas ofertadas bolsa empleo",
	urlPatterns = {
			"/srv/es/informacionadministrativa/bolsaempleo/plazasofertadas",
			"/srv/en/informacionadministrativa/bolsaempleo/plazasofertadas",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/plazasofertadas",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/plazasofertadas"
	})
public class ControladorPlazasOfertadas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorPlazasOfertadas.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_ACEPTAR_CONTRATACION = "aceptarcontratacion";
	public static final String ACCION_ACEPTAR_PLAZA = "aceptarplaza";
	public static final String ACCION_DATATABLE_PLAZAS_OFERTADAS = "datatableplazasofertadas";
	public static final String ACCION_GUARDAR_PREFERENCIAS = "guardarpreferencias";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_RECHAZAR_CONTRATACION = "rechazarcontratacion";
	public static final String ACCION_RECHAZAR_PLAZA = "rechazarplaza";
	public static final String ACCION_SELECCIONAR_CONTRATO = "seleccionarcontrato";
	public static final String ACCION_SELECCIONAR_PLAZA_OFERTADA = "seleccionarplazaofertada";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_CONTRATACION = "contratacion";
	public static final String PARAM_OFERTAS_PREFERENCIAS = "ofertaspreferencias";
	public static final String PARAM_PLAZA_OFERTADA = "plazaofertada";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso";
	public static final String MENSAJE_EXITO_ACEPTAR = "Plaza aceptada correctamente";
	public static final String MENSAJE_EXITO_GUARDAR_PREFERENCIA = "Orden de preferencia para las plazas cambiado correctamente";
	public static final String MENSAJE_EXITO_RECHAZAR = "Plaza rechazada correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_PLO = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/plazasofertadas/";
	public static final String JSP_INDEX = RUTA_BEP_PLO + "index.jsp";
	public static final String JSP_PLAZA = RUTA_BEP_PLO + "plazaOfertada.jsp";
	public static final String JSP_CONTRATO = RUTA_BEP_PLO + "contratoPlaza.jsp";
	
	// errors
	public static final Integer RESPONSE_HTTP_CODE_ERROR_400 = 400;
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/plazasofertadas";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:executablestatementcount", "checkstyle:javancss"})
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaPlazasOfertadas bean = new VistaPlazasOfertadas();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));
		if (nombreAccion == null || nombreAccion.isEmpty()) {
			nombreAccion = ACCION_INDEX;
		}
		try {
			init(bean, datos, request, response);
			
			switch (nombreAccion) {
				case ACCION_DATATABLE_PLAZAS_OFERTADAS:
					listaPlazasOfertadas(bean, datos, request, response);
					break;
				case ACCION_GUARDAR_PREFERENCIAS:
					guardarPreferencias(bean, datos, request, response);
					break;
				case ACCION_INDEX:
					bean.setListaOfertasCandidatos(ModeloOfertaCandidato.obtenerInstancia().listaOfertasCandidatoPreferentes(bean.getUsuarioLogeado()));
					break;
				case ACCION_ACEPTAR_CONTRATACION:
				case ACCION_ACEPTAR_PLAZA:
				case ACCION_RECHAZAR_CONTRATACION:
				case ACCION_RECHAZAR_PLAZA:
				case ACCION_SELECCIONAR_CONTRATO:
				case ACCION_SELECCIONAR_PLAZA_OFERTADA:
					seleccionarPlazaOfertada(bean, datos, request, response, nombreAccion);
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
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
		}
	}
	
	private void init(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
			
			// personal, direccion
			int[] rolesValidos = {ModeloRol.ID_ROL_CANDIDATO};
			boolean contains = IntStream.of(rolesValidos).
					anyMatch(x -> x == bean.getUsuarioLogeado().getRol().getCodNum());
			
			if (!contains) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void errorFatal(VistaPlazasOfertadas bean, String mensaje) {
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
	
	private void guardarPreferencias(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, UVException {
		Gson gson = new GsonBuilder().create();
		ModeloOfertaCandidato modeloOferta = ModeloOfertaCandidato.obtenerInstancia();
		
		try {
			HashMap<Integer, Integer> preferencias;
			try {
				preferencias = gson.fromJson(request.getParameter(PARAM_OFERTAS_PREFERENCIAS), new TypeToken<HashMap<Integer, Integer>>() { }.getType());
			} catch (Exception ex) {
				throw new UVException("preferencias incorrectas");
			}
			
			for (Map.Entry<Integer, Integer> entry: preferencias.entrySet()) {
				modeloOferta.actualizaPreferenciaOfertaCandidato(entry.getKey(), entry.getValue(), bean.getUsuarioLogeado());
			}
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_GUARDAR_PREFERENCIA, bean, request);
		} catch (UVException ex) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(ex));
			LOGGER.log(Level.SEVERE, ex.toString());
			BolsaEmpleoUtils.addMensajeDeError(ex.getMessage(), bean, request);
		}
		
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void seleccionarPlazaOfertada(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_PLAZA);
		ModeloOfertaCandidato modeloOferta = ModeloOfertaCandidato.obtenerInstancia();
		
		PlazaOfertada plaza = new PlazaOfertada(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_PLAZA_OFERTADA)));
		OfertaCandidato oferta = modeloOferta.getOfertaByPlazaCandidato(plaza, bean.getUsuarioLogeado());
		
		if (!ModeloPlazaOfertada.obtenerInstancia().checkPlazaOfertadaCandidato(oferta.getPlaza(), bean.getUsuarioLogeado())) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		}
		
		bean.setOfertaCandidato(oferta);
		
		switch (nombreAccion) {
			case ACCION_ACEPTAR_CONTRATACION:
				aceptarContrato(bean, datos, request, response);
				break;
			case ACCION_ACEPTAR_PLAZA:
				aceptarPlazaOfertada(bean, datos, request, response);
				break;
			case ACCION_RECHAZAR_CONTRATACION:
				rechazarContrato(bean, datos, request, response);
				break;
			case ACCION_RECHAZAR_PLAZA:
				rechazarPlazaOfertada(bean, datos, request, response);
				break;
			case ACCION_SELECCIONAR_PLAZA_OFERTADA:
				break;
			case ACCION_SELECCIONAR_CONTRATO:
				bean.setVista(JSP_CONTRATO);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void aceptarContrato(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, UVException, SQLException {
		ModeloContratacion modelo = ModeloContratacion.obtenerInstancia();
		modelo.aceptarContrato(bean.getOfertaCandidato().getPlaza(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ACEPTAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void rechazarContrato(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, UVException, SQLException {
		ModeloContratacion modelo = ModeloContratacion.obtenerInstancia();
		modelo.rechazarContrato(bean.getOfertaCandidato().getPlaza(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_RECHAZAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void aceptarPlazaOfertada(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, UVException, SQLException {
		ModeloOfertaCandidato modelo = ModeloOfertaCandidato.obtenerInstancia();
		modelo.aceptarOfertaCandidato(bean.getOfertaCandidato().getPlaza(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ACEPTAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void rechazarPlazaOfertada(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, UVException, SQLException {
		ModeloOfertaCandidato modelo = ModeloOfertaCandidato.obtenerInstancia();
		modelo.rechazarOfertaCandidato(bean.getOfertaCandidato().getPlaza(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_RECHAZAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void listaPlazasOfertadas(VistaPlazasOfertadas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<OfertaCandidato> dataTable = ModeloOfertaCandidato.obtenerInstancia().listadoPlazasOfertadasCandidato(
						request.getParameterMap(), bean.getUsuarioLogeado());
				bean.setDatatableOfertasCandidatos(dataTable);
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
