package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDepartamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los uauarios de UVIRTUAL.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.usuarios", 
	description = "Gestión de las áreas a baremar", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/usuarios", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/usuarios",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/usuarios",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/usuarios"
	})
public class ControladorUsuarioBolsaEmpleo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorUsuarioBolsaEmpleo.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AGREGAR = "agregar";
	public static final String PARAM_EXCLUIDO = "excluido";
	public static final String PARAM_EXCLUIDO_TIPO = "excluidotipo";
	public static final String PARAM_FECHA_EXCLUIDO = "fechaexcluido";
	public static final String PARAM_FECHA_EXCLUIDO_INICIO = "fechaexcluidoinicio";
	public static final String PARAM_FECHA_EXCLUIDO_FIN = "fechaexcluidofin";
	public static final String PARAM_GUARDAR = "guardar";
	public static final String PARAM_LISTA = "listadist";
	public static final String PARAM_NOMBRE_USUARIO = "nombreusuario";
	public static final String PARAM_VOLVER = "volver";
	public static final String PARAM_RAZON_BORRADO = "razonborrado";
	public static final String PARAM_RAZON_EXCLUIDO = "razonexcluido";
	public static final String PARAM_ROLE = "rol";
	public static final String PARAM_USUARIO = "usuario";
	
	// acciones
	public static final String ACCION_ACTUALIZAR_AREAS = "actualizarareas";
	public static final String ACCION_AREAS_EVALUABLES = "areasevaluables";
	public static final String ACCION_AGREGAR_USUARIO = "agregarusuario";
	public static final String ACCION_BUSCAR_USUARIO = "buscarusuario";
	public static final String ACCION_DATATABLE_AREAS_EVALUABLES_USUARIO = "datatableareasevaluablesuruario";
	public static final String ACCION_DATATABLE_DEPARTAMENTOS_USUARIO = "datatabledepartamentosuruario";
	public static final String ACCION_DATATABLE_USUARIOS = "datatableusuarios";
	public static final String ACCION_DEPARTAMENTOS = "departamentos";
	public static final String ACCION_EDITAR_USUARIO = "editarusuario";
	public static final String ACCION_ELIMINAR_USUARIO = "eliminarusuario";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_RECUPERAR_USUARIO = "recuperarusuario";
	public static final String ACCION_SELECCIONAR_USUARIO = "seleccionarusuario";
	public static final String ACCION_USUARIO = "accionusuario";
	public static final String ACCION_VOLVER_USUARIO = "volverusuario";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_EXCLUSION_USUARIO_LOGUEADO =
			"No puede auto excluirse, si desea que su perfil se excluya del sistema contacte con un administrador";
	public static final String MENSAJE_ERROR_ELIMINAR_USUARIO_LOGUEADO =
			"No puede auto eliminarse, si desea que su perfil sea dado de baja del sistema contacte con un administrador";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_VACIO = "Si excluye al usuario, debe especificar una razón";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_LARGO = "La razón de exclusión no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_SIN_PERMISO_PERSONAL = "No tienes permiso de personal";
	public static final String MENSAJE_ERROR_USUARIO_NO_EXISTE = "No existe el usuario";
	
	public static final String MENSAJE_EXITO_ACTUALIZAR_AREAS = "Áreas actualizadas correctamente";
	public static final String MENSAJE_EXITO_AGREGAR = "Usuario creado correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "usuario editado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Usuario eliminado correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Usuario restaurado correctamente";
	
	
	// ajax	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/usuarios";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	// ruta vistas
	public static final String RUTA_BEP_CONF_USU = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/usuarios/";
	public static final String JSP_USUARIOS = RUTA_BEP_CONF_USU + "usuarios.jsp";
	public static final String JSP_FORM_USUARIO = RUTA_BEP_CONF_USU + "formUsuario.jsp";
		
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:JavaNCSS"})
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaUsuarioBolsaEmpleo bean = new VistaUsuarioBolsaEmpleo();		
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_AGREGAR_USUARIO:
				case ACCION_BUSCAR_USUARIO:
					accionesUsuarioArcos(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DATATABLE_USUARIOS:
					listadoUsuarios(bean, datos, request, response);
					break;
				case ACCION_ACTUALIZAR_AREAS:
				case ACCION_AREAS_EVALUABLES:
				case ACCION_DATATABLE_AREAS_EVALUABLES_USUARIO:
				case ACCION_DATATABLE_DEPARTAMENTOS_USUARIO:
				case ACCION_DEPARTAMENTOS:
				case ACCION_EDITAR_USUARIO:
				case ACCION_ELIMINAR_USUARIO:
				case ACCION_RECUPERAR_USUARIO:
				case ACCION_SELECCIONAR_USUARIO:
					accionesUsuarioBolsaEmpleo(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_INDEX:
					bean.setVista(JSP_USUARIOS);
					break;
				case ACCION_VOLVER_USUARIO:
					datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
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
			bean.getMensajesDeError().add(MENSAJE_ERROR_SIN_PERMISO_PERSONAL);
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
	
	/** Redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	private void init(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_USUARIOS);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}		
	}
	
	private void errorFatal(VistaUsuarioBolsaEmpleo bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// USUARIOS ARCOS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesUsuarioArcos(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
		if (usuArcos == null) {
			throw new UVException(ModeloUsuarioBolsaEmpleo.MENSAJE_BUSCAR_USUARIO_NO_EXISTE);
		}
		bean.setUsuarioArcos(usuArcos);
		
		switch (nombreAccion) {
			case ACCION_AGREGAR_USUARIO:
				agregarUsuario(bean, datos, request, response);
				break;
			case ACCION_BUSCAR_USUARIO:
				buscarUsuario(bean);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	/** agrega un nuevo usuario.
	 * @param bean de la vista a la que poner los valores .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error en bd .
	 */
	public void agregarUsuario(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		UsuarioBolsaEmpleo usuarioForm = this.getValidatorUsuario(bean, request);
		
		modelo.crearUsuarioBolsaEmpleo(usuarioForm.getRol().getCodNum(), bean.getUsuarioArcos().getUid(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	/** Busca un usuario .
	 * @param bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error en bd .
	 */
	private void buscarUsuario(VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException {
		obtenerRoles(bean);
		
		UsuarioBolsaEmpleo usu = ModeloUsuarioBolsaEmpleo.obtenerInstancia().compruebaUsuarioByCodCuenta(bean.getUsuarioArcos().getUid());
		
		if (usu != null) {
			bean.setBusqueda(true);
			bean.setUsuario(usu);
			bean.setRol(usu.getRol());
		} else {
			bean.setBusqueda(false);
		}
		
		bean.setVista(JSP_FORM_USUARIO);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// USUARIOS BOLSA EMPLEO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesUsuarioBolsaEmpleo(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_FORM_USUARIO);
		
		obtenerRoles(bean);
		
		Integer idUsuario = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_USUARIO));
		UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(idUsuario);
		Usuario usuArcos = CrearUsuario.usuario(usuario.getCodCuenta());
		bean.setUsuario(usuario);
		bean.setUsuarioArcos(usuArcos);
		bean.setBusqueda(false);
		bean.setRol(usuario.getRol());
		
		switch (nombreAccion) {
			case ACCION_ACTUALIZAR_AREAS:
				actualizarAreasDirectorDepartamento(bean, datos, request, response);
				break;
			case ACCION_AREAS_EVALUABLES:
				bean.setApartadoAreasEvaluables(true);
				break;
			case ACCION_DATATABLE_AREAS_EVALUABLES_USUARIO:
				listadoAreasEvaluables(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_DEPARTAMENTOS_USUARIO:
				listadoDepartamentos(bean, datos, request, response);
				break;
			case ACCION_DEPARTAMENTOS:
				bean.setApartadoDepartamentos(true);
				break;
			case ACCION_EDITAR_USUARIO:
				editarUsuario(bean, datos, request, response);
				break;
			case ACCION_ELIMINAR_USUARIO:
				eliminarUsuario(bean, datos, request, response);
				break;
			case ACCION_RECUPERAR_USUARIO:
				recuperarUsuario(bean, datos, request, response);
				break;
			case ACCION_SELECCIONAR_USUARIO:
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void actualizarAreasDirectorDepartamento(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, UVException {
		ModeloEvaluador.obtenerInstancia().actualizaAreasDirectorDepartamento(bean.getUsuario(), bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ACTUALIZAR_AREAS, bean, request);
		redireccionConUsuarioSeleccionado(bean, datos, request, response, ACCION_AREAS_EVALUABLES);
	}
	
	private void eliminarUsuario(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		if (bean.getUsuarioArcos().getDocumentoNumero().equals(bean.getUsuarioLogeado().getPrsNif())) {
			throw new UVException(MENSAJE_ERROR_ELIMINAR_USUARIO_LOGUEADO);
		}
		
		String razonBorrado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_BORRADO));
		bean.getUsuario().setRazonBorrado(razonBorrado);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoBorrado(bean.getUsuario(), bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void recuperarUsuario(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoNoBorrado(bean.getUsuario(), bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_RESTAURAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	/** edita un usuario .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error en bd .
	 * @throws IOException en caso de error de IO .
	 */
	private void editarUsuario(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_GUARDAR)) != null) {
			UsuarioBolsaEmpleo usuarioForm = this.getValidatorUsuario(bean, request);
			usuarioForm.setCodCuenta(bean.getUsuario().getCodCuenta());
			usuarioForm.setCodNum(bean.getUsuario().getCodNum());
			usuarioForm.setUsuarioArcos(bean.getUsuarioArcos());
			usuarioForm.setDireccion(bean.getUsuario().getDireccion());
			usuarioForm.setCodigoPostal(bean.getUsuario().getCodigoPostal());
			usuarioForm.setLocalidad(bean.getUsuario().getLocalidad());
			usuarioForm.setProvincia(bean.getUsuario().getProvincia());
			usuarioForm.setNacionalidad(bean.getUsuario().getNacionalidad());
			usuarioForm.setTelefono(bean.getUsuario().getTelefono());
			
			modelo.actualizaUsuario(usuarioForm, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void redireccionConUsuarioSeleccionado(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_ACCION, accion);
		params.put(PARAM_USUARIO, bean.getUsuario().getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	/**
	 * AJAX para devolver datatable áreas de un evaluador .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoAreasEvaluables(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = ModeloArea.obtenerInstancia().listaAreasEvaluadorDatatable(request.getParameterMap(), bean.getUsuario());
				bean.setDatatableAreasEvaluables(dataTable);
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
	
	/**
	 * AJAX para devolver datatable departamentos de un director de departamento .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoDepartamentos(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Departamento> dataTable = ModeloDepartamento.obtenerInstancia().listaDepartamentosDirectorDatatable(
						request.getParameterMap(), bean.getUsuario());
				bean.setDataTableDepartamentos(dataTable);
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
	
	/**
	 * AJAX para devolver listado de usuarios.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException . 
	 * @throws SQLException .
	 */
	private void listadoUsuarios(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioBolsaEmpleoDatatable(request.getParameterMap());
				bean.setDatatableUsuarios(dataTable);
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
	
	/** muestra todos los roles en un select .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerRoles(VistaUsuarioBolsaEmpleo bean) throws SQLException {
		ModeloRol modelo = ModeloRol.obtenerInstancia();
		List<Rol> roles = modelo.listaRoles();
		bean.setRoles(roles);
	}
	
	private UsuarioBolsaEmpleo getValidatorUsuario(VistaUsuarioBolsaEmpleo bean, HttpServletRequest request) throws UVException, SQLException {
		UsuarioBolsaEmpleo u = new UsuarioBolsaEmpleo();
		
		String excluido = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO));
		u.setExcluido("true".equals(excluido));
		if (excluido != null) {
			Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
			if (usuArcos.getDocumentoNumero().equals(bean.getUsuarioLogeado().getPrsNif())) {
				throw new UVException(MENSAJE_ERROR_EXCLUSION_USUARIO_LOGUEADO);
			}
			
			u.setRazonExcluido(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO)));
			if (u.getRazonExcluido() == null || u.getRazonExcluido().isBlank()) {
				throw new UVException(MENSAJE_ERROR_RAZON_EXCLUSION_VACIO);
			}
			if (u.getRazonExcluido().length() > ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH) {
				throw new UVException(String.format(MENSAJE_ERROR_RAZON_EXCLUSION_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH));
			}
			
			u.setExcluidoTipo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO_TIPO)));
			if ("T".equals(u.getExcluidoTipo())) {
				u.setFechaExclusionInicio(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_INICIO), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
				u.setFechaExclusionFin(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_FIN), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
			}
			u.setFechaExclusion(BolsaEmpleoUtils.getCurrentDateTime());
		}
		
		Rol rol = ModeloRol.obtenerInstancia().getRoleById(Formateador.leeParametroInteger(request.getParameter(PARAM_ROLE)));
		u.setRol(rol);
		
		u.setListaDist("true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LISTA))));
		
		return u;
	}
}
