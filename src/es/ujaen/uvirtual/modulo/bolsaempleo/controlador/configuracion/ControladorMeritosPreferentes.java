package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteOpcion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentes;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los méritos preferentes.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.meritospreferentes", 
	description = "Gestión de los méritos preferentes", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/meritospreferentes", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/meritospreferentes",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/meritospreferentes", 
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/meritospreferentes"
	})
public class ControladorMeritosPreferentes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorMeritosPreferentes.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones 
	public static final String ACCION_INDEX = "indice";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_MODIFICAR = "detalle";
	public static final String ACCION_NUEVO_MERITO = "nuevoMerito";
	public static final String ACCION_NUEVO_MERITO_CONFIRM = "nuevoMeritoConfirm";
	public static final String ACCION_MODIFICAR_MERITO = "modificarMerito";
	public static final String ACCION_MODIFICAR_MERITO_CONFIRM = "modificarMeritoConfirm";
	public static final String ACCION_DESACTIVAR = "desactivarmerito";
	public static final String ACCION_ACTIVAR = "activarmerito";
	public static final String ACCION_DATATABLE_OPCIONES = "datatableopciones";
	public static final String ACCION_NUEVA_OPCION = "nuevaopcion";
	public static final String ACCION_NUEVA_OPCION_CONFIRM = "nuevaopcionconfirm";
	public static final String ACCION_DESACTIVAR_OPCION = "desactivaropcion";
			
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ID = "id";
	public static final String PARAM_ID_OPCION = "idOpcion";
	public static final String PARAM_MERITO_CODIGO = "codigo";
	public static final String PARAM_MERITO_NOMBRE = "nombre";
	public static final String PARAM_MERITO_OBSERVACIONES = "observaciones";
	public static final String PARAM_MERITO_TIPO = "tipo";
	public static final String PARAM_MERITO_TIPO_TITULACION = "tipoTitulacion";
	public static final String PARAM_MERITO_TIPO_ITEMBAREMACION = "tipoItemBaremacion";
	public static final String PARAM_MERITO_APLICABLE = "aplicable";
	public static final String PARAM_MERITO_APLICABLE_BLOQUE = "aplicableBloque";
	public static final String PARAM_MERITO_APLICABLE_APARTADO = "aplicableApartado";
	public static final String PARAM_MERITO_APLICABLE_ITEM = "aplicableItem";
	public static final String PARAM_MERITO_VALORMAXIMO = "valorMaximo";
	public static final String PARAM_MERITO_TIPO_CALCULO = "tipocalculo";
	public static final String PARAM_MERITO_FACTOR_FACTOR = "factor_factor";
	public static final String PARAM_MERITO_FACTOR_POR_VALOR_MERITO = "factor_valor_merito";
	public static final String PARAM_MERITO_FACTOR_POR_VALOR_MERITO_MAYOR_QUE = "factor_valor_merito_mayor_que";	
	public static final String PARAM_MERITO_BASE_POR_VALOR_MERITO = "base_valor_merito";
	public static final String PARAM_MERITO_BASE_POR_VALOR_MERITO_MAYOR_QUE = "base_valor_merito_mayor_que";
	public static final String PARAM_MERITO_OPCION_NOMBRE = "nombre";
	public static final String PARAM_MERITO_OPCION_FACTOR = "factor";
	
	// mensajes
	public static final String MENSAJE_ERROR_CODIGO_VACIO = "El código no puede estar vacio";
	public static final String MENSAJE_ERROR_FACTOR_NO_VALIDO = "Introduce un valor válido para factor";	
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/meritospreferentes/";
	public static final String JSP_INDEX = RUTA_BEP_CONF + "indexMeritosPreferentes.jsp";
	public static final String JSP_FORM = RUTA_BEP_CONF + "formMeritosPreferentes.jsp";
	public static final String JSP_FORM_OPCION = RUTA_BEP_CONF + "formMeritosPreferentesOpcion.jsp";
	
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/meritospreferentes";
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
		
		VistaMeritosPreferentes bean = new VistaMeritosPreferentes();		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
				
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					indice(bean);
					break;
				case ACCION_DATATABLE:
					datatable(bean, datos, request, response);					
					break;
				case ACCION_DATATABLE_OPCIONES:
					datatableOpciones(bean, datos, request, response);
					break;
				case ACCION_NUEVO_MERITO:
					nuevoMerito(bean);
					break;
				case ACCION_NUEVO_MERITO_CONFIRM:
					nuevoMeritoConfirm(bean, request, response);
					break;
				case ACCION_MODIFICAR:
					editarMerito(bean, request);
					break;
				case ACCION_MODIFICAR_MERITO_CONFIRM:
					editarMeritoConfirm(bean, request, response);
					break;
				case ACCION_ACTIVAR:
					activarMerito(bean, request, response);
					break;
				case ACCION_DESACTIVAR:
					desactivarMerito(bean, request, response);
					break;
				case ACCION_NUEVA_OPCION:
					nuevaOpcion(bean, request);
					break;
				case ACCION_NUEVA_OPCION_CONFIRM:
					nuevaOpcionConfirm(bean, request, response);
					break;
				case ACCION_DESACTIVAR_OPCION:
					desactivarOpcion(bean, request, response);
					break;
				default:
					errorFatal(bean, "Acción no contemplada");
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
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	private void init(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		this.indice(bean);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
		}		
	}
	
	private void errorFatal(VistaMeritosPreferentes bean, String mensaje) {
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
	
	private void indice(VistaMeritosPreferentes bean) {
		bean.setVista(JSP_INDEX);
	}
	
	private void datatable(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<MeritoPreferente> dataTable = ModeloMeritosPreferentes.obtenerInstancia().
						listadoMeritosPreferentes(request.getParameterMap());
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
	
	private void datatableOpciones(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().
						getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
				BolsaEmpleoDataTable<MeritoPreferenteOpcion> dataTable = ModeloMeritosPreferentes.obtenerInstancia().
						listadoOpcionesMeritosPreferentes(request.getParameterMap(), merito);
				bean.setDatatableOpciones(dataTable);
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
	
	private void nuevoMerito(VistaMeritosPreferentes bean) throws SQLException, UVException {
		bean.setMeritoPreferente(null);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacionActivosOrdenadosPorCodigo());
		bean.setVista(JSP_FORM);		
	}
	
	private void nuevoMeritoConfirm(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setMeritoPreferente(null);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacionActivosOrdenadosPorCodigo());
		bean.setVista(JSP_FORM);
		
		MeritoPreferente merito = new MeritoPreferente();
		merito.setActivo(true);
		
		ModeloMeritosPreferentes.obtenerInstancia().crearMeritoPreferente(this.validate(merito, request), bean.getUsuarioLogeado());
					
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente añadido correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void editarMerito(VistaMeritosPreferentes bean, HttpServletRequest request) throws SQLException, UVException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		
		bean.setMeritoPreferente(merito);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacionActivosOrdenadosPorCodigo());
		bean.setVista(JSP_FORM);
	}
	
	private void editarMeritoConfirm(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
				
		bean.setMeritoPreferente(merito);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacionActivosOrdenadosPorCodigo());
		bean.setVista(JSP_FORM);
		
		ModeloMeritosPreferentes.obtenerInstancia().editarMeritoPreferente(this.validate(merito, request), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente editado correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
		
	private void activarMerito(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		
		ModeloMeritosPreferentes.obtenerInstancia().cambiaFlagActivoMeritoPreferente(merito, "S", bean.getUsuarioLogeado());
		bean.setVista(JSP_INDEX);
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente activado correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void desactivarMerito(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		
		ModeloMeritosPreferentes.obtenerInstancia().cambiaFlagActivoMeritoPreferente(merito, "N", bean.getUsuarioLogeado());
		bean.setVista(JSP_INDEX);
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente activado correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}

	private void nuevaOpcion(VistaMeritosPreferentes bean, HttpServletRequest request) throws SQLException, UVException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		bean.setMeritoPreferente(merito);
		bean.setVista(JSP_FORM_OPCION);
	}
	
	private void nuevaOpcionConfirm(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		
		bean.setMeritoPreferente(merito);
		bean.setVista(JSP_FORM_OPCION);

		ModeloMeritosPreferentes.obtenerInstancia().crearMeritoPreferenteOpcion(this.validateOpcionMerito(merito, new MeritoPreferenteOpcion(), request),
				bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito("Opción mérito preferente añadido correctamente", bean, request);
		response.sendRedirect(request.getServletPath() + "?" + PARAM_ACCION + "=" + ACCION_MODIFICAR + "&" + PARAM_ID + "=" + merito.getCodNum());		
	}
	
	private void desactivarOpcion(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		MeritoPreferenteOpcion meritoOpcion = ModeloMeritosPreferentes.obtenerInstancia().
				getMeritoPreferenteOpcionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID_OPCION)));
		
		if (!meritoOpcion.getMeritoPreferenteCodNum().equals(merito.getCodNum())) {
			throw new UVException("La opción del mérito no es válida");
		}
		
		ModeloMeritosPreferentes.obtenerInstancia().desactivarMeritoPreferenteOpcion(meritoOpcion, bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito("Opción mérito preferente desactivado correctamente", bean, request);
		response.sendRedirect(request.getServletPath());		
	}
	
	private MeritoPreferente validate(MeritoPreferente merito, HttpServletRequest request) throws UVException, SQLException {
		merito.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_CODIGO)));
		if (merito.getCodigo().isBlank()) {
			throw new UVException("El código del mérito es requerido");
		}
		if (merito.getCodigo().length() > ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_CODIGO) {
			throw new UVException("El código del mérito tiene demasiados caracteres. Máximo" + ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_CODIGO);
		}
		if (ModeloMeritosPreferentes.obtenerInstancia().isMeritoActivoConCodigo(merito)) {
			throw new UVException("Ya existe un mérito preferente activo con el código introducido");
		}
		
		this.validateNombreObservaciones(merito, request);

		this.validateAplicable(merito, request);
		
		merito.setTipo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_TIPO)));		
		if (merito.getTipo().equals(ModeloMeritosPreferentes.TIPO_MERITO)) {
			merito.setTipoItemBaremacion(ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_TIPO_ITEMBAREMACION))));
		} else if (!merito.getTipo().equals(ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE) && !merito.getTipo().equals(ModeloMeritosPreferentes.TIPO_POSESION)) {
			throw new UVException("Introduce un tipo de mérito");
		}
		
		this.validateTipoCalculo(merito, request);
		
		return merito;
	}
	
	private MeritoPreferente validateNombreObservaciones(MeritoPreferente merito, HttpServletRequest request) throws UVException {
		merito.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_NOMBRE)));
		if (merito.getNombre().isBlank()) {
			throw new UVException("El nombre del mérito es requerido");
		}
		if (merito.getNombre().length() > ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_NOMBRE) {
			throw new UVException("El nombre tiene demasiados caracteres. Máximo: " + ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_NOMBRE);
		}
		
		merito.setObservaciones(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_OBSERVACIONES)));
		if (merito.getObservaciones().length() > ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_OBSERVACIONES) {
			throw new UVException("Las observaciones tienen demasiados caracteres. Máximo: " + ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_OBSERVACIONES);
		}
		
		return merito;
	}
	
	private MeritoPreferente validateAplicable(MeritoPreferente merito, HttpServletRequest request) throws UVException, SQLException {
		merito.setAplicable(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_APLICABLE)));
		if (merito.getAplicable().equals(ModeloMeritosPreferentes.APLICABLE_BLOQUE)) {
			merito.setAplicableBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_APLICABLE_BLOQUE))));	
		} else if (merito.getAplicable().equals(ModeloMeritosPreferentes.APLICABLE_APARTADO)) {
			merito.setAplicableApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_APLICABLE_APARTADO))));
		} else if (merito.getAplicable().equals(ModeloMeritosPreferentes.APLICABLE_ITEM)) {
			merito.setAplicableItemBaremacion(ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_APLICABLE_ITEM))));
		} else if (!merito.getAplicable().equals(ModeloMeritosPreferentes.APLICABLE_TOTAL)) {
			throw new UVException("Introduce el campo aplicable");
		}
		
		return merito;
	}
	
	private MeritoPreferente validateTipoCalculo(MeritoPreferente merito, HttpServletRequest request) throws UVException {
		merito.setTipoCalculo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_TIPO_CALCULO)));
		
		if (merito.getTipoCalculo().equals(ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR)) {
			this.validateTipoCalculoFactor(merito, request);
		} else if (merito.getTipoCalculo().equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR)) {
			this.validateTipoCalculoValorMerito(merito, request);
		} else if (merito.getTipoCalculo().equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE)) {
			this.validateTipoCalculoValorMeritoMayorQue(merito, request);
		} else if (merito.getTipoCalculo().equals(ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES)) {
			merito.setFactor(0.0);
		} else {
			throw new UVException("Tipo de cálculo no válido");
		}
		
		return merito;
	}
	
	private MeritoPreferente validateTipoCalculoFactor(MeritoPreferente merito, HttpServletRequest request) throws UVException {
		merito.setFactor(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_FACTOR_FACTOR)));
		if (merito.getFactor() == null) {
			throw new UVException(MENSAJE_ERROR_FACTOR_NO_VALIDO);
		}
		
		return merito;
	}
	
	private MeritoPreferente validateTipoCalculoValorMerito(MeritoPreferente merito, HttpServletRequest request) throws UVException {
		merito.setBase(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_BASE_POR_VALOR_MERITO)));
		if (merito.getBase() == null) {
			throw new UVException("Introduce un valor válido para base");
		}
		
		merito.setFactor(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_FACTOR_POR_VALOR_MERITO)));
		if (merito.getFactor() == null) {
			throw new UVException(MENSAJE_ERROR_FACTOR_NO_VALIDO);
		}
		
		merito.setValorMaximo(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_VALORMAXIMO)));
		if (merito.getValorMaximo() != null && merito.getValorMaximo() <= 0) {
			throw new UVException("El valor máximo debe ser mayor que cero");
		}
		
		return merito;
	}
	
	private MeritoPreferente validateTipoCalculoValorMeritoMayorQue(MeritoPreferente merito, HttpServletRequest request) throws UVException {
		merito.setBase(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_BASE_POR_VALOR_MERITO_MAYOR_QUE)));
		if (merito.getBase() == null) {
			throw new UVException("Introduce un valor válido para valor mínimo mérito");
		}
		
		merito.setFactor(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_FACTOR_POR_VALOR_MERITO_MAYOR_QUE)));
		if (merito.getFactor() == null) {
			throw new UVException(MENSAJE_ERROR_FACTOR_NO_VALIDO);
		}
		
		return merito;
	}

	private MeritoPreferenteOpcion validateOpcionMerito(MeritoPreferente merito, MeritoPreferenteOpcion meritoOpcion, HttpServletRequest request) throws UVException {
		meritoOpcion.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_OPCION_NOMBRE)));
		if (meritoOpcion.getNombre().isBlank()) {
			throw new UVException("El nombre de la opción es requerida");
		}
		if (meritoOpcion.getNombre().length() > ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_NOMBRE_OPCION) {
			throw new UVException("El nombre de la opción tiene demasiados caracteres. Máximo" + ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_NOMBRE_OPCION);
		}
		
		meritoOpcion.setFactor(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_OPCION_FACTOR)));
		if (meritoOpcion.getFactor() == null) {
			throw new UVException("El factor es requerido");
		}
		
		meritoOpcion.setMeritoPreferenteCodNum(merito.getCodNum());
		
		return meritoOpcion;
	}
}
