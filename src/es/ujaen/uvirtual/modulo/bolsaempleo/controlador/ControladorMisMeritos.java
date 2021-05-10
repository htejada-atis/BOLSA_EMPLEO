package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

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
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
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
		description = "Seleccionar méritos a baremar por las comisiones", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/mismeritos", 
				"/srv/en/informacionadministrativa/bolsaempleo/mismeritos",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/mismeritos", 
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/mismeritos"
		})
@MultipartConfig
public class ControladorMisMeritos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMisMeritos.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_AGREGAR_MERITO = "agregarmerito";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_ELIMINAR_MERITOS = "eliminarmeritos";
	public static final String ACCION_LISTAR = "listar";
	
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
	public static final String RUTA_BEP_MERITOS = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mismeritos/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/mismeritos";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaMeritos bean = new VistaMeritos();		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
				
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR;
		}
		
		try {
			modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_AGREGAR_MERITO:
					agregarMerito(bean, datos, request, response);
					break;
				case ACCION_DATATABLE:
					listadoMeritos(bean, datos, request, response);
					break;
				case ACCION_DESCARGAR_FICHERO:
					descargarFichero(bean, datos, request, response);
					break;
				case ACCION_ELIMINAR_MERITOS:
					eliminarMeritos(request, response);
					break;
				case ACCION_LISTAR:
					listaMeritos(bean);
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} catch (ServletException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.toString());
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
	
	/**
	 * lista meritos .
	 * @param bean .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	private void listaMeritos(VistaMeritos bean) throws SQLException, UVException {
		bean.setVista(RUTA_BEP_MERITOS + "index.jsp");
		
		ModeloBaremacion modeloBaremacion = ModeloBaremacion.obtenerInstancia();
		bean.setApartados(modeloBaremacion.getApartadosActivos());
	}
	
	/** agrega un nuevo mérito.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 * @throws ServletException .
	 */
	private void agregarMerito(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException, ServletException {
		bean.setVista(RUTA_BEP_MERITOS + "formMerito.jsp");
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		bean.setApartados(modelo.getApartadosActivos());
		
		if (request.getParameter(PARAM_APARTADO) != null) {
			ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO))); 
			bean.setApartado(apartado);
			bean.setItems(modelo.getItemsDeApartado(apartado));
			
			if (request.getParameter(PARAM_ITEM) != null) {
				Part uploadedFile = request.getPart(PARAM_ARCHIVO);
				Merito merito = this.validarMerito(request, uploadedFile);
				
				// TODO: un metodo en ModeloUsuarioBolsaEmpleo para obtener un usuario adecuadamente
				Usuario usuArcos = datos.getUsuario();
				Integer idUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().listaUsuario(usuArcos.getUid()).getCodNum();
				ModeloMerito.obtenerInstancia().insertaMerito(merito, idUsuario);
							
				HttpSession session = request.getSession(false);
				session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR);
				response.sendRedirect(request.getServletPath());
			}
		}
		
	}
	
	/** descarga un fichero .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void descargarFichero(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ID)) != null) {
			Merito merito = modelo.listaMerito(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
			bean.setMerito(merito);
			
			response.setContentType("application/pdf");
			datos.setRespuestaEnviada(true);
	        
			try (ServletOutputStream stream = response.getOutputStream(); BufferedInputStream buf = new BufferedInputStream(merito.getArchivo())) {
				int readBytes = 0;
				while ((readBytes = buf.read()) != -1) {
					stream.write(readBytes);
	            }
				stream.flush();
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
	        }
		}
	}
	
	/** elimina una lista de méritos seleccionados .
	 * @param request .
	 * @param response .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws IOException .
	 */
	private void eliminarMeritos(HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		
		Gson gson = new GsonBuilder().create();
		
		try {
			List<String> meritos = gson.fromJson(request.getParameter(PARAM_MERITOS), new TypeToken<List<String>>() { }.getType());
			modelo.eliminarMeritos(meritos);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_ELIMINAR);
			response.sendRedirect(request.getServletPath());
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new UVException(MENSAJE_ERROR_ELIMINAR_MERITO);
		} catch (Exception ex) {
			throw new UVException(MENSAJE_ERROR_MERITOS_SELECCIONADOS_INCORRECTOS);
		}
	}
	
	/** Listado de méritos .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 */
	private void listadoMeritos(VistaMeritos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
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
				BolsaEmpleoDataTable<Merito> dataTable = modelo.listaMeritosDatatable(request.getParameterMap(), idUsuario);
				bean.setDatatable(dataTable);
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
	
	private Merito validarMerito(HttpServletRequest request, Part uploadedFile) throws UVException, SQLException, IOException {		
		Merito merito = new Merito();
		
		merito.setArchivo(this.validateFicheroMerito(uploadedFile));
		BolsaEmpleoUtils.checkFileSize(merito.getArchivo());
		
		// valor
		merito = this.validateValorDelMerito(request, new Merito());
				
		// descripcion
		merito.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_DESCRIPCION)));
		if (merito.getDescripcion() == null || merito.getDescripcion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_DESCRIPCION_VACIA);
		}
		if (merito.getDescripcion().length() > ModeloMerito.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_LARGO, ModeloMerito.COLUMN_DESCRIPCION_MAXLENGTH));
		}
		
		// observaciones
		merito.setObservacion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_OBSERVACION)));
		if (merito.getObservacion() != null && merito.getObservacion().length() > ModeloMerito.COLUMN_OBSERVACION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_OBSERVACION_LARGO, ModeloMerito.COLUMN_OBSERVACION_MAXLENGTH));
		}
		
		return merito;
	}
	
	private InputStream validateFicheroMerito(Part uploadedFile) throws UVException {
		// TODO: mover estos chequeos a BolsaEmpleoUtils
		
		// comprobamos si hay fichero
		if (uploadedFile == null) {
			throw new UVException("El fichero es requerido");
		}
		if (uploadedFile.getSize() == 0) {
			throw new UVException("El fichero es requerido");
		}
		
		// comprobamos si es pdf
		String nombre = BolsaEmpleoUtils.obtenerNombreFichero(uploadedFile);
		int i = nombre.lastIndexOf('.');
		if (i > 0) {
			String extension = nombre.substring(i + 1);
			if (!"pdf".equalsIgnoreCase(extension)) {
				throw new UVException("No se puede subir un fichero que sea distinto de pdf");
			}
		}
		
		try {
			return uploadedFile.getInputStream();
		} catch (IOException ex) {
			LOGGER.log(Level.WARNING, ex.toString());
			throw new UVException("Error guardando fichero");
		}		
	}
	
	private Merito validateValorDelMerito(HttpServletRequest request, Merito merito) throws UVException, SQLException {
		// item de baremación
		ItemBaremacion item = ModeloBaremacion.obtenerInstancia().getItemBaremacionById(Formateador.leeParametroInteger(PARAM_ITEM));
		merito.setItemBaremacion(item);
		
		// chequeo tipo de valor
		String valorStr = request.getParameter(PARAM_VALOR);
		switch (item.getUnidades()) {
			case ModeloBaremacion.ITEM_UNIDADES_MEDICION_SINO:
				merito.setValor((float) 1);
				break;
			case ModeloBaremacion.ITEM_UNIDADES_MEDICION_ENTERO:
				if (!BolsaEmpleoUtils.isInteger(valorStr)) {
					throw new UVException(MENSAJE_ERROR_VALOR_ENTERO_NO_PERMITIDO);
				}
				break;
			case ModeloBaremacion.ITEM_UNIDADES_MEDICION_DECIMAL:
				if (!BolsaEmpleoUtils.isFloat(valorStr)) {
					throw new UVException(MENSAJE_ERROR_VALOR_DECIMAL_NO_PERMITIDO);
				}
				break;
			default:
				throw new UVException("Tipo de unidad no válido");
		}
		
		// chequeo máximo y mínimo
		Float valor = BolsaEmpleoUtils.leeParametroFloat(valorStr);
		if (valor == null) {
			throw new UVException("El valor no es válido");
		}
		if (valor < item.getValorMinimo()) {
			throw new UVException(String.format(MENSAJE_ERROR_VALOR_MINIMO_PERMITIDO, item.getValorMinimo()));
		}
		if (valor > item.getValorMaximo()) {
			throw new UVException(String.format(MENSAJE_ERROR_VALOR_MAXIMO_PERMITIDO, item.getValorMaximo()));
		}
		merito.setValor(valor);
		
		return merito;
	}
}
