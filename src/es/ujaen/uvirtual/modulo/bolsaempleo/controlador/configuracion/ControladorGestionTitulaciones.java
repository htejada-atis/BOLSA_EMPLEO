package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, modificar y eliminar titulaciones .
 * Controlador - Opers. con nombres: obtener, modificar, eliminar .
 * */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.titulaciones", 
	description = "Gestión de titulaciones", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/titulaciones", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/titulaciones",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulaciones",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/titulaciones"
	})
public class ControladorGestionTitulaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionTitulaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_AGREGAR_TITULACION = "agregartitulacion";
	public static final String ACCION_BORRAR_TITULACION = "borrartitulacion";
	public static final String ACCION_DATATABLE_TITULACIONES = "datatabletitulaciones";
	public static final String ACCION_INDEX = "listartitulaciones";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_TITULACIONES = "titulaciones";

	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensajeenviado";
	public static final String MENSAJE_EXITO_AGREGAR = "Titulación agregada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Titulación eliminada correctamente";
	public static final String MENSAJE_ERROR_ELIMINAR_TITULACION = "No se puede eliminar una titulación que está asignada a un área";
	public static final String MENSAJE_ERROR_NOMBRE_VACIO = "El nombre es obligatorio para almacenar una titulación";
	public static final String MENSAJE_ERROR_NOMBRE_LARGO = "El nombre no puede contener mas de %d caracteres";
	
	// ajax
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/titulaciones/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulaciones";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaTitulaciones bean = new VistaTitulaciones();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(RUTA_BEP_CONF + "titulaciones.jsp");
					break;
				case ACCION_AGREGAR_TITULACION:
					agregarTitulacion(bean, datos, request, response);
					break;
				case ACCION_BORRAR_TITULACION:
					eliminarTitulacion(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_TITULACIONES:
					listadoTitulaciones(bean, datos, request, response);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
		}
	}
	
	private void init(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "titulaciones.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}		
	}
	
	private void errorFatal(VistaTitulaciones bean, String mensaje) {
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
	
	/** eliminar una titulación.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void eliminarTitulacion(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		try {
			modelo.borraTitulacion(new Titulacion(codNum), bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR, bean, request);
		} catch (SQLIntegrityConstraintViolationException e) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_ELIMINAR_TITULACION, bean, request);
		}
		
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	/** agrega una nueva titulación .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void agregarTitulacion(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formTitulacion.jsp");
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE)) != null) {
			Titulacion titulacion = this.validateTitulacion(request); 							
			ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
			modelo.insertaTitulacion(titulacion, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
			datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
		}
	}
	
	/** carga las titulaciones en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de input u output .
	 */
	private void listadoTitulaciones(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Titulacion> dataTable = modelo.listaTitulacionesDatatable(request.getParameterMap());
				bean.setDatatableTitulaciones(dataTable);
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
		
		datos.setRespuestaEnviada(true);
	}
	
	private Titulacion validateTitulacion(HttpServletRequest request) throws UVException {
		Titulacion t = new Titulacion();
		
		t.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE)));
		if (t.getNombre() == null || t.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (t.getNombre().length() > ModeloTitulacion.COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_LARGO, ModeloTitulacion.COLUMN_NOMBRE_MAXLENGTH));
		}
		
		return t;
	}
}
