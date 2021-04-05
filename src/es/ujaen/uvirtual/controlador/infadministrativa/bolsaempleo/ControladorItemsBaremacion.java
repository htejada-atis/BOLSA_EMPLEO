package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBaremacion;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los items de baremación.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.itemsbaremacion", 
		description = "Gestión de los items de baremación", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion", 
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion"
		})
public class ControladorItemsBaremacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorItemsBaremacion.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_APARTADO_SELECCIONADO = "apartadoseleccionado";
	public static final String ACCION_BLOQUE_SELECCIONADO = "bloqueseleccionado";
	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_DATATABLE_APARTADOS = "datatable_apartados";
	public static final String ACCION_DATATABLE_BLOQUES = "datatable_bloques";
	public static final String ACCION_DATATABLE_ITEMS = "datatable_items";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_APARTADO = "apartado";
	public static final String PARAM_BLOQUE = "bloque";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_AREA_NO_VALIDA = "Acción no válida";
	public static final String MENSAJE_EXITO_AREA_MODIFICADA_CORRECTAMENTE = "Area/s modificada/s correctamente";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaItemsBaremacion bean = new VistaItemsBaremacion();		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR;
		}
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/itemsbaremacion.jsp");
		
		try {
			switch (nombreAccion) {
				case ACCION_APARTADO_SELECCIONADO:
					seleccionarApartado(bean, request);
					break;
				case ACCION_BLOQUE_SELECCIONADO:
					seleccionarBloque(bean, request);
					break;
				case ACCION_DATATABLE_APARTADOS:
					listadoApartados(bean, datos, request, response);
					break;				
				case ACCION_DATATABLE_BLOQUES:
					listadoBloques(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_ITEMS:
					listadoItems(bean, datos, request, response);
					break;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
		} finally {
			datos.getVistas().put(bean.getClass().getName(), bean);
			datos.getFicherosJSP().add(bean.getVista());
			datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
			datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
			datos.getFicherosJS().add("/js/jquery.ui.datepicker-es.js");
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	/** Redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/** Devuelve un apartado con id dado a la vista .
	 * @param bean .
	 * @param request .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void seleccionarApartado(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ApartadoBaremacion apartado = new ModeloBaremacion().getApartadoBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO)));
		bean.setApartadoBaremacion(apartado);
	}
	
	/** Devuelve un bloque con id dado a la vista .
	 * @param bean .
	 * @param request .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void seleccionarBloque(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		BloqueBaremacion bloque = new ModeloBaremacion().getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
		bean.setBloqueBaremacion(bloque);
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
	}
	
	/**
	 * Listado de apartados de items baremación.
     * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoApartados(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloBaremacion modelo = new ModeloBaremacion();
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				DataTable<ApartadoBaremacion> dataTable = modelo.listadoApartadosGeneralesBaremacionDatatable(request.getParameterMap());
				Gson gson = new GsonBuilder().setExclusionStrategies(DataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatable(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * Listado de bloques.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoBloques(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloBaremacion modelo = new ModeloBaremacion();
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer apartado = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
				DataTable<BloqueBaremacion> dataTable = modelo.listadoBloquesBaremacionDatatable(request.getParameterMap(), apartado);
				Gson gson = new GsonBuilder().setExclusionStrategies(DataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatable(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * Listado de items.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoItems(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloBaremacion modelo = new ModeloBaremacion();
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer bloque = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
				DataTable<ItemBaremacion> dataTable = modelo.listadoItemsBaremacionDatatable(request.getParameterMap(), bloque);
				Gson gson = new GsonBuilder().setExclusionStrategies(DataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatable(dataTable);
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
