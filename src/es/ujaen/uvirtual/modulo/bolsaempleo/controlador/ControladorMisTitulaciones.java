package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoValidator;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/**
 * Gestión de las titulaciones de usuarios de UVIRTUAL.
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
@MultipartConfig
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
	public static final String PARA_AGREGAR_TITULACION = "agregartitulacion";
	public static final String PARAM_FICHERO = "fichero";
	public static final String PARAM_TITULACIONES_USUARIOS_SELECCIONADOS = "titulacionesusuariosselected";

	// acciones
	public static final String ACCION_ENVIAR_MISTITULACIONES = "enviarmistitulaciones";
	public static final String ACCION_DATATABLE_TITULACIONES_USUARIO = "datatabletitulacionesusuario";
	public static final String ACCION_DATATABLE_TITULACIONES = "datatabletitulaciones";
	public static final String ACCION_FORMULARIO_TITULACIONES_USUARIO = "formulariotitulacionesusuario";
	public static final String ACCION_TITULACION_SELECCIONADA = "titulacionseleccionada";
	public static final String ACCION_AGREGAR_TITULACION = "agregartitulacion";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_ELIMINAR_TITULACION_USUARIO = "eliminartitulacionusuario";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_AGREGAR = "Titulación creada correctamente";
	public static final String MENSAJE_EXITO_TITULACION_BORRADA = "Titulación eliminada correctamente";
	
	public static final String MENSAJE_ERROR_ARCHIVO_VACIO = "El archivo no puede estar vacio";

	public static final String MENSAJE_ERROR_TELEFONO_STRING = "El telefono debe ser un número";
	public static final String MENSAJE_ERROR_MOVIL_STRING = "El móvil debe ser un número";
	public static final String MENSAJE_ERROR_CODIGO_POSTAL_STRING = "El código postal debe ser un número";
	
	public static final String MENSAJE_ERROR_DESCRIPCION_VACIO = "El campo descripción no puede estar vacio";
	public static final String MENSAJE_ERROR_DESCRIPCION_REQUERIDO = "El campo descripción es obligatorio";
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGA = "La descripción no puede contener mas de %d caracteres";

	public static final String URL_PATTERN_FILES_PRIVADA = "/srv/es/informacionadministrativa/bolsaempleo/mistitulaciones";
	
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/mistitulaciones";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mistitulaciones/";
		
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaTitulaciones bean = new VistaTitulaciones();		
		Usuario usuario = datos.getUsuario();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			modeloUsuario.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mistitulaciones/index.jsp");
					break;					
				case ACCION_DATATABLE_TITULACIONES_USUARIO:
					listadoTitulacionesUsuario(bean, datos, request, response, modeloUsuario.getUsuarioByCodCuenta(usuario.getUid()).getCodNum());
					break;
				case ACCION_DATATABLE_TITULACIONES:
					listadoTitulaciones(bean, datos, request, response, modeloUsuario.getUsuarioByCodCuenta(usuario.getUid()).getCodNum());
					break;
				case ACCION_FORMULARIO_TITULACIONES_USUARIO:
					formularioTitulacionesUsuario(bean);
					break;
				case ACCION_TITULACION_SELECCIONADA:
					seleccionarTitulacion(bean, request);
					break;
				case ACCION_AGREGAR_TITULACION:
					agregarTitulacion(request, response, bean, modeloUsuario.getUsuarioByCodCuenta(usuario.getUid()));
					break;
				case ACCION_ELIMINAR_TITULACION_USUARIO:
					eliminarTitulacionUsuario(request, bean);
					break;
				case ACCION_DESCARGAR_FICHERO:
					descargarFichero(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
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
	
	/** carga las titulaciones en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param codnum .
	 * @throws IOException en caso de error de input u output .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoTitulaciones(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, Integer codnum) 
			throws IOException, SQLException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Titulacion> dataTable = modelo.listaTitulacionesDatatable(request.getParameterMap(), codnum);
				bean.setDatatableTitulaciones(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (UVException ex) {
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	
	/** carga las titulaciones de un usuario en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param codnum .
	 * @throws IOException en caso de error de input u output .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoTitulacionesUsuario(VistaTitulaciones bean, UVDatos datos,
			HttpServletRequest request, HttpServletResponse response, Integer codnum) throws IOException, SQLException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Titulacion> dataTable = modelo.listaTitulacionesUsuarioDatatable(request.getParameterMap(), codnum);
				bean.setDatatableTitulaciones(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (UVException ex) {
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	/** dirige al formulario de creación de una nueva titulación para un usuario.
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void formularioTitulacionesUsuario(VistaTitulaciones bean) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mistitulaciones/formMisTitulaciones.jsp");
	}
	
	private void seleccionarTitulacion(VistaTitulaciones bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION));
		Titulacion titulacion = modelo.listaTitulacion(codNum);
				
		bean.setTitulacion(titulacion);
		bean.setVista(RUTA_BEP_CONF + "formMisTitulaciones.jsp");
	}
	
	
	/** agrega una nueva titulación a un usuario.
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @param usu .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws ServletException .
	 */
	public void agregarTitulacion(HttpServletRequest request, HttpServletResponse response, VistaTitulaciones bean, UsuarioBolsaEmpleo usu) 
			throws SQLException, UVException, IOException, ServletException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();		
				
		bean.setVista(RUTA_BEP_CONF + "formMisTitulaciones.jsp");
		
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));		
		Titulacion titulacionCont = modelo.listaTitulacion(codNum);
		
		Part uploadedFile = request.getPart(PARAM_ARCHIVO);
		
		if (uploadedFile != null) {
			Titulacion titulacion = this.validarTitulacion(request, uploadedFile);
			titulacion.setTitulacion(titulacionCont);
			titulacion.setUsuario(usu);
				
			modelo.insertaTitulacionUsuario(titulacion);
				
			bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR);
			response.sendRedirect(request.getServletPath());	
		}
	}
	
	private void eliminarTitulacionUsuario(HttpServletRequest request, VistaTitulaciones bean) throws SQLException, UVException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TITULACIONES_USUARIOS_SELECCIONADOS));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		
		List<Titulacion> titulaciones = modelo.getTitulacionesUsuarios(selected);

		modelo.borraTitulacionUsuario(titulaciones);
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mistitulaciones/index.jsp");

		bean.getMensajesDeExito().add(MENSAJE_EXITO_TITULACION_BORRADA);
	}
	
	/** descarga un fichero .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void descargarFichero(VistaTitulaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_FICHERO)) != null) {
			Titulacion titulacion = modelo.listaTitulacionUsuario(Formateador.leeParametroInteger(request.getParameter(PARAM_FICHERO)));
			bean.setTitulacion(titulacion);
			
			response.setContentType("application/pdf");
			datos.setRespuestaEnviada(true);

			try (ServletOutputStream stream = response.getOutputStream(); BufferedInputStream buf = new BufferedInputStream(titulacion.getArchivo())) {
				int readBytes = 0;
				while ((readBytes = buf.read()) != -1) {
					stream.write(readBytes);
				}
				stream.flush();
	        }
		}
	}
	
	private Titulacion validarTitulacion(HttpServletRequest request, Part uploadedFile) throws UVException, IOException, SQLException {
		Titulacion t = new Titulacion();
		
		t.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DESCRIPCION)));
		if (t.getDescripcion() == null || t.getDescripcion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_DESCRIPCION_REQUERIDO);
		}
		if (t.getDescripcion().length() > ModeloMisTitulaciones.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_LARGA, ModeloMisTitulaciones.COLUMN_DESCRIPCION_MAXLENGTH));
		}
				
		String nombre = BolsaEmpleoUtils.obtenerNombreFichero(uploadedFile);				
		int i = nombre.lastIndexOf('.');
		
		if (i > 0) {
			String extension = nombre.substring(i + 1);
			if (!"pdf".equalsIgnoreCase(extension)) {
				throw new UVException("No se puede subir un fichero que sea distinto de pdf");
		    }
		}
		
		t.setArchivo(uploadedFile.getInputStream());
		BolsaEmpleoUtils.checkFileSize(t.getArchivo());
		
		return t;
	}
}
