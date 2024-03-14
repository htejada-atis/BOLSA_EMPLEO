package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

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

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.EvaluadorCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDepartamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEvaluadores;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMiembrosComision;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Listado de bolsas y su estado.
 */
@WebServlet(name = "informacionadministrativa.bolsaempleo.miembroscomision", 
	description = "Quien ha baremabo cada area", 
	urlPatterns = {
		"/srv/es/informacionadministrativa/bolsaempleo/miembroscomision",
		"/srv/en/informacionadministrativa/bolsaempleo/miembroscomision",
		"/srv/es/ajax/informacionadministrativa/bolsaempleo/miembroscomision",
		"/srv/en/ajax/informacionadministrativa/bolsaempleo/miembroscomision" 
})
public class ControladorMiembrosComision extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMiembrosComision.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";

	// acciones
	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_INDEX = "listarareas";
	public static final String ACCION_SELECCIONAR_AREA = "seleccionararea";
	public static final String ACCION_DATATABLE_EVALUADORES = "datatableevaluadores";

	// mensajes
	public static final String MENSAJE_ERROR_FOO = "Mensaje de error";
	public static final String MENSAJE_EXITO_BAR = "Mensaje de exito";

	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/miembroscomision/";
	public static final String JSP_INDEX = RUTA_BEP_CONF + "index.jsp";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/miembroscomision";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	/**
	 * Peticion GET.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");

		VistaMiembrosComision bean = new VistaMiembrosComision();
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());

		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}

		try {
			if (!init(bean, datos, request, response)) {
				return;
			}

			switch (nombreAccion) {
			case ACCION_INDEX:
				obtenerAreas(bean);
				break;
			case ACCION_SELECCIONAR_AREA:
				seleccionarArea(bean, request);
				break;
			case ACCION_DATATABLE_EVALUADORES:
				listadoEvaluadores(bean, datos, request, response);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}

	private boolean init(VistaMiembrosComision bean, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/miembroscomision/index.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);

		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)
					&& !bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_CANDIDATO)) {
				throw new UVException("No tienes permiso");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());

			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}

		return true;
	}

	private void errorFatal(VistaMiembrosComision bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}

	/**
	 * redireccion de do post.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * muestra todas las areas en un select .
	 * 
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerAreas(VistaMiembrosComision bean) throws SQLException {
		bean.setVista(JSP_INDEX);
		bean.setAreas(getAreas());
	}

	private void seleccionarArea(VistaMiembrosComision bean, HttpServletRequest request)
			throws SQLException, UVException {
		bean.setVista(JSP_INDEX);
		bean.setAreas(getAreas());
		bean.setArea(getAreaSeleccionada(request));
	}

	private void listadoEvaluadores(VistaMiembrosComision bean, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException, UVException {
		bean.setArea(getAreaSeleccionada(request));

		ModeloEvaluador modeloEvaluador = ModeloEvaluador.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<EvaluadorCandidato> dataTable = modeloEvaluador.listaEvaluadoresCandidatosDatatable(
						request.getParameterMap(), bean.getArea().getCodNum());
				bean.setDatatableEvaluadores(dataTable);
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

		datos.setRespuestaEnviada(true);
	}

	private List<Area> getAreas() throws SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();
		return modelo.listaAreas();
	}

	private Area getAreaSeleccionada(HttpServletRequest request) throws SQLException, UVException {
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		return modeloArea.getAreaById(Formateador.leeParametroInteger(request.getParameter(PARAM_AREA)));
	}
}
