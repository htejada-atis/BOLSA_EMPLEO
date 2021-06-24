package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/**
 * Gestión de las titulacionesde usuarios de UVIRTUAL.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.mistitulaciones",
	description = "Gestión de las titulaciones de usuario",
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/mistitulaciones",
			"/srv/en/informacionadministrativa/bolsaempleo/mistitulaciones",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/mistitulaciones",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/mistitulaciones"
	})
public class ControladorMisTitulaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisTitulaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ID = "id";
	public static final String ACCION_INDEX = "index";
	public static final String PARAM_TITULACION = "titulacion";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_DESCRIPCION = "descripcion";
	public static final String PARAM_OTRA_TITULACION = "otratitulacion";
	public static final String PARAM_TITULACIONES_USUARIOS_SELECCIONADOS = "titulacionesusuariosselected";

	// acciones
	public static final String ACCION_DATATABLE_TITULACIONES_USUARIO = "datatabletitulacionesusuario";
	public static final String ACCION_DATATABLE_TITULACIONES = "datatabletitulaciones";
	public static final String ACCION_FORMULARIO_TITULACIONES_USUARIO = "formulariotitulacionesusuario";
	public static final String ACCION_TITULACION_SELECCIONADA = "titulacionseleccionada";
	public static final String ACCION_AGREGAR_TITULACION = "agregartitulacion";
	public static final String ACCION_AGREGAR_TITULACION_CONFIRM = "agregartitulacionconfirm";
	public static final String ACCION_ELIMINAR_TITULACION_USUARIO = "eliminartitulacionusuario";
	
	// mensajes
	public static final String MENSAJE_EXITO_AGREGAR = "Titulación creada correctamente";
	public static final String MENSAJE_EXITO_TITULACION_BORRADA = "Titulación eliminada correctamente";
	public static final String MENSAJE_ERROR_DESCRIPCION_REQUERIDO = "El campo descripción es obligatorio";
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGA = "La descripción no puede contener mas de %d caracteres";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/mistitulaciones";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mistitulaciones/";
	public static final String JSP_INDEX = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mistitulaciones/index.jsp";
		
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
		if (ServletFileUpload.isMultipartContent(request)) {
			nombreAccion = ACCION_AGREGAR_TITULACION_CONFIRM;
		}
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(JSP_INDEX);
					break;					
				case ACCION_DATATABLE_TITULACIONES_USUARIO:
					listadoTitulacionesUsuario(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_TITULACIONES:
					listadoTitulaciones(bean, datos, request, response);
					break;
				case ACCION_FORMULARIO_TITULACIONES_USUARIO:
					formularioTitulacionesUsuario(bean);
					break;
				case ACCION_TITULACION_SELECCIONADA:
					seleccionarTitulacion(bean, request);
					break;
				case ACCION_AGREGAR_TITULACION:
					agregarTitulacionFormulario(bean);
					break;
				case ACCION_AGREGAR_TITULACION_CONFIRM:
					agregarTitulacion(datos, request, response, bean);
					break;
				case ACCION_ELIMINAR_TITULACION_USUARIO:
					eliminarTitulacionUsuario(datos, request, response, bean);
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
		} catch (FileUploadException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al subir fichero");
		} finally {
			datos.getVistas().put(bean.getClass().getName(), bean);
			datos.getFicherosJSP().add(bean.getVista());
			datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
			datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
			datos.getFicherosJS().add("/js/jquery.ui.datepicker-es.js");
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private void init(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_CANDIDATO)) {
				throw new UVException("No eres un candidato");
			}
			
			bean.setSePuedeAgregar(ModeloSolicitud.obtenerInstancia().comprobarTitulacionPuedeSerCreada());
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}		
	}
	
	private void errorFatal(VistaTitulaciones bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	/** Redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	
	
	private void listadoTitulaciones(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Titulacion> dataTable = modelo.listaTitulacionesDatatable(request.getParameterMap(), bean.getUsuarioLogeado().getCodNum());
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
	}
	
	private void listadoTitulacionesUsuario(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<TitulacionUsuario> dataTable = modelo.listaTitulacionesUsuarioDatatable(
						request.getParameterMap(),
						bean.getUsuarioLogeado().getCodNum()
				);
				bean.setDatatableTitulacionesUsuario(dataTable);
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
	
	private void formularioTitulacionesUsuario(VistaTitulaciones bean) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mistitulaciones/formMisTitulaciones.jsp");
	}
	
	private void seleccionarTitulacion(VistaTitulaciones bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION));
		Titulacion titulacion = modelo.listaTitulacion(codNum);
			
		bean.setTitulacion(titulacion);
		bean.setVista(RUTA_BEP_CONF + "formMisTitulaciones.jsp");
	}
	
	private void agregarTitulacionFormulario(VistaTitulaciones bean) throws SQLException, UVException {			
		bean.setVista(RUTA_BEP_CONF + "formMisTitulaciones.jsp");		
	}
	
	private void agregarTitulacion(UVDatos datos, HttpServletRequest request, HttpServletResponse response, VistaTitulaciones bean) 
			throws SQLException, UVException, IOException, FileUploadException {		
		agregarTitulacionFormulario(bean);
		
		// comprobamos si se puede añadir titulaciones.
		if (!ModeloSolicitud.obtenerInstancia().comprobarTitulacionPuedeSerCreada()) {
			throw new UVException("No se puede añadir, la convocatoria está cerrada");
		}
		
		// leemos los parametros del form, chequeando el fichero
		List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		HashMap<String, Object> parametros = new HashMap<>();		
		for (FileItem item : items) {
			if (item.isFormField()) {
				parametros.put(item.getFieldName(), item.getString());
			} else {
				if (item.getSize() > 0 && item.getName().toLowerCase().endsWith(".pdf")) {									
					try (InputStream contenidoDelFichero = item.getInputStream(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
						parametros.put(PARAM_ARCHIVO, BolsaEmpleoUtils.checkFileSize(contenidoDelFichero));
					}
				}
			}
		}
		
		// validamos e insertamos
		TitulacionUsuario titulacion = validarTitulacion(parametros, bean);			
		ModeloMisTitulaciones.obtenerInstancia().insertaTitulacionUsuario(titulacion, bean.getUsuarioLogeado());
			
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());		
	}
	
	private void eliminarTitulacionUsuario(UVDatos datos, HttpServletRequest request, HttpServletResponse response, VistaTitulaciones bean) throws SQLException, UVException, IOException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TITULACIONES_USUARIOS_SELECCIONADOS));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		
		List<TitulacionUsuario> titulaciones = new ArrayList<>();
		
		for (TitulacionUsuario tu : modelo.getTitulacionesUsuarioByIds(selected)) {
			if (!tu.getUsuario().getCodNum().equals(bean.getUsuarioLogeado().getCodNum())) {
				throw new UVException("No tienes permisos");
			}
			
			if (!ModeloSolicitud.obtenerInstancia().comprobarTitulacionUsuarioPuedeSerBorrada(tu)) {
				BolsaEmpleoUtils.addMensajeDeError(String.format("La titulación %s no puede ser borrada", tu.getTitulacion().getNombre()), bean, request);
			} else {
				titulaciones.add(tu);
			}
		}

		if (!titulaciones.isEmpty()) {
			modelo.borraTitulacionUsuario(titulaciones, bean.getUsuarioLogeado());
			bean.getMensajesDeExito().add(MENSAJE_EXITO_TITULACION_BORRADA);
		}
		
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());		
	}
	
	private TitulacionUsuario validarTitulacion(HashMap<String, Object> parametros, VistaTitulaciones bean) throws UVException, SQLException {
		// otra titulacion						
		Titulacion titulacionCont = null;
		String otraTitulacion = null;
		
		if (Formateador.leeParametroInteger((String) parametros.get(PARAM_ID)) != null) {
			titulacionCont = ModeloTitulacion.obtenerInstancia().listaTitulacion(
					Formateador.leeParametroInteger((String) parametros.get(PARAM_ID)));
		} else {
			otraTitulacion = Formateador.leeParametroString((String) parametros.get(PARAM_OTRA_TITULACION));
			if (otraTitulacion == null) {
				throw new UVException("Introduce un nombre para la titulación");				
			}
			if (otraTitulacion.length() > ModeloMisTitulaciones.COLUMN_OTRATITULACION_MAXLENGTH) {
				throw new UVException(String.format("El nombre para la titulación debe ser como máximo %d caracteres", 
						ModeloMisTitulaciones.COLUMN_OTRATITULACION_MAXLENGTH));
			}
		}
		
		TitulacionUsuario t = new TitulacionUsuario();
		
		if (titulacionCont != null) {
			t.setTitulacion(titulacionCont);
		} else {
			t.setOtraTitulacion(otraTitulacion);
		}
		
		t.setDescripcion(EscapaHTML.ajustaCodificacion((String) parametros.get(PARAM_DESCRIPCION)));
		if (t.getDescripcion() == null || t.getDescripcion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_DESCRIPCION_REQUERIDO);
		}
		if (t.getDescripcion().length() > ModeloMisTitulaciones.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_LARGA, ModeloMisTitulaciones.COLUMN_DESCRIPCION_MAXLENGTH));
		}
		
		t.setUsuario(bean.getUsuarioLogeado());
		t.setArchivo((InputStream) parametros.get(PARAM_ARCHIVO));
		
		return t;
	}
}
