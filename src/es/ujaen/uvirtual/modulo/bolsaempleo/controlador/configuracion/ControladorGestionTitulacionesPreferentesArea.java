package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulacionesArea;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase controlador para obtener, incluir y eliminar titulaciones preferentes
 * por área . Controlador - Opers. con nombres: obtener, incluir, eliminar .
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.titulacionespreferentesarea", 
		description = "Gestión de titulaciones preferentes por área", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea"
		})
public class ControladorGestionTitulacionesPreferentesArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionTitulacionesPreferentesArea.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_DATATABLE_TITULACIONES = "datatabletitulaciones";
	public static final String ACCION_DATATABLE_TITULACIONES_PREFERENTES_AREA = "datatabletitulacionespreferentesarea";
	public static final String ACCION_ELIMINAR_TITULACION_AREA = "eliminartitulacionarea";
	public static final String ACCION_INCLUIR_TITULACION_AREA = "incluirtitulacionarea";
	public static final String ACCION_INDEX = "listarareas";
	public static final String ACCION_SELECCIONAR_AREA = "seleccionarArea";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_TITULACIONES = "titulaciones";
	
	// Mensajes
	public static final String MENSAJE_ERROR_TITULACIONES_SELECCIONADAS_INCORRECTAS = "No hay titulaciones seleccionadas válidas";
	public static final String MENSAJE_ERROR_TITULACIONES_PREFERENTES_YA_SELECCIONADAS_PREVIAMENTE = "Las titulaciones ya han sido incluidas para éste área";
	public static final String MENSAJE_EXITO_TITULACIONES_ELIMINADAS_CORRECTAMENTE = "Titulacion/es eliminada/s correctamente";
	public static final String MENSAJE_EXITO_TITULACIONES_INCLUIDAS_CORRECTAMENTE = "Titulacion/es incluida/s correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/";
	
	// ajax	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaTitulacionesArea bean = new VistaTitulacionesArea();
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					index(bean);
					break;	
				case ACCION_SELECCIONAR_AREA:
					selecccionarArea(bean, request);
					break;
				case ACCION_ELIMINAR_TITULACION_AREA:
					eliminarTitulacionesArea(bean, request, response);
					break;
				case ACCION_INCLUIR_TITULACION_AREA:
					incluirTitulacionesPreferentesArea(bean, request, response);
					break;
				case ACCION_DATATABLE_TITULACIONES:
					listadoTitulaciones(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_TITULACIONES_PREFERENTES_AREA:
					listadoTitulacionesPreferentesArea(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
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
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
		}
	}
	
	private void init(VistaTitulacionesArea bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "titulacionespreferentesarea.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		try {
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuarioBolsaEmpleo(datos);
		} catch (UVException e) {
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}
	}
	
	private void errorFatal(VistaTitulacionesArea bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void index(VistaTitulacionesArea bean) throws SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();	
		List<Area> areas = modelo.listaAreas();
		bean.setVista(RUTA_BEP_CONF + "titulacionespreferentesarea.jsp");
		bean.setAreas(areas);
	}
	
	private Area selecccionarArea(VistaTitulacionesArea bean, HttpServletRequest request) throws SQLException, UVException {
		index(bean);
		
		String idArea = BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_AREA);
		Area area = ModeloArea.obtenerInstancia().getAreaById(Formateador.leeParametroInteger(idArea));		
		bean.setArea(area);
		
		return area;
	}
		
	private void eliminarTitulacionesArea(VistaTitulacionesArea bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		Area area = selecccionarArea(bean, request);
		
		try {
			Gson gson = new GsonBuilder().create();
			List<String> titulaciones = gson.fromJson(request.getParameter(PARAM_TITULACIONES), new TypeToken<List<String>>() { }.getType());
			ModeloTitulacion.obtenerInstancia().eliminarTitulacionesPreferentesArea(titulaciones, area.getCodNum());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_TITULACIONES_ELIMINADAS_CORRECTAMENTE, bean, request);
			redireccionConAreaSeleccionada(request, response, area);
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_TITULACIONES_SELECCIONADAS_INCORRECTAS, bean, request);
		}
	}
	
	private void incluirTitulacionesPreferentesArea(VistaTitulacionesArea bean, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		Area area = selecccionarArea(bean, request);
		
		try {
			Gson gson = new GsonBuilder().create();
			List<String> titulaciones = gson.fromJson(request.getParameter(PARAM_TITULACIONES), new TypeToken<List<String>>() { }.getType());
			ModeloTitulacion.obtenerInstancia().incluirTitulacionesPreferentesArea(titulaciones, area.getCodNum());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_TITULACIONES_INCLUIDAS_CORRECTAMENTE, bean, request);						
			redireccionConAreaSeleccionada(request, response, area);
		} catch (SQLIntegrityConstraintViolationException e) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_TITULACIONES_PREFERENTES_YA_SELECCIONADAS_PREVIAMENTE, bean, request);
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_TITULACIONES_SELECCIONADAS_INCORRECTAS, bean, request);
		}
	}
	
	private void listadoTitulaciones(VistaTitulacionesArea bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				String idArea = BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_AREA); 
				Integer area = Formateador.leeParametroInteger(idArea);
				BolsaEmpleoDataTable<Titulacion> dataTable = modelo.listaTitulacionesDatatable(request.getParameterMap(), area);
				bean.setDatatableTitulaciones(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
		
		datos.setRespuestaEnviada(true);
	}
	
	private void listadoTitulacionesPreferentesArea(VistaTitulacionesArea bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				String idArea = BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_AREA);
				Integer area = Formateador.leeParametroInteger(idArea);
				BolsaEmpleoDataTable<TitulacionArea> dataTable = modelo.listaTitulacionesAreaDatatable(request.getParameterMap(), area);
				bean.setDatatableTitulacionesArea(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
		
		datos.setRespuestaEnviada(true);
	}

	private void redireccionConAreaSeleccionada(HttpServletRequest request, HttpServletResponse response, Area area) throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, ControladorGestionTitulacionesPreferentesArea.ACCION_SELECCIONAR_AREA);
		params.put(ControladorGestionTitulacionesPreferentesArea.PARAM_AREA, area.getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(request, response, params);
	}
}
