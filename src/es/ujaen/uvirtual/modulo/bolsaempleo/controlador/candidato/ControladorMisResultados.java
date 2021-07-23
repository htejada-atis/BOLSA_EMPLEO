package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoResultadoTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase controlador de los resultados del candidato .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.misresultados", 
	description = "Controlador de mis resultados bolsa empleo", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/misresultados", 
			"/srv/en/informacionadministrativa/bolsaempleo/misresultados",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/misresultados",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/misresultados"
	})
public class ControladorMisResultados extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisResultados.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_DATATABLE_BOLSAS = "datatablebolsas";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_RESULTADOS = "datatableresultados";
	public static final String ACCION_DETALLE_RESULTADOS_BOLSA = "detalleresultadosbolsa";
	public static final String ACCION_INDEX = "listarbolsas";
	public static final String ACCION_MIS_RESULTADOS = "misresultados";
	public static final String ACCION_SELECCIONAR_BOLSA = "seleccionarbolsa";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_ENVIAR = "enviar";
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_SIN_PERMISO_CANDIDATO = "No eres un candidato";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	
	// Respuesta error
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_MR = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misresultados/";
	public static final String JSP_INDEX = RUTA_BEP_MR + "index.jsp";	
	public static final String JSP_RESULTADOS_AREA = RUTA_BEP_MR + "resultadosarea.jsp";
	public static final String JSP_RESULTADO_DETALLE = RUTA_BEP_MR + "resultadodetalle.jsp";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/misresultados";
	
	// ajax
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaMisResultados bean = new VistaMisResultados();
		
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
				case ACCION_DETALLE_RESULTADOS_BOLSA:
				case ACCION_MIS_RESULTADOS:
				case ACCION_SELECCIONAR_BOLSA:
					accionesBolsa(bean, datos, request, response, nombreAccion);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
		}
	}
	
	private void init(VistaMisResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		bean.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria());
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_CANDIDATO)) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO_CANDIDATO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}		
	}
	
	private void errorFatal(VistaMisResultados bean, String mensaje) {
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
	
	private void accionesBolsa(VistaMisResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_RESULTADOS_AREA);
		Integer idBolsa = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_BOLSA));
		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(idBolsa);
		bean.setBolsa(bolsa);
		
		switch (nombreAccion) {
			case ACCION_DATATABLE_CANDIDATOS:
				listadoCandidatos(bean, datos, request, response);
				break;
			case ACCION_DETALLE_RESULTADOS_BOLSA:
				break;
			case ACCION_MIS_RESULTADOS:
				resultadosCandidato(bean);
				break;
			case ACCION_SELECCIONAR_BOLSA:
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void resultadosCandidato(VistaMisResultados bean) throws SQLException, UVException, IOException {
		ModeloResultados modeloResultados = ModeloResultados.obtenerInstancia();
		
		bean.setVista(JSP_RESULTADO_DETALLE);
		BolsaResultado bolsaResultado = modeloResultados.getBolsaResultado(bean.getBolsa(), bean.getUsuarioLogeado(), bean.getConvocatoria());
		bean.setBolsaResultado(bolsaResultado);
		
		bean.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteTipoMerito());
	}
	
	private void listadoBolsas(VistaMisResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloMisResultados modelo = ModeloMisResultados.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaResultado> dataTable = modelo.listaBolsasResultadosCandidatoDatatable(
						bean.getConvocatoria(), bean.getUsuarioLogeado(), request.getParameterMap());
				bean.setDataTableBolsas(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e.getClass().equals(SQLException.class)) {
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
	
	private void listadoCandidatos(VistaMisResultados bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloMisResultados modelo = ModeloMisResultados.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoResultadoTable> dataTable = modelo.listadoResultadosCandidatosArea(
						bean.getBolsa(), bean.getConvocatoria(), bean.getUsuarioLogeado(), request.getParameterMap());
				bean.setDataTableCandidatos(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e.getClass().equals(SQLException.class)) {
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
}
