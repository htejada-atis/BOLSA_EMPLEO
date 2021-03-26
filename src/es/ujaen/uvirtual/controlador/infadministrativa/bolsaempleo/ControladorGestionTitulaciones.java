package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

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
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulaciones;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloNoticia;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloTitulacion;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, modificar y eliminar titulaciones .
 * Controlador - Opers. con nombres: obtener, modificar, eliminar .
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.titulaciones", 
		description = "Gestión de titulaciones", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/titulaciones", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/titulaciones",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulaciones",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/titulaciones"
		})
public class ControladorGestionTitulaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionTitulaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_AGREGAR_TITULACION = "agregartitulacion";
	public static final String ACCION_BORRAR_TITULACION = "borrartitulacion";
	public static final String ACCION_DATATABLE_TITULACIONES = "datatabletitulaciones";
	public static final String ACCION_EDITAR_TITULACION = "editartitulacion";
	public static final String ACCION_LISTAR_TITULACIONES = "listartitulaciones";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_TITULACIONES = "titulaciones";
	
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_AGREGAR = "titulación agregada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "titulación eliminada correctamente";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulaciones";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaTitulaciones bean = new VistaTitulaciones();
		bean.setVista(RUTA_BEP_CONF + "titulaciones.jsp");
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_TITULACIONES;
		}
		
		try {
			switch (nombreAccion) {
			case ACCION_AGREGAR_TITULACION:
				agregarTitulacion(request, response, bean);
				break;
			case ACCION_EDITAR_TITULACION:
				editarTitulacion(request, response, bean);
				break;
			case ACCION_BORRAR_TITULACION:
				eliminarTitulacion(request, response);
				break;
			case ACCION_DATATABLE_TITULACIONES:
				listadoTitulaciones(datos, request, response);
				break;
			}
		} catch (SQLException e) {
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
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
	
	/** edita una titulación .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void editarTitulacion(HttpServletRequest request, HttpServletResponse response, VistaTitulaciones bean) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formTitulacion.jsp");
		ModeloTitulacion modelo = new ModeloTitulacion();
		bean.setTitulacion(modelo.listaTitulacion(Formateador.leeParametroInteger(request.getParameter(PARAM_ID))));
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE)) != null) {
			Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE));
			modelo.actualizaTitulacion(new Titulacion(codNum, nombre));
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** eliminar una titulación.
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void eliminarTitulacion(HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloNoticia modelo = new ModeloNoticia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		new ModeloTitulacion().borraTitulacion(new Titulacion(codNum));
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_ELIMINAR);
		response.sendRedirect(request.getServletPath());
	}
	
	/** agrega una nueva titulación .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void agregarTitulacion(HttpServletRequest request, HttpServletResponse response, VistaTitulaciones bean) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formTitulacion.jsp");
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE)) != null) {
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE));
			new ModeloTitulacion().insertaTitulacion(new Titulacion(nombre));
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** carga las titulaciones en una tabla .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de IO .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoTitulaciones(UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloTitulacion modelo = new ModeloTitulacion();
		datos.setContentType("application/json");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				DataTable<Titulacion> dataTable = modelo.listaTitulacionesDatatable(request.getParameterMap());
				Gson gson = new GsonBuilder().setExclusionStrategies(DataTable.GSONEXCLUSIONSTRATEGY).create();
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
