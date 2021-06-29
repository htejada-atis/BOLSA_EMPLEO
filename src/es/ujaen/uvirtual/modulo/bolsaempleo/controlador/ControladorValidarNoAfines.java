package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoValidarTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ValorMeritoBolsaTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloValidar;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Validación meritos de bolsas no sujetos a afinidad.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.validarnoafines", 
	description = "Validación de méritos no sujetos a afinidad", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/validarnoafines", 
			"/srv/en/informacionadministrativa/bolsaempleo/validarnoafines",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/validarnoafines", 
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/validarnoafines"
	})
public class ControladorValidarNoAfines extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorValidarNoAfines.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_DATATABLE_BOLSAS = "datatablebolsas";
	public static final String ACCION_DATATABLE_BOLSAS_CANDIDATO = "datatablebolsascandidato";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_MERITOS = "datatablemeritos";
	public static final String ACCION_DATATABLE_VALORES_MERITO_BOLSA = "datatablevaloresmeritobolsa";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String ACCION_BOLSA_SELECCIONADA = "bolsaseleccionada";
	public static final String ACCION_CANDIDATO_SELECCIONADO = "candidatoseleccionado";
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_MERITO_SELECCIONADO = "meritoseleccionado";
	public static final String ACCION_VALIDAR_MERITO = "validarmerito";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACEPTAR_MERITO = "aceptarmerito";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_BOLSAS = "bolsas";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_EXCLUIR_MERITO = "excluirmerito";
	public static final String PARAM_ITEM = "item";
	public static final String PARAM_MERITO = "merito";
	public static final String PARAM_OBSERVACION_CANDIDATO = "observacioncandidato";
	public static final String PARAM_VALOR = "valor";
	
	// vistas
	public static final String RUTA_BEP_VALIDAR_NO_AFINES = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/validarnoafines/";
	public static final String JSQP_INDEX = RUTA_BEP_VALIDAR_NO_AFINES + "index.jsp";
	public static final String JSP_MERITOS_CANDIDATOS = RUTA_BEP_VALIDAR_NO_AFINES + "meritoscandidatos.jsp";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_NO_HAY_BOLSAS_SELECCIONADAS = "No hay bolsas seleccionadas";
	public static final String MENSAJE_ERROR_SIN_PERMISO_PERSONAL = "No tienes permiso de personal";
	public static final String MENSAJE_EXITO_MERITO_EXCLUIDO = "Mérito excluido correctamente para la bolsa: %s";
	public static final String MENSAJE_EXITO_MERITO_GUARDAR = "Mérito guardado correctamente para la bolsa: %s";
	public static final String MENSAJE_EXITO_MERITO_VALIDADO = "Mérito validado correctamente para la bolsa: %s";
	
	// ajax 
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/validarnoafines";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaValidarNoAfines bean = new VistaValidarNoAfines();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_INDEX:
					break;
				case ACCION_BOLSA_SELECCIONADA:
				case ACCION_CANDIDATO_SELECCIONADO:
				case ACCION_DATATABLE_BOLSAS_CANDIDATO:
				case ACCION_DATATABLE_CANDIDATOS:
				case ACCION_DATATABLE_VALORES_MERITO_BOLSA:
				case ACCION_DATATABLE_MERITOS:
				case ACCION_MERITO_SELECCIONADO:
				case ACCION_VALIDAR_MERITO:
					bolsaSeleccionada(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_DATATABLE_BOLSAS:
					listadoBolsas(bean, datos, request, response);
					break;
				default:
					this.accionNodefinida(bean);
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	private void init(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSQP_INDEX);
		bean.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria());
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
			
			if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO_PERSONAL);
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void accionNodefinida(VistaValidarNoAfines bean) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
	}
	
	private void bolsaSeleccionada(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		bean.setBolsa(modeloBolsa.getBolsaById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_BOLSA))));
		bean.setVista(JSP_MERITOS_CANDIDATOS);
		
		switch (nombreAccion) {
			case ACCION_BOLSA_SELECCIONADA:
				break;
			case ACCION_DATATABLE_BOLSAS_CANDIDATO:
			case ACCION_CANDIDATO_SELECCIONADO:
			case ACCION_DATATABLE_MERITOS:
			case ACCION_DATATABLE_VALORES_MERITO_BOLSA:
			case ACCION_MERITO_SELECCIONADO:
			case ACCION_VALIDAR_MERITO:
				candidatoSeleccionado(bean, datos, request, response, nombreAccion);
				break;
			case ACCION_DATATABLE_CANDIDATOS:
				listadoCandidatos(bean, datos, request, response);
				break;
			default:
				accionNodefinida(bean);
		}
	}
	
	private void candidatoSeleccionado(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		bean.setCandidato(modeloUsuario.getUsuarioById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_CANDIDATO))));
		
		switch (nombreAccion) {
			case ACCION_CANDIDATO_SELECCIONADO:
				break;
			case ACCION_DATATABLE_MERITOS:
				listadoMeritos(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_BOLSAS_CANDIDATO:
			case ACCION_DATATABLE_VALORES_MERITO_BOLSA:
			case ACCION_MERITO_SELECCIONADO:
			case ACCION_VALIDAR_MERITO:
				meritoSeleccionado(bean, datos, request, response, nombreAccion);
				break;
			default:
				accionNodefinida(bean);
		}
		
	}
	
	private void meritoSeleccionado(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		
		ModeloBaremacionItems modeloItem = ModeloBaremacionItems.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Merito merito = new Merito(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_MERITO)));
		MeritoSolicitud meritoSolicitud = modeloSolicitud.getMeritoSolicitudByConvocatoria(bean.getConvocatoria(), bean.getBolsa(), merito);
		
		bean.setMerito(meritoSolicitud);
		bean.setItems(modeloItem.getItemsDeApartado(meritoSolicitud.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion()));
		
		switch (nombreAccion) {
			case ACCION_DATATABLE_BOLSAS_CANDIDATO:
				listadoBolsasCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_VALORES_MERITO_BOLSA:
				listadoValoresMeritoBolsa(bean, datos, request, response);
				break;
			case ACCION_MERITO_SELECCIONADO:
				break;
			case ACCION_VALIDAR_MERITO:
				validarMerito(bean, datos, request, response);
				break;
			default:
				accionNodefinida(bean);
		}
	}
	
	private void validarMerito(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, IOException {
		ModeloValidar modeloValidar = ModeloValidar.obtenerInstancia();
		Gson gson = new GsonBuilder().create();
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		
		try {
			String observacionCandidato = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_OBSERVACION_CANDIDATO));
			Collection<String> bolsas = gson.fromJson(request.getParameter(PARAM_BOLSAS), new TypeToken<Collection<String>>() { }.getType());
			if (bolsas.size() <= 0) {
				throw new UVException(MENSAJE_ERROR_NO_HAY_BOLSAS_SELECCIONADAS);
			}
			
			if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_VALOR)) != null) {
				// item de baremación
				Integer idItem = Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM));
				ItemBaremacion item = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(idItem);
				bean.getMerito().getMerito().setItemBaremacion(item);
				
				// valor
				Double valor = ModeloMerito.validateValorDelMerito(request.getParameter(PARAM_VALOR), bean.getMerito().getMerito());
				bean.getMerito().getMerito().setValor(valor);
				bean.getMerito().setValor(valor);
				
				bean.getMerito().getMerito().setUsuario(bean.getCandidato());
				
				ModeloMerito.obtenerInstancia().actualizaMerito(bean.getMerito().getMerito(), bean.getUsuarioLogeado());
								
				if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACEPTAR_MERITO)) != null) {
					for (String idBolsa : bolsas) {
						modeloValidar.validarMerito(idBolsa, bean.getMerito(), observacionCandidato,
								bean.getConvocatoria(), bean.getUsuarioLogeado());
						Bolsa bolsa = modeloBolsa.getBolsaById(Integer.parseInt(idBolsa));
						BolsaEmpleoUtils.addMensajeDeExito(String.format(MENSAJE_EXITO_MERITO_VALIDADO, bolsa.getArea().getDescripcion()), bean, request);
					}
				} else if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIR_MERITO)) != null) {
					for (String idBolsa : bolsas) {
						modeloValidar.excluirMerito(idBolsa, bean.getMerito(), observacionCandidato,
								bean.getConvocatoria(), bean.getUsuarioLogeado());
						Bolsa bolsa = modeloBolsa.getBolsaById(Integer.parseInt(idBolsa));
						BolsaEmpleoUtils.addMensajeDeExito(String.format(MENSAJE_EXITO_MERITO_EXCLUIDO, bolsa.getArea().getDescripcion()), bean, request);
					}
				}
			}
			
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(ex.getMessage(), bean, request);
		}
		
		redireccionConMeritoSeleccionado(bean, datos, request, response, ACCION_MERITO_SELECCIONADO);
	}
	
	private void redireccionConMeritoSeleccionado(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		params.put(PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		params.put(PARAM_MERITO, bean.getMerito().getMerito().getCodNum().toString());
		params.put(PARAM_ACCION, accion);
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	private void listadoBolsas(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaValidacion> dataTable = ModeloValidar.obtenerInstancia().
						listadoAreasNoSujetasAfinidad(bean.getConvocatoria(), request.getParameterMap());
				bean.setDatatableBolsas(dataTable);
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
	
	private void listadoBolsasCandidato(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = ModeloValidar.obtenerInstancia().listadoAreasCandidatoNoSujetasAfinidad(
						bean.getConvocatoria(), bean.getCandidato(), bean.getMerito().getMerito(), request.getParameterMap());
				bean.setDatatableBolsasCandidato(dataTable);
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
	
	private void listadoCandidatos(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoValidacion> dataTable = ModeloValidar.obtenerInstancia().
						listadoCandidatosNoSujetosAfinidad(bean.getConvocatoria(), bean.getBolsa(), request.getParameterMap());
				bean.setDatatableCandidatos(dataTable);
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
	
	private void listadoMeritos(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<MeritoValidarTable> dataTable = ModeloValidar.obtenerInstancia().
						listadoMeritosNoSujetosAfinidad(bean.getConvocatoria(), bean.getBolsa(), bean.getCandidato(), request.getParameterMap());
				bean.setDatatableMeritos(dataTable);
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
	
	private void listadoValoresMeritoBolsa(VistaValidarNoAfines bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<ValorMeritoBolsaTable> dataTable = ModeloValidar.obtenerInstancia().
						listadoValoresMeritoBolsa(bean.getCandidato(), bean.getMerito().getMerito(), request.getParameterMap());
				bean.setDatatableValoresMeritoBolsa(dataTable);
				writer.write(dataTable.toJson("YYYY"));
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
	
}
