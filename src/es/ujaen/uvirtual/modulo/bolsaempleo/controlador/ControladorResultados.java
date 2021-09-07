package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoResultadoTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaResultados;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Controlador de resultados .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.resultados", 
	description = "Resultado de la última baremación", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/resultados", 
			"/srv/en/informacionadministrativa/bolsaempleo/resultados",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/resultados", 
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/resultados"
	})
public class ControladorResultados extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorResultados.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_CANDIDATO = "candidato";
	
	// acciones
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_DATATABLE_BOLSAS = "datatablebolsas";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_SELECCIONAR_BOLSA = "seleccionarbolsa";
	public static final String ACCION_SELECCIONAR_CANDIDATO = "seleccionarcandidato";
	public static final String ACCION_EXPORTAR_RESULTADOS = "exportarresultados";
	
	// mensajes
	public static final String MENSAJE_ERROR_FOO = "Mensaje de error";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso";
	public static final String MENSAJE_EXITO_BAR = "Mensaje de exito";
	
	// ruta vistas
	public static final String RUTA_BEP_RESULTADOS = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/resultados/";
	public static final String JSP_INDEX = RUTA_BEP_RESULTADOS + "index.jsp";	
	public static final String JSP_RESULTADOS_AREA = RUTA_BEP_RESULTADOS + "resultadosarea.jsp";
	public static final String JSP_RESULTADO_DETALLE = RUTA_BEP_RESULTADOS + "resultadodetalle.jsp";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/resultados";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// csv
	public static final String RESPONSE_CSV_CONTENTTYPE = "text/csv";
	public static final String RESPONSE_CSV_ENCODING = "UTF-8";
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaResultados bean = new VistaResultados();
		Usuario usuario = datos.getUsuario();
		
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(JSP_INDEX);
					break;
				case ACCION_DATATABLE_BOLSAS:
					listadoBolsas(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_CANDIDATOS:
				case ACCION_SELECCIONAR_BOLSA:
				case ACCION_SELECCIONAR_CANDIDATO:
					accionesBolsa(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_EXPORTAR_RESULTADOS:
					exportarResultados(datos, request, response);
					break;
				default:
					errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private void init(VistaResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		bean.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria());
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
			
			// personal, comision, direccion
			int[] rolesValidos = {ModeloRol.ID_ROL_SERVICIO_PERSONAL, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO, ModeloRol.ID_ROL_MIEMBRO_COMISION};
			boolean contains = IntStream.of(rolesValidos).
					anyMatch(x -> x == bean.getUsuarioLogeado().getRol().getCodNum());
			
			if (!contains) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void errorFatal(VistaResultados bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	private void accionesBolsa(VistaResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_RESULTADOS_AREA);
		Integer idBolsa = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_BOLSA));
		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(idBolsa);
		bean.setBolsa(bolsa);
		
		switch (nombreAccion) {
			case ACCION_DATATABLE_CANDIDATOS:
				listadoCandidatos(bean, datos, request, response);
				break;
			case ACCION_SELECCIONAR_BOLSA:
				break;
			case ACCION_SELECCIONAR_CANDIDATO:
				seleccionarCandidato(bean, request);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void seleccionarCandidato(VistaResultados bean, HttpServletRequest request) throws SQLException, UVException, IOException {
		ModeloResultados modeloResultados = ModeloResultados.obtenerInstancia();
		
		bean.setVista(JSP_RESULTADO_DETALLE);
		Integer idCandidato = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_CANDIDATO));
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(idCandidato);
		bean.setCandidato(candidato);
		
		BolsaResultado bolsaResultado = modeloResultados.getBolsaResultado(bean.getBolsa(), candidato, bean.getConvocatoria());
		bean.setBolsaResultado(bolsaResultado);
		
		bean.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteTipoMerito());
	}
		
	private void listadoBolsas(VistaResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaResultado> dataTable = modelo.listaBolsasResultadosDatatable(bean.getUsuarioLogeado(), request.getParameterMap());
				bean.setDataTableBolsas(dataTable);
				writer.write(dataTable.toJson("dd/MM/yyyy"));
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
					LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
					LOGGER.log(Level.SEVERE, e.toString());
				} else {
					LOGGER.log(Level.WARNING, e.toString());
				}
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void listadoCandidatos(VistaResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloResultados modelo = ModeloResultados.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoResultadoTable> dataTable = modelo.listadoResultadosCandidatosArea(
						bean.getBolsa(), bean.getConvocatoria(), request.getParameterMap());
				bean.setDataTableCandidatos(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
					LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
					LOGGER.log(Level.SEVERE, e.toString());
				} else {
					LOGGER.log(Level.WARNING, e.toString());
				}
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void exportarResultados(UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
		
		Integer idBolsa = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_BOLSA));
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_CSV_CONTENTTYPE);		
		response.setContentType(RESPONSE_CSV_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_CSV_ENCODING);
		response.setHeader("Content-Disposition", "attachment; filename=\"candidatos-sin-titulacion.csv\"");
		
		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(idBolsa);
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		List<String[]> rows = ModeloResultados.obtenerInstancia().listadoResultadosCandidatosAreaCsv(bolsa, convocatoria);
		
		try (ServletOutputStream stream = response.getOutputStream()) {
			try (PrintWriter printer = new PrintWriter(stream)) {
				for (String[] row : rows) {
					printer.println(String.join(";", row));
				}
			}
			stream.flush();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(ex));
			LOGGER.log(Level.SEVERE, ex.toString());
			throw new UVException(ex.getMessage());
        }
	}
}
