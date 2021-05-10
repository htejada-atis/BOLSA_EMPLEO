package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

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
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoValidator;
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
	public static final String ACCION_LISTAR_AFINIDADES = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_AGREGAR_AFINIDAD = "addAfinidad";
	public static final String ACCION_FORMULARIO_AFINIDAD = "formularioafinidad";
	public static final String ACCION_MODIFICAR_AFINIDAD = "editafinidad";
	public static final String ACCION_BORRAR_AFINIDAD = "borrarafinidad";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_ELIMINAR = "Afinidad eliminada correctamente";
	
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGA = "La descripción no puede ser superior a %d";
	public static final String MENSAJE_ERROR_DESCRIPCION_REQUERIDA = "La descripción es requerida";
	public static final String MENSAJE_ERROR_DESCRIPCION_VACIA = "La descripción no puede estar vacia";
	
	public static final String MENSAJE_ERROR_CODIGO_LARGA = "El código no puede ser superior a %d";
	public static final String MENSAJE_ERROR_CODIGO_REQUERIDO = "El código es requerido";
	public static final String MENSAJE_ERROR_CODIGO_VACIO = "El código no puede estar vacio";
	
	public static final String MENSAJE_ERROR_MODULACION_LARGA = "La modulación no puede ser superior a %d";
	public static final String MENSAJE_ERROR_MODULACION_REQUERIDA = "La modulación es requerida";
	public static final String MENSAJE_ERROR_MODULACION_VACIA = "La modulación no puede estar vacia";	
	
	public static final String MENSAJE_INFO_AFINIDAD_INSERTADA_CORRECTAMENTE = "Afinidad insertada correctamente.";
	public static final String MENSAJE_INFO_AFINIDAD_ACTUALIZADA_CORRECTAMENTE = "Afinidad actualizada correctamente.";

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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaAfinidades bean = new VistaAfinidades();		
		Usuario usuario = datos.getUsuario();
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));		
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_AFINIDADES;
		}
					
		try {
			modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_LISTAR_AFINIDADES:
					index(bean);
					break;				
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_FORMULARIO_AFINIDAD:
					formularioAfinidad(bean);
					break;
				case ACCION_AGREGAR_AFINIDAD:
					nuevaAfinidad(bean, request);
					break;
				case ACCION_MODIFICAR_AFINIDAD:
					modificarAfinidad(bean, request);
					break;
				case ACCION_BORRAR_AFINIDAD:
					borrarAfinidad(bean, request, response);
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
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");			
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
		
	private void listado(VistaAfinidades bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia(); 
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Afinidad> dataTable = modelo.listaAfinidadesDatatable(request.getParameterMap());
				bean.setDatatableAfinidades(dataTable);
				
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();				
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void nuevaAfinidad(VistaAfinidades bean, HttpServletRequest request) throws UVException, SQLException {
		bean.setVista(RUTA_BEP_CON + "formAfinidades.jsp");
		
		BolsaEmpleoValidator validator = this.getValidatorAfinidad(request); 							
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
		
		if (!validator.isValid()) {
			for (String param : validator.getErrors().keySet()) {
				for (String paramError : validator.getErrors().get(param)) {
					bean.getMensajesDeError().add(paramError);
				}
			}
		} else {
			
			Afinidad afinidad = new Afinidad();
			afinidad.setCodigo(validator.getValueString(PARAM_CODIGO));
			afinidad.setDescripcion(validator.getValueString(PARAM_DESCRIPCION));
			afinidad.setModulacion(validator.getValueFloat(PARAM_MODULACION)); 
			modelo.nuevaAfinidad(afinidad);	
						
			bean.getMensajesDeExito().add(MENSAJE_INFO_AFINIDAD_INSERTADA_CORRECTAMENTE);			
			
			this.index(bean);
		}			
	}
		
	private void modificarAfinidad(VistaAfinidades bean, HttpServletRequest request) throws UVException, SQLException { 
		bean.setVista(RUTA_BEP_CON + "formAfinidades.jsp");
		
		BolsaEmpleoValidator validator = this.getValidatorAfinidad(request); 							
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
		
		Afinidad afinidad = modelo.getAfinidadById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		bean.setAfinidad(afinidad);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DESCRIPCION)) != null) {
			if (!validator.isValid()) {
				for (String param : validator.getErrors().keySet()) {
					for (String paramError : validator.getErrors().get(param)) {
						bean.getMensajesDeError().add(paramError);
					}
				}
			} else {
				
				Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ID));
				String descripcion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DESCRIPCION));
				String codigo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CODIGO));
				Float modulacion = BolsaEmpleoUtils.leeParametroFloat(request.getParameter(PARAM_MODULACION));
				
				Afinidad afinidadEdit = new Afinidad(codNum, codigo, descripcion, modulacion); 
				modelo.actualizaAfinidad(afinidadEdit);
				
				bean.getMensajesDeExito().add(MENSAJE_INFO_AFINIDAD_ACTUALIZADA_CORRECTAMENTE);
				
				this.index(bean);
			}
		}
	}
	
	/** eliminar una afinidad.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void borrarAfinidad(VistaAfinidades bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_AFINIDADES_SELECCIONADAS));
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		List<Afinidad> afinidades = modelo.getAfinidadesByIds(selected);
		
		modelo.borraAfinidades(afinidades);
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ELIMINAR);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_ELIMINAR);
		response.sendRedirect(request.getServletPath());
	}
	
	private BolsaEmpleoValidator getValidatorAfinidad(HttpServletRequest request) throws UVException {
		BolsaEmpleoValidator validator = new BolsaEmpleoValidator(request);
		
		validator.addParamString(PARAM_CODIGO);
		validator.addRule(PARAM_CODIGO, "required", MENSAJE_ERROR_CODIGO_REQUERIDO);
		validator.addRule(PARAM_CODIGO, "noBlank", MENSAJE_ERROR_CODIGO_VACIO);
		validator.addRule(PARAM_CODIGO, "max:" + ModeloAfinidad.COLUMN_CODIGO_MAXLENGTH, 
				String.format(MENSAJE_ERROR_CODIGO_LARGA, ModeloAfinidad.COLUMN_CODIGO_MAXLENGTH));
		
		validator.addParamString(PARAM_DESCRIPCION);
		validator.addRule(PARAM_DESCRIPCION, "required", MENSAJE_ERROR_DESCRIPCION_REQUERIDA);
		validator.addRule(PARAM_DESCRIPCION, "noBlank", MENSAJE_ERROR_DESCRIPCION_VACIA);
		validator.addRule(PARAM_DESCRIPCION, "max:" + ModeloAfinidad.COLUMN_DESCRIPCION_MAXLENGTH, 
				String.format(MENSAJE_ERROR_DESCRIPCION_LARGA, ModeloAfinidad.COLUMN_DESCRIPCION_MAXLENGTH));
		
		validator.addParamFloat(PARAM_MODULACION);
		validator.addRule(PARAM_MODULACION, "required", MENSAJE_ERROR_MODULACION_VACIA);
		
		return validator;
	}
}
