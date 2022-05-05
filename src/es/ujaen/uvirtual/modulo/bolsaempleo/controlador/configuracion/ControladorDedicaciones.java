package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaDedicacion;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Servlet implementation class ControladorDedicaciones.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.dedicaciones",
	description = "Gestión de dedicaciones bolsa empleo",
	urlPatterns = {
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/dedicaciones",
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/dedicaciones",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/dedicaciones",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/dedicaciones"
	})
public class ControladorDedicaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorDedicaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_DATATABLE_DEDICACIONES = "datatablededicaciones";
	public static final String ACCION_EDITAR_DEDICACION = "editardedicacion";
	public static final String ACCION_ELIMINAR_DEDICACION = "eliminardedicacion";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_NUEVA_DEDICACION = "nuevadedicacion";
	public static final String ACCION_RESTAURAR_DEDICACION = "restaurardedicacion";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACTIVA = "activa";
	public static final String PARAM_DEDICACION = "dedicacion";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_TIPO = "tipo";
	public static final String PARAM_TEXTO_DEDICACION = "textodedicacion";
	public static final String PARAM_SUELDO = "sueldo";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_TEXTO_VACIO = "El texto no puede estar vacío";
	public static final String MENSAJE_ERROR_SUELDO_VACIO = "El sueldo no puede estar vacío";
	public static final String MENSAJE_ERROR_TIPO_NO_VALIDO = "El tipo seleccionado no es válido";
	public static final String MENSAJE_ERROR_TIPO_VACIO = "El tipo no puede estar vacío";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso";
	
	public static final String MENSAJE_EXITO_AGREGAR = "Dedicación creada correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "Dedicación editada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Dedicación eliminada correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Dedicación restaurada correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_DEDICACIONES = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/dedicaciones/";
	public static final String JSP_INDEX = RUTA_BEP_DEDICACIONES + "index.jsp";
	public static final String JSP_FORM = RUTA_BEP_DEDICACIONES + "formDedicacion.jsp";
	
	// errors
	public static final Integer RESPONSE_HTTP_CODE_ERROR_400 = 400;
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/dedicaciones";
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
		
		VistaDedicacion bean = new VistaDedicacion();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null || nombreAccion.isEmpty()) {
			nombreAccion = ACCION_INDEX;
		}
		try {
			if (!init(bean, datos, request, response)) {
				return;
			}
			
			switch (nombreAccion) {
				case ACCION_DATATABLE_DEDICACIONES:
					listaDedicaciones(bean, datos, request, response);
					break;
				case ACCION_INDEX:
					break;
				case ACCION_EDITAR_DEDICACION:
					editarDedicacion(bean, datos, request, response);
					break;
				case ACCION_ELIMINAR_DEDICACION:
					eliminaDedicacion(bean, datos, request, response);
					break;
				case ACCION_NUEVA_DEDICACION:
					nuevaDedicacion(bean, datos, request, response);
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
	
	private boolean init(VistaDedicacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));
			
			// personal, comision, direccion
			int[] rolesValidos = {ModeloRol.ID_ROL_SERVICIO_PERSONAL, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO};
			boolean contains = IntStream.of(rolesValidos).
					anyMatch(x -> x == bean.getUsuarioLogeado().getRol().getCodNum());
			
			if (!contains) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private void errorFatal(VistaDedicacion bean, String mensaje) {
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
	
	private void editarDedicacion(VistaDedicacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, UVException, SQLException {
		bean.setVista(JSP_FORM);
		ModeloDedicacion modeloDedicacion = ModeloDedicacion.obtenerInstancia();
		Dedicacion dedicacion = modeloDedicacion.getDedicacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_DEDICACION)));
		bean.setDedicacion(dedicacion);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENVIAR)) != null) {
			dedicacion = validarDedicacion(dedicacion, request);
			modeloDedicacion.actualizaDedicacion(dedicacion, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void eliminaDedicacion(VistaDedicacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		ModeloDedicacion modeloDedicacion = ModeloDedicacion.obtenerInstancia();
		
		Dedicacion dedicacion = modeloDedicacion.getDedicacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_DEDICACION)));
		bean.setDedicacion(dedicacion);
		Boolean activa = "true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACTIVA)));
		dedicacion.setActiva(activa);
		modeloDedicacion.actualizaActivaDedicacion(dedicacion, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(activa ? MENSAJE_EXITO_RESTAURAR : MENSAJE_EXITO_ELIMINAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void nuevaDedicacion(VistaDedicacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, UVException, SQLException {
		bean.setVista(JSP_FORM);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENVIAR)) != null) {
			Dedicacion dedicacion = validarDedicacion(new Dedicacion(), request);
			ModeloDedicacion.obtenerInstancia().insertaDedicacion(dedicacion, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void listaDedicaciones(VistaDedicacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Dedicacion> dataTable = ModeloDedicacion.obtenerInstancia().listadoDedicaciones(request.getParameterMap());
				bean.setDatatableDedicaciones(dataTable);
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
	
	private Dedicacion validarDedicacion(Dedicacion dedicacion, HttpServletRequest request) throws UVException {
		dedicacion.setTexto(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO_DEDICACION)));
		if (dedicacion.getTexto() == null || dedicacion.getTexto().isBlank()) {
			throw new UVException(MENSAJE_ERROR_TEXTO_VACIO);
		}
		
		dedicacion.setSueldo(Formateador.leeParametroDouble(request.getParameter(PARAM_SUELDO)));
		if (dedicacion.getSueldo() == null) {
			throw new UVException(MENSAJE_ERROR_SUELDO_VACIO);
		}
		
		dedicacion.setTipo(Formateador.leeParametroString(request.getParameter(PARAM_TIPO)));
		if (dedicacion.getTipo() == null || dedicacion.getTipo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_TIPO_VACIO);
		}
		if (!ModeloDedicacion.TIPOS_DEDICACION.containsKey(dedicacion.getTipo())) {
			throw new UVException(MENSAJE_ERROR_TIPO_NO_VALIDO);
		}
		
		return dedicacion;
	}
	
}
