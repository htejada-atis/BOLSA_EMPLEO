package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAreasBaremar;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de las áreas a baremar.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion.areasbaremar", 
		description = "Gestión de las áreas a baremar", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion/areasbaremar", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion/areasbaremar",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/areasbaremar",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/areasbaremar"
		})
public class ControladorAreasABaremar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorAreasABaremar.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_AREA = "accionarea";
	public static final String ACCION_AREA_PASAR_A_BAREMALE = "accionareabaremable";
	public static final String ACCION_AREA_PASAR_A_NO_BAREMALE = "accionareanobaremable";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DATATABLE_EXCLUIDOS = "datatableexcluidos";
	public static final String ACCION_IMPORTAR_AREAS_UVIRTUAL = "importarareasuvirtual";
	public static final String ACCION_INDEX = "listar";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_AREA_NO_VALIDA = "Acción no válida";
	public static final String MENSAJE_EXITO_AREA_MODIFICADA_CORRECTAMENTE = "Area/s modificada/s correctamente"; 
	public static final String MENSAJE_EXITO_AREAS_AGREGADAS = "Se han agregado %d áreas nuevas al sistema";
	public static final String MENSAJE_EXITO_AREA_AGREGADA = "Se ha agregado un área nueva al sistema";
	public static final String MENSAJE_EXITO_AREAS_ACTUALIZADAS = "Se han actualizado %d áreas del sistema";
	public static final String MENSAJE_EXITO_AREA_ACTUALIZADA = "Se ha actualizado un área del sistema";
	public static final String MENSAJE_EXITO_SISTEMA_ACTUALIZADO = "No hay cambios necesarios, el sistema está actualizado";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_AREA = "aa";
	public static final String PARAM_AREAS_SELECCIONADAS = "areasselected";
	public static final String PARAM_ID = "id";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/";
		
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaAreasBaremar bean = new VistaAreasBaremar();		
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
					break;
				case ACCION_AREA:
					accionSobreArea(bean, request, response);
					break;
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_EXCLUIDOS:
					listadoAreasExcluidasUsuario(bean, datos, request, response);
					break;
				case ACCION_IMPORTAR_AREAS_UVIRTUAL:
					importarAreasDeUvirtual(bean, request, response);
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
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private void init(VistaAreasBaremar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "areasbaremar.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		try {
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
			bean.setUsuarioBolsa(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(datos.getUsuario().getDocumentoNumero()));
		} catch (UVException e) {
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}
	}
	
	private void errorFatal(VistaAreasBaremar bean, String mensaje) {
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
		
	/**
	 * AJAX para devolver listado de areas (bolsas).
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listado(VistaAreasBaremar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreaDatatable(request.getParameterMap());
				bean.setDatatableAreas(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * AJAX para devolver listado de areas excluidas de un usuario(bolsas).
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoAreasExcluidasUsuario(VistaAreasBaremar bean, UVDatos datos, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();	
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreaExcluidasUsuarioDatatable(request.getParameterMap(), codNum);
				bean.setDatatableAreas(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void accionSobreArea(VistaAreasBaremar bean, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException, IOException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		
		String nombreAccionBolsa = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION_AREA));
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_AREAS_SELECCIONADAS));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		List<Bolsa> bolsas = modelo.getBolsasByIds(selected);
		bean.setListaBolsas(bolsas);

		switch (nombreAccionBolsa) {
			case ACCION_AREA_PASAR_A_BAREMALE:
				modelo.ponerAreaComoBaremable(bolsas);
				break;
			case ACCION_AREA_PASAR_A_NO_BAREMALE:
				modelo.ponerAreaComoNoBaremable(bolsas);
				break;
			default:
				BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_ACCION_AREA_NO_VALIDA, bean, request);
				return;
		}
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AREA_MODIFICADA_CORRECTAMENTE, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	/**
	 * método para actualizar las áreas del sistema con las áreas de uvirtual .
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void importarAreasDeUvirtual(VistaAreasBaremar bean, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException, IOException {
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		Integer nuevas = modeloArea.insertarAreasNuevasExternas(bean.getUsuarioBolsa());
		Integer actualizadas = modeloArea.actualizaAreasDeExternas(bean.getUsuarioBolsa());
		String mensajes = "";
		
		if (nuevas > 0 || actualizadas > 0) {
			if (nuevas > 0) {
				mensajes += nuevas > 1 ? String.format(MENSAJE_EXITO_AREAS_AGREGADAS, nuevas) : MENSAJE_EXITO_AREA_AGREGADA;
				mensajes += ". ";
			}
			
			if (actualizadas > 0) {
				mensajes += actualizadas > 1 ? String.format(MENSAJE_EXITO_AREAS_ACTUALIZADAS, actualizadas) : MENSAJE_EXITO_AREA_ACTUALIZADA;
			}
		} else {
			mensajes = MENSAJE_EXITO_SISTEMA_ACTUALIZADO;
		}
		
		if (mensajes.length() > 0) {
			BolsaEmpleoUtils.addMensajeDeExito(mensajes, bean, request);
			response.sendRedirect(request.getServletPath());
		}
	}	
}
