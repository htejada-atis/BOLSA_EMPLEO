package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Noticia;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloNoticia;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaNoticias;
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
	public static final String ACCION_INDEX = "listar_noticias";
	
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
	
	public static final String MENSAJE_ERROR_FECHA_REQUERIDA = "La fecha es requerida";
	public static final String MENSAJE_ERROR_TEXTO_VACIO = "El texto no puede estar vacio";
	
	public static final String MENSAJE_ERROR_TEXTO_LARGO = "El texto no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_ENLACE_LARGO = "El enlace no puede contener mas de %d caracteres";
	
	public static final String MENSAJE_EXITO_AGREGAR = "Noticia creada correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "Noticia editada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Noticia eliminada correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Noticia restaurada correctamente";
	
	// ajax
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_NOTICIAS = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/gestionnoticias/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/noticias";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaNoticias bean = new VistaNoticias();

		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(RUTA_BEP_NOTICIAS + "indice.jsp");
					break;
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
			datos.getFicherosJS().add("/js/jquery.ui.datepicker-es.js");
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
		}
	}
	
	private void init(VistaNoticias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_NOTICIAS + "indice.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
		} catch (UVException e) {
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}
	}
	
	private void errorFatal(VistaNoticias bean, String mensaje) {
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
			
			Noticia noticia = this.validarNoticia(request);
			noticia.setActiva(true);
			
			ModeloNoticia.obtenerInstancia().insertaNoticia(noticia, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
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
		ModeloNoticia modelo = ModeloNoticia.obtenerInstancia();
		Noticia noticia = modelo.listaNoticia(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		bean.setNoticia(noticia);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO)) != null) {
			Noticia noticiaForm = this.validarNoticia(request);
			noticiaForm.setCodNum(noticia.getCodNum());
			modelo.actualizaNoticia(noticiaForm, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
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
		ModeloNoticia modelo = ModeloNoticia.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		Noticia noticia = new Noticia();
		noticia.setCodNum(codNum);
		boolean activa = "true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACTIVA))); 
		noticia.setActiva(activa);
		modelo.borraRestauraNoticia(noticia, bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(activa ? MENSAJE_EXITO_RESTAURAR : MENSAJE_EXITO_ELIMINAR, bean, request);
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
		ModeloNoticia modelo = ModeloNoticia.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Noticia> dataTable = modelo.listaNoticiasDatatable(request.getParameterMap());
				bean.setDatatableNoticias(dataTable);
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
		
		datos.setRespuestaEnviada(true);
	}
	
	/** Valida el formulario de Gestión Noticias.
	 * @param request .
	 * @return BolsaEmpleoValidator validator .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws IOException .
	 */
	private Noticia validarNoticia(HttpServletRequest request) throws UVException {
		Noticia n = new Noticia();
		
		n.setTexto(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TEXTO)));
		if (n.getTexto() == null || n.getTexto().isBlank()) {
			throw new UVException(MENSAJE_ERROR_TEXTO_VACIO);
		}
		if (n.getTexto().length() > ModeloNoticia.COLUMN_TEXTO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_TEXTO_LARGO, ModeloNoticia.COLUMN_TEXTO_MAXLENGTH));
		}
				
		n.setEnlace(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENLACE)));
		if (n.getEnlace() != null && n.getEnlace().length() > ModeloNoticia.COLUMN_ENLACE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_ENLACE_LARGO, ModeloNoticia.COLUMN_ENLACE_MAXLENGTH));
		}
		
		n.setFecha(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA), Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
		n.setPublica("true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PUBLICA))));
				
		return n;
	}
	
}
