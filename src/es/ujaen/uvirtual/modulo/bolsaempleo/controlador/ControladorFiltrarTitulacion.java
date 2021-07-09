package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoTitulacionTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFiltrar;
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
public class ControladorFiltrarTitulacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorFiltrarTitulacion.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_CANDIDATO_SELECCIONADO = "candidatoseleccionado";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_TITULACIONES_CANDIDATO = "datatabletitulacionescandidato";
	public static final String ACCION_DATATABLE_SIN_TITULACIONES = "datatablesintitulaciones";
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_TITULACION_DESELECCIONADA = "titulaciondeseleccionada";
	public static final String ACCION_TITULACION_SELECCIONADA = "titulacionseleccionada";
	public static final String ACCION_CAMBIAR_TITULACION = "cambiarTitulacion";
	public static final String ACCION_CANDIDATOS_SIN_TITULACION = "sinTitulacion";
	public static final String ACCION_EXPORTAR_SIN_TITULACION = "exportarSinTitulacion";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_TITULACION = "titulacion";
	public static final String PARAM_TITULACION_USUARIO = "titulacionUsuario";
	
	// ruta vistas
	public static final String RUTA_BEP_FILTRAR = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/filtrartitulacion/";
	public static final String JSP_INDEX = RUTA_BEP_FILTRAR + "index.jsp";
	public static final String JSP_SIN_TITULACION = RUTA_BEP_FILTRAR + "sinTitulacion.jsp";
	
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/filtrar";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// csv
	public static final String RESPONSE_CSV_CONTENTTYPE = "text/csv";
	public static final String RESPONSE_CSV_ENCODING = "UTF-8";
	

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
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					index(bean);
					break;
				case ACCION_CANDIDATO_SELECCIONADO:
					seleccionarCandidato(bean, request);
					break;
				case ACCION_DATATABLE_CANDIDATOS:
					listadoCandidatos(bean, datos, request, response);
					break;
				case ACCION_DATATABLE_TITULACIONES_CANDIDATO:
					listadoTitulacionesCandidato(bean, datos, request, response);
					break;
				case ACCION_TITULACION_DESELECCIONADA:
					seleccionarTitulacion(bean, datos, request, response, false);
					break;
				case ACCION_TITULACION_SELECCIONADA:
					seleccionarTitulacion(bean, datos, request, response, true);
					break;
				case ACCION_CAMBIAR_TITULACION:
					cambiarTitulacion(bean, datos, request, response);
					break;
				case ACCION_CANDIDATOS_SIN_TITULACION:
					irAListadoSinTitulacion(bean);
					break;
				case ACCION_DATATABLE_SIN_TITULACIONES:
					listadoCandidatosSinTitulacion(bean, datos, request, response);
					break;
				case ACCION_EXPORTAR_SIN_TITULACION:
					exportarSinTitulacion(datos, response);
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
			datos.getFicherosJS().add(ModeloParametrosConfiguracion.JS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
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
	
	private void init(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
			
			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException("No tienes permiso de personal");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void index(VistaFiltrar bean) {
		bean.setVista(JSP_INDEX);
	}
	
	private void errorFatal(VistaFiltrar bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	private void seleccionarCandidato(VistaFiltrar bean, HttpServletRequest request) throws UVException, SQLException {
		bean.setVista(JSP_INDEX);
		
		ModeloMisTitulaciones modeloTitulacion = ModeloMisTitulaciones.obtenerInstancia();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		UsuarioBolsaEmpleo candidato = modeloUsuario.getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
		bean.setCandidato(candidato);
		bean.setTitulaciones(ModeloTitulacion.obtenerInstancia().listaTitulacionesActivas());
		
		List<TitulacionUsuario> listaValidadas = modeloTitulacion.listaTitulacionesValidadasCandidato(candidato.getCodNum());
		bean.setValidadas(listaValidadas);
	}
	
	private void seleccionarTitulacion(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, boolean seleccionado)
			throws IOException, UVException, SQLException {
		ModeloMisTitulaciones modeloTitulacion = ModeloMisTitulaciones.obtenerInstancia();
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				TitulacionUsuario titulacion = modeloTitulacion.getTitulacionUsuarioById(
						Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION_USUARIO)));

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
					modeloTitulacion.validaTitulacion(titulacion, candidato, BolsaEmpleoUtils.getCurrentDateTime(), bean.getUsuarioLogeado());
				} else {
					modeloTitulacion.desvalidaTitulacion(titulacion, candidato, bean.getUsuarioLogeado());
				}
				
				CodigoDescripcion mensaje = new CodigoDescripcion("ok", seleccionado ? "titulación validada" : "titulación no validada");
				writer.write(new Gson().toJson(mensaje));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void listadoCandidatos(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloCandidato modeloCandidato = ModeloCandidato.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoTitulacionTable> dataTable = modeloCandidato.listaCandidatosDatatable(request.getParameterMap());
				bean.setDatatableCandidatos(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e.getClass().equals(SQLException.class)) {
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
	
	private void listadoTitulacionesCandidato(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer candidato = Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO));
				BolsaEmpleoDataTable<TitulacionUsuario> dataTable = modelo.listaTitulacionesUsuarioDatatable(request.getParameterMap(), candidato);
				bean.setDatatableTitulaciones(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e.getClass().equals(SQLException.class)) {
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
	
	private void cambiarTitulacion(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				UsuarioBolsaEmpleo candidato = modeloUsuario.getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
				if (candidato == null) {
					throw new UVException("Candidato requerido");
				}
				
				TitulacionUsuario titulacionUsuario = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(
						Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION_USUARIO)));
				if (titulacionUsuario == null) {
					throw new UVException("Titulación usuario requerida");
				}
				
				Titulacion titulacion = ModeloTitulacion.obtenerInstancia().getTitulacionById(
						Formateador.leeParametroInteger(request.getParameter(PARAM_TITULACION)));
				if (titulacion == null) {
					throw new UVException("Titulación requerida");
				}
				
				ModeloMisTitulaciones.obtenerInstancia().cambiarTitulacion(titulacionUsuario, candidato, titulacion, bean.getUsuarioLogeado());
				CodigoDescripcion mensaje = new CodigoDescripcion("ok", "Titulación actualizada");
				writer.write(new Gson().toJson(mensaje));				
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

	private void irAListadoSinTitulacion(VistaFiltrar bean) {
		bean.setVista(JSP_SIN_TITULACION);		
	}
	
	private void listadoCandidatosSinTitulacion(VistaFiltrar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloCandidato modeloCandidato = ModeloCandidato.obtenerInstancia();
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoTitulacionTable> dataTable = modeloCandidato.listaCandidatosSinTitulacionDatatable(request.getParameterMap());
				bean.setDatatableCandidatos(dataTable);
				writer.write(dataTable.toJson());
			} catch (UVException | SQLException e) {
				if (e.getClass().equals(SQLException.class)) {
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
	
	private void exportarSinTitulacion(UVDatos datos, HttpServletResponse response) throws IOException, SQLException, UVException {
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_CSV_CONTENTTYPE);		
		response.setContentType(RESPONSE_CSV_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_CSV_ENCODING);
		response.setHeader("Content-Disposition", "attachment; filename=\"candidatos-sin-titulacion.csv\"");
		
		List<String[]> rows = ModeloCandidato.obtenerInstancia().listaCandidatosSinTitulacionCsv();
		
		try (ServletOutputStream stream = response.getOutputStream()) {
			try (PrintWriter printer = new PrintWriter(stream)) {
				for (String[] row : rows) {
					printer.println(String.join(";", row));
				}
			}
			stream.flush();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(ex));
			LOGGER.log(Level.SEVERE, ex.toString());
			throw new UVException(ex.getMessage());
        }
	}
}
