package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlantillas;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de plantillas.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.plantillas", 
	description = "Gestión de plantillas", 
	urlPatterns = {
		"/srv/es/informacionadministrativa/bolsaempleo/configuracion/plantillas",
		"/srv/en/informacionadministrativa/bolsaempleo/configuracion/plantillas",
		"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/plantillas",
		"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/plantillas"
})
public class ControladorPlantillas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorPlantillas.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parametros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_BORRAR = "borrar";
	public static final String PARAM_CUERPO = "cuerpo";
	public static final String PARAM_DESTINATARIOS = "destinatarios";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_GUARDAR = "guardar";
	public static final String PARAM_MENSAJE_ID = "mensaje";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_PLANTILLA = "plantilla";
	public static final String PARAM_TITULO = "titulo";
	
	// acciones
	public static final String ACCION_DATATABLE_PLANTILLAS = "datatableplantillas";
	public static final String ACCION_EDITAR_PLANTILLA = "editarplantilla";
	public static final String ACCION_ELIMINAR_PLANTILLA = "eliminarplantilla";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_NUEVA_PLANTILLA = "nuevaPlantilla";
		
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_CUERPO_VACIO = "El cuerpo del mensaje no puede estar vacio";
	public static final String MENSAJE_ERROR_NOMBRE_VACIO = "El nombre de la plantilla no puede estar vacío";
	public static final String MENSAJE_ERROR_NOMBRE_LARGO = "El nombre no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_TITULO_LARGO = "El título no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_TITULO_VACIO = "El título del mensaje no puede estar vacio";
	public static final String MENSAJE_EXITO_PLANTILLA_EDITADA = "Plantilla editada correctamente";
	public static final String MENSAJE_EXITO_PLANTILLA_ELIMINADA = "Plantilla eliminada correctamente";
	public static final String MENSAJE_EXITO_PLANTILLA_NUEVA = "Plantilla creada correctamente";

	// ruta vistas
	public static final String RUTA_BEP_MEN = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/plantillas/";
	public static final String JSP_INDEX = RUTA_BEP_MEN + "index.jsp";
	public static final String JSP_FORM = RUTA_BEP_MEN + "formPlantilla.jsp";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/plantillas";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;

	/**
	 * Peticion GET.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");

		VistaPlantillas bean = new VistaPlantillas();
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());

		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}

		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					index(bean);
					break;
				case ACCION_DATATABLE_PLANTILLAS:
					listadoPlantillas(bean, datos, request, response);
					break;
				case ACCION_ELIMINAR_PLANTILLA:
					eliminarPlantilla(bean, datos, request, response);
					break;
				case ACCION_EDITAR_PLANTILLA:
					editarPlantilla(bean, datos, request, response);
					break;
				case ACCION_NUEVA_PLANTILLA:
					nuevaPlantilla(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_TINY);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
		}
	}

	private void init(VistaPlantillas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
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

	private void errorFatal(VistaPlantillas bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}

	/**
	 * redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	private void index(VistaPlantillas bean) {
		bean.setVista(JSP_INDEX);
	}
	
	private void editarPlantilla(VistaPlantillas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, IOException, SQLException {
		bean.setVista(JSP_FORM);
		ModeloPlantilla modelo = ModeloPlantilla.obtenerInstancia();
		
		Plantilla plantilla = modelo.getPlantillaById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_PLANTILLA)));
		bean.setPlantilla(plantilla);
		
		if (request.getParameter(PARAM_ENVIAR) != null) {
			Plantilla plantillaFinal = getValidatorPlantilla(request);
			plantillaFinal.setCodNum(plantilla.getCodNum());
			modelo.actualizarPlantilla(plantillaFinal, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_PLANTILLA_EDITADA, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void eliminarPlantilla(VistaPlantillas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloPlantilla modelo = ModeloPlantilla.obtenerInstancia();
		
		Plantilla plantilla = modelo.getPlantillaById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_PLANTILLA)));
		bean.setPlantilla(plantilla);
		
		modelo.borraPlantilla(plantilla, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_PLANTILLA_ELIMINADA, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void nuevaPlantilla(VistaPlantillas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_FORM);
		
		if (request.getParameter(PARAM_ENVIAR) != null) {
			ModeloPlantilla modelo = ModeloPlantilla.obtenerInstancia();

			Plantilla plantilla = getValidatorPlantilla(request);
			modelo.nuevaPlantilla(plantilla, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_PLANTILLA_NUEVA, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void listadoPlantillas(VistaPlantillas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Plantilla> dataTable = ModeloPlantilla.obtenerInstancia().listaPlantillasDatatable(request.getParameterMap());
				bean.setDataTablePlantillas(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
					LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
					LOGGER.log(Level.SEVERE, e.toString());
				} else {
					LOGGER.log(Level.WARNING, e.toString());
				}
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private Plantilla getValidatorPlantilla(HttpServletRequest request) throws UVException {
		Plantilla plantilla = new Plantilla();
		
		plantilla.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_NOMBRE)));
		if (plantilla.getNombre() == null || plantilla.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (plantilla.getNombre().length() > ModeloPlantilla.PLANTILLAS_COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_LARGO, ModeloPlantilla.PLANTILLAS_COLUMN_NOMBRE_MAXLENGTH));
		}
		
		plantilla.setTitulo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_TITULO)));
		if (plantilla.getTitulo() == null || plantilla.getTitulo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_TITULO_VACIO);
		}
		if (plantilla.getTitulo().length() > ModeloPlantilla.PLANTILLAS_COLUMN_TITULO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_TITULO_LARGO, ModeloPlantilla.PLANTILLAS_COLUMN_TITULO_MAXLENGTH));
		}
		
		plantilla.setCuerpo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CUERPO)));
		if (plantilla.getCuerpo() == null || plantilla.getCuerpo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CUERPO_VACIO);
		}
		
		return plantilla;
	}
	
}
