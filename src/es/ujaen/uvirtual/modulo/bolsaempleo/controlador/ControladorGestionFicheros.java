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
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloFichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoValidator;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFicheros;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, eliminar y agregar ficheros.
 * Controlador - Opers. con nombres: obtener, eliminar, agregar
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.ficheros", 
		description = "Gestión de ficheros", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/ficheros", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/ficheros",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/ficheros",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/ficheros"
		})
@MultipartConfig
public class ControladorGestionFicheros extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionFicheros.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	
	public static final String ACCION_BORRAR_FICHEROS = "borrarficheros";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_HACER_FICHEROS_PUBLICOS = "ficherospublicos";
	public static final String ACCION_HACER_FICHEROS_PRIVADOS = "ficherosprivados";
	public static final String ACCION_LISTAR_FICHEROS = "listarficheros";
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
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_FICHEROS;
		}
		
		try {
			modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_LISTAR_FICHEROS:
					bean.setVista(RUTA_BEP_CONF + "ficheros.jsp");
					break;
				case ACCION_BORRAR_FICHEROS:
					eliminarFicheros(request, response);
					break;
				case ACCION_DATATABLE:
					listadoFicheros(bean, datos, request, response);
					return;
				case ACCION_DESCARGAR_FICHERO:
					descargarFichero(bean, datos, request, response);
					break;
				case ACCION_HACER_FICHEROS_PUBLICOS:
					cambiarFicherosPublico(request, response, true);
					break;
				case ACCION_HACER_FICHEROS_PRIVADOS:
					cambiarFicherosPublico(request, response, false);
					break;
				case ACCION_SUBIR_FICHERO:
					agregarFichero(request, response, bean);
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
			}
		} catch (SQLException e) {
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (ServletException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
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
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws ServltetException .
	 * @throws IOException en caso de error de input u output .
	 * @throws UVException en caso de error de parametros .
	 */
	private void agregarFichero(HttpServletRequest request, HttpServletResponse response, VistaFicheros bean) throws SQLException, ServletException, IOException, UVException {
		bean.setVista(RUTA_BEP_CONF + "formFichero.jsp");
		
		if (request.getParameter(PARAM_TITULO) != null) {
			ModeloFichero modelo = ModeloFichero.obtenerInstancia();
			Part uploadedFile = request.getPart(PARAM_ARCHIVO);
			if (uploadedFile != null) {
				if (uploadedFile.getSize() > 0) {
					
					Fichero fichero = this.validatorFichero(request, uploadedFile);										
					fichero.setArchivo(uploadedFile.getInputStream());
					
					BolsaEmpleoUtils.checkFileSize(fichero.getArchivo());
					
					modelo.insertaFichero(fichero);
					HttpSession session = request.getSession(false);
					session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
					response.sendRedirect(request.getServletPath());
				} else {
					throw new UVException("No se puede subir un fichero sin archivo");
				}
			}
		}
	}
	
	/** cambia ficheros a publicos .
	 * @param request .
	 * @param response .
	 * @param publico .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void cambiarFicherosPublico(HttpServletRequest request, HttpServletResponse response, Boolean publico) throws SQLException, UVException {
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		
		Gson gson = new GsonBuilder().create();
		
		try {
			List<Integer> ficheros = gson.fromJson(request.getParameter(PARAM_FICHEROS), new TypeToken<List<Integer>>() { }.getType());
			
			for (Integer fichero: ficheros) {
				modelo.modificarPublicoFichero(new Fichero(fichero, publico));
			}
			
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_CAMBIAR_PUBLICO);
			response.sendRedirect(request.getServletPath());
		} catch (Exception ex) {
			throw new UVException(MENSAJE_ERROR_FICHEROS_SELECCIONADOS_INCORRECTOS);
		}
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
	private void descargarFichero(VistaFicheros bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_FICHERO)) != null) {
			Fichero fichero = modelo.listaFichero(Formateador.leeParametroInteger(request.getParameter(PARAM_FICHERO)));
			bean.setFichero(fichero);
			
			int i = fichero.getNombre().lastIndexOf('.');
			if (i > 0) {
				String extension = fichero.getNombre().substring(i + 1);
				if ("pdf".equalsIgnoreCase(extension)) {
					response.setContentType("application/pdf");
					datos.setRespuestaEnviada(true);
			        
					try (ServletOutputStream stream = response.getOutputStream();
			             BufferedInputStream buf = new BufferedInputStream(fichero.getArchivo())) {
						int readBytes = 0;
						while ((readBytes = buf.read()) != -1) {
							stream.write(readBytes);
						}
						stream.flush();
			        }
				} else {
					response.sendRedirect(request.getServletPath());
			    }
			}			
		}
	}
	
	/** eliminar ficheros .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de IO .
	 */
	private void eliminarFicheros(HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		
		Gson gson = new GsonBuilder().create();
		
		try {
			List<Integer> ficheros = gson.fromJson(request.getParameter(PARAM_FICHEROS), new TypeToken<List<Integer>>() { }.getType());
			
			for (Integer fichero: ficheros) {
				modelo.borraFichero(new Fichero(fichero));
			}
			
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, ficheros.size() > 1 ? MENSAJE_EXITO_ELIMINAR_FICHEROS : MENSAJE_EXITO_ELIMINAR_FICHERO);
			response.sendRedirect(request.getServletPath());
		} catch (Exception ex) {
			throw new UVException(MENSAJE_ERROR_FICHEROS_SELECCIONADOS_INCORRECTOS);
		}
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
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	/** Valida el formulario de Gestión Ficheros.
	 * @param request .
	 * @param uploadedFile .
	 * @return BolsaEmpleoValidator validator .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws IOException .
	 */
	private Fichero validatorFichero(HttpServletRequest request, Part uploadedFile) throws UVException {			
		// nombre fichero
		String nombre = BolsaEmpleoUtils.obtenerNombreFichero(uploadedFile);
		int i = nombre.lastIndexOf('.');
		if (i > 0) {
			String extension = nombre.substring(i + 1);
			if (!"pdf".equalsIgnoreCase(extension)) {
				throw new UVException("No se puede subir un fichero que sea distinto de pdf");
		    }
		}
		Fichero f = new Fichero();
		f.setNombre(nombre);
		
		// titulo
		f.setTitulo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TITULO)));
		if (f.getTitulo() == null || f.getTitulo().isBlank()) {
			throw new UVException("El título no puede estar vacio");
		}
		if (f.getTitulo().length() > ModeloFichero.COLUMN_TITULO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_TITULO_LARGO, ModeloFichero.COLUMN_TITULO_MAXLENGTH));
		}
		
		// publico
		boolean publico = "on".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PUBLICO)));
		f.setPublico(publico);
		
		return f;
	}
}
