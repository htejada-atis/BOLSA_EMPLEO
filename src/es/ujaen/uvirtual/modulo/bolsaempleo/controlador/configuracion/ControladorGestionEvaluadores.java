package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

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
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDepartamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEvaluadores;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para la gestión de los evaluadores .
 * Controlador - Opers. con nombres: obtener, eliminar .
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.evaluadores", 
		description = "Gestión de evaluadores de un área", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/evaluadores", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/evaluadores",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/evaluadores",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/evaluadores"
		})
public class ControladorGestionEvaluadores extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorGestionEvaluadores.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// Acciones
	public static final String ACCION_AGREGAR_EVALUADORES = "agregarevaluadores";
	public static final String ACCION_AREA_SELECCIONADA = "areaseleccionada";
	public static final String ACCION_DATATABLE_AREAS = "datatableareas";
	public static final String ACCION_DATATABLE_EVALUADORES = "datatableevaluadores";
	public static final String ACCION_ELIMINAR_EVALUADOR = "eliminarevaluador";
	public static final String ACCION_INDEX = "listar";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACTIVO = "activo";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_AREAS = "areas";
	public static final String PARAM_DEPARTAMENTO = "departamento";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_USUARIO = "usuario";
	public static final String PARAM_USUARIOS = "usuarios";
	public static final String PARAM_NOMBRE_EVALUADOR = "nombreevaluador";
	public static final Integer PARAM_ROL_COMISION_ID = 1051;

	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_EVALUADOR_YA_EXISTE = "El evaluador ya forma parte del área: %s";
	public static final String MENSAJE_ERROR_ROL_COMISION = "El usuario no tiene rol de comisión";
	public static final String MENSAJE_ERROR_USUARIOS_SELECCIONADOS_INCORRECTOS = "Usuarios seleccionados incorrectos";
	public static final String MENSAJE_EXITO_AGREGAR_EVALUADOR = "Evaluador agregado correctamente al área: %s";
	public static final String MENSAJE_ERROR_AREAS_SELECCIONADAS_INCORRECTAS = "Las áreas seleccionadas no son válidas";
	public static final String MENSAJE_ERROR_SIN_AREAS = "No se ha seleccionado ningún área";
	public static final String MENSAJE_EXITO_BORRAR = "Evaluador borrado correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Evaluador restaurado correctamente";
	
	// Respuesta error
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/evaluadores/";
	public static final String JSP_INDEX = RUTA_BEP_CONF + "evaluadores.jsp";
	
	// ajax	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/evaluadores";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaEvaluadores bean = new VistaEvaluadores();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					index(bean, request);
					break;
				case ACCION_AGREGAR_EVALUADORES:
					agregarEvaluadores(bean, request, response);
					break;
				case ACCION_AREA_SELECCIONADA:
				case ACCION_DATATABLE_EVALUADORES:
				case ACCION_ELIMINAR_EVALUADOR:
					accionesArea(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DATATABLE_AREAS:
					listadoAreas(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
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
	
	private void init(VistaEvaluadores bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		try {
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
		} catch (UVException e) {
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}
	}
	
	private void errorFatal(VistaEvaluadores bean, String mensaje) {
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
	
	/** muestra una tabla seleccionable de usuarios .
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void agregarEvaluadores(VistaEvaluadores bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		Integer idDepartamento = Formateador.leeParametroInteger(request.getParameter(PARAM_DEPARTAMENTO));
		String nombreUsuario = request.getParameter(PARAM_NOMBRE_EVALUADOR);
		
		HttpSession session = request.getSession(false);
		session.setAttribute(PARAM_DEPARTAMENTO, idDepartamento);
		
		// Comprueba si existe el usuario, si no existe lo crea
		try {
			UsuarioBolsaEmpleo usuario = modeloUsuario.obtenerUsuarioBolsaEmpleoSiExiste(nombreUsuario);
			
			if (usuario == null) {
				usuario = modeloUsuario.crearUsuarioBolsaEmpleo(ModeloRol.ID_ROL_MIEMBRO_COMISION, nombreUsuario);
			} else {
				if (!usuario.getRol().getCodNum().equals(ModeloRol.ID_ROL_MIEMBRO_COMISION)) {
					throw new UVException(MENSAJE_ERROR_ROL_COMISION);
				}
			}
			
			ModeloEvaluador modeloEvaluador = ModeloEvaluador.obtenerInstancia();
			
			List<String> areas;
			
			try {
				areas = new GsonBuilder().create().fromJson(request.getParameter(PARAM_AREAS), new TypeToken<List<String>>() { }.getType());
			} catch (Exception ex) {
				throw new UVException(MENSAJE_ERROR_AREAS_SELECCIONADAS_INCORRECTAS);
			}
			
			if (areas != null && areas.size() > 0) {
				for (String idArea: areas) {
					Area area = ModeloArea.obtenerInstancia().getAreaById(Integer.parseInt(idArea));
					
					if (modeloEvaluador.checkEvaluadorArea(area, usuario)) {
						BolsaEmpleoUtils.addMensajeDeError(String.format(MENSAJE_ERROR_EVALUADOR_YA_EXISTE, area.getDescripcion()), bean, request);
						continue;
					}
					
					modeloEvaluador.insertaEvaluador(usuario, Integer.parseInt(idArea));
					BolsaEmpleoUtils.addMensajeDeExito(String.format(MENSAJE_EXITO_AGREGAR_EVALUADOR, area.getDescripcion()), bean, request);
				}
			} else {
				throw new UVException(MENSAJE_ERROR_SIN_AREAS);
			}
			
			
		} catch (UVException ex) {
			BolsaEmpleoUtils.addMensajeDeError(ex.getMessage(), bean, request);
		}
		
		response.sendRedirect(request.getServletPath());
	}
	
	/** eliminar un evaluador.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void eliminarEvaluador(VistaEvaluadores bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloDepartamento modeloDepartamento = ModeloDepartamento.obtenerInstancia();
		ModeloEvaluador modelo = ModeloEvaluador.obtenerInstancia();
		bean.setDepartamento(modeloDepartamento.getDepartamentoByCodNum(Formateador.leeParametroInteger(request.getParameter(PARAM_DEPARTAMENTO))));
		
		Integer codNumUsuario = Formateador.leeParametroInteger(request.getParameter(PARAM_USUARIO));
		boolean activo = "true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACTIVO)));
		Evaluador evaluador = new Evaluador(bean.getArea().getCodNum(), activo);
		evaluador.setCodNum(codNumUsuario);
		bean.setEvaluador(evaluador);
		modelo.borraRestauraEvaluador(evaluador);
		
		HttpSession session = request.getSession(false);
		session.setAttribute(PARAM_AREA, bean.getArea().getCodNum());
		session.setAttribute(PARAM_DEPARTAMENTO, bean.getDepartamento().getCodNum());
		BolsaEmpleoUtils.addMensajeDeExito(activo ? MENSAJE_EXITO_RESTAURAR : MENSAJE_EXITO_BORRAR, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	/** muestra todas las areas en un select .
	 * @param bean bean de la vista a la que poner los valores.
	 * @param request .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException .
	 */
	private void index(VistaEvaluadores bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_INDEX);
		
		ModeloDepartamento modeloDepartamento = ModeloDepartamento.obtenerInstancia();
		bean.setDepartamentos(modeloDepartamento.listaDepartamentosOrderByDesc());
		
		HttpSession session = request.getSession(false);
		if (session.getAttribute(PARAM_AREA) != null) {
			ModeloArea modeloArea = ModeloArea.obtenerInstancia();
			Area area = modeloArea.getAreaById((Integer) session.getAttribute(PARAM_AREA));
			bean.setArea(area);
			session.removeAttribute(PARAM_AREA);
		}
		if (session.getAttribute(PARAM_DEPARTAMENTO) != null) {
			Departamento departamento = modeloDepartamento.getDepartamentoByCodNum((Integer) session.getAttribute(PARAM_DEPARTAMENTO));
			bean.setDepartamento(departamento);
			session.removeAttribute(PARAM_DEPARTAMENTO);
		}
	}
	
	/** acciones con un área .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param nombreAccion .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void accionesArea(VistaEvaluadores bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		bean.setArea(modeloArea.getAreaById(Formateador.leeParametroInteger(request.getParameter(PARAM_AREA))));
		
		switch (nombreAccion) {
			case ACCION_AREA_SELECCIONADA:
				seleccionarArea(bean, request, response);
				break;
			case ACCION_DATATABLE_EVALUADORES:
				listadoEvaluadores(bean, datos, request, response);
				break;
			case ACCION_ELIMINAR_EVALUADOR:
				eliminarEvaluador(bean, request, response);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
		
	}
	
	/** seleccionar un área .
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarArea(VistaEvaluadores bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloDepartamento modeloDepartamento = ModeloDepartamento.obtenerInstancia();
		bean.setDepartamento(modeloDepartamento.getDepartamentoByCodNum(Formateador.leeParametroInteger(request.getParameter(PARAM_DEPARTAMENTO))));
		
		HttpSession session = request.getSession(false);
		session.setAttribute(PARAM_DEPARTAMENTO, bean.getDepartamento().getCodNum());
		session.setAttribute(PARAM_AREA, bean.getArea().getCodNum());
		response.sendRedirect(request.getServletPath());
	}
	
	/** lista de areas datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de IO .
	 * @throws SQLException excepcion de bbdd .
	 */
	private void listadoAreas(VistaEvaluadores bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer idDepartamento = Formateador.leeParametroInteger(request.getParameter(PARAM_DEPARTAMENTO));
				BolsaEmpleoDataTable<Bolsa> dataTable = modeloArea.listaAreaDepartamentoDatatable(request.getParameterMap(), idDepartamento);
				bean.setDatatableAreas(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
		
		datos.setRespuestaEnviada(true);
	}
	
	/** carga los evaluadores en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de IO .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoEvaluadores(VistaEvaluadores bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		ModeloEvaluador modeloEvaluador = ModeloEvaluador.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Evaluador> dataTable = modeloEvaluador.listaEvaluadoresDatatable(request.getParameterMap(), bean.getArea().getCodNum());
				bean.setDatatableEvaluadores(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
		
		datos.setRespuestaEnviada(true);
	}
	
}
