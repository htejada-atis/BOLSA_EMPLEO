package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConvocatorias;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase controlador de convocatorias .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.convocatorias", 
	description = "Gestión de convocatorias", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/convocatorias", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/convocatorias",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/convocatorias",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/convocatorias"
})
public class ControladorConvocatorias extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorConvocatorias.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_ENVIAR = "enviar"; 
	public static final String PARAM_CONVOCATORIA_DESCRIPCION = "descripcion";
	public static final String PARAM_CONVOCATORIA_FECHACIERRE = "fechaCierre";
	public static final String PARAM_CONVOCATORIA_ID = "id";
	public static final String PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO = "numBolsasMaximo";
	public static final String PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE = "numMeritosPorBloque";
	public static final String PARAM_CONVOCATORIA_CURSO = "curso";
	
	// acciones
	public static final String ACCION_ABRIR_CONVOCATORIA = "abrirConvocatoria";
	public static final String ACCION_AGREGAR_CONVOCATORIA = "addConvocatoria";
	public static final String ACCION_BORRAR_CONVOCATORIA = "borrarConvocatoria";
	public static final String ACCION_CERRAR_CONVOCATORIA = "cerrarConvocatoria";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_FINALIZAR_CONVOCATORIA = "finalizarconvocatoria";
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_MODIFICAR_CONVOCATORIA = "editConvocatoria";
	public static final String ACCION_SELECCIONAR_CONVOCATORIA = "seleccionarconvocatoria";
	public static final String ACCION_REABRIR_FINALIZADA_CONVOCATORIA = "abrirConvocatoriaFinalizada";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_APARTADOS_PORCENTAGES = 
			"Los apartados de baremación no alcanzan el 100% de porcentaje, porfavor edite los apartados antes de abrir una convocatoria";
	public static final String MENSAJE_ERROR_BOLSAS_BLOQUEADAS = "Debe desbloquear primero el total de las bolsas para abrir la convocatoria";
	public static final String MENSAJE_ERROR_CONVOCATORIAS_ABIERTAS = "Ya existen convocatorias abiertas.";
	public static final String MENSAJE_ERROR_EXISTE_CONVOCATORIA_NO_FINALIZADA = "No se puede añadir una nueva convocatoria si hay convocatoria no finalizada";
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGA = "La descripción no puede ser superior a %d";
	public static final String MENSAJE_ERROR_DESCRIPCION_VACIA = "La descripción no puede estar vacia";
	public static final String MENSAJE_ERROR_EDITAR_CONVOCATORIA_CON_SOLICITUDES = "No puede editar la convocatoria, ya existen solicitudes para ella";
	public static final String MENSAJE_ERROR_ESTADO_CERRADA_REQUERIDO = "Es requerido estado de cerrada para la convocatoria";
	public static final String MENSAJE_ERROR_FECHACIERRE_INCORRECTA = "La fecha de cierre no tiene el formato DD/MM/YYYY";
	public static final String MENSAJE_ERROR_FECHACIERRE_REQUERIDA = "La fecha de cierre es requerida";
	public static final String MENSAJE_ERROR_FECHACIERRE_MINIMA = "La fecha de cierre debe ser mayor que la fecha actual";
	public static final String MENSAJE_ERROR_MERITOS_SIN_VALIDAR = "Para finalizar la convocatoria no puede haber méritos sin validar";
	public static final String MENSAJE_ERROR_MODIFICAR_CONVOCATORIA_FINALIZADA = "La convocatoria ya está finalizada, no puede modificarse";
	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_REQUERIDA = "El número de bolsas máximas es requerido";
	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_INCORRECTA = "El número de bolsas máximas debe ser un entero";
	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_MINIMO = "El número de bolsas máximas debe ser al menos una";
	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_REQUERIDA = "El número de méritos por bloque es requerido";
	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_INCORRECTA = "El número de méritos por bloque debe ser un entero";
	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_MINIMO = "El número de méritos por bloque debe ser al menos uno";
	public static final String MENSAJE_ERROR_SIN_BOLSAS_BAREMABLES = "Antes de abrir una convocatoria debe de tener areas baremables";
	public static final String MENSAJE_ERROR_SIN_PERMISO_PERSONAL = "No tienes permiso de personal";
	public static final String MENSAJE_ERROR_CONVOCATORIA_DEBE_ESTAR_CERRADA = "La convocatoria debe estar finalizada";
	public static final String MENSAJE_ERROR_CURSO_NO_UNICO = "El curso ya se está usando en otra convocatoria";
	public static final String MENSAJE_ERROR_CURSO_LARGO = "El curso no puede ser superior a %d";
	public static final String MENSAJE_ERROR_CURSO_VACIO = "El curso no puede estar vacío";
	public static final String MENSAJE_EXITO_ABRIR_CONVOCATORIA = "Convocatoria editada correctamente";
	public static final String MENSAJE_EXITO_CERRAR_CONVOCATORIA = "Convocatoria cerrada correctamente";
	public static final String MENSAJE_EXITO_EDITAR_CONVOCATORIA = "Convocatoria editada correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR_CONVOCATORIA = "Convocatoria eliminada correctamente";
	public static final String MENSAJE_EXITO_FINALIZAR_CONVOCATORIA = "Convocatoria finalizada correctamente";
	public static final String MENSAJE_EXITO_INSERTAR_CONVOCATORIA = "Convocatoria insertada correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_CON = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/convocatorias/";
	public static final String JSP_INDEX = RUTA_BEP_CON + "indexConvocatorias.jsp";
	public static final String JSP_FORM_CONVOCATORIA = RUTA_BEP_CON + "formConvocatoria.jsp";
	
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/convocatorias";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:JavaNCSS", "checkstyle:ExecutableStatementCount"})
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaConvocatorias bean = new VistaConvocatorias();
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(
				request, PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			if (!init(bean, datos, request, response)) {
				return;
			}
			switch (nombreAccion) {
				case ACCION_ABRIR_CONVOCATORIA:
				case ACCION_BORRAR_CONVOCATORIA:
				case ACCION_CERRAR_CONVOCATORIA:
				case ACCION_FINALIZAR_CONVOCATORIA:
				case ACCION_MODIFICAR_CONVOCATORIA:
				case ACCION_SELECCIONAR_CONVOCATORIA:
				case ACCION_REABRIR_FINALIZADA_CONVOCATORIA:
					seleccionarConvocatoria(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_AGREGAR_CONVOCATORIA:
					nuevaConvocatoria(bean, datos, request, response);
					break;
				case ACCION_DATATABLE:
					listaConvocatorias(bean, datos, request, response);
					break;
				case ACCION_INDEX:
					index(bean);
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
	
	private boolean init(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));
			
			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO_PERSONAL);
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private void errorFatal(VistaConvocatorias bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	private void index(VistaConvocatorias bean) {
		bean.setVista(JSP_INDEX);
	}
	
	/** insertar una convocatoria.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void nuevaConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		bean.setVista(JSP_FORM_CONVOCATORIA);
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
			
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CONVOCATORIA_DESCRIPCION)) != null) {
			modelo.chequearCreacionConvocatoria();
			
			Convocatoria convocatoria = this.validateConvocatoria(request, null);
			
			// creamos convocatoria, por defecto cerrada
			convocatoria.setEstado(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA);
			modelo.nuevaConvocatoria(convocatoria, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_INSERTAR_CONVOCATORIA, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void seleccionarConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_FORM_CONVOCATORIA);
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		Convocatoria convocatoria = modelo.getConvocatoriaById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_CONVOCATORIA_ID)));
		bean.setConvocatoria(convocatoria);
		
		// poner en cerrado la convocatoria finalizada
		if (nombreAccion.equals(ACCION_REABRIR_FINALIZADA_CONVOCATORIA) 
				&& convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_FINALIZADA)) {
			reabrirConvocatoriaFinalizada(bean, datos, request, response);
			return;
		}
		
		// no permitir moficiar convocatorias finalizadas
		if (!nombreAccion.equals(ACCION_SELECCIONAR_CONVOCATORIA) 
				&& convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_FINALIZADA)) {
			throw new UVException(MENSAJE_ERROR_MODIFICAR_CONVOCATORIA_FINALIZADA);
		}
		
		switch (nombreAccion) {
			case ACCION_ABRIR_CONVOCATORIA:
				abrirConvocatoria(bean, datos, request, response);
				break;
			case ACCION_CERRAR_CONVOCATORIA:
				cerrarConvocatoria(bean, datos, request, response);
				break;
			case ACCION_BORRAR_CONVOCATORIA:
				borrarConvocatoria(bean, datos, request, response);
				break;
			case ACCION_MODIFICAR_CONVOCATORIA:
				editarConvocatoria(bean, datos, request, response);
				break;
			case ACCION_FINALIZAR_CONVOCATORIA:
				finalizarConvocatoria(bean, datos, request, response);
				break;
		}
	}
	
	/** cambiar estado de una convocatoria a abierta.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void abrirConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		ModeloBolsa modeloBolsas = ModeloBolsa.obtenerInstancia();
		
		if (!modeloBolsas.hayBolsasBaremables()) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_SIN_BOLSAS_BAREMABLES, bean, request);
		} else if (modelo.checkBolsasDesbloqueadas()) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_BOLSAS_BLOQUEADAS, bean, request);
		} else if (ModeloBaremacionApartados.obtenerInstancia().checkSumaPorcentagesApartadosInvalido()) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_APARTADOS_PORCENTAGES, bean, request);
		} else {
			modelo.abrirConvocatoria(bean.getConvocatoria(), bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ABRIR_CONVOCATORIA, bean, request);
		}
		
		redireccionConConvocatoriaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_CONVOCATORIA);
	}
	
	/** eliminar una convocatoria.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void borrarConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloConvocatoria.obtenerInstancia().borraConvocatoria(bean.getConvocatoria(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR_CONVOCATORIA, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void cerrarConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		Convocatoria convocatoria = bean.getConvocatoria();
		convocatoria.setEstado(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA);
		modelo.cambiaEstadoConvocatoria(convocatoria, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_CERRAR_CONVOCATORIA, bean, request);
		redireccionConConvocatoriaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_CONVOCATORIA);
	}
	
	/** editar una convocatoria.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void editarConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		Integer solicitudes = modelo.getNumSolicitudesByConvocatoriaId(bean.getConvocatoria().getCodNum());
		if (solicitudes > 0) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_EDITAR_CONVOCATORIA_CON_SOLICITUDES, bean, request);
		} else {
			Convocatoria convocatoriaForm = this.validateConvocatoria(request, bean.getConvocatoria().getCodNum());
			
			Convocatoria conFinal = new Convocatoria(
					bean.getConvocatoria().getCodNum(),
					convocatoriaForm.getDescripcion(),
					convocatoriaForm.getCurso(),
					convocatoriaForm.getFechaCierre(),
					bean.getConvocatoria().getEstado(),
					convocatoriaForm.getNumBolsasMaximo(),
					convocatoriaForm.getNumMeritosPorBloque()
			);
			modelo.actualizaConvocatoria(conFinal, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR_CONVOCATORIA, bean, request);
		}
		
		redireccionConConvocatoriaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_CONVOCATORIA);
	}
	
	private void finalizarConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		if (!bean.getConvocatoria().getEstado().contains(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_ESTADO_CERRADA_REQUERIDO, bean, request);
		} else if (modelo.contieneMeritosSinValidar(bean.getConvocatoria())) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_MERITOS_SIN_VALIDAR, bean, request);
		} else {
			Convocatoria convocatoria = bean.getConvocatoria();
			modelo.finalizarConvocatoria(convocatoria, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_CERRAR_CONVOCATORIA, bean, request);
		}
		
		redireccionConConvocatoriaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_CONVOCATORIA);
	}
	
	private void reabrirConvocatoriaFinalizada(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		if (!bean.getConvocatoria().getEstado().contains(ModeloConvocatoria.CONVOCATORIA_ESTADO_FINALIZADA)) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_CONVOCATORIA_DEBE_ESTAR_CERRADA, bean, request);
		} else {
			Convocatoria convocatoria = bean.getConvocatoria();
			modelo.ponerEnCerradaConvocatoriaFinalizada(convocatoria, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ABRIR_CONVOCATORIA, bean, request);
		}
		
		redireccionConConvocatoriaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_CONVOCATORIA);
	}
	
	private void redireccionConConvocatoriaSeleccionada(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_CONVOCATORIA_ID, bean.getConvocatoria().getCodNum().toString());
		params.put(PARAM_ACCION, accion);
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	private void listaConvocatorias(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Convocatoria> dataTable = modelo.listaConvocatoriasDatatable(request.getParameterMap());
				bean.setDatatableConvocatorias(dataTable);
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
	
	private Convocatoria validateConvocatoria(HttpServletRequest request, Integer codNum) throws UVException, SQLException {
		Convocatoria c = new Convocatoria();
		c.setCodNum(codNum);
		
		c.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CONVOCATORIA_DESCRIPCION)));
		if (c.getDescripcion() == null || c.getDescripcion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_DESCRIPCION_VACIA);
		}
		if (c.getDescripcion().length() > ModeloConvocatoria.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_LARGA, ModeloConvocatoria.COLUMN_DESCRIPCION_MAXLENGTH));
		}
		
		c.setCurso(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CONVOCATORIA_CURSO)));
		if (c.getCurso() == null || c.getCurso().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CURSO_VACIO);
		}
		if (c.getCurso().length() > ModeloConvocatoria.COLUMN_CURSO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CURSO_LARGO, ModeloConvocatoria.COLUMN_CURSO_MAXLENGTH));
		}
		if (!ModeloConvocatoria.obtenerInstancia().checkCursoUnico(c)) {
			throw new UVException(MENSAJE_ERROR_CURSO_NO_UNICO);
		}
		
		c.setFechaCierre(Formateador.leeParametroFecha(request.getParameter(PARAM_CONVOCATORIA_FECHACIERRE), Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
		if (c.getFechaCierre().before(BolsaEmpleoUtils.getCurrentDateTime())) {
			throw new UVException(MENSAJE_ERROR_FECHACIERRE_MINIMA);
		}
		c.setFechaCierre(BolsaEmpleoUtils.setDateTimeAtEndOfDay(c.getFechaCierre()));
		
		c.setNumBolsasMaximo(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO)));
		if (c.getNumBolsasMaximo() == null) {
			throw new UVException(MENSAJE_ERROR_NUMBOLSASMAXIMAS_REQUERIDA);
		}
		if (c.getNumBolsasMaximo() < 1) {
			throw new UVException(MENSAJE_ERROR_NUMBOLSASMAXIMAS_MINIMO);
		}

		c.setNumMeritosPorBloque(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE)));
		if (c.getNumMeritosPorBloque() == null) {
			throw new UVException(MENSAJE_ERROR_NUMMERITOSBLOQUE_REQUERIDA);
		}
		if (c.getNumMeritosPorBloque() < 1) {
			throw new UVException(MENSAJE_ERROR_NUMMERITOSBLOQUE_MINIMO);
		}
		
		return c;
	}
}
