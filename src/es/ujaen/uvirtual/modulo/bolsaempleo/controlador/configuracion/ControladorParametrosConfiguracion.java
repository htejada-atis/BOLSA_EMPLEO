package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
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
	public static final String ACCION_INDEX = "listaparametros";
	public static final String ACCION_EDITAR_PARAMETROS = "editarparametros";
	
	// mensajes
	public static final String MENSAJE_EXITO_EDITAR = "Parámetros editados correctamente";

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/parametros/";
		
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
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					listaParametros(bean);
					break;
				case ACCION_EDITAR_PARAMETROS:
					editarParametros(bean, datos, request, response);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private void init(VistaParametrosConfiguracion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "index.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}		
	}
	
	private void errorFatal(VistaParametrosConfiguracion bean, String mensaje) {
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
	 * @param bean bean de la vista a la que poner los valores .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws IOException .
	 * @throws IOException .
	 * @throws UVException .
	 */
	private void editarParametros(VistaParametrosConfiguracion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloParametrosConfiguracion modelo = ModeloParametrosConfiguracion.obtenerInstancia();
		List<ParametrosConfiguracion> parametros = modelo.listaParametros();
		
		for (ParametrosConfiguracion param : parametros) {
			param.setValor(EscapaHTML.ajustaCodificacion(request.getParameter(param.getNombre() + PARAM_VALOR)));
			param.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(param.getNombre() + PARAM_DESCRIPCION)));
			modelo.actualizaParametro(param, bean.getUsuarioLogeado());
		}
		
		List<ParametrosConfiguracion> parametrosCont = modelo.listaParametros();
		bean.setParametros(parametrosCont);
		bean.getMensajesDeExito().add(MENSAJE_EXITO_EDITAR);
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}

}
