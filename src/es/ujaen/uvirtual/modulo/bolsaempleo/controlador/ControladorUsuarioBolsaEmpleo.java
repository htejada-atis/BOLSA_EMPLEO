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
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
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
	public static final String PARAM_ACCION_USUARIO = "aa";
	public static final String PARAM_USUARIOS_SELECCIONADOS = "usuariosselected";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_NOMBRE_USUARIO = "nombreusuario";
	public static final String PARAM_LISTA = "listadist";
	public static final String PARAM_EXCLUIDO = "excluido";
	public static final String PARAM_EXCLUIDO_TIPO = "excluidotipo";
	public static final String PARAM_FECHA_EXCLUIDO = "fechaexcluido";
	public static final String PARAM_FECHA_EXCLUIDO_INICIO = "fechaexcluidoinicio";
	public static final String PARAM_FECHA_EXCLUIDO_FIN = "fechaexcluidofin";
	
	public static final String PARAM_RAZON_EXCLUIDO = "razonexcluido";
	public static final String PARAM_ROLE = "rol";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_BORRADO = "borrar";
	public static final String PARAM_EMAIL = "email";
	
	// acciones	
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_USUARIO = "accionusuario";
	public static final String ACCION_VOLVER_USUARIO = "volverusuario";
	
	public static final String ACCION_FORMULARIO_USUARIO = "formulariousuario";
	public static final String ACCION_BUSCAR_USUARIO = "buscarusuario";
	public static final String ACCION_AGREGAR_USUARIO = "agregarusuario";
	public static final String ACCION_EDITAR_USUARIO = "editarusuario";
	public static final String ACCION_ELIMINAR_USUARIO = "eliminarusuario";
	public static final String ACCION_LISTAR_USUARIOS = "listar_usuarios";
	public static final String ACCION_LISTAR_ROLES = "listar_roles";
	public static final String ACCION_EXCLUIR_USUARIO = "excluirusuario";
	public static final String ACCION_RECUPERAR_USUARIO = "recuperarusuario";
	public static final String ACCION_INCLUIR_USUARIO = "incluirusuario";
	
	public static final String ACCION_DATATABLE_USUARIOS = "datatableusuarios";
	public static final String ACCION_DATATABLE_USUARIOS_CANDIDATOS = "datatableusuarioscandidatos";
	public static final String ACCION_DATATABLE_USUARIOS_EXCLUIDOS_AREA = "datatableusuariosexcluidosarea";
	public static final String ACCION_DATATABLE_USUARIOS_BORRADOS = "datatableusuariosborrados";
	public static final String ACCION_DATATABLE_USUARIOS_EXCLUIDOS = "datatableusuariosexcluidos";
	
	public static final String ACCION_EXCLUIR_USUARIO_AREA = "excluirusuarioarea";
	public static final String ACCION_INCLUIR_USUARIO_AREA = "incluirusuarioarea";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_AGREGAR = "usuario creado correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "usuario editadi correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "usuario eliminado correctamente";
	public static final String MENSAJE_ERROR_ACCION_USUARIO_NO_VALIDA = "Acción no válida";
	public static final String MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE = "Usuario/s modificado/s correctamente"; 
	
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_VACIO = "Si excluye al usuario, debe especificar una razón";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_LARGO = "La razón de exclusión no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_ROL_VACIO = "El rol no puede estar vacio";
	public static final String MENSAJE_ERROR_ROL_NEGATIVO = "Debe seleccionar un role válido";

	public static final String MENSAJE_ERROR_EXCLUIDO_TIPO_VACIO = "El tipo de exclusión no puede estar vacio";
	
	public static final String MENSAJE_ERROR_FECHA_EXCLUIDO_INICIO_REQUERIDA = "La fecha de inicio es requerida";
	public static final String MENSAJE_ERROR_FECHA_EXCLUIDO_FIN_REQUERIDA = "La fecha de fin es requerida";
	
	// ajax	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/usuarios";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/usuarios/";
	public static final String JSP_USUARIOS = RUTA_BEP_CONF + "usuarios.jsp";
	public static final String JSP_BUSCAR_USUARIO = RUTA_BEP_CONF + "buscarUsuario.jsp";
	public static final String JSP_FORM_USUARIO = RUTA_BEP_CONF + "formUsuario.jsp";
		
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaUsuarioBolsaEmpleo bean = new VistaUsuarioBolsaEmpleo();		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
					
		try {
			init(bean, datos);
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(JSP_USUARIOS);
					break;
				case ACCION_FORMULARIO_USUARIO:
					formularioUsuario(bean);
					break;
				case ACCION_BUSCAR_USUARIO:
					buscarUsuario(request, bean);
					break;
				case ACCION_DATATABLE_USUARIOS:
					listado(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_USUARIOS_BORRADOS:
					listadoBorrados(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_USUARIOS_EXCLUIDOS:
					listadoExcluidos(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_USUARIOS_EXCLUIDOS_AREA:
					listadoExcluidosArea(bean, datos, request, response);
					break;
				case ACCION_LISTAR_ROLES:
					obtenerRoles(bean);
					break;
				case ACCION_AGREGAR_USUARIO:
					agregarUsuario(request, response, bean);
					break;
				case ACCION_EDITAR_USUARIO:
					editarUsuario(request, response, bean);
					break;
				case ACCION_INCLUIR_USUARIO:
					incluirUsuario(request, response, bean);
					break;
				case ACCION_EXCLUIR_USUARIO:
					excluirUsuario(request, response, bean);
					break;
				case ACCION_USUARIO:
					accionSobreUsuario(request, bean);						
					break;
				case ACCION_VOLVER_USUARIO:
					volverUsuario(bean);
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
	
	/** Redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void init(VistaUsuarioBolsaEmpleo bean, UVDatos datos) throws SQLException, UVException {
		bean.setVista(JSP_USUARIOS);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
	}
	
	private void errorFatal(VistaUsuarioBolsaEmpleo bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
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
	private void listado(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioBolsaEmpleoDatatable(request.getParameterMap());
				bean.setDatatable(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void listadoExcluidosArea(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreasExcluidasPorUsuarioDatatable(request.getParameterMap(), codNum);
				bean.setDatatableBolsa(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void accionSobreUsuario(HttpServletRequest request, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_USUARIOS_SELECCIONADOS));
		String nombreAccionUsuario = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION_USUARIO));
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		
		List<UsuarioBolsaEmpleo> usuarios = modelo.getUsuariosByIds(selected);
		List<Area> areas = modeloArea.getAreasByIds(selected);

		switch (nombreAccionUsuario) {
			case ACCION_ELIMINAR_USUARIO:
				modelo.ponerUsuarioComoBorrado(usuarios);
				break;
			case ACCION_RECUPERAR_USUARIO:
				modelo.ponerUsuarioComoNoBorrado(usuarios);
				break;
			case ACCION_EXCLUIR_USUARIO_AREA:
				excluirUsuarioArea(bean, codNum, modelo, areas);
				break;
			case ACCION_INCLUIR_USUARIO_AREA:
				incluirUsuarioArea(bean, codNum, modelo, areas);
				break;
			default:
				bean.getMensajesDeError().add(MENSAJE_ERROR_ACCION_USUARIO_NO_VALIDA);
				return;
		}

		bean.getMensajesDeExito().add(MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE);
	}

	private void incluirUsuarioArea(VistaUsuarioBolsaEmpleo bean, Integer codNum, ModeloUsuarioBolsaEmpleo modelo,
			List<Area> areas) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuCont = modelo.getUsuarioById(codNum);
		Usuario usuArcosCont = CrearUsuario.usuario(usuCont.getCodCuenta());
		modelo.incluirUsuarioArea(usuCont, areas);

		bean.setUsuarioArcos(usuArcosCont);
		bean.setBusqueda(false);
		
		bean.setUsuario(usuCont);
		bean.setVista(JSP_FORM_USUARIO);
	}

	private void excluirUsuarioArea(VistaUsuarioBolsaEmpleo bean, Integer codNum, ModeloUsuarioBolsaEmpleo modelo, List<Area> areas) throws SQLException, UVException {
		UsuarioBolsaEmpleo usu = modelo.getUsuarioById(codNum);
		Usuario usuArcos = CrearUsuario.usuario(usu.getCodCuenta());
		modelo.excluirUsuarioArea(usu, areas);
		
		bean.setUsuarioArcos(usuArcos);
		bean.setBusqueda(false);
		bean.setUsuario(usu);
		bean.setVista(JSP_FORM_USUARIO);
	}
	
	/** Busca un usuario.
	 * @param request .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void buscarUsuario(HttpServletRequest request, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException {			
		obtenerRoles(bean);
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE));
		if (usuArcos == null) {
			throw new UVException("No existe el usuario");
		}
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		try {
			UsuarioBolsaEmpleo usu = modelo.listaUsuario(Formateador.leeParametroString(usuArcos.getDocumentoNumero()));
			
			bean.setBusqueda(true);
			
			bean.setUsuarioArcos(usuArcos);
			bean.setUsuario(usu);
			bean.setRol(usu.getRol());
			
			bean.setVista(JSP_FORM_USUARIO);
		} catch (UVException e) {
			usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE));
			if (usuArcos != null) {
				bean.setBusqueda(false);
				bean.setUsuarioArcos(usuArcos);
				bean.setVista(JSP_FORM_USUARIO);
			} else {
				bean.setVista(JSP_BUSCAR_USUARIO);
				throw new UVException("No existe el usuario");
			}
		}
	}
	
	
	
	/** agrega un nuevo usuario.
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	public void agregarUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException, IOException {
		bean.setVista(JSP_USUARIOS);
		
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_ID));
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();		
		UsuarioBolsaEmpleo usuarioForm = this.getValidatorUsuarios(request);
		usuarioForm.setCodCuenta(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ID)));		
		usuarioForm.setNumDocumento(usuArcos.getDocumentoNumero());
						
		modelo.insertaUsuario(usuarioForm);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
		response.sendRedirect(request.getServletPath());	
	}
	
	/** edita un usuario .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void editarUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) 
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_FORM_USUARIO);
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		obtenerRoles(bean);
		
		UsuarioBolsaEmpleo usu = modelo.getUsuarioByCodCuenta(Formateador.leeParametroString(request.getParameter(PARAM_NOMBRE_USUARIO)));
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
		
		bean.setBusqueda(false);
		bean.setUsuarioArcos(usuArcos);
		bean.setUsuario(usu);
		bean.setRol(usu.getRol());
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ROLE)) != null) {
			UsuarioBolsaEmpleo usuarioForm = this.getValidatorUsuarios(request);
			usuarioForm.setCodNum(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
			
			modelo.actualizaUsuario(usuarioForm);
			response.sendRedirect(request.getServletPath());	
			bean.getMensajesDeExito().add(MENSAJE_EXITO_EDITAR);			
		}
	}
	
	
	/** incluir un usuario .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void incluirUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException, IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		obtenerRoles(bean);
		
		UsuarioBolsaEmpleo usu = modelo.getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
			
		modelo.ponerUsuarioComoNoExcluido(usu);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR);
		response.sendRedirect(request.getServletPath());
	}
	
	
	/** excluir un usuario .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void excluirUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException, IOException {
		bean.setVista(JSP_BUSCAR_USUARIO);
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		obtenerRoles(bean);
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
		if (usuArcos == null) {
			throw new UVException("No existe el usuario");
		}
		
		UsuarioBolsaEmpleo usu = modelo.listaUsuario(usuArcos.getDocumentoNumero());

		bean.setBusqueda(true);
		
		bean.setUsuarioArcos(usuArcos);
		bean.setUsuario(usu);
		bean.setRol(usu.getRol());
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO)) != null) {
			UsuarioBolsaEmpleo usuarioForm = this.validateExclusion(request);			
			usuarioForm.setCodNum(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
			usuarioForm.setExcluido(true);

			modelo.ponerUsuarioComoExcluido(usuarioForm);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** dirige al formulario de busqueda de un usuario.
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void formularioUsuario(VistaUsuarioBolsaEmpleo bean) {
		bean.setVista(JSP_BUSCAR_USUARIO);
		bean.setBusqueda(false);
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
	
	/** dirige a la vista de usuarios .
	 * @param bean bean de la vista a la que poner los valores.
	 */
	private void volverUsuario(VistaUsuarioBolsaEmpleo bean) {
		bean.setVista(JSP_USUARIOS);
	}
	
	private UsuarioBolsaEmpleo getValidatorUsuarios(HttpServletRequest request) throws UVException, SQLException {
		UsuarioBolsaEmpleo u = new UsuarioBolsaEmpleo();
	
		String excluido = request.getParameter(PARAM_EXCLUIDO);
		if (excluido != null) {
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
			u.setFechaExclusion(BolsaEmpleoUtils.getCurrentDate());
		}
		
		Rol rol = ModeloRol.obtenerInstancia().getRoleById(Formateador.leeParametroInteger(request.getParameter(PARAM_ROLE)));
		u.setRol(rol);
		
		u.setListaDist("true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LISTA))));
		u.setExcluido("true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO))));
		
		return u;
	}
	
	private UsuarioBolsaEmpleo validateExclusion(HttpServletRequest request) throws UVException {
		UsuarioBolsaEmpleo u = new UsuarioBolsaEmpleo();
				
		if (request.getParameter(PARAM_RAZON_EXCLUIDO) != null) {
			u.setRazonExcluido(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO)));
			if (u.getRazonExcluido() == null || u.getRazonExcluido().isBlank()) {
				throw new UVException(MENSAJE_ERROR_RAZON_EXCLUSION_VACIO);
			}
			if (u.getRazonExcluido().length() > ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH) {
				throw new UVException(String.format(MENSAJE_ERROR_RAZON_EXCLUSION_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH));
			}
			
			u.setFechaExclusion(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO), Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
			
			u.setExcluidoTipo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO_TIPO)));
			if ("T".equals(u.getExcluidoTipo())) {
				u.setFechaExclusionInicio(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_INICIO), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
				u.setFechaExclusionFin(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_FIN), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
			}
		}
		
		return u;
	}
	
	/**
	 * AJAX para devolver listado de usuarios borrados.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 */
	private void listadoBorrados(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioBorradoBolsaEmpleoDatatable(request.getParameterMap());
				bean.setDatatable(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
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
	 */
	private void listadoExcluidos(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioExcluidoBolsaEmpleoDatatable(request.getParameterMap());
				bean.setDatatable(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
}
