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
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBaremacion;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los items de baremación.
 */
@WebServlet(name = "informacionadministrativa.bolsaempleo.configuracion.itemsbaremacion", description = "Gestión de los items de baremación", urlPatterns = {
		"/srv/es/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion",
		"/srv/en/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion",
		"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion",
		"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion" })
public class ControladorItemsBaremacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorItemsBaremacion.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	// acciones
	public static final String ACCION_ACTIVAR_APARTADO = "activarapartado";
	public static final String ACCION_ACTIVAR_BLOQUE = "activarabloque";
	public static final String ACCION_ACTIVAR_ITEM = "activaraitem";
	public static final String ACCION_AGREGAR_APARTADO = "agregarapartado";
	public static final String ACCION_AGREGAR_BLOQUE = "agregarbloque";
	public static final String ACCION_AGREGAR_ITEM = "agregaritem";
	public static final String ACCION_EDITAR_APARTADO = "editarapartado";
	public static final String ACCION_EDITAR_BLOQUE = "editarbloque";
	public static final String ACCION_EDITAR_ITEM = "editaritem";
	public static final String ACCION_APARTADO_SELECCIONADO = "apartadoseleccionado";
	public static final String ACCION_BLOQUE_SELECCIONADO = "bloqueseleccionado";
	public static final String ACCION_ITEM_SELECCIONADO = "itemseleccionado";
	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_DATATABLE_APARTADOS = "datatable_apartados";
	public static final String ACCION_DATATABLE_BLOQUES = "datatable_bloques";
	public static final String ACCION_DATATABLE_ITEMS = "datatable_items";
	public static final String ACCION_DESACTIVAR_APARTADO = "desactivarapartado";
	public static final String ACCION_DESACTIVAR_BLOQUE = "desactivarabloque";
	public static final String ACCION_DESACTIVAR_ITEM = "desactivaraitem";

	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_APARTADO = "apartado";
	public static final String PARAM_APARTADO_NOMBRE = "nombreapartado";
	public static final String PARAM_APARTADO_CODIGO = "codigoapartado";
	public static final String PARAM_APARTADO_PUNTUACIONMAXIMA = "puntuacionMaxima";
	public static final String PARAM_APARTADO_PORCENTAJEMAXIMO = "porcentajeMaximo";
	public static final String PARAM_BLOQUE = "bloque";
	public static final String PARAM_BLOQUE_NOMBRE = "nombrebloque";
	public static final String PARAM_BLOQUE_CODIGO = "codigobloque";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ITEM = "item";
	public static final String PARAM_ITEM_NOMBRE = "nombreitem";
	public static final String PARAM_ITEM_CODIGO = "codigoitem";

	// mensajes
	public static final String MENSAJE_ENVIADO = "mensaje";
	public static final String MENSAJE_EXITO_ACTIVAR_APARTADO = "apartado activado correctamente";
	public static final String MENSAJE_EXITO_ACTIVAR_BLOQUE = "bloque activado correctamente";
	public static final String MENSAJE_EXITO_ACTIVAR_ITEM = "ítem activado correctamente";
	public static final String MENSAJE_EXITO_AGREGAR_APARTADO = "apartado agregado correctamente";
	public static final String MENSAJE_EXITO_AGREGAR_BLOQUE = "bloque agregado correctamente";
	public static final String MENSAJE_EXITO_AGREGAR_ITEM = "ítem agregado correctamente";
	public static final String MENSAJE_EXITO_DESACTIVAR_APARTADO = "apartado desactivado correctamente";
	public static final String MENSAJE_EXITO_DESACTIVAR_BLOQUE = "bloque desactivado correctamente";
	public static final String MENSAJE_EXITO_DESACTIVAR_ITEM = "ítem desactivado correctamente";
	public static final String MENSAJE_EXITO_EDITAR_APARTADO = "apartado editado correctamente";
	public static final String MENSAJE_EXITO_EDITAR_BLOQUE = "bloque editado correctamente";
	public static final String MENSAJE_EXITO_EDITAR_ITEM = "ítem editado correctamente";

	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion";

	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/itemsbaremacion/";

	public static final int RESPONSE_HTTP_CODE_ERROR = 400;

	// variables
	public static boolean anonimo = true;

	/**
	 * Peticion GET.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");

		VistaItemsBaremacion bean = new VistaItemsBaremacion();
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());

		ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();

		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR;
		}

		try {
			anonimo = !modelo.checkUser(datos);
			switch (nombreAccion) {
			case ACCION_ACTIVAR_APARTADO:
				actualizaActivoApartado(bean, request, response, true);
				break;
			case ACCION_ACTIVAR_BLOQUE:
				actualizaActivoBloque(bean, request, response, true);
				break;
			case ACCION_ACTIVAR_ITEM:
				actualizaActivoItem(bean, request, response, true);
				break;
			case ACCION_LISTAR:
			case ACCION_APARTADO_SELECCIONADO:
			case ACCION_BLOQUE_SELECCIONADO:
			case ACCION_ITEM_SELECCIONADO:
				listarItemsBaremacion(bean, request, response, nombreAccion);
				break;
			case ACCION_AGREGAR_APARTADO:
				agregarApartado(bean, request, response);
				break;
			case ACCION_AGREGAR_BLOQUE:
				agregarBloque(bean, request, response);
				break;
			case ACCION_AGREGAR_ITEM:
				agregarItem(bean, request, response);
				break;
			case ACCION_DATATABLE_APARTADOS:
				listadoApartados(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_BLOQUES:
				listadoBloques(bean, datos, request, response);
				break;
			case ACCION_DATATABLE_ITEMS:
				listadoItems(bean, datos, request, response);
				break;
			case ACCION_DESACTIVAR_APARTADO:
				actualizaActivoApartado(bean, request, response, false);
				break;
			case ACCION_DESACTIVAR_BLOQUE:
				actualizaActivoBloque(bean, request, response, false);
				break;
			case ACCION_DESACTIVAR_ITEM:
				actualizaActivoItem(bean, request, response, false);
				break;
			case ACCION_EDITAR_APARTADO:
				editarApartado(bean, request, response);
				break;
			case ACCION_EDITAR_BLOQUE:
				editarBloque(bean, request, response);
				break;
			case ACCION_EDITAR_ITEM:
				editarItem(bean, request, response);
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

	/**
	 * Redireccion de do post.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * asda.
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @param accion   .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	private void listarItemsBaremacion(VistaItemsBaremacion bean, HttpServletRequest request,
			HttpServletResponse response, String accion) throws SQLException, UVException {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");

		switch (accion) {
		case ACCION_LISTAR:
			HttpSession session = request.getSession(false);
			String mensaje = (String) session.getAttribute(MENSAJE_ENVIADO);

			if (mensaje == null) {
				mensaje = ACCION_LISTAR;
			}

			switch (mensaje) {
			case MENSAJE_EXITO_AGREGAR_BLOQUE:
			case MENSAJE_EXITO_EDITAR_APARTADO:
			case MENSAJE_EXITO_ACTIVAR_APARTADO:
			case MENSAJE_EXITO_DESACTIVAR_APARTADO:
				seleccionarApartado(bean, request, (Integer) session.getAttribute(PARAM_APARTADO));
				session.removeAttribute(PARAM_APARTADO);
				break;
			case MENSAJE_EXITO_AGREGAR_ITEM:
			case MENSAJE_EXITO_EDITAR_BLOQUE:
			case MENSAJE_EXITO_ACTIVAR_BLOQUE:
			case MENSAJE_EXITO_DESACTIVAR_BLOQUE:
				seleccionarBloque(bean, request, (Integer) session.getAttribute(PARAM_BLOQUE));
				session.removeAttribute(PARAM_BLOQUE);
				break;
			case MENSAJE_EXITO_EDITAR_ITEM:
			case MENSAJE_EXITO_ACTIVAR_ITEM:
			case MENSAJE_EXITO_DESACTIVAR_ITEM:
				seleccionarItem(bean, request, (Integer) session.getAttribute(PARAM_ITEM));
				session.removeAttribute(PARAM_ITEM);
				break;
			default:
				break;
			}

			break;
		case ACCION_APARTADO_SELECCIONADO:
			seleccionarApartado(bean, request, Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO)));
			break;
		case ACCION_BLOQUE_SELECCIONADO:
			seleccionarBloque(bean, request, Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
			break;
		case ACCION_ITEM_SELECCIONADO:
			seleccionarItem(bean, request, Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
			break;
		}

	}

	/**
	 * desactiva o activa un apartado .
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @param activo   .
	 * @throws SQLException .
	 * @throws UVException  .
	 * @throws IOException  .
	 */
	private void actualizaActivoApartado(VistaItemsBaremacion bean, HttpServletRequest request,
			HttpServletResponse response, boolean activo) throws SQLException, UVException, IOException {

		ModeloBaremacion modelo = new ModeloBaremacion();
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO)));
		modelo.activaDesactivaApartado(apartado);

		bean.getMensajesDeExito().add(activo ? MENSAJE_EXITO_ACTIVAR_APARTADO : MENSAJE_EXITO_DESACTIVAR_APARTADO);
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");

//	HttpSession session = request.getSession(false);
//	session.setAttribute(MENSAJE_ENVIADO,
//		activo ? MENSAJE_EXITO_ACTIVAR_APARTADO : MENSAJE_EXITO_DESACTIVAR_APARTADO);
//	session.setAttribute(PARAM_APARTADO, codNum);
//	response.sendRedirect(request.getServletPath());
	}

	/**
	 * edita un apartado.
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException  en caso de error de parametros .
	 * @throws IOException  en caso de error de input u output .
	 */
	private void editarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {			
		ModeloBaremacion modelo = new ModeloBaremacion();
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO)));
		
		bean.setVista(RUTA_BEP_CONF + "formApartadoBaremacion.jsp");
		bean.setApartadoBaremacion(apartado);
		
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_NOMBRE)) != null) {
			String codigo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_CODIGO));
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_NOMBRE));
			Float puntuacionMaxima = Formateador.leeParametroFloat(request.getParameter(PARAM_APARTADO_PUNTUACIONMAXIMA));
			Float porcentajeMaximo = Formateador.leeParametroFloat(request.getParameter(PARAM_APARTADO_PORCENTAJEMAXIMO));

			apartado.setCodigo(codigo);
			apartado.setNombre(nombre);
			apartado.setPuntuacionMaxima(puntuacionMaxima);
			apartado.setPorcentajeMaximo(porcentajeMaximo);

			modelo.actualizaApartado(apartado);

			bean.getMensajesDeExito().add(MENSAJE_EXITO_EDITAR_APARTADO);
			bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");

//	    HttpSession session = request.getSession(false);
//	    session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR_APARTADO);
//	    session.setAttribute(PARAM_APARTADO, codNum);
//	    response.sendRedirect(request.getServletPath());
		}
	}

	/**
	 * desactiva o activa un bloque .
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @param activo   .
	 * @throws SQLException .
	 * @throws UVException  .
	 * @throws IOException  .
	 */
	private void actualizaActivoBloque(VistaItemsBaremacion bean, HttpServletRequest request,
			HttpServletResponse response, boolean activo) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = new ModeloBaremacion();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = new BloqueBaremacion(codNum, activo);
		modelo.actualizaBloque(bloque, ModeloBaremacion.OPCION_6);
		bean.getMensajesDeExito().add(activo ? MENSAJE_EXITO_ACTIVAR_BLOQUE : MENSAJE_EXITO_DESACTIVAR_BLOQUE);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, activo ? MENSAJE_EXITO_ACTIVAR_BLOQUE : MENSAJE_EXITO_DESACTIVAR_BLOQUE);
		session.setAttribute(PARAM_BLOQUE, codNum);
		response.sendRedirect(request.getServletPath());
	}

	/**
	 * desactiva o activa un item .
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @param activo   .
	 * @throws SQLException .
	 * @throws UVException  .
	 * @throws IOException  .
	 */
	private void actualizaActivoItem(VistaItemsBaremacion bean, HttpServletRequest request,
			HttpServletResponse response, boolean activo) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = new ModeloBaremacion();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM));
		ItemBaremacion item = new ItemBaremacion(codNum, activo);
		modelo.actualizaItem(item, ModeloBaremacion.OPCION_6);
		bean.getMensajesDeExito().add(activo ? MENSAJE_EXITO_ACTIVAR_ITEM : MENSAJE_EXITO_DESACTIVAR_ITEM);
		HttpSession session = request.getSession(false);
		session.setAttribute(MENSAJE_ENVIADO, activo ? MENSAJE_EXITO_ACTIVAR_ITEM : MENSAJE_EXITO_DESACTIVAR_ITEM);
		session.setAttribute(PARAM_ITEM, codNum);
		response.sendRedirect(request.getServletPath());
	}

	/**
	 * edita un bloque.
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException  en caso de error de parametros .
	 * @throws IOException  en caso de error de input u output .
	 */
	private void editarBloque(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formBloqueBaremacion.jsp");
		ModeloBaremacion modelo = new ModeloBaremacion();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);
		bean.setBloqueBaremacion(bloque);
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_NOMBRE)) != null) {
			String codigo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_CODIGO));
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_NOMBRE));
			BloqueBaremacion bloqueActualizado = new BloqueBaremacion(codNum, bloque.getApartadoBaremacion(), codigo,
					nombre);
			modelo.actualizaBloque(bloqueActualizado, ModeloBaremacion.OPCION_5);
			bean.getMensajesDeExito().add(MENSAJE_EXITO_EDITAR_BLOQUE);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR_BLOQUE);
			session.setAttribute(PARAM_BLOQUE, codNum);
			response.sendRedirect(request.getServletPath());
		}
	}

	/**
	 * edita un ítem.
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException  en caso de error de parametros .
	 * @throws IOException  en caso de error de input u output .
	 */
	private void editarItem(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formItemBaremacion.jsp");
		ModeloBaremacion modelo = new ModeloBaremacion();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM));
		ItemBaremacion item = modelo.getItemBaremacionById(codNum);
		bean.setItemBaremacion(item);
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_NOMBRE)) != null) {
			String codigo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_CODIGO));
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_NOMBRE));
			ItemBaremacion itemActualizado = new ItemBaremacion(codNum, item.getBloqueBaremacion(), codigo, nombre);
			modelo.actualizaItem(itemActualizado, ModeloBaremacion.OPCION_5);
			bean.getMensajesDeExito().add(MENSAJE_EXITO_EDITAR_ITEM);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_EDITAR_ITEM);
			session.setAttribute(PARAM_ITEM, codNum);
			response.sendRedirect(request.getServletPath());
		}
	}

	/**
	 * agrega un nuevo apartado .
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @throws SQLException .
	 * @throws UVException  .
	 * @throws IOException  .
	 */
	private void agregarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formApartadoBaremacion.jsp");
		ModeloBaremacion modelo = new ModeloBaremacion();
		ApartadoBaremacion apartado = modelo.getUltimoCodigoApartado();
		bean.setApartadoBaremacion(apartado);
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_NOMBRE)) != null) {
			String codigo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_CODIGO));
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_NOMBRE));
			ApartadoBaremacion apartadoNuevo = new ApartadoBaremacion(codigo, nombre);
			modelo.insertaApartado(apartadoNuevo);
			bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR_APARTADO);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR_APARTADO);
			response.sendRedirect(request.getServletPath());
		}
	}

	/**
	 * agrega un nuevo bloque .
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @throws SQLException .
	 * @throws UVException  .
	 * @throws IOException  .
	 */
	private void agregarBloque(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formBloqueBaremacion.jsp");
		ModeloBaremacion modelo = new ModeloBaremacion();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		BloqueBaremacion bloque = modelo.getUltimoCodigoBloque(codNum);
		bean.setBloqueBaremacion(bloque);
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_NOMBRE)) != null) {
			String codigo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_CODIGO));
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_NOMBRE));
			BloqueBaremacion bloqueNuevo = new BloqueBaremacion(bloque.getApartadoBaremacion(), codigo, nombre);
			modelo.insertaBloque(bloqueNuevo);
			bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR_BLOQUE);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR_BLOQUE);
			session.setAttribute(PARAM_APARTADO, codNum);
			response.sendRedirect(request.getServletPath());
		}
	}

	/**
	 * agrega un nuevo ítem .
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param response .
	 * @throws SQLException .
	 * @throws UVException  .
	 * @throws IOException  .
	 */
	private void agregarItem(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formItemBaremacion.jsp");
		ModeloBaremacion modelo = new ModeloBaremacion();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		ItemBaremacion item = modelo.getUltimoCodigoItem(codNum);
		bean.setItemBaremacion(item);
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_NOMBRE)) != null) {
			String codigo = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_CODIGO));
			String nombre = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_NOMBRE));
			ItemBaremacion itemNuevo = new ItemBaremacion(item.getBloqueBaremacion(), codigo, nombre);
			modelo.insertaItem(itemNuevo);
			bean.getMensajesDeExito().add(MENSAJE_EXITO_AGREGAR_ITEM);
			HttpSession session = request.getSession(false);
			session.setAttribute(MENSAJE_ENVIADO, MENSAJE_EXITO_AGREGAR_ITEM);
			session.setAttribute(PARAM_BLOQUE, codNum);
			response.sendRedirect(request.getServletPath());
		}
	}

	/**
	 * Devuelve un apartado con id dado a la vista .
	 * 
	 * @param bean       .
	 * @param request    .
	 * @param idApartado .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void seleccionarApartado(VistaItemsBaremacion bean, HttpServletRequest request, Integer idApartado)
			throws SQLException, UVException {
		ApartadoBaremacion apartado = new ModeloBaremacion().getApartadoBaremacionById(idApartado);
		bean.setApartadoBaremacion(apartado);
	}

	/**
	 * Devuelve un bloque con id dado a la vista .
	 * 
	 * @param bean     .
	 * @param request  .
	 * @param idBloque .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	private void seleccionarBloque(VistaItemsBaremacion bean, HttpServletRequest request, Integer idBloque)
			throws SQLException, UVException {
		BloqueBaremacion bloque = new ModeloBaremacion().getBloqueBaremacionById(idBloque);
		bean.setBloqueBaremacion(bloque);
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
	}

	/**
	 * Devuelve un ítem con id dado a la vista .
	 * 
	 * @param bean    .
	 * @param request .
	 * @param idItem  .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	private void seleccionarItem(VistaItemsBaremacion bean, HttpServletRequest request, Integer idItem)
			throws SQLException, UVException {
		ItemBaremacion item = new ModeloBaremacion().getItemBaremacionById(idItem);
		bean.setItemBaremacion(item);
		bean.setBloqueBaremacion(item.getBloqueBaremacion());
		bean.setApartadoBaremacion(item.getBloqueBaremacion().getApartadoBaremacion());
	}

	/**
	 * Listado de apartados de items baremación.
	 * 
	 * @param bean     .
	 * @param datos    .
	 * @param request  .
	 * @param response .
	 * @throws IOException  .
	 * @throws SQLException .
	 */
	private void listadoApartados(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		ModeloBaremacion modelo = new ModeloBaremacion();

		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<ApartadoBaremacion> dataTable = modelo.listadoApartadosGeneralesBaremacionDatatable(request.getParameterMap());
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

	/**
	 * Listado de bloques.
	 * 
	 * @param bean     .
	 * @param datos    .
	 * @param request  .
	 * @param response .
	 * @throws IOException  .
	 * @throws SQLException .
	 */
	private void listadoBloques(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		ModeloBaremacion modelo = new ModeloBaremacion();

		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer apartado = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
				BolsaEmpleoDataTable<BloqueBaremacion> dataTable = modelo
						.listadoBloquesBaremacionDatatable(request.getParameterMap(), apartado);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY)
						.create();
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

	/**
	 * Listado de items.
	 * 
	 * @param bean     .
	 * @param datos    .
	 * @param request  .
	 * @param response .
	 * @throws IOException  .
	 * @throws SQLException .
	 */
	private void listadoItems(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		ModeloBaremacion modelo = new ModeloBaremacion();

		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter writer = response.getWriter()) {
			try {
				Integer bloque = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
				BolsaEmpleoDataTable<ItemBaremacion> dataTable = modelo
						.listadoItemsBaremacionDatatable(request.getParameterMap(), bloque);
				Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY)
						.create();
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
}
