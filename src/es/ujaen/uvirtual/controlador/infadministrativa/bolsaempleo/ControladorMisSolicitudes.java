package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Fichero;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.MeritoSolicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloArea;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloFichero;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloMerito;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
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
	
	// acciones solicitudes
	public static final String ACCION_CONSULTAR_SOLICITUD = "consultarSolicitud";
	public static final String ACCION_CREAR_SOLICITUD = "crearSolicitud";
	public static final String ACCION_DATATABLE_SOLICITUDES = "datatablesolicitudes";
	public static final String ACCION_LISTAR_SOLICITUDES = "listar";
	
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
	
	// acciones paso 3: Confirmar Solicitud
	
	public static final String ACCION_CONFIRMAR_SOLICITUD = "confirmarsolicitud";
	public static final String ACCION_RESUMEN_SOLICITUD = "resumensolicitud";
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
	public static final Integer PDF_TABLE_PADDING = 5;

	
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
				case ACCION_CONSULTAR_SOLICITUD:
				case ACCION_CREAR_SOLICITUD:
				case ACCION_DATATABLE_SOLICITUDES:
				case ACCION_LISTAR_SOLICITUDES:
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
					accionesPaso2(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_CONFIRMAR_SOLICITUD:
				case ACCION_RESUMEN_SOLICITUD:
				case ACCION_DESCARGAR_PDF:
					accionesPaso3(bean, datos, request, response, nombreAccion);
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SOLICITUDES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesSolicitudes(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_CONSULTAR_SOLICITUD:
				consultarSolicitud(bean, datos, request, response);
				break;	
			case ACCION_CREAR_SOLICITUD:
				crearSolicitud(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_SOLICITUDES:
				listadoSolicitudes(bean, datos, request, response);
				break;
			case ACCION_LISTAR_SOLICITUDES:
				bean.setVista(RUTA_BEP_SOL + "indexSolicitudes.jsp");
				break;
		}
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
				seleccionarBolsas(bean, datos, request, response);
				break;
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
		
		// añadimos las bolsas a la solicitud
		modeloSolicitud.asignarBolsasASolicitud(solicitud, bolsas);
		
		bean.setVista(RUTA_BEP_SOL + "paso2.jsp");
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PASO 2: ASIGNACIÓN DE MÉRITOS A ÁREAS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesPaso2(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_BOLSA_SELECCIONADA:
				seleccionarBolsa(bean, datos, request, response);
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
				seleccionarMerito(bean, datos, request, response, false);
				break;
			case ACCION_MERITO_SELECCIONADO:
				seleccionarMerito(bean, datos, request, response, true);
				break;
		}
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
				BolsaEmpleoDataTable<MeritoSolicitud> dataTable = modelo.listaMeritosSolicitudDatatable(request.getParameterMap(), this.usuario);
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PASO 3: CONFIRMAR SOLICITUD
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesPaso3(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_CONFIRMAR_SOLICITUD:
				confirmarSolicitud(bean, request, response);
				break;
			case ACCION_RESUMEN_SOLICITUD:
				resumenSolicitud(bean, datos, request, response);
				break;
			case ACCION_DESCARGAR_PDF:
				descargarPDF(bean, datos, request, response);
				break;
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
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void confirmarSolicitud(VistaSolicitudes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		bean.setVista(RUTA_BEP_SOL + "paso3.jsp");
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
		bean.setListaBolsasSolicitud(listaBolsas);
		
		solicitud.setEstado(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
		solicitud.setFechaConfirmacion(new Date());
		solicitud.setArchivo(generarPDF(solicitud, listaBolsas));
		
		modeloSolicitud.confirmacionSolicitud(solicitud);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_SOLICITUD_CONFIRMADA);
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
		
		if (this.usuario.getCodNum() != solicitud.getUsuario().getCodNum()) {
			throw new UVException("No tienes permisos");
		}
        
        try (ServletOutputStream stream = response.getOutputStream();
             BufferedInputStream buf = new BufferedInputStream(solicitud.getArchivo());) {
            int readBytes = 0;
            while ((readBytes = buf.read()) != -1) {
                stream.write(readBytes);
            }
            stream.flush();
        }
        
        response.setContentType("application/pdf");
        datos.setRespuestaEnviada(true);
	}
	
	/**
	 * generar PDF de la solicitud .
	 * @param solicitud .
	 * @param bolsasSolicitud .
	 * @return InputStream .
	 * @throws UVException .
	 */
	public InputStream generarPDF(Solicitud solicitud, List<BolsaSolicitud> bolsasSolicitud) throws UVException {
		try {
			Document document = new Document();

			// create a PDF writer instance and pass output stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(this.usuario.getNombre() + " " + this.usuario.getPrimerApellido() + " " + this.usuario.getSegundoApellido());
			document.addTitle("Solicitud_" + solicitud.getConvocatoria().getDescripcion());
			document.addCreationDate();

			document.add(new Paragraph(new Chunk("SOLICITUD", FontFactory.getFont(FontFactory.HELVETICA, 40, Font.BOLDITALIC))));
			
			document.add(new Paragraph("\n"));
			
			Font font2 = new Font(Font.BOLD);
			font2.setStyle("bold");
			
			document.add(new Paragraph("Usuario: " + this.usuario.getNombre() + " " + this.usuario.getPrimerApellido() + " " 
					+ this.usuario.getSegundoApellido(), font2));
			document.add(new Paragraph("Convocatoria: " + solicitud.getConvocatoria().getDescripcion(), font2));
			document.add(new Paragraph("Fecha confirmación de solicitud: " 
					+ Formateador.formatoFecha(solicitud.getFechaConfirmacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS), font2));

			document.add(new Paragraph("\n"));
			
	        com.lowagie.text.List lista = new com.lowagie.text.List();
	        lista.setListSymbol("• ");
			
			for (BolsaSolicitud bolsa: bolsasSolicitud) {
		        lista.add("Área - " + bolsa.getArea().getDescripcion());  
				
				Table table = new Table(PDF_TABLE_COLUMNS, bolsasSolicitud.size());
				table.setBorderWidth(1);
				table.setBorderColor(new Color(0, 0, 0));
				table.setPadding(PDF_TABLE_PADDING);
				table.setWidth(100);
				
				Font font = new Font();
				font.setColor(new Color(0, 51, 153));

				Phrase phrase = new Phrase("Cod. Mérito", font);
				Cell cell = new Cell(phrase);
				cell.setHeader(true);
				cell.setBackgroundColor(new Color(185, 201, 254));
				table.addCell(cell);
				
				Phrase phrase2 = new Phrase("Mérito", font);
				cell = new Cell(phrase);
				cell.setHeader(true);
				cell.setBackgroundColor(new Color(185, 201, 254));
				table.addCell(cell);
				
				Phrase phrase3 = new Phrase("Valor", font);
				cell = new Cell(phrase3);
				cell.setHeader(true);
				cell.setBackgroundColor(new Color(185, 201, 254));
				table.addCell(cell);
				
				Phrase phrase4 = new Phrase("Descripción", font);
				cell = new Cell(phrase4);
				cell.setHeader(true);
				cell.setBackgroundColor(new Color(185, 201, 254));
				table.addCell(cell);
				table.endHeaders();
				
				if (bolsa.getListaMeritos() != null && bolsa.getListaMeritos().size() > 0) {
					for (Merito merito: bolsa.getListaMeritos()) {
						String codigoItem = merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
								+ merito.getItemBaremacion().getBloqueBaremacion().getCodigo() + "." + merito.getItemBaremacion().getCodigo();
						
						cell = new Cell(codigoItem);
						cell.setBackgroundColor(new Color(241, 241, 241));
						table.addCell(cell);
						cell = new Cell(merito.getItemBaremacion().getNombre());
						cell.setBackgroundColor(new Color(241, 241, 241));
						table.addCell(cell);
						cell = new Cell(merito.getValor().toString());
						cell.setBackgroundColor(new Color(241, 241, 241));
						table.addCell(cell);
						cell = new Cell(merito.getDescripcion().toString());
						cell.setBackgroundColor(new Color(241, 241, 241));
						table.addCell(cell);
					}
				}
								
		        document.add(lista);
				
				document.add(table);

			}
			document.close();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			throw new UVException("Error generando pdf, consulte con los administradores");
		}
	}
	
}
