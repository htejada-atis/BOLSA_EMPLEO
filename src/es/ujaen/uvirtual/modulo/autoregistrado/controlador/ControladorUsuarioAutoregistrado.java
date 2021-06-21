package es.ujaen.uvirtual.modulo.autoregistrado.controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.autoregistrado.beans.vista.VistaUsuarioAutoregistrado;
import es.ujaen.uvirtual.modulo.autoregistrado.modelo.ModeloUsuarioAutoregistrado;
import es.ujaen.uvirtual.utilidades.AyudaURL;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.RecaptchaUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase controlador para obtener, cambiar, eliminar y agregar convocatorias.
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * */
@WebServlet(
		name = "operaciones.autoregistrado.usuarioautoresgistrado", 
		description = "Usuario autoregistrado", 
		urlPatterns = { 
				"/pub/es/operaciones/autoregistrado/usuarioautoresgistrado", 
				"/pub/es/operaciones/autoregistrado/usuarioautoresgistrado/*", 
				"/pub/en/operaciones/autoregistrado/usuarioautoresgistrado"
		})
public class ControladorUsuarioAutoregistrado extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorUsuarioAutoregistrado.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_CORREO_REPETIDO = "emailRepetido";
	public static final String PARAM_CORREO = "email";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_APELLIDO1 = "apellido1";
	public static final String PARAM_APELLIDO2 = "apellido2";
	public static final String PARAM_DOCUMENTO = "documento";
	public static final String PARAM_TIPO_DOCUMENTO = "tipodocumento";
	public static final String PARAM_FECHA_NACIMIENTO = "fechaNacimiento";
	public static final String PARAM_SEXO = "sexo";
	public static final String PARAM_CODIGO_TEMPORAL = "codigoTemporal";
	public static final String PARAM_ID_CAMBIO = "idCambio";
	public static final String PARAM_CLAVE = "cla";
	public static final String PARAM_ID_MODULO = "idmodulo";
	
	public static final String ACCION_WAYF = "wayf";
	public static final String ACCION_VALIDA = "valida";
	public static final String ACCION_MOSTRAR_CREAR = "mostrarcrear";
	public static final String ACCION_CREAR = "crearUsuario";
	public static final String ACCION_OLVIDO = "olvido";
	public static final String ACCION_VALIDA_CODIGO_TEMPORAL = "validaTemporal";
	public static final String ACCION_MUESTRA_LOGIN = "muestraLogin";
	public static final String ACCION_MUESTRA_OLVIDO = "muestraOlvido";
	
	private static final String JSP_VALIDA_CODIGO_TEMPORAL = "/WEB-INF/jsp/vista/operaciones/autoaprovisionado/validaCodigoTemporal.jsp";
	private static final String JSP_LOGIN = "/WEB-INF/jsp/vista/operaciones/autoaprovisionado/login.jsp";

	private void compruebaNoRobot(HttpServletRequest request) throws UVException {
		boolean recaptchaActivo = false;
		try {
			recaptchaActivo = ConfiguracionGlobal.getParametroLogico("autoaprovisionado.recaptcha.activado");
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			recaptchaActivo = false;
		}
		if (recaptchaActivo) {
			try {
				RecaptchaUtils.validaRechaptcha(request);
			} catch (UVException e) {
				throw e;
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, e.toString());
			}
		}
	}
	
	private void realizaAccion(HttpServletRequest request, HttpServletResponse response, VistaUsuarioAutoregistrado bean,
		                       String nombreAccion) throws SQLException, IOException, UVException {
		bean.setVista(JSP_LOGIN);
		String modulo = obtenerIdModulo(request); 
		bean.setIdModulo(modulo);
		bean.setDescripcionModulo(obtenerDescripcionModulo(modulo));
		String urlModulo = obtenerUrlModulo(request, modulo);
		bean.setPaginaRedireccion(urlModulo);
		bean.setMostrarCaptcha(true);
		try {
			bean.setCaptchaPublica(ConfiguracionGlobal.getParametroCadena("administracion.recaptcha.claveDelSitio"));
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
		}

		switch (nombreAccion) {
			case ACCION_VALIDA:
				compruebaNoRobot(request);
				validaUsuario(request, response, bean);
				break;
			case ACCION_MOSTRAR_CREAR:
				mostrarCrear(bean);
				break;
			case ACCION_CREAR:
				compruebaNoRobot(request);
				crearUsuario(request, bean);
				break;
			case ACCION_OLVIDO:
				compruebaNoRobot(request);
				mandarClaveTemporal(request, bean);
				break;
			case ACCION_VALIDA_CODIGO_TEMPORAL:
				validaClaveTemporal(request, bean);
				break;
			case ACCION_MUESTRA_LOGIN:
				muestraLogin(request, response, bean);
				break;
			case ACCION_MUESTRA_OLVIDO:
				muestraOlvido(bean);
				break;
			default:
				muestraLogin(request, response, bean);
				break;
		}
	}
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		VistaUsuarioAutoregistrado bean = new VistaUsuarioAutoregistrado();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_MUESTRA_LOGIN;
		}
		String idCambio = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ID_CAMBIO));
		if (idCambio != null) {
			nombreAccion = ACCION_VALIDA_CODIGO_TEMPORAL;
		}
		
		try {
			realizaAccion(request, response, bean, nombreAccion);
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMensajeUsuario());
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
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}

	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String obtenerIdModulo(HttpServletRequest request) {
		String[] parametros = AyudaURL.obtenerParametros(request.getRequestURI());
		String modulo = "defecto";
		if (parametros != null && parametros.length >= 1) {
			modulo = Formateador.leeParametroString(parametros[0]);
		}
		return modulo;
	}
	
	private String obtenerDescripcionModulo(String idModulo) {
		String salida = "Universidad Virtual";
		if ("bep".equals(idModulo)) {
			salida = "Bolsa de empleo";
		}
		return salida;
	}
	
	private String obtenerUrlModulo(HttpServletRequest request, String idModulo) {
		String salida = null;
		String servidor = request.getServerName();
		int puerto = request.getServerPort();
		final int puertoHttps = 443;
		if (puerto != puertoHttps) {
			servidor = servidor + ":" + puerto;
		}
		
		salida = request.getScheme() + "://" + servidor + "/srv/es/index";
		if ("bep".equals(idModulo)) {
			salida = request.getScheme() + "://" + servidor + "/srv/es/informacionadministrativa/bolsaempleo";
		}
		return salida;
	}

	private void muestraLogin(HttpServletRequest request, HttpServletResponse response, VistaUsuarioAutoregistrado bean) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		String uid = datos.getIdentificadorUsuario();
		if (uid == null) {
			uid = (String) request.getSession().getAttribute(UVDatos.ID_USUARIO_SESION);
		}
		String modulo = obtenerIdModulo(request); 
		String urlModulo = obtenerUrlModulo(request, modulo);
		bean.setPaginaRedireccion(urlModulo);
		if (uid != null) {
			response.sendRedirect(bean.getPaginaRedireccion());
		}
	}

	private void muestraOlvido(VistaUsuarioAutoregistrado bean) {
		bean.setVista("/WEB-INF/jsp/vista/operaciones/autoaprovisionado/olvido.jsp");
	}
	
	private void meterUsuarioEnSesion(HttpServletRequest request, String correo) {
        HttpSession session = request.getSession(true);
		session.setAttribute("esValidaLaSesion", correo);
		session.setAttribute(UVDatos.ID_USUARIO_SESION, correo);
		UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		uvdatos.getAcceso().setUsuario(correo);
		uvdatos.setIdentificadorUsuario(correo);
		Usuario usuario = CrearUsuario.usuario(correo);
		uvdatos.setUsuario(usuario);
	}
	
	private void validaUsuario(HttpServletRequest request, HttpServletResponse response, VistaUsuarioAutoregistrado bean) throws SQLException, IOException {
		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
		correo = correo.trim();
		correo = correo.toLowerCase();
		String clave = Formateador.leeParametroString(request.getParameter(PARAM_CLAVE));
		ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
		String ip = request.getRemoteAddr();
		if (modelo.validaClaveUsuario(correo, clave, ip)) {
			meterUsuarioEnSesion(request, correo);
			String idModulo = obtenerIdModulo(request);
			String paginaRedirect = obtenerUrlModulo(request, idModulo);
			response.sendRedirect(paginaRedirect);
		} else {
			bean.getMensajesDeError().add("Usuario/clave no válidos");
		}
	}

	private void mostrarCrear(VistaUsuarioAutoregistrado bean) {
		bean.setVista("/WEB-INF/jsp/vista/operaciones/autoaprovisionado/crearUsuario.jsp");
	}

	private void crearUsuario(HttpServletRequest request, VistaUsuarioAutoregistrado bean) throws SQLException, UVException {
		Usuario usuario = new Usuario();
		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
		correo = correo.trim();
		correo = correo.toLowerCase();
		String correoRepetido = Formateador.leeParametroString(request.getParameter(PARAM_CORREO_REPETIDO));
		correoRepetido = correoRepetido.toLowerCase();
		if (!correo.equals(correoRepetido)) {
			throw new UVException("los correos deben ser iguales");
		}
		String nombre = Formateador.leeParametroString(request.getParameter(PARAM_NOMBRE));
		String apellido1 = Formateador.leeParametroString(request.getParameter(PARAM_APELLIDO1));
		String apellido2 = Formateador.leeParametroString(request.getParameter(PARAM_APELLIDO2));
		String documento = Formateador.leeParametroString(request.getParameter(PARAM_DOCUMENTO));
		documento = documento.toUpperCase();
		String tipoDocumento = Formateador.leeParametroString(request.getParameter(PARAM_TIPO_DOCUMENTO));
		String sexo = Formateador.leeParametroString(request.getParameter(PARAM_SEXO));
		usuario.setEmailCuentaPersona(correo);
		usuario.setNombre(nombre);
		usuario.setApellido1(apellido1);
		usuario.setApellido2(apellido2);
		usuario.setDocumentoNumero(documento);
		usuario.setDocumentoTipo(tipoDocumento);
		usuario.setSexo(sexo);
		ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
		modelo.insertaUsuarioAutoregistrado(usuario);
		mandarClaveTemporal(request, bean);
	}
	
	private void mandarClaveTemporal(HttpServletRequest request, VistaUsuarioAutoregistrado bean) throws SQLException, UVException {
		bean.setVista(JSP_VALIDA_CODIGO_TEMPORAL);
		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
		correo = correo.toLowerCase();
		String ip = request.getRemoteAddr();
		ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
		String idSolicitud;
		bean.setCorreo(correo);
		try {
			idSolicitud = modelo.mandarClaveTemporal(correo, ip);
			bean.setIdSolicitud(idSolicitud);
		} catch (SQLException | UVException e) {
			bean.setVista(JSP_LOGIN);
			throw e;
		}
	}

	private void validaClaveTemporal(HttpServletRequest request, VistaUsuarioAutoregistrado bean) throws SQLException, UVException {
		bean.setVista("/WEB-INF/jsp/vista/operaciones/autoaprovisionado/codigoTemporalValidado.jsp");
		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
		String temporal = Formateador.leeParametroString(request.getParameter(PARAM_CODIGO_TEMPORAL));
		String idSolicitud = Formateador.leeParametroString(request.getParameter(PARAM_ID_CAMBIO));
		String modulo = obtenerIdModulo(request); 
		String urlModulo = obtenerUrlModulo(request, modulo);
		bean.setPaginaRedireccion(urlModulo);
		bean.setIdModulo(modulo);
		String ip = request.getRemoteAddr();
		ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
		bean.setCorreo(correo);
		bean.setIdSolicitud(idSolicitud);
		String clave;
		if (temporal != null && !temporal.isBlank()) {
			try {
				clave = modelo.verificaPeticionCambio(correo, idSolicitud, temporal, ip);
				bean.setClave(clave);
				meterUsuarioEnSesion(request, correo);
			} catch (SQLException | UVException e) {
				bean.setVista(JSP_VALIDA_CODIGO_TEMPORAL);
				throw e;
			}
		} else {
			bean.setVista(JSP_VALIDA_CODIGO_TEMPORAL);
		}
	}
	
}
