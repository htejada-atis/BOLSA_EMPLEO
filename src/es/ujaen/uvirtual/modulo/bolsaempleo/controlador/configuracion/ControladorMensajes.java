package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Destinatario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
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
	public static final String PARAM_BORRAR = "borrar";
	public static final String PARAM_CUERPO = "cuerpo";
	public static final String PARAM_DESTINATARIOS = "destinatarios";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_GUARDAR = "guardar";
	public static final String PARAM_MENSAJE_ID = "mensaje";
	public static final String PARAM_TITULO = "titulo";
	
	// acciones
	public static final String ACCION_AGREGAR_DESTINATARIOS = "agregardestinatarios";
	public static final String ACCION_BORRAR_MENSAJE = "borrarMensaje";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DATATABLE_DESTINATARIOS = "datatableDestinatarios";
	public static final String ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES = "datatableDestinatariosDisponibles";
	public static final String ACCION_DETALLE_MENSAJE = "detalleMensaje";
	public static final String ACCION_ELIMINAR_DESTINATARIOS = "eliminardestinatarios";
	public static final String ACCION_ENVIAR_MENSAJE = "enviarmensaje";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_MODIFICAR_MENSAJE = "modificarMensaje";
	public static final String ACCION_NUEVO_MENSAJE = "nuevoMensaje";
		
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_CUERPO_VACIO = "El cuerpo del mensaje no puede estar vacio";
	public static final String MENSAJE_ERROR_USUARIOS_SELECCIONADOS_INCORRECTOS = "Los usuarios seleccionados no son válidos";
	public static final String MENSAJE_ERROR_TITULO_LARGO = "El título no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_TITULO_VACIO = "El título del mensaje no puede estar vacio";
	public static final String MENSAJE_EXITO_DESTINATARIOS_AGREGADOS = "Destinatarios agregados correctamente";
	public static final String MENSAJE_EXITO_DESTINATARIO_AGREGADO = "Destinatario agregado correctamente";
	public static final String MENSAJE_EXITO_DESTINATARIOS_BORRADOS = "Destinatarios borrados correctamente";
	public static final String MENSAJE_EXITO_DESTINATARIO_BORRADO = "Destinatario borrado correctamente";
	public static final String MENSAJE_EXITO_ENVIADO = "Mensaje enviado correctamente";
	public static final String MENSAJE_EXITO_MENSAJE_GUARDADO = "Mensaje guardado correctamente";

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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
				case ACCION_AGREGAR_DESTINATARIOS:
				case ACCION_BORRAR_MENSAJE:
				case ACCION_DATATABLE_DESTINATARIOS:
				case ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES:
				case ACCION_DETALLE_MENSAJE:
				case ACCION_ELIMINAR_DESTINATARIOS:
				case ACCION_ENVIAR_MENSAJE:
				case ACCION_MODIFICAR_MENSAJE:
					accionesMensaje(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_INDEX:
					index(bean);
					break;
				case ACCION_DATATABLE:
					listadoMensajes(bean, datos, request, response);
					break;
				case ACCION_NUEVO_MENSAJE:
					nuevoMensaje(bean, datos, request, response);
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
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	private void index(VistaMensajes bean) {
		bean.setVista(JSP_INDEX);
	}
	
	private void accionesMensaje(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		bean.setVista(JSP_DETALLE);
		
		ModeloMensajes modeloMensajes = ModeloMensajes.obtenerInstancia();
		Integer idMensaje = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_MENSAJE_ID));
		bean.setMensaje(modeloMensajes.getMensajeById(idMensaje));
		
		bean.setConvocatorias(ModeloConvocatoria.obtenerInstancia().listaConvocatorias());
		bean.setAreas(ModeloArea.obtenerInstancia().listaAreas());
		bean.setDestinatarios(modeloMensajes.obtenerDestinatariosMensaje(bean.getMensaje()));
		
		switch (nombreAccion) {
			case ACCION_AGREGAR_DESTINATARIOS:
				agregarDestinatarios(bean, datos, request, response);
				break;
			case ACCION_BORRAR_MENSAJE:
				borrarMensaje(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_DESTINATARIOS:
				listadoDestinatarios(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES:
				listadoDestinatariosDisponibles(bean, datos, request, response);
				break;
			case ACCION_DETALLE_MENSAJE:
				break;
			case ACCION_ELIMINAR_DESTINATARIOS:
				eliminarDestinatarios(bean, datos, request, response);
				break;
			case ACCION_ENVIAR_MENSAJE:
				enviarMensaje(bean, datos, request, response);
				break;
			case ACCION_MODIFICAR_MENSAJE:
				guardarMensaje(bean, datos, request, response); 
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void agregarDestinatarios(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, IOException {
		ModeloMensajes modeloMensaje = ModeloMensajes.obtenerInstancia();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		List<UsuarioBolsaEmpleo> destinatarios = null;
		try {
			
			try {
				destinatarios = new ArrayList<UsuarioBolsaEmpleo>();
				List<String> idDestinatarios = new GsonBuilder().create().fromJson(request.getParameter(PARAM_DESTINATARIOS),
						new TypeToken<List<String>>() { }.getType());
				for (String idDestinatario: idDestinatarios) { 
					destinatarios.add(modeloUsuario.getUsuarioById(Integer.parseInt(idDestinatario)));
				}
			} catch (Exception ex) {
				throw new UVException(MENSAJE_ERROR_USUARIOS_SELECCIONADOS_INCORRECTOS);
			}
			
			for (UsuarioBolsaEmpleo destinatario: destinatarios) {
				modeloMensaje.agregarDestinatario(bean.getMensaje(), destinatario, bean.getUsuarioLogeado());
			}
			
			if (destinatarios.size() > 0) {
				BolsaEmpleoUtils.addMensajeDeExito(destinatarios.size() > 1 ? MENSAJE_EXITO_DESTINATARIOS_AGREGADOS : MENSAJE_EXITO_DESTINATARIO_AGREGADO,
						bean, request);
			}
		
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(ex.getMessage(), bean, request);
		}
		
		redireccionConMensajeSeleccionado(bean, datos, request, response, ACCION_DETALLE_MENSAJE);
	}
	
	private void borrarMensaje(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloMensajes.obtenerInstancia().eliminarMensajeBorrador(bean.getMensaje(), bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito("Mensaje eliminado correctamente", bean, request);
		datos.setRespuestaEnviada(true);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void eliminarDestinatarios(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, IOException {
		ModeloMensajes modeloMensaje = ModeloMensajes.obtenerInstancia();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		List<UsuarioBolsaEmpleo> destinatarios = null;
		try {
			
			try {
				destinatarios = new ArrayList<UsuarioBolsaEmpleo>();
				List<String> idDestinatarios = new GsonBuilder().create().fromJson(request.getParameter(PARAM_DESTINATARIOS),
						new TypeToken<List<String>>() { }.getType());
				for (String idDestinatario: idDestinatarios) { 
					destinatarios.add(modeloUsuario.getUsuarioById(Integer.parseInt(idDestinatario)));
				}
			} catch (Exception ex) {
				throw new UVException(MENSAJE_ERROR_USUARIOS_SELECCIONADOS_INCORRECTOS);
			}
			
			for (UsuarioBolsaEmpleo destinatario: destinatarios) {
				modeloMensaje.eliminarDestinatario(bean.getMensaje(), destinatario, bean.getUsuarioLogeado());
			}
			
			if (destinatarios.size() > 0) {
				BolsaEmpleoUtils.addMensajeDeExito(destinatarios.size() > 1 ? MENSAJE_EXITO_DESTINATARIOS_BORRADOS : MENSAJE_EXITO_DESTINATARIO_BORRADO,
						bean, request);
			}
		
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(ex.getMessage(), bean, request);
		}
		
		redireccionConMensajeSeleccionado(bean, datos, request, response, ACCION_DETALLE_MENSAJE);
	}
	
	private void enviarMensaje(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, UVException, SQLException {
		Mensaje mensaje = bean.getMensaje();
		
		if (mensaje.getTitulo().equals(ModeloMensajes.MENSAJE_ESTADO_BORRADOR)) {
			throw new UVException("No se puede enviar un mensaje con título borrador");
		}
		
		if (bean.getDestinatarios().size() < 0) {
			throw new UVException("No se puede enviar un mensaje sin destinatarios");
		}
		
		ModeloMensajes.obtenerInstancia().actualizaEstadoMensajeComoEnviando(bean.getMensaje(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ENVIADO, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void guardarMensaje(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_GUARDAR)) != null) {
			ModeloMensajes modeloMensaje = ModeloMensajes.obtenerInstancia();
			Mensaje mensaje = this.getValidatorMensaje(request);
			mensaje.setCodNum(bean.getMensaje().getCodNum());
			mensaje.setEstado(bean.getMensaje().getEstado());
			
			modeloMensaje.actualizarMensaje(mensaje, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_MENSAJE_GUARDADO, bean, request);
			redireccionConMensajeSeleccionado(bean, datos, request, response, ACCION_DETALLE_MENSAJE);
		}
	}
	
	private void nuevoMensaje(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloMensajes modeloMensaje = ModeloMensajes.obtenerInstancia();
		Integer idMensaje = modeloMensaje.nuevoMensajeEnBorrador(bean.getUsuarioLogeado());
		bean.setMensaje(modeloMensaje.getMensajeById(idMensaje));
		
		redireccionConMensajeSeleccionado(bean, datos, request, response, ACCION_DETALLE_MENSAJE);
	}
	
	private void redireccionConMensajeSeleccionado(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_ACCION, accion);
		params.put(PARAM_MENSAJE_ID, bean.getMensaje().getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	private void listadoMensajes(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Mensaje> dataTable = ModeloMensajes.obtenerInstancia().listaMensajesDatatable(request.getParameterMap());
				bean.setDatatableMensajes(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
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
	
	private void listadoDestinatarios(VistaMensajes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			try {
				ModeloMensajes modelo = ModeloMensajes.obtenerInstancia();
				BolsaEmpleoDataTable<Destinatario> dataTable = modelo.listaDestinatariosMensajeDatatable(request.getParameterMap(), bean.getMensaje());
				bean.setDatatableDestinatarios(dataTable);
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
				bean.setDatatableDestinatariosDisponibles(dataTable);
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
	
	private Mensaje getValidatorMensaje(HttpServletRequest request) throws UVException {
		Mensaje mensaje = new Mensaje();
		
		mensaje.setTitulo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TITULO)));
		if (mensaje.getTitulo() == null || mensaje.getTitulo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_TITULO_VACIO);
		}
		if (mensaje.getTitulo().length() > ModeloMensajes.MENSAJES_COLUMN_TITULO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_TITULO_LARGO, ModeloMensajes.MENSAJES_COLUMN_TITULO_MAXLENGTH));
		}
		
		mensaje.setCuerpo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CUERPO)));
		if (mensaje.getCuerpo() == null || mensaje.getCuerpo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CUERPO_VACIO);
		}
		
		return mensaje;
	}
}
