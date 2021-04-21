package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticias;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloNoticia;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, cambiar, eliminar y agregar noticias.
 * Controlador - Opers. con nombres: obtener,  cambiar, eliminar, agregar
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.noticias", 
		description = "Gestión de noticias", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/noticias", 
				"/srv/en/informacionadministrativa/bolsaempleo/noticias",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/noticias",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/noticias"
		})
public class ControladorGestionNoticias extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionNoticias.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	
	// Acciones
	public static final String ACCION_AGREGAR_NOTICIA = "agregarnoticia";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_EDITAR_NOTICIA = "editarnoticia";
	public static final String ACCION_ELIMINAR_NOTICIA = "eliminarnoticia";
	public static final String ACCION_LISTAR_NOTICIAS = "listar_noticias";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACTIVA = "activa";
	public static final String PARAM_ENLACE = "enlace";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_FECHA = "fecha";
	public static final String PARAM_ID = "id";
	public static final String PARAM_PUBLICA = "publica";
	public static final String PARAM_TEXTO = "texto";
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_AGREGAR = "Noticia creada correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "Noticia editada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Noticia eliminada correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Noticia restaurada correctamente";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_NOTICIAS = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/gestionnoticias/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/noticias";

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
		
		VistaNoticias bean = new VistaNoticias();
		ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();

		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_NOTICIAS;
		}
		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_AGREGAR_NOTICIA:
					agregarNoticia(bean, request, response);
					break;
				case ACCION_DATATABLE:
					listadoNoticias(bean, datos, request, response);
					return;
				case ACCION_EDITAR_NOTICIA:
					editarNoticia(bean, request, response);
					break;
				case ACCION_ELIMINAR_NOTICIA:
					eliminarNoticia(bean, request, response);
					break;
				case ACCION_LISTAR_NOTICIAS:
					bean.setVista(RUTA_BEP_NOTICIAS + "indice.jsp");
					break;
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
		} catch (SQLException e) {
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
	
	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/** agrega una nueva noticia.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void agregarNoticia(VistaNoticias bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_NOTICIAS + "formNoticia.jsp");
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO)) != null) {
			ModeloNoticia modelo = new ModeloNoticia();
			String enlace = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENLACE));
			String texto = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO));
			Boolean publica = request.getParameter(PARAM_PUBLICA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PUBLICA)).equals("true");
			Date fecha = Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA), Formateador.FORMATO_FECHA_DDMMYYYY, "/");
			Noticia noticia = new Noticia(enlace, texto, fecha, publica, true);
			modelo.insertaNoticia(noticia);
			bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** edita una noticia.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void editarNoticia(VistaNoticias bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_NOTICIAS + "formNoticia.jsp");
		ModeloNoticia modelo = new ModeloNoticia();
		bean.setNoticia(modelo.listaNoticia(Formateador.leeParametroInteger(request.getParameter(PARAM_ID))));
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO)) != null) {
			Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
			String enlace = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENLACE));
			String texto = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO));
			Boolean publica = request.getParameter(PARAM_PUBLICA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PUBLICA)).equals("true");
			Date fecha = Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA), Formateador.FORMATO_FECHA_DDMMYYYY, "/");
			Noticia noticia = new Noticia(codNum, enlace, texto, fecha, publica, true);
			modelo.actualizaNoticia(noticia);
			bean.getMensajesDeExito().add(MENSAJE_EXITO_EDITAR);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** eliminar una noticia.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void eliminarNoticia(VistaNoticias bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloNoticia modelo = new ModeloNoticia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		Noticia noticia = new Noticia();
		noticia.setCodNum(codNum);
		Boolean activa = request.getParameter(PARAM_ACTIVA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACTIVA)).equals("true");
		noticia.setActiva(activa);
		modelo.borraRestauraNoticia(noticia);
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ELIMINAR);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, activa ? MENSAJE_EXITO_RESTAURAR : MENSAJE_EXITO_ELIMINAR);
		response.sendRedirect(request.getServletPath());
	}
	
	/** carga las noticias en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de input u output .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoNoticias(VistaNoticias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloNoticia modelo = new ModeloNoticia();
		
		datos.setContentType("application/json");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Noticia> dataTable = modelo.listaNoticiasDatatable(request.getParameterMap());
				bean.setDatatableNoticias(dataTable);
				Gson gson = new GsonBuilder().setDateFormat("dd/M/yyyy").setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (UVException ex) {
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
		
		datos.setRespuestaEnviada(true);
	}
	
}
