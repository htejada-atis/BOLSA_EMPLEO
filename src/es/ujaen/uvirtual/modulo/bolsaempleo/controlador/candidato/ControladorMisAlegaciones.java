package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionGeneral;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisAlegaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase controlador de las alegaciones del candidato .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.misalegaciones",
	description = "Controlador de mis alegaciones bolsa empleo",
	urlPatterns = {
			"/srv/es/informacionadministrativa/bolsaempleo/misalegaciones",
			"/srv/en/informacionadministrativa/bolsaempleo/misalegaciones",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/misalegaciones",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/misalegaciones"
	})
public class ControladorMisAlegaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisAlegaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	// Acciones
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_VER_DETALLE_ALEGACION = "verdetallealegacion";
	public static final String ACCION_DATATABLE_MIS_ALEGACIONES = "datatablemisalegaciones";
	public static final String ACCION_ALEGACION_MERITO = "alegacionmerito";
	public static final String ACCION_AGREGAR_MERITO_CONFIRM = "agregarmeritoconfirm";
	public static final String ACCION_AGREGAR_DESCRIPCION_ALEGACION_CONFIRM = "agregardescripcionalegacionconfirm";
	public static final String ACCION_AGREGAR_ALEGACION_CONFIRM = "agregaralegacionconfirm";
	public static final String ACCION_CONFIRMAR_ALEGACION = "confirmaralegacion";
	public static final String ACCION_DATATATABLE_ALEGACION_FICHEROS = "datatablealegacionficheros";
	public static final String ACCION_ELIMINAR_ARCHIVOS_ALEGECIONES = "eliminararchivosalegaciones";
	public static final String ACCION_ELIMINAR_ALEGACION_FROM_MERITO = "eliminaralegacionfrommerito";
	public static final String ACCION_AGREGAR_DESCRIPCION_ALEGACION_MERITO_CONFIRM = "agregardescripcionalegacionmeritoconfirm";
	public static final String ACCION_AGREGAR_ALEGACION_MERITO_CONFIRM = "agregaralegacionmeritoconfirm";
	public static final String ACCION_DATATATABLE_ALEGACION_MERITO_FICHEROS = "datatablealegacionmeritoficheros";
	public static final String ACCION_MIS_ALEGACIONES = "misalegaciones";
	public static final String ACCION_AGREGAR_FICHERO = "agregarfichero";
	public static final String ACCION_RESUMEN_ALEGACION = "resumenlegacion";

	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ALEGACIONES_ID = "alegacionesid";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_CONVOCATORIA = "convocatoria";
	public static final String PARAM_SOL_BOL_MERITO = "solbolmeritoid";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_ALEGACIONES_MERITO = "alegacionesmerito";
	public static final String PARAM_DESCRIPCION_ALEGACION_MERITO = "descripcionalegacionmerito";

	// Mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_SIN_PERMISO_CANDIDATO = "No eres un candidato";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_EXITO_AGREGAR_FICHERO = "Fichero añadido correctamente";
	public static final String MENSAJE_EXITO_AGREGAR_DESCRIPCION = "Comentario añadido correctamente";

	// Respuesta error
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;

	// ruta vistas
	public static final String RUTA_BEP_MA = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misalegaciones/";
	public static final String JSP_INDEX = RUTA_BEP_MA + "index.jsp";
	public static final String JSP_DETALLE_ALEGACION = RUTA_BEP_MA + "detallealegacion.jsp";
	public static final String JSP_RESUMEN_ALEGACION = RUTA_BEP_MA + "resumenAlegacion.jsp";
	public static final String RUTA_BEP_MR = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/misresultados/";
	public static final String JSP_RESULTADO_DETALLE = RUTA_BEP_MR + "resultadodetalle.jsp";
	public static final String JSP_FORM_ALEGACION_MERITO = RUTA_BEP_MA + "formAlegacionMerito.jsp";

	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/misalegaciones";
	public static final String URL_MIS_ALEGACIONES = "/srv/es/informacionadministrativa/bolsaempleo/misalegaciones";

	// ajax
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;


	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");

		VistaMisAlegaciones bean = new VistaMisAlegaciones();

		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (ServletFileUpload.isMultipartContent(request) && nombreAccion == null) {
			nombreAccion = ACCION_AGREGAR_FICHERO;
		}
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}

		try {
			if (!init(bean, datos, request, response)) {
				return;
			}
			switch (nombreAccion) {
			case ACCION_INDEX:
			case ACCION_MIS_ALEGACIONES:
				bean.setVista(JSP_INDEX);
				break;
			case ACCION_DATATABLE_MIS_ALEGACIONES:
				datatableMisAlegaciones(bean, datos, request, response);
				break;
			case ACCION_VER_DETALLE_ALEGACION:
				verDetalleAlegacion(bean, datos, request, response);
				break;
			case ACCION_DATATATABLE_ALEGACION_FICHEROS:
				datatableFicherosGeneral(bean, datos, request, response);
				break;
			case ACCION_AGREGAR_DESCRIPCION_ALEGACION_CONFIRM:
				agregarDescripcionAlegacionGeneral(bean, datos, request, response);
				break;
			case ACCION_ELIMINAR_ARCHIVOS_ALEGECIONES:
				eliminarArchivosAlegacion(bean, datos, request, response);
				break;
			case ACCION_ALEGACION_MERITO:
				verFormAlegacionMerito(bean, request);
				break;
			case ACCION_AGREGAR_DESCRIPCION_ALEGACION_MERITO_CONFIRM:
				agregarDescripcionAlegacionMerito(bean, datos, request, response);
				break;
			case ACCION_DATATATABLE_ALEGACION_MERITO_FICHEROS:
				datatableFicherosMerito(bean, datos, request, response);
				break;
			case ACCION_ELIMINAR_ALEGACION_FROM_MERITO:
				eliminarAlegacionMerito(bean, datos, request, response);
				break;
			case ACCION_AGREGAR_FICHERO:
				agregarFichero(bean, datos, request, response);
				break;
			case ACCION_RESUMEN_ALEGACION:
				verResumenAlegacion(bean, datos, request, response);
				break;
			case ACCION_CONFIRMAR_ALEGACION:
				confirmarAlegacion(bean, datos, request, response);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
		}
	}

	private boolean init(VistaMisAlegaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);

		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));
			if (bean.getUsuarioLogeado().getCodNum() == null) {
				Usuario usuArcos = datos.getUsuario();
				throw new UVException(String.format("No existe el usuario [%s]. Asegúrese de pulsar el botón 'Participar en la bolsa de empleo'"
						+ " desde la pantalla principal 'Inicio'.", usuArcos != null ? usuArcos.getUid() : "-"));
			}

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_CANDIDATO)) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO_CANDIDATO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());

			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}

		return true;
	}

	private void errorFatal(VistaMisAlegaciones beanAlegaciones, String mensaje) {
		beanAlegaciones.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		beanAlegaciones.getMensajesDeError().add(mensaje);
	}

	private void validarAlegacionEditable(VistaMisAlegaciones beanAlegaciones, Integer alegacionId) throws SQLException, UVException {
		ModeloAlegaciones.obtenerInstancia().validarAlegacionEditablePorCandidato(alegacionId, beanAlegaciones.getUsuarioLogeado());
	}

	private void validarAlegacionConfirmable(VistaMisAlegaciones beanAlegaciones, Integer alegacionId) throws SQLException, UVException {
		ModeloAlegaciones.obtenerInstancia().validarAlegacionConfirmablePorCandidato(alegacionId, beanAlegaciones.getUsuarioLogeado());
	}

	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	private void datatableMisAlegaciones(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		try (PrintWriter writer = response.getWriter()) {
			BolsaEmpleoDataTable<Alegacion> dataTable = ModeloAlegaciones.obtenerInstancia()
					.listaAlegacionesCandidatoDatatable(beanAlegaciones.getUsuarioLogeado(), request.getParameterMap());
			beanAlegaciones.setDataTableAlegaciones(dataTable);
			writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
		}
	}

	private void verDetalleAlegacion(VistaMisAlegaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		Integer codNumAlegacion = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia();
		Alegacion alegacion = modelo.obtenerDetalleAlegacionCandidato(codNumAlegacion, bean.getUsuarioLogeado());
		VistaMisResultados beanResultados = getBeanResultados(alegacion, null);

		bean.setConvocatoria(beanResultados.getConvocatoria());
		bean.setAlegacion(alegacion);
		bean.setBeanResultados(beanResultados);
		bean.setVista(JSP_DETALLE_ALEGACION);
	}

	private void datatableFicherosGeneral(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		Integer bolsaId = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));
		Integer convId = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA));
		Convocatoria conv = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convId);

		try (PrintWriter writer = response.getWriter()) {
			BolsaEmpleoDataTable<ArchivoAlegacionGeneral> dataTable = ModeloAlegaciones.obtenerInstancia()
					.datatableFicherosAlegacionGeneral(bolsaId, beanAlegaciones.getUsuarioLogeado(), conv, request.getParameterMap());
			writer.write(dataTable.toJson());
		}
	}

	private void eliminarArchivosAlegacion(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		String jsonIds = request.getParameter(PARAM_ALEGACIONES_MERITO);
		Gson gson = new Gson();
		List<Integer> ids = gson.fromJson(jsonIds, new TypeToken<List<Integer>>(){}.getType());

		try {
			Integer alegacionId = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
			if (alegacionId != null) {
				validarAlegacionEditable(beanAlegaciones, alegacionId);
			}

			ModeloAlegaciones.obtenerInstancia().eliminarArchivoAlegacion(ids, beanAlegaciones.getUsuarioLogeado());

			try (PrintWriter writer = response.getWriter()) {
				writer.write("{\"ok\":true}");
			}
		} catch (UVException e) {
			response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			try (PrintWriter writer = response.getWriter()) {
				writer.write("{\"" + RESPONSE_AJAX_ERROR + "\": \"" + e.getMessage() + "\"}");
			}
		}
	}

	/**
	 * Agrega un fichero a la alegación general a un merito de la alegacion
	 */
	private void agregarFichero(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		// maximo filesize request
		ModeloParametrosConfiguracion modeloParam = ModeloParametrosConfiguracion.obtenerInstancia();
		Integer maxSize = Formateador.leeParametroInteger(modeloParam.getParametroByNombre("bolsaempleo.maxEspacioArchivo").getValor());
		List<FileItem> items;
		try {
			ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
			upload.setSizeMax(maxSize);
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			String maxSizeHuman = ModeloParametrosConfiguracion.obtenerInstancia().getMaxEspacioArchivoHuman();

			LOGGER.log(Level.SEVERE, String.format("Error subiendo fichero [%s]", e));
			throw new UVException("Error subiendo fichero. Recuerde tamaño máximo de fichero: " + maxSizeHuman);
		}

		// leemos parametros
		HashMap<String, Object> parametros = new HashMap<>();
		for (FileItem item : items) {
			if (item.isFormField()) {
				parametros.put(item.getFieldName(), item.getString());
			} else {
				if (item.getSize() > 0 && item.getName().toLowerCase().endsWith(".pdf")) {
					try (InputStream contenidoDelFichero = item.getInputStream(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
						parametros.put(PARAM_ARCHIVO, BolsaEmpleoUtils.checkFileSize(contenidoDelFichero));
					}
				}
			}
		}

		Integer bolsaId = Formateador.leeParametroInteger((String) parametros.get(PARAM_BOLSA));
		Integer convId = Formateador.leeParametroInteger((String) parametros.get(PARAM_CONVOCATORIA));
		Integer aleId = Formateador.leeParametroInteger((String) parametros.get(PARAM_ALEGACIONES_ID));
		Integer solBolMeritoId = Formateador.leeParametroInteger((String) parametros.get(PARAM_SOL_BOL_MERITO));

		if (bolsaId == null || convId == null || aleId == null) {
			throw new UVException("Parámetros requeridos ausentes para subir fichero");
		}

		validarAlegacionEditable(beanAlegaciones, aleId);

		Convocatoria conv = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convId);
		ModeloAlegaciones modeloAlegaciones = ModeloAlegaciones.obtenerInstancia();

		// insertamos fichero
		try {
			for (FileItem item : items) {
				if (!item.isFormField() && item.getSize() > 0) {
					String nombreArchivo = item.getName();
					try (InputStream is = item.getInputStream()) {
						if (solBolMeritoId != null) {
							modeloAlegaciones.agregarFicheroAlegacionMerito(bolsaId, beanAlegaciones.getUsuarioLogeado(), conv, solBolMeritoId, is, nombreArchivo);
						} else {
							modeloAlegaciones.agregarFicheroAlegacionGeneral(bolsaId, beanAlegaciones.getUsuarioLogeado(), conv, is, nombreArchivo);
						}
					}
					break;
				}
			}
			beanAlegaciones.getMensajesInformativos().add("Archivo adjuntado correctamente");
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.toString());
			beanAlegaciones.getMensajesDeError().add("Error al subir el archivo: " + e.getMessage());
		}

		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR_FICHERO, beanAlegaciones, request);

		// si estamos en el detalle de un mérito nos quedamos ahi
		request.getSession().setAttribute(PARAM_ALEGACIONES_ID, String.valueOf(aleId));
		if (solBolMeritoId != null) {
			request.getSession().setAttribute(PARAM_SOL_BOL_MERITO, String.valueOf(solBolMeritoId));
			verFormAlegacionMerito(beanAlegaciones, request);
		} else {
			verDetalleAlegacion(beanAlegaciones, datos, request, response);
		}
	}

	private void agregarDescripcionAlegacionGeneral(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		Integer bolsaId = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));
		Integer convId = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA));
		Integer aleId = Formateador.leeParametroInteger(request.getParameter(PARAM_ALEGACIONES_ID));
		String descripcion = request.getParameter("descripcionalegacionmerito");

		validarAlegacionEditable(beanAlegaciones, aleId);

		Convocatoria conv = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convId);
		ModeloAlegaciones.obtenerInstancia().agregarDescripcionAlegacionGeneral(bolsaId, beanAlegaciones.getUsuarioLogeado(), conv, descripcion);

		beanAlegaciones.getMensajesInformativos().add("Descripción guardada correctamente");
		request.getSession().setAttribute(PARAM_ALEGACIONES_ID, String.valueOf(aleId));
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR_DESCRIPCION, beanAlegaciones, request);
		verDetalleAlegacion(beanAlegaciones, datos, request, response);
	}

	private void agregarDescripcionAlegacionMerito(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		Integer bolsaId = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));

		Integer convId = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA));
		if (convId == null) {
			throw new UVException("Convocatoria es requerida");
		}
		Convocatoria conv = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convId);

		Integer solBolMeritoId = Formateador.leeParametroInteger(request.getParameter(PARAM_SOL_BOL_MERITO));
		if (solBolMeritoId == null) {
			throw new UVException("Mérito es requerido");
		}

		Integer aleId = Formateador.leeParametroInteger(request.getParameter(PARAM_ALEGACIONES_ID));
		validarAlegacionEditable(beanAlegaciones, aleId);

		String descripcion = request.getParameter(PARAM_DESCRIPCION_ALEGACION_MERITO);
		if (descripcion == null || descripcion.equals("")) {
			throw new UVException("La descripción es requerida");
		}

		// agregamos la descripción de la alegación para el mérito
		ModeloAlegaciones.obtenerInstancia()
			.agregarDescripcionAlegacionMerito(bolsaId, beanAlegaciones.getUsuarioLogeado(), conv, solBolMeritoId, descripcion);
		BolsaEmpleoUtils.addMensajeDeExito("Descripción del mérito guardada correctamente", beanAlegaciones, request);
		verFormAlegacionMerito(beanAlegaciones, request);
	}

	private void verFormAlegacionMerito(VistaMisAlegaciones bean, HttpServletRequest request) throws SQLException, UVException, IOException {
		Integer codNumAlegacion = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		Integer codSolBolMerito = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_SOL_BOL_MERITO));
		ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia();
		Alegacion alegacion = modelo.obtenerDetalleAlegacionCandidato(codNumAlegacion, bean.getUsuarioLogeado());
		VistaMisResultados beanResultados = getBeanResultados(alegacion, codSolBolMerito);

		// leemo mérito
		MeritoSolicitud meritoSolicitud = ModeloSolicitud.obtenerInstancia().getMeritoSolicitudById(codSolBolMerito);
		if (meritoSolicitud == null) {
			throw new UVException("Parámetro id mérito requerido");
		}
		Merito merito = meritoSolicitud.getMerito();
		Integer depiteCodNum = merito.getItemBaremacion().getCodNum();
		if (depiteCodNum == null) {
			throw new UVException("Item de baremación no encontrado");
		}
		ItemBaremacion itemBaremacion = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(depiteCodNum);
		beanResultados.setItemBaremacion(itemBaremacion);

		beanResultados.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteTipoMerito());
		beanResultados.setAlegacion(alegacion);

		bean.setMeritoSolicitud(meritoSolicitud);
		bean.setConvocatoria(beanResultados.getConvocatoria());
		bean.setAlegacion(alegacion);
		bean.setBeanResultados(beanResultados);
		bean.setVista(JSP_FORM_ALEGACION_MERITO);
	}

	private void datatableFicherosMerito(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws SQLException, UVException, IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

		Integer bolsaId = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));
		Integer convId = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA));
		Integer solBolMeritoId = Formateador.leeParametroInteger(request.getParameter(PARAM_SOL_BOL_MERITO));
		Convocatoria conv = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convId);

		try (PrintWriter writer = response.getWriter()) {
			BolsaEmpleoDataTable<ArchivoAlegacionGeneral> dataTable = ModeloAlegaciones.obtenerInstancia()
					.datatableFicherosAlegacionMerito(bolsaId, solBolMeritoId, beanAlegaciones.getUsuarioLogeado(), conv, request.getParameterMap());
			writer.write(dataTable.toJson());
		}
	}

	private void eliminarAlegacionMerito(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloAlegaciones modeloAlegaciones = ModeloAlegaciones.obtenerInstancia();

		Integer alegacionId = Formateador.leeParametroInteger(request.getParameter(PARAM_ALEGACIONES_ID));
		validarAlegacionEditable(beanAlegaciones, alegacionId);
		Alegacion alegacion = modeloAlegaciones.getAlegacionByCodNum(alegacionId);

		Integer solBolMeritoId = Formateador.leeParametroInteger(request.getParameter(PARAM_SOL_BOL_MERITO));
		if (solBolMeritoId == null) {
			throw new UVException("Mérito requerido");
		}

		ModeloAlegaciones.obtenerInstancia().eliminarAlegacionMerito(alegacion, solBolMeritoId, beanAlegaciones.getUsuarioLogeado());

		beanAlegaciones.getMensajesInformativos().add("Alegación del mérito eliminada correctamente");
		request.getSession().setAttribute(PARAM_ALEGACIONES_ID, String.valueOf(alegacionId));
		verDetalleAlegacion(beanAlegaciones, datos, request, response);
	}

	private VistaMisResultados getBeanResultados(Alegacion alegacion, Integer solBolMeritoCodNum) throws SQLException, UVException {
		Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaByArea(alegacion.getArea());
		BolsaResultado bolsaResultado = ModeloResultados.obtenerInstancia().getBolsaResultado(bolsa, alegacion.getCandidato(), alegacion.getConvocatoria());

		VistaMisResultados beanResultados = new VistaMisResultados();
		beanResultados.setBolsa(bolsa);
		beanResultados.setConvocatoria(alegacion.getConvocatoria());
		beanResultados.setBolsaResultado(bolsaResultado);
		beanResultados.setUsuarioLogeado(alegacion.getCandidato());
		beanResultados.setVista(JSP_DETALLE_ALEGACION);

		// asignamos las alegaciones de los méritos a los resultados
		ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia();
		modelo.establecerDescripcionesSiExistenAlegacionesEnLosMeritos(beanResultados, alegacion.getConvocatoria(), alegacion.getCandidato(), bolsa.getCodNum());

		// asignamos la alegación general
		SolMerBolAlegacion solMerBolAlegacion = modelo.obtenerSolMerBolAlegacionExistente(bolsa, alegacion.getConvocatoria(), alegacion.getCandidato(), solBolMeritoCodNum);
		beanResultados.setSolMerBolAlegacion(solMerBolAlegacion);
		modelo.asignarArchivosAlegacion(beanResultados, solMerBolAlegacion);

		beanResultados.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteTipoMerito());

		return beanResultados;
	}

	private boolean estaBolsaEnEstadoDeAlegaciones(int codNum) throws SQLException {
		String estado = ModeloBolsa.obtenerInstancia().getEstadoPorCodNum(codNum);
		return ModeloBolsa.BOLSA_ESTADO_ALEGACIONES.equals(estado);
	}

	// ---------------------------------------------


	private void verResumenAlegacion(VistaMisAlegaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		Integer codNumAlegacion = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia();
		Alegacion alegacion = modelo.obtenerDetalleAlegacionCandidato(codNumAlegacion, bean.getUsuarioLogeado());

		VistaMisResultados beanResultados = getBeanResultados(alegacion, null);

		bean.setConvocatoria(beanResultados.getConvocatoria());
		bean.setAlegacion(alegacion);
		bean.setBeanResultados(beanResultados);
		bean.setVista(JSP_RESUMEN_ALEGACION);
	}

	private void confirmarAlegacion(VistaMisAlegaciones beanAlegaciones, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		Integer bolsaId = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA));
		Integer convId = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA));
		Integer aleId = Formateador.leeParametroInteger(request.getParameter(PARAM_ALEGACIONES_ID));
		Convocatoria conv = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(convId);

		validarAlegacionConfirmable(beanAlegaciones, aleId);

		ModeloAlegaciones.obtenerInstancia().confirmarAlegacion(
				bolsaId, beanAlegaciones.getUsuarioLogeado(), conv);

		beanAlegaciones.getMensajesInformativos().add("Alegación confirmada correctamente");
		BolsaEmpleoUtils.addMensajeDeExito("Alegación confirmada correctamente", beanAlegaciones, request);

		// Redirigir al listado de mis alegaciones
		beanAlegaciones.setVista(JSP_INDEX);
	}

}

