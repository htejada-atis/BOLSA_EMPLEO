package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.Date;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoValidator;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConvocatorias;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Listado de bolsas y su estado.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.convocatorias", 
	description = "Gestión de convocatorias", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/convocatorias", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/convocatorias",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/convocatorias",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/convocatorias"
})
public class ControladorConvocatorias extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorConvocatorias.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_ENVIAR = "enviar"; 
	public static final String PARAM_CONVOCATORIA_ID = "id";
	public static final String PARAM_CONVOCATORIA_DESCRIPCION = "descripcion";
	public static final String PARAM_CONVOCATORIA_FECHACIERRE = "fechaCierre";
	public static final String PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO = "numBolsasMaximo";
	public static final String PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE = "numMeritosPorBloque";
	
	// acciones
	public static final String ACCION_LISTAR_CONVOCATORIAS = "listar";
	public static final String ACCION_DATATABLE = "datatable";
	public static final String ACCION_FORMULARIO_CONVOCATORIA = "formConvocatoria";
	public static final String ACCION_AGREGAR_CONVOCATORIA = "addConvocatoria";
	public static final String ACCION_FORMULARIO_EDITAR_CONVOCATORIA = "formEditarConvocatoria";
	public static final String ACCION_MODIFICAR_CONVOCATORIA = "editConvocatoria";
	public static final String ACCION_ABRIR_CONVOCATORIA = "abrirConvocatoria";
	public static final String ACCION_CERRAR_CONVOCATORIA = "cerrarConvocatoria";
	public static final String ACCION_BORRAR_CONVOCATORIA = "borrarConvocatoria";

	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_ELIMINAR = "Convocatoria eliminada correctamente";
	public static final String MENSAJE_ERROR_DESCRIPCION_LARGA = "La descripción no puede ser superior a %d";
	public static final String MENSAJE_ERROR_DESCRIPCION_VACIA = "La descripción no puede estar vacia";
	public static final String MENSAJE_ERROR_FECHACIERRE_INCORRECTA = "La fecha de cierre no tiene el formato DD/MM/YYYY";
	public static final String MENSAJE_ERROR_FECHACIERRE_REQUERIDA = "La fecha de cierre es requerida";
	public static final String MENSAJE_ERROR_FECHACIERRE_MINIMA = "La fecha de cierre debe ser mayor que la fecha actual";
	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_REQUERIDA = "El número de bolsas máximas es requerido";
	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_INCORRECTA = "El número de bolsas máximas debe ser un entero";
	public static final String MENSAJE_ERROR_NUMBOLSASMAXIMAS_MINIMO = "El número de bolsas máximas debe ser al menos una";
	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_REQUERIDA = "El número de méritos por bloque es requerido";
	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_INCORRECTA = "El número de méritos por bloque debe ser un entero";
	public static final String MENSAJE_ERROR_NUMMERITOSBLOQUE_MINIMO = "El número de méritos por bloque debe ser al menos uno";
	public static final String MENSAJE_ERROR_CONVOCATORIAS_ABIERTAS = "Ya existen convocatorias abiertas.";
	public static final String MENSAJE_ERROR_APARTADOS_PORCENTAGES = 
			"Los apartados de baremación no alcanzan el 100% de porcentaje, porfavor edite los apartados antes de abrir una convocatoria";
	public static final String MENSAJE_ERROR_SIN_BOLSAS_BAREMABLES = "Antes de abrir una convocatoria debe de tener areas baremables";
	public static final String MENSAJE_INFO_CONVOCATORIAS_INSERTADA_CORRECTAMENTE = "Convocatoria insertada correctamente.";
	public static final String MENSAJE_INFO_CONVOCATORIA_ACTUALIZADA_CORRECTAMENTE = "Convocatoria actualizada correctamente.";
	public static final String MENSAJE_ERROR_BOLSAS_BLOQUEADAS = "Debe desbloquear primero el total de las bolsas para abrir la convocatoria";

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CON = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/convocatorias/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/convocatorias";
	
	// variables
	public static boolean anonimo = true;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaConvocatorias bean = new VistaConvocatorias();		
		Usuario usuario = datos.getUsuario();
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
				
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));		
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_CONVOCATORIAS;
		}
					
		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_LISTAR_CONVOCATORIAS:
					index(bean, datos, request, response);
					break;				
				case ACCION_DATATABLE:
					listado(bean, datos, request, response);
					break;	
				case ACCION_FORMULARIO_CONVOCATORIA:
					formConvocatoria(bean, datos, request, response);
					break;
				case ACCION_AGREGAR_CONVOCATORIA:
					nuevaConvocatoria(bean, datos, request, response);
					break;
				case ACCION_MODIFICAR_CONVOCATORIA:
					editarConvocatoria(bean, datos, request, response);
					break;
				case ACCION_ABRIR_CONVOCATORIA:
					abrirConvocatoria(bean, datos, request, response);
					break;	
				case ACCION_CERRAR_CONVOCATORIA:
					cerrarConvocatoria(bean, datos, request, response);
					break;	
				case ACCION_BORRAR_CONVOCATORIA:
					borrarConvocatoria(bean, request, response);
					break;	
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
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");			
		}
	}	

	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void index(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) {
		bean.setVista(RUTA_BEP_CON + "indexConvocatorias.jsp");
	}
		
	private void listado(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia(); 
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Convocatoria> dataTable = modelo.listaConvocatoriasDatatable(request.getParameterMap());
				bean.setDatatableConvocatorias(dataTable);
				
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
	
	private void formConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) {
		bean.setVista(RUTA_BEP_CON + "formConvocatoria.jsp");
	}
	
	private void nuevaConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
		bean.setVista(RUTA_BEP_CON + "formConvocatoria.jsp");
		
		BolsaEmpleoValidator validator = this.getValidatorConvocatoria(request); 							
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		if (!validator.isValid()) {
			for (String param : validator.getErrors().keySet()) {
				for (String paramError : validator.getErrors().get(param)) {
					bean.getMensajesDeError().add(paramError);
				}
			}
		} else if (modelo.hayConvocatoriaAbierta()) {
			bean.getMensajesDeError().add(MENSAJE_ERROR_CONVOCATORIAS_ABIERTAS);
		} else {
			// creamos convocatoria, por defecto cerrada
			
			Convocatoria convocatoria = new Convocatoria();
			convocatoria.setDescripcion(validator.getValueString(PARAM_CONVOCATORIA_DESCRIPCION));
			convocatoria.setEstado(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA);
			convocatoria.setFechaCierre(validator.getValueDate(PARAM_CONVOCATORIA_FECHACIERRE));
			convocatoria.setNumBolsasMaximo(validator.getValueInteger(PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO));
			convocatoria.setNumMeritosPorBloque(validator.getValueInteger(PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE));		 
			modelo.nuevaConvocatoria(convocatoria);	
						
			bean.getMensajesDeExito().add(MENSAJE_INFO_CONVOCATORIAS_INSERTADA_CORRECTAMENTE);			
			
			this.index(bean, datos, request, response);
		}			
	}
		
	private void editarConvocatoria(VistaConvocatorias bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException { 
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		Convocatoria convocatoria = modelo.getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID)));
		Integer solicitudes = modelo.getNumSolicitudesByConvocatoriaId(convocatoria.getCodNum());
		if (solicitudes > 0) {
			bean.setVista(RUTA_BEP_CON + "indexConvocatorias.jsp");
			throw new UVException("No puede editar la convocatoria, ya existen solicitudes abiertas para ella");
		}
		
		bean.setVista(RUTA_BEP_CON + "formConvocatoria.jsp");
		
		BolsaEmpleoValidator validator = this.getValidatorConvocatoria(request); 							
	
		bean.setConvocatoria(convocatoria);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CONVOCATORIA_DESCRIPCION)) != null) {
			if (!validator.isValid()) {
				for (String param : validator.getErrors().keySet()) {
					for (String paramError : validator.getErrors().get(param)) {
						bean.getMensajesDeError().add(paramError);
					}
				}
			} else {
				
				Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID));
				String descripcion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_CONVOCATORIA_DESCRIPCION));
				Integer nbolsas = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO));
				Integer nmeritos = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE));
				Date fechacierre = Formateador.leeParametroFecha(request.getParameter(PARAM_CONVOCATORIA_FECHACIERRE), Formateador.FORMATO_FECHA_DDMMYYYY, "/");
				
				Convocatoria conFinal = new Convocatoria(codNum, descripcion, fechacierre, ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA, nbolsas, nmeritos); 
				modelo.actualizaConvocatoria(conFinal);	
				
				bean.getMensajesDeExito().add(MENSAJE_INFO_CONVOCATORIA_ACTUALIZADA_CORRECTAMENTE);
				
				this.index(bean, datos, request, response);
			}	
		}	
	}	
	
	
	private void abrirConvocatoria(VistaConvocatorias bean, UVDatos datos,
			HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
				
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		ModeloBolsa modeloBolsas = ModeloBolsa.obtenerInstancia();
		
		ModeloBaremacion modeloBaremacion = ModeloBaremacion.obtenerInstancia();
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		
		Convocatoria convocatoria = modelo.getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID)));
		bean.setConvocatoria(convocatoria);
		
		if (!modeloBolsas.hayBolsasBaremables()) {
			bean.getMensajesDeError().add(MENSAJE_ERROR_SIN_BOLSAS_BAREMABLES);
		} else if (modelo.checkBolsasDesbloqueadas()) {
			bean.getMensajesDeError().add(MENSAJE_ERROR_BOLSAS_BLOQUEADAS);
		} else if (modelo.hayConvocatoriaAbierta()) {
			bean.getMensajesDeError().add(MENSAJE_ERROR_CONVOCATORIAS_ABIERTAS);
		} else if (modeloBaremacion.checkSumaPorcentagesApartados(apartado, "Alcanzar")) {
			bean.getMensajesDeError().add(MENSAJE_ERROR_APARTADOS_PORCENTAGES);
		} else {
			Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID));
			Convocatoria conFinal = new Convocatoria(codNum, ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA); 
			modelo.cambiaEstadoConvocatoria(conFinal);	

			bean.getMensajesDeExito().add(MENSAJE_INFO_CONVOCATORIA_ACTUALIZADA_CORRECTAMENTE);
		}	
		
		this.index(bean, datos, request, response);
	}
	
	
	private void cerrarConvocatoria(VistaConvocatorias bean, UVDatos datos,
			HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException {
				
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		Convocatoria convocatoria = modelo.getConvocatoriaById(Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID)));
		bean.setConvocatoria(convocatoria);
		
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID));
		Convocatoria conFinal = new Convocatoria(codNum, ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA); 
		modelo.cambiaEstadoConvocatoria(conFinal);	

		bean.getMensajesDeExito().add(MENSAJE_INFO_CONVOCATORIA_ACTUALIZADA_CORRECTAMENTE);	
		
		this.index(bean, datos, request, response);
	}
	
	private BolsaEmpleoValidator getValidatorConvocatoria(HttpServletRequest request) throws UVException {
		BolsaEmpleoValidator validator = new BolsaEmpleoValidator(request);
		validator.addParamString(PARAM_CONVOCATORIA_DESCRIPCION);
		validator.addRule(PARAM_CONVOCATORIA_DESCRIPCION, "required", MENSAJE_ERROR_DESCRIPCION_VACIA);
		validator.addRule(PARAM_CONVOCATORIA_DESCRIPCION, "noBlank", MENSAJE_ERROR_DESCRIPCION_VACIA);
		validator.addRule(PARAM_CONVOCATORIA_DESCRIPCION, "max:" + ModeloConvocatoria.COLUMN_DESCRIPCION_MAXLENGTH, 
				String.format(MENSAJE_ERROR_DESCRIPCION_LARGA, ModeloConvocatoria.COLUMN_DESCRIPCION_MAXLENGTH));
		
		validator.addParamDate(PARAM_CONVOCATORIA_FECHACIERRE);
		validator.addRule(PARAM_CONVOCATORIA_FECHACIERRE, "required", MENSAJE_ERROR_FECHACIERRE_REQUERIDA);
		validator.addRule(PARAM_CONVOCATORIA_FECHACIERRE, "min:" + Formateador.formatoFecha(new Date(), Formateador.FORMATO_FECHA_DDMMYYYY), 
				MENSAJE_ERROR_FECHACIERRE_MINIMA);
		
		validator.addParamInteger(PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO);
		validator.addRule(PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO, "required", MENSAJE_ERROR_NUMBOLSASMAXIMAS_REQUERIDA);
		validator.addRule(PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO, "min:1", MENSAJE_ERROR_NUMBOLSASMAXIMAS_MINIMO);
		
		validator.addParamInteger(PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE);
		validator.addRule(PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE, "required", MENSAJE_ERROR_NUMMERITOSBLOQUE_REQUERIDA);
		validator.addRule(PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE, "min:1", MENSAJE_ERROR_NUMMERITOSBLOQUE_MINIMO);
		
		return validator;
	}
	
	/** eliminar una convocatoria.
	 * @param bean .
	 * @param request .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void borrarConvocatoria(VistaConvocatorias bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_CONVOCATORIA_ID));
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setCodNum(codNum);
		modelo.borraConvocatoria(convocatoria);
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ELIMINAR);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_ELIMINAR);
		response.sendRedirect(request.getServletPath());
	}
	
}
