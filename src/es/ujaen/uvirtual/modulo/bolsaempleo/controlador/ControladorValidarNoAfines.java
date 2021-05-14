package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

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
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloValidar;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Validación meritos de bolsas no sujetos a afinidad.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.validarnoafines", 
		description = "Validación de méritos no sujetos a afinidad", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/validarnoafines", 
				"/srv/en/informacionadministrativa/bolsaempleo/validarnoafines",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/validarnoafines", 
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/validarnoafines"
		})
public class ControladorValidarNoAfines extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorValidarNoAfines.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_DATATABLE_BOLSAS = "datatablebolsas";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_MERITOS = "datatablemeritos";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_BOLSA_SELECCIONADA = "bolsaseleccionada";
	public static final String ACCION_CANDIDATO_SELECCIONADO = "candidatoseleccionado";
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_MERITO_SELECCIONADO = "meritoseleccionado";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_MERITO = "merito";
	
	// url y rutas
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/validarnoafines";
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/validarnoafines/";	
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaValidarNoAfines bean = new VistaValidarNoAfines();
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
						
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(RUTA_BEP_CONF + "index.jsp");
					break;	
				case ACCION_BOLSA_SELECCIONADA:
					break;
				case ACCION_DATATABLE_BOLSAS:
					listadoBolsas(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
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
	
	private void init(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "index.jsp");
		bean.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria());
		try {
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
		} catch (UVException e) {
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}
	}
	
	private void errorFatal(VistaValidarNoAfines bean, String mensaje) {
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
		
	/**
	 * Listado de bolsas de validación datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void listadoBolsas(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException, UVException {
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaValidacion> dataTable = ModeloValidar.obtenerInstancia().
						listadoAreasNoSujetasAfinidad(bean.getConvocatoria(), request.getParameterMap());
				bean.setDatatableBolsas(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
}
