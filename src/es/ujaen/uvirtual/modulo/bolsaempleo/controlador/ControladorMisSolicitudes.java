package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaSolicitudes;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Table;

/**
 * Clase controlador para las solicitudes de un candidato de bolsa de empleo.
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
	public static final String PARAM_MERITO_ID = "merito";
	public static final String PARAM_SOLICITUD_ID = "idSolicitud";
	public static final String PARAM_AFINIDAD_ID = "idAfinidad";
	public static final String PARAM_MERITO_SOLICITUD_ID = "idMeritoSolicitud";
	public static final String PARAM_AFINIDADES = "afinidades";
	
	// acciones solicitudes
	public static final String ACCION_CONSULTAR_SOLICITUD = "consultarSolicitud";
	public static final String ACCION_CREAR_SOLICITUD = "crearSolicitud";
	public static final String ACCION_DATATABLE_SOLICITUDES = "datatablesolicitudes";
	public static final String ACCION_INDEX = "listar";
	
	// acciones paso 1: Selección de áreas
	public static final String ACCION_DATATABLE_AREAS = "datatableareas";
	public static final String ACCION_SELECCIONAR_BOLSAS = "seleccionarbolsas";
	
	// acciones paso 2: Asignación de méritos a áreas
	public static final String ACCION_BOLSA_SELECCIONADA = "bolsaseleccionada";
	public static final String ACCION_DATATABLE_BOLSAS_SELECCIONADAS = "datatableBolsasSolicitud";
	public static final String ACCION_DATATABLE_MERITOS_BOLSA = "datatablemeritosbolsa";
	public static final String ACCION_LISTAR_BOLSAS_SOLICITUD = "listarbolsassolicitud";
	public static final String ACCION_MERITO_DESELECCIONADO = "meritodeseleccionado";
	public static final String ACCION_MERITO_SELECCIONADO = "meritoseleccionado";
	public static final String ACCION_MERITO_AFINIDAD_INDIVIDUALIZADO = "afinidadseleccionadaindividualizado";
	public static final String ACCION_MERITO_AFINIDAD_NOINDIVIDUALIZADO = "afinidadseleccionadanoindividualizado";
	
	// acciones paso 3: Confirmar Solicitud
	public static final String ACCION_CONFIRMAR_SOLICITUD = "confirmarsolicitud";
	public static final String ACCION_RESUMEN_SOLICITUD = "resumensolicitud";
	public static final String ACCION_DESCARGAR_PDF = "descargarpdf";
	
	// mensajes
	public static final String MENSAJE_AREA_SIN_MERITOS = "No hay méritos asignados a éste área.";
	public static final String MENSAJE_ENVIADO = "enviadomissolicitudes";
	public static final String MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS = "No hay bolsas seleccionadas válidas";
	public static final String MENSAJE_ERROR_BORRAR_BOLSA = "No puede deseleccionar ésta bolsa, tiene méritos asociados";
	public static final String MENSAJE_ERROR_CONVOCATORIA_ID_REQUERIDA = "El id de la convocatoria es requerído";
	public static final String MENSAJE_ERROR_NUMERO_MAXIMO_MERITOS_BLOQUE = "Se ha alcanzado el número máximo de méritos por bloque";
	public static final String MENSAJE_ERROR_SIN_MERITOS = "Dene incluir al menos un mérito en una bolsa para continuar";
	public static final String MENSAJE_ERROR_SOLICITUD_ID_REQUERIDO = "El id de la solicitud es requerído";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_MERITO_NO_NULO = "Merito no puede ser nulo";
	public static final String MENSAJE_ERROR_SOLICITUD_NO_NULO = "Solicitud no puede ser nula";
		
	public static final String MENSAJE_EXITO_SOLICITUD_CONFIRMADA = "La solicitud ha sido confirmada correctamente";
	
		
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_SOL = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/missolicitudes/";
	public static final String JSP_INDEX = RUTA_BEP_SOL + "indexSolicitudes.jsp";
	public static final String JSP_PASO2 = RUTA_BEP_SOL + "paso2.jsp";
	public static final String JSP_PASO3 = RUTA_BEP_SOL + "paso3.jsp";
		
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/missolicitudes";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	//pdf
	public static final int PDF_ANCHO = 4;
	public static final int PDF_ALTO = 4;
	public static final int PDF_FORMATO = 4;
	public static final int PDF_TABLE_COLUMNS = 4;
	public static final int PDF_TABLE_PADDING = 5;
	public static final int SIZE_40 = 40;
	public static final int SIZE_100 = 100;	
	public static final int COLOR_51 = 51;
	public static final int COLOR_153 = 153;
	public static final int COLOR_185 = 185;
	public static final int COLOR_201 = 201;
	public static final int COLOR_241 = 241;
	public static final int COLOR_254 = 254;
	public static final int COLSPAN_4 = 4;
	
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
			nombreAccion = ACCION_INDEX;
		}

		try {
			init(bean, datos);
			switch (nombreAccion) {
				case ACCION_CONSULTAR_SOLICITUD:
				case ACCION_CREAR_SOLICITUD:
				case ACCION_DATATABLE_SOLICITUDES:
				case ACCION_INDEX:
					accionesSolicitudes(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DATATABLE_AREAS:
				case ACCION_SELECCIONAR_BOLSAS:
					accionesPaso1(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_BOLSA_SELECCIONADA:
				case ACCION_DATATABLE_BOLSAS_SELECCIONADAS:
				case ACCION_DATATABLE_MERITOS_BOLSA:
				case ACCION_LISTAR_BOLSAS_SOLICITUD:
				case ACCION_MERITO_DESELECCIONADO:
				case ACCION_MERITO_SELECCIONADO:
				case ACCION_MERITO_AFINIDAD_INDIVIDUALIZADO:
				case ACCION_MERITO_AFINIDAD_NOINDIVIDUALIZADO:
					accionesPaso2(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_CONFIRMAR_SOLICITUD:
				case ACCION_RESUMEN_SOLICITUD:
				case ACCION_DESCARGAR_PDF:
					accionesPaso3(bean, datos, request, response, nombreAccion);
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
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
		}
	}
	
	private void init(VistaSolicitudes bean, UVDatos datos) throws SQLException, UVException {
		bean.setVista(JSP_INDEX);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
		bean.setCandidato(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioCandidato(datos));
	}
	
	private void errorFatal(VistaSolicitudes bean, String mensaje) {
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SOLICITUDES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesSolicitudes(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_CONSULTAR_SOLICITUD:
				consultarSolicitud(bean, request);
				break;	
			case ACCION_CREAR_SOLICITUD:
				crearSolicitud(bean, request);
				break;
			case ACCION_DATATABLE_SOLICITUDES:
				listadoSolicitudes(bean, datos, request, response);
				break;
			case ACCION_INDEX:
				bean.setVista(JSP_INDEX);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	/** 
	 * Crea la solicitud .
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void crearSolicitud(VistaSolicitudes bean, HttpServletRequest request) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia();
		
		bean.setVista(JSP_INDEX);
		
		Convocatoria convocatoria = modeloConvocatoria.getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID)));
				
		Solicitud solicitud = modeloSolicitud.nuevaSolicitud(bean.getCandidato(), convocatoria);
				
		bean.setSolicitud(solicitud);
		listaBolsas(bean, solicitud);
	}
	
	/** 
	 * Consultar solicitud .
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void consultarSolicitud(VistaSolicitudes bean, HttpServletRequest request) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = new ModeloSolicitud();
		
		bean.setVista(JSP_INDEX);
		
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
			bean.setVista(JSP_PASO3);
		}
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
	private void listadoSolicitudes(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia(); 
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Solicitud> dataTable = modelo.listaSolicitudesDatatable(bean.getCandidato(), request.getParameterMap());
				bean.setDatatableSolicitudes(dataTable);
				
				Gson gson = new GsonBuilder().setDateFormat("dd/M/yyyy").
						setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();				
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PASO 1: SELECCIÓN DE ÁREAS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesPaso1(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_DATATABLE_AREAS:
				listadoAreas(bean, datos, request, response);
				break;
			case ACCION_SELECCIONAR_BOLSAS:
				seleccionarBolsas(bean, request);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	/**
	 * Seleccionar bolsas para la solicitud .
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarBolsas(VistaSolicitudes bean, HttpServletRequest request)
			throws UVException, SQLException {
		bean.setVista(RUTA_BEP_SOL + "paso1.jsp");
		
		Gson gson = new GsonBuilder().create();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
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
		List<Bolsa> bolsas = new ArrayList<>();
		for (String idBolsa : idBolsas) {
			Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(Integer.parseInt(idBolsa));
			
			if (Boolean.FALSE.equals(bolsa.getBaremable())) {
				throw new UVException("Ha selecciona una bolsa no baremable");
			}
			
			if (ModeloArea.obtenerInstancia().isUsuarioExcluidoBolsa(bean.getCandidato(), bolsa.getArea())) {
				throw new UVException("Usuario excluido de la bolsa");
			}
							
			bolsas.add(bolsa);
		}
		
		if (bolsas.size() > solicitud.getConvocatoria().getNumBolsasMaximo()) {
			throw new UVException("Número de bolsas seleccionada no valido");
		}
		
		// añadimos las bolsas a la solicitud
		modeloSolicitud.asignarBolsasASolicitud(solicitud, bolsas);
				
		bean.setVista(JSP_PASO2);
		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
		bean.setListaTipoAfinidades(ModeloAfinidad.obtenerInstancia().getTiposAfinidad());
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
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaCandidato> dataTable = modelo.listaAreaSolicitudDatatable(request.getParameterMap(), bean.getCandidato().getCodNum());
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDataTableBolsasCandidato(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PASO 2: ASIGNACIÓN DE MÉRITOS A ÁREAS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesPaso2(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_BOLSA_SELECCIONADA:
				seleccionarBolsa(bean, request);
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
			case ACCION_MERITO_DESELECCIONADO:
				seleccionarMerito(bean, request, false);
				break;
			case ACCION_MERITO_SELECCIONADO:
				seleccionarMerito(bean, request, true);
				break;
			case ACCION_MERITO_AFINIDAD_INDIVIDUALIZADO:
				seleccionarAfinidadIndividualizado(bean, request);
				break;
			case ACCION_MERITO_AFINIDAD_NOINDIVIDUALIZADO:
				seleccionarAfinidadNoIndividualizado(bean, request);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	/**
	 * Selecciona una bolsa para mostrar los méritos .
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarBolsa(VistaSolicitudes bean, HttpServletRequest request) throws UVException, SQLException {
		bean.setVista(JSP_PASO2);
		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
		bean.setListaTipoAfinidades(ModeloAfinidad.obtenerInstancia().getTiposAfinidad());
		
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Bolsa area = modeloBolsa.getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
		bean.setArea(area);
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		List<MeritoSolicitud> listaMeritos = modeloSolicitud.getMeritosSolicitudBolsa(solicitud, area);
		bean.setListaMeritosSolicitud(listaMeritos);
	}
	
	/** 
	 * Selecciona un mérito para agregarlo o quitarlo de una bolsa .
	 * @param bean .
	 * @param request .
	 * @param seleccionado .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void seleccionarMerito(VistaSolicitudes bean, HttpServletRequest request, Boolean seleccionado)
			throws UVException, SQLException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloMerito modeloMerito = ModeloMerito.obtenerInstancia();
					
		Bolsa area = modeloBolsa.getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
		bean.setArea(area);

		Merito merito = modeloMerito.listaMerito(Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_ID)));
		bean.setMerito(merito);

		if (merito == null) {
			throw new UVException(MENSAJE_ERROR_MERITO_NO_NULO);
		}

		Solicitud solicitud = this.listaBolsasSolicitud(bean, request);
		
		if (solicitud == null) {
			throw new UVException(MENSAJE_ERROR_SOLICITUD_NO_NULO);
		}

		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		if (Boolean.TRUE.equals(seleccionado)) {
			// comprobamos que el total de méritos por bloque de la solicitud sea menor que
			// el permitido por la convocatoria
			Integer totalMeritos = modeloSolicitud.obtenerTotalMeritosPorBloqueSolicitud(solicitud, merito);

			if (totalMeritos >= solicitud.getConvocatoria().getNumMeritosPorBloque()) {
				throw new UVException(MENSAJE_ERROR_NUMERO_MAXIMO_MERITOS_BLOQUE);
			}
			
			modeloSolicitud.asignarMeritosASolicitudBolsa(solicitud, area, merito);
		} else {
			modeloSolicitud.borrarMeritoDeSolicitudBolsa(solicitud, area, merito);
		}

		this.seleccionarBolsa(bean, request);
	}
	
	/**
	 * Establece la afinidad del merito dentro del area que ha seleccionado el candidato, del tipo individualizado.
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarAfinidadIndividualizado(VistaSolicitudes bean, HttpServletRequest request) 
			throws SQLException, UVException {			
		Bolsa area = ModeloBolsa.obtenerInstancia().getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
		bean.setArea(area);

		Merito merito = ModeloMerito.obtenerInstancia().listaMerito(Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_ID)));
		bean.setMerito(merito);
				
		if (merito == null) {
			throw new UVException(MENSAJE_ERROR_MERITO_NO_NULO);
		}

		Solicitud solicitud = this.listaBolsasSolicitud(bean, request);
		
		if (solicitud == null) {
			throw new UVException(MENSAJE_ERROR_SOLICITUD_NO_NULO);
		}
		
		Afinidad afinidad = ModeloAfinidad.obtenerInstancia().getAfinidadById(Formateador.leeParametroInteger(request.getParameter(PARAM_AFINIDAD_ID)));
		ModeloSolicitud.obtenerInstancia().asignarAfinidadMeritoIndividualizado(solicitud, area, merito, afinidad);

		this.seleccionarBolsa(bean, request);
	}
	
	/**
	 * Establece la afinidad del merito dentro del area que ha seleccionado el candidato, del tipo no individualizado.
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarAfinidadNoIndividualizado(VistaSolicitudes bean, HttpServletRequest request) 
			throws SQLException, UVException {
		Bolsa area = ModeloBolsa.obtenerInstancia().getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
		bean.setArea(area);

		Merito merito = ModeloMerito.obtenerInstancia().listaMerito(Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_ID)));
		bean.setMerito(merito);
		
		if (merito == null) {
			throw new UVException(MENSAJE_ERROR_MERITO_NO_NULO);
		}

		Solicitud solicitud = this.listaBolsasSolicitud(bean, request);
		
		if (solicitud == null) {
			throw new UVException(MENSAJE_ERROR_SOLICITUD_NO_NULO);
		}
		
		// parseamos json bolsas
		Gson gson = new GsonBuilder().create();
		HashMap<String, Float> afinidadesRaw;
		try {			
			afinidadesRaw = gson.fromJson(request.getParameter(PARAM_AFINIDADES), 
					new TypeToken<HashMap<String, Float>>() { }.getType());					
		} catch (Exception e) {
			throw new UVException("Afinidades incorrectas");
		}
		
		// comprobamos validez de las afinidades
		HashMap<Afinidad, Float> afinidades = new HashMap<>();
		ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
		Float total = (float) 0.0;				
		for (Map.Entry<String, Float> entry : afinidadesRaw.entrySet()) {
			Afinidad a = modeloAfinidad.getAfinidadById(Formateador.leeParametroInteger(entry.getKey()));
			afinidades.put(a, entry.getValue());
			total += entry.getValue(); 
		}
		
		Float totalRounder = (float) BolsaEmpleoUtils.redondeo(total);		
		if (!totalRounder.equals(merito.getValor())) {
			throw new UVException("El total de afinidades tiene que ser igual al valor del mérito");
		}
		
		// asignamos afinidades al merito de la bolsa
		ModeloSolicitud.obtenerInstancia().asignarAfinidadMeritoNoIndividualizado(solicitud, area, merito, afinidades);

		this.seleccionarBolsa(bean, request);
	}
	
	/** 
	 * Agrega la vista del paso 2 y le añade la solicitud a la vista.
	 * @param bean .
	 * @param request .
	 * @return solicitud .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private Solicitud listaBolsasSolicitud(VistaSolicitudes bean, HttpServletRequest request) throws UVException, SQLException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		
		bean.setVista(JSP_PASO2);
		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
		bean.setListaTipoAfinidades(ModeloAfinidad.obtenerInstancia().getTiposAfinidad());
		bean.setSolicitud(solicitud);
		
		return solicitud;
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
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		Solicitud solicitud = modelo.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaSolicitudTable> dataTable = modelo.listaBolsasSolicitudesDatatable(solicitud, request.getParameterMap());
				bean.setDatatableBolsasSolicitud(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();				
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
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
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
				
				BolsaEmpleoDataTable<MeritoSolicitudTable> dataTable = ModeloSolicitud.obtenerInstancia().
						listaMeritosSolicitudDatatable(request.getParameterMap(), bean.getCandidato(), bolsa);
				bean.setDataTableMeritos(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PASO 3: RESUMEN, CONFIRMAR SOLICITUD
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesPaso3(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_CONFIRMAR_SOLICITUD:
				confirmarSolicitud(bean, request, response);
				break;
			case ACCION_RESUMEN_SOLICITUD:
				resumenSolicitud(bean, request);
				break;
			case ACCION_DESCARGAR_PDF:
				descargarPDF(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	/** Resumen de la solicitud .
	 * @param bean .
	 * @param request .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void resumenSolicitud(VistaSolicitudes bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_PASO2);
		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
		bean.setListaTipoAfinidades(ModeloAfinidad.obtenerInstancia().getTiposAfinidad());
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		ModeloTitulacion modeloTitulacion = ModeloTitulacion.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		
		List<Titulacion> titulaciones = modeloTitulacion.listaTitulacionesCandidato(bean.getCandidato().getCodNum());
		bean.setListaTitulaciones(titulaciones);
		
		// comprobamos que el total de méritos sea mayor que 0
		Integer totalMeritos = modeloSolicitud.obtenerTotalMeritosSolicitud(solicitud);
		
		if (totalMeritos <= 0) {
			throw new UVException(MENSAJE_ERROR_SIN_MERITOS);
		}
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
		bean.setListaBolsasSolicitud(listaBolsas);
		
		bean.setVista(JSP_PASO3);
	}
	
	/**
	 * El usuario confirma la solicitud .
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void confirmarSolicitud(VistaSolicitudes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_PASO3);
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
		bean.setListaBolsasSolicitud(listaBolsas);
		
		solicitud.setEstado(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
		solicitud.setFechaConfirmacion(BolsaEmpleoUtils.getCurrentDate());
		solicitud.setArchivo(generarPDF(bean, solicitud, listaBolsas));
		
		modeloSolicitud.confirmacionSolicitud(solicitud);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_SOLICITUD_CONFIRMADA);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_SOLICITUD_CONFIRMADA);
		response.sendRedirect(request.getServletPath());
	}
	
	/**
	 * descarga de pdf .
	 * @param response .
	 * @param datos    .
	 * @param bean     .
	 * @param request  .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	private void descargarPDF(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		Solicitud solicitud = modeloSolicitud.getSolicitudByIdArchivo(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		if (!bean.getCandidato().getCodNum().equals(solicitud.getUsuario().getCodNum())) {
			throw new UVException("No tienes permisos");
		}
		
		if (solicitud.getArchivo() == null) {
			throw new UVException("El archivo no existe");
		}
        
		try (ServletOutputStream stream = response.getOutputStream(); BufferedInputStream buf = new BufferedInputStream(solicitud.getArchivo())) {
			int readBytes = 0;
			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
		} catch (Exception ex) {
			throw new UVException(ex.toString());
		}
        
		response.setContentType("application/pdf");
		datos.setRespuestaEnviada(true);
	}
	
	/**
	 * generar PDF de la solicitud .
	 * @param bean .
	 * @param solicitud .
	 * @param bolsasSolicitud .
	 * @return InputStream .
	 * @throws UVException .
	 */
	public InputStream generarPDF(VistaSolicitudes bean, Solicitud solicitud, List<BolsaSolicitud> bolsasSolicitud) throws UVException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try (Document document = new Document()) {
			// create a PDF writer instance and pass output stream
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(bean.getCandidato().getNombre() + " " + bean.getCandidato().getPrimerApellido() + " " + bean.getCandidato().getSegundoApellido());
			document.addTitle("Solicitud_" + solicitud.getConvocatoria().getDescripcion());
			document.addCreationDate();

			document.add(new Paragraph(new Chunk("SOLICITUD", FontFactory.getFont(FontFactory.HELVETICA, SIZE_40, Font.BOLDITALIC))));
			
			document.add(new Paragraph("\n"));
			
			Font font2 = new Font(Font.BOLD);
			font2.setStyle("bold");
			
			document.add(new Paragraph("Usuario: " + bean.getCandidato().getNombre() + " " + bean.getCandidato().getPrimerApellido() + " " 
					+ bean.getCandidato().getSegundoApellido(), font2));
			document.add(new Paragraph("Convocatoria: " + solicitud.getConvocatoria().getDescripcion(), font2));
			document.add(new Paragraph("Fecha confirmación de solicitud: " 
					+ Formateador.formatoFecha(solicitud.getFechaConfirmacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS), font2));

			document.add(new Paragraph("\n"));
				        
			for (BolsaSolicitud bolsa: bolsasSolicitud) {
				this.generarPDFArea(bolsa, bolsasSolicitud, document);
			}
		} catch (Exception exp) {
			throw new UVException("Error generando pdf, consulte con los administradores");
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private void generarPDFArea(BolsaSolicitud bolsa, List<BolsaSolicitud> bolsasSolicitud, Document document) {
		Table table = new Table(PDF_TABLE_COLUMNS, bolsasSolicitud.size());

		this.generarPDFAreaHeader(table);

		if (bolsa.getNumeroMeritos() != null && bolsa.getNumeroMeritos() > 0) {
			for (MeritoSolicitud merito: bolsa.getListaMeritos()) {
				String codigoItem = merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
						+ merito.getMerito().getItemBaremacion().getBloqueBaremacion().getCodigo() + "." 
						+ merito.getMerito().getItemBaremacion().getCodigo();
				
				Cell cell = new Cell(codigoItem);
				cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
				table.addCell(cell);
				cell = new Cell(merito.getMerito().getItemBaremacion().getNombre());
				cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
				table.addCell(cell);
				cell = new Cell(merito.getMerito().getValor().toString());
				cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
				table.addCell(cell);
				cell = new Cell(merito.getMerito().getDescripcion());
				cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
				table.addCell(cell);
			}
		} else {
			Cell cell = new Cell(MENSAJE_AREA_SIN_MERITOS);
			cell.setColspan(COLSPAN_4);
			cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
			table.addCell(cell);
		}

		com.lowagie.text.List lista = new com.lowagie.text.List();
		lista.setListSymbol("• ");
		lista.add("Área - " + bolsa.getArea().getDescripcion());

		document.add(lista);
		document.add(table);
	}
	
	private void generarPDFAreaHeader(Table table) {
		table.setBorderWidth(1);
		table.setBorderColor(new Color(0, 0, 0));
		table.setPadding(PDF_TABLE_PADDING);
		table.setWidth(SIZE_100);

		Font font = new Font();
		font.setColor(new Color(0, COLOR_51, COLOR_153));

		Phrase phrase = new Phrase("Cod. Mérito", font);
		Cell cell = new Cell(phrase);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase2 = new Phrase("Mérito", font);
		cell = new Cell(phrase2);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase3 = new Phrase("Valor", font);
		cell = new Cell(phrase3);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase4 = new Phrase("Descripción", font);
		cell = new Cell(phrase4);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);
		table.endHeaders();
	}
}
