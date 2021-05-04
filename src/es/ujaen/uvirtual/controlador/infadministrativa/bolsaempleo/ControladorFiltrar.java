package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Candidato;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFiltrar;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloCandidato;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloTitulacion;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Listado de bolsas y su estado.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.filtrar", 
		description = "Filtrar candidatos de las bolsas", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/filtrar", 
				"/srv/en/informacionadministrativa/bolsaempleo/filtrar",
				"/srv/es/ajax/informacionadministrativa/bolsaempleo/filtrar", 
				"/srv/en/ajax/informacionadministrativa/bolsaempleo/filtrar"
		})
public class ControladorFiltrar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorFiltrar.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_CANDIDATO_SELECCIONADO = "candidatoseleccionado";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_TITULACIONES_CANDIDATO = "datatabletitulacionescandidato";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_TITULACION_DESELECCIONADA = "titulaciondeseleccionada";
	public static final String ACCION_TITULACION_SELECCIONADA = "titulacionseleccionada";
	
	// mensajes
	public static final String MENSAJE_ERROR_FOO = "Mensaje de error";
	public static final String MENSAJE_EXITO_BAR = "Mensaje de exito";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_FICHERO = "fichero";
	public static final String PARAM_TITULACION = "titulacion";
	
	// ruta vistas
	public static final String RUTA_BEP_FILTRAR = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/filtrar/";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/filtrar";
	public static final String URL_PATTERN_FILES_PRIVADA = "/srv/es/informacionadministrativa/bolsaempleo/filtrar";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaFiltrar bean = new VistaFiltrar();		
		Usuario usuario = datos.getUsuario();
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR;
		}
		
		try {
			modelo.checkUser(datos);
			switch (nombreAccion) {
				case ACCION_CANDIDATO_SELECCIONADO:
					seleccionarCandidato(bean, request);
					break;
				case ACCION_DATATABLE_CANDIDATOS:
					listadoCandidatos(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_TITULACIONES_CANDIDATO:
					listadoTitulacionesCandidato(bean, datos, request, response);
					break;
				case ACCION_DESCARGAR_FICHERO:
					descargarFichero(bean, datos, request, response);
					break;
				case ACCION_LISTAR:
					bean.setVista(RUTA_BEP_FILTRAR + "index.jsp");
					break;
				case ACCION_TITULACION_DESELECCIONADA:
					seleccionarTitulacion(bean, datos, request, response, false);
					break;
				case ACCION_TITULACION_SELECCIONADA:
					seleccionarTitulacion(bean, datos, request, response, true);
					break;
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
			datos.getFicherosJS().add("/js/jquery.ui.datepicker-es.js");
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/datatable.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
	
	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Selecciona un candidato para mostrar sus titulaciones .
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private void seleccionarCandidato(VistaFiltrar bean, HttpServletRequest request) throws UVException, SQLException {
		bean.setVista(RUTA_BEP_FILTRAR + "index.jsp");
		
		ModeloTitulacion modeloTitulacion = ModeloTitulacion.obtenerInstancia();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		UsuarioBolsaEmpleo candidato = modeloUsuario.getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
		bean.setCandidato(candidato);
		
		List<Titulacion> listaValidadas = modeloTitulacion.listaTitulacionesValidadasCandidato(candidato.getCodNum());
		bean.setValidadas(listaValidadas);
	}
	
	/** 
	 * Selecciona una titulación para validarla o no .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param seleccionado .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void seleccionarTitulacion(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, Boolean seleccionado)
			throws IOException, UVException, SQLException {
		ModeloTitulacion modeloTitulacion = ModeloTitulacion.obtenerInstancia();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Titulacion titulacion = modeloTitulacion.getTitulacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION)));
				bean.setTitulacion(titulacion);
				UsuarioBolsaEmpleo candidato = modeloUsuario.getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
				bean.setCandidato(candidato);
				
				if (titulacion == null) {
					throw new UVException("titulacion no puede ser nula");
				}
				
				if (candidato == null) {
					throw new UVException("candidato no puede ser nulo");
				}
				
				if (seleccionado) {	
					modeloTitulacion.validaTitulacion(titulacion, candidato, new Date());
				} else {
					modeloTitulacion.desvalidaTitulacion(titulacion, candidato);
				}
				
				CodigoDescripcion mensaje = new CodigoDescripcion("ok", seleccionado ? "titulación validada" : "titulación no validada");
				writer.write(new Gson().toJson(mensaje));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
			}
		}
	}
	
	/**
	 * Lista de candidatos datatable .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoCandidatos(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloCandidato modeloCandidato = ModeloCandidato.obtenerInstancia();
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Candidato> dataTable = modeloCandidato.listaCandidatosDatatable(request.getParameterMap());
				bean.setDatatableCandidatos(dataTable);
				
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
	
	/** carga las titulaciones de un usuario en una tabla .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException en caso de error de input u output .
	 * @throws SQLException excepcion de bbdd.
	 */
	private void listadoTitulacionesCandidato(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		datos.setContentType("application/json");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer candidato = Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO));
				BolsaEmpleoDataTable<Titulacion> dataTable = modelo.listaTitulacionesUsuarioDatatable(request.getParameterMap(), candidato);
				bean.setDatatableTitulaciones(dataTable);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
				writer.write(gson.toJson(dataTable));
			} catch (UVException ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion("error", ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_HTTP_CODE_ERROR);
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
	private void descargarFichero(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_FICHERO)) != null) {
			Titulacion titulacion = modelo.listaTitulacionUsuario(Formateador.leeParametroInteger(request.getParameter(PARAM_FICHERO)));
			bean.setTitulacion(titulacion);
			
	    	response.setContentType("application/pdf");
	        datos.setRespuestaEnviada(true);
	        
	        try (ServletOutputStream stream = response.getOutputStream();
	             BufferedInputStream buf = new BufferedInputStream(titulacion.getArchivo());) {
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
}
