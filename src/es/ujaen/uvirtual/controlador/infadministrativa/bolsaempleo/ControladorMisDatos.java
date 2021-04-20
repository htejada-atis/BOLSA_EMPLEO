package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloRol;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los datos de usuarios de UVIRTUAL.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.misdatos", 
		description = "Gestión de las áreas a baremar", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/misdatos", 
				"/srv/en/informacionadministrativa/bolsaempleo/misdatos",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/misdatos",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/misdatos"
		})
public class ControladorMisDatos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorUsuarioBolsaEmpleo.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_DOCUMENTO = "documento";
	
	public static final String PARAM_PRIMER_APELLIDO = "primer_apellido";
	public static final String PARAM_SEGUNDO_APELLIDO = "segundo_apellido";
	public static final String PARAM_DIRECCION = "direccion";
	public static final String PARAM_CODIGO_POSTAL = "codigo_postal";
	public static final String PARAM_LOCALIDAD = "localidad";
	public static final String PARAM_PROVINCIA = "provincia";
	public static final String PARAM_MOVIL = "movil";
	public static final String PARAM_TELEFONO = "telefono";
	public static final String PARAM_NACIONALIDAD = "nacionalidad";
	public static final String PARAM_SEXO = "sexo";

	// acciones
	public static final String ACCION_ENVIAR_MISDATOS = "enviarmisdatos";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_ENVIAR = "datos personales actualizados correctamente";

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/misdatos";
	
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
		ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		try {
			anonimo = !modelo.checkUser(datos);
		} catch (SQLException | UVException e) {
			bean.getMensajesDeError().add(e.getMessage());
		}
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = PARAM_ACCION;
		}
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misdatos/index.jsp");
		
		Usuario usuArcos = CrearUsuario.usuario(usuario.getUid());
		
		bean.setUsuarioArcos(usuArcos);
	
		UsuarioBolsaEmpleo usu;
		try {
			usu = modelo.listaUsuario(usuario.getUid());
			bean.setUsuario(usu);
		} catch (SQLException | UVException exy) {
			exy.printStackTrace();
		}	
		
		try {
			switch (nombreAccion) {
			case ACCION_ENVIAR_MISDATOS:
				enviarMisDatos(request, response, bean, usuario);
				break;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
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
	
	/** Envia el formulario con los datos del usuario logueado.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @param usuario .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws IOException .
	 */
	public void enviarMisDatos(HttpServletRequest request, HttpServletResponse response, 
			VistaUsuarioBolsaEmpleo bean, Usuario usuario) throws SQLException, UVException, IOException {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misdatos/index.jsp");
		
		ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
		UsuarioBolsaEmpleo usu = modelo.listaUsuario(usuario.getUid());
		
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		String email = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EMAIL));
		String direccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DIRECCION));
		String codigopostal = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CODIGO_POSTAL));
		String localidad = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LOCALIDAD));
		String provincia = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PROVINCIA));
		String movil = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MOVIL));
		String telefono = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TELEFONO));
		String nacionalidad = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NACIONALIDAD));
		String sexo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_SEXO));
			
		UsuarioBolsaEmpleo usuarioFinal = new UsuarioBolsaEmpleo(codNum, email, direccion, codigopostal, localidad, provincia, movil, telefono, nacionalidad, sexo);
			
		modelo.actualizaUsuarioMisDatos(usuarioFinal);
		
		
    	bean.getMensajesDeExito().add(MENSAJE_EXITO_ENVIAR);
	}
	
}
