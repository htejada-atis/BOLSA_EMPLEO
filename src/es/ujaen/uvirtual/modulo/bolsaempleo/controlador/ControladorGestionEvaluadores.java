package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

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

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
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
	public static final String ACCION_DATATABLE_EVALUADORES = "datatableevaluadores";
	public static final String ACCION_DATATABLE_USUARIOS = "datatableusuarios";
	public static final String ACCION_ELIMINAR_EVALUADOR = "eliminarevaluador";
	public static final String ACCION_LISTAR_AREAS = "listarareas";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACTIVO = "activo";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_USUARIO = "usuario";
	public static final String PARAM_USUARIOS = "usuarios";
	public static final String PARAM_NOMBRE_EVALUADOR = "nombreevaluador";
	public static final Integer PARAM_ROL_COMISION_ID = 1051;

	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_USUARIOS_SELECCIONADOS_INCORRECTOS = "Usuarios seleccionados incorrectos";
	public static final String MENSAJE_EXITO_AGREGAR_EVALUADORES = "Evaluadores agregados correctamente";
	public static final String MENSAJE_EXITO_BORRAR = "Evaluador borrado correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Evaluador restaurado correctamente";
	
	// Respuesta error
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/evaluadores/";
	public static final String JSP_INDEX = RUTA_BEP_CONF + "evaluadoresListar.jsp";
	
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
			nombreAccion = ACCION_LISTAR_AREAS;
		}
		
		try {
			init(bean, datos);
			switch (nombreAccion) {
				case ACCION_LISTAR_AREAS:
					obtenerAreas(bean, request);
					break;
				case ACCION_AGREGAR_EVALUADORES:
					agregarEvaluadores(bean, request, response);
					break;
				case ACCION_DATATABLE_EVALUADORES:
					listadoEvaluadores(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_USUARIOS:
					listadoUsuarios(bean, datos, request, response);
					break;
				case ACCION_ELIMINAR_EVALUADOR:
					eliminarEvaluador(bean, request, response);
					break;				
				default:
					errorFatal(bean, "Acción no contemplada");
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
	
	private void init(VistaEvaluadores bean, UVDatos datos) throws SQLException, UVException {
		bean.setVista(JSP_INDEX);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
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
//		bean.setVista(RUTA_BEP_CONF + "evaluadoresAgregar.jsp");
//		
//		ModeloArea modelo = ModeloArea.obtenerInstancia();	
//		Integer idArea = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
//		Area area = modelo.getAreaById(idArea);
//		bean.setArea(area);
//		
//		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_USUARIOS)) != null) {
//			Gson gson = new GsonBuilder().create();
//			
//			try {
//				List<String> usuarios = gson.fromJson(request.getParameter(PARAM_USUARIOS), new TypeToken<List<String>>() { }.getType());
//				ModeloEvaluador modeloEvaluador = ModeloEvaluador.obtenerInstancia();
//				modeloEvaluador.insertaEvaluadores(usuarios, idArea);
//				bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR_EVALUADORES);
//				HttpSession session = request.getSession(false);
//				session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR_EVALUADORES);
//				session.setAttribute(PARAM_AREA, idArea);
//				response.sendRedirect(request.getServletPath());
//			} catch (Exception ex) {
//				throw new UVException(MENSAJE_ERROR_USUARIOS_SELECCIONADOS_INCORRECTOS);
//			}
//		}
				
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_EVALUADOR));
		
		if (usuArcos == null) {
			bean.setVista(JSP_INDEX);
			List<Area> areas = ModeloArea.obtenerInstancia().listaAreas();
			bean.setAreas(areas);
			throw new UVException("No existe el evaluador en el sistema, primero debe crearlo");
		}
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		ModeloEvaluador modeloEvaluador = ModeloEvaluador.obtenerInstancia();
		
		try {			
			UsuarioBolsaEmpleo usu = modelo.listaUsuario(usuArcos.getDocumentoNumero());
			Integer idArea = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
			
			modeloEvaluador.insertaEvaluador(usu, idArea);
			List<Area> areas = ModeloArea.obtenerInstancia().listaAreas();
			bean.setAreas(areas);
			
			Area area = ModeloArea.obtenerInstancia().getAreaById(idArea);
			bean.setArea(area);
			
			bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR_EVALUADORES);
			bean.setVista(JSP_INDEX);

		} catch (UVException e) {
			
			try {
				
				Rol role = ModeloRol.obtenerInstancia().getRoleById(PARAM_ROL_COMISION_ID);
				UsuarioBolsaEmpleo usuarioFinal = 
						new UsuarioBolsaEmpleo(usuArcos.getDocumentoNumero(), usuArcos.getUid(), role, true, false, null, null, null);
						
				modelo.insertaUsuario(usuarioFinal);
					
				CrearUsuario.refrescarUsuario(usuarioFinal.getCodCuenta());
				
				UsuarioBolsaEmpleo usu = modelo.listaUsuario(usuarioFinal.getNumDocumento());
				Integer idArea = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
				
				modeloEvaluador.insertaEvaluador(usu, idArea);
				
				List<Area> areas = ModeloArea.obtenerInstancia().listaAreas();
				bean.setAreas(areas);

				bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR_EVALUADORES);
			} catch (UVException ex) {
				throw new UVException("No existe el evaluador en el sistema, primero debe crearlo");
			}
			
			bean.setVista(JSP_INDEX);
		}
	}
	
	/** eliminar un evaluador.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void eliminarEvaluador(VistaEvaluadores bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloEvaluador modelo = ModeloEvaluador.obtenerInstancia();
		Integer codNumUsuario = Formateador.leeParametroInteger(request.getParameter(PARAM_USUARIO));
		Integer codNumArea = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
		boolean activo = "true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACTIVO)));
		Evaluador evaluador = new Evaluador(codNumArea, activo);
		evaluador.setCodNum(codNumUsuario);
		bean.setEvaluador(evaluador);
		modelo.borraRestauraEvaluador(evaluador);
		bean.setVista(JSP_INDEX);
		bean.getMensajesDeExito().add(activo ? MENSAJE_EXITO_RESTAURAR : MENSAJE_EXITO_BORRAR);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, activo ? MENSAJE_EXITO_RESTAURAR : MENSAJE_EXITO_BORRAR);
		session.setAttribute(PARAM_AREA, codNumArea);
		response.sendRedirect(request.getServletPath());
	}
	
	/** muestra todas las areas en un select .
	 * @param bean bean de la vista a la que poner los valores.
	 * @param request .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException .
	 */
	private void obtenerAreas(VistaEvaluadores bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_INDEX);
		
		ModeloArea modelo = ModeloArea.obtenerInstancia();
		List<Area> areas = modelo.listaAreas();
		bean.setAreas(areas);
		
		HttpSession session = request.getSession(false);
		String mensaje = (String) session.getAttribute(MENSAJE_ENVIADO);

		if (mensaje != null) {
			switch (mensaje) {
				case MENSAJE_EXITO_AGREGAR_EVALUADORES:
				case MENSAJE_EXITO_BORRAR:
				case MENSAJE_EXITO_RESTAURAR:
					Area area = modelo.getAreaById((Integer) session.getAttribute(PARAM_AREA));
					bean.setArea(area);
					session.removeAttribute(PARAM_AREA);
					break;
			}
		}
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
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer idArea = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
				Area area = modeloArea.getAreaById(idArea);
				bean.setArea(area);
				BolsaEmpleoDataTable<Evaluador> dataTable = modeloEvaluador.listaEvaluadoresDatatable(request.getParameterMap(), area.getCodNum());
				bean.setDatatableUsuarios(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
		
		datos.setRespuestaEnviada(true);
	}
	
	/** carga los usuarios en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de IO .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoUsuarios(VistaEvaluadores bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		ModeloEvaluador modelo = ModeloEvaluador.obtenerInstancia();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer idArea = Formateador.leeParametroInteger(request.getParameter(PARAM_AREA));
				Area area = modeloArea.getAreaById(idArea);
				bean.setArea(area);
				BolsaEmpleoDataTable<Evaluador> dataTable = modelo.listaUsuariosRestantesDatatable(request.getParameterMap(), area.getCodNum());
				bean.setDatatableUsuarios(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
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
