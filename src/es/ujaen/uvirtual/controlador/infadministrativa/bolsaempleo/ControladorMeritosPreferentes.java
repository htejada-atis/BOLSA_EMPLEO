package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

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
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.MeritoPreferente;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaMeritosPreferentes;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBaremacion;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
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
	public static final String ACCION_DETALLE = "detalle";
	public static final String ACCION_NUEVO_MERITO = "nuevoMerito";
	public static final String ACCION_NUEVO_MERITO_CONFIRM = "nuevoMeritoConfirm";
	public static final String ACCION_MODIFICAR_MERITO = "modificarMerito";
	public static final String ACCION_MODIFICAR_MERITO_CONFIRM = "modificarMeritoConfirm";
			
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ID = "id";
	public static final String PARAM_MERITO_DESCRIPCION = "descripcion";
	public static final String PARAM_MERITO_TIPO = "tipo";
	public static final String PARAM_MERITO_TIPO_TITULACION = "tipoTitulacion";
	public static final String PARAM_MERITO_TIPO_ITEMBAREMACION = "tipoItemBaremacion";
	public static final String PARAM_MERITO_APLICABLE = "aplicable";
	public static final String PARAM_MERITO_APLICABLE_BLOQUE = "aplicableBloque";
	public static final String PARAM_MERITO_APLICABLE_APARTADO = "aplicableApartado";
	public static final String PARAM_MERITO_APLICABLE_ITEM = "aplicableItem";
	public static final String PARAM_MERITO_FACTOR = "factor";
	public static final String PARAM_MERITO_VALORMAXIMO = "valorMaximo";
	
	// mensajes
	public static final String MENSAJE_ERROR_CODIGO_VACIO = "El código no puede estar vacio";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/meritospreferentes";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/meritospreferentes/";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
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
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
			switch (nombreAccion) {
				case ACCION_INDEX:
					indice(bean, request, response);
					break;
				case ACCION_DATATABLE:
					datatable(bean, datos, request, response);					
					break;	
				case ACCION_NUEVO_MERITO:
					nuevoMerito(bean, datos, request, response);
					break;
				case ACCION_NUEVO_MERITO_CONFIRM:
					nuevoMeritoConfirm(bean, datos, request, response);
					break;
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
	
	/** Redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void indice(VistaMeritosPreferentes bean, HttpServletRequest request, HttpServletResponse response) {
		bean.setVista(RUTA_BEP_CONF + "indexMeritosPreferentes.jsp");
	}
	
	private void datatable(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<MeritoPreferente> dataTable = ModeloMeritosPreferentes.obtenerInstancia().
						listadoMeritosPreferentes(request.getParameterMap());
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				bean.setDatatable(dataTable);
				writer.write(gson.toJson(dataTable));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void nuevoMerito(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		bean.setMeritoPreferente(null);
		bean.setItemsBaremacion(ModeloBaremacion.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacion.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacion.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(RUTA_BEP_CONF + "formMeritosPreferentes.jsp");		
	}
	
	private void nuevoMeritoConfirm(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		bean.setMeritoPreferente(null);
		bean.setItemsBaremacion(ModeloBaremacion.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacion.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacion.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(RUTA_BEP_CONF + "formMeritosPreferentes.jsp");
		
		ModeloMeritosPreferentes.obtenerInstancia().crearMeritoPreferente(this.validate(request));
		bean.getMensajesDeExito().add("Mérito preferente añadido correctamente");
		bean.setVista(RUTA_BEP_CONF + "indexMeritosPreferentes.jsp");
	}
	
	private MeritoPreferente validate(HttpServletRequest request) throws UVException, SQLException {
		MeritoPreferente merito = new MeritoPreferente();
		merito.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_DESCRIPCION)));
		if (merito.getDescripcion().isBlank()) {
			throw new UVException("La descripción del mérito es requerida");
		}
		
		merito.setTipo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_TIPO)));		
		if (merito.getTipo().equals(ModeloMeritosPreferentes.TIPO_MERITO)) {
			merito.setTipoItemBaremacion(ModeloBaremacion.obtenerInstancia().getItemBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_TIPO_ITEMBAREMACION))));
		} else if (!merito.getTipo().equals(ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE)) {
			throw new UVException("Introduce un tipo de mérito");
		}
		
		merito.setAplicable(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_APLICABLE)));
		if (merito.getAplicable().equals(ModeloMeritosPreferentes.APLICABLE_BLOQUE)) {
			merito.setAplicableBloqueBaremacion(ModeloBaremacion.obtenerInstancia().getBloqueBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_APLICABLE_BLOQUE))));	
		} else if (merito.getAplicable().equals(ModeloMeritosPreferentes.APLICABLE_APARTADO)) {
			merito.setAplicableApartadoBaremacion(ModeloBaremacion.obtenerInstancia().getApartadoBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_APLICABLE_APARTADO))));
		} else if (merito.getAplicable().equals(ModeloMeritosPreferentes.APLICABLE_ITEM)) {
			merito.setAplicableItemBaremacion(ModeloBaremacion.obtenerInstancia().getItemBaremacionById(
					Formateador.leeParametroInteger(request.getParameter(PARAM_MERITO_APLICABLE_ITEM))));
		} else {
			throw new UVException("Introduce el campo aplicable");
		}
				
		merito.setFactor(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_MERITO_FACTOR)));
		if (merito.getFactor().isBlank()) {
			throw new UVException("El factor es requerido");
		}
		merito.setValorMaximo(Formateador.leeParametroFloat(request.getParameter(PARAM_MERITO_VALORMAXIMO)));
		
		return merito;
	}
}
