package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMensajes;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de mensajes.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.mensajeria", 
	description = "Gestión de afinidades", 
	urlPatterns = {
		"/srv/es/informacionadministrativa/bolsaempleo/configuracion/mensajeria",
		"/srv/en/informacionadministrativa/bolsaempleo/configuracion/mensajeria",
		"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/mensajeria",
		"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/mensajeria" 
})
public class ControladorMensajes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMensajes.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parametros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_MENSAJE_ID = "mensaje";
	public static final String PARAM_TITULO = "titulo";
	public static final String PARAM_CUERPO = "cuerpo";
	
	// acciones
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_NUEVO_MENSAJE = "nuevoMensaje";
	public static final String ACCION_BORRAR_MENSAJE = "borrarMensaje";
	public static final String ACCION_DETALLE_MENSAJE = "detalleMensaje";
	public static final String ACCION_MODIFICAR_MENSAJE = "modificarMensaje";
	public static final String ACCION_DATATABLE_DESTINATARIOS = "datatableDestinatarios";
	public static final String ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES = "datatableDestinatariosDisponibles";
		
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";

	// ruta vistas
	public static final String RUTA_BEP_MEN = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/mensajeria/";
	public static final String JSP_INDEX = RUTA_BEP_MEN + "index.jsp"; 
	public static final String JSP_DETALLE = RUTA_BEP_MEN + "formMensaje.jsp";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/mensajeria";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	/**
	 * Peticion GET.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");

		VistaMensajes bean = new VistaMensajes();
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());

		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}

		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_BORRAR_MENSAJE:
				case ACCION_DATATABLE_DESTINATARIOS:
				case ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES:
				case ACCION_DETALLE_MENSAJE:
					accionesMensaje(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_INDEX:
					index(bean);
					break;
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_NUEVO_MENSAJE:
					nuevoMensaje(bean, request, response);
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
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
		}
	}

	private void init(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}
	}

	private void errorFatal(VistaMensajes bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}

	/**
	 * redireccion de do post.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void index(VistaMensajes bean) {
		bean.setVista(JSP_INDEX);
	}
	
	private void accionesMensaje(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		Integer idMensaje = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_MENSAJE_ID));
		bean.setMensaje(ModeloMensajes.obtenerInstancia().getMensajeById(idMensaje));
		
		switch (nombreAccion) {
			case ACCION_BORRAR_MENSAJE:
				borrarMensaje(bean, request, response);
				break;
			case ACCION_DATATABLE_DESTINATARIOS:
				listadoDestinatarios(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES:
				listadoDestinatariosDisponibles(bean, datos, request, response);
				break;
			case ACCION_DETALLE_MENSAJE:
				detalleMensaje(bean);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}

	private void listado(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Mensaje> dataTable = ModeloMensajes.obtenerInstancia().listaMensajesDatatable(request.getParameterMap());
				bean.setDatatableMensajes(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
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
	}
	
	private void nuevoMensaje(VistaMensajes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		Integer idMensaje = ModeloMensajes.obtenerInstancia().nuevoMensajeEnBorrador(bean.getUsuarioLogeado());
		bean.setMensaje(ModeloMensajes.obtenerInstancia().getMensajeById(idMensaje));
		
		redireccionConMensajeSeleccionado(bean, request, response, ACCION_DETALLE_MENSAJE);
	}
	
	private void borrarMensaje(VistaMensajes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloMensajes.obtenerInstancia().eliminarMensajeBorrador(bean.getMensaje(), bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito("Mensaje eliminado correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void detalleMensaje(VistaMensajes bean) throws SQLException {
		bean.setVista(JSP_DETALLE);
		bean.setConvocatorias(ModeloConvocatoria.obtenerInstancia().listaConvocatorias());
		bean.setAreas(ModeloArea.obtenerInstancia().listaAreas());
	}
	
	private void redireccionConMensajeSeleccionado(VistaMensajes bean, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_ACCION, accion);
		params.put(PARAM_MENSAJE_ID, bean.getMensaje().getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(request, response, params);
	}
	
	private void listadoDestinatarios(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			try {
				ModeloMensajes modelo = ModeloMensajes.obtenerInstancia();
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaDestinatariosMensajeDatatable(request.getParameterMap(), bean.getMensaje());
				bean.setDatatableDestinatarios(dataTable);
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
	}
	
	private void listadoDestinatariosDisponibles(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			try {
				ModeloMensajes modelo = ModeloMensajes.obtenerInstancia();
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaDestinatariosDisponiblesMensajeDatatable(
						request.getParameterMap(), bean.getMensaje());
				bean.setDatatableDestinatarios(dataTable);
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
	}
}
