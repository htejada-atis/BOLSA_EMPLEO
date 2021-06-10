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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.AdaptadorDocumentoIdentidad;
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
	public static final String ACCION_USUARIO = "accionusuario";
	public static final String ACCION_VOLVER_USUARIO = "volverusuario";
	public static final String ACCION_FORMULARIO_USUARIO = "formulariousuario";
	public static final String ACCION_BUSCAR_USUARIO = "buscarusuario";
	public static final String ACCION_AGREGAR_USUARIO = "agregarusuario";
	public static final String ACCION_EDITAR_USUARIO = "editarusuario";
	public static final String ACCION_ELIMINAR_USUARIO = "eliminarusuario";
	public static final String ACCION_LISTAR_ROLES = "listar_roles";
	public static final String ACCION_EXCLUIR_USUARIO = "excluirusuario";
	public static final String ACCION_RECUPERAR_USUARIO = "recuperarusuario";
	public static final String ACCION_INCLUIR_USUARIO = "incluirusuario";
	public static final String ACCION_DATATABLE_USUARIOS = "datatableusuarios";
	
	// mensajes
	public static final String MENSAJE_EXITO_AGREGAR = "Usuario creado correctamente";
	public static final String MENSAJE_EXITO_EDITAR = "usuario editadi correctamente";
	public static final String MENSAJE_ERROR_ACCION_USUARIO_NO_VALIDA = "Acción no válida";
	public static final String MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE = "Usuario/s modificado/s correctamente"; 
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_VACIO = "Si excluye al usuario, debe especificar una razón";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_LARGO = "La razón de exclusión no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_EXCLUSION_USUARIO_LOGUEADO =
			"No puede auto excluirse, si desea que su perfil se excluya del sistema contacte con un administrador";
	
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
			init(bean, datos, request, response);
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
				case ACCION_LISTAR_ROLES:
					obtenerRoles(bean);
					break;
				case ACCION_AGREGAR_USUARIO:
					agregarUsuario(request, response, bean, usuario);
					break;
				case ACCION_EDITAR_USUARIO:
					editarUsuario(request, response, bean, usuario);
					break;
				case ACCION_INCLUIR_USUARIO:
					incluirUsuario(request, response, bean);
					break;
				case ACCION_EXCLUIR_USUARIO:
					excluirUsuario(request, response, bean, usuario);
					break;
				case ACCION_USUARIO:
					accionSobreUsuario(bean, request, response);						
					break;
				case ACCION_VOLVER_USUARIO:
					volverUsuario(bean);
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
	
	private void init(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_USUARIOS);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}		
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
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void accionSobreUsuario(VistaUsuarioBolsaEmpleo bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_USUARIOS_SELECCIONADOS));
		String nombreAccionUsuario = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION_USUARIO));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		List<UsuarioBolsaEmpleo> usuarios = modelo.getUsuariosByIds(selected);

		switch (nombreAccionUsuario) {
			case ACCION_ELIMINAR_USUARIO:
				modelo.ponerUsuarioComoBorrado(usuarios, bean.getUsuarioLogeado());
				break;
			case ACCION_RECUPERAR_USUARIO:
				modelo.ponerUsuarioComoNoBorrado(usuarios, bean.getUsuarioLogeado());
				break;
			default:
				BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_ACCION_USUARIO_NO_VALIDA, bean, request);
				response.sendRedirect(request.getServletPath());
				return;
		}

		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE, bean, request);
		response.sendRedirect(request.getServletPath());
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
		
		try {
			UsuarioBolsaEmpleo usu = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(usuArcos.getUid());
			
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
	 * @param usu .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	public void agregarUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean, Usuario usu) 
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_USUARIOS);
		
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_ID));
		if (usuArcos == null) {
			throw new UVException("No existe el usuario");
		}
			
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();		
		UsuarioBolsaEmpleo usuarioForm = this.getValidatorUsuario(request, usu);
						
		modelo.crearUsuarioBolsaEmpleo(usuarioForm.getRol().getCodNum(), usuArcos.getUid(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	/** edita un usuario .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @param usuar .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void editarUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean, Usuario usuar) 
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
			UsuarioBolsaEmpleo usuarioForm = this.getValidatorUsuario(request, usuar);
			usuarioForm.setCodCuenta(usu.getCodCuenta());
			usuarioForm.setCodNum(usu.getCodNum());			
			usuarioForm.setCodCuenta(usuArcos.getUid());
			usuarioForm.setTipoDocumento(usuArcos.getDocumentoTipo());
			usuarioForm.setIdNif(AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuArcos));
			usuarioForm.setLetraNif(AdaptadorDocumentoIdentidad.letraNIF(usuArcos.getDocumentoTipo(), usuArcos.getDocumentoNumero()));
			usuarioForm.setPrsNif(usuArcos.getDocumentoNumero());
			usuarioForm.setNombre(usuArcos.getNombre());
			usuarioForm.setPrimerApellido(usuArcos.getApellido1());
			usuarioForm.setSegundoApellido(usuArcos.getApellido2());
			usuarioForm.setEmail(usuArcos.getEmailCalculado());
			
			modelo.actualizaUsuario(usuarioForm, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
			response.sendRedirect(request.getServletPath());
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
			
		modelo.ponerUsuarioComoNoExcluido(usu, bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	
	/** excluir un usuario .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @param usua .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 * @throws IOException en caso de error de IO.
	 */
	private void excluirUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean, Usuario usua)
			throws SQLException, UVException, IOException {
				
		obtenerRoles(bean);
		
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
		if (usuArcos == null) {
			throw new UVException("No existe el usuario");
		}
		if (usuArcos.getDocumentoNumero().equals(usua.getDocumentoNumero())) {
			bean.setVista(JSP_USUARIOS);
			throw new UVException(MENSAJE_ERROR_EXCLUSION_USUARIO_LOGUEADO);
		}
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		UsuarioBolsaEmpleo usu = modelo.getUsuarioByCodCuenta(usuArcos.getUid());

		bean.setBusqueda(true);
		bean.setUsuarioArcos(usuArcos);
		bean.setUsuario(usu);
		bean.setRol(usu.getRol());
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO)) != null) {
			UsuarioBolsaEmpleo usuarioForm = this.validateExclusion(request);			
			usuarioForm.setCodNum(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
			usuarioForm.setExcluido(true);
			usuarioForm.setCodCuenta(usu.getCodCuenta());
			usuarioForm.setIdNif(usu.getIdNif());

			modelo.ponerUsuarioComoExcluido(usuarioForm, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
			response.sendRedirect(request.getServletPath());
		}
		bean.setVista(JSP_BUSCAR_USUARIO);
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
	
	private UsuarioBolsaEmpleo getValidatorUsuario(HttpServletRequest request, Usuario usuarioLogeado) throws UVException, SQLException {
		UsuarioBolsaEmpleo u = new UsuarioBolsaEmpleo();
	
		String excluido = request.getParameter(PARAM_EXCLUIDO);
		if (excluido != null) {
			Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
			if (usuArcos.getDocumentoNumero().equals(usuarioLogeado.getDocumentoNumero())) {
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
			if (u.getFechaExclusion() == null) {
				u.setFechaExclusion(BolsaEmpleoUtils.getCurrentDateTime());
			}
			
			u.setExcluidoTipo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO_TIPO)));
			if ("T".equals(u.getExcluidoTipo())) {				
				u.setFechaExclusionInicio(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_INICIO), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
				u.setFechaExclusionFin(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_FIN), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
				
				if (u.getFechaExclusionInicio() == null) {
					throw new UVException("Fecha de inicio de exclusión requerida");
				}
				
				if (u.getFechaExclusionFin() == null) {
					throw new UVException("Fecha de fin de exclusión requerida");
				}
			}
		}
		
		return u;
	}
}
