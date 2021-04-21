package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.Date;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaConvocatorias;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoValidator;
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
//	public static final String PARAM_CONVOCATORIA_DESCRIPCION = "descripcion";
//	public static final String PARAM_CONVOCATORIA_FECHACIERRE = "fechaCierre";
//	public static final String PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO = "numBolsasMaximo";
//	public static final String PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE = "numMeritosPorBloque";
	
	// acciones
	public static final String ACCION_LISTAR_SOLICITUDES = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_CREAR_SOLICITUD = "crearSolicitud";
	
	// mensajes
	public static final String MENSAJE_ERROR_CONVOCATORIA_ID_REQUERIDA = "El id de la convocatoria es requerído";
	public static final String MENSAJE_ERROR_SOLICITUDES_ABIERTAS = "Ya existen solicitides abiertas";
	public static final String MENSAJE_ERROR_NO_EXISTE_CONVOCATORIA = "No existe la convocatoria";
	public static final String MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA = "Convocatoria no abierta";
	
//	public static final String MENSAJE_ERROR_DESCRIPCION_LARGA = "La descripción no puede ser superior a %d";
//	public static final String MENSAJE_ERROR_DESCRIPCION_VACIA = "La descripción no puede estar vacia";
//	public static final String MENSAJE_ERROR_FECHACIERRE_INCORRECTA = "La fecha de cierre no tiene el formato DD/MM/YYYY";
//	public static final String MENSAJE_ERROR_FECHACIERRE_REQUERIDA = "La fecha de cierre es requerida";
//	public static final String MENSAJE_ERROR_FECHACIERRE_MINIMA = "La fecha de cierre debe ser mayor que la fecha actual";
//	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_REQUERIDA = "El número de bolsas máximas es requerido";
//	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_INCORRECTA = "El número de bolsas máximas debe ser un entero";
//	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_MINIMO = "El número de bolsas máximas debe ser al menos una";
//	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_REQUERIDA = "El número de méritos por bloque es requerido";
//	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_INCORRECTA = "El número de méritos por bloque debe ser un entero";
//	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_MINIMO = "El número de méritos por bloque debe ser al menos uno";
//	public static final String MENSAJE_ERROR_CONVOCATORIAS_ABIERTAS = "Ya existen convocatorias abiertas.";
//	public static final String MENSAJE_INFO_CONVOCATORIAS_INSERTADA_CORRECTAMENTE = "Convocatoria insertada correctamente.";

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_SOL = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/missolicitudes/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/missolicitudes";
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");

		VistaSolicitudes bean = new VistaSolicitudes();
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());

		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_SOLICITUDES;
		}
					
		try {
			switch (nombreAccion) {
				case ACCION_LISTAR_SOLICITUDES:
					index(bean, datos, request, response);
					break;
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_CREAR_SOLICITUD:
					crearSolicitud(bean, datos, request, response);
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
				BolsaEmpleoDataTable<Solicitud> dataTable = modelo.listaSolicitudesDatatable(request.getParameterMap());
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
		
		BolsaEmpleoValidator validator = new BolsaEmpleoValidator(request);
		validator.addParamInteger(PARAM_CONVOCATORIA_ID);
		validator.addRule(PARAM_CONVOCATORIA_ID, "required", MENSAJE_ERROR_CONVOCATORIA_ID_REQUERIDA);
		
		if (!validator.isValid()) {
			for (String param : validator.getErrors().keySet()) {
				for (String paramError : validator.getErrors().get(param)) {
					bean.getMensajesDeError().add(paramError);
				}
			}				
		} else {
			Convocatoria convocatoria = modeloConvocatoria.getConvocatoriaById(validator.getValueInteger(PARAM_CONVOCATORIA_ID));
			if (convocatoria == null) {
				bean.getMensajesDeError().add(MENSAJE_ERROR_NO_EXISTE_CONVOCATORIA);				
			} else if (!convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA)) {
				bean.getMensajesDeError().add(MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);				
			} else if (modeloSolicitud.haySolicitudAbiertaParaConvocatoria(convocatoria)) {
				bean.getMensajesDeError().add(MENSAJE_ERROR_SOLICITUDES_ABIERTAS);				
			} else {
				// creamos solicitud
				Solicitud solicitud = modeloSolicitud.nuevaSolicitud(convocatoria);
					
				bean.setSolicitud(solicitud);
				bean.setVista(RUTA_BEP_SOL + "paso1.jsp");
			}
		}
	}
}
