package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.awt.Color;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
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
import com.lowagie.text.DocumentException;
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
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_BOLSAS = "bolsas";
	public static final String PARAM_CONVOCATORIA_ID = "idConvocatoria";
	public static final String PARAM_MERITO_ID = "merito";
	public static final String PARAM_SOLICITUD_ID = "idSolicitud";
	public static final String PARAM_AFINIDAD_ID = "idAfinidad";
	public static final String PARAM_AFINIDADES = "afinidades";
	
	// acciones solicitudes
	public static final String ACCION_CONSULTAR_SOLICITUD = "consultarSolicitud";
	public static final String ACCION_CREAR_SOLICITUD = "crearSolicitud";
	public static final String ACCION_DATATABLE_SOLICITUDES = "datatablesolicitudes";
	public static final String ACCION_INDEX = "listar";
	
	// acciones paso 1: Selección de áreas
	public static final String ACCION_DATATABLE_AREAS_SELECCIONADAS = "datatableareasseleccionadas";
	public static final String ACCION_DATATABLE_AREAS = "datatableareas";
	public static final String ACCION_SELECCIONAR_BOLSAS = "seleccionarbolsas";
	public static final String ACCION_DESELECCIONAR_BOLSAS = "deseleccionarbolsas";
	public static final String ACCION_IR_A_MERITOS_POR_AREA = "meritosporarea";
	
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
	
	// mensajes
	public static final String MENSAJE_AREA_SIN_MERITOS = "No hay méritos asignados a éste área.";
	public static final String MENSAJE_ENVIADO = "enviadomissolicitudes";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS = "No hay bolsas seleccionadas válidas";
	public static final String MENSAJE_ERROR_ITEM_EXCLUYENTE = "El item que ha seleccionado es excluyente con los items: ";
	public static final String MENSAJE_ERROR_MERITOS_SIN_VALORACION = "No puede haber méritos con afinidad sin valoración";
	public static final String MENSAJE_ERROR_MERITO_NO_NULO = "Merito no puede ser nulo";
	public static final String MENSAJE_ERROR_NUMERO_MAXIMO_MERITOS_BLOQUE = "Se ha alcanzado el número máximo de méritos por bloque";
	public static final String MENSAJE_ERROR_SIN_MERITOS = "Dene incluir al menos un mérito en una bolsa para continuar";
	public static final String MENSAJE_EXITO_SOLICITUD_CONFIRMADA = "La solicitud ha sido confirmada correctamente";
		
	// ruta vistas
	public static final String RUTA_BEP_SOL = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/missolicitudes/";
	public static final String JSP_INDEX = RUTA_BEP_SOL + "indexSolicitudes.jsp";
	public static final String JSP_PASO1 = RUTA_BEP_SOL + "paso1.jsp";
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
	public static final int PDF_TABLE_COLUMNS = 5;
	public static final int PDF_TABLE_PADDING = 5;
	public static final int SIZE_10 = 10;
	public static final int SIZE_20 = 20;
	public static final int SIZE_30 = 30;
	public static final int SIZE_100 = 100;	
	public static final int COLOR_51 = 51;
	public static final int COLOR_153 = 153;
	public static final int COLOR_185 = 185;
	public static final int COLOR_201 = 201;
	public static final int COLOR_241 = 241;
	public static final int COLOR_254 = 254;
	public static final int COLSPAN_5 = 5;
	
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
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_CONSULTAR_SOLICITUD:
				case ACCION_CREAR_SOLICITUD:
				case ACCION_DATATABLE_SOLICITUDES:
				case ACCION_INDEX:
					accionesSolicitudes(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DATATABLE_AREAS_SELECCIONADAS:
				case ACCION_DATATABLE_AREAS:
				case ACCION_SELECCIONAR_BOLSAS:
				case ACCION_DESELECCIONAR_BOLSAS:
				case ACCION_IR_A_MERITOS_POR_AREA:
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
	
	private void init(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_CANDIDATO)) {
				throw new UVException("No eres un candidato");
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}		
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
		
		UsuarioBolsaEmpleo usuario = bean.getUsuarioLogeado();
		UsuarioBolsaEmpleo creador = bean.getUsuarioLogeado();
		
		Solicitud solicitud = modeloSolicitud.nuevaSolicitud(usuario, convocatoria, creador);
				
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
		bean.setVista(JSP_INDEX);
		
		Solicitud solicitud = this.getSolicitud(bean, request);
		bean.setSolicitud(solicitud);
		
		if (solicitud.getConvocatoria().getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
			throw new UVException(ModeloSolicitud.MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);
		}
		
		if (solicitud.getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) {
			listaBolsas(bean, solicitud);
		} else {
			List<BolsaSolicitud> listaBolsas = ModeloSolicitud.obtenerInstancia().getBolsasSolicitudMeritos(solicitud);
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
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Solicitud> dataTable = modelo.listaSolicitudesDatatable(bean.getUsuarioLogeado(), request.getParameterMap());
				bean.setDatatableSolicitudes(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PASO 1: SELECCIÓN DE ÁREAS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesPaso1(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws IOException, SQLException, UVException {
		switch (nombreAccion) {
			case ACCION_DATATABLE_AREAS_SELECCIONADAS:
				listadoAreasSeleccionadas(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_AREAS:
				listadoAreas(bean, datos, request, response);
				break;
			case ACCION_DESELECCIONAR_BOLSAS:
				deseleccionarBolsas(bean, request);
				break;
			case ACCION_SELECCIONAR_BOLSAS:
				seleccionarBolsas(bean, request);
				break;
			case ACCION_IR_A_MERITOS_POR_AREA:
				irAMeritosPorBolsa(bean, request);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void seleccionarBolsas(VistaSolicitudes bean, HttpServletRequest request) throws UVException, SQLException {
		bean.setVista(JSP_PASO1);
		
		Solicitud solicitud = this.getSolicitud(bean, request);
		bean.setSolicitud(solicitud);
		listaBolsas(bean, solicitud);
		
		ArrayList<Bolsa> bolsas = getListadoBolsasFromJson(bean, request, solicitud);
			
		// añadimos las bolsas a la solicitud
		ModeloSolicitud.obtenerInstancia().asignarBolsasASolicitud(solicitud, bolsas, bean.getUsuarioLogeado());
				
		// recargamos el listado de bolsas seleccionadas
		listaBolsas(bean, solicitud);
		
		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
		bean.setListaTipoAfinidades(ModeloAfinidad.obtenerInstancia().getTiposAfinidad());
	}

	private void deseleccionarBolsas(VistaSolicitudes bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_PASO1);
		
		Solicitud solicitud = this.getSolicitud(bean, request);
		bean.setSolicitud(solicitud);
		listaBolsas(bean, solicitud);
		
		ArrayList<Bolsa> bolsas = getListadoBolsasFromJson(bean, request, solicitud);
		
		// quitamos bolsas de la solicitud
		ModeloSolicitud.obtenerInstancia().desasignarBolsasASolicitud(solicitud, bolsas, bean.getUsuarioLogeado());
		
		// recargamos el listado de bolsas seleccionadas
		listaBolsas(bean, solicitud);

		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
		bean.setListaTipoAfinidades(ModeloAfinidad.obtenerInstancia().getTiposAfinidad());
	}
	
	private void listaBolsas(VistaSolicitudes bean, Solicitud solicitud) throws UVException, SQLException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
		bean.setVista(JSP_PASO1);
		
		List<Bolsa> listaBolsas = modelo.getBolsasSolicitud(solicitud);
		bean.setListaBolsas(listaBolsas);
	}
	
	private void listadoAreasSeleccionadas(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Solicitud solicitud = this.getSolicitud(bean, request);
				
				BolsaEmpleoDataTable<Bolsa> dataTable = ModeloSolicitud.obtenerInstancia().
						listaAreaSolicitudSeleccionadasDatatable(request.getParameterMap(), solicitud);
				
				bean.setDataTableBolsasCandidatoSeleccionadas(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void listadoAreas(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaCandidato> dataTable = modelo.listaAreaSolicitudDatatable(
						request.getParameterMap(), bean.getUsuarioLogeado().getCodNum());
				bean.setDataTableBolsasCandidato(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void irAMeritosPorBolsa(VistaSolicitudes bean, HttpServletRequest request) throws UVException, SQLException {
		bean.setVista(JSP_PASO1);
		
		Solicitud solicitud = this.getSolicitud(bean, request);
		bean.setSolicitud(solicitud);
		listaBolsas(bean, solicitud);
		
		if (bean.getListaBolsas().isEmpty()) {
			throw new UVException("Debe seleccionar al menos una bolsa");
		}
		
		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
		bean.setListaTipoAfinidades(ModeloAfinidad.obtenerInstancia().getTiposAfinidad());
		bean.setVista(JSP_PASO2);
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
		
		Solicitud solicitud = this.getSolicitud(bean, request);
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
		this.seleccionarBolsa(bean, request);

		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
				
		if (Boolean.TRUE.equals(seleccionado)) {
			// comprobamos que el total de méritos por bloque de la solicitud sea menor que
			// el permitido por la convocatoria
			Integer totalMeritos = modeloSolicitud.obtenerTotalMeritosPorBloqueSolicitud(solicitud, area, merito);

			if (totalMeritos >= solicitud.getConvocatoria().getNumMeritosPorBloque()) {
				this.seleccionarBolsa(bean, request);
				throw new UVException(MENSAJE_ERROR_NUMERO_MAXIMO_MERITOS_BLOQUE);
			}
			
			ModeloBaremacionItems modeloItems = ModeloBaremacionItems.obtenerInstancia();
			
			List<MeritoSolicitud> listaMeritos = modeloSolicitud.getMeritosSolicitudBolsa(solicitud, area);
			for (MeritoSolicitud mer : listaMeritos) {
				modeloItems.checkItemsExcluyentes(mer.getMerito().getItemBaremacion(), merito.getItemBaremacion());
			}
			
			modeloSolicitud.asignarMeritosASolicitudBolsa(solicitud, area, merito, bean.getUsuarioLogeado());			
		} else {
			modeloSolicitud.borrarMeritoDeSolicitudBolsa(solicitud, area, merito, bean.getUsuarioLogeado());
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
		
		Afinidad afinidad = ModeloAfinidad.obtenerInstancia().getAfinidadById(Formateador.leeParametroInteger(request.getParameter(PARAM_AFINIDAD_ID)));
		ModeloSolicitud.obtenerInstancia().asignarAfinidadMeritoIndividualizado(solicitud, area, merito, afinidad, bean.getUsuarioLogeado());

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

		// parseamos json bolsas
		Gson gson = new GsonBuilder().create();
		HashMap<String, Double> afinidadesRaw;
		try {			
			afinidadesRaw = gson.fromJson(request.getParameter(PARAM_AFINIDADES), 
					new TypeToken<HashMap<String, Double>>() { }.getType());					
		} catch (Exception e) {
			throw new UVException("Afinidades incorrectas");
		}
		
		// comprobamos validez de las afinidades
		HashMap<Afinidad, Double> afinidades = new HashMap<>();
		ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
		Double total = 0.0;				
		for (Map.Entry<String, Double> entry : afinidadesRaw.entrySet()) {
			Afinidad a = modeloAfinidad.getAfinidadById(Formateador.leeParametroInteger(entry.getKey()));
			afinidades.put(a, entry.getValue());
			total += entry.getValue(); 
		}
		
		Double totalRounder = BolsaEmpleoUtils.redondeo(total);		
		if (!totalRounder.equals(merito.getValor())) {
			throw new UVException("El total de afinidades tiene que ser igual al valor del mérito");
		}
		
		Solicitud solicitud = this.listaBolsasSolicitud(bean, request);
		
		// asignamos afinidades al merito de la bolsa
		ModeloSolicitud.obtenerInstancia().asignarAfinidadMeritoNoIndividualizado(solicitud, area, merito, afinidades, bean.getUsuarioLogeado());

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
		Solicitud solicitud = this.getSolicitud(bean, request);
		
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
			throws IOException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
				
		try (PrintWriter writer = response.getWriter()) {
			try {
				Solicitud solicitud = this.getSolicitud(bean, request);
				BolsaEmpleoDataTable<BolsaSolicitudTable> dataTable = modelo.listaBolsasSolicitudesDatatable(solicitud, request.getParameterMap());
				bean.setDatatableBolsasSolicitud(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
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
			throws IOException {
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA)));
				
				BolsaEmpleoDataTable<MeritoSolicitudTable> dataTable = ModeloSolicitud.obtenerInstancia().
						listaMeritosSolicitudDatatable(request.getParameterMap(), bean.getUsuarioLogeado(), bolsa, this.getSolicitud(bean, request));
				bean.setDataTableMeritos(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			} catch (SQLException e) { 
				LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
				LOGGER.log(Level.SEVERE, e.toString());
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
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
				confirmarSolicitud(bean, datos, request, response);
				break;
			case ACCION_RESUMEN_SOLICITUD:
				resumenSolicitud(bean, request);
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
		
		Solicitud solicitud = this.getSolicitud(bean, request);
		bean.setSolicitud(solicitud);
		
		List<TitulacionUsuario> titulaciones = modeloTitulacion.listaTitulacionesCandidato(bean.getUsuarioLogeado().getCodNum(), false);
		bean.setListaTitulaciones(titulaciones);
		
		// comprobamos que el total de méritos sea mayor que 0
		Integer totalMeritos = modeloSolicitud.obtenerTotalMeritosSolicitud(solicitud);
		
		if (totalMeritos <= 0) {
			throw new UVException(MENSAJE_ERROR_SIN_MERITOS);
		}
		
		// comprobamos que la valoración de los méritos con afinidad no esté vacía
		if (modeloSolicitud.comprobarMeritosAfinidadSinValoracion(solicitud)) {
			throw new UVException(MENSAJE_ERROR_MERITOS_SIN_VALORACION);
		}
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
		bean.setListaBolsasSolicitud(listaBolsas);
		
		bean.setVista(JSP_PASO3);
	}
	
	/**
	 * El usuario confirma la solicitud .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void confirmarSolicitud(VistaSolicitudes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_PASO3);
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Solicitud solicitud = modeloSolicitud.getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		bean.setSolicitud(solicitud);
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(solicitud);
		bean.setListaBolsasSolicitud(listaBolsas);
		
		modeloSolicitud.comprobarSolicitudCorrecta(solicitud, false);
		
		solicitud.setEstado(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
		solicitud.setFechaConfirmacion(BolsaEmpleoUtils.getCurrentDateTime());
		solicitud.setArchivo(generarPDF(bean.getUsuarioLogeado(), solicitud, listaBolsas));
		
		modeloSolicitud.confirmacionSolicitud(solicitud, bean.getUsuarioLogeado());
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_SOLICITUD_CONFIRMADA);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_SOLICITUD_CONFIRMADA);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	/**
	 * generar PDF de la solicitud .
	 * @param bean .
	 * @param solicitud .
	 * @param bolsasSolicitud .
	 * @return InputStream .
	 * @throws UVException .
	 */
	public static InputStream generarPDF(UsuarioBolsaEmpleo usuario, Solicitud solicitud, List<BolsaSolicitud> bolsasSolicitud) throws UVException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try (Document document = new Document()) {
			// create a PDF writer instance and pass output stream
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(usuario.getNombre() + " " + usuario.getPrimerApellido() + " " 
					+ usuario.getSegundoApellido());
			document.addTitle("Solicitud_" + solicitud.getConvocatoria().getDescripcion());
			document.addCreationDate();

			document.add(new Paragraph(new Chunk("SOLICITUD", FontFactory.getFont(FontFactory.HELVETICA, SIZE_30, Font.BOLDITALIC))));
			
			document.add(new Paragraph("\n"));
			
			Font font2 = new Font(Font.BOLD);
			font2.setStyle("bold");
			
			document.add(new Paragraph("Convocatoria: " + solicitud.getConvocatoria().getDescripcion(), font2));
			document.add(new Paragraph("Fecha confirmación de solicitud: " 
					+ Formateador.formatoFecha(solicitud.getFechaConfirmacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS), font2));
			
			document.add(new Paragraph("Usuario: " 
					+ getStringOrBlack(usuario.getPrsNif()) + " " 
					+ getStringOrBlack(usuario.getNombre()) + " " 
					+ getStringOrBlack(usuario.getPrimerApellido()) + " " 
					+ getStringOrBlack(usuario.getSegundoApellido()), font2));
			
			document.add(new Paragraph("Dirección: " 
					+ getStringOrBlack(usuario.getDireccion()) + " " 
					+ getStringOrBlack(usuario.getCodigoPostal()) + " "
					+ getStringOrBlack(usuario.getLocalidad()) + " "
					+ getStringOrBlack(usuario.getProvincia()) + " "
					+ getStringOrBlack(usuario.getTelefono()), font2));
			
			document.add(new Paragraph("\n"));
				        
			for (BolsaSolicitud bolsa: bolsasSolicitud) {
				generarPDFArea(bolsa, bolsasSolicitud, document);
			}
			
			generarTitulaciones(solicitud.getUsuario(), document);
			
			generarAcreditaciones(solicitud.getUsuario(), document);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			throw new UVException("Error generando pdf, consulte con los administradores");
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private static String getStringOrBlack(String txt) {
		if (txt == null) {
			return "";
		}
		return txt;
	}
	
	private static void generarPDFArea(BolsaSolicitud bolsa, List<BolsaSolicitud> bolsasSolicitud, Document document) {
		Table table = new Table(PDF_TABLE_COLUMNS, bolsasSolicitud.size());

		generarPDFAreaHeader(table);

		if (bolsa.getNumeroMeritos() != null && bolsa.getNumeroMeritos() > 0) {
			for (MeritoSolicitudTable merito: bolsa.getListaMeritos()) {
				String codigoItem = merito.getCodNum() + " " 
						+ merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
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
				
				String afinidad = "";
				
				if (merito.getCodNum() != null && merito.getMeritoSolicitud() != null && merito.getMerito().getItemBaremacion().getAfinidad() != null
						&& merito.getValoraciones().size() > 0) {
					if (merito.getMerito().getItemBaremacion().getIndividualizado()) {
						afinidad = merito.getValoraciones().get(0).getAfinidad().getCodigo() + " " 
								+ merito.getValoraciones().get(0).getAfinidad().getModulacion() * 100 + "%";
					} else {
						for (MeritoSolicitudValoracion valoracion: merito.getValoraciones()) {
							afinidad += valoracion.getValor() + " - " + valoracion.getAfinidad().getCodigo() + " "
									+ valoracion.getAfinidad().getModulacion() * 100 + "%\n";
						}
					}
				}
				
				cell = new Cell(afinidad);
				cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
				table.addCell(cell);
						
			}
		} else {
			Cell cell = new Cell(MENSAJE_AREA_SIN_MERITOS);
			cell.setColspan(COLSPAN_5);
			cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
			table.addCell(cell);
		}

		Font font2 = new Font(Font.BOLD);
		font2.setStyle("bold");		
		document.add(new Paragraph("Área - " + bolsa.getArea().getDescripcion(), font2));
		document.add(table);
		document.add(new Paragraph("\n"));
	}
	
	private static void generarPDFAreaHeader(Table table) {
		table.setBorderWidth(1);
		table.setBorderColor(new Color(0, 0, 0));
		table.setPadding(PDF_TABLE_PADDING);
		table.setWidth(SIZE_100);

		Font font = new Font();
		font.setColor(new Color(0, COLOR_51, COLOR_153));
		
		generarColumnPDFAreaHeader(table, "Cod. Mérito", font);
		generarColumnPDFAreaHeader(table, "Mérito", font);
		generarColumnPDFAreaHeader(table, "Valor", font);
		generarColumnPDFAreaHeader(table, "Descripción", font);
		generarColumnPDFAreaHeader(table, "Afinidad", font);
		
		float[] columnWidths = new float[] {SIZE_10, SIZE_30, SIZE_10, SIZE_30, SIZE_20};
		table.setWidths(columnWidths);
	}
	
	private static void generarColumnPDFAreaHeader(Table table, String titulo, Font font) {
		Phrase phrase = new Phrase(titulo, font);
		Cell cell = new Cell(phrase);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);
	}
	
	private static void generarTitulaciones(UsuarioBolsaEmpleo usuario, Document document) throws DocumentException, SQLException, UVException {
		Font font2 = new Font(Font.BOLD);
		font2.setStyle("bold");		
		
		document.add(new Paragraph("Titulaciones", font2));
		document.add(new Paragraph("\n"));
		
		for (TitulacionUsuario t : ModeloTitulacion.obtenerInstancia().listaTitulacionesCandidato(usuario.getCodNum(), false)) {
			document.add(new Paragraph(t.getCodNum() + " " 
				+ (t.getTitulacion() != null ? t.getTitulacion().getNombre() : ("Otra titulación: " + t.getOtraTitulacion()))));
			document.add(new Paragraph("\n"));
		}			
	}
	
	private static void generarAcreditaciones(UsuarioBolsaEmpleo usuario, Document document) throws DocumentException, SQLException, UVException {
		Font font2 = new Font(Font.BOLD);
		font2.setStyle("bold");		
		
		document.add(new Paragraph("Acreditaciones", font2));
		document.add(new Paragraph("\n"));
					
		for (MeritoPreferenteUsuario m : ModeloMeritosPreferentesCandidato.obtenerInstancia().listaMeritosPreferentesUsuarioPorPosesion(usuario)) {			
			document.add(new Paragraph(m.getCodNum() + " " 
					+ m.getMeritoPreferente().getNombre() 
					+ (m.getMeritoPreferenteOpcion() != null ? (" " + m.getMeritoPreferenteOpcion().getNombre()) : "")
					+ (m.getDescripcion() != null ? (" " + m.getDescripcion()) : "")));
			document.add(new Paragraph("\n"));
		}
	}
	
	private ArrayList<Bolsa> getListadoBolsasFromJson(VistaSolicitudes bean, HttpServletRequest request, Solicitud solicitud) throws UVException, SQLException {
		Gson gson = new GsonBuilder().create();		
		List<String> idBolsas = null;

		// parseamos json bolsas
		try {			
			idBolsas = gson.fromJson(request.getParameter(PARAM_BOLSAS), new TypeToken<List<String>>() { }.getType());					
		} catch (Exception e) {
			throw new UVException(MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS);
		}
		
		// comprobamos si son bolsas válidas
		ArrayList<Bolsa> bolsas = new ArrayList<>();
		for (String idBolsa : idBolsas) {
			Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(Integer.parseInt(idBolsa));
			
			if (Boolean.FALSE.equals(bolsa.getBaremable())) {
				throw new UVException("Ha selecciona una bolsa no baremable");
			}
			
			if (ModeloArea.obtenerInstancia().isUsuarioExcluidoBolsa(bean.getUsuarioLogeado(), bolsa.getArea())) {
				throw new UVException("Usuario excluido de la bolsa");
			}

			bolsas.add(bolsa);
		}
		
		if (bolsas.size() > solicitud.getConvocatoria().getNumBolsasMaximo()) {
			throw new UVException("Número de bolsas seleccionada no valido");
		}
		
		return bolsas;
	}
	
	/**
	 * Lee la solicitud del formulario y comprueba si le pertenece al usuario logeado.
	 * @param bean . 
	 * @param request .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private Solicitud getSolicitud(VistaSolicitudes bean, HttpServletRequest request) throws SQLException, UVException {
		Solicitud solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudById(Formateador.leeParametroInteger(request.getParameter(PARAM_SOLICITUD_ID)));
		
		if (!solicitud.getUsuario().getCodNum().equals(bean.getUsuarioLogeado().getCodNum())) {
			throw new UVException("No tienes permiso");
		}
		
		if (solicitud.getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA)) {
			throw new UVException("La solicitud está cerrada");
		}
		
		if (ModeloConvocatoria.obtenerInstancia().isConvocatoriaCerrada(solicitud.getConvocatoria())) {
			throw new UVException("La convocatoria está cerrada");
		}
		
		return solicitud;
	}	
}
