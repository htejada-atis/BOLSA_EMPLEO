package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
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

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;
import org.json.JSONObject;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloFichero;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloNoticia;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, cambiar, eliminar y agregar noticias.
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.ficheros", 
		description = "Gestión de noticias", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/ficheros", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/ficheros"
		})
@MultipartConfig
public class ControladorGestionFicheros extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionFicheros.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	
	// Acciones
	public static final String ACCION_AGREGAR_FICHERO = "agregarfichero";
	public static final String ACCION_LISTAR_FICHEROS = "listar_ficheros";
	public static final String ACCION_DATATABLE = "datatable";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_FICHERO = "fichero";
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_AGREGAR = "fichero creado correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "fichero editado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "fichero eliminado correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/";

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
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_FICHEROS;
		}
		try {
			switch (nombreAccion) {
				case ACCION_AGREGAR_FICHERO:
					agregarFichero(request, response, bean);
					break;
				case ACCION_DATATABLE:
					datatableFicheros(request, response);
					return;
				default:
					break;
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
		} catch (SQLException e) {
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (ServletException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
		} finally {
			datos.getVistas().put(bean.getClass().getName(), bean);
			datos.getFicherosJSP().add(bean.getVista());
			datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
			datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
			datos.getFicherosJS().add("/js/jquery.ui.datepicker-es.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
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
	
	/** agrega una nueva noticia.
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void agregarFichero(HttpServletRequest request, HttpServletResponse response, VistaFicheros bean) throws SQLException, ServletException, IOException {
		ModeloFichero modelo = new ModeloFichero();
		if (request.getPart(PARAM_FICHERO) != null) {
			try {
				Part uploadedFile = request.getPart(PARAM_FICHERO);
				InputStream input = uploadedFile.getInputStream();
				modelo.insertaFichero(input, BolsaEmpleoUtils.obtenerNombreFichero(uploadedFile));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** carga las ficheros en una tabla .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd.
	 * @throws IOException en caso de error de IO.
	 */
	private void datatableFicheros(HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloNoticia modelo = new ModeloNoticia();
		List<Noticia> listaNoticias = modelo.listaNoticias();
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		
		try {
			json.put("recordsTotal", listaNoticias.size());
			json.put("recordsFiltered", listaNoticias.size());
			json.put("data", listaNoticias);
		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "Error creando json {0}", e);
			throw new UVException("Error creando json");
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
        out.print(json);
        out.close();
	}
	
	
	
}
