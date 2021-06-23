package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoValidarTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisMeritos;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloValidar;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Validación de meritos de bolsas sujetas a afinidad.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.validar", 
	description = "Validación de méritos sujetos a afinidad", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/validar", 
			"/srv/en/informacionadministrativa/bolsaempleo/validar",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/validar",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/validar"
	})
public class ControladorValidar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorValidar.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones
	public static final String ACCION_DATATABLE_BOLSAS = "datatablebolsas";
	public static final String ACCION_DATATABLE_CANDIDATOS = "datatablecandidatos";
	public static final String ACCION_DATATABLE_MERITOS = "datatablemeritos";
	public static final String ACCION_BOLSA_SELECCIONADA = "bolsaseleccionada";
	public static final String ACCION_CANDIDATO_SELECCIONADO = "candidatoseleccionado";
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_MERITO_SELECCIONADO = "meritoseleccionado";
	public static final String ACCION_MODIFICAR_MERITO = "modificarmerito";
	public static final String ACCION_VALIDAR_MERITO = "validarmerito";
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACEPTAR_MERITO = "aceptarmerito";
	public static final String PARAM_AFINIDADES = "afinidades";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_BOLSAS = "bolsas";
	public static final String PARAM_BOLSA_MERITO = "bolsamerito";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_EXCLUIR_MERITO = "excluirmerito";
	public static final String PARAM_GUARDAR_MERITO = "guardarmerito";
	public static final String PARAM_ITEM = "item";
	public static final String PARAM_MERITO = "merito";
	public static final String PARAM_OBSERVACION_CANDIDATO = "observacioncandidato";
	public static final String PARAM_VALOR = "valor";
	
	// vistas
	public static final String RUTA_BEP_VALIDAR = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/validar/";
	public static final String JSQP_INDEX = RUTA_BEP_VALIDAR + "index.jsp";
	public static final String JSP_MERITOS_CANDIDATOS = RUTA_BEP_VALIDAR + "meritoscandidatos.jsp";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_NO_HAY_BOLSAS_SELECCIONADAS = "No hay bolsas seleccionadas";
	public static final String MENSAJE_EXITO_MERITO_EXCLUIDO = "Mérito excluido correctamente para la bolsa: %s";
	public static final String MENSAJE_EXITO_MERITO_GUARDAR = "Mérito guardado correctamente para la bolsa: %s";
	public static final String MENSAJE_EXITO_MERITO_VALIDADO = "Mérito validado correctamente para la bolsa: %s";
	public static final String MENSAJE_EXITO_MERITO_MODIFICAR = "Mérito modificado correctamente";
	
	// ajax 
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/validar";
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
		
		VistaValidar bean = new VistaValidar();
		
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
				case ACCION_DATATABLE_CANDIDATOS:
				case ACCION_DATATABLE_MERITOS:
				case ACCION_MERITO_SELECCIONADO:
				case ACCION_MODIFICAR_MERITO:
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	private void init(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSQP_INDEX);
		bean.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria());
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		
		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos));
			
			// personal, comision, direccion
			int[] rolesValidos = {ModeloRol.ID_ROL_SERVICIO_PERSONAL, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO, ModeloRol.ID_ROL_MIEMBRO_COMISION};
			boolean contains = IntStream.of(rolesValidos).
					anyMatch(x -> x == bean.getUsuarioLogeado().getRol().getCodNum());
			
			if (!contains) {
				throw new UVException("No tienes permiso");
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			
			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
		}
	}
	
	private void accionNodefinida(VistaValidar bean) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
	}
	
	private void bolsaSeleccionada(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		bean.setBolsa(modeloBolsa.getBolsaById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_BOLSA))));
		bean.setVista(JSP_MERITOS_CANDIDATOS);
		
		switch (nombreAccion) {
			case ACCION_BOLSA_SELECCIONADA:
				break;
			case ACCION_CANDIDATO_SELECCIONADO:
			case ACCION_DATATABLE_MERITOS:
			case ACCION_MERITO_SELECCIONADO:
			case ACCION_MODIFICAR_MERITO:
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
	
	private void candidatoSeleccionado(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		bean.setCandidato(modeloUsuario.getUsuarioById(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_CANDIDATO))));
		
		switch (nombreAccion) {
			case ACCION_CANDIDATO_SELECCIONADO:
				break;
			case ACCION_DATATABLE_MERITOS:
				listadoMeritos(bean, datos, request, response);
				break;
			case ACCION_MERITO_SELECCIONADO:
			case ACCION_MODIFICAR_MERITO:
			case ACCION_VALIDAR_MERITO:
				meritoSeleccionado(bean, datos, request, response, nombreAccion);
				break;
			default:
				accionNodefinida(bean);
		}
		
	}
	
	private void meritoSeleccionado(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		
		ModeloBaremacionItems modeloItem = ModeloBaremacionItems.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Merito merito = new Merito(Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_MERITO)));
		MeritoSolicitud meritoSolicitud = modeloSolicitud.getMeritoSolicitudByConvocatoria(bean.getConvocatoria(), bean.getBolsa(), merito);
		
		bean.setMerito(meritoSolicitud);
		bean.setItems(modeloItem.getItemsDeApartado(meritoSolicitud.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion()));
		
		switch (nombreAccion) {
			case ACCION_MERITO_SELECCIONADO:
				obtenerValoresMeritoBolsasCandidato(bean);
				break;
			case ACCION_MODIFICAR_MERITO:
				modificarMerito(bean, datos, request, response, merito);
				break;
			case ACCION_VALIDAR_MERITO:
				validarMerito(bean, datos, request, response);
				break;
			default:
				accionNodefinida(bean);
		}
		
	}
	
	private void modificarMerito(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, Merito merito) throws UVException, IOException {
		try {
			if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_GUARDAR_MERITO)) != null) {
				// item de baremación
				Integer idItem = Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM));
				ItemBaremacion item = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(idItem);
				merito.setItemBaremacion(item);
				
				// valor
				merito.setValor(ControladorMisMeritos.validateValorDelMerito(request.getParameter(PARAM_VALOR), merito));
				merito.setUsuario(bean.getCandidato());
				
				ModeloMerito.obtenerInstancia().actualizaMerito(merito, bean.getUsuarioLogeado());
				BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_MERITO_MODIFICAR, bean, request);
			}
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(ex.getMessage(), bean, request);
		}
		
		redireccionConMeritoSeleccionado(bean, datos, request, response, ACCION_MERITO_SELECCIONADO);
	}
	
	private void obtenerValoresMeritoBolsasCandidato(VistaValidar bean) throws SQLException, UVException {
		ModeloValidar modeloValidar = ModeloValidar.obtenerInstancia();
		
		bean.setBolsas(modeloValidar.listadoAreasCandidatoSujetasAfinidad(bean.getConvocatoria(), bean.getCandidato(), bean.getMerito(), bean.getUsuarioLogeado()));
		bean.setListaAfinidades(ModeloAfinidad.obtenerInstancia().listaAfinidades());
	}
	
	private void validarMerito(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, IOException {
		ModeloValidar modeloValidar = ModeloValidar.obtenerInstancia();
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		
		try {
			String observacionesCandidato = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_OBSERVACION_CANDIDATO));
			Integer idBolsa = Formateador.leeParametroInteger(request.getParameter(PARAM_BOLSA_MERITO));
			Bolsa bolsa = modeloBolsa.getBolsaById(idBolsa);
			actualizaAfinidades(bean, request, bolsa);
			
			if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACEPTAR_MERITO)) != null) {
				modeloValidar.validarMerito(idBolsa.toString(), bean.getMerito().getMerito().getCodNum(),
						observacionesCandidato, bean.getConvocatoria(), bean.getUsuarioLogeado());
				BolsaEmpleoUtils.addMensajeDeExito(String.format(MENSAJE_EXITO_MERITO_VALIDADO, bolsa.getArea().getDescripcion()), bean, request);
			} else if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIR_MERITO)) != null) {
				modeloValidar.excluirMeritoEnBolsas(bean.getMerito().getMerito().getCodNum(),
						observacionesCandidato, bean.getConvocatoria(), bean.getUsuarioLogeado());
				BolsaEmpleoUtils.addMensajeDeExito(String.format(MENSAJE_EXITO_MERITO_EXCLUIDO, bolsa.getArea().getDescripcion()), bean, request);
			}
			
		} catch (Exception ex) {
			BolsaEmpleoUtils.addMensajeDeError(ex.getMessage(), bean, request);
		}
		
		redireccionConMeritoSeleccionado(bean, datos, request, response, ACCION_MERITO_SELECCIONADO);
	}
	
	private void actualizaAfinidades(VistaValidar bean, HttpServletRequest request, Bolsa bolsa) throws UVException, SQLException {
		Gson gson = new GsonBuilder().create();
		
		// parseamos json afinidades
		HashMap<String, Double> afinidadesRaw;
		try {
			afinidadesRaw = gson.fromJson(request.getParameter(PARAM_AFINIDADES), new TypeToken<HashMap<String, Double>>() { }.getType());			
		} catch (Exception e) {
			throw new UVException("Afinidades incorrectas");
		}
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
		Solicitud solicitud = modeloSolicitud.getSolicitudCerradaByConvocatoriaUsuario(bean.getCandidato(), bean.getConvocatoria());
		
		
		if (!bean.getMerito().getMerito().getItemBaremacion().getIndividualizado()) {
			// comprobamos validez de las afinidades
			HashMap<Afinidad, Double> afinidades = new HashMap<>();
			Double total = 0.0;
			for (Map.Entry<String, Double> entry : afinidadesRaw.entrySet()) {
				Afinidad a = modeloAfinidad.getAfinidadById(Formateador.leeParametroInteger(entry.getKey()));
				afinidades.put(a, entry.getValue());
				total += entry.getValue();
			}
			
			Double totalRounder = BolsaEmpleoUtils.redondeo(total);
			if (!totalRounder.equals(bean.getMerito().getMerito().getValor())) {
				throw new UVException("El total de afinidades tiene que ser igual al valor del mérito");
			}
			
			modeloSolicitud.actualizarAfinidadesMeritoNoIndividualizado(solicitud, bolsa, bean.getMerito().getMerito(), afinidades, bean.getUsuarioLogeado());
		} else {
			Map.Entry<String, Double> entry = afinidadesRaw.entrySet().iterator().next();
			String idAfinidad = entry.getKey();
			Double idValoracion = entry.getValue();
			Afinidad afinidad = modeloAfinidad.getAfinidadById(Formateador.leeParametroInteger(idAfinidad));
			modeloSolicitud.actualizarAfinidadesMeritoIndividualizado(solicitud, bolsa, bean.getMerito().getMerito(), afinidad,
					(int) Math.round(idValoracion), bean.getUsuarioLogeado());
		}
	}
	
	private void redireccionConMeritoSeleccionado(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		params.put(PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		params.put(PARAM_MERITO, bean.getMerito().getMerito().getCodNum().toString());
		params.put(PARAM_ACCION, accion);
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	private void listadoBolsas(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BolsaValidacion> dataTable = ModeloValidar.obtenerInstancia().
						listadoAreasSujetasAfinidad(bean.getConvocatoria(), bean.getUsuarioLogeado(), request.getParameterMap());
				bean.setDatatableBolsas(dataTable);
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
	
	private void listadoCandidatos(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoValidacion> dataTable = ModeloValidar.obtenerInstancia().
						listadoCandidatosSujetosAfinidad(bean.getConvocatoria(), bean.getBolsa(), request.getParameterMap());
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
	
	private void listadoMeritos(VistaValidar bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<MeritoValidarTable> dataTable = ModeloValidar.obtenerInstancia().
						listadoMeritosSujetosAfinidad(bean.getConvocatoria(), bean.getBolsa(), bean.getCandidato(), request.getParameterMap());
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
	
	
	
}
