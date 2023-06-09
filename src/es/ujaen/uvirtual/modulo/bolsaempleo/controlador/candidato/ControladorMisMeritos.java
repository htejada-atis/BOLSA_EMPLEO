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
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritos;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Apartado de mis méritos de bolsa empleo.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.mismeritos", 
	description = "Méritos del candidato", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/mismeritos", 
			"/srv/en/informacionadministrativa/bolsaempleo/mismeritos",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/mismeritos", 
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/mismeritos"
	})
public class ControladorMisMeritos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisMeritos.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones	
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_ELIMINAR_MERITOS = "eliminarmeritos";
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_AGREGAR_MERITO = "agregarmerito";
	public static final String ACCION_AGREGAR_MERITO_CONFIRM = "agregarmeritoconfirm";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_APARTADO = "apartado";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_DESCRIPCION = "descripcion";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ID = "id";
	public static final String PARAM_ITEM = "item";
	public static final String PARAM_MERITOS = "meritos";
	public static final String PARAM_OBSERVACION = "observacion";
	public static final String PARAM_VALOR = "valor";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGO = "La descripción no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_DESCRIPCION_VACIA = "La descripción no puede estar vacía";
	public static final String MENSAJE_ERROR_ELIMINAR = "El mérito con id %s no se puede borrar";
	public static final String MENSAJE_ERROR_MERITOS_SELECCIONADOS_INCORRECTOS = "Méritos seleccionados incorrectos";
	public static final String MENSAJE_ERROR_OBSERVACION_LARGO = "La observación no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_SIN_PERMISO_CANDIDATO = "No eres un candidato";
	public static final String MENSAJE_EXITO_AGREGAR = "Mérito agregado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Mérito eliminado correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_MERITOS = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mismeritos/";
	public static final String JSP_INDEX = RUTA_BEP_MERITOS + "index.jsp";
	public static final String JSP_FORM = RUTA_BEP_MERITOS + "formMerito.jsp";
	
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/mismeritos";
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
		
		VistaMeritos bean = new VistaMeritos();		
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (ServletFileUpload.isMultipartContent(request)) {
			nombreAccion = ACCION_AGREGAR_MERITO_CONFIRM;
		}
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			if (!init(bean, datos, request, response)) {
				return;
			}
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private boolean init(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
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
	
	private void errorFatal(VistaMeritos bean, String mensaje) {
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
	
	private void accionesMeritos(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, IOException, UVException, FileUploadException {
		
		switch (nombreAccion) {
			case ACCION_INDEX:
				listaMeritos(bean);
				break;
			case ACCION_DATATABLE:
				listadoMeritos(bean, datos, request, response);
				break;
			case ACCION_AGREGAR_MERITO:
				formularioAgregarMerito(bean, request);
				break;
			case ACCION_AGREGAR_MERITO_CONFIRM:
				agregarMerito(bean, datos, request, response);
				break;
			case ACCION_ELIMINAR_MERITOS:
				eliminarMeritos(bean, datos, request, response);
				break;				
			default:
				errorFatal(bean, "Acción no contemplada");
		}
	}
	
	private void listaMeritos(VistaMeritos bean) throws SQLException {
		bean.setVista(JSP_INDEX);
		
		ModeloBaremacionApartados modeloBaremacion = ModeloBaremacionApartados.obtenerInstancia();
		bean.setApartados(modeloBaremacion.getApartadosActivos());
	}
	
	private void formularioAgregarMerito(VistaMeritos bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_FORM);
		
		ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
		bean.setApartados(modelo.getApartadosActivos());
				
		if (request.getParameter(PARAM_APARTADO) != null) {
			ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO))); 
			bean.setApartado(apartado);
			bean.setItems(ModeloBaremacionItems.obtenerInstancia().getItemsDeApartado(apartado));
		}
	}
	
	private void agregarMerito(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException, FileUploadException {
		bean.setVista(JSP_FORM);
		
		ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
		bean.setApartados(modelo.getApartadosActivos());
		
		// maximo filesize request
		ModeloParametrosConfiguracion modeloParam = ModeloParametrosConfiguracion.obtenerInstancia();
		Integer maxSize = Formateador.leeParametroInteger(modeloParam.getParametroByNombre("bolsaempleo.maxEspacioArchivo").getValor());
		
		// leemos los parametros del form, chequeando el fichero
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		upload.setSizeMax(maxSize);
		
		List<FileItem> items = upload.parseRequest(request);
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
		
		// comprobamos validez de todos los parametros e insertamos en db si todo ok
		if (parametros.get(PARAM_APARTADO) != null) {
			ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(Formateador.leeParametroInteger((String) parametros.get(PARAM_APARTADO))); 
			bean.setApartado(apartado);
			bean.setItems(ModeloBaremacionItems.obtenerInstancia().getItemsDeApartado(apartado));
			
			if (parametros.get(PARAM_ITEM) != null) {
				Merito merito = this.validarMerito(parametros);
				
				ModeloMerito.obtenerInstancia().insertaMerito(merito, bean.getUsuarioLogeado());
							
				BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
				datos.setRespuestaEnviada(true);
				response.sendRedirect(request.getServletPath());
			}
		}
	}
	
	private void eliminarMeritos(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		
		Gson gson = new GsonBuilder().create();
		
		List<String> idMeritos = new ArrayList<>();
		try {
			idMeritos = gson.fromJson(request.getParameter(PARAM_MERITOS), new TypeToken<List<String>>() { }.getType());
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(MENSAJE_ERROR_MERITOS_SELECCIONADOS_INCORRECTOS, bean, request);
		}
		
		// comprobamos si lo mérito son los del usuario logeado
		List<Merito> meritos = new ArrayList<>();
		for (String idMerito : idMeritos) {
			Merito m = modelo.getMeritoById(Formateador.leeParametroInteger(idMerito));
			if (!m.getUsuario().getCodNum().equals(bean.getUsuarioLogeado().getCodNum())) {
				throw new UVException("No tienes permisos");
			}
			if (!ModeloSolicitud.obtenerInstancia().comprobarMeritoPuedeSerBorrado(m)) {
				BolsaEmpleoUtils.addMensajeDeError(String.format(MENSAJE_ERROR_ELIMINAR, idMerito), bean, request);
			} else {
				meritos.add(m);
			}
		}
		
		if (!meritos.isEmpty()) {
			modelo.eliminarMeritos(meritos, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR, bean, request);
		}
		
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void listadoMeritos(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Merito> dataTable = modelo.listaMeritosDatatable(request.getParameterMap(), bean.getUsuarioLogeado());
				bean.setDatatable(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
					LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
					LOGGER.log(Level.SEVERE, e.toString());
				} else {
					LOGGER.log(Level.WARNING, e.toString());
				}
				bean.getMensajesDeError().add(e.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, e.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private Merito validarMerito(HashMap<String, Object> parametros) throws UVException, SQLException {
		Merito merito = new Merito();
		
		// item de baremación
		ItemBaremacion item = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(Formateador.leeParametroInteger((String) parametros.get(PARAM_ITEM)));
		merito.setItemBaremacion(item);
		
		// valor
		merito.setValor(ModeloMerito.validateValorDelMerito((String) parametros.get(PARAM_VALOR), merito));
		
		// descripcion
		merito.setDescripcion(EscapaHTML.ajustaCodificacion((String) parametros.get(PARAM_DESCRIPCION)));
		if (merito.getDescripcion() == null || merito.getDescripcion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_DESCRIPCION_VACIA);
		}
		if (merito.getDescripcion().length() > ModeloMerito.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_LARGO, ModeloMerito.COLUMN_DESCRIPCION_MAXLENGTH));
		}
		
		// observaciones
		merito.setObservacion(EscapaHTML.ajustaCodificacion((String) parametros.get(PARAM_OBSERVACION)));
		if (merito.getObservacion() != null && merito.getObservacion().length() > ModeloMerito.COLUMN_OBSERVACION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_OBSERVACION_LARGO, ModeloMerito.COLUMN_OBSERVACION_MAXLENGTH));
		}
		
		// fichero
		merito.setArchivo((InputStream) parametros.get(PARAM_ARCHIVO));
		
		return merito;
	}
	
}
