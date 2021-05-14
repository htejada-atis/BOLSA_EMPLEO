package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase controlador de los resultados del candidato .
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.misresultados", 
		description = "Controlador de mis resultados bolsa empleo", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/misresultados", 
				"/srv/en/informacionadministrativa/bolsaempleo/misresultados"
		})
public class ControladorMisResultados extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisResultados.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_DATATABLE_AREAS = "datatableareas";
	public static final String ACCION_DATATABLE_RESULTADOS = "datatableresultados";
	public static final String ACCION_INDEX = "listarareas";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_ENVIAR = "enviar";
	
	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	
	// Respuesta error
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misresultados/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/misresultados";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaMisResultados bean = new VistaMisResultados();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(RUTA_BEP + "index.jsp");
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, e.toString());
			response.sendRedirect(request.getServletPath());
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
	
	private void init(VistaMisResultados bean, UVDatos datos, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(RUTA_BEP + "index.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
	}
	
	private void errorFatal(VistaMisResultados bean, String mensaje) {
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
}
