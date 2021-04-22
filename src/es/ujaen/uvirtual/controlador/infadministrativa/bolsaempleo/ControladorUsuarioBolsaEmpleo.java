package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticias;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloArea;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloNoticia;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloRol;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoValidator;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
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
	public static final String PARAM_RAZON_EXCLUIDO = "razonexcluido";
	public static final String PARAM_ROLE = "rol";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_BORRADO = "borrar";
	public static final String PARAM_EMAIL = "email";
	
	// acciones
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
	public static final String ACCION_DATATABLE_USUARIOS_BORRADOS = "datatableusuariosborrados";
	public static final String ACCION_DATATABLE_USUARIOS_EXCLUIDOS = "datatableusuariosexcluidos";
	public static final String ACCION_DATATABLE_USUARIOS_EXCLUIDOS_AREA = "datatableusuariosexcluidosarea";
	
	public static final String ACCION_EXCLUIR_USUARIO_AREA = "excluirusuarioarea";
	
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

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/usuarios";
	
	// variables
	public static boolean anonimo = true;
	
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
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR;
		}
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/usuarios.jsp");
		
		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
			case ACCION_FORMULARIO_USUARIO:
				formularioUsuario(request, response, bean);
				break;
			case ACCION_BUSCAR_USUARIO:
				buscarUsuario(request, response, bean);
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
			case ACCION_EXCLUIR_USUARIO:
				excluirUsuario(request, response, bean);
				break;
			case ACCION_USUARIO:
				accionSobreUsuario(request, response, bean);						
				break;
			case ACCION_VOLVER_USUARIO:
				volverUsuario(request, response, bean);
				break;
				
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
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioBolsaEmpleoDatatable(request.getParameterMap());
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatable(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * AJAX para devolver listado de usuarios borrados.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 */
	public void listadoBorrados(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioBorradoBolsaEmpleoDatatable(request.getParameterMap());
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatable(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
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
	public void listadoExcluidos(VistaUsuarioBolsaEmpleo bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioExcluidoBolsaEmpleoDatatable(request.getParameterMap());
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatable(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	
	
	/**
	 * AJAX para devolver listado de areas excluidas de un usuario.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .

	 */
	public void listadoExcluidosArea(VistaUsuarioBolsaEmpleo bean, 
			UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreasExcluidasPorUsuarioDatatable(request.getParameterMap(), codNum);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatableBolsa(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	
	
	private void accionSobreUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException, IOException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_USUARIOS_SELECCIONADOS));
		String nombreAccionUsuario = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION_USUARIO));
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		
		UsuarioBolsaEmpleo usu = modelo.getUsuarioById(codNum);
		
		List<UsuarioBolsaEmpleo> usuarios = modelo.getUsuariosByIds(selected);
		List<Area> areas = modeloArea.getAreasByIds(selected);

		switch (nombreAccionUsuario) {
		case ACCION_ELIMINAR_USUARIO:
			modelo.ponerUsuarioComoBorrado(usuarios);							
			break;
		case ACCION_RECUPERAR_USUARIO:
			modelo.ponerUsuarioComoNoBorrado(usuarios);
			break;
		case ACCION_INCLUIR_USUARIO:
			modelo.ponerUsuarioComoNoExcluido(usuarios);
			break;
		case ACCION_EXCLUIR_USUARIO_AREA:
			modelo.excluirUsuarioArea(usu, areas);
			break;
			
		default:
			bean.getMensajesDeError().add(MENSAJE_ERROR_ACCION_USUARIO_NO_VALIDA);
			return;
		}
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE);
	}
	
	
	
	
	/** Busca un usuario.
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void buscarUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		obtenerRoles(bean);
		try {
			UsuarioBolsaEmpleo usu = modelo.listaUsuario(Formateador.leeParametroString(request.getParameter(PARAM_NOMBRE)));
			Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE));
			
			bean.setBusqueda(true);
			
			bean.setUsuarioArcos(usuArcos);
			bean.setUsuario(usu);
			bean.setRol(usu.getRol());
			
			bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/formUsuario.jsp");
		} catch (UVException e) {
			Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE));
			if (usuArcos != null) {
				bean.setBusqueda(false);
				bean.setUsuarioArcos(usuArcos);
				bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/formUsuario.jsp");
			} else {
				bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/buscarUsuario.jsp");
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
	public static void agregarUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/usuarios.jsp");
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		Boolean listadist = request.getParameter(PARAM_LISTA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LISTA)).equals("true");
		Boolean excluido = request.getParameter(PARAM_EXCLUIDO) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO)).equals("true");
					
		String razonexcluido = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO));
			
		ModeloRol modeloRol = ModeloRol.obtenerInstancia();
		Rol role = modeloRol.getRoleById(Formateador.leeParametroInteger(request.getParameter(PARAM_ROLE)));
			
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
			
		String usu = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ID));
			
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_ID));
			
		UsuarioBolsaEmpleo usuarioFinal = new UsuarioBolsaEmpleo(usuArcos.getCodigoPersonaArcos(), usu, role, listadist, excluido, razonexcluido, date);
			
		modelo.insertaUsuario(usuarioFinal);
		
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
	private void editarUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException, IOException {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/formUsuario.jsp");
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		obtenerRoles(bean);
		
		UsuarioBolsaEmpleo usu = modelo.listaUsuario(Formateador.leeParametroString(request.getParameter(PARAM_NOMBRE_USUARIO)));
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
		
		bean.setBusqueda(false);
		
		bean.setUsuarioArcos(usuArcos);
		bean.setUsuario(usu);
		bean.setRol(usu.getRol());
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ROLE)) != null) {
			BolsaEmpleoValidator validator = this.getValidatorUsuarios(request); 							
			
			if (!validator.isValid()) {
				for (String param : validator.getErrors().keySet()) {
					for (String paramError : validator.getErrors().get(param)) {
						bean.getMensajesDeError().add(paramError);
					}
				}
			} else {
				Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
				Boolean listadist = request.getParameter(PARAM_LISTA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LISTA)).equals("true");
				
				Boolean excluido = 
						request.getParameter(PARAM_EXCLUIDO) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO)).equals("true");
						
				String razonexcluido = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO));
				
				ModeloRol modeloRol = ModeloRol.obtenerInstancia();
				Rol role = modeloRol.getRoleById(Formateador.leeParametroInteger(request.getParameter(PARAM_ROLE)));
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				Date date = new Date(System.currentTimeMillis());
				
				String usua = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE_USUARIO));
				
				UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(codNum, role, listadist, excluido, razonexcluido, date);
				
				modelo.actualizaUsuario(usuario);
				HttpSession session = request.getSession(false);
				session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR);
				response.sendRedirect(request.getServletPath());	
			}
		}
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
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/buscarUsuario.jsp");
		
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		obtenerRoles(bean);
		
		UsuarioBolsaEmpleo usu = modelo.listaUsuario(Formateador.leeParametroString(request.getParameter(PARAM_NOMBRE_USUARIO)));
		Usuario usuArcos = CrearUsuario.usuario(request.getParameter(PARAM_NOMBRE_USUARIO));
		
		bean.setBusqueda(true);
		
		bean.setUsuarioArcos(usuArcos);
		bean.setUsuario(usu);
		bean.setRol(usu.getRol());
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO)) != null) {
			Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
			Boolean excluido = true;
					
			String razonexcluido = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO));
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			
			UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(codNum, excluido, razonexcluido, date);
			
			modelo.ponerUsuarioComoExcluido(usuario);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** dirige al formulario de busqueda de un usuario.
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void formularioUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/buscarUsuario.jsp");
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
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void volverUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException, IOException {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/usuarios.jsp");
	}
	
	
	/** Valida el formulario de Usuarios.
	 * @param request .
	 * @return validator
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws IOException .
	 */
	private BolsaEmpleoValidator getValidatorUsuarios(HttpServletRequest request) throws UVException {
		BolsaEmpleoValidator validator = new BolsaEmpleoValidator(request);
		
		if (request.getParameter(PARAM_EXCLUIDO) != null) {
			validator.addParamString(PARAM_RAZON_EXCLUIDO);
			validator.addRule(PARAM_RAZON_EXCLUIDO, "required", MENSAJE_ERROR_RAZON_EXCLUSION_VACIO);
			validator.addRule(PARAM_RAZON_EXCLUIDO, "noBlank", MENSAJE_ERROR_RAZON_EXCLUSION_VACIO);
			validator.addRule(PARAM_RAZON_EXCLUIDO, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH, 
					String.format(MENSAJE_ERROR_RAZON_EXCLUSION_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH));
		}
		
		validator.addParamString(PARAM_ROLE);
		validator.addRule(PARAM_ROLE, "required", MENSAJE_ERROR_ROL_VACIO);
		validator.addRule(PARAM_ROLE, "noBlank", MENSAJE_ERROR_ROL_VACIO);
		validator.addRule(PARAM_ROLE, "select", MENSAJE_ERROR_ROL_NEGATIVO);
		
		return validator;
	}
	
}
