package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;
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
	public static final String ACCION_INDEX = "index";
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
	public static final String JSP_INDEX = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misdatos/index.jsp";

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
					index(bean, datos);
					break;
				case ACCION_ENVIAR_MISDATOS:
					enviarMisDatos(request, bean);
					break;
				case ACCION_BAJA_USUARIO:
					bajaUsuario(request, bean);
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
	
	private void init(VistaUsuarioBolsaEmpleo bean, UVDatos datos) throws SQLException, UVException {
		this.index(bean, datos);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
	}
    
	private void errorFatal(VistaUsuarioBolsaEmpleo bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
    }
    
    /** Redireccion de do post.
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}   
	
	private void index(VistaUsuarioBolsaEmpleo bean, UVDatos datos) throws SQLException, UVException {
		Usuario usuario = datos.getUsuario();
		Usuario usuArcos = CrearUsuario.usuario(usuario.getUid());
		bean.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().listaUsuario(usuario.getDocumentoNumero()));
		bean.setUsuarioArcos(usuArcos);
		bean.setVista(JSP_INDEX);
	}
    
    /** Envia el formulario con los datos del usuario logueado.
     * @param bean .
     * @param request .
     * @throws UVException .
     * @throws SQLException .
     */
	public void enviarMisDatos(HttpServletRequest request, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException {
		bean.setVista(JSP_INDEX);
                                        
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		UsuarioBolsaEmpleo usuario = modelo.getUsuarioById(codNum);
		
		UsuarioBolsaEmpleo usuarioForm = this.validarDatosUsuario(request);
		usuarioForm.setCodNum(usuario.getCodNum());
		    
		modelo.actualizaUsuarioMisDatos(usuarioForm);
		
		UsuarioBolsaEmpleo usuaCont = modelo.getUsuarioById(codNum);
		Usuario usuArcos = CrearUsuario.usuario(usuaCont.getCodCuenta());
		bean.setUsuarioArcos(usuArcos);
		bean.setUsuario(usuaCont);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ENVIAR);
    }
    
	private void bajaUsuario(HttpServletRequest request, VistaUsuarioBolsaEmpleo bean) throws SQLException, UVException {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misdatos/formBaja.jsp");
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_BORRADO)) != null) {
			String razonBorrado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_BORRADO));
			if (razonBorrado == null || razonBorrado.isBlank()) {
				throw new UVException(MENSAJE_ERROR_RAZON_BORRADO_VACIO);
			}
			if (razonBorrado.length() > ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_BORRADO_MAXLENGTH) {
				throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_BORRADO_MAXLENGTH));
			}
			bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/inicio/indice.jsp");
			ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
			
			Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
			UsuarioBolsaEmpleo usu = modelo.getUsuarioById(codNum);
			
			usu.setRazonBorrado(razonBorrado);
			
			modelo.cambiarFlagBorradoUsuarioRazon(usu);
			
			bean.getMensajesDeExito().add(MENSAJE_EXITO_DARSE_BAJA);
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
	private UsuarioBolsaEmpleo validarDatosUsuario(HttpServletRequest request) throws UVException {
		UsuarioBolsaEmpleo u = this.validarDatosUsuarioDireccion(request, new UsuarioBolsaEmpleo());
		        
		u.setNacionalidad(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NACIONALIDAD)));
		if (u.getNacionalidad() == null || u.getNacionalidad().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NACIONALIDAD_VACIO);
		}
		if (u.getNacionalidad().length() > ModeloUsuarioBolsaEmpleo.COLUMN_NACIONALIDAD_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NACIONALIDAD_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_NACIONALIDAD_MAXLENGTH));
		}
	    
		u.setListaDist("true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LISTA))));
	
		return u;
	}
    
	private UsuarioBolsaEmpleo validarDatosUsuarioDireccion(HttpServletRequest request, UsuarioBolsaEmpleo u)
			throws UVException {
		u.setDireccion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DIRECCION)));
		if (u.getDireccion() != null
				&& u.getDireccion().length() > ModeloUsuarioBolsaEmpleo.COLUMN_DIRECCION_MAXLENGTH) {
			throw new UVException(
					String.format(MENSAJE_ERROR_DIRECCION_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_DIRECCION_MAXLENGTH));
		}

		u.setCodigoPostal(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CODIGO_POSTAL)));
		if (u.getCodigoPostal() != null) {
			String regex = "[0-9]+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(u.getCodigoPostal());

			if (!m.matches()) {
				throw new UVException(MENSAJE_ERROR_CODIGO_POSTAL_STRING);
			}
			if (u.getCodigoPostal().length() > ModeloUsuarioBolsaEmpleo.COLUMN_CODIGO_POSTAL_MAXLENGTH) {
				throw new UVException(String.format(MENSAJE_ERROR_CODIGO_POSTAL_LARGO,
						ModeloUsuarioBolsaEmpleo.COLUMN_CODIGO_POSTAL_MAXLENGTH));
			}
		}

		u.setLocalidad(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LOCALIDAD)));
		if (u.getLocalidad() != null
				&& u.getLocalidad().length() > ModeloUsuarioBolsaEmpleo.COLUMN_LOCALIDAD_MAXLENGTH) {
			throw new UVException(
					String.format(MENSAJE_ERROR_LOCALIDAD_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_LOCALIDAD_MAXLENGTH));
		}

		u.setProvincia(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_PROVINCIA)));
		if (u.getProvincia() != null
				&& u.getProvincia().length() > ModeloUsuarioBolsaEmpleo.COLUMN_PROVINCIA_MAXLENGTH) {
			throw new UVException(
					String.format(MENSAJE_ERROR_PROVINCIA_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_PROVINCIA_MAXLENGTH));
		}

		return u;
	}
}
