package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
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
@MultipartConfig
public class ControladorMisMeritosPreferentes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisMeritosPreferentes.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_AGREGAR_MERITO = "agregarmerito";	
	public static final String ACCION_AGREGAR_MERITO_CONFIRM = "agregarmeritoconfirm";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_ELIMINAR_MERITOS = "eliminarmeritos";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ID = "id";
	public static final String PARAM_MERITO_PREFERENTE = "meritoPreferente";
	public static final String PARAM_ARCHIVO = "archivo";
	public static final String PARAM_OBSERVACION = "observacion";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_MERITOS = "meritos";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	
	public static final String MENSAJE_ERROR_APARTADO_REQUERIDO = "Debe seleccionar un apartado";
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGO = "La descripción no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_DESCRIPCION_VACIA = "La descripción no puede estar vacía";
	public static final String MENSAJE_ERROR_ITEM_REQUERIDO = "Debe seleccionar un ítem";
	public static final String MENSAJE_ERROR_OBSERVACION_LARGO = "La observación no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_VALOR_VACIO = "El valor no puede estar vacio";
	public static final String MENSAJE_ERROR_VALOR_MAXIMO_PERMITIDO = "El valor máximo permitido es";
	public static final String MENSAJE_ERROR_VALOR_MINIMO_PERMITIDO = "El valor mínimo permitido es";
	public static final String MENSAJE_ERROR_MERITOS_SELECCIONADOS_INCORRECTOS = "No hay méritos seleccionados válidos";
	public static final String MENSAJE_ERROR_ELIMINAR_MERITO = "No se puede eliminar un mérito que ya está asociado a una solicitud";
	
	public static final String MENSAJE_ERROR_VALOR_DECIMAL_NO_PERMITIDO = "El valor debe ser decimal";
	public static final String MENSAJE_ERROR_VALOR_ENTERO_NO_PERMITIDO = "El valor debe ser entero";
	
	public static final String MENSAJE_EXITO_AGREGAR = "Mérito agregado correctamente";
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
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request);
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
					agregarMerito(bean, datos, request, response);
					break;
				case ACCION_DESCARGAR_FICHERO:
					descargarPdf(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
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
	
	private void init(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request) throws SQLException, UVException {
		this.index(bean);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
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
	
	private void listadoMeritos(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Usuario usuArcos = datos.getUsuario();
				ModeloUsuarioBolsaEmpleo modeloUsuarioBolsaEmpleo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
				UsuarioBolsaEmpleo usuario = modeloUsuarioBolsaEmpleo.listaUsuario(usuArcos.getDocumentoNumero());
				BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable = ModeloMeritosPreferentesCandidato.obtenerInstancia().
						listaMeritosCandidatoDatatable(request.getParameterMap(), usuario);
				bean.setDatatable(dataTable);
				writer.write(dataTable.toJson());
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void formularioAgregarMerito(VistaMeritosPreferentesCandidato bean) 
			throws SQLException, UVException {
		bean.setVista(JSP_FORMULARIO);
		bean.setMeritosPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritosPreferentesPorPosesion());
		// bean.setCodigoPadreMeritoPreferente(ModeloParametrosConfiguracion.obtenerInstancia().getParametroByNombre("codmeritopreferente"));
		bean.setCodigoPadreMeritoPreferente("IV");
	}
	
	private void agregarMerito(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		formularioAgregarMerito(bean);
				
		MeritoPreferenteUsuario mp = validateMeritoPreferenteUsuario(request, datos);		
		ModeloMeritosPreferentesCandidato.obtenerInstancia().insertaMeritoUsuario(mp);
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente añadido correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void descargarPdf(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		this.index(bean);
		
		MeritoPreferenteUsuario mpu = ModeloMeritosPreferentesCandidato.obtenerInstancia().getMeritoPreferenteUsuarioById(
				Formateador.leeParametroInteger(request.getParameter(PARAM_ID)), ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioCandidato(datos));
		
		response.setContentType("application/pdf");
		datos.setRespuestaEnviada(true);
        
		try (ServletOutputStream stream = response.getOutputStream(); BufferedInputStream buf = new BufferedInputStream(mpu.getArchivo())) {
			int readBytes = 0;
			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
            }
			stream.flush();
		} catch (Exception ex) {
			bean.getMensajesDeError().add(ex.getMessage());
        }		
	}
	
	private MeritoPreferenteUsuario validateMeritoPreferenteUsuario(HttpServletRequest request, UVDatos datos) throws SQLException, UVException {
		MeritoPreferenteUsuario mpu = new MeritoPreferenteUsuario();
		
		mpu.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(
				Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_PREFERENTE))));
		
		mpu.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioCandidato(datos));
		if (!mpu.getUsuario().isCandidato()) {
			throw new UVException("El usuario debe ser un candidato");
		}
		
		mpu.setDescripcion(request.getParameter(PARAM_OBSERVACION));
		if (mpu.getDescripcion() != null && mpu.getDescripcion().length() > ModeloMeritosPreferentesCandidato.MAX_LENGTH_COLUMN_DESCRIPCION) {
			throw new UVException(String.format("Los comentarios para la comisión no puede contener mas de %d caracteres", 
					ModeloMeritosPreferentesCandidato.MAX_LENGTH_COLUMN_DESCRIPCION));
		}
		
		try {
			Part uploadedFile = request.getPart(PARAM_ARCHIVO);
			if (!BolsaEmpleoUtils.checkFileIsPDF(uploadedFile)) {
				throw new UVException("El fichero debe ser un pdf válido");
			}
			mpu.setArchivo(uploadedFile.getInputStream());
			BolsaEmpleoUtils.checkFileSize(mpu.getArchivo());
		} catch (ServletException | IOException ex) {
			LOGGER.log(Level.WARNING, ex.toString());
			throw new UVException("Error guardando fichero");
		}
				
		return mpu;
	}
}
