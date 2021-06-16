package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteOpcion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentesCandidato;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Apartado de mis méritos preferentes de bolsa empleo.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.mismeritospreferentes", 
	description = "Méritos preferentes del candidato", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/mismeritospreferentes", 
			"/srv/en/informacionadministrativa/bolsaempleo/mismeritospreferentes",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/mismeritospreferentes", 
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/mismeritospreferentes"
	})
public class ControladorMisMeritosPreferentes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisMeritosPreferentes.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_AGREGAR_MERITO = "agregarmerito";	
	public static final String ACCION_AGREGAR_MERITO_CONFIRM = "agregarmeritoconfirm";
	public static final String ACCION_ELIMINAR_MERITOS = "eliminarmeritos";
	public static final String ACCION_LISTADO_OPCIONES = "opciones";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ID = "id";
	public static final String PARAM_MERITO_PREFERENTE = "meritoPreferente";
	public static final String PARAM_MERITO_PREFERENTE_OPCION = "meritoPreferenteOpcion";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_OBSERVACION = "observacion";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_MERITOS = "meritos";
	
	// mensajes
	public static final String MENSAJE_EXITO_ELIMINAR = "Mérito eliminado correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_MERITOS_PREFERENTES = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mismeritospreferentes/";
	public static final String JSP_INDEX = RUTA_BEP_MERITOS_PREFERENTES + "index.jsp"; 
	public static final String JSP_FORMULARIO = RUTA_BEP_MERITOS_PREFERENTES + "formMisMeritosPreferentes.jsp";
	
	// ajax	
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/mismeritospreferentes";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaMeritosPreferentesCandidato bean = new VistaMeritosPreferentesCandidato();
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (ServletFileUpload.isMultipartContent(request)) {
			nombreAccion = ACCION_AGREGAR_MERITO_CONFIRM;
		}
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			accionesMeritos(bean, datos, request, response, nombreAccion);
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (FileUploadException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al subir fichero");
		} finally {
			datos.getVistas().put(bean.getClass().getName(), bean);
			datos.getFicherosJSP().add(bean.getVista());
			datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
			datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private void init(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		this.index(bean);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));

			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_CANDIDATO)) {
				throw new UVException("No eres un candidato");
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}		
	}
	
	private void errorFatal(VistaMeritosPreferentesCandidato bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	/** Redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	
	
	private void index(VistaMeritosPreferentesCandidato bean) {
		bean.setVista(JSP_INDEX);
		bean.setCodigoPadreMeritoPreferente("IV");
	}
	
	private void accionesMeritos(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws IOException, SQLException, UVException, FileUploadException {
		
		switch (nombreAccion) {
			case ACCION_INDEX:
				index(bean);
				break;
			case ACCION_DATATABLE:
				listadoMeritos(bean, datos, request, response);
				break;
			case ACCION_AGREGAR_MERITO:
				formularioAgregarMerito(bean);
				break;
			case ACCION_AGREGAR_MERITO_CONFIRM:
				agregarMerito(bean, request, response);
				break;
			case ACCION_ELIMINAR_MERITOS:
				eliminarMeritos(bean, request, response);
				break;
			case ACCION_LISTADO_OPCIONES:
				listadoOpciones(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, "Acción no contemplada");
		}
	}
	
	private void listadoMeritos(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable = ModeloMeritosPreferentesCandidato.obtenerInstancia().
						listaMeritosCandidatoDatatable(request.getParameterMap(), bean.getUsuarioLogeado());
				bean.setDatatable(dataTable);
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
	
	private void listadoOpciones(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				List<MeritoPreferenteOpcion> opciones = ModeloMeritosPreferentes.obtenerInstancia().listadoOpcionesMeritosPreferentes(ModeloMeritosPreferentes.
						obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID))));
				bean.setOpcionesMerito(opciones);				
				Gson gson = new GsonBuilder().setDateFormat("dd/M/yyyy").create();						
				writer.write(gson.toJson(opciones));
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
	
	private void formularioAgregarMerito(VistaMeritosPreferentesCandidato bean) 
			throws SQLException, UVException {
		bean.setVista(JSP_FORMULARIO);
		bean.setMeritosPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritosPreferentesPorPosesion());
		
		ParametrosConfiguracion config = ModeloParametrosConfiguracion.obtenerInstancia().getParametroByNombre("bolsaempleo.local.codMeritoPreferente");
		bean.setCodigoPadreMeritoPreferente(config.getValor());		
	}
	
	private void agregarMerito(VistaMeritosPreferentesCandidato bean, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException, FileUploadException {
		formularioAgregarMerito(bean);
		
		// leemos los parametros del form, chequeando el fichero
		List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
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
				
		MeritoPreferenteUsuario mp = validateMeritoPreferenteUsuario(bean, parametros);
		ModeloMeritosPreferentesCandidato.obtenerInstancia().insertaMeritoUsuario(mp, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente añadido correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void eliminarMeritos(VistaMeritosPreferentesCandidato bean, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITOS));
		int[] selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
		
		ModeloMeritosPreferentesCandidato modelo = ModeloMeritosPreferentesCandidato.obtenerInstancia();
		List<MeritoPreferenteUsuario> meritos = new ArrayList<>();
		
		for (int sel : selected) {
			MeritoPreferenteUsuario m = modelo.getMeritoPreferenteUsuarioById(sel);
			if (!m.getUsuario().getCodNum().equals(bean.getUsuarioLogeado().getCodNum())) {
				throw new UVException("No tienes permisos");
			}
			if (!ModeloSolicitud.obtenerInstancia().comprobarMeritoPreferentePuedeSerBorrado(m)) {
				BolsaEmpleoUtils.addMensajeDeError(String.format("La acreditación %s no puede ser borrada", m.getMeritoPreferente().getNombre()), bean, request);
			} else {
				meritos.add(m);
			}
		}

		if (!meritos.isEmpty()) {
			modelo.cambiarFlagBorradoMeritos(meritos, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR, bean, request);
		}
		
		response.sendRedirect(request.getServletPath());
	}
	
	private MeritoPreferenteUsuario validateMeritoPreferenteUsuario(VistaMeritosPreferentesCandidato bean, HashMap<String, Object> parametros) 
			throws SQLException, UVException {
		
		MeritoPreferenteUsuario mpu = new MeritoPreferenteUsuario();
		
		mpu.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(
				Formateador.leeParametroInteger((String) parametros.get(PARAM_MERITO_PREFERENTE))));
		
		if (mpu.getMeritoPreferente().getTipoCalculo().equals(ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES)) {
			mpu.setMeritoPreferenteOpcion(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(
					Formateador.leeParametroInteger((String) parametros.get(PARAM_MERITO_PREFERENTE_OPCION))));
			
			if (!mpu.getMeritoPreferenteOpcion().getMeritoPreferenteCodNum().equals(mpu.getMeritoPreferente().getCodNum())) {
				throw new UVException("Opción no válida");
			}
		}
		
		mpu.setUsuario(bean.getUsuarioLogeado());
		if (!mpu.getUsuario().isCandidato()) {
			throw new UVException("El usuario debe ser un candidato");
		}
		
		mpu.setDescripcion((String) parametros.get(PARAM_OBSERVACION));
		if (mpu.getDescripcion() != null && mpu.getDescripcion().length() > ModeloMeritosPreferentesCandidato.MAX_LENGTH_COLUMN_DESCRIPCION) {
			throw new UVException(String.format("Los comentarios para la comisión no puede contener mas de %d caracteres", 
					ModeloMeritosPreferentesCandidato.MAX_LENGTH_COLUMN_DESCRIPCION));
		}
		
		mpu.setArchivo((InputStream) parametros.get(PARAM_ARCHIVO));
		
		// solo puede haber un mérito preferente por tipo y activo y usuario
		if (!ModeloMeritosPreferentesCandidato.obtenerInstancia().compruebaSoloUnTipoDeMeritoPrefenteActivo(mpu)) {
			throw new UVException("Ya tiene una acreditación del mismo tipo");
		}
				
		return mpu;
	}
}
