package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEstadoBolsas;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
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
	public static final String ACCION_LISTAR_BOLSAS = "listar";
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

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
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
		
		VistaEstadoBolsas bean = new VistaEstadoBolsas();		
		Usuario usuario = datos.getUsuario();
		ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());

		try {
			anonimo = !modelo.checkUser(datos);
		} catch (SQLException | UVException e) {
			bean.getMensajesDeError().add(e.getMessage());
		}
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));		
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_BOLSAS;
		}
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/bolsas/index.jsp");
		
		try {
			switch (nombreAccion) {
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_BOLSA:
					accionSobreBolsas(bean, datos, request);					
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
		
	private void listado(VistaEstadoBolsas bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloBolsa modelo = new ModeloBolsa();
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				DataTable<Bolsa> dataTable = modelo.listaBolsaEmpleoDatatable(request.getParameterMap());
				bean.setDatatableBolsas(dataTable);
				
				Gson gson = new GsonBuilder().setExclusionStrategies(DataTable.GSONEXCLUSIONSTRATEGY).create();				
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void accionSobreBolsas(VistaEstadoBolsas bean, UVDatos datos, HttpServletRequest request) throws UVException, SQLException {
		ModeloBolsa modelo = new ModeloBolsa();
		
		String nombreAccionBolsa = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION_BOLSA));
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BOLSAS_SELECCIONADAS));
		int[] selected;
		List<Bolsa> bolsas;
		
		try {
			selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
			bolsas = modelo.getBolsasByIds(selected);
		} catch (Exception ex) {
			throw new UVException(MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS);
		}
				
		switch (nombreAccionBolsa) {
		case ACCION_BOLSAS_BLOQUEAR:
			modelo.bloquearBolsas(bolsas);
			break;
		case ACCION_BOLSAS_REVISION:
			modelo.ponerBolsasEnRevision(bolsas);
			break;
		case ACCION_BOLSAS_BAREMACION:
			modelo.ponerBolsasEnBaremacion(bolsas);
			break;
		case ACCION_BOLSAS_ALEGACION:
			modelo.ponerBolsasEnAlegaciones(bolsas);
			break;
		case ACCION_BOLSAS_DESBLOQUEAR:
			modelo.desbloquearBolsas(bolsas);
			break;
		case ACCION_BOLSAS_BAREMAR:
			modelo.baremarBolsas(bolsas);
			break;
		default:
			bean.getMensajesDeError().add(MENSAJE_ERROR_ACCION_BOLSA_NO_VALIDA);
			return;
		}
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_BOLSA_MODIFICADA_CORRECTAMENTE);
	}
}
