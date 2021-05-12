package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

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
			
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ID = "id";
	public static final String PARAM_MERITO_CODIGO = "codigo";
	public static final String PARAM_MERITO_DESCRIPCION = "descripcion";
	public static final String PARAM_MERITO_TIPO = "tipo";
	public static final String PARAM_MERITO_TIPO_TITULACION = "tipoTitulacion";
	public static final String PARAM_MERITO_TIPO_ITEMBAREMACION = "tipoItemBaremacion";
	public static final String PARAM_MERITO_APLICABLE = "aplicable";
	public static final String PARAM_MERITO_APLICABLE_BLOQUE = "aplicableBloque";
	public static final String PARAM_MERITO_APLICABLE_APARTADO = "aplicableApartado";
	public static final String PARAM_MERITO_APLICABLE_ITEM = "aplicableItem";
	public static final String PARAM_MERITO_FACTOR = "factor";
	public static final String PARAM_MERITO_BASE = "base";	
	public static final String PARAM_MERITO_VALORMAXIMO = "valorMaximo";
	public static final String PARAM_MERITO_TIPO_CALCULO = "tipocalculo";
	
	// mensajes
	public static final String MENSAJE_ERROR_CODIGO_VACIO = "El código no puede estar vacio";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/meritospreferentes/";
	public static final String JSP_INDEX = RUTA_BEP_CONF + "indexMeritosPreferentes.jsp";
	public static final String JSP_FORM = RUTA_BEP_CONF + "formMeritosPreferentes.jsp";
	
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
			init(bean, datos, request);
			switch (nombreAccion) {
				case ACCION_INDEX:
					indice(bean);
					break;
				case ACCION_DATATABLE:
					datatable(bean, datos, request, response);					
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
				default:
					errorFatal(bean, "Acción no contemplada");
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add(e.toString());
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
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
	
	private void init(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request) throws SQLException, UVException {
		this.indice(bean);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);		
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
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void nuevoMerito(VistaMeritosPreferentes bean) throws SQLException, UVException {
		bean.setMeritoPreferente(null);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(JSP_FORM);		
	}
	
	private void nuevoMeritoConfirm(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setMeritoPreferente(null);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(JSP_FORM);
		
		MeritoPreferente merito = new MeritoPreferente();
		merito.setActivo(true);
		
		ModeloMeritosPreferentes.obtenerInstancia().crearMeritoPreferente(this.validate(merito, request));
					
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente añadido correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void editarMerito(VistaMeritosPreferentes bean, HttpServletRequest request) throws SQLException, UVException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		
		bean.setMeritoPreferente(merito);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(JSP_FORM);
	}
	
	private void editarMeritoConfirm(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
				
		bean.setMeritoPreferente(merito);
		bean.setItemsBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(JSP_FORM);
		
		ModeloMeritosPreferentes.obtenerInstancia().editarMeritoPreferente(this.validate(merito, request));
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente editado correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	
	private void activarMerito(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		
		ModeloMeritosPreferentes.obtenerInstancia().cambiaFlagActivoMeritoPreferente(merito, "S");
		bean.setVista(JSP_INDEX);
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente activado correctamente", bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void desactivarMerito(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(Formateador.leeParametroInteger(request.getParameter(PARAM_ID)));
		
		ModeloMeritosPreferentes.obtenerInstancia().cambiaFlagActivoMeritoPreferente(merito, "N");
		bean.setVista(JSP_INDEX);
		
		BolsaEmpleoUtils.addMensajeDeExito("Mérito preferente activado correctamente", bean, request);
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
		if (ModeloMeritosPreferentes.obtenerInstancia().isMeritoActivoConCodigo(merito.getCodigo())) {
			throw new UVException("Ya existe un mérito preferente activo con el código introducido");
		}
		
		merito.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_DESCRIPCION)));
		if (merito.getDescripcion().isBlank()) {
			throw new UVException("La descripción del mérito es requerida");
		}
		if (merito.getDescripcion().length() > ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_DESCRIPCION) {
			throw new UVException("La descripción tiene demasiados caracteres. Máximo: " + ModeloMeritosPreferentes.MAX_LENGTH_COLUMN_DESCRIPCION);
		}

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
			merito.setFactor(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_FACTOR)));
			if (merito.getFactor() == null) {
				throw new UVException("Introduce un valor válido para factor");
			}
		} else if (merito.getTipoCalculo().equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR)) {
			merito.setBase(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_BASE)));
			if (merito.getBase() == null) {
				throw new UVException("Introduce un valor válido para base");
			}
			
			merito.setFactor(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_FACTOR)));
			if (merito.getFactor() == null) {
				throw new UVException("Introduce un valor válido para factor");
			}
			
			merito.setValorMaximo(Formateador.leeParametroDouble(request.getParameter(PARAM_MERITO_VALORMAXIMO)));
			if (merito.getValorMaximo() != null && merito.getValorMaximo() <= 0) {
				throw new UVException("El valor máximo debe ser mayor que cero");
			}
		} else {
			throw new UVException("Tipo de cálculo no válido");
		}
		
		return merito;
	}
}
