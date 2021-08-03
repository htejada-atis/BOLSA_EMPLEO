package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Servlet implementation class ControladorContratacion.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.contratacion",
	description = "Contratación bolsa empleo",
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/contratacion",
			"/srv/en/informacionadministrativa/bolsaempleo/contratacion",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/contratacion",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/contratacion"
	})
public class ControladorContratacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorContratacion.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_PLAZAS_OFERTADAS = "datatableplazasofertadas";
	public static final String ACCION_EDITAR_PLAZA_OFERTADA = "editarplazaofertada";
	public static final String ACCION_SELECCIONAR_PLAZA_OFERTADA = "seleccionarplazaofertada";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_NUEVA_PLAZA_OFERTADA = "nuevaplazaofertada";
	public static final String ACCION_RESTAURAR_PLAZA_OFERTADA = "restaurarplazaofertada";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_CENTRO_DESTINO = "centrodestino";
	public static final String PARAM_CUATRIMESTRE = "cuatrimestre";
	public static final String PARAM_DURACION_PREVISTA = "duracionprevista";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ESTADO = "estado";
	public static final String PARAM_HORARIO = "horario";
	public static final String PARAM_JUSTIFICACION = "justificacion";
	public static final String PARAM_NRI_FECHA = "nrifecha";
	public static final String PARAM_NRI = "nri";
	public static final String PARAM_PLAZA_OFERTADA = "plazaofertada";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso";
	
	public static final String MENSAJE_ERROR_JUSTIFICACION_VACIA = "La justificación no puede estar vacía";
	public static final String MENSAJE_ERROR_DURACION_PREVISTA_VACIA = "La duración prevista no puede estar vacía";
	public static final String MENSAJE_ERROR_CUATRIMESTRE_VACIO = "La cuatrimestre no puede estar vacío";
	public static final String MENSAJE_ERROR_CENTRO_DESTINO_VACIO = "El centro de destino no puede estar vacío";
	
	// ruta vistas
	public static final String RUTA_BEP_CONTRATACION = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/contratacion/";
	public static final String JSP_INDEX = RUTA_BEP_CONTRATACION + "index.jsp";
	public static final String JSP_CREATE = RUTA_BEP_CONTRATACION + "createPlazaOfertada.jsp";
	public static final String JSP_EDIT = RUTA_BEP_CONTRATACION + "editPlazaOfertada.jsp";
	
	// errors
	public static final Integer RESPONSE_HTTP_CODE_ERROR_400 = 400;
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/contratacion";
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
		
		VistaContratacion bean = new VistaContratacion();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null || nombreAccion.isEmpty()) {
			nombreAccion = ACCION_INDEX;
		}
		try {
			init(bean, datos, request, response);
			
			switch (nombreAccion) {
				case ACCION_DATATABLE_PLAZAS_OFERTADAS:
					listaPlazasOfertadas(bean, datos, request, response);
					break;
				case ACCION_INDEX:
					break;
				case ACCION_NUEVA_PLAZA_OFERTADA:
					nuevaPlazaOfertada(bean, datos, request, response);
					break;
				case ACCION_SELECCIONAR_PLAZA_OFERTADA:
					seleccionarPlazaOfertada(bean, datos, request, response, nombreAccion);
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
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
		}
	}
	
	private void init(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
			
			// personal, comision, direccion
			int[] rolesValidos = {ModeloRol.ID_ROL_SERVICIO_PERSONAL, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO};
			boolean contains = IntStream.of(rolesValidos).
					anyMatch(x -> x == bean.getUsuarioLogeado().getRol().getCodNum());
			
			if (!contains) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void errorFatal(VistaContratacion bean, String mensaje) {
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
	
	private void seleccionarPlazaOfertada(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException {
		bean.setVista(JSP_EDIT);
		cargarListasFormulario(bean);
		
		ModeloPlazaOfertada modelo = ModeloPlazaOfertada.obtenerInstancia();
		PlazaOfertada plaza = modelo.getPlazaOfertadaById(Formateador.leeParametroInteger(request.getParameter(PARAM_PLAZA_OFERTADA)));
		bean.setPlazaOfertada(plaza);
		
		switch (nombreAccion) {
			case ACCION_SELECCIONAR_PLAZA_OFERTADA:
				break;
		}
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENVIAR)) != null) {
			plaza = validarPlazaOfertada(plaza, request);
			
		}
	}
	
	private void nuevaPlazaOfertada(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		bean.setVista(JSP_CREATE);
		cargarListasFormulario(bean);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ENVIAR)) != null) {
			PlazaOfertada plaza = validarPlazaOfertada(new PlazaOfertada(), request);
			
		}
	}
	
	private void cargarListasFormulario(VistaContratacion bean) throws SQLException {
		bean.setListaAreas(ModeloArea.obtenerInstancia().listaAreas());
		bean.setListaDedicaciones(ModeloDedicacion.obtenerInstancia().listaDedicacionesActivas());
	}
	
	private void listaPlazasOfertadas(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<PlazaOfertada> dataTable = ModeloPlazaOfertada.obtenerInstancia().listadoPlazasOfertadas(request.getParameterMap());
				bean.setDatatablePlazasOfertadas(dataTable);
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
	
	private PlazaOfertada validarPlazaOfertada(PlazaOfertada plaza, HttpServletRequest request) throws UVException, SQLException {
		plaza.setArea(ModeloArea.obtenerInstancia().getAreaById(Formateador.leeParametroInteger(request.getParameter(PARAM_AREA))));
		
		plaza.setJustificacion(Formateador.leeParametroString(request.getParameter(PARAM_JUSTIFICACION)));
		if (plaza.getJustificacion() == null || plaza.getJustificacion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_JUSTIFICACION_VACIA);
		}
		
		plaza.setDuracionPrevista(Formateador.leeParametroString(request.getParameter(PARAM_DURACION_PREVISTA)));
		if (plaza.getDuracionPrevista() == null || plaza.getDuracionPrevista().isBlank()) {
			throw new UVException(MENSAJE_ERROR_JUSTIFICACION_VACIA);
		}
		
		plaza.setCentroDestino(Formateador.leeParametroString(request.getParameter(PARAM_CENTRO_DESTINO)));
		if (plaza.getCentroDestino() == null || plaza.getCentroDestino().isBlank()) {
			throw new UVException(MENSAJE_ERROR_JUSTIFICACION_VACIA);
		}
		
		return plaza;
	}
	
}
