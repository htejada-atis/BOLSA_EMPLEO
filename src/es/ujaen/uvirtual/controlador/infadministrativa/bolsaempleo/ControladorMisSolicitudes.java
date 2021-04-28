package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;
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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BolsaCandidato;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BolsaSolicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.MeritoSolicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloArea;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloMerito;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Table;

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
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_BOLSAS = "bolsas";
	public static final String PARAM_CONVOCATORIA_ID = "idConvocatoria";
	public static final String PARAM_MERITO = "merito";
	public static final String PARAM_SOLICITUD_ID = "idSolicitud";
	
	// acciones
	public static final String ACCION_BOLSA_SELECCIONADA = "bolsaseleccionada";
	public static final String ACCION_CONFIRMAR_SOLICITUD = "confirmarsolicitud";
	public static final String ACCION_CONSULTAR_SOLICITUD = "consultarSolicitud";
	public static final String ACCION_CREAR_SOLICITUD = "crearSolicitud";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DATATABLE_AREAS = "datatableareas";
	public static final String ACCION_DATATABLE_BOLSAS_SELECCIONADAS = "datatableBolsasSolicitud";
	public static final String ACCION_DATATABLE_MERITOS_BOLSA = "datatablemeritosbolsa";
	public static final String ACCION_LISTAR_BOLSAS_SOLICITUD = "listarbolsassolicitud";
	public static final String ACCION_LISTAR_SOLICITUDES = "listar";
	public static final String ACCION_MERITO_DESELECCIONADO = "meritodeseleccionado";
	public static final String ACCION_MERITO_SELECCIONADO = "meritoseleccionado";
	public static final String ACCION_RESUMEN_SOLICITUD = "resumensolicitud";
	public static final String ACCION_SELECCIONAR_BOLSAS = "seleccionarbolsas";
	public static final String ACCION_DESCARGAR_PDF = "descargarpdf";
	
	// mensajes
	public static final String MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS = "No hay bolsas seleccionadas válidas";
	public static final String MENSAJE_ERROR_BORRAR_BOLSA = "No puede deseleccionar ésta bolsa, tiene méritos asociados";
	public static final String MENSAJE_ERROR_CONVOCATORIA_ID_REQUERIDA = "El id de la convocatoria es requerído";
	public static final String MENSAJE_ERROR_NUMERO_MAXIMO_MERITOS_BLOQUE = "Se ha alcanzado el número máximo de méritos por bloque";
	public static final String MENSAJE_ERROR_SIN_MERITOS = "Dene incluir al menos un mérito en una bolsa para continuar";
	public static final String MENSAJE_ERROR_SOLICITUD_ID_REQUERIDO = "El id de la solicitud es requerído";
	
	public static final String MENSAJE_EXITO_SOLICITUD_CONFIRMADA = "La solicitud ha sido confirmada correctamente";
		
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_SOL = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/missolicitudes/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/missolicitudes";
	
	//pdf
	public static final Integer PDF_ANCHO = 4;
	public static final Integer PDF_ALTO = 4;
	public static final Integer PDF_FORMATO = 4;
	public static final Integer PDF_TABLE_COLUMNS = 4;

	
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
				case ACCION_BOLSA_SELECCIONADA:
					seleccionarBolsa(bean, datos, request, response);
					break;
				case ACCION_CONFIRMAR_SOLICITUD:
					confirmarSolicitud(bean, request);
					break;
				case ACCION_CONSULTAR_SOLICITUD:
					consultarSolicitud(bean, datos, request, response);
					break;
				case ACCION_CREAR_SOLICITUD:
					crearSolicitud(bean, datos, request, response);
					break;
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_AREAS:
					listadoAreas(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_BOLSAS_SELECCIONADAS:
					listadoBolsasSolicitud(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_MERITOS_BOLSA:
					listadoMeritosCandidato(bean, datos, request, response);
					break;
				case ACCION_LISTAR_BOLSAS_SOLICITUD:
					listaBolsasSolicitud(bean, request);
					break;
				case ACCION_LISTAR_SOLICITUDES:
					index(bean, datos, request, response);
					break;
				case ACCION_MERITO_DESELECCIONADO:
					seleccionarMerito(bean, datos, request, response, false);
					break;
				case ACCION_MERITO_SELECCIONADO:
					seleccionarMerito(bean, datos, request, response, true);
					break;
				case ACCION_RESUMEN_SOLICITUD:
					resumenSolicitud(bean, datos, request, response);
					break;
				case ACCION_SELECCIONAR_BOLSAS:
					seleccionarBolsas(bean, datos, request, response);
					break;
				case ACCION_DESCARGAR_PDF:
					descargarPDF(bean, datos, response);
					break;
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
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
	
	/**
	 * Lista de solicitudes .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
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
	
	/** 
	 * Agrega la vista del paso 1 y le añade la lista de bolsas de la solicitud .
	 * @param bean .
	 * @param solicitud .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void listaBolsas(VistaSolicitudes bean, Solicitud solicitud) throws UVException, SQLException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
		bean.setVista(RUTA_BEP_SOL + "paso1.jsp");
		
		List<Bolsa> listaBolsas = modelo.getBolsasSolicitud(solicitud);
		bean.setListaBolsas(listaBolsas);
	}
	
	/** 
	 * Agrega la vista del paso 2 y le añade la solicitud a la vista.
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void listaBolsasSolicitud(VistaSolicitudes bean, HttpServletRequest request) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		
		bean.setVista(RUTA_BEP_SOL + "paso2.jsp");
		bean.setSolicitud(solicitud);
	}
	
	/** 
	 * Crea la solicitud .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void crearSolicitud(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia();
		
		bean.setVista(RUTA_BEP_SOL + "indexSolicitudes.jsp");
		
		Convocatoria convocatoria = modeloConvocatoria.getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID)));		
		Solicitud solicitud = modeloSolicitud.nuevaSolicitud(usuario, convocatoria);
				
		bean.setSolicitud(solicitud);
		listaBolsas(bean, solicitud);
	}
	
	/** 
	 * Consultar solicitud .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void consultarSolicitud(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = new ModeloSolicitud();
		
		bean.setVista(RUTA_BEP_SOL + "indexSolicitudes.jsp");
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		if (solicitud.getConvocatoria().getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
			throw new UVException(ModeloSolicitud.MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);
		}
		
		if (solicitud.getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) {
			listaBolsas(bean, solicitud);
		} else {
			List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
			bean.setListaBolsasSolicitud(listaBolsas);
			bean.setVista(RUTA_BEP_SOL + "paso3.jsp");
		}
	}
	
	/**
	 * Seleccionar bolsas para la solicitud .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarBolsas(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, SQLIntegrityConstraintViolationException {
		bean.setVista(RUTA_BEP_SOL + "paso1.jsp");
		
		Gson gson = new GsonBuilder().create();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		listaBolsas(bean, solicitud);
		
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
		
		
		try {
			// añadimos las bolsas a la solicitud
			modeloSolicitud.asignarBolsasASolicitud(solicitud, bolsas);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new SQLIntegrityConstraintViolationException(MENSAJE_ERROR_BORRAR_BOLSA);
		}
		
		bean.setVista(RUTA_BEP_SOL + "paso2.jsp");
	}
	
	/**
	 * Selecciona una bolsa para mostrar los méritos .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarBolsa(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
		bean.setVista(RUTA_BEP_SOL + "paso2.jsp");
		
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloMerito modeloMerito = ModeloMerito.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Bolsa area = modeloBolsa.getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
		bean.setArea(area);
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		List<Merito> listaMeritos = modeloMerito.getMeritosSolicitudBolsa(solicitud, area);
		bean.setListaMeritos(listaMeritos);
	}
	
	/** 
	 * Selecciona un mérito para agregarlo a una bolsa .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param seleccionado .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void seleccionarMerito(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, Boolean seleccionado)
			throws IOException, UVException, SQLException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloMerito modeloMerito = ModeloMerito.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Bolsa area = modeloBolsa.getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
				bean.setArea(area);
				Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
				bean.setSolicitud(solicitud);
				Merito merito = modeloMerito.listaMerito(Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO)));
				
				if (merito == null) {
					throw new UVException("merito no puede ser nulo");
				}
				
				if (solicitud == null) {
					throw new UVException("solicitud no puede ser nula");
				}
				
				if (seleccionado) {
					// comprobamos que el total de méritos por bloque de la solicitud sea menor que el permitido por la convocatoria
					Integer totalMeritos = modeloSolicitud.obtenerTotalMeritosPorBloqueSolicitud(solicitud, merito);
					
					if (totalMeritos >= solicitud.getConvocatoria().getNumMeritosPorBloque()) {
						throw new Exception(MENSAJE_ERROR_NUMERO_MAXIMO_MERITOS_BLOQUE);
					}
					
					modeloSolicitud.asignarMeritosASolicitudBolsa(solicitud, area, merito);
				} else {
					modeloSolicitud.borrarMeritoDeSolicitudBolsa(solicitud, area, merito);
				}
				
				CodigoDescripcion mensaje = new CodigoDescripcion("ok", seleccionado ? "mérito agregado a la bolsa" : "mérito quitado de la bolsa");
				writer.write(new Gson().toJson(mensaje));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	/** Resumen de la solicitud .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void resumenSolicitud(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException, UVException {
		bean.setVista(RUTA_BEP_SOL + "paso2.jsp");
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		// comprobamos que el total de méritos sea mayor que 0
		Integer totalMeritos = modeloSolicitud.obtenerTotalMeritosSolicitud(solicitud);
		
		if (!(totalMeritos > 0)) {
			throw new UVException(MENSAJE_ERROR_SIN_MERITOS);
		}
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
		bean.setListaBolsasSolicitud(listaBolsas);
		
		bean.setVista(RUTA_BEP_SOL + "paso3.jsp");
	}
	
	/**
	 * El usuario confirma la solicitud .
	 * @param bean .
	 * @param request .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void confirmarSolicitud(VistaSolicitudes bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(RUTA_BEP_SOL + "paso3.jsp");
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
		bean.setListaBolsasSolicitud(listaBolsas);
		
		solicitud.setEstado(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
		solicitud.setFechaConfirmacion(new Date());
		
		modeloSolicitud.confirmacionSolicitud(solicitud);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_SOLICITUD_CONFIRMADA);
	}
	
	/**
	 * Lista de áreas datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoAreas(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
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
				BolsaEmpleoDataTable<BolsaCandidato> dataTable = modelo.listaAreaSolicitudDatatable(request.getParameterMap(), idUsuario);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDataTableBolsasCandidato(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * Lista de bolsas de la solicitud datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws IOException .
	 */
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
				BolsaEmpleoDataTable<BolsaSolicitud> dataTable = modelo.listaBolsasSolicitudesDatatable(solicitud, request.getParameterMap());
				bean.setDatatableBolsasSolicitud(dataTable);
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
	
	/** lista de méritos del candidato datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void listadoMeritosCandidato(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException, UVException {
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Usuario usuArcos = datos.getUsuario();
				ModeloUsuarioBolsaEmpleo modeloUsuarioBolsaEmpleo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
				Integer idUsuario = modeloUsuarioBolsaEmpleo.listaUsuario(usuArcos.getUid()).getCodNum();
				BolsaEmpleoDataTable<MeritoSolicitud> dataTable = modelo.listaMeritosSolicitudDatatable(request.getParameterMap(), idUsuario);
				bean.setDataTableMeritos(dataTable);
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
	
	
	
	
	/** exportar .
	 * @param response .
	 * @param bean .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	  public void exportar(VistaSolicitudes bean, HttpServletResponse response) {
		  try {
			    Document document = new Document();

			    //create a PDF writer instance and pass output stream
			    PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

			    document.open();
			    document.addAuthor(this.usuario.getNombre() + this.usuario.getPrimerApellido() + this.usuario.getSegundoApellido());
			    document.addTitle("Solicitud_" + bean.getSolicitud().getConvocatoria().getDescripcion());
			    document.addCreationDate();


//			    document.add(new Paragraph("Usuario : " + this.usuario.getNombre() + this.usuario.getPrimerApellido() + this.usuario.getSegundoApellido()));
//			    document.add(new Paragraph("Convocatoria : " + bean.getSolicitud().getConvocatoria().getDescripcion()));
//			    document.add(new Paragraph("Fecha cierre de solicitud : " + bean.getSolicitud()));
			    
//			    bean.getListaBolsas().size()

			    document.newPage();
			    
				Table table = new Table(PDF_TABLE_COLUMNS, 2); // 4 columns, 2 rows
				table.addCell("0.0");
				table.addCell("0.1");
				table.addCell("1.0");
				table.addCell("1.1");
				table.addCell("2.2");
				table.addCell("2.2");
				table.addCell("2.2");
				table.addCell("2.2");
				document.add(table);
				document.add(new Paragraph("converted to PdfPTable:"));
				table.setConvert2pdfptable(true);
				document.add(table);
				
				
				Table table2 = new Table(3);
				table2.setBorderWidth(1);
				table2.setBorderColor(new Color(0, 0, 255));
				table2.setPadding(5);
				table2.setSpacing(5);
				 Cell cell = new Cell("header");
				 cell.setHeader(true);
				 cell.setColspan(3);
				 table2.addCell(cell);
				 table2.endHeaders();
				 cell = new Cell("example cell with colspan 1 and rowspan 2");
				 cell.setRowspan(2);
				 cell.setBorderColor(new Color(255, 0, 0));
				 table2.addCell(cell);
				 table2.addCell("1.1");
				 table2.addCell("2.1");
				 table2.addCell("1.2");
				 table2.addCell("2.2");
				 table2.addCell("cell test1");
				 cell = new Cell("big cell");
				 cell.setRowspan(2);
				 cell.setColspan(2);
				 table2.addCell(cell);
				 table2.addCell("cell test2");
				
				
					document.add(table2);
					document.add(new Paragraph("converted to PdfPTable:"));
					table2.setConvert2pdfptable(true);
					document.add(table2);
				
				
				
				
				
				
				
				

			    document.close();
			    
			 } catch (Exception exp) {
			    System.out.println(exp.getMessage());
			 }
	  }
	
	
	/** descarga de pdf .
	 * @param response .
	 * @param datos .
	 * @param bean .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	  public void descargarPDF(VistaSolicitudes bean, UVDatos datos, HttpServletResponse response) {
		  response.setContentType("application/pdf");
	      datos.setRespuestaEnviada(true);
	      exportar(bean, response);    
	  }
	
}
