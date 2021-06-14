package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloFichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFicheros;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, eliminar y agregar ficheros.
 * Controlador - Opers. con nombres: obtener, eliminar, agregar
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.ficheros", 
	description = "Gestión de ficheros", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/ficheros", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/ficheros",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/ficheros",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/ficheros"
	})
@MultipartConfig(maxFileSize = ModeloParametrosConfiguracion.MAX_FILE_SIZE)
public class ControladorGestionFicheros extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionFicheros.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	
	public static final String ACCION_BORRAR_FICHEROS = "borrarficheros";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_HACER_FICHEROS_PUBLICOS = "ficherospublicos";
	public static final String ACCION_HACER_FICHEROS_PRIVADOS = "ficherosprivados";
	public static final String ACCION_INDEX = "listarficheros";
	public static final String ACCION_SUBIR_FICHERO = "subirfichero";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_FICHERO = "fichero";
	public static final String PARAM_FICHEROS = "ficheros";
	public static final String PARAM_PUBLICO = "publico";
	public static final String PARAM_TITULO = "titulo";
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	
	public static final String MENSAJE_ERROR_EXTENSION_FICHERO = "Solo se admiten ficheros de tipo pdf";
	public static final String MENSAJE_ERROR_FICHEROS_SELECCIONADOS_INCORRECTOS = "Ficheros seleccionados no válidos";
	public static final String MENSAJE_ERROR_TITULO_LARGO = "El enlace no puede contener mas de %d caracteres";
	
	public static final String MENSAJE_EXITO_AGREGAR = "Fichero subido correctamente";
	public static final String MENSAJE_EXITO_CAMBIAR_PUBLICO = "Fichero publico cambiado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR_FICHERO = "Fichero eliminado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR_FICHEROS = "Ficheros eliminados correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/ficheros/";
	
	// urls
	public static final String URL_PATTERN_FILES_PRIVADA = "/srv/es/informacionadministrativa/bolsaempleo/configuracion/ficheros";
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/ficheros";
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
		
		VistaFicheros bean = new VistaFicheros();		
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(RUTA_BEP_CONF + "ficheros.jsp");
					break;
				case ACCION_BORRAR_FICHEROS:
					eliminarFicheros(bean, request, response);
					break;
				case ACCION_DATATABLE:
					listadoFicheros(bean, datos, request, response);
					return;
				case ACCION_HACER_FICHEROS_PUBLICOS:
					cambiarFicherosPublico(bean, request, response, true);
					break;
				case ACCION_HACER_FICHEROS_PRIVADOS:
					cambiarFicherosPublico(bean, request, response, false);
					break;
				case ACCION_SUBIR_FICHERO:
					agregarFichero(bean, request, response);
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
	
	private void init(VistaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "ficheros.jsp");
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
	
	private void errorFatal(VistaFicheros bean, String mensaje) {
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
	
	/** agrega un nuevo fichero .
	 * @param bean bean de la vista a la que poner los valores .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd .
	 * @throws ServltetException .
	 * @throws IOException en caso de error de input u output .
	 * @throws UVException en caso de error de parametros .
	 */
	private void agregarFichero(VistaFicheros bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, UVException {
		bean.setVista(RUTA_BEP_CONF + "formFichero.jsp");
		
		if (request.getParameter(PARAM_TITULO) != null) {
			ModeloFichero modelo = ModeloFichero.obtenerInstancia();
			Part uploadedFile = request.getPart(PARAM_ARCHIVO);
			if (uploadedFile != null) {
				if (uploadedFile.getSize() > 0) {
					
					Fichero fichero = this.validatorFichero(request, uploadedFile);										
					
					modelo.insertaFichero(fichero, bean.getUsuarioLogeado());
					BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
					response.sendRedirect(request.getServletPath());
				} else {
					throw new UVException("No se puede subir un fichero sin archivo");
				}
			}
		}
	}
	
	/** cambia ficheros a publicos .
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @param publico .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void cambiarFicherosPublico(VistaFicheros bean, HttpServletRequest request, HttpServletResponse response, Boolean publico)
			throws SQLException, UVException, IOException {
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		
		Gson gson = new GsonBuilder().create();
		
		try {
			List<Integer> ficheros = gson.fromJson(request.getParameter(PARAM_FICHEROS), new TypeToken<List<Integer>>() { }.getType());
			
			for (Integer fichero: ficheros) {
				modelo.modificarPublicoFichero(new Fichero(fichero, publico), bean.getUsuarioLogeado());
			}
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_CAMBIAR_PUBLICO, bean, request);
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_FICHEROS_SELECCIONADOS_INCORRECTOS, bean, request);
		}
		
		response.sendRedirect(request.getServletPath());
	}
	
	/** eliminar ficheros .
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de IO .
	 */
	private void eliminarFicheros(VistaFicheros bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		
		Gson gson = new GsonBuilder().create();
		
		try {
			List<Integer> ficheros = gson.fromJson(request.getParameter(PARAM_FICHEROS), new TypeToken<List<Integer>>() { }.getType());
			
			for (Integer fichero: ficheros) {
				modelo.borraFichero(new Fichero(fichero));
			}
			
			BolsaEmpleoUtils.addMensajeDeExito(ficheros.size() > 1 ? MENSAJE_EXITO_ELIMINAR_FICHEROS : MENSAJE_EXITO_ELIMINAR_FICHERO, bean, request);
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_FICHEROS_SELECCIONADOS_INCORRECTOS, bean, request);
		}
		
		response.sendRedirect(request.getServletPath());
	}
	
	/**
	 * Listado de ficheros .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws IOException en caso de error de IO .
	 */
	private void listadoFicheros(VistaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Fichero> dataTable = modelo.listaFicherosDatatable(request.getParameterMap());
				bean.setDatatableFicheros(dataTable);
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
	
	/** Valida el formulario de Gestión Ficheros.
	 * @param request .
	 * @param uploadedFile .
	 * @return BolsaEmpleoValidator validator .
	 * @throws SQLException . 
	 * @throws UVException .
	 * @throws IOException .
	 */
	private Fichero validatorFichero(HttpServletRequest request, Part uploadedFile) throws UVException, SQLException, IOException {
		String nombre = uploadedFile.getSubmittedFileName(); 
				
		// nombre fichero
		if (!BolsaEmpleoUtils.checkFileIsPDF(nombre)) {
			throw new UVException("El fichero debe ser un pdf válido");
		}
		
		Fichero f = new Fichero();
		f.setNombre(nombre);
		f.setArchivo(BolsaEmpleoUtils.checkFileSize(uploadedFile.getInputStream()));
		
		// titulo
		f.setTitulo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TITULO)));
		if (f.getTitulo().length() > ModeloFichero.COLUMN_TITULO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_TITULO_LARGO, ModeloFichero.COLUMN_TITULO_MAXLENGTH));
		}
		
		// publico
		boolean publico = "on".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PUBLICO)));
		f.setPublico(publico);
		
		return f;
	}
}
