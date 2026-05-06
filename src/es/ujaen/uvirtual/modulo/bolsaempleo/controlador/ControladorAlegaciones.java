package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionGeneral;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDepartamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAlegaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Controlador de alegaciones .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.alegaciones",
	description = "Alegaciones",
	urlPatterns = {
			"/srv/es/informacionadministrativa/bolsaempleo/alegaciones",
			"/srv/en/informacionadministrativa/bolsaempleo/alegaciones",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/alegaciones",
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/alegaciones"
	})
public class ControladorAlegaciones extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorAlegaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static final int LIMITE_CLOB_ORACLE = 1000000;

	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_ALEGACIONES_ID = "alegacionesid";
	public static final String PARAM_CONVOCATORIA = "convocatoria";
	public static final String PARAM_SOLICITUD = "solicitud";
	public static final String PARAM_CANDIDATO = "candidato";
	public static final String PARAM_BOLSA = "bolsa";
	public static final String PARAM_GUARDAR_CAMBIOS_RESOLUCION_DEPARTAMENTO = "guardarcambiosresoluciondepartamento";
	public static final String PARAM_GUARDAR_CAMBIOS_RESOLUCION_FINAL = "guardarcambiosresolucionfinal";
	public static final String PARAM_DESCRIPCION_RESOLUCION_FINAL= "descripcionresolucionfinal";
	public static final String PARAM_DESCRIPCION_RESOLUCION_DEPARTAMENTO = "descripcionresoluciondepartamento";

	// acciones
	public static final String ACCION_INDEX = "listar";
	public static final String ACCION_VER_DETALLE_ALEGACION = "verdetallealegacion";
	public static final String ACCION_DATATABLE_ALEGACIONES = "datatablealegaciones";
	public static final String ACCION_CONFIRMAR_RESOLUCION_FINAL = "confirmarresolucionfinal";
	public static final String ACCION_CONFIRMAR_RESOLUCION_DEPARTAMENTO = "confirmarresoluciondepartamento";
	public static final String ACCION_MARCAR_EN_RESOLUCION = "marcarenresolucion";
	public static final String ACCION_ENVIAR_DEPARTAMENTO = "enviardepartamento";
	public static final String ACCION_AGREGAR_DESCRIPCION_RESOLUCION_FINAL = "agregardescripcionresolucionfinal";
	public static final String ACCION_AGREGAR_DESCRIPCION_DEPARTAMENTO = "agregardescripciondepartamento";
	public static final String ACCION_ENVIAR_RESOLUCION_FINAL = "enviarresolucionfinal";
	public static final String ACCION_ENVIAR_RESOLUCION_DEPARTAMENTO = "enviarresoluciondepartamento";
	public static final String ACCION_REABRIR_ALEGACION = "reabriralegacion";

	// mensajes
	public static final String MENSAJE_ERROR_ACCION_NO_CONTEMPLADA = "Acción no contemplada";
	public static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso";

	// ruta vistas
	public static final String RUTA_BEP_ALEGACIONES = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/alegaciones/";
	public static final String JSP_INDEX = RUTA_BEP_ALEGACIONES + "index.jsp";
	public static final String JSP_DETALLE_ALEGACION = RUTA_BEP_ALEGACIONES + "detallealegacion.jsp";

	// ajax
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/alegaciones";
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

		VistaAlegaciones bean = new VistaAlegaciones();
		VistaMisResultados beanResultados = new VistaMisResultados();
		Usuario usuario = datos.getUsuario();

		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());

		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_INDEX;
		}

		try {
			if (!init(bean, datos, request, response)) {
				return;
			}
			switch (nombreAccion) {
				case ACCION_INDEX:
					bean.setVista(JSP_INDEX);
					bean.setListaConvocatorias(ModeloConvocatoria.obtenerInstancia().listaConvocatorias());
					break;
				case ACCION_DATATABLE_ALEGACIONES:
					datatableAlegaciones(bean, datos, request, response);
					break;
				case ACCION_AGREGAR_DESCRIPCION_RESOLUCION_FINAL:
					agregarDescripcionResolucionFinal(bean, beanResultados, datos, request, response);
					break;
				case ACCION_AGREGAR_DESCRIPCION_DEPARTAMENTO:
					agregarDescripcionDepartamento(bean, beanResultados, datos, request, response);
					break;
				case ACCION_VER_DETALLE_ALEGACION:
	                verDetalleAlegacion(bean, beanResultados, datos, request, response);
	                break;
				case ACCION_MARCAR_EN_RESOLUCION:
					marcarEnResolucion(bean, beanResultados, datos, request, response);
					break;
				case ACCION_ENVIAR_RESOLUCION_FINAL:
					enviarResolucionFinal(bean, beanResultados, datos, request, response);
					break;
				case ACCION_ENVIAR_RESOLUCION_DEPARTAMENTO:
					enviarResolucionDepartamento(bean, beanResultados, datos, request, response);
					break;
				case ACCION_ENVIAR_DEPARTAMENTO:
					enviarAlDepartamento(bean, beanResultados, datos, request, response);
					break;			case ACCION_REABRIR_ALEGACION:
				reabrirAlegacionBolsaCandidato(bean, datos, request, response);
				break;				default:
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
			//if (ACCION_VER_DETALLE_ALEGACION.equals(nombreAccion)) {
				datos.getVistas().put(beanResultados.getClass().getName(), beanResultados);
			//}
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

	private boolean init(VistaAlegaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_INDEX);
		BolsaEmpleoUtils.readMensajeSession(bean, request);
		bean.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria());


		try {
			bean.setUsuarioLogeado(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getAndRefreshUsuario(datos));
			bean.setCursos(ModeloPlazaOfertada.obtenerInstancia().listadoCursosPlazasOfertadas());

			// personal, direccion
			int[] rolesValidos = {ModeloRol.ID_ROL_SERVICIO_PERSONAL, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO};
			boolean contains = IntStream.of(rolesValidos).
					anyMatch(x -> x == bean.getUsuarioLogeado().getRol().getCodNum());

			if (!contains) {
				throw new UVException(MENSAJE_ERROR_SIN_PERMISO);
			}
		} catch (UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());

			BolsaEmpleoUtils.redirectToError(bean, datos, request, response, e.getMessage());
			return false;
		}

		return true;
	}

	private void errorFatal(VistaAlegaciones bean, String mensaje) {
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

	private void datatableAlegaciones(VistaAlegaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
	    ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia(); // Obtenemos la instancia del modelo de alegaciones
	    List<Convocatoria> listaConvocatorias = ModeloConvocatoria.obtenerInstancia().listaConvocatorias();
	    bean.setListaConvocatorias(listaConvocatorias);
	    datos.setRespuestaEnviada(true);
	    datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
	    response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
	    response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);

	    try (PrintWriter writer = response.getWriter()) {
	    	BolsaEmpleoDataTable<Alegacion> dataTable = modelo.listaAlegacionesEmpleoDatatable(request.getParameterMap(), bean.getUsuarioLogeado(), bean.getConvocatoria());
            bean.setDatatableBolsas(dataTable);
            writer.write(dataTable.toJson());
		}
	}

	private void enviarResolucionDepartamento(VistaAlegaciones bean, VistaMisResultados beanResultados, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException, IOException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) {
			throw new UVException("No tienes permiso");
		}
		Integer codNumAlegacion = Formateador
				.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		ModeloAlegaciones modeloMisAlegaciones = ModeloAlegaciones.obtenerInstancia();
		boolean exito = modeloMisAlegaciones.actualizarEstadoAlegacion(codNumAlegacion, ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION, bean.getUsuarioLogeado().getCodCuenta());
		modeloMisAlegaciones.actualizarFechaResolucionDepartamento(codNumAlegacion, bean.getUsuarioLogeado().getCodCuenta());
		 if (exito) {
	        BolsaEmpleoUtils.addMensajeDeExito("La alegación ha sido informada por el departamento correctamente.", bean, request);
	    } else {
	        throw new UVException("No se pudo marcar la alegación como informada.");
	    }
		 String nuevaUrl = request.getServletPath()
		            + "?" + ControladorAlegaciones.PARAM_ACCION + "=" + ControladorAlegaciones.ACCION_VER_DETALLE_ALEGACION
		            + "&" + ControladorAlegaciones.PARAM_ALEGACIONES_ID + "=" + codNumAlegacion;

		 response.sendRedirect(nuevaUrl);
	}

	private void enviarResolucionFinal(VistaAlegaciones bean, VistaMisResultados beanResultados, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException, IOException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
			throw new UVException("No tienes permiso de personal");
		}
		Integer codNumAlegacion = Formateador
				.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		ModeloAlegaciones modeloMisAlegaciones = ModeloAlegaciones.obtenerInstancia();
		boolean exito = modeloMisAlegaciones.actualizarEstadoAlegacion(codNumAlegacion, ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION, bean.getUsuarioLogeado().getCodCuenta());
		modeloMisAlegaciones.actualizarFechaResolucionfinal(codNumAlegacion, bean.getUsuarioLogeado().getCodCuenta());
		 if (exito) {
	        BolsaEmpleoUtils.addMensajeDeExito("La alegación ha sido resuelta correctamente.", bean, request);
	    } else {
	        throw new UVException("No se pudo marcar la alegación como resuelta.");
	    }
		Alegacion alegacion = modeloMisAlegaciones.getAlegacionByCodNum(codNumAlegacion);
		// Crear notificación en mensajaría para el candidato de la alegación
		 modeloMisAlegaciones.notificarCandidatoResolucionAlegacion(bean.getUsuarioLogeado(),alegacion);

		 String nuevaUrl = request.getServletPath()
		            + "?" + ControladorAlegaciones.PARAM_ACCION + "=" + ControladorAlegaciones.ACCION_VER_DETALLE_ALEGACION
		            + "&" + ControladorAlegaciones.PARAM_ALEGACIONES_ID + "=" + codNumAlegacion;

		 response.sendRedirect(nuevaUrl);
	}

	private void enviarAlDepartamento(VistaAlegaciones bean, VistaMisResultados beanResultados, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException, IOException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
			throw new UVException("No tienes permiso de personal");
		}
		Integer codNumAlegacion = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		ModeloAlegaciones modeloMisAlegaciones = ModeloAlegaciones.obtenerInstancia();		
		Alegacion alegacion = modeloMisAlegaciones.getAlegacionByCodNum(codNumAlegacion);
		if (!ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION.equals(alegacion.getEstado()) &&
			!ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION.equals(alegacion.getEstado())) {
			throw new UVException("Solo se pueden enviar al departamento alegaciones en presentacion o resolucion");
		}		
		
		modeloMisAlegaciones.actualizarFechaEnvioDepartamento(codNumAlegacion, bean.getUsuarioLogeado().getCodCuenta());
		boolean exito = modeloMisAlegaciones.actualizarEstadoAlegacion(codNumAlegacion, ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION, bean.getUsuarioLogeado().getCodCuenta());
		 if (exito) {
	        BolsaEmpleoUtils.addMensajeDeExito("La alegación ha sido enviada al departamento correctamente.", bean, request);
	    } else {
	        throw new UVException("No se pudo enviar la alegacion al departamento.");
	    }
		 
		// Crear notificación en mensajaría para el candidato de la alegación
		modeloMisAlegaciones.notificarAlegacionEnviadaADepartamento(bean.getUsuarioLogeado(), alegacion);

		 String nuevaUrl = request.getServletPath()
		            + "?" + ControladorAlegaciones.PARAM_ACCION + "=" + ControladorAlegaciones.ACCION_VER_DETALLE_ALEGACION
		            + "&" + ControladorAlegaciones.PARAM_ALEGACIONES_ID + "=" + codNumAlegacion;

		 response.sendRedirect(nuevaUrl);
	}

	private void marcarEnResolucion(VistaAlegaciones bean, VistaMisResultados beanResultados, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws UVException, SQLException, IOException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
			throw new UVException("No tienes permiso de personal");
		}
		Integer codNumAlegacion = Formateador
				.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		ModeloAlegaciones modeloMisAlegaciones = ModeloAlegaciones.obtenerInstancia();
		boolean exito = modeloMisAlegaciones.actualizarEstadoAlegacion(codNumAlegacion, ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION, bean.getUsuarioLogeado().getCodCuenta());
		 if (exito) {
	        BolsaEmpleoUtils.addMensajeDeExito("La alegación ha sido marcada 'En resolución' correctamente.", bean, request);
	    } else {
	        throw new UVException("No se pudo marcar la alegación como 'En resolución'.");
	    }
		 String nuevaUrl = request.getServletPath()
		            + "?" + ControladorAlegaciones.PARAM_ACCION + "=" + ControladorAlegaciones.ACCION_VER_DETALLE_ALEGACION
		            + "&" + ControladorAlegaciones.PARAM_ALEGACIONES_ID + "=" + codNumAlegacion;

		 response.sendRedirect(nuevaUrl);
	}

	private void agregarDescripcionDepartamento(VistaAlegaciones bean, VistaMisResultados beanResultados, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) {
			throw new UVException("No tienes permiso");
		}
		ModeloAlegaciones modeloMisAlegaciones = ModeloAlegaciones.obtenerInstancia();
		String descripcionDepartamento = BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_DESCRIPCION_RESOLUCION_DEPARTAMENTO);
		Integer codNumAlegacion = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		// Validar que el director gestiona el area
		Alegacion alegacion = modeloMisAlegaciones.getAlegacionByCodNum(codNumAlegacion);
		Integer areaCodNum = alegacion.getArea().getCodNum();
		ModeloEvaluador modeloEval = ModeloEvaluador.obtenerInstancia();
		if (modeloEval.getEvaluadorById(bean.getUsuarioLogeado().getCodNum(), areaCodNum) == null) {
			throw new UVException("No tienes permiso para modificar alegaciones de esta area");
		}
	    if (descripcionDepartamento != null && descripcionDepartamento.length() > LIMITE_CLOB_ORACLE) {
	        // Enviar un error o mensaje adecuado al usuario
	    	response.sendError(HttpServletResponse.SC_BAD_REQUEST,
	                "La descripción no puede superar los " + LIMITE_CLOB_ORACLE + " caracteres.");
	        return;
	    }
	    modeloMisAlegaciones.insertarDescripcionResolucionDepartamento(codNumAlegacion, descripcionDepartamento, bean.getUsuarioLogeado().getCodCuenta());

        // Paso 6: Añadimos el mensaje de éxito a la interfaz
        BolsaEmpleoUtils.addMensajeDeExito("Resolución del departamento guardada correctamente", bean, request);
        datos.setRespuestaEnviada(true);
	    String nuevaUrl = request.getServletPath()
                + "?" + ControladorAlegaciones.PARAM_ACCION + "=" + ACCION_VER_DETALLE_ALEGACION
                + "&" + ControladorAlegaciones.PARAM_ALEGACIONES_ID + "=" + codNumAlegacion;

		// Redirigir a la nueva URL
		response.sendRedirect(nuevaUrl);
	}

	private void agregarDescripcionResolucionFinal(VistaAlegaciones bean, VistaMisResultados beanResultados, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, UVException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
			throw new UVException("No tienes permiso de personal");
		}
		ModeloAlegaciones modeloMisAlegaciones = ModeloAlegaciones.obtenerInstancia();
		String descripcionResolucionFinal = BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_DESCRIPCION_RESOLUCION_FINAL);
		Integer codNumAlegacion = Formateador
				.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
	    if (descripcionResolucionFinal != null && descripcionResolucionFinal.length() > LIMITE_CLOB_ORACLE) {
	        // Enviar un error o mensaje adecuado al usuario
	    	response.sendError(HttpServletResponse.SC_BAD_REQUEST,
	                "La descripción no puede superar los " + LIMITE_CLOB_ORACLE + " caracteres.");
	        return;
	    }
	    modeloMisAlegaciones.insertarDescripcionResolucionFinal(codNumAlegacion, descripcionResolucionFinal, bean.getUsuarioLogeado().getCodCuenta());

        // Paso 6: Añadimos el mensaje de éxito a la interfaz
        BolsaEmpleoUtils.addMensajeDeExito("Resolución final guardada correctamente", bean, request);
        datos.setRespuestaEnviada(true);
	    String nuevaUrl = request.getServletPath()
                + "?" + ControladorAlegaciones.PARAM_ACCION + "=" + ACCION_VER_DETALLE_ALEGACION
                + "&" + ControladorAlegaciones.PARAM_ALEGACIONES_ID + "=" + codNumAlegacion;

		// Redirigir a la nueva URL
		response.sendRedirect(nuevaUrl);
	}

	private void verDetalleAlegacion(VistaAlegaciones bean, VistaMisResultados beanResultados, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL) && !bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) {
			throw new UVException("No tienes permiso para visualizar.");
		}
		Integer codNumAlegacion = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));

		// Leemos la alegación
	    ModeloAlegaciones modeloMisAlegaciones = ModeloAlegaciones.obtenerInstancia();
	    Alegacion detalleAlegacion = modeloMisAlegaciones.getAlegacionByCodNum(codNumAlegacion);

	    // Si el usuario es director, verificar que la alegación esté en un área donde sea evaluador activo
	    if (bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) {
	    	ModeloEvaluador modeloEvaluador = ModeloEvaluador.obtenerInstancia();
	    	Evaluador evaluador = modeloEvaluador.getEvaluadorById(bean.getUsuarioLogeado().getCodNum(), detalleAlegacion.getArea().getCodNum());

	    	if (evaluador == null) {
	    		throw new UVException("No tienes permiso para ver esta alegación");
	    	}
	    }

	    bean.setAlegacion(detalleAlegacion);

	    // Leemos los resultado de la bolsa para el candidato
	    Area area = detalleAlegacion.getArea();
	    Bolsa bolsa = ModeloBolsa.obtenerInstancia().getBolsaByArea(area);
	    BolsaResultado bolsaResultado = ModeloResultados.obtenerInstancia().getBolsaResultado(bolsa, detalleAlegacion.getCandidato(), detalleAlegacion.getConvocatoria());
	    beanResultados.setBolsaResultado(bolsaResultado);
		beanResultados.setBolsa(bolsa);
		beanResultados.setConvocatoria(detalleAlegacion.getConvocatoria());
		beanResultados.setUsuarioLogeado(detalleAlegacion.getCandidato());

		modeloMisAlegaciones.establecerDescripcionesSiExistenAlegacionesEnLosMeritos(beanResultados, detalleAlegacion.getConvocatoria(), detalleAlegacion.getCandidato(), bolsa.getCodNum());
	    SolMerBolAlegacion solMerBolAlegacion = modeloMisAlegaciones.obtenerSolMerBolAlegacionExistente(
	    		beanResultados.getBolsa(),
	    		detalleAlegacion.getConvocatoria(),
	    		detalleAlegacion.getCandidato(),
	    		null
	    );
	    modeloMisAlegaciones.asignarArchivosAlegacion(beanResultados, solMerBolAlegacion);
	    beanResultados.setSolMerBolAlegacion(solMerBolAlegacion);

	    // Asigna la nueva vista JSP
	    bean.setVista(JSP_DETALLE_ALEGACION);
	    beanResultados.setVista(JSP_DETALLE_ALEGACION);
	}

	private void reabrirAlegacionBolsaCandidato(VistaAlegaciones bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		if (!bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
			throw new UVException("No tienes permiso para reabrir alegaciones");
		}
		Integer idCandidato = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_CANDIDATO));
		Integer idBolsa = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_BOLSA));
		Integer idConvocatoria = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_CONVOCATORIA));

		// Obtener objetos necesarios
		UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(idCandidato);
		Bolsa bolsaAReabrir = ModeloBolsa.obtenerInstancia().getBolsaById(idBolsa);
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(idConvocatoria);

		// Reabrir la alegación
		ModeloAlegaciones modelo = ModeloAlegaciones.obtenerInstancia();
		modelo.reabrirAlegacionBolsaCandidato(candidato, convocatoria, bolsaAReabrir, bean.getUsuarioLogeado().getCodCuenta());

		// Agregar mensaje de éxito
		String mensajeExito = String.format("Alegación reabierta para el área de: %s", bolsaAReabrir.getArea().getDescripcion());
		BolsaEmpleoUtils.addMensajeDeExito(mensajeExito, bean, request);

		// Redirigir a detalle de alegación
		Integer idAlegacion = Formateador.leeParametroInteger(BolsaEmpleoUtils.getParamRequestOrSession(request, PARAM_ALEGACIONES_ID));
		Alegacion alegacion = (idAlegacion != null) ? modelo.getAlegacionByCodNum(idAlegacion) : null;
		if (alegacion != null) {
			response.sendRedirect(request.getServletPath()
				+ "?" + PARAM_ACCION + "=" + ACCION_VER_DETALLE_ALEGACION
				+ "&" + PARAM_ALEGACIONES_ID + "=" + alegacion.getCodNum());
		} else {
			response.sendRedirect(request.getServletPath()
				+ "?" + PARAM_ACCION + "=" + ACCION_INDEX);
		}
	}
}
