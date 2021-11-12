package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAfinidades;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de las afinidades.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.afinidades", 
	description = "Gestión de afinidades", 
	urlPatterns = { 
		"/srv/es/informacionadministrativa/bolsaempleo/configuracion/afinidades", 
		"/srv/en/informacionadministrativa/bolsaempleo/configuracion/afinidades",
		"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/afinidades",
		"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/afinidades"
	})
public class ControladorAfinidades extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorAfinidades.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_ENVIAR = "enviar"; 
	public static final String PARAM_ID = "id";
	public static final String PARAM_CODIGO = "codigo";
	public static final String PARAM_DESCRIPCION = "descripcion";
	public static final String PARAM_MODULACION = "modulacion";
	public static final String PARAM_AFINIDADES_SELECCIONADAS = "afinidadesseleccionadas";
	
	// acciones
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_AGREGAR_AFINIDAD = "addAfinidad";
	public static final String ACCION_FORMULARIO_AFINIDAD = "formularioafinidad";
	public static final String ACCION_MODIFICAR_AFINIDAD = "editafinidad";
	public static final String ACCION_BORRAR_AFINIDAD = "borrarafinidad";
	
	// mensajes
	public static final String MENSAJE_EXITO_ELIMINAR = "Afinidad eliminada correctamente";
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGA = "La descripción no puede ser superior a %d";
	public static final String MENSAJE_ERROR_DESCRIPCION_REQUERIDA = "La descripción es requerida";
	public static final String MENSAJE_ERROR_CODIGO_LARGA = "El código no puede ser superior a %d";
	public static final String MENSAJE_ERROR_CODIGO_REQUERIDO = "El código es requerido";
	public static final String MENSAJE_ERROR_MODULACION_VACIA = "La modulación no puede estar vacia";	
	public static final String MENSAJE_INFO_AFINIDAD_INSERTADA_CORRECTAMENTE = "Afinidad insertada correctamente.";
	public static final String MENSAJE_INFO_AFINIDAD_ACTUALIZADA_CORRECTAMENTE = "Afinidad actualizada correctamente.";
	
	// mensajes de error comunes en los controladores
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_SIN_PERMISOS = "No tienes permiso";

	// ruta vistas
	public static final String RUTA_BEP_CON = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/afinidades/";
	
	// ajax	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/afinidades";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
		
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	@SuppressWarnings({"checkstyle:ExecutableStatementCount"})
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaAfinidades bean = new VistaAfinidades();		
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
					index(bean);
					break;
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_FORMULARIO_AFINIDAD:
					formularioAfinidad(bean);
					break;
				case ACCION_AGREGAR_AFINIDAD:
					nuevaAfinidad(bean, datos, request, response);
					break;
				case ACCION_MODIFICAR_AFINIDAD:
					modificarAfinidad(bean, datos, request, response);
					break;
				case ACCION_BORRAR_AFINIDAD:
					borrarAfinidad(bean, datos, request, response);
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
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);			
		}
	}
	
	private void init(VistaAfinidades bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CON + "index.jsp");
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISOS);
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void errorFatal(VistaAfinidades bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}

	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void index(VistaAfinidades bean) {
		bean.setVista(RUTA_BEP_CON + "index.jsp");
	}
	
	/** dirige al formulario de busqueda de un usuario.
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error en bd
	 */
	private void formularioAfinidad(VistaAfinidades bean) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/afinidades/formAfinidades.jsp");
	}
		
	private void listado(VistaAfinidades bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia(); 
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Afinidad> dataTable = modelo.listaAfinidadesDatatable(request.getParameterMap());
				bean.setDatatableAfinidades(dataTable);
				writer.write(dataTable.toJson());
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
	
	private void nuevaAfinidad(VistaAfinidades bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException, IOException {
		bean.setVista(RUTA_BEP_CON + "formAfinidades.jsp");
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DESCRIPCION)) != null) {
			Afinidad afinidad = this.validarAfinidad(request);		
			ModeloAfinidad.obtenerInstancia().nuevaAfinidad(afinidad, bean.getUsuarioLogeado());	

			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_INFO_AFINIDAD_INSERTADA_CORRECTAMENTE, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
		
	private void modificarAfinidad(VistaAfinidades bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		bean.setVista(RUTA_BEP_CON + "formAfinidades.jsp");
		
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
		
		Afinidad afinidad = modelo.getAfinidadById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		bean.setAfinidad(afinidad);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DESCRIPCION)) != null) {
			Afinidad afinidadForm = this.validarAfinidad(request);
			afinidadForm.setCodNum(afinidad.getCodNum());
			
			modelo.actualizaAfinidad(afinidadForm, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_INFO_AFINIDAD_ACTUALIZADA_CORRECTAMENTE, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	/** eliminar una afinidad.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void borrarAfinidad(VistaAfinidades bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_AFINIDADES_SELECCIONADAS));
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		List<Afinidad> afinidades = modelo.getAfinidadesByIds(selected);
		
		modelo.borraAfinidades(afinidades, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private Afinidad validarAfinidad(HttpServletRequest request) throws UVException {
		Afinidad afinidad = new Afinidad();
		
		afinidad.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CODIGO)));
		if (afinidad.getCodigo() == null || afinidad.getCodigo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CODIGO_REQUERIDO);
		}
		if (afinidad.getCodigo().length() > ModeloAfinidad.COLUMN_CODIGO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CODIGO_LARGA, ModeloAfinidad.COLUMN_CODIGO_MAXLENGTH));
		}
		
		afinidad.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DESCRIPCION)));
		if (afinidad.getDescripcion() == null || afinidad.getDescripcion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_DESCRIPCION_REQUERIDA);
		}
		if (afinidad.getDescripcion().length() > ModeloAfinidad.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_LARGA, ModeloAfinidad.COLUMN_DESCRIPCION_MAXLENGTH));
		}
		
		afinidad.setModulacion(Formateador.leeParametroDouble(request.getParameter(PARAM_MODULACION)));
		if (afinidad.getModulacion() == null) {
			throw new UVException(MENSAJE_ERROR_MODULACION_VACIA);
		}
		
		return afinidad;
	}
}
