package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.BufferedInputStream;
import java.io.IOException;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Fichero;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaInicio;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloFichero;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloNoticia;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo", 
		description = "Informacion bolsa empleo, raiz", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo", 
				"/srv/en/informacionadministrativa/bolsaempleo",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo",
				"/pub/es/ajax/informacionadministrativa/bolsaempleo",
				"/pub/en/ajax/informacionadministrativa/bolsaempleo",
				"/pub/es/informacionadministrativa/bolsaempleo", 
				"/pub/en/informacionadministrativa/bolsaempleo"
		})
@MultipartConfig
public class ControladorInicio extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorInicio.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_AYUDA = "ayuda";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_DOCUMENTOS = "documentos";
	public static final String ACCION_FAQ = "faq";
	public static final String ACCION_LISTAR_NOTICIAS = "listar_noticias";
	public static final String ACCION_LISTAR_TODAS_NOTICIAS = "listar_todas_noticias";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_FICHERO = "fichero";
	public static final String PARAM_NOTICIAS = "noticias";
	
	public static final String MENSAJE_ERROR_BORRADO = "usuario ";
	
	// ruta vistas
	public static final String RUTA_BEP_INICIO = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/inicio/";
	
	// errors
	public static final Integer RESPONSE_HTTP_CODE_ERROR_400 = 400;
	
	// urls
	public static final String URL_PATTERN_AJAX_PUBLICA = "/pub/es/ajax/informacionadministrativa/bolsaempleo";
	public static final String URL_PATTERN_AJAX_PRIVADA = "/srv/es/ajax/informacionadministrativa/bolsaempleo";
	public static final String URL_PATTERN_FILES_PUBLICA = "/pub/es/informacionadministrativa/bolsaempleo";
	public static final String URL_PATTERN_FILES_PRIVADA = "/srv/es/informacionadministrativa/bolsaempleo";
	
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
		
		VistaInicio bean = new VistaInicio();
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null || nombreAccion.isEmpty()) {
			nombreAccion = ACCION_LISTAR_NOTICIAS;
		}
		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_AYUDA:
					bean.setVista(RUTA_BEP_INICIO + "ayuda.jsp");
					break;
				case ACCION_DESCARGAR_FICHERO:
					descargarFichero(bean, datos, request, response);
					break;
				case ACCION_DOCUMENTOS:
					obtenerFicheros(bean);
					break;
				case ACCION_FAQ:
					bean.setVista(RUTA_BEP_INICIO + "faq.jsp");
					break;
				case ACCION_LISTAR_NOTICIAS:
					obtenerNoticias(bean);
					break;
				case ACCION_LISTAR_TODAS_NOTICIAS:
					obtenerTodasNoticias(bean, request, response, datos);
					break;
			}
		} catch (SQLException e) {
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
	
	/** descarga un fichero .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void descargarFichero(VistaInicio bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_INICIO + "documentos.jsp");
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
			
		} else {
			bean.getMensajesDeAdvertencia().add("El fichero no puede ser nulo");
		}
	}
	
	/** muestra todas las noticias.
	 * @param bean bean de la vista a la que poner los valores.
	 * @param request de la petición del servidor .
	 * @param response de la respuesta del servidor .
	 * @param datos .
	 * @throws ServletException .
	 * @throws IOException  en caso de error de input u output .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerTodasNoticias(VistaInicio bean, HttpServletRequest request, HttpServletResponse response, UVDatos datos) 
			throws ServletException, IOException, SQLException {
		ModeloNoticia modelo = ModeloNoticia.obtenerInstancia();
		
		bean.setVista(RUTA_BEP_INICIO + "indice.jsp");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		datos.setRespuestaEnviada(true);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Gson gson = new GsonBuilder().setDateFormat("dd/M/yyyy").create();
				List<String> idsNoticias = gson.fromJson(request.getParameter(PARAM_NOTICIAS), new TypeToken<List<String>>() { }.getType());
				List<Noticia> listaNoticias = modelo.listaNoticiasInicioRestantes(idsNoticias, anonimo);
				bean.setNoticias(listaNoticias);
				
				JsonArray result = (JsonArray) gson.toJsonTree(listaNoticias, new TypeToken<List<Noticia>>() { }.getType());
				writer.print(result);
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR_400);
			}
		}
	}
	
	/** muestra las 3 primeras noticias.
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerNoticias(VistaInicio bean) throws SQLException {
		bean.setVista(RUTA_BEP_INICIO + "indice.jsp");
		ModeloNoticia modelo = ModeloNoticia.obtenerInstancia();
		List<Noticia> noticias = modelo.listaNoticiasInicio(anonimo);
		bean.setNoticias(noticias);
	}
	
	private void obtenerFicheros(VistaInicio bean) throws SQLException {
		bean.setVista(RUTA_BEP_INICIO + "documentos.jsp");
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		List<Fichero> ficheros = modelo.listaFicherosInicio(anonimo);
		bean.setFicheros(ficheros);
	}
}