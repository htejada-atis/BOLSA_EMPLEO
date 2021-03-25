package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulacionesArea;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloArea;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloTitulacion;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, cambiar, eliminar y agregar titulaciones .
 * Controlador - Opers. con nombres: obtener, cambiar, eliminar, agregar .
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.titulacionesarea", 
		description = "Gestión de titulaciones por área", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/titulacionesarea", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/titulacionesarea",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulacionesarea",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/titulacionesarea"
		})
public class ControladorGestionTitulacionesArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionTitulacionesArea.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_AGREGAR_TITULACION = "agregartitulacion";
	public static final String ACCION_BORRAR_TITULACION = "borrartitulacion";
	public static final String ACCION_DATATABLE_TITULACIONES = "datatabletitulaciones";
	public static final String ACCION_DATATABLE_TITULACIONES_AREA = "datatabletitulacionesarea";
	public static final String ACCION_ELIMINAR_TITULACION_AREA = "eliminartitulacionarea";
	public static final String ACCION_INCLUIR_TITULACION_AREA = "incluirtitulacionarea";
	public static final String ACCION_LISTAR_TITULACIONES = "listartitulaciones";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_AREA = "area";
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_AGREGAR = "titulación agregada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "titulación eliminada correctamente";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulacionesarea";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaTitulacionesArea bean = new VistaTitulacionesArea();
		bean.setVista(RUTA_BEP_CONF + "titulacionesarea.jsp");
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_TITULACIONES;
		}
		
		try {
			switch (nombreAccion) {
			case ACCION_AGREGAR_TITULACION:
				agregarTitulacion(request, response, bean);
				break;
			case ACCION_ELIMINAR_TITULACION_AREA:
				eliminarTitulacionesArea(request, response);
				break;
			case ACCION_INCLUIR_TITULACION_AREA:
				incluirTitulacionesArea(request, response);
				break;
			case ACCION_LISTAR_TITULACIONES:
				obtenerAreas(bean);
				break;
			case ACCION_DATATABLE_TITULACIONES:
				listadoTitulaciones(datos, request, response);
				break;
			case ACCION_DATATABLE_TITULACIONES_AREA:
				listadoTitulacionesArea(datos, request, response);
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
	
	/** agrega una nueva titulación .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void agregarTitulacion(HttpServletRequest request, HttpServletResponse response, VistaTitulacionesArea bean) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formTitulacion.jsp");
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE)) != null) {
			ModeloTitulacion modelo = new ModeloTitulacion();
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE));
			
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** elimina una lista de titulaciones afines a un área .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void eliminarTitulacionesArea(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
	}
	
	/** incluye una lista de titulaciones en las afines a un área .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void incluirTitulacionesArea(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
	}
	
	/** muestra todas las areas en un select .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerAreas(VistaTitulacionesArea bean) throws SQLException {
		ModeloArea modelo = new ModeloArea();
		List<Area> areas = modelo.listaAreas();
		bean.setAreas(areas);
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
				Integer area = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
				DataTable<Titulacion> dataTable = modelo.listaTitulacionesDatatable(request.getParameterMap(), area);
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
	
	/** carga las titulaciones de un área en una tabla .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de IO .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoTitulacionesArea(UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloTitulacion modelo = new ModeloTitulacion();
		datos.setContentType("application/json");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer area = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
				DataTable<Titulacion> dataTable = modelo.listaTitulacionesAreaDatatable(request.getParameterMap(), area);
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
