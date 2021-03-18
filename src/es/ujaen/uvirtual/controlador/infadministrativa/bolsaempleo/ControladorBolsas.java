package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEstadoBolsas;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.utilidades.DataTable;
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
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/bolsas"
		})
public class ControladorBolsas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorBolsas.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_BOLSAS_SELECTED = "selected";
	public static final String PARAM_BOLSA_ESTADO = "estado";
	public static final String PARAM_BOLSA_ESTADO_SELECTED = "selected";
	public static final String PARAM_BOLSA_ESTADO_BLOQUEAR = "bloquear";
	public static final String PARAM_BOLSA_ESTADO_REVISION = "revision";
	public static final String PARAM_BOLSA_ESTADO_BAREMACION = "baremacion";
	public static final String PARAM_BOLSA_ESTADO_ALEGACION = "alegacion";
	public static final String PARAM_BOLSA_ESTADO_DESBLOQUEAR = "desbloquear";
	public static final String PARAM_BOLSA_ESTADO_BAREMAR = "baremar";
	
	// acciones
	public static final String ACCION_LISTAR_BOLSAS = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_CAMBIAR_ESTADO_BOLSA = "cambiarestadobolsa";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaEstadoBolsas bean = new VistaEstadoBolsas();	
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/bolsas/index.jsp");
		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		LOGGER.log(Level.WARNING, "accion {0}", nombreAccion);
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_BOLSAS;
		}
		
		// respuesta ajax
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (ajax) {
			this.responseAjax(nombreAccion, request, response);
			return;
		} 
		
		// respuesta normal
		try {
			switch (nombreAccion) {
				case ACCION_LISTAR_BOLSAS:
					// listado por ajax
					break;
				case ACCION_CAMBIAR_ESTADO_BOLSA:
					cambiarEstadoBolsa(bean, request);
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
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
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
	
	private void cambiarEstadoBolsa(VistaEstadoBolsas bean, HttpServletRequest request) throws UVException, SQLException {
		ModeloBolsa modelo = new ModeloBolsa();
		
		// operación sobre las bolsas
		String estado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BOLSA_ESTADO));
		List<String> estadosValidos = Arrays.asList(new String[]{
				PARAM_BOLSA_ESTADO_BLOQUEAR, PARAM_BOLSA_ESTADO_REVISION, PARAM_BOLSA_ESTADO_BAREMACION, 
				PARAM_BOLSA_ESTADO_ALEGACION, PARAM_BOLSA_ESTADO_DESBLOQUEAR, PARAM_BOLSA_ESTADO_BAREMAR,
		});				
		if (!estadosValidos.contains(estado)) {
			throw new UVException("Estado no válido");
		}
		
		// listado de ids de las bolsas
		int[] ids;
		String selected = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BOLSA_ESTADO_SELECTED));		
		try {
			ids = Arrays.stream(selected.split(",")).mapToInt(Integer::parseInt).toArray();
		} catch (Exception ex) {
			LOGGER.log(Level.WARNING, "Error recibiendo listado de ids", ex);
			throw new UVException("Lista de ids no válida");
		}
		List<Bolsa> bolsas = modelo.getBolsasByIds(ids);
				
		switch (estado) {
			case PARAM_BOLSA_ESTADO_BLOQUEAR:
				modelo.bloquearBolsas(bolsas);
				break;
			case PARAM_BOLSA_ESTADO_REVISION:
				modelo.ponerBolsasEnRevision(bolsas);
				break;
			case PARAM_BOLSA_ESTADO_BAREMACION:
				modelo.ponerBolsasEnBaremacion(bolsas);
				break;
			case PARAM_BOLSA_ESTADO_ALEGACION:
				modelo.ponerBolsasEnAlegaciones(bolsas);
				break;
			case PARAM_BOLSA_ESTADO_DESBLOQUEAR:
				modelo.desbloquearBolsas(bolsas);
				break;
			case PARAM_BOLSA_ESTADO_BAREMAR:
				modelo.baremarBolsas(bolsas);
				break;		
		}		
	}
	
	
	private void responseAjax(String nombreAccion, HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			switch (nombreAccion) {
				case ACCION_DATATABLE:
					datatable(out, request);
					break;				
				default:
					throw new UVException("Acción no válida " + nombreAccion);
			}				
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.toString());
			JSONObject json = new JSONObject();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
			try {
				json.put("error", e.getMessage());
				out.print(json);
			} catch (JSONException err) {
				err.printStackTrace();
			}				
		} finally {
			out.close();
		}
	}
	
	private void datatable(PrintWriter out, HttpServletRequest request) throws SQLException, UVException, IOException {		
		ModeloBolsa modelo = new ModeloBolsa();
		DataTable<Bolsa> dataTable = modelo.listaBolsaEmpleoDatatable(request.getParameterMap());		
		JSONObject json = new JSONObject();
		
		try {
			json.put("recordsTotal", dataTable.getRecordsTotal());
			json.put("pagesTotal", dataTable.getPagesTotal());
			json.put("data", dataTable.getData());
		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "Error creando json {0}", e);
			throw new UVException("Error creando json");
		}
				
        out.print(json);
        out.close();		
	}	
}
