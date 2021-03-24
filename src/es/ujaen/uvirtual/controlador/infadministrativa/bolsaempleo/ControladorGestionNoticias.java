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
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, cambiar, eliminar y agregar noticias.
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.noticias", 
		description = "Gestión de noticias", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/noticias", 
				"/srv/en/informacionadministrativa/bolsaempleo/noticias"
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
	public static final String MENSAJE_EXITO_AGREGAR = "noticia creada correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "noticia editada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "noticia eliminada correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_NOTICIAS = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/gestionnoticias/";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaNoticias bean = new VistaNoticias();
		bean.setVista(RUTA_BEP_NOTICIAS + "indice.jsp");
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_NOTICIAS;
		}
		try {
			switch (nombreAccion) {
				case ACCION_AGREGAR_NOTICIA:
					agregarNoticia(request, response, bean);
					break;
				case ACCION_DATATABLE:
					listadoNoticias(datos, request, response);
					return;
				case ACCION_EDITAR_NOTICIA:
					editarNoticia(request, response, bean);
					break;
				case ACCION_ELIMINAR_NOTICIA:
					eliminarNoticia(request, response);
					break;
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
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
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void agregarNoticia(HttpServletRequest request, HttpServletResponse response, VistaNoticias bean) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_NOTICIAS + "formNoticia.jsp");
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO)) != null) {
			ModeloNoticia modelo = new ModeloNoticia();
			String enlace = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENLACE));
			String texto = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO));
			Boolean publica = request.getParameter(PARAM_PUBLICA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PUBLICA)).equals("true");
			Date fecha = Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA), Formateador.FORMATO_FECHA_DDMMYYYY, "/");
			Noticia noticia = new Noticia(enlace, texto, fecha, publica, true);
			modelo.insertaNoticia(noticia);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** edita una noticia.
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void editarNoticia(HttpServletRequest request, HttpServletResponse response, VistaNoticias bean) throws SQLException, UVException, IOException {
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
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** eliminar una noticia.
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void eliminarNoticia(HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloNoticia modelo = new ModeloNoticia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		Noticia noticia = new Noticia();
		noticia.setCodNum(codNum);
		Boolean activa = request.getParameter(PARAM_ACTIVA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACTIVA)).equals("true");
		noticia.setActiva(activa);
		modelo.borraRestauraNoticia(noticia);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_ELIMINAR);
		response.sendRedirect(request.getServletPath());
	}
	
	/** carga las noticias en una tabla .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de IO .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoNoticias(UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloNoticia modelo = new ModeloNoticia();
		
		datos.setContentType("application/json");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				DataTable<Noticia> dataTable = modelo.listaNoticiasDatatable(request.getParameterMap());
				Gson gson = new GsonBuilder().setDateFormat("dd/M/yyyy").setExclusionStrategies(DataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (UVException ex) {
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
			}
		}
		
		datos.setRespuestaEnviada(true);
	}
	
}
