package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.google.gson.Gson;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoEstado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Contratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloContratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEstadoCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloOfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Servlet implementation class ControladorContratacion.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.contratacion",
	description = "Contratación bolsa empleo",
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/contratacion",
			"/srv/en/informacionadministrativa/bolsaempleo/contratacion",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/contratacion",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/contratacion"
	})
public class ControladorContratacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorContratacion.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_COMPROBAR_CONTRATACION_PLAZA = "comprobarcontratacionplaza";
	public static final String ACCION_CONTRATAR_CANDIDATO = "contratarcandidato";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_CANDIDATOS_APERTURA = "datatablecandidatosapertura";
	public static final String ACCION_DATATABLE_CANDIDATOS_CONTRATO = "datatablecandidatoscontrato";
	public static final String ACCION_DATATABLE_PLAZAS_OFERTADAS = "datatableplazasofertadas";
	public static final String ACCION_PLAZA_ABIERTA = "plazaabierta";
	public static final String ACCION_PLAZA_CERRADA = "plazacerrada";
	public static final String ACCION_PLAZA_TRAMITACION = "plazatramitacion";
	public static final String ACCION_EDITAR_PLAZA_OFERTADA = "editarplazaofertada";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_PREFERENCIAS_CANDIDATO = "preferenciascandidato";
	public static final String ACCION_NUEVA_PLAZA_OFERTADA = "nuevaplazaofertada";
	public static final String ACCION_RECHAZAR_CONTRATACION_CANDIDATO = "rechazarcontratacioncandidato";
	public static final String ACCION_REENVIAR_CITA_CONTRATACION = "reenviarcitacontratacion";
	public static final String ACCION_RESTAURAR_PLAZA_OFERTADA = "restaurarplazaofertada";
	public static final String ACCION_SELECCIONAR_PLAZA_OFERTADA = "seleccionarplazaofertada";
	
	// Parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_CENTRO_DESTINO = "centrodestino";
	public static final String PARAM_CUATRIMESTRE = "cuatrimestre";
	public static final String PARAM_DEDICACION = "dedicacion";
	public static final String PARAM_DURACION_PREVISTA = "duracionprevista";
	public static final String PARAM_EMAILS_CIERRE = "emailcierre";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ESTADO = "estado";
	public static final String PARAM_FECHA_CITA = "fechacita";
	public static final String PARAM_FECHA_FIN_OFERTA = "fechafinoferta";
	public static final String PARAM_HORARIO = "horario";
	public static final String PARAM_HORA_CITA = "horacita";
	public static final String PARAM_HORA_FIN_OFERTA = "horafinoferta";
	public static final String PARAM_JUSTIFICACION = "justificacion";
	public static final String PARAM_NRI_FECHA = "nrifecha";
	public static final String PARAM_NRI = "nri";
	public static final String PARAM_PLAZA_OFERTADA = "plazaofertada";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_CENTRO_DESTINO_VACIO = "El centro de destino no puede estar vacío";
	public static final String MENSAJE_ERROR_CENTRO_DESTINO_NO_VALIDO = "El centro de destino seleccionado no es válido";
	public static final String MENSAJE_ERROR_CANDIDATO_CONTRATACION_ACTIVA = "Ya hay una contratación activa para el candidato %s";
	public static final String MENSAJE_ERROR_CANDIDATO_CONTRATACION_NO_ACTIVA = "No hay una contratación activa para el candidato %s";
	public static final String MENSAJE_ERROR_CUATRIMESTRE_NO_VALIDO = "El cuatrimestre seleccionado no es válido";
	public static final String MENSAJE_ERROR_DURACION_PREVISTA_LARGA = "La duración prevista no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_ESTADO_CONTRATACION_REQUERIDO = "Es requerido estado de contratación para la plaza";
	public static final String MENSAJE_ERROR_ESTADO_CREACION_REQUERIDO = "Es requerido estado de creación para la plaza";
	public static final String MENSAJE_ERROR_ESTADO_TRAMITACION_REQUERIDO = "Es requerido estado de tramitación para la plaza";
	public static final String MENSAJE_ERROR_FECHA_FIN_OFERTA_VACIA = "La fecha fin de la oferta no puede estar vacía";
	public static final String MENSAJE_ERROR_FORMATO_FECHA = "Error al formatear fecha. Formato: DD/MM/YYYY HH:MM:SS";
	public static final String MENSAJE_ERROR_JUSTIFICACION_LARGA = "La justificación no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_JUSTIFICACION_VACIA = "La justificación no puede estar vacía";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso";
	
	public static final String MENSAJE_EXITO_ABRIR_PLAZA = "Plaza abierta correctamente";
	public static final String MENSAJE_EXITO_AGREGAR = "Plaza creada correctamente";
	public static final String MENSAJE_EXITO_CERRAR_PLAZA = "Plaza cerrada correctamente";
	public static final String MENSAJE_EXITO_CONTRATACION = "Contratación creada correctamente para el usuario: %s";
	public static final String MENSAJE_EXITO_EDITAR = "Plaza editada correctamente";
	public static final String MENSAJE_EXITO_TRAMITACION_PLAZA = "Cambiado el estado de la plaza a tramitación correctamente";
	public static final String MENSAJE_EXITO_REENVIO_CONTRATACION = "Se ha reenviado la contratación correctamente para el usuario: %s";
	public static final String MENSAJE_EXITO_SUSPENSION_PLAZA = "Suspendido el contrato del candidato correctamente";
	
	// ruta vistas
	public static final String RUTA_BEP_CONTRATACION = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/contratacion/";
	public static final String JSP_INDEX = RUTA_BEP_CONTRATACION + "index.jsp";
	public static final String JSP_CREATE = RUTA_BEP_CONTRATACION + "createPlazaOfertada.jsp";
	public static final String JSP_EDIT = RUTA_BEP_CONTRATACION + "editPlazaOfertada.jsp";
	
	// errors
	public static final Integer RESPONSE_HTTP_CODE_ERROR_400 = 400;
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/contratacion";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:executablestatementcount", "checkstyle:javancss"})
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaContratacion bean = new VistaContratacion();
		
		HashMap<String, Object> parametros = new HashMap<>();
		
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) {
						parametros.put(item.getFieldName(), item.getString());
					} else {
						if (item.getSize() > 0 && item.getName().toLowerCase().endsWith(".pdf")) {
							try (InputStream contenidoDelFichero = item.getInputStream(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
								parametros.put(item.getFieldName(), BolsaEmpleoUtils.checkFileSize(contenidoDelFichero));
							}
						}
					}
				}
			} catch (FileUploadException | UVException | SQLException e) {
				e.printStackTrace();
			}
		}
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
				request, parametros, PARAM_ACCION));
		if (nombreAccion == null || nombreAccion.isEmpty()) {
			nombreAccion = ACCION_INDEX;
		}
		try {
			init(bean, datos, request, response);
			
			switch (nombreAccion) {
				case ACCION_COMPROBAR_CONTRATACION_PLAZA:
				case ACCION_CONTRATAR_CANDIDATO:
				case ACCION_DATATABLE_CANDIDATOS:
				case ACCION_DATATABLE_CANDIDATOS_APERTURA:
				case ACCION_DATATABLE_CANDIDATOS_CONTRATO:
				case ACCION_EDITAR_PLAZA_OFERTADA:
				case ACCION_PLAZA_ABIERTA:
				case ACCION_PLAZA_CERRADA:
				case ACCION_PLAZA_TRAMITACION:
				case ACCION_RECHAZAR_CONTRATACION_CANDIDATO:
				case ACCION_REENVIAR_CITA_CONTRATACION:
				case ACCION_SELECCIONAR_PLAZA_OFERTADA:
					seleccionarPlazaOfertada(bean, datos, request, response, nombreAccion, parametros);
					break;
				case ACCION_DATATABLE_PLAZAS_OFERTADAS:
					listaPlazasOfertadas(bean, datos, request, response);
					break;
				case ACCION_INDEX:
					break;
				case ACCION_NUEVA_PLAZA_OFERTADA:
					nuevaPlazaOfertada(bean, datos, request, response, parametros);
					break;
				case ACCION_PREFERENCIAS_CANDIDATO:
					obtenerPreferenciasCandidato(bean, datos, request, response);
					break;
				default:
					errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
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
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
		}
	}
	
	private void init(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
			
			// personal, direccion
			int[] rolesValidos = {ModeloRol.ID_ROL_SERVICIO_PERSONAL, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO};
			boolean contains = IntStream.of(rolesValidos).
					anyMatch(x -> x == bean.getUsuarioLogeado().getRol().getCodNum());
			
			if (!contains) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void errorFatal(VistaContratacion bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	/** redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity"})
	private void seleccionarPlazaOfertada(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion, 
			HashMap<String, Object> parametros) throws SQLException, UVException, IOException {
		bean.setVista(JSP_EDIT);
		cargarListasFormulario(bean);
		
		ModeloPlazaOfertada modelo = ModeloPlazaOfertada.obtenerInstancia();
		PlazaOfertada plaza = modelo.getPlazaOfertadaById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
				request, parametros, PARAM_PLAZA_OFERTADA)));
		
		if ((!nombreAccion.equals(ACCION_SELECCIONAR_PLAZA_OFERTADA) && !bean.getUsuarioLogeado().isServicioPersonal() 
				&& !plaza.getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_CREACION)) || !bean.getUsuarioLogeado().isServicioPersonal()
				&& !ModeloEvaluador.obtenerInstancia().checkEvaluadorArea(plaza.getArea(), bean.getUsuarioLogeado())) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		}
		
		bean.setPlazaOfertada(plaza);
		
		switch (nombreAccion) {
			case ACCION_COMPROBAR_CONTRATACION_PLAZA:
				comprobarContratacionPlaza(bean, datos, request, response);
				break;
			case ACCION_CONTRATAR_CANDIDATO:
				contratarCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_CANDIDATOS:
				listaCandidatos(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_CANDIDATOS_APERTURA:
				listaCandidatosApertura(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_CANDIDATOS_CONTRATO:
				listaCandidatosContrato(bean, datos, request, response);
				break;
			case ACCION_EDITAR_PLAZA_OFERTADA:
				editarPlazaOfertada(bean, datos, request, response, parametros);
				break;
			case ACCION_PLAZA_ABIERTA:
				abrirPlaza(bean, datos, request, response);
				break;
			case ACCION_PLAZA_CERRADA:
				cerrarPlaza(bean, datos, request, response);
				break;
			case ACCION_PLAZA_TRAMITACION:
				tramitacionPlaza(bean, datos, request, response);
				break;
			case ACCION_SELECCIONAR_PLAZA_OFERTADA:
				break;
			case ACCION_RECHAZAR_CONTRATACION_CANDIDATO:
				rechazarContratacionCandidato(bean, datos, request, response);
				break;
			case ACCION_REENVIAR_CITA_CONTRATACION:
				reenviarContratacionCandidato(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void abrirPlaza(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloPlazaOfertada modeloPlaza = ModeloPlazaOfertada.obtenerInstancia();
		
		PlazaOfertada plaza = bean.getPlazaOfertada();
		if (!plaza.getEstado().contains(ModeloPlazaOfertada.PLAZA_ESTADO_TRAMITACION)) {
			throw new UVException(MENSAJE_ERROR_ESTADO_TRAMITACION_REQUERIDO);
		}
		
		modeloPlaza.crearMensajeAperturaPlaza(plaza, bean.getUsuarioLogeado());
		modeloPlaza.abrirPlazaOfertada(plaza, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ABRIR_PLAZA, bean, request);
		redireccionConPlazaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_PLAZA_OFERTADA);
	}
	
	private void cerrarPlaza(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		if (!bean.getUsuarioLogeado().isServicioPersonal()) {
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, MENSAJE_ERROR_SIN_PERMISO);
		}
		
		ModeloPlazaOfertada modeloPlaza = ModeloPlazaOfertada.obtenerInstancia();
		
		PlazaOfertada plaza = bean.getPlazaOfertada();
		
		if (!plaza.getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION)) {
			throw new UVException(MENSAJE_ERROR_ESTADO_CONTRATACION_REQUERIDO);
		}
		
		String cadenaEmails = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EMAILS_CIERRE));
		
		// para el caso de la plaza que no es desierta se requiere los destinatarios para comunicar el cierre de la misma
		if (cadenaEmails != null) {
			cadenaEmails = cadenaEmails.replaceAll(" ", "");
			String[] emails = cadenaEmails.split(",");
			
			Contratacion contratacion = ModeloContratacion.obtenerInstancia().getContratacionByPlazaContratada(plaza);
			if (contratacion == null) {
				throw new UVException("No hay ningún contrato para esta plaza");
			}
			
			modeloPlaza.crearMensajeCierrePlaza(plaza, contratacion, emails, bean.getUsuarioLogeado());
			
			String estadoCandidato = ModeloEstadoCandidato.ESTADO_CONTRATADO;
			
			if (plaza.getCuatrimestre().equals(ModeloPlazaOfertada.CUATRIMESTRE_PRIMERO)) {
				estadoCandidato = ModeloEstadoCandidato.ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE;
			}
			
			ModeloEstadoCandidato.obtenerInstancia().cambiarEstadoCandidato(contratacion.getCandidato(), estadoCandidato, 
					ModeloBolsa.obtenerInstancia().getBolsaById(bean.getPlazaOfertada().getArea().getCodNum()), bean.getUsuarioLogeado());
		}
		
		modeloPlaza.cerrarPlazaOfertada(plaza, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_CERRAR_PLAZA, bean, request);
		redireccionConPlazaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_PLAZA_OFERTADA);
	}
	
	private void comprobarContratacionPlaza(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Contratacion contratacion = ModeloContratacion.obtenerInstancia().getContratacionByPlazaContratada(bean.getPlazaOfertada());
				ParametrosConfiguracion paramEmails = ModeloParametrosConfiguracion.obtenerInstancia().getParametroByNombre(
						ModeloParametrosConfiguracion.PARAMETRO_EMAILS_CIERRE_PLAZA);
				writer.write(new Gson().toJson(contratacion == null 
						? "{\"result\": false}" : "{\"result\": true, \"emails\": \"" + paramEmails.getValor() + "\"}"));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void contratarCandidato(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloContratacion modeloContratacion = ModeloContratacion.obtenerInstancia();
		
		String fechaCita = request.getParameter(PARAM_FECHA_CITA);
		String horaCita = request.getParameter(PARAM_HORA_CITA);
		Date fecha = BolsaEmpleoUtils.leeParametroFechaHora(fechaCita + " " + horaCita, "/", ":");
		
		if (fecha == null) {
			throw new UVException(MENSAJE_ERROR_FORMATO_FECHA);
		}
		
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
		
		if (modeloContratacion.comprobarContratoCandidato(bean.getPlazaOfertada(), candidato)) {
			throw new UVException(String.format(MENSAJE_ERROR_CANDIDATO_CONTRATACION_ACTIVA, candidato.getPrsNif()));
		}
		
		Contratacion contratacion = new Contratacion(candidato, fecha);
		modeloContratacion.crearMensajeCitaContratacion(bean.getPlazaOfertada(), contratacion, bean.getUsuarioLogeado());
		modeloContratacion.insertarContratacion(bean.getPlazaOfertada(), fecha, candidato, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(String.format(MENSAJE_EXITO_CONTRATACION, candidato.getPrsNif()), bean, request);
		redireccionConPlazaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_PLAZA_OFERTADA);
	}
	
	private void editarPlazaOfertada(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> parametros) 
			throws SQLException, UVException, IOException {
		PlazaOfertada plaza = bean.getPlazaOfertada();
		plaza = validarPlazaOfertada(bean, request, plaza, parametros);
		
		ModeloPlazaOfertada.obtenerInstancia().actualizaPlazaOfertada(plaza, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
		redireccionConPlazaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_PLAZA_OFERTADA);
	}
	
	private void nuevaPlazaOfertada(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> parametros) 
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_CREATE);
		cargarListasFormulario(bean);
		
		if (BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(request, parametros, PARAM_AREA) != null) {
			PlazaOfertada plaza = validarPlazaOfertada(bean, request, new PlazaOfertada(), parametros);
			
			ModeloPlazaOfertada.obtenerInstancia().insertaPlazaOfertada(plaza, bean.getUsuarioLogeado());
			
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_AGREGAR, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void cargarListasFormulario(VistaContratacion bean) throws SQLException {
		ModeloArea modelo = ModeloArea.obtenerInstancia();
		bean.setListaAreas(bean.getUsuarioLogeado().isServicioPersonal() ? modelo.listaAreas() : modelo.getAreasByEvaluador(bean.getUsuarioLogeado()));
		bean.setListaDedicaciones(ModeloDedicacion.obtenerInstancia().listaDedicacionesActivas());
	}
	
	private void redireccionConPlazaSeleccionada(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_PLAZA_OFERTADA, bean.getPlazaOfertada().getCodNum().toString());
		params.put(PARAM_ACCION, accion);
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	private void tramitacionPlaza(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloPlazaOfertada modeloPlaza = ModeloPlazaOfertada.obtenerInstancia();
		
		if (!bean.getPlazaOfertada().getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_CREACION)) {
			throw new UVException(MENSAJE_ERROR_ESTADO_CREACION_REQUERIDO);
		}
		
		modeloPlaza.cambiarEstadoPlazaATramitacion(bean.getPlazaOfertada(), bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_TRAMITACION_PLAZA, bean, request);
		redireccionConPlazaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_PLAZA_OFERTADA);
	}
	
	private void rechazarContratacionCandidato(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloContratacion modeloContratacion = ModeloContratacion.obtenerInstancia();
		
		if (!bean.getPlazaOfertada().getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION)) {
			throw new UVException(MENSAJE_ERROR_ESTADO_CONTRATACION_REQUERIDO);
		}
		
		Integer idCandidato = Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO));
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(idCandidato);
		
		modeloContratacion.rechazarContrato(bean.getPlazaOfertada(), candidato, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_SUSPENSION_PLAZA, bean, request);
		redireccionConPlazaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_PLAZA_OFERTADA);
	}
	
	private void reenviarContratacionCandidato(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		ModeloContratacion modeloContratacion = ModeloContratacion.obtenerInstancia();
		
		String fechaCita = request.getParameter(PARAM_FECHA_CITA);
		String horaCita = request.getParameter(PARAM_HORA_CITA);
		Date fecha = BolsaEmpleoUtils.leeParametroFechaHora(fechaCita + " " + horaCita, "/", ":");
		
		if (fecha == null) {
			throw new UVException(MENSAJE_ERROR_FORMATO_FECHA);
		}
		
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
		
		if (!modeloContratacion.comprobarContratoCandidato(bean.getPlazaOfertada(), candidato)) {
			throw new UVException(String.format(MENSAJE_ERROR_CANDIDATO_CONTRATACION_NO_ACTIVA, candidato.getPrsNif()));
		}
		
		modeloContratacion.reestrablecerContratacion(bean.getPlazaOfertada(), fecha, candidato, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(String.format(MENSAJE_EXITO_REENVIO_CONTRATACION, candidato.getPrsNif()), bean, request);
		redireccionConPlazaSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_PLAZA_OFERTADA);
	}
	
	private void obtenerPreferenciasCandidato(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer idCandidato = Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO));
				UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(idCandidato);
				
				List<OfertaCandidato> listaOfertas = ModeloOfertaCandidato.obtenerInstancia().listaOfertasCandidatoPreferentes(candidato);
				bean.setListaOfertas(listaOfertas);
				
				writer.write(new Gson().toJson(listaOfertas));
			} catch (Exception ex) {
				bean.getMensajesDeError().add(ex.getMessage());
				CodigoDescripcion mensaje = new CodigoDescripcion(RESPONSE_AJAX_ERROR, ex.getMessage());
				writer.write(new Gson().toJson(mensaje));
				response.setStatus(RESPONSE_AJAX_HTTP_CODE_ERROR);
			}
		}
	}
	
	private void listaCandidatos(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<OfertaCandidato> dataTable = ModeloPlazaOfertada.obtenerInstancia().listadoCandidatosDisponibles(
						request.getParameterMap(), bean.getPlazaOfertada());
				bean.setDatatableCandidatos(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
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
	
	private void listaCandidatosApertura(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoEstado> dataTable = ModeloEstadoCandidato.obtenerInstancia().listaEstadosCandidatoDisponiblesPlaza(
						request.getParameterMap(), bean.getPlazaOfertada());
				bean.setDatatableCandidatosEstado(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
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
	
	private void listaCandidatosContrato(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<OfertaCandidato> dataTable = ModeloContratacion.obtenerInstancia().listadoCandidatosContrato(
						request.getParameterMap(), bean.getPlazaOfertada());
				bean.setDatatableCandidatos(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
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
	
	private void listaPlazasOfertadas(VistaContratacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<PlazaOfertada> dataTable = ModeloPlazaOfertada.obtenerInstancia().listadoPlazasOfertadas(
						request.getParameterMap(), bean.getUsuarioLogeado());
				bean.setDatatablePlazasOfertadas(dataTable);
				writer.write(dataTable.toJson("dd/M/yyyy HH:mm:ss"));
			} catch (UVException | SQLException e) {
				if (e instanceof SQLException) {
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
	
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private PlazaOfertada validarPlazaOfertada(VistaContratacion bean, HttpServletRequest request, PlazaOfertada plaza, HashMap<String, Object> parametros) 
			throws UVException, SQLException {
		plaza.setArea(ModeloArea.obtenerInstancia().getAreaById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
				request, parametros, PARAM_AREA))));
		
		plaza.setJustificacion(Formateador.leeParametroString(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
				request, parametros, PARAM_JUSTIFICACION)));
		if (plaza.getJustificacion() == null || plaza.getJustificacion().isBlank()) {
			throw new UVException(MENSAJE_ERROR_JUSTIFICACION_VACIA);
		}
		if (plaza.getJustificacion().length() > ModeloPlazaOfertada.COLUMN_JUSTIFICACION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_JUSTIFICACION_LARGA, ModeloPlazaOfertada.COLUMN_JUSTIFICACION_MAXLENGTH));
		}
		
		plaza.setCentroDestino(Formateador.leeParametroString(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
				request, parametros, PARAM_CENTRO_DESTINO)));
		if (plaza.getCentroDestino() == null || plaza.getCentroDestino().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CENTRO_DESTINO_VACIO);
		}
		if (!ModeloPlazaOfertada.CENTROS_DESTINO.containsKey(plaza.getCentroDestino())) {
			throw new UVException(MENSAJE_ERROR_CENTRO_DESTINO_NO_VALIDO);
		}
		
		if (bean.getUsuarioLogeado().isServicioPersonal()) {
			if (Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
					request, parametros, PARAM_DEDICACION)) != null) {
				plaza.setDedicacion(ModeloDedicacion.obtenerInstancia().getDedicacionById(Formateador.leeParametroInteger(
						BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(request, parametros, PARAM_DEDICACION))));
			}
			
			plaza.setCuatrimestre(Formateador.leeParametroString(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
					request, parametros, PARAM_CUATRIMESTRE)));
			if (!plaza.getCuatrimestre().isEmpty() && !ModeloPlazaOfertada.CUATRIMESTRES.containsKey(plaza.getCuatrimestre())) {
				throw new UVException(MENSAJE_ERROR_CUATRIMESTRE_NO_VALIDO);
			}
			
			plaza.setDuracionPrevista(Formateador.leeParametroString(BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(
					request, parametros, PARAM_DURACION_PREVISTA)));
			if (plaza.getDuracionPrevista().length() > ModeloPlazaOfertada.COLUMN_DURACION_PREVISTA_MAXLENGTH) {
				throw new UVException(MENSAJE_ERROR_DURACION_PREVISTA_LARGA);
			}
			
			String fechaFinOferta = BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(request, parametros, PARAM_FECHA_FIN_OFERTA);
			String horaFinOferta = BolsaEmpleoUtils.getParamRequestOrMultipartOrSession(request, parametros, PARAM_HORA_FIN_OFERTA);
			Date fecha = BolsaEmpleoUtils.leeParametroFechaHora(fechaFinOferta + " " + horaFinOferta, "/", ":");
			plaza.setFechaFinOferta(fecha);
			
			plaza.setNri((InputStream) parametros.get(PARAM_NRI));
		}
		
		plaza.setHorario((InputStream) parametros.get(PARAM_HORARIO));
		
		return plaza;
	}
	
}
