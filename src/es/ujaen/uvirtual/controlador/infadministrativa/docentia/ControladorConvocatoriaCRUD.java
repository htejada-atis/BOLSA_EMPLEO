package es.ujaen.uvirtual.controlador.infadministrativa.docentia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;
import es.ujaen.uvirtual.beans.vistas.uvirtual.docentia.VistaConvocatoriaCRUD;
import es.ujaen.uvirtual.modelo.ModeloDocentia;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase controlador para obtener, cambiar, eliminar y agregar convocatorias.
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * */
@WebServlet(
		name = "informacionadministrativa.docentia.convocatoriacrud", 
		description = "Docentia mantenimiento convocatorias", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/docentia/convocatoriacrud", 
				"/srv/en/informacionadministrativa/docentia/convocatoriacrud"
		})
public class ControladorConvocatoriaCRUD extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorConvocatoriaCRUD.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	
	//Posibles acciones
	public static final String ACCION_OBTENER_CONVOCATORIAS = "obtenerconvocatorias";
	public static final String ACCION_AGREGAR_CONVOCATORIA = "agregarconvocatoria";
	public static final String ACCION_ELIMINAR_CONVOCATORIA = "eliminarconvocatoria";
	public static final String ACCION_EDITAR_CONVOCATORIA = "editarconvocatoria";
	public static final String ACCION_CAMBIAR_CONVOCATORIA = "cambiarConvocatoria";
	
	//Posibles parametros
	public static final String PARAM_NOMBRE_CONVOCATORIA = "nombreConvocatoria";
	public static final String PARAM_ID = "idConvocatoria";
	public static final String PARAM_ESTADO = "estadoConvocatoria";
	public static final String PARAM_OBSERVACIONES = "observacionesConvocatoria";
	public static final String PARAM_FECHA_LIMITE = "fechaLimmiteConvocatoria";
	public static final String PARAM_FECHA_COMISION = "fechaComision";
	public static final String MENSAJE_EXITO_AGREGAR = "convocatoria creada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "convocatoria eliminada correctamente";
	private static final String FORMATO_FECHA = "DDMMYYYY";
	private static final String SEPARADOR_FECHA = "/";
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		VistaConvocatoriaCRUD bean = new VistaConvocatoriaCRUD();
		//ejemplo para obtener el usuario
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/docentia/Convocatorias.jsp");
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_OBTENER_CONVOCATORIAS;
		}
		try {
			switch (nombreAccion) {
				case ACCION_OBTENER_CONVOCATORIAS:
					obtenerConvocatorias(bean);
					break;
				case ACCION_AGREGAR_CONVOCATORIA:
					agregarConvocatoria(request, bean);
					break;
				case ACCION_ELIMINAR_CONVOCATORIA:
					eliminarConvocatoria(request, bean);
					break;
				case ACCION_EDITAR_CONVOCATORIA:
					editarConvocatoria(request, bean);
					break;
				case ACCION_CAMBIAR_CONVOCATORIA:
					cambiarConvocatoria(request, bean);
					break;
				default:
					obtenerConvocatorias(bean);
					break;
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
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
	
	/** muestra todas las convocatorias.
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerConvocatorias(VistaConvocatoriaCRUD bean) throws SQLException {
		//ejemplo para obtener un valor de la configuracion
		String urlAyuda = ConfiguracionGlobal.getParametroCadenaNE("docentia.urlAyuda");
		LOGGER.log(Level.FINEST, "el parametro urlAyuda es {0}", urlAyuda);

		ModeloDocentia modelo = new ModeloDocentia();
		List<Convocatoria> convocatorias = modelo.listaConvocatorias();
		bean.setConvocatorias(convocatorias);
	}
	
	/** agrega una nueva convocatoria.
	 * @param request .
	 * @param bean bean de la vista a la que poner los valores
	 * @throws SQLException en caso de error de bd
	 * @throws UVException en caso de error de parametros
	 */
	private void agregarConvocatoria(HttpServletRequest request, VistaConvocatoriaCRUD bean) throws SQLException, UVException {
		String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE_CONVOCATORIA));
		String estado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ESTADO));
		Date fechaLimite = Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_LIMITE), FORMATO_FECHA, SEPARADOR_FECHA);
		Date fechaComision = Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_COMISION), FORMATO_FECHA, SEPARADOR_FECHA);
		String observaciones = Formateador.leeParametroString(request.getParameter(PARAM_OBSERVACIONES));
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setNombreConvocatoria(nombre);
		convocatoria.setEstado(estado);
		convocatoria.setObservaciones(observaciones);
		convocatoria.setFechaLimite(fechaLimite);
		convocatoria.setFechaComision(fechaComision);
		ModeloDocentia modelo = new ModeloDocentia();
		modelo.insertaConvocatoria(convocatoria);
		bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR);
		obtenerConvocatorias(bean);
	}
	
	/** elimina convocatoria.
	 * @param request .
	 * @param bean bean de la vista
	 * @throws SQLException en caso de error en bd
	 * @throws UVException en caso de error de parametros
	 */
	private void eliminarConvocatoria(HttpServletRequest request, VistaConvocatoriaCRUD bean) throws SQLException, UVException {
		ModeloDocentia modelo = new ModeloDocentia();
		Integer idConvocatoria = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setIdConvocatoria(idConvocatoria);
		modelo.borraConvocatoria(convocatoria);
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ELIMINAR);
		obtenerConvocatorias(bean);
	}

	/** edita convocatoria.
	 * @param request .
	 * @param bean bean de la vista
	 * @throws SQLException en caso de error en bd
	 * @throws UVException errores controlados
	 */
	private void editarConvocatoria(HttpServletRequest request, VistaConvocatoriaCRUD bean) throws SQLException, UVException {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/docentia/editarConvocatoria.jsp");
		ModeloDocentia modelo = new ModeloDocentia();
		Integer idConvocatoria = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		Convocatoria conv = modelo.listaConvocatoria(idConvocatoria);
		bean.setConvocatoria(conv);
	}

	/** cambia la convocatoria.
	 * @param request .
	 * @param bean de la vista
	 * @throws SQLException en caso de error de parametros
	 * @throws UVException en caso de error en bd
	 */
	private void cambiarConvocatoria(HttpServletRequest request, VistaConvocatoriaCRUD bean) throws SQLException, UVException {
		ModeloDocentia modelo = new ModeloDocentia();
		Integer idConvocatoria = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		String nombreConvocatoria = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE_CONVOCATORIA));
		String estado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ESTADO));
		String observaciones = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_OBSERVACIONES));
		Date fechaLimite = Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_LIMITE), Formateador.FORMATO_FECHA_DDMMYYYY, "/");
		Date fechaComision = Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_COMISION), Formateador.FORMATO_FECHA_DDMMYYYY, "/");
		Convocatoria convocatoria = new Convocatoria(idConvocatoria, nombreConvocatoria, estado, observaciones, fechaLimite, fechaComision);
		modelo.actualizaConvocatoria(convocatoria);
		obtenerConvocatorias(bean);
	}
}
