package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaParametrosConfiguracion;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los parametros de configuracion de la bolsa de empleo de UVIRTUAL.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.parametros", 
		description = "Gestión de parametros de configuración de la bolsa de empleo", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/parametros", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/parametros",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/parametros",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/parametros"
		})
@MultipartConfig
public class ControladorParametrosConfiguracion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorParametrosConfiguracion.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_EDITAR_PARAMETROS = "editarparametros";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_VALOR = "valor";
	public static final String PARAM_DESCRIPCION = "descripcion";
	public static final String PARAM_ID = "id";
	
	
	// acciones
	public static final String ACCION_LISTA_PARAMETROS = "listaparametros";
	public static final String ACCION_EDITAR_PARAMETROS = "editarparametros";
	
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

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/candidatos";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/parametros/";
	
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
		
		VistaParametrosConfiguracion bean = new VistaParametrosConfiguracion();		
		Usuario usuario = datos.getUsuario();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTA_PARAMETROS;
		}
		
		bean.setVista(RUTA_BEP_CONF + "parametros.jsp");
		
		try {
			anonimo = !modeloUsuario.checkUser(datos);
			switch (nombreAccion) {
			case ACCION_LISTA_PARAMETROS:
				listaParametros(bean);
				break;
			case ACCION_EDITAR_PARAMETROS:
				editarParametros(request, response, bean);
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
	
	/** lista los parametros de configuracion .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listaParametros(VistaParametrosConfiguracion bean) throws SQLException {
		bean.setVista(RUTA_BEP_CONF + "index.jsp");
		
		ModeloParametrosConfiguracion modelo = ModeloParametrosConfiguracion.obtenerInstancia();
		
		List<ParametrosConfiguracion> parametros = modelo.listaParametros();
		
		bean.setParametros(parametros);
	}
	
	
	/** edita los parametros de configuracion .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void editarParametros(HttpServletRequest request, HttpServletResponse response, VistaParametrosConfiguracion bean) throws SQLException {
		// ModeloParametrosConfiguracion modelo = ModeloParametrosConfiguracion.obtenerInstancia();
		// List<ParametrosConfiguracion> parametros = modelo.listaParametros();
		
		//request.getParameter(PARAM_RAZON_EXCLUIDO)
		
	}

}
