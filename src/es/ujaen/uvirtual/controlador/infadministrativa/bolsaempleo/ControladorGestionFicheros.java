package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Fichero;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloFichero;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
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
	public static final String MENSAJE_EXITO_AGREGAR = "fichero subido correctamente";
	public static final String MENSAJE_EXITO_CAMBIAR_PUBLICO = "fichero publico cambiado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR_FICHERO = "fichero eliminado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR_FICHEROS = "ficheros eliminados correctamente";
	public static final String MENSAJE_ERROR_FICHEROS_SELECCIONADOS_INCORRECTOS = "ficheros seleccionados no válidos";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/ficheros/";
	
	// urls
	public static final String URL_PATTERN_FILES_PRIVADA = "/srv/es/informacionadministrativa/bolsaempleo/configuracion/ficheros";
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/ficheros";

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// variables
	public static boolean anonimo = true;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaFicheros bean = new VistaFicheros();
		bean.setVista(RUTA_BEP_CONF + "ficheros.jsp");
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_FICHEROS;
		}
		
		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
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
					String nombre = BolsaEmpleoUtils.obtenerNombreFichero(uploadedFile);
					
					int i = nombre.lastIndexOf('.');
					if (i > 0) {
					    String extension = nombre.substring(i + 1);
					    if (!extension.toLowerCase().equals("pdf")) {
					    	throw new UVException("No se puede subir un fichero que sea distinto de pdf");
					    }
					}
					
					InputStream input = uploadedFile.getInputStream();
					String titulo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TITULO));
					Boolean publico = request.getParameter(PARAM_PUBLICO) != null 
							&& EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PUBLICO)).equals("on");
					Fichero fichero = new Fichero(nombre, titulo, input, publico);
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
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_FICHERO));
		
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
			    if (extension.toLowerCase().equals("pdf")) {
			    	response.setContentType("application/pdf");
			        datos.setRespuestaEnviada(true);
			        
			        try (ServletOutputStream stream = response.getOutputStream();
			             BufferedInputStream buf = new BufferedInputStream(fichero.getArchivo());) {
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
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_FICHERO));
		
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
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Fichero> dataTable = modelo.listaFicherosDatatable(request.getParameterMap());
				bean.setDatatableFicheros(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
}
