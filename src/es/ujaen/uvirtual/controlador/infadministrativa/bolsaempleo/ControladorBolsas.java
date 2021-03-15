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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEstadoBolsas;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
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
				"/srv/en/informacionadministrativa/bolsaempleo/bolsas"
		})
public class ControladorBolsas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorBolsas.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	
	// acciones
	public static final String ACCION_LISTAR_BOLSAS = "listar";
	public static final String ACCION_DATATABLE = "datatable";

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
		
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/bolsas.jsp");
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_BOLSAS;
		}
		try {
			switch (nombreAccion) {
				case ACCION_LISTAR_BOLSAS:
					listadoBolsasEmpleo(bean);
					break;
				case ACCION_DATATABLE:
					datatable(request, response);
					return;
				default:
					listadoBolsasEmpleo(bean);
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
			datos.getFicherosJS().add("/js/bolsa-empleo.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	/** Listado de bolsas de empleo y su estado.
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException error al cargar las bolsas
	 */
	private void listadoBolsasEmpleo(VistaEstadoBolsas bean) throws SQLException, UVException {
		ModeloBolsa modelo = new ModeloBolsa();
		List<Bolsa> bolsas = modelo.listaBolsaEmpleo();
		bean.setBolsasEmpleo(bolsas);
	}
	
	private void datatable(HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBolsa modelo = new ModeloBolsa();
		List<Bolsa> bolsas = modelo.listaBolsaEmpleo();
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		
		try {
			json.put("recordsTotal", bolsas.size());
			json.put("recordsFiltered", bolsas.size());
			json.put("data", bolsas);
		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "Error creando json {0}", e);
			throw new UVException("Error creando json");
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
        out.print(json);
        out.close();		
	}
}
