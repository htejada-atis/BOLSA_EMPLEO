package es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfWriter;

import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaItemsBaremacion;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Gestión de los items de baremación.
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.configuracion.itemsbaremacion", 
	description = "Gestión de los items de baremación", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion", 
			"/srv/en/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion",
			"/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion", 
			"/srv/en/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion"
	})
public class ControladorItemsBaremacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorItemsBaremacion.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	// acciones apartados
	public static final String ACCION_DATATABLE_APARTADOS = "datatable_apartados";
	public static final String ACCION_AGREGAR_APARTADO = "agregarapartado";
	public static final String ACCION_AGREGAR_APARTADO_CONFIRM = "agregarapartadoconfirm";
	public static final String ACCION_EDITAR_APARTADO = "editarapartado";
	public static final String ACCION_EDITAR_APARTADO_CONFIRM = "editarapartadoconfirm";
	public static final String ACCION_DESACTIVAR_APARTADO = "desactivarapartado";
	public static final String ACCION_ACTIVAR_APARTADO = "activarapartado";
	public static final String ACCION_APARTADO_SELECCIONADO = "apartadoseleccionado";
	public static final String MENSAJE_EXITO_APARTADO_AGREGAR = "Bloque agregado correctamente";
	public static final String MENSAJE_EXITO_APARTADO_EDITAR = "Bloque editado correctamente";
	public static final String MENSAJE_EXITO_APARTADO_DESACTIVAR = "Bloque desactivado correctamente";
	public static final String MENSAJE_EXITO_APARTADO_ACTIVAR = "Bloque activado correctamente";
	public static final String MENSAJE_ERROR_AFINIDAD_VACIA = "Debe seleccionar un tipo de afinidad";
	
	// acciones bloques
	public static final String ACCION_DATATABLE_BLOQUES = "datatable_bloques";
	public static final String ACCION_AGREGAR_BLOQUE = "agregarbloque";
	public static final String ACCION_AGREGAR_BLOQUE_CONFIRM = "agregarbloqueconfirm";
	public static final String ACCION_EDITAR_BLOQUE = "editarbloque";
	public static final String ACCION_EDITAR_BLOQUE_CONFIRM = "editarbloqueconfirm";
	public static final String ACCION_DESACTIVAR_BLOQUE = "desactivarabloque";
	public static final String ACCION_ACTIVAR_BLOQUE = "activarabloque";	
	public static final String ACCION_BLOQUE_SELECCIONADO = "bloqueseleccionado";
	public static final String MENSAJE_EXITO_BLOQUE_AGREGAR = "Apartado agregado correctamente";
	public static final String MENSAJE_EXITO_BLOQUE_EDITAR = "Apartado editado correctamente";
	public static final String MENSAJE_EXITO_BLOQUE_DESACTIVAR = "Apartado desactivado correctamente";
	public static final String MENSAJE_EXITO_BLOQUE_ACTIVAR = "Apartado activado correctamente";
	public static final String MENSAJE_ERROR_BLOQUE_NUMMAXMERITOS_NUMERICO = "El número máximo de méritos debe ser un número";
	public static final String MENSAJE_ERROR_BLOQUE_NUMMAXMERITOS_MINIMO = "El número máximo de méritos debe ser al menos uno";
	
	// acciones items
	public static final String ACCION_DATATABLE_ITEMS = "datatable_items";
	public static final String ACCION_DATATABLE_ITEMS_EXCLUYENTES = "datatable_items_excluyentes";
	public static final String ACCION_AGREGAR_ITEM = "agregaritem";
	public static final String ACCION_AGREGAR_ITEM_CONFIRM = "agregaritemconfirm";
	public static final String ACCION_EDITAR_ITEM = "editaritem";
	public static final String ACCION_EDITAR_ITEM_CONFIRM = "editaritemconfirm";
	public static final String ACCION_DESACTIVAR_ITEM = "desactivaraitem";
	public static final String ACCION_ACTIVAR_ITEM = "activaraitem";	
	public static final String ACCION_ITEM_SELECCIONADO = "itemseleccionado";
	public static final String ACCION_ITEM_EXCLUYENTE_SELECCIONADO = "itemexcluyenteseleccionado";
	public static final String ACCION_ITEM_EXCLUYENTE_DESELECCIONADO = "itemexcluyentedeseleccionado";
	public static final String ACCION_DESCARGAR_FICHERO = "descargarfichero";
	public static final String MENSAJE_EXITO_ITEM_AGREGAR = "Item agregado correctamente";
	public static final String MENSAJE_EXITO_ITEM_EDITAR = "Item editado correctamente";
	public static final String MENSAJE_EXITO_ITEM_DESACTIVAR = "Item desactivado correctamente";
	public static final String MENSAJE_EXITO_ITEM_ACTIVAR = "Item activado correctamente";	
		
	public static final String ACCION_INDEX = "indice";	
	
	// parámetros
	public static final String PARAM_ACCION = "a";
	public static final String PARAM_APARTADO = "apartado";
	public static final String PARAM_APARTADO_NOMBRE = "nombreapartado";
	public static final String PARAM_APARTADO_CODIGO = "codigoapartado";
	public static final String PARAM_APARTADO_PUNTUACIONMAXIMA = "puntuaacionmaxima";
	public static final String PARAM_APARTADO_PORCENTAJEMAXIMO = "porcentajemaximo";
	public static final String PARAM_BLOQUE = "bloque";
	public static final String PARAM_BLOQUE_NOMBRE = "nombrebloque";
	public static final String PARAM_BLOQUE_CODIGO = "codigobloque";
	public static final String PARAM_BLOQUE_NUMEROMAXIMOMERITOS = "numeromaximomeritos";
	public static final String PARAM_ENVIAR = "enviar";
	public static final String PARAM_ITEM = "item";
	public static final String PARAM_ITEM_EXCLUYENTE = "itemexcluyente";
	public static final String PARAM_ITEM_NOMBRE = "nombreitem";
	public static final String PARAM_ITEM_DESCRIPCION = "descripcion";
	public static final String PARAM_ITEM_CODIGO = "codigoitem";
	public static final String PARAM_ITEM_UNIDADES = "unidades";
	public static final String PARAM_ITEM_VALOR = "valor";
	public static final String PARAM_ITEM_VALOR_MINIMO = "valorminimo";
	public static final String PARAM_ITEM_VALOR_MAXIMO = "valormaximo";
	public static final String PARAM_ITEM_AFINIDAD = "afinidad";
	public static final String PARAM_ITEM_INDIVIDUALIZADO = "individualizado";
	public static final String PARAM_ITEMS_SELECCIONADOS = "itemsseleccionados";
	
	//pdf
	public static final int PDF_ANCHO = 4;
	public static final int PDF_ALTO = 4;
	public static final int PDF_FORMATO = 4;
	public static final int PDF_TABLE_COLUMNS = 5;
	public static final int PDF_TABLE_PADDING = 5;
	public static final int SIZE_8 = 8;
	public static final int SIZE_10 = 10;
	public static final int SIZE_40 = 40;
	public static final int SIZE_100_WIDTH = 100;	
	public static final int[] SIZE_100 = {10, 60, 10, 10, 10};	
	
	public static final int COLOR_51 = 51;
	public static final int COLOR_112 = 112;
	public static final int COLOR_153 = 153;
	public static final int COLOR_185 = 185;
	public static final int COLOR_201 = 201;
	public static final int COLOR_241 = 241;
	public static final int COLOR_254 = 254;
	public static final int COLSPAN = 5;
	public static final int INDENTATION_LIST = 20;
	
	// mensajes
	public static final String MENSAJE_ERROR_CODIGO_VACIO = "El código no puede estar vacio";
	public static final String MENSAJE_ERROR_CODIGO_MAXIMO = "El código no puede ser mayor que %d caracteres";
	public static final String MENSAJE_ERROR_CODIGO_NUMERO = "El código debe ser un número";
	public static final String MENSAJE_ERROR_NOMBRE_VACIO = "El nombre no puede estar vacio";
	public static final String MENSAJE_ERROR_NOMBRE_MAXIMO = "El nombre no puede contener mas de %d caracteres";
	public static final String MENSAJE_ERROR_DESCRIPCION_MAXIMO = "La descripción no puede ser mayor que %d caracteres";
	public static final String MENSAJE_ERROR_VALOR_NO_VALIDO = "Valor no válido";
	public static final String MENSAJE_ERROR_AFINIDAD_VACIO = "La afinidad no puede estar vacia";
	
	// vistas	
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/itemsbaremacion/";
	public static final String JSP_FORM_APARTADO_BAREMACION = RUTA_BEP_CONF + "formApartadoBaremacion.jsp";
	public static final String JSP_FORM_BLOQUE_BAREMACION = RUTA_BEP_CONF + "formBloqueBaremacion.jsp";
	public static final String JSP_FORM_ITEM_BAREMACION = RUTA_BEP_CONF + "formItemBaremacion.jsp";
	public static final String JSP_ITEM_BAREMACION = RUTA_BEP_CONF + "itemsbaremacion.jsp";

	// ajax
	public static final String URL_PATTERN_FILES_PRIVADA = "/srv/es/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion";
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion";
	public static final String RESPONSE_AJAX_CONTENTTYPE = "application/json";
	public static final String RESPONSE_AJAX_ENCODING = "UTF-8";
	public static final String RESPONSE_AJAX_ERROR = "error";
	public static final int RESPONSE_AJAX_HTTP_CODE_ERROR = 400;
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaItemsBaremacion bean = new VistaItemsBaremacion();		
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
					indice(bean);
					break;
				case ACCION_DESCARGAR_FICHERO:
					descargarItems(bean, datos, request, response, usuario);
					break;
				case ACCION_DATATABLE_APARTADOS:
				case ACCION_AGREGAR_APARTADO:
				case ACCION_AGREGAR_APARTADO_CONFIRM:
				case ACCION_EDITAR_APARTADO:
				case ACCION_EDITAR_APARTADO_CONFIRM:	
				case ACCION_DESACTIVAR_APARTADO:
				case ACCION_ACTIVAR_APARTADO:				
				case ACCION_APARTADO_SELECCIONADO:
					accionesApartados(bean, datos, request, response, nombreAccion);					
					break;
				case ACCION_DATATABLE_BLOQUES:
				case ACCION_AGREGAR_BLOQUE:
				case ACCION_AGREGAR_BLOQUE_CONFIRM:
				case ACCION_EDITAR_BLOQUE:
				case ACCION_EDITAR_BLOQUE_CONFIRM:	
				case ACCION_DESACTIVAR_BLOQUE:
				case ACCION_ACTIVAR_BLOQUE:				
				case ACCION_BLOQUE_SELECCIONADO:
					accionesBloques(bean, datos, request, response, nombreAccion);					
					break;
				
				case ACCION_DATATABLE_ITEMS:
				case ACCION_AGREGAR_ITEM:
				case ACCION_AGREGAR_ITEM_CONFIRM:
				case ACCION_EDITAR_ITEM:
				case ACCION_EDITAR_ITEM_CONFIRM:	
				case ACCION_DESACTIVAR_ITEM:
				case ACCION_ACTIVAR_ITEM:				
				case ACCION_ITEM_SELECCIONADO:
				case ACCION_DATATABLE_ITEMS_EXCLUYENTES:
				case ACCION_ITEM_EXCLUYENTE_SELECCIONADO:
				case ACCION_ITEM_EXCLUYENTE_DESELECCIONADO:
					accionesItems(bean, datos, request, response, nombreAccion);					
					break;
				
				default:
					errorFatal(bean);
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
	 * @throws IOException .
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void indice(VistaItemsBaremacion bean) {
		bean.setVista(JSP_ITEM_BAREMACION);
	}
	
	private void init(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_ITEM_BAREMACION);
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
	
	private void errorFatal(VistaItemsBaremacion bean) {
		bean.getMensajesDeError().add("Acción no definida");
		this.indice(bean);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// APARTADOS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesApartados(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		switch (nombreAccion) {
			case ACCION_DATATABLE_APARTADOS:
				listadoApartados(bean, datos, request, response);
				break;	
			case ACCION_AGREGAR_APARTADO:
				agregarApartado(bean);
				break;
			case ACCION_AGREGAR_APARTADO_CONFIRM:
				agregarApartadoConfirm(bean, request);
				break;
			case ACCION_EDITAR_APARTADO:
				editarApartado(bean, request);
				break;
			case ACCION_EDITAR_APARTADO_CONFIRM:
				editarApartadoConfirm(bean, request);
				break;
			case ACCION_DESACTIVAR_APARTADO:
				desactivarApartado(bean, request, response);
				break;
			case ACCION_ACTIVAR_APARTADO:
				activarApartado(bean, request, response);
				break;
			case ACCION_APARTADO_SELECCIONADO:
				seleccionarApartado(bean, request);
				break;	
			default:
				this.errorFatal(bean);
		}
	}
	
	private void listadoApartados(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<ApartadoBaremacion> dataTable = ModeloBaremacionApartados.obtenerInstancia().
						listadoApartadosGeneralesBaremacionDatatable(request.getParameterMap());
				bean.setDatatable(dataTable);
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
	
	private void agregarApartado(VistaItemsBaremacion bean) {
		bean.setVista(JSP_FORM_APARTADO_BAREMACION);
		bean.setApartadoBaremacion(null);
		bean.setUltimoCodigo("");
	}
	
	private void agregarApartadoConfirm(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_FORM_APARTADO_BAREMACION);
		
		ApartadoBaremacion apartado = this.validateApartadoBaremacion(new ApartadoBaremacion(), request);
		apartado.setActivo(true);
		ModeloBaremacionApartados.obtenerInstancia().insertaApartado(apartado, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_APARTADO_AGREGAR, bean, request);
		bean.setVista(JSP_ITEM_BAREMACION);				
	}
	
	private void desactivarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_ITEM_BAREMACION);
		
		ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		modelo.desactivarApartado(apartado, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_APARTADO_DESACTIVAR, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void activarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(JSP_ITEM_BAREMACION);
		
		ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		modelo.activarApartado(apartado, bean.getUsuarioLogeado());
		
		BolsaEmpleoUtils.addMensajeDeExito(MENSAJE_EXITO_APARTADO_ACTIVAR, bean, request);
		response.sendRedirect(request.getServletPath());
	}
	
	private void editarApartado(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		bean.setApartadoBaremacion(modelo.getApartadoBaremacionById(codNum));
		
		bean.setVista(JSP_FORM_APARTADO_BAREMACION);
		bean.setUltimoCodigo(modelo.getUltimoCodigoApartado());	
	}
	
	private void editarApartadoConfirm(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = this.validateApartadoBaremacion(modelo.getApartadoBaremacionById(codNum), request); 
				
		bean.setVista(JSP_FORM_APARTADO_BAREMACION);
		bean.setApartadoBaremacion(apartado);
		
		modelo.actualizaApartado(apartado, bean.getUsuarioLogeado());
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_APARTADO_EDITAR);
		
		bean.setVista(JSP_ITEM_BAREMACION);
	}
	
	private void seleccionarApartado(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(codNum);
				
		bean.setApartadoBaremacion(apartado);
		bean.setVista(JSP_ITEM_BAREMACION);
	}
	
	private ApartadoBaremacion validateApartadoBaremacion(ApartadoBaremacion apartado, HttpServletRequest request) throws UVException {
		apartado.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_CODIGO)));
		if (apartado.getCodigo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CODIGO_VACIO);
		}
		if (apartado.getCodigo().length() > ModeloBaremacionApartados.COLUMN_CODIGO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CODIGO_MAXIMO, ModeloBaremacionItems.COLUMN_CODIGO_MAXLENGTH));
		}
		
		apartado.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_NOMBRE)));
		if (apartado.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (apartado.getNombre().length() > ModeloBaremacionApartados.COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_MAXIMO, ModeloBaremacionItems.COLUMN_NOMBRE_MAXLENGTH));
		}
		
		apartado.setPuntuacionMaxima(Formateador.leeParametroDouble(request.getParameter(PARAM_APARTADO_PUNTUACIONMAXIMA)));
		apartado.setPorcentajeMaximo(Formateador.leeParametroDouble(request.getParameter(PARAM_APARTADO_PORCENTAJEMAXIMO)));
		
		return apartado;
	}
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// BLOQUES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void accionesBloques(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		switch (nombreAccion) {
			case ACCION_DATATABLE_BLOQUES:
				listadoBloques(bean, datos, request, response);
				break;	
			case ACCION_AGREGAR_BLOQUE:
				agregarBloque(bean, request);
				break;
			case ACCION_AGREGAR_BLOQUE_CONFIRM:
				agregarBloqueConfirm(bean, request);
				break;
			case ACCION_EDITAR_BLOQUE:
				editarBloque(bean, request);
				break;
			case ACCION_EDITAR_BLOQUE_CONFIRM:
				editarBloqueConfirm(bean, request);
				break;
			case ACCION_DESACTIVAR_BLOQUE:
				desactivarBloque(bean, request);
				break;
			case ACCION_ACTIVAR_BLOQUE:
				activarBloque(bean, request);
				break;
			case ACCION_BLOQUE_SELECCIONADO:
				seleccionarBloque(bean, request);
				break;	
			default:
				this.errorFatal(bean);
		}
	}
	
	private void listadoBloques(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, UVException, IOException {
		
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(codNum);
		
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		datos.setRespuestaEnviada(true);
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BloqueBaremacion> dataTable = ModeloBaremacionBloques.obtenerInstancia().listadoBloquesBaremacionDatatable(
						request.getParameterMap(), apartado);				
				bean.setDatatable(dataTable);
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
	
	private void agregarBloque(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		bean.setApartadoBaremacion(apartado);
		bean.setBloqueBaremacion(null);
		bean.setVista(JSP_FORM_BLOQUE_BAREMACION);	
		
		bean.setUltimoCodigo((Integer.parseInt(ModeloBaremacionBloques.obtenerInstancia().getUltimoCodigoBloque(apartado)) + 1) + "");	
	}
	
	private void agregarBloqueConfirm(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(codNum);
		
		bean.setApartadoBaremacion(apartado);
		bean.setBloqueBaremacion(null);
		bean.setUltimoCodigo(ModeloBaremacionBloques.obtenerInstancia().getUltimoCodigoBloque(apartado));
		bean.setVista(JSP_FORM_BLOQUE_BAREMACION);
				
		BloqueBaremacion bloque = this.validateBloqueBaremacion(new BloqueBaremacion(), request); 
		bloque.setActivo(true);
		bloque.setApartadoBaremacion(apartado);
		
		// agregamos
		ModeloBaremacionBloques.obtenerInstancia().insertaBloque(bloque, bean.getUsuarioLogeado());
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_AGREGAR);
		bean.setVista(JSP_ITEM_BAREMACION);	
	}
	
	private void desactivarBloque(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_ITEM_BAREMACION);
		
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
		
		modelo.desactivarBloque(bloque, bean.getUsuarioLogeado());		
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_DESACTIVAR);
		this.seleccionarBloque(bean, request);
	}
	
	private void activarBloque(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_ITEM_BAREMACION);
		
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
		
		modelo.activarBloque(bloque, bean.getUsuarioLogeado());
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_ACTIVAR);
		this.seleccionarBloque(bean, request);			
	}
	
	private void editarBloque(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
		
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);
		
		bean.setVista(JSP_FORM_BLOQUE_BAREMACION);
		bean.setUltimoCodigo(ModeloBaremacionApartados.obtenerInstancia().getUltimoCodigoApartado());			
	}
	
	private void editarBloqueConfirm(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		Integer codBloque = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));

		bean.setVista(JSP_FORM_BLOQUE_BAREMACION);
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codBloque);
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);

		modelo.actualizaBloque(this.validateBloqueBaremacion(bloque, request), bean.getUsuarioLogeado());

		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_EDITAR);

		bean.setVista(JSP_ITEM_BAREMACION);
	}
	
	private void seleccionarBloque(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
				
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);		
		bean.setVista(JSP_ITEM_BAREMACION);
	}
	
	private BloqueBaremacion validateBloqueBaremacion(BloqueBaremacion bloque, HttpServletRequest request) throws UVException {
		bloque.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_CODIGO)));
		if (bloque.getCodigo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CODIGO_VACIO);
		}
		if (bloque.getCodigo().length() > ModeloBaremacionItems.COLUMN_CODIGO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CODIGO_MAXIMO, ModeloBaremacionItems.COLUMN_CODIGO_MAXLENGTH));
		}
		
		bloque.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_NOMBRE)));
		if (bloque.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (bloque.getNombre().length() > ModeloBaremacionItems.COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_MAXIMO, ModeloBaremacionItems.COLUMN_NOMBRE_MAXLENGTH));
		}
		
		bloque.setNumeroMaximoMeritos(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE_NUMEROMAXIMOMERITOS)));
		if (bloque.getNumeroMaximoMeritos() != null && bloque.getNumeroMaximoMeritos() < 1) {
			throw new UVException(MENSAJE_ERROR_BLOQUE_NUMMAXMERITOS_MINIMO);
		}
		
		return bloque;
	}	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ITEM
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void accionesItems(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response, String nombreAccion) 
			throws SQLException, UVException, IOException {
		switch (nombreAccion) {
			case ACCION_DATATABLE_ITEMS:
				listadoItems(bean, datos, request, response);
				break;	
			case ACCION_DATATABLE_ITEMS_EXCLUYENTES:
				listadoItemsExcluyentes(bean, datos, request, response);
				break;	
			case ACCION_AGREGAR_ITEM:
				agregarItem(bean, request);
				break;
			case ACCION_AGREGAR_ITEM_CONFIRM:
				agregarItemConfirm(bean, request);
				break;
			case ACCION_EDITAR_ITEM:
				editarItem(bean, request);
				break;
			case ACCION_EDITAR_ITEM_CONFIRM:
				editarItemConfirm(bean, request);
				break;
			case ACCION_DESACTIVAR_ITEM:
				desactivarItem(bean, request);
				break;
			case ACCION_ACTIVAR_ITEM:
				activarItem(bean, request);
				break;
			case ACCION_ITEM_SELECCIONADO:
				seleccionarItem(bean, request);
				break;
			case ACCION_ITEM_EXCLUYENTE_SELECCIONADO:
				seleccionarItemExcluyente(bean, request, true);
				break;
			case ACCION_ITEM_EXCLUYENTE_DESELECCIONADO:
				seleccionarItemExcluyente(bean, request, false);
				break;
			default:
				this.errorFatal(bean);
		}
	}
	
	private void listadoItems(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<ItemBaremacion> dataTable = ModeloBaremacionItems.obtenerInstancia().
						listadoItemsBaremacionDatatable(request.getParameterMap(), bloque);
				bean.setDatatable(dataTable);
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
	
	private void listadoItemsExcluyentes(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM));
		ItemBaremacion item = modelo.getItemBaremacionById(codNum);
		
		datos.setRespuestaEnviada(true);
		datos.setContentType(RESPONSE_AJAX_CONTENTTYPE);		
		response.setContentType(RESPONSE_AJAX_CONTENTTYPE);
		response.setCharacterEncoding(RESPONSE_AJAX_ENCODING);
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<ItemBaremacion> dataTable = modelo.listadoItemsBaremacionExcluyentesDatatable(request.getParameterMap(), item);
				bean.setDatatable(dataTable);
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
	
	private void agregarItem(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);
		
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);
		bean.setItemBaremacion(null);
		
		ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
		bean.setAfinidades(modeloAfinidad.getTiposAfinidad());
		
		bean.setVista(JSP_FORM_ITEM_BAREMACION);	
		
		bean.setUltimoCodigo((Integer.parseInt(ModeloBaremacionItems.obtenerInstancia().getUltimoCodigoItem(bloque)) + 1) + "");	
	}
	
	private void agregarItemConfirm(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);

		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);
		bean.setItemBaremacion(null);
		bean.setUltimoCodigo(ModeloBaremacionItems.obtenerInstancia().getUltimoCodigoItem(bloque));
		bean.setVista(JSP_FORM_ITEM_BAREMACION);

		// agregamos
		ItemBaremacion item = this.validateItemBaremacion(new ItemBaremacion(), request);
		item.setBloqueBaremacion(bloque);
		item.setActivo(true);
		ModeloBaremacionItems.obtenerInstancia().insertaItem(item, bean.getUsuarioLogeado());

		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_AGREGAR);
		bean.setVista(JSP_ITEM_BAREMACION);
	}
	
	private void editarItem(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		
		bean.setApartadoBaremacion(item.getBloqueBaremacion().getApartadoBaremacion());
		bean.setBloqueBaremacion(item.getBloqueBaremacion());
		bean.setItemBaremacion(item);
		
		bean.setAfinidades(modeloAfinidad.getTiposAfinidad());
		
		bean.setVista(JSP_FORM_ITEM_BAREMACION);
		bean.setUltimoCodigo(ModeloBaremacionApartados.obtenerInstancia().getUltimoCodigoApartado());
		
		List<ItemBaremacion> listaItemsExcluyentes = modelo.getItemsExcluyentes(item);
		bean.setListaItemsExcluyentes(listaItemsExcluyentes);
	}
	
	private void editarItemConfirm(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		List<ItemBaremacion> listaItemsExcluyentes = modelo.getItemsExcluyentes(item);
		bean.setListaItemsExcluyentes(listaItemsExcluyentes);
		
		bean.setVista(JSP_FORM_ITEM_BAREMACION);
		bean.setApartadoBaremacion(item.getBloqueBaremacion().getApartadoBaremacion());
		bean.setBloqueBaremacion(item.getBloqueBaremacion());
		bean.setItemBaremacion(item);

		modelo.actualizaItem(this.validateItemBaremacion(item, request), bean.getUsuarioLogeado());

		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_EDITAR);

		bean.setVista(JSP_ITEM_BAREMACION);
	}
	
	private void desactivarItem(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_ITEM_BAREMACION);
		
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		
		modelo.desactivarItem(item, bean.getUsuarioLogeado());		
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_DESACTIVAR);
		this.seleccionarItem(bean, request);
	}
	
	private void activarItem(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		bean.setVista(JSP_ITEM_BAREMACION);
		
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		
		modelo.activarItem(item, bean.getUsuarioLogeado());
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_ACTIVAR);
		this.seleccionarItem(bean, request);			
	}
	
	private void seleccionarItem(VistaItemsBaremacion bean, HttpServletRequest request) throws SQLException, UVException {
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM));
		ItemBaremacion item = modelo.getItemBaremacionById(codNum);
				
		bean.setApartadoBaremacion(item.getBloqueBaremacion().getApartadoBaremacion());
		bean.setBloqueBaremacion(item.getBloqueBaremacion());
		bean.setItemBaremacion(item);
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");		
	}
	
	/** 
	 * Selecciona un item para excluirlo o añadirlo de otro item .
	 * @param bean .
	 * @param request .
	 * @param seleccionado .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	private void seleccionarItemExcluyente(VistaItemsBaremacion bean, HttpServletRequest request, Boolean seleccionado)
			throws UVException, SQLException {
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		
		ItemBaremacion itemPadre = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		ItemBaremacion itemHijo = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM_EXCLUYENTE)));

		if (Boolean.TRUE.equals(seleccionado)) {
			modelo.asignarItemsExcluyentesAItem(itemPadre, itemHijo, bean.getUsuarioLogeado());
		} else {
			modelo.borrarItemsExcluyentesAItem(itemPadre, itemHijo, bean.getUsuarioLogeado());
		}

		List<ItemBaremacion> listaItemsExcluyentes = modelo.getItemsExcluyentes(itemPadre);
		bean.setListaItemsExcluyentes(listaItemsExcluyentes);
		bean.setItemBaremacion(itemPadre);
		bean.setApartadoBaremacion(itemPadre.getBloqueBaremacion().getApartadoBaremacion());
		bean.setBloqueBaremacion(itemPadre.getBloqueBaremacion());
		bean.setVista(RUTA_BEP_CONF + "formItemBaremacion.jsp");
	}
	
	/** descarga un fichero .
	 * @param bean .
	 * @param datos .
	 * @param request .
	 * @param response .
	 * @param usu .
	 * @throws SQLException excepcion de bbdd.
	 * @throws UVException en caso de error de parametros .
	 * @throws IOException en caso de error de input u output .
	 */
	private void descargarItems(VistaItemsBaremacion bean, UVDatos datos, 
			HttpServletRequest request, HttpServletResponse response, Usuario usu) throws SQLException, UVException, IOException {
		try (ServletOutputStream stream = response.getOutputStream(); BufferedInputStream buf = new BufferedInputStream(generarPDF(usu))) {
			int readBytes = 0;
			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(ex));
			LOGGER.log(Level.SEVERE, ex.toString());
			throw new UVException(ex.toString());
		}
        
		response.setContentType("application/pdf");
		datos.setRespuestaEnviada(true);
	}
	
	/**
	 * generar PDF de los items .
	 * @param usu .
	 * @return InputStream .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public InputStream generarPDF(Usuario usu) throws UVException, SQLException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		List<ItemBaremacion> listaItems = modelo.listaItemBaremacion();
		
		try (Document document = new Document()) {
			// create a PDF writer instance and pass output stream
			PdfWriter.getInstance(document, out);

			document.open();
			document.addAuthor(usu.getApellidosYNombre());
			document.addTitle("Listado_Items");
			document.addCreationDate();

			document.add(new Paragraph(new Chunk("Listado de Items", FontFactory.getFont(FontFactory.HELVETICA, SIZE_40, Font.BOLDITALIC))));
				        
			this.generarPDFTable(listaItems, document);
		} catch (Exception exp) {
			throw new UVException("Error generando pdf, consulte con los administradores" + exp);
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private void generarPDFTable(List<ItemBaremacion> listaItems, Document document) throws SQLException, UVException {
		Table table = new Table(PDF_TABLE_COLUMNS, listaItems.size());
		ModeloBaremacionBloques modeloBloque = ModeloBaremacionBloques.obtenerInstancia();
		ModeloBaremacionApartados modeloApartado = ModeloBaremacionApartados.obtenerInstancia();
		List<BloqueBaremacion> listaBloques = modeloBloque.listaBloqueBaremacion();
		List<ApartadoBaremacion> listaApartados = modeloApartado.listaApartadoBaremacionActivosOrdenadosPorCodigo();

		this.generarPDFTableHeader(table);
		
		for (ApartadoBaremacion apa: listaApartados) {
			Cell cell = new Cell(new Paragraph(
					"Apartado " + apa.getCodigo() + " - " + apa.getNombre(), new Font(Font.HELVETICA, SIZE_8)));
			cell.setBackgroundColor(new Color(COLOR_112, COLOR_112, COLOR_112));
			cell.setColspan(COLSPAN);
			cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.addCell(cell);
			for (BloqueBaremacion bloq: listaBloques) {
				if (bloq.getApartadoBaremacion().getCodNum().equals(apa.getCodNum())) {
					cell = new Cell(new Paragraph(
							"Bloque " + bloq.getCodigo() + " - " + bloq.getNombre(), new Font(Font.HELVETICA, SIZE_8)));
					cell.setBackgroundColor(new Color(COLOR_185, COLOR_185, COLOR_185));
					cell.setColspan(COLSPAN);
					cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
					table.addCell(cell);
					for (ItemBaremacion item: listaItems) {
						if (item.getBloqueBaremacion().getCodNum().equals(bloq.getCodNum())) {
							String codigoItem = item.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
									+ item.getBloqueBaremacion().getCodigo() + "." 
									+ item.getCodigo();
							
							cell = new Cell(new Paragraph(
									codigoItem, new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
							
							String nombre = item.getNombre();
							if (item.getDescripcion() != null) {
								nombre = item.getNombre() + " ( " + item.getDescripcion() + " )";
							}
							
							cell = new Cell(new Paragraph(nombre, new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							table.addCell(cell);
							cell = new Cell(new Paragraph(
									item.getValor().toString(), new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph(item.getAfinidad() != null ? item.getAfinidad().toString() : item.getAfinidad(),
									new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
							cell = new Cell(new Paragraph(item.getIndividualizado() ? "Si" : "No",
									new Font(Font.HELVETICA, SIZE_8)));
							cell.setBackgroundColor(new Color(COLOR_241, COLOR_241, COLOR_241));
							cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
							table.addCell(cell);
						}
					}
				}
			}
		}
		document.add(table);
	}
	
	private void generarPDFTableHeader(Table table) {
		table.setBorderWidth(1);
		table.setBorderColor(new Color(0, 0, 0));
		table.setPadding(PDF_TABLE_PADDING);
		table.setWidth(SIZE_100_WIDTH);
		table.setWidths(SIZE_100);

		Font font = new Font(Font.HELVETICA, SIZE_10);
		font.setColor(new Color(0, COLOR_51, COLOR_153));

		Phrase phrase = new Phrase("Código", font);
		Cell cell = new Cell(phrase);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase2 = new Phrase("Nombre", font);
		cell = new Cell(phrase2);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase3 = new Phrase("Valor Unitario", font);
		cell = new Cell(phrase3);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);

		Phrase phrase4 = new Phrase("Afinidad", font);
		cell = new Cell(phrase4);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);
		table.endHeaders();
		
		Phrase phrase5 = new Phrase("Individualizado", font);
		cell = new Cell(phrase5);
		cell.setHeader(true);
		cell.setBackgroundColor(new Color(COLOR_185, COLOR_201, COLOR_254));
		table.addCell(cell);
		table.endHeaders();
	}
		
	private ItemBaremacion validateItemBaremacion(ItemBaremacion item, HttpServletRequest request) throws UVException, SQLException {
		item.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_CODIGO)));
		if (item.getCodigo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CODIGO_VACIO);
		}
		if (item.getCodigo().length() > ModeloBaremacionItems.COLUMN_CODIGO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CODIGO_MAXIMO, ModeloBaremacionItems.COLUMN_CODIGO_MAXLENGTH));
		}
		if (!BolsaEmpleoUtils.isInteger(item.getCodigo())) {
			throw new UVException(MENSAJE_ERROR_CODIGO_NUMERO);
		}

		item.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_NOMBRE)));
		if (item.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (item.getNombre().length() > ModeloBaremacionItems.COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_MAXIMO, ModeloBaremacionItems.COLUMN_NOMBRE_MAXLENGTH));
		}
		
		item.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_DESCRIPCION)));
		if (item.getDescripcion().length() > ModeloBaremacionItems.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_MAXIMO, ModeloBaremacionItems.COLUMN_DESCRIPCION_MAXLENGTH));
		}

		item.setUnidades(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_UNIDADES)));

		item.setAfinidad(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_AFINIDAD)));
		if (item.getAfinidad() == null) {
			throw new UVException(MENSAJE_ERROR_AFINIDAD_VACIA);
		}		
		if ("N".equals(item.getAfinidad())) {
			item.setAfinidad(null);
		} else {
			List<String> afinidadesValidas = ModeloAfinidad.obtenerInstancia().getTiposAfinidad();
			if (!afinidadesValidas.contains(item.getAfinidad())) {
				throw new UVException("Afinidad no válida");
			}
		}
		
		item.setValor(Formateador.leeParametroDouble(request.getParameter(PARAM_ITEM_VALOR)));
		if (item.getValor() == null) {
			throw new UVException("El valor unitario no puede estar vacio");
		}

		item.setValorMinimo(Formateador.leeParametroDouble(request.getParameter(PARAM_ITEM_VALOR_MINIMO)));
		if (item.getValorMinimo() == null) {
			throw new UVException("El valor mínimo no puede estar vacio");
		}

		item.setValorMaximo(Formateador.leeParametroDouble(request.getParameter(PARAM_ITEM_VALOR_MAXIMO)));
		if (item.getValorMaximo() == null) {
			throw new UVException("El valor máximo no puede estar vacio");
		}
		
		if (item.getValorMinimo() > item.getValorMaximo()) {
			throw new UVException("El valor mínimo debe ser menor o igual que el valor máximo");
		}

		String individualizado = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_INDIVIDUALIZADO));
		item.setIndividualizado("S".equals(individualizado));
				
		return item;
	}	
	
}
