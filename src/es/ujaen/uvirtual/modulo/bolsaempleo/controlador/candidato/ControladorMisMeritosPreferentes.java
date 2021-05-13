package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritos;
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
	public static final String ACCION_AGREGAR_MERITO = "agregarmerito";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_ELIMINAR_MERITOS = "eliminarmeritos";
	public static final String ACCION_INDEX = "listar";
	
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
	public static final String RUTA_BEP_MERITOS = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mismeritospreferentes/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/mismeritospreferentes";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
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
			init(bean, datos);
			switch (nombreAccion) {
				case ACCION_INDEX:
					listaMeritos(bean);
					break;
				case ACCION_DATATABLE:
					listadoMeritos(bean, datos, request, response);
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
	
	private void init(VistaMeritosPreferentesCandidato bean, UVDatos datos) throws SQLException, UVException {
		bean.setVista(RUTA_BEP_MERITOS + "index.jsp");
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
	
	/**
	 * lista meritos .
	 * @param bean .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	private void listaMeritos(VistaMeritosPreferentesCandidato bean) {
		bean.setVista(RUTA_BEP_MERITOS + "index.jsp");
	}
	
	/** Listado de méritos .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 */
	private void listadoMeritos(VistaMeritosPreferentesCandidato bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
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
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
}
