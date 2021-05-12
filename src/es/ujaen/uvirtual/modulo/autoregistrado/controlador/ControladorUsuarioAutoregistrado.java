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

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.autoregistrado.beans.vista.VistaUsuarioAutoregistrado;
import es.ujaen.uvirtual.modulo.autoregistrado.modelo.ModeloUsuarioAutoregistrado;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase controlador para obtener, cambiar, eliminar y agregar convocatorias.
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * */
@WebServlet(
		name = "operaciones.autoregistrado.usuarioautoresgistrado", 
		description = "Usuario autoregistrado", 
		urlPatterns = { 
				"/pub/es/operaciones/autoregistrado/usuarioautoresgistrado", 
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
	
	public static final String ACCION_WAYF = "wayf";
	public static final String ACCION_VALIDA = "valida";
	public static final String ACCION_MOSTRAR_CREAR = "mostrarcrear";
	public static final String ACCION_CREAR = "crearUsuario";
	public static final String ACCION_OLVIDO = "olvido";
	public static final String ACCION_VALIDA_CODIGO_TEMPORAL = "validaTemporal";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		VistaUsuarioAutoregistrado bean = new VistaUsuarioAutoregistrado();
		
		bean.setVista("/WEB-INF/jsp/vista/operaciones/autoaprovisionado/usuarioExternoWayf.jsp");
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_WAYF;
		}
		try {
			switch (nombreAccion) {
				case ACCION_MOSTRAR_CREAR:
					mostrarCrear(bean);
					break;
				case ACCION_CREAR:
					crearUsuario(request, bean);
					break;
				case ACCION_OLVIDO:
					mandarClaveTemporal(request, bean);
					break;
				case ACCION_VALIDA_CODIGO_TEMPORAL:
					validaClaveTemporal(request, bean);
					break;
				default:
					break;
			}
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
	
//	private void validaUsuario(HttpServletRequest request, VistaUsuarioAutoregistrado bean) throws SQLException {
//		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
//		usuario = usuario.trim();
//		String clave = Formateador.leeParametroString(request.getParameter(PARAM_CLAVE));
//		ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
//		if (modelo.validaClaveUsuario(usuario, clave)) {
//	        HttpSession session = request.getSession(true);
//			session.setAttribute("esValidaLaSesion", usuario);
//			session.setAttribute(UVDatos.ID_USUARIO_SESION, usuario);
//			
//			// 20121009 - define el usuario que se ha logueado para que se registre el valor correcto.
//			UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
//			uvdatos.getAcceso().setUsuario(usuario);
//			uvdatos.setIdentificadorUsuario(usuario);			
//		}
//	}

	private void mostrarCrear(VistaUsuarioAutoregistrado bean) {
		bean.setVista("/WEB-INF/jsp/vista/operaciones/autoaprovisionado/crearUsuario.jsp");
	}

	private void crearUsuario(HttpServletRequest request, VistaUsuarioAutoregistrado bean) throws SQLException, UVException {
		Usuario usuario = new Usuario();
		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
		String correoRepetido = Formateador.leeParametroString(request.getParameter(PARAM_CORREO_REPETIDO));
		if (!correo.equals(correoRepetido)) {
			throw new UVException("los correos deben ser iguales");
		}
		String nombre = Formateador.leeParametroString(request.getParameter(PARAM_NOMBRE));
		String apellido1 = Formateador.leeParametroString(request.getParameter(PARAM_APELLIDO1));
		String apellido2 = Formateador.leeParametroString(request.getParameter(PARAM_APELLIDO2));
		String documento = Formateador.leeParametroString(request.getParameter(PARAM_DOCUMENTO));
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
		bean.setVista("/WEB-INF/jsp/vista/operaciones/autoaprovisionado/validaCodigoTemporal.jsp");
		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
		String ip = request.getRemoteAddr();
		ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
		String idSolicitud = modelo.mandarClaveTemporal(correo, ip);
		bean.setIdSolicitud(idSolicitud);
		bean.setCorreo(correo);
	}

	private void validaClaveTemporal(HttpServletRequest request, VistaUsuarioAutoregistrado bean) throws SQLException, UVException {
		bean.setVista("/WEB-INF/jsp/vista/operaciones/autoaprovisionado/codigoTemporalValidado.jsp");
		String correo = Formateador.leeParametroString(request.getParameter(PARAM_CORREO));
		String temporal = Formateador.leeParametroString(request.getParameter(PARAM_CODIGO_TEMPORAL));
		String idSolicitud = Formateador.leeParametroString(request.getParameter(PARAM_ID_CAMBIO));
		String ip = request.getRemoteAddr();
		ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
		String clave = modelo.verificaPeticionCambio(correo, idSolicitud, temporal, ip);
		bean.setClave(clave);
		bean.setCorreo(correo);
	}
	
}
