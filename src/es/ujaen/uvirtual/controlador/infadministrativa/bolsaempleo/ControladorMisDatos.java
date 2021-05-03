package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloRol;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoValidator;
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
	public static final String PARAM_LISTA = "listadist";
	
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
	public static final String PARAM_DARSE_BAJA = "baja";
	public static final String PARAM_RAZON_BORRADO = "razonborrado";
	
	// acciones
	public static final String ACCION_ENVIAR_MISDATOS = "enviarmisdatos";
	public static final String ACCION_BAJA_USUARIO = "bajausuario";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_ENVIAR = "Datos personales actualizados correctamente";
	public static final String MENSAJE_EXITO_DARSE_BAJA = "Usuario dado de baja correctamente";
	
	public static final String MENSAJE_ERROR_PRIMER_APELLIDO_VACIO = "El primer apellido no puede estar vacio";
	public static final String MENSAJE_ERROR_NACIONALIDAD_VACIO = "La nacionalidad no puede estar vacia";
	public static final String MENSAJE_ERROR_NOMBRE_VACIO = "El nombre no puede estar vacio";
	
	public static final String MENSAJE_ERROR_RAZON_BORRADO_VACIO = "La razón de borrado no puede estar vacía";
	
	public static final String MENSAJE_ERROR_TELEFONO_STRING = "El telefono debe ser un número";
	public static final String MENSAJE_ERROR_MOVIL_STRING = "El móvil debe ser un número";
	public static final String MENSAJE_ERROR_CODIGO_POSTAL_STRING = "El código postal debe ser un número";
	
	public static final String MENSAJE_ERROR_NOMBRE_LARGO = "El nombre no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_PRIMER_APELLIDO_LARGO = "El primer apellido no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_SEGUNDO_APELLIDO_LARGO = "El segundo apellido no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_DIRECCION_LARGO = "La dirección no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_CODIGO_POSTAL_LARGO = "El código postal no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_LOCALIDAD_LARGO = "La localidad no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_PROVINCIA_LARGO = "La provincia no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_MOVIL_LARGO = "El movil no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_TELEFONO_LARGO = "El telefono no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_NACIONALIDAD_LARGO = "La nacionalidad no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_EMAIL_LARGO = "El email no puede contener mas de %d caracteres";
	
	
	

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
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
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
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
			case ACCION_ENVIAR_MISDATOS:
				enviarMisDatos(request, response, bean, usuario);
				break;
			case ACCION_BAJA_USUARIO:
				bajaUsuario(request, response, bean, usuario);
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
		
		BolsaEmpleoValidator validator = this.getValidatorMisDatos(request); 							
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		UsuarioBolsaEmpleo usu = modelo.listaUsuario(usuario.getUid());
		
		if (!validator.isValid()) {
			for (String param : validator.getErrors().keySet()) {
				for (String paramError : validator.getErrors().get(param)) {
					bean.getMensajesDeError().add(paramError);
				}
			}
		} else {
		
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE));
			String primerapellido = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PRIMER_APELLIDO));
			String segundoapellido = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_SEGUNDO_APELLIDO));
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
			Boolean listadist = request.getParameter(PARAM_LISTA) != null && EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LISTA)).equals("true");
			
			UsuarioBolsaEmpleo usuarioFinal = new UsuarioBolsaEmpleo(codNum, nombre, primerapellido, 
					segundoapellido, email, direccion, codigopostal, localidad, provincia, movil, telefono, nacionalidad, sexo, listadist);
			
			UsuarioBolsaEmpleo usua = modelo.getUsuarioById(codNum);
			usuarioFinal.setCodPersona(usua.getCodPersona());
			
			modelo.actualizaUsuarioMisDatos(usuarioFinal);

			UsuarioBolsaEmpleo usuaCont = modelo.getUsuarioById(usuarioFinal.getCodNum());
			Usuario usuArcos = CrearUsuario.usuario(usuaCont.getCodCuenta());
			bean.setUsuarioArcos(usuArcos);
			bean.setUsuario(usuaCont);
			
			bean.getMensajesDeExito().add(MENSAJE_EXITO_ENVIAR);
		}
	}
	
	
	/** Da de baja al usuario .
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @param usuario .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws IOException .
	 */
	public void bajaUsuario(HttpServletRequest request, HttpServletResponse response, 
			VistaUsuarioBolsaEmpleo bean, Usuario usuario) throws SQLException, UVException, IOException {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misdatos/formBaja.jsp");
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_BORRADO)) != null) {
			BolsaEmpleoValidator validator = this.getValidatorBajaUsuario(request); 							
			
			if (!validator.isValid()) {
				for (String param : validator.getErrors().keySet()) {
					for (String paramError : validator.getErrors().get(param)) {
						bean.getMensajesDeError().add(paramError);
					}
				}
			} else {
				bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/inicio/indice.jsp");
				ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
				
				Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
				String razonborrado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_BORRADO));
				UsuarioBolsaEmpleo usu = modelo.getUsuarioById(codNum);
				
				usu.setRazonBorrado(razonborrado);
				
				modelo.cambiarFlagBorradoUsuarioRazon(usu);

				bean.getMensajesDeExito().add(MENSAJE_EXITO_DARSE_BAJA);
			}
		}
	}
	
	
	
	/** Valida el formulario de Mis Datos.
	 * @param request .
	 * @return validator .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws IOException .
	 */
	private BolsaEmpleoValidator getValidatorMisDatos(HttpServletRequest request) throws UVException {
		BolsaEmpleoValidator validator = new BolsaEmpleoValidator(request);
		validator.addParamString(PARAM_NOMBRE);
		validator.addRule(PARAM_NOMBRE, "required", MENSAJE_ERROR_NOMBRE_VACIO);
		validator.addRule(PARAM_NOMBRE, "noBlank", MENSAJE_ERROR_NOMBRE_VACIO);
		validator.addRule(PARAM_NOMBRE, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_NOMBRE_MAXLENGTH, 
				String.format(MENSAJE_ERROR_NOMBRE_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_NOMBRE_MAXLENGTH));
		
		validator.addParamString(PARAM_PRIMER_APELLIDO);
		validator.addRule(PARAM_PRIMER_APELLIDO, "required", MENSAJE_ERROR_PRIMER_APELLIDO_VACIO);
		validator.addRule(PARAM_PRIMER_APELLIDO, "noBlank", MENSAJE_ERROR_PRIMER_APELLIDO_VACIO);
		validator.addRule(PARAM_PRIMER_APELLIDO, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_NOMBRE_MAXLENGTH, 
				String.format(MENSAJE_ERROR_PRIMER_APELLIDO_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_NOMBRE_MAXLENGTH));
		
		validator.addParamString(PARAM_SEGUNDO_APELLIDO);
		validator.addRule(PARAM_SEGUNDO_APELLIDO, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_SEGUNDO_APELLIDO_MAXLENGTH, 
				String.format(MENSAJE_ERROR_SEGUNDO_APELLIDO_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_SEGUNDO_APELLIDO_MAXLENGTH));

		validator.addParamString(PARAM_DIRECCION);
		validator.addRule(PARAM_DIRECCION, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_DIRECCION_MAXLENGTH, 
				String.format(MENSAJE_ERROR_DIRECCION_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_DIRECCION_MAXLENGTH));
		
		validator.addParamString(PARAM_CODIGO_POSTAL);
		validator.addRule(PARAM_CODIGO_POSTAL, "number:", MENSAJE_ERROR_CODIGO_POSTAL_STRING);
		validator.addRule(PARAM_CODIGO_POSTAL, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_CODIGO_POSTAL_MAXLENGTH, 
				String.format(MENSAJE_ERROR_CODIGO_POSTAL_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_CODIGO_POSTAL_MAXLENGTH));
		
		validator.addParamString(PARAM_LOCALIDAD);
		validator.addRule(PARAM_LOCALIDAD, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_LOCALIDAD_MAXLENGTH, 
				String.format(MENSAJE_ERROR_LOCALIDAD_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_LOCALIDAD_MAXLENGTH));
		
		validator.addParamString(PARAM_PROVINCIA);
		validator.addRule(PARAM_PROVINCIA, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_PROVINCIA_MAXLENGTH, 
				String.format(MENSAJE_ERROR_PROVINCIA_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_PROVINCIA_MAXLENGTH));
		
		validator.addParamString(PARAM_MOVIL);
		validator.addRule(PARAM_MOVIL, "number:", MENSAJE_ERROR_MOVIL_STRING);
		validator.addRule(PARAM_MOVIL, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_MOVIL_MAXLENGTH, 
				String.format(MENSAJE_ERROR_MOVIL_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_MOVIL_MAXLENGTH));
		
		validator.addParamString(PARAM_TELEFONO);
		validator.addRule(PARAM_TELEFONO, "number:", MENSAJE_ERROR_TELEFONO_STRING);
		validator.addRule(PARAM_TELEFONO, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_TELEFONO_MAXLENGTH, 
				String.format(MENSAJE_ERROR_TELEFONO_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_TELEFONO_MAXLENGTH));
		
		validator.addParamString(PARAM_NACIONALIDAD);
		validator.addRule(PARAM_NACIONALIDAD, "required", MENSAJE_ERROR_NACIONALIDAD_VACIO);
		validator.addRule(PARAM_NACIONALIDAD, "noBlank", MENSAJE_ERROR_NACIONALIDAD_VACIO);
		validator.addRule(PARAM_NACIONALIDAD, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_NACIONALIDAD_MAXLENGTH, 
				String.format(MENSAJE_ERROR_NACIONALIDAD_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_NACIONALIDAD_MAXLENGTH));
		
		validator.addParamString(PARAM_EMAIL);
		validator.addRule(PARAM_EMAIL, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_EMAIL_MAXLENGTH, 
				String.format(MENSAJE_ERROR_EMAIL_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_EMAIL_MAXLENGTH));
		
		return validator;
	}
	
	
	
	/** Valida el formulario de Baja de usuario.
	 * @param request .
	 * @return validator .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws IOException .
	 */
	private BolsaEmpleoValidator getValidatorBajaUsuario(HttpServletRequest request) throws UVException {
		BolsaEmpleoValidator validator = new BolsaEmpleoValidator(request);
		validator.addParamString(PARAM_RAZON_BORRADO);
		validator.addRule(PARAM_RAZON_BORRADO, "required", MENSAJE_ERROR_RAZON_BORRADO_VACIO);
		validator.addRule(PARAM_RAZON_BORRADO, "noBlank", MENSAJE_ERROR_RAZON_BORRADO_VACIO);
		validator.addRule(PARAM_RAZON_BORRADO, "max:" + ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_BORRADO_MAXLENGTH, 
				String.format(MENSAJE_ERROR_NOMBRE_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_BORRADO_MAXLENGTH));
		
		return validator;
	}
	
}
