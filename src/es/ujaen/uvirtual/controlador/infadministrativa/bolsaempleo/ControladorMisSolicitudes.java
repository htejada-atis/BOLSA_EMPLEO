package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloArea;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Listado de bolsas y su estado.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.missolicitudes", 
	description = "Gestión de mis solicitudes", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/missolicitudes", 
			"/srv/en/informacionadministrativa/bolsaempleo/missolicitudes",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/missolicitudes",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/missolicitudes"
})
public class ControladorMisSolicitudes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisSolicitudes.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_ENVIAR = "enviar"; 
	public static final String PARAM_CONVOCATORIA_ID = "idConvocatoria";
	public static final String PARAM_SOLICITUD_ID = "idSolicitud";
	public static final String PARAM_BOLSAS = "bolsas";
	
	// acciones
	public static final String ACCION_LISTAR_SOLICITUDES = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DATATABLE_AREAS = "datatableareas";
	public static final String ACCION_CREAR_SOLICITUD = "crearSolicitud";
	public static final String ACCION_CONSULTAR_SOLICITUD = "consultarSolicitud";
	public static final String ACCION_SELECCIONAR_BOLSAS = "seleccionarbolsas";
	public static final String ACCION_DATATABLE_BOLSAS_SELECCIONADAS = "datatableBolsasSolicitud";
	
	// mensajes
	public static final String MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS = "No hay bolsas seleccionadas válidas";
	public static final String MENSAJE_ERROR_CONVOCATORIA_ID_REQUERIDA = "El id de la convocatoria es requerído";
	public static final String MENSAJE_ERROR_SOLICITUD_ID_REQUERIDO = "El id de la solicitud es requerído";
		
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_SOL = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/missolicitudes/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/missolicitudes";
	
	private UsuarioBolsaEmpleo usuario = null;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");		

		VistaSolicitudes bean = new VistaSolicitudes();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_SOLICITUDES;
		}
					
		try {
			this.usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioLogeado(datos);
			
			switch (nombreAccion) {
				case ACCION_LISTAR_SOLICITUDES:
					index(bean, datos, request, response);
					break;
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_AREAS:
					listadoAreas(bean, datos, request, response);
					break;
				case ACCION_CREAR_SOLICITUD:
					crearSolicitud(bean, datos, request, response);
					break;	
				case ACCION_CONSULTAR_SOLICITUD:
					consultarSolicitud(bean, datos, request, response);
					break;
				case ACCION_SELECCIONAR_BOLSAS:
					seleccionarBolsas(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_BOLSAS_SELECCIONADAS:
					listadoBolsasSolicitud(bean, datos, request, response);
					break;
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

	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void index(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) {
		bean.setVista(RUTA_BEP_SOL + "indexSolicitudes.jsp");
	}
		
	private void listado(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia(); 
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Solicitud> dataTable = modelo.listaSolicitudesDatatable(usuario, request.getParameterMap());
				bean.setDatatableSolicitudes(dataTable);
				
				Gson gson = new GsonBuilder().setDateFormat("dd/M/yyyy").
						setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();				
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void crearSolicitud(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia();
		
		bean.setVista(RUTA_BEP_SOL + "indexSolicitudes.jsp");
		
		Convocatoria convocatoria = modeloConvocatoria.getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID)));		
		Solicitud solicitud = modeloSolicitud.nuevaSolicitud(usuario, convocatoria);
				
		bean.setSolicitud(solicitud);
		bean.setVista(RUTA_BEP_SOL + "paso1.jsp");					
	}
	
	private void consultarSolicitud(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = new ModeloSolicitud();
		
		bean.setVista(RUTA_BEP_SOL + "indexSolicitudes.jsp");
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		if (solicitud.getConvocatoria().getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
			throw new UVException(ModeloSolicitud.MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);
		}
		
		if (solicitud.getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) {
			bean.setVista(RUTA_BEP_SOL + "paso1.jsp");
		} else {
			bean.setVista(RUTA_BEP_SOL + "paso3.jsp");
		}
	}
	
	private void seleccionarBolsas(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
		bean.setVista(RUTA_BEP_SOL + "paso1.jsp");
		
		Gson gson = new GsonBuilder().create();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		List<String> idBolsas = null;
			
		// parseamos json bolsas
		try {			
			idBolsas = gson.fromJson(request.getParameter(PARAM_BOLSAS), new TypeToken<List<String>>() { }.getType());					
		} catch (Exception e) {
			throw new UVException(MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS);
		}
		
		// comprobamos si son bolsas válidas
		List<Bolsa> bolsas = new ArrayList<Bolsa>();
		for (String idBolsa : idBolsas) {
			Bolsa bolsa = modeloBolsa.getBolsaById(Integer.parseInt(idBolsa));
			
			if (!bolsa.getBaremable()) {
				throw new UVException("Ha selecciona una bolsa no baremable");
			}
			
			if (modeloArea.isUsuarioExcluidoBolsa(usuario, bolsa.getArea())) {
				throw new UVException("Usuario excluido de la bolsa");
			}
							
			bolsas.add(bolsa);
		}
		
		if (bolsas.size() > solicitud.getConvocatoria().getNumBolsasMaximo()) {
			throw new UVException("Número de bolsas seleccionada no valido");
		}
		
		// añadimos las bolsas a la solicitud
		modeloSolicitud.asignarBolsasASolicitud(solicitud, bolsas);
		
		bean.setVista(RUTA_BEP_SOL + "paso2.jsp");
	}
	
	private void listadoAreas(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
		ModeloArea modelo = ModeloArea.obtenerInstancia();
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Usuario usuArcos = datos.getUsuario();
				ModeloUsuarioBolsaEmpleo modeloUsuarioBolsaEmpleo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
				Integer idUsuario = modeloUsuarioBolsaEmpleo.listaUsuario(usuArcos.getUid()).getCodNum();
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaAreaSolicitudDatatable(request.getParameterMap(), idUsuario);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatableAreas(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void listadoBolsasSolicitud(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Solicitud solicitud = modelo.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = modelo.listaBolsasSolicitudesDatatable(solicitud, request.getParameterMap());
				bean.setDatatableAreas(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();				
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
}
