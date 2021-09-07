package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoEstado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.informes.GenerarSolicitudPDF;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEstadoCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentesCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los usuarios CANDIDATOS de UVIRTUAL.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.candidatos", 
	description = "Gestión de usuarios candidatos", 
	urlPatterns = {
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/candidatos", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/candidatos",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/candidatos",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/candidatos"
	})
public class ControladorUsuarioCandidato extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorUsuarioBolsaEmpleo.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ACCION_USUARIO = "aa";
	public static final String PARAM_AREAS_SELECCIONADAS = "usuariosselected";
	public static final String PARAM_BOLSAS = "bolsas";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_ESTADO = "estado";
	public static final String PARAM_EXCLUIDO = "excluido";
	public static final String PARAM_EXCLUIDO_TIPO = "excluidotipo";
	public static final String PARAM_FECHA_EXCLUIDO_INICIO = "fechaexcluidoinicio";
	public static final String PARAM_FECHA_EXCLUIDO_FIN = "fechaexcluidofin";
	public static final String PARAM_GUARDAR = "guardar";
	public static final String PARAM_LISTA = "lista";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_NOMBRE_USUARIO = "nombreusuario";
	public static final String PARAM_RAZON_BORRADO = "razonborrado";
	public static final String PARAM_RAZON_EXCLUIDO = "razonexcluido";
	public static final String PARAM_SOLICITUD = "solicitud";
	public static final String PARAM_RAZON_EXCLUSION_SOLICITUD = "razonexclusionsolicitud";
	
	// acciones
	public static final String ACCION_ACREDITACIONES_CANDIDATO = "acreditacionescandidato";
	public static final String ACCION_AREAS_EXCLUIDAS_CANDIDATO = "areasexcluidascandidato";
	public static final String ACCION_CAMBIAR_ESTADO_CANDIDATO = "cambiarestadocandidato";
	public static final String ACCION_CONFIRMAR_SOLICITUD = "confirmarsolicitud";
	public static final String ACCION_CONTRATACIONES_CANDIDATO = "contratacionescandidato";
	public static final String ACCION_DATATABLE_ACREDITACIONES_CANDIDATO = "datatableacreditacionescandidato";
	public static final String ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO = "datatableusuariosexcluidoscandidato";
	public static final String ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO = "datatableareasnoexcluidascandidato";
	public static final String ACCION_DATATABLE_ESTADOS_CANDIDATO = "datatableestadoscandidato";
	public static final String ACCION_DATATABLE_MERITOS_CANDIDATO = "datatablemeritoscandidato";
	public static final String ACCION_DATATABLE_SOLICITUDES = "datatablesolicitudes";
	public static final String ACCION_DATATABLE_TITULACIONES_CANDIDATO = "datatabletitulacionescandidato";
	public static final String ACCION_DATATABLE_USUARIOS_CANDIDATOS = "datatableusuarioscandidatos";
	public static final String ACCION_EDITAR_CANDIDATO = "editarcandidato";
	public static final String ACCION_ELIMINAR_CANDIDATO = "eliminarcandidato";
	public static final String ACCION_EXCLUIR_USUARIO_AREA = "excluirusuarioarea";
	public static final String ACCION_INCLUIR_USUARIO_AREA = "incluirusuarioarea";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_MERITOS_CANDIDATO = "meritoscandidato";
	public static final String ACCION_REABRIR_SOLICITUD = "reabrirsolicitud";
	public static final String ACCION_RECUPERAR_CANDIDATO = "recuperarusuario";
	public static final String ACCION_SELECCIONAR_CANDIDATO = "seleccionarcandidato";
	public static final String ACCION_SELECCIONAR_SOLICITUD = "seleccionarsolicitud";
	public static final String ACCION_SOLICITUDES_CANDIDATO = "solicitudescandidato";
	public static final String ACCION_TITULACIONES_CANDIDATO = "titulacionescandidato";
	public static final String ACCION_VOLVER_CANDIDATO = "volvercandidato";
	public static final String ACCION_EXCLUIR_SOLICITUD = "excluirsolicitudcandidato";	
	public static final String ACCION_INCLUIR_SOLICITUD = "incluirsolicitudcandidato";
	
	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS = "No hay bolsas seleccionadas válidas";
	public static final String MENSAJE_ERROR_EXCLUIDO_TIPO_VACIO = "El tipo de exclusión no puede estar vacio";
	public static final String MENSAJE_ERROR_EXCLUIR_AREAS = "Error al excluir áreas";
	public static final String MENSAJE_ERROR_FECHA_EXCLUIDO_INICIO_REQUERIDA = "La fecha de inicio es requerida";
	public static final String MENSAJE_ERROR_FECHA_EXCLUIDO_FIN_REQUERIDA = "La fecha de fin es requerida";
	public static final String MENSAJE_ERROR_INCLUIR_AREAS = "Error al borrar áreas excluidas";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_LARGO = "La razón de exclusión no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_VACIO = "Si excluye al usuario, debe especificar una razón";
	public static final String MENSAJE_ERROR_USUARIO_NO_EXISTE = "No existe el usuario";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_REQUERIDA = "La razón de exclusión es requerida";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_LARGA = "La razón de exclusión es demasiado larga. Máximo %d carácteres";
	public static final String MENSAJE_ERROR_SIN_PERMISO_PERSONAL = "No tienes permiso de personal";
	
	public static final String MENSAJE_EXITO_AGREGAR = "Usuario creado correctamente";
	public static final String MENSAJE_EXITO_AREAS_EXCLUIDAS = "Áreas excluidas correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_AREA_EXCLUIDA = "Área excluida correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_AREAS_INCLUIDAS = "Áreas excluidas borrada correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_AREA_INCLUIDA = "Área excluida borrada correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_EDITAR = "Usuario editado correctamente";
	public static final String MENSAJE_EXITO_ESTADO_CAMBIADO = "El estado se ha modificado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Usuario eliminado correctamente";
	public static final String MENSAJE_EXITO_REABRIR_SOLICITUD = "Solicitud reabrierta correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Usuario restaurado correctamente";
	public static final String MENSAJE_EXITO_SOLICITUD_CONFIRMADA = "La solicitud ha sido confirmada correctamente";
	public static final String MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE = "Usuario/s modificado/s correctamente";
	public static final String MENSAJE_EXITO_EXCLUSION_SOLICITUD = "Solicitud excluida correctamente";
	public static final String MENSAJE_EXITO_INCLUSION_SOLICITUD = "Solicitud incluida correctamente";
	
	public static final String URL_PATTERN = "/srv/es/informacionadministrativa/bolsaempleo/configuracion/candidatos";
	
	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/candidatos";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF_CAND = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/candidatos/";
	public static final String JSP_INDEX = RUTA_BEP_CONF_CAND + "candidatos.jsp";	
	public static final String JSP_FORM_CANDIDATO = RUTA_BEP_CONF_CAND + "formCandidatos.jsp";
	public static final String JSP_RESUMEN_SOLICITUD = RUTA_BEP_CONF_CAND + "resumenSolicitud.jsp";
		
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaCandidatos bean = new VistaCandidatos();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_ACREDITACIONES_CANDIDATO:
				case ACCION_AREAS_EXCLUIDAS_CANDIDATO:
				case ACCION_CAMBIAR_ESTADO_CANDIDATO:
				case ACCION_CONFIRMAR_SOLICITUD:
				case ACCION_CONTRATACIONES_CANDIDATO:
				case ACCION_DATATABLE_ACREDITACIONES_CANDIDATO:
				case ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO:
				case ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO:
				case ACCION_DATATABLE_ESTADOS_CANDIDATO:
				case ACCION_DATATABLE_MERITOS_CANDIDATO:
				case ACCION_DATATABLE_SOLICITUDES:
				case ACCION_DATATABLE_TITULACIONES_CANDIDATO:
				case ACCION_EDITAR_CANDIDATO:
				case ACCION_ELIMINAR_CANDIDATO:
				case ACCION_EXCLUIR_USUARIO_AREA:
				case ACCION_INCLUIR_USUARIO_AREA:
				case ACCION_MERITOS_CANDIDATO:
				case ACCION_REABRIR_SOLICITUD:
				case ACCION_RECUPERAR_CANDIDATO:
				case ACCION_SELECCIONAR_CANDIDATO:
				case ACCION_SELECCIONAR_SOLICITUD:
				case ACCION_SOLICITUDES_CANDIDATO:
				case ACCION_TITULACIONES_CANDIDATO:
				case ACCION_VOLVER_CANDIDATO:
				case ACCION_EXCLUIR_SOLICITUD:
				case ACCION_INCLUIR_SOLICITUD:
					accionesCandidato(bean, datos, request, response, nombreAccion);
					break;
				case ACCION_INDEX:
					bean.setVista(JSP_INDEX);
					break;
				case ACCION_DATATABLE_USUARIOS_CANDIDATOS:
					listadoCandidatos(bean, datos, request, response);
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
			datos.getFicherosCSS().add(ModeloParametrosConfiguracion.CSS_BOLSA_EMPLEO);
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		}
	}
		
	/** Redireccion de do post.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	private void init(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
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
	
	private void errorFatal(VistaCandidatos bean, String mensaje) {
		bean.setVista("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
		bean.getMensajesDeError().add(mensaje);
	}
	
	private void accionesCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_FORM_CANDIDATO);
		
		Integer idCandidato = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_CANDIDATO));
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(idCandidato);
		bean.setCandidato(candidato);
		
		switch (nombreAccion) {
			case ACCION_ACREDITACIONES_CANDIDATO:
				obtenerCodigoPadreAcreditacion(bean);
				break;
			case ACCION_AREAS_EXCLUIDAS_CANDIDATO:
				bean.setApartadoAreasExcluidas(true);
				break;
			case ACCION_CAMBIAR_ESTADO_CANDIDATO:
				cambiarEstadoCandidato(bean, datos, request, response);
				break;
			case ACCION_CONTRATACIONES_CANDIDATO:
				bean.setApartadoContrataciones(true);
				break;
			case ACCION_DATATABLE_ACREDITACIONES_CANDIDATO:
				listadoAcreditacionesCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO:
				listadoAreasExcluidasCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO:
				listadoAreasNoExcluidasCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_ESTADOS_CANDIDATO:
				listadoEstadosCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_MERITOS_CANDIDATO:
				listadoMeritosCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_SOLICITUDES:
				listadoSolicitudes(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_TITULACIONES_CANDIDATO:
				listadoTitulacionesCandidato(bean, datos, request, response);
				break;
			case ACCION_EDITAR_CANDIDATO:
				editarCandidato(bean, datos, request, response);
				break;
			case ACCION_ELIMINAR_CANDIDATO:
				eliminarCandidato(bean, datos, request, response);
				break;
			case ACCION_EXCLUIR_USUARIO_AREA:
				excluirUsuarioArea(bean, datos, request, response, true);
				break;
			case ACCION_INCLUIR_USUARIO_AREA:
				excluirUsuarioArea(bean, datos, request, response, false);
				break;
			case ACCION_MERITOS_CANDIDATO:
				meritosCandidato(bean);
				break;
			case ACCION_CONFIRMAR_SOLICITUD:
			case ACCION_REABRIR_SOLICITUD:
			case ACCION_SELECCIONAR_SOLICITUD:
			case ACCION_EXCLUIR_SOLICITUD:
			case ACCION_INCLUIR_SOLICITUD:
				accionesSolicitud(bean, datos, request, response, nombreAccion);
				break;
			case ACCION_RECUPERAR_CANDIDATO:
				recuperarCantidato(bean, datos, request, response);
				break;
			case ACCION_SELECCIONAR_CANDIDATO:
				break;
			case ACCION_SOLICITUDES_CANDIDATO:
				bean.setApartadoSolicitudes(true);
				break;
			case ACCION_TITULACIONES_CANDIDATO:
				bean.setApartadoTitulaciones(true);
				break;
			case ACCION_VOLVER_CANDIDATO:
				redireccionConCandidatoSeleccionado(bean, datos, request, response, ACCION_SOLICITUDES_CANDIDATO);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void accionesSolicitud(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion)
			throws SQLException, UVException, IOException {
		Integer idSolicitud = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_SOLICITUD));
		Solicitud solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudById(idSolicitud);
		bean.setSolicitud(solicitud);
		
		switch (nombreAccion) {
			case ACCION_CONFIRMAR_SOLICITUD:
				confirmarSolicitudCandidato(bean, datos, request, response);
				break;
			case ACCION_REABRIR_SOLICITUD:
				reabrirSolicitudCandidato(bean, datos, request, response);
				break;
			case ACCION_SELECCIONAR_SOLICITUD:
				resumenSolicitudCandidato(bean);
				break;
			case ACCION_EXCLUIR_SOLICITUD:
				excluirSolicitudCandidato(bean, datos, request, response);
				break;
			case ACCION_INCLUIR_SOLICITUD:
				incluirSolicitudCandidato(bean, datos, request, response);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	private void excluirSolicitudCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		Solicitud solicitud = bean.getSolicitud();
		
		solicitud.setRazonExclusion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUSION_SOLICITUD)));
		if (solicitud.getRazonExclusion() == null || solicitud.getRazonExclusion().isEmpty()) {
			throw new UVException(MENSAJE_ERROR_RAZON_EXCLUSION_REQUERIDA);
		}
		if (solicitud.getRazonExclusion().length() > ModeloSolicitud.RAZON_EXCLUSION_SOLICITUD_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_RAZON_EXCLUSION_LARGA, ModeloSolicitud.RAZON_EXCLUSION_SOLICITUD_MAXLENGTH));
		}
			
		ModeloSolicitud.obtenerInstancia().excluirIncluirSolicitud(solicitud, true, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EXCLUSION_SOLICITUD, bean, request);
		redireccionConSolicitudSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_SOLICITUD);
	}
	
	private void incluirSolicitudCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws UVException, SQLException, IOException {
		Solicitud solicitud = bean.getSolicitud();
		ModeloSolicitud.obtenerInstancia().excluirIncluirSolicitud(solicitud, false, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_INCLUSION_SOLICITUD, bean, request);
		redireccionConSolicitudSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_SOLICITUD);
	}
	
	/** Confirmar la solicitud del candidato .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void confirmarSolicitudCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(JSP_RESUMEN_SOLICITUD);
		
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		List<BolsaSolicitud> listaBolsas = modeloSolicitud.getBolsasSolicitudMeritos(bean.getSolicitud());
		bean.setListaBolsasSolicitud(listaBolsas);
		
		modeloSolicitud.comprobarSolicitudCorrecta(bean.getSolicitud(), true);
		
		bean.getSolicitud().setEstado(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
		bean.getSolicitud().setFechaConfirmacion(BolsaEmpleoUtils.getCurrentDateTime());
		
		try (InputStream archivo = GenerarSolicitudPDF.generarPDF(bean.getSolicitud(), listaBolsas)) {
			bean.getSolicitud().setArchivo(archivo);
		}
		
		modeloSolicitud.confirmacionSolicitud(bean.getSolicitud(), bean.getCandidato());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_SOLICITUD_CONFIRMADA, bean, request);
		redireccionConSolicitudSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_SOLICITUD);
	}
	
	private void cambiarEstadoCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		String selectedJson = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BOLSAS));
		String estado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ESTADO));
		
		int[] selected;
		List<Bolsa> bolsas;
		
		try {
			selected = (new Gson()).fromJson(selectedJson, new TypeToken<int[]>() { }.getType());
			bolsas = ModeloBolsa.obtenerInstancia().getBolsasByIds(selected);
		} catch (Exception ex) {
			throw new UVException(MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS);
		}
		
		ModeloEstadoCandidato.obtenerInstancia().cambiarEstadosCandidato(bean.getCandidato(), estado, bolsas, bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ESTADO_CAMBIADO, bean, request);
		
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_ACCION, ACCION_CONTRATACIONES_CANDIDATO);
		params.put(PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	/** edita un candidato .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error en bd .
	 * @throws IOException en caso error de input u output .
	 */
	private void editarCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		
		if (request.getParameter(PARAM_GUARDAR) != null) {
			Usuario usuArcos = CrearUsuario.usuario(bean.getCandidato().getCodCuenta());
			if (usuArcos == null) {
				throw new UVException(MENSAJE_ERROR_USUARIO_NO_EXISTE);
			}
			
			UsuarioBolsaEmpleo usuarioForm = this.getValidatorUsuarios(request);
			
			usuarioForm.setUsuarioArcos(usuArcos);
			usuarioForm.setCodCuenta(bean.getCandidato().getCodCuenta());
			usuarioForm.setCodNum(Formateador.leeParametroInteger(request.getParameter(PARAM_CANDIDATO)));
			usuarioForm.setDireccion(bean.getCandidato().getDireccion());
			usuarioForm.setCodigoPostal(bean.getCandidato().getCodigoPostal());
			usuarioForm.setLocalidad(bean.getCandidato().getLocalidad());
			usuarioForm.setProvincia(bean.getCandidato().getProvincia());
			usuarioForm.setNacionalidad(bean.getCandidato().getNacionalidad());
			usuarioForm.setTelefono(bean.getCandidato().getTelefono());
			
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuario(usuarioForm, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
			datos.setRespuestaEnviada(true);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void excluirUsuarioArea(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, boolean excluir)
			throws SQLException, UVException, IOException {
		ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		int[] idsAreas = (new Gson()).fromJson(request.getParameter(PARAM_AREAS_SELECCIONADAS), new TypeToken<int[]>() { }.getType());
		List<Area> areas = ModeloArea.obtenerInstancia().getAreasByIds(idsAreas);
		bean.setListaAreas(areas);
		
		try {
			if (areas.size() > 0) {
				if (excluir) {
					modeloUsuario.excluirUsuarioArea(bean.getCandidato(), areas, bean.getUsuarioLogeado());
					String mensaje = String.format(areas.size() > 1 ? MENSAJE_EXITO_AREAS_EXCLUIDAS : MENSAJE_EXITO_AREA_EXCLUIDA,
							bean.getCandidato().getCodCuenta());
					BolsaEmpleoUtils.addMensajeDeExito(mensaje, bean, request);
				} else {
					modeloUsuario.incluirUsuarioArea(bean.getCandidato(), areas);
					String mensaje = String.format(areas.size() > 1 ? MENSAJE_EXITO_AREAS_INCLUIDAS : MENSAJE_EXITO_AREA_INCLUIDA,
							bean.getCandidato().getCodCuenta());
					BolsaEmpleoUtils.addMensajeDeExito(mensaje, bean, request);
				}
			}
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(ex));
			LOGGER.log(Level.SEVERE, ex.toString());
			
			BolsaEmpleoUtils.addMensajeDeError(excluir ? MENSAJE_ERROR_EXCLUIR_AREAS : MENSAJE_ERROR_INCLUIR_AREAS, bean, request);
		}
		
		redireccionConCandidatoSeleccionado(bean, datos, request, response, ACCION_AREAS_EXCLUIDAS_CANDIDATO);
	}
	
	private void meritosCandidato(VistaCandidatos bean) throws SQLException {
		bean.setApartadoMeritos(true);
		ModeloBaremacionApartados modeloBaremacion = ModeloBaremacionApartados.obtenerInstancia();
		bean.setApartados(modeloBaremacion.getApartadosActivos());
	}
	
	private void obtenerCodigoPadreAcreditacion(VistaCandidatos bean) throws SQLException, UVException {
		ParametrosConfiguracion config = ModeloParametrosConfiguracion.obtenerInstancia().getParametroByNombre("bolsaempleo.local.codMeritoPreferente");
		bean.setCodigoPadreMeritoPreferente(config.getValor());
		bean.setApartadoAcreditaciones(true);
	}
	
	private void reabrirSolicitudCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		Solicitud solicitud = bean.getSolicitud();
		solicitud.setEstado(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA);
		modeloSolicitud.reabrirSolicitud(solicitud, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_REABRIR_SOLICITUD, bean, request);
		redireccionConSolicitudSeleccionada(bean, datos, request, response, ACCION_SELECCIONAR_SOLICITUD);
	}
	
	private void redireccionConCandidatoSeleccionado(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_ACCION, accion);
		params.put(PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	private void redireccionConSolicitudSeleccionada(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_ACCION, accion);
		params.put(PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		params.put(PARAM_SOLICITUD, bean.getSolicitud().getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(datos, request, response, params);
	}
	
	private void eliminarCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		String razonBorrado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_BORRADO));
		bean.getCandidato().setRazonBorrado(razonBorrado);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoBorrado(bean.getCandidato(), bean.getUsuarioLogeado());	
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void recuperarCantidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoNoBorrado(bean.getCandidato(), bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_RESTAURAR, bean, request);
		datos.setRespuestaEnviada(true);
		response.sendRedirect(request.getServletPath());
	}
	
	private void resumenSolicitudCandidato(VistaCandidatos bean)
			throws SQLException, UVException {
		List<BolsaSolicitud> listaBolsas = ModeloSolicitud.obtenerInstancia().getBolsasSolicitudMeritos(bean.getSolicitud());
		bean.setListaBolsasSolicitud(listaBolsas);
		bean.setVista(JSP_RESUMEN_SOLICITUD);
	}
	
	/**
	 * Lista de acreditaciones .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoAcreditacionesCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable = ModeloMeritosPreferentesCandidato.obtenerInstancia().
						listaAcreditacionesCandidatoDatatable(request.getParameterMap(), bean.getCandidato());
				bean.setDataTableAcreditaciones(dataTable);
				writer.write(dataTable.toJson());
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
	
	/**
	 * AJAX para devolver listado de usuarios con rol CANDIDATO.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException . 
	 * @throws SQLException .
	 */
	private void listadoCandidatos(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();	
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = modelo.listaUsuarioCandidatosBolsaEmpleoDatatable(request.getParameterMap());
				bean.setDatatableCandidatos(dataTable);
				writer.write(dataTable.toJson());
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
	
	/**
	 * AJAX para devolver listado de areas excluidas de un usuario.
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 */
	private void listadoAreasExcluidasCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = ModeloArea.obtenerInstancia().
						listaAreasExcluidasPorUsuarioDatatable(request.getParameterMap(), bean.getCandidato().getCodNum());
				bean.setDatatableAreas(dataTable);
				writer.write(dataTable.toJson());
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
	
	/**
	 * AJAX para devolver listado de areas no excluidas de un usuario(bolsas).
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoAreasNoExcluidasCandidato(VistaCandidatos bean, UVDatos datos, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Bolsa> dataTable = ModeloArea.obtenerInstancia().
						listaAreaExcluidasUsuarioDatatable(request.getParameterMap(), bean.getCandidato().getCodNum());
				bean.setDatatableAreas(dataTable);
				writer.write(dataTable.toJson());
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
	
	/**
	 * Lista de méritos del candidato .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoMeritosCandidato(VistaCandidatos bean, UVDatos datos,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Merito> dataTable = ModeloMerito.obtenerInstancia().listaMeritosCandidatoDatatable(
						request.getParameterMap(), bean.getCandidato());
				bean.setDataTableMeritos(dataTable);
				writer.write(dataTable.toJson());
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
	
	/**
	 * Lista de solicitudes .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoSolicitudes(VistaCandidatos bean, UVDatos datos,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<Solicitud> dataTable = ModeloSolicitud.obtenerInstancia().listaSolicitudesCandidatoDatatable(
						bean.getCandidato(), request.getParameterMap());
				bean.setDataTableSolicitudes(dataTable);
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
	
	/**
	 * Lista de titulaciones .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @throws UVException .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void listadoTitulacionesCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<TitulacionUsuario> dataTable = ModeloMisTitulaciones.obtenerInstancia().
						listaTitulacionesCandidatoDatatable(request.getParameterMap(), bean.getCandidato());
				bean.setDataTableTitulaciones(dataTable);
				writer.write(dataTable.toJson());
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
	
	private void listadoEstadosCandidato(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<CandidatoEstado> dataTable = ModeloEstadoCandidato.obtenerInstancia().
						listaEstadosCandidato(request.getParameterMap(), bean.getCandidato());
				bean.setDataTableEstadosCandidato(dataTable);
				writer.write(dataTable.toJson());
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
	
	private UsuarioBolsaEmpleo getValidatorUsuarios(HttpServletRequest request) throws UVException, SQLException {
		UsuarioBolsaEmpleo u = new UsuarioBolsaEmpleo();
		
		String excluido = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO));
		u.setExcluido("true".equals(excluido));
		if (excluido != null) {
			u.setRazonExcluido(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_RAZON_EXCLUIDO)));
			if (u.getRazonExcluido() == null || u.getRazonExcluido().isBlank()) {
				throw new UVException(MENSAJE_ERROR_RAZON_EXCLUSION_VACIO);
			}
			if (u.getRazonExcluido().length() > ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH) {
				throw new UVException(String.format(MENSAJE_ERROR_RAZON_EXCLUSION_LARGO, ModeloUsuarioBolsaEmpleo.COLUMN_RAZON_EXCLUSION_MAXLENGTH));
			}
			u.setExcluidoTipo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_EXCLUIDO_TIPO)));
			
			if (u.getExcluidoTipo() == null) {
				throw new UVException(MENSAJE_ERROR_EXCLUIDO_TIPO_VACIO);
			}
			
			if ("T".equals(u.getExcluidoTipo())) {
				
				u.setFechaExclusionInicio(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_INICIO), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
				u.setFechaExclusionFin(Formateador.leeParametroFecha(request.getParameter(PARAM_FECHA_EXCLUIDO_FIN), 
						Formateador.FORMATO_FECHA_DDMMYYYY, "/"));
				
				if (u.getFechaExclusionInicio() == null) {
					throw new UVException(MENSAJE_ERROR_FECHA_EXCLUIDO_INICIO_REQUERIDA);
				}
				
				if (u.getFechaExclusionFin() == null) {
					throw new UVException(MENSAJE_ERROR_FECHA_EXCLUIDO_FIN_REQUERIDA);
				}
			}
			u.setFechaExclusion(BolsaEmpleoUtils.getCurrentDateTime());
		}
		
		u.setRol(ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_CANDIDATO));
		u.setListaDist("true".equals(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_LISTA))));
		return u;
	}
	
	/**
	 * Devuelve una url de. detalle de un candidato.
	 * @param codnum .
	 * @return .
	 */
	public static String getUrlCandidato(String codnum) {
		return String.format("%s?%s=%s&%s=%s", URL_PATTERN, PARAM_ACCION, ACCION_SELECCIONAR_CANDIDATO, PARAM_CANDIDATO, codnum);
	}
}
