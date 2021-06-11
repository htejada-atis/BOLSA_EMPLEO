package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
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
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_EXCLUIDO = "excluido";
	public static final String PARAM_EXCLUIDO_TIPO = "excluidotipo";
	public static final String PARAM_FECHA_EXCLUIDO_INICIO = "fechaexcluidoinicio";
	public static final String PARAM_FECHA_EXCLUIDO_FIN = "fechaexcluidofin";
	public static final String PARAM_GUARDAR = "guardar";
	public static final String PARAM_LISTA = "lista";
	public static final String PARAM_NOMBRE = "nombre";
	public static final String PARAM_NOMBRE_USUARIO = "nombreusuario";
	public static final String PARAM_RAZON_EXCLUIDO = "razonexcluido";
	public static final String PARAM_SOLICITUD = "solicitud";
	
	// acciones
	public static final String ACCION_APARTADO_AREA = "apartadoarea";
	public static final String ACCION_APARTADO_SOLICITUDES = "apartadosolicitudes";
	public static final String ACCION_APARTADO_COMUNICACIONES = "apartadocomunicaciones";
	public static final String ACCION_AREAS_EXCLUIDAS_CANDIDATO = "areasexcluidascandidato";
	public static final String ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO = "datatableusuariosexcluidoscandidato";
	public static final String ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO = "datatableareasnoexcluidascandidato";
	public static final String ACCION_DATATABLE_SOLICITUDES = "datatablesolicitudes";
	public static final String ACCION_DATATABLE_USUARIOS_CANDIDATOS = "datatableusuarioscandidatos";
	public static final String ACCION_EDITAR_CANDIDATO = "editarcandidato";
	public static final String ACCION_ELIMINAR_CANDIDATO = "eliminarcandidato";
	public static final String ACCION_EXCLUIR_USUARIO_AREA = "excluirusuarioarea";
	public static final String ACCION_INCLUIR_USUARIO_AREA = "incluirusuarioarea";
	public static final String ACCION_INDEX = "index";
	public static final String ACCION_RECUPERAR_CANDIDATO = "recuperarusuario";
	public static final String ACCION_SELECCIONAR_CANDIDATO = "seleccionarcandidato";
	public static final String ACCION_SELECCIONAR_SOLICITUD = "seleccionarsolicitud";
	public static final String ACCION_SOLICITUDES_CANDIDATO = "solicitudescandidato";
	
	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_ERROR_INCLUIR_AREAS = "Error al borrar áreas excluidas";
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_EXCLUIDO_TIPO_VACIO = "El tipo de exclusión no puede estar vacio";
	public static final String MENSAJE_ERROR_EXCLUIR_AREAS = "Error al excluir áreas";
	public static final String MENSAJE_ERROR_FECHA_EXCLUIDO_INICIO_REQUERIDA = "La fecha de inicio es requerida";
	public static final String MENSAJE_ERROR_FECHA_EXCLUIDO_FIN_REQUERIDA = "La fecha de fin es requerida";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_LARGO = "La razón de exclusión no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_RAZON_EXCLUSION_VACIO = "Si excluye al usuario, debe especificar una razón";
	public static final String MENSAJE_ERROR_USUARIO_NO_EXISTE = "No existe el usuario";
	
	public static final String MENSAJE_EXITO_AGREGAR = "Usuario creado correctamente";
	public static final String MENSAJE_EXITO_AREAS_EXCLUIDAS = "Áreas excluidas correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_AREA_EXCLUIDA = "Área excluida correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_AREAS_INCLUIDAS = "Áreas excluidas borrada correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_AREA_INCLUIDA = "Área excluida borrada correctamente para el candidato: %s";
	public static final String MENSAJE_EXITO_EDITAR = "Usuario editado correctamente";
	public static final String MENSAJE_EXITO_ELIMINAR = "Usuario eliminado correctamente";
	public static final String MENSAJE_EXITO_RESTAURAR = "Usuario restaurado correctamente";
	public static final String MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE = "Usuario/s modificado/s correctamente";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/candidatos";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/candidatos/";
	public static final String RUTA_BEP_CONF_USU = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/usuarios/";
	public static final String JSP_INDEX = RUTA_BEP_CONF + "candidatos.jsp";	
	public static final String JSP_FORM_CANDIDATO = RUTA_BEP_CONF + "formCandidatos.jsp";
	public static final String JSP_BUSCAR_USUARIO = RUTA_BEP_CONF_USU + "buscarUsuario.jsp";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaCandidatos bean = new VistaCandidatos();		
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ACCION));;
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}
		
		try {
			init(bean, datos, request, response);
			switch (nombreAccion) {
				case ACCION_AREAS_EXCLUIDAS_CANDIDATO:
				case ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO:
				case ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO:
				case ACCION_DATATABLE_SOLICITUDES:
				case ACCION_EDITAR_CANDIDATO:
				case ACCION_ELIMINAR_CANDIDATO:
				case ACCION_EXCLUIR_USUARIO_AREA:
				case ACCION_INCLUIR_USUARIO_AREA:
				case ACCION_RECUPERAR_CANDIDATO:
				case ACCION_SELECCIONAR_CANDIDATO:
				case ACCION_SELECCIONAR_SOLICITUD:
				case ACCION_SOLICITUDES_CANDIDATO:
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
	
	private void init(VistaCandidatos bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
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
			
			BolsaEmpleoUtils.addMensajeDeError(e.getMessage(), bean, request);
			response.sendRedirect("/srv/es/informacionadministrativa/bolsaempleo/error");
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
			case ACCION_AREAS_EXCLUIDAS_CANDIDATO:
				bean.setApartadoAreasExcluidas(true);
				break;
			case ACCION_DATATABLE_SOLICITUDES:
				listadoSolicitudes(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO:
				listadoAreasExcluidasCandidato(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO:
				listadoAreasNoExcluidasCandidato(bean, datos, request, response);
				break;
			case ACCION_EDITAR_CANDIDATO:
				editarCandidato(bean, request, response);
				break;
			case ACCION_ELIMINAR_CANDIDATO:
				eliminarCandidato(bean, request, response);
				break;
			case ACCION_EXCLUIR_USUARIO_AREA:
				excluirUsuarioArea(bean, request, response, true);
				break;
			case ACCION_INCLUIR_USUARIO_AREA:
				excluirUsuarioArea(bean, request, response, false);
				break;
			case ACCION_SELECCIONAR_CANDIDATO:
				break;
			case ACCION_RECUPERAR_CANDIDATO:
				recuperarCantidato(bean, request, response);
				break;
			case ACCION_SELECCIONAR_SOLICITUD:
				resumenSolicitudCandidato(bean, request, response);
				break;
			case ACCION_SOLICITUDES_CANDIDATO:
				bean.setApartadoSolicitudes(true);
				break;
			default:
				errorFatal(bean, MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		}
	}
	
	/** edita un candidato .
	 * @param request .
	 * @param response .
	 * @param bean bean de la vista a la que poner los valores .
	 * @throws SQLException excepcion de bbdd .
	 * @throws UVException en caso de error en bd .
	 * @throws IOException en caso error de input u output .
	 */
	private void editarCandidato(VistaCandidatos bean, HttpServletRequest request, HttpServletResponse response) 
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
			
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuario(usuarioForm, bean.getUsuarioLogeado());
			BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_EDITAR, bean, request);
			response.sendRedirect(request.getServletPath());
		}
	}
	
	private void excluirUsuarioArea(VistaCandidatos bean, HttpServletRequest request, HttpServletResponse response, boolean excluir)
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
		
		redireccionConCandidatoSeleccionado(bean, request, response, ACCION_AREAS_EXCLUIDAS_CANDIDATO);
	}
	
	private void redireccionConCandidatoSeleccionado(VistaCandidatos bean, HttpServletRequest request, HttpServletResponse response, String accion)
			throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put(PARAM_ACCION, accion);
		params.put(PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		BolsaEmpleoUtils.redirectWithParams(request, response, params);
	}
	
	private void eliminarCandidato(VistaCandidatos bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		List<UsuarioBolsaEmpleo> users = new ArrayList<>();
		users.add(bean.getCandidato());
		
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoBorrado(users, bean.getUsuarioLogeado());	
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_ELIMINAR, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void recuperarCantidato(VistaCandidatos bean, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		List<UsuarioBolsaEmpleo> users = new ArrayList<>();
		users.add(bean.getCandidato());
		
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoNoBorrado(users, bean.getUsuarioLogeado());
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_RESTAURAR, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void resumenSolicitudCandidato(VistaCandidatos bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		
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
				BolsaEmpleoDataTable<Bolsa> dataTable = ModeloUsuarioBolsaEmpleo.obtenerInstancia().
						listaAreasExcluidasPorUsuarioDatatable(request.getParameterMap(), bean.getCandidato().getCodNum());
				bean.setDatatableAreas(dataTable);
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
	
}
