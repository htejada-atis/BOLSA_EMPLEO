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
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatoTitulacionesArea;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.titulacionespreferentesarea", 
		description = "Controlador de configuración de bolsa empleo", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/titulacionespreferentesarea", 
				"/srv/en/informacionadministrativa/bolsaempleo/titulacionespreferentesarea",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/titulacionespreferentesarea",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/titulacionespreferentesarea"
		})
public class ControladorCandidatoTitulacionesPreferentesArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorCandidatoTitulacionesPreferentesArea.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_BOLSA_SELECCIONADA = "seleccionarbolsa";
	public static final String ACCION_DATATABLE_BOLSAS = "datatablebolsas";
	public static final String ACCION_DATATABLE_TITULACIONES = "datatabletitulaciones";
	public static final String ACCION_LISTAR_AREAS = "listarareas";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_ENVIAR = "enviar";
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	
	// Respuesta error
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/titulacionespreferentesarea/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/titulacionespreferentesarea";

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
		
		VistaCandidatoTitulacionesArea bean = new VistaCandidatoTitulacionesArea();
		
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
	
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_AREAS;
		}
		
		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_BOLSA_SELECCIONADA:
					seleccionarArea(bean, request, response);
					break;
				case ACCION_DATATABLE_BOLSAS:
					listadoBolsas(bean, datos, request, response);
					break;
				case ACCION_LISTAR_AREAS:
					bean.setVista(RUTA_BEP + "index.jsp");
					break;
				case ACCION_DATATABLE_TITULACIONES:
					listadoTitulacionesPreferentesArea(bean, datos, request, response);
					break;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
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
	
	private void seleccionarArea(VistaCandidatoTitulacionesArea bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));
		Bolsa area = modelo.getBolsaById(codNum);
		
		bean.setArea(area);
		bean.setVista(RUTA_BEP + "index.jsp");
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
	private void listadoBolsas(VistaCandidatoTitulacionesArea bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreaCandidatoDatatable(request.getParameterMap());
				bean.setDatatableAreas(dataTable);
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
	
	/** carga las titulaciones de un área en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de IO .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoTitulacionesPreferentesArea(VistaCandidatoTitulacionesArea bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		datos.setContentType("application/json");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer area = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
				BolsaEmpleoDataTable<TitulacionArea> dataTable = modelo.listaTitulacionesAreaCandidatoDatatable(request.getParameterMap(), area);
				bean.setDatatableTitulaciones(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (UVException ex) {
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				bean.getMensajesDeError().add(mensaje.toString());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
		
		datos.setRespuestaEnviada(true);
	}
	
}