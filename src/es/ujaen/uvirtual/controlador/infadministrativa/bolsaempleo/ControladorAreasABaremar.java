package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaAreasBaremar;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloArea;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de las áreas a baremar.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.areasbaremar", 
		description = "Gestión de las áreas a baremar", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/areasbaremar", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/areasbaremar",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/areasbaremar",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/areasbaremar"
		})
public class ControladorAreasABaremar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorAreasABaremar.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_AREA = "aa";
	public static final String PARAM_AREAS_SELECCIONADAS = "areasselected";
	public static final String PARAM_ID = "id";
	
	// acciones
	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DATATABLE_EXCLUIDOS = "datatableexcluidos";
	public static final String ACCION_AREA = "accionarea";
	public static final String ACCION_AREA_PASAR_A_BAREMALE = "accionareabaremable";
	public static final String ACCION_AREA_PASAR_A_NO_BAREMALE = "accionareanobaremable";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_AREA_NO_VALIDA = "Acción no válida";
	public static final String MENSAJE_EXITO_AREA_MODIFICADA_CORRECTAMENTE = "Area/s modificada/s correctamente"; 

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/";
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
		
		VistaAreasBaremar bean = new VistaAreasBaremar();		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
				
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();		
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR;
		}
		
		bean.setVista(RUTA_BEP_CONF + "areasbaremar.jsp");
		
		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_EXCLUIDOS:
					listadoAreasExcluidasUsuario(bean, datos, request, response);
					break;
				case ACCION_AREA:
					accionSobreArea(bean, datos, request);
					break;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
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
		
	/**
	 * AJAX para devolver listado de areas (bolsas).
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listado(VistaAreasBaremar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreaDatatable(request.getParameterMap());
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
	
	
	/**
	 * AJAX para devolver listado de areas excluidas de un usuario(bolsas).
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoAreasExcluidasUsuario(VistaAreasBaremar bean, UVDatos datos, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();	
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreaExcluidasUsuarioDatatable(request.getParameterMap(), codNum);
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
	
	private void accionSobreArea(VistaAreasBaremar bean, UVDatos datos, HttpServletRequest request) throws UVException, SQLException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		
		String nombreAccionBolsa = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION_AREA));
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_AREAS_SELECCIONADAS));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		List<Bolsa> bolsas = modelo.getBolsasByIds(selected);

		switch (nombreAccionBolsa) {
		case ACCION_AREA_PASAR_A_BAREMALE:
			modelo.ponerAreaComoBaremable(bolsas);							
			break;
		case ACCION_AREA_PASAR_A_NO_BAREMALE:
			modelo.ponerAreaComoNoBaremable(bolsas);
			break;
		default:
			bean.getMensajesDeError().add(MENSAJE_ERROR_ACCION_AREA_NO_VALIDA);
			return;
		}
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_AREA_MODIFICADA_CORRECTAMENTE);
	}
}