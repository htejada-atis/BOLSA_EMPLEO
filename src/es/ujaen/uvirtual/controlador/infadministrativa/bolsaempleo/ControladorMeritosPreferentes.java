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
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloTitulacion;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoValidator;
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
		bean.setTitulaciones(ModeloTitulacion.obtenerInstancia().listaTitulaciones());
		bean.setItemsBaremacion(ModeloBaremacion.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacion.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacion.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(RUTA_BEP_CONF + "formMeritosPreferentes.jsp");		
	}
	
	private void nuevoMeritoConfirm(VistaMeritosPreferentes bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		bean.setMeritoPreferente(null);
		bean.setTitulaciones(ModeloTitulacion.obtenerInstancia().listaTitulaciones());
		bean.setItemsBaremacion(ModeloBaremacion.obtenerInstancia().listaItemBaremacion());
		bean.setBloqueBaremacion(ModeloBaremacion.obtenerInstancia().listaBloqueBaremacion());
		bean.setApartadoBaremacion(ModeloBaremacion.obtenerInstancia().listaApartadoBaremacion());
		bean.setVista(RUTA_BEP_CONF + "formMeritosPreferentes.jsp");
		
		BolsaEmpleoValidator validator = this.getValidator(request);		
		if (!validator.isValid()) {
			for (String param : validator.getErrors().keySet()) {
				for (String paramError : validator.getErrors().get(param)) {
					bean.getMensajesDeError().add(paramError);
				}
			}
			return;
		}
	}
	
	private BolsaEmpleoValidator getValidator(HttpServletRequest request) throws UVException {
		BolsaEmpleoValidator validator = new BolsaEmpleoValidator(request);
		validator.addParamString(PARAM_MERITO_DESCRIPCION);
		validator.addRule(PARAM_MERITO_DESCRIPCION, "required", "Introduce la descripción");
		validator.addRule(PARAM_MERITO_DESCRIPCION, "noBlank", "Introduce la descripción");
		
		validator.addParamString(PARAM_MERITO_TIPO);
		validator.addRule(PARAM_MERITO_TIPO, "required", "Introduce el tipo de mérito");
		validator.addRule(PARAM_MERITO_TIPO, "noBlank", "Introduce la descripción");
		validator.addParamString(PARAM_MERITO_TIPO_TITULACION);
		validator.addParamString(PARAM_MERITO_TIPO_ITEMBAREMACION);
		
		validator.addParamString(PARAM_MERITO_APLICABLE);
		validator.addRule(PARAM_MERITO_APLICABLE, "required", "Introduce el campo aplicable");
		validator.addRule(PARAM_MERITO_APLICABLE, "noBlank", "Introduce el campo aplicable");
		validator.addParamString(PARAM_MERITO_APLICABLE_BLOQUE);
		validator.addParamString(PARAM_MERITO_APLICABLE_APARTADO);
		validator.addParamString(PARAM_MERITO_APLICABLE_ITEM);
		
		validator.addParamString(PARAM_MERITO_FACTOR);
		validator.addRule(PARAM_MERITO_FACTOR, "required", "Introduce factor");
		validator.addRule(PARAM_MERITO_FACTOR, "noBlank", "Introduce factor");
				
		validator.addParamFloat(PARAM_MERITO_VALORMAXIMO);
				
		return validator;
	}
}
