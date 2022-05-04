package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEstadoBolsas;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Listado de bolsas y su estado.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.bolsas", 
		description = "Gestión de estados de bolsas", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/bolsas", 
				"/srv/en/informacionadministrativa/bolsaempleo/bolsas",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/bolsas",
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/bolsas"
		})
public class ControladorBolsas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorBolsas.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_BOLSA = "ab";
	public static final String PARAM_BOLSAS_SELECCIONADAS = "bolsasselected";
	
	// acciones
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_BOLSA = "accionbolsa";
	public static final String ACCION_BOLSAS_BLOQUEAR = "bloquear";
	public static final String ACCION_BOLSAS_REVISION = "revision";
	public static final String ACCION_BOLSAS_BAREMACION = "baremacion";
	public static final String ACCION_BOLSAS_ALEGACION = "alegacion";
	public static final String ACCION_BOLSAS_DESBLOQUEAR = "desbloquear";
	public static final String ACCION_BOLSAS_BAREMAR = "baremar";	
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_BOLSA_NO_VALIDA = "Acción no válida";
	public static final String MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS = "No hay bolsas seleccionadas válidas";
	public static final String MENSAJE_EXITO_BOLSA_MODIFICADA_CORRECTAMENTE = "Bolsa/s modificada/s correctamente";
	
	// ajax
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
		
		VistaEstadoBolsas bean = new VistaEstadoBolsas();		
		Usuario usuario = datos.getUsuario();		
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));		
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {			
			if (!init(bean, datos, request, response)) {
				return;
			}
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/bolsas/index.jsp");
					break;
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_BOLSA:
					accionSobreBolsas(bean, datos, request, response);
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
	
	private boolean init(VistaEstadoBolsas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/bolsas/index.jsp");
		bean.setTotalBolsasBloqueadas(modeloBolsa.getBolsasBloqueadas());
		bean.setTotalBolsasRevisadas(modeloBolsa.getBolsasRevisadas());
		bean.setTotalBolsasBaremables(modeloBolsa.getBolsasBaremables());
		bean.setTotalBolsas(modeloBolsa.getTotalBolsas());
		
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));
			
			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private void errorFatal(VistaEstadoBolsas bean, String mensaje) {
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
		
	private void listado(VistaEstadoBolsas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaBolsaEmpleoDatatable(request.getParameterMap());				
				bean.setDatatableBolsas(dataTable);
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
	
	private void accionSobreBolsas(VistaEstadoBolsas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();		
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BOLSAS_SELECCIONADAS));
		int[] selected;
		List<Bolsa> bolsas;
		
		try {
			selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
			bolsas = modelo.getBolsasByIds(selected);
		} catch (Exception ex) {
			throw new UVException(MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS);
		}
		
		String nombreAccionBolsa = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION_BOLSA));
		switch (nombreAccionBolsa) {
			case ACCION_BOLSAS_BLOQUEAR:
				modelo.bloquearBolsas(bolsas, bean.getUsuarioLogeado());
				break;
			case ACCION_BOLSAS_REVISION:
				modelo.ponerBolsasEnRevision(bolsas, bean.getUsuarioLogeado());
				break;
			case ACCION_BOLSAS_BAREMACION:
				modelo.ponerBolsasEnBaremacion(bolsas, bean.getUsuarioLogeado());
				break;
			case ACCION_BOLSAS_ALEGACION:
				modelo.ponerBolsasEnAlegaciones(bolsas, bean.getUsuarioLogeado());
				break;
			case ACCION_BOLSAS_DESBLOQUEAR:
				modelo.desbloquearBolsas(bolsas, bean.getUsuarioLogeado());
				break;
			case ACCION_BOLSAS_BAREMAR:
				modelo.ponerBolsasComoPendientesBaremacion(bolsas, bean.getUsuarioLogeado());
				this.baremarBolsas(bolsas);
				break;
			default:
				BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_ACCION_BOLSA_NO_VALIDA, bean, request);
				datos.setRespuestaEnviada(true);
				response.sendRedirect(request.getServletPath());
				return;
		}
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_BOLSA_MODIFICADA_CORRECTAMENTE, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void baremarBolsas(List<Bolsa> bolsas) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloResultados modeloResultados = ModeloResultados.obtenerInstancia();
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		
		if (!convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
			throw new UVException("La convocatoria no está cerrada");
		}
		
		for (Bolsa bolsa: bolsas) {
			List<Solicitud> solicitudes = modeloResultados.listaSolicitudesBolsa(bolsa, convocatoria);
			
			for (Solicitud solicitud: solicitudes) {
				modeloResultados.calcularSolicitud(solicitud, bolsa);
			}
			
			modeloBolsa.baremarBolsa(bolsa, null);
		}
	}
}
