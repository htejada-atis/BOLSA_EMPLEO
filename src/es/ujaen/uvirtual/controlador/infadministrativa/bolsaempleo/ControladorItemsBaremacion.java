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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloAfinidad;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBaremacion;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
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
	public static final String ACCION_AGREGAR_ITEM = "agregaritem";
	public static final String ACCION_AGREGAR_ITEM_CONFIRM = "agregaritemconfirm";
	public static final String ACCION_EDITAR_ITEM = "editaritem";
	public static final String ACCION_EDITAR_ITEM_CONFIRM = "editaritemconfirm";
	public static final String ACCION_DESACTIVAR_ITEM = "desactivaraitem";
	public static final String ACCION_ACTIVAR_ITEM = "activaraitem";	
	public static final String ACCION_ITEM_SELECCIONADO = "itemseleccionado";
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
	public static final String PARAM_ITEM_NOMBRE = "nombreitem";
	public static final String PARAM_ITEM_DESCRIPCION = "descripcion";
	public static final String PARAM_ITEM_CODIGO = "codigoitem";
	public static final String PARAM_ITEM_UNIDADES = "unidades";
	public static final String PARAM_ITEM_VALOR = "valor";
	public static final String PARAM_ITEM_VALOR_MINIMO = "valorminimo";
	public static final String PARAM_ITEM_VALOR_MAXIMO = "valormaximo";
	public static final String PARAM_ITEM_AFINIDAD = "afinidad";
	
	// mensajes
	public static final String MENSAJE_ERROR_CODIGO_VACIO = "El código no puede estar vacio";
	public static final String MENSAJE_ERROR_CODIGO_MAXIMO = "El código no puede ser mayor que ";
	public static final String MENSAJE_ERROR_CODIGO_NUMERO = "El código debe ser un número";
	public static final String MENSAJE_ERROR_NOMBRE_VACIO = "El nombre no puede estar vacio";
	public static final String MENSAJE_ERROR_NOMBRE_MAXIMO = "El nombre no puede ser mayor que ";
	public static final String MENSAJE_ERROR_DESCRIPCION_MAXIMO = "La descripción no puede ser mayor que ";
	public static final String MENSAJE_ERROR_VALOR_NO_VALIDO = "Valor no válido";
	public static final String MENSAJE_ERROR_AFINIDAD_VACIO = "La afinidad no puede estar vacia";
	
	// urls
	public static final String URL_PATTERN_AJAX = "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion";
	
	// ruta vistas
	public static final String RUTA_BEP_CONF = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/itemsbaremacion/";
	
	public static final int RESPONSE_HTTP_CODE_ERROR = 400;
	
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
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().checkUser(datos);
			switch (nombreAccion) {
				case ACCION_INDEX:
					indice(bean, request, response);
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
					accionesItems(bean, datos, request, response, nombreAccion);					
					break;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
			bean.getMensajesDeError().add(e.toString());
			
			ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
			try {
				bean.setAfinidades(modeloAfinidad.listaAfinidades());
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
			bean.getMensajesDeError().add(e.getMessage());
			
			ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
			try {
				bean.setAfinidades(modeloAfinidad.listaAfinidades());
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
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
	
	private void indice(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
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
				agregarApartado(bean, request, response);
				break;
			case ACCION_AGREGAR_APARTADO_CONFIRM:
				agregarApartadoConfirm(bean, request, response);
				break;
			case ACCION_EDITAR_APARTADO:
				editarApartado(bean, request, response);
				break;
			case ACCION_EDITAR_APARTADO_CONFIRM:
				editarApartadoConfirm(bean, request, response);
				break;
			case ACCION_DESACTIVAR_APARTADO:
				desactivarApartado(bean, request, response);
				break;
			case ACCION_ACTIVAR_APARTADO:
				activarApartado(bean, request, response);
				break;
			case ACCION_APARTADO_SELECCIONADO:
				seleccionarApartado(bean, request, response);
				break;				
		}
	}
	
	private void listadoApartados(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<ApartadoBaremacion> dataTable = ModeloBaremacion.obtenerInstancia().
						listadoApartadosGeneralesBaremacionDatatable(request.getParameterMap());
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
	
	private void agregarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formApartadoBaremacion.jsp");
		bean.setApartadoBaremacion(null);
		bean.setUltimoCodigo((Integer.parseInt(ModeloBaremacion.obtenerInstancia().getUltimoCodigoApartado()) + 1) + "");
	}
	
	private void agregarApartadoConfirm(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "formApartadoBaremacion.jsp");
		
		ApartadoBaremacion apartado = this.validateApartadoBaremacion(new ApartadoBaremacion(), request);
		apartado.setActivo(true);
		ModeloBaremacion.obtenerInstancia().insertaApartado(apartado);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_APARTADO_AGREGAR);
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");				
	}
	
	private void desactivarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		modelo.desactivarApartado(apartado);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_APARTADO_DESACTIVAR);
	}
	
	private void activarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		modelo.activarApartado(apartado);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_APARTADO_ACTIVAR);	
	}
	
	private void editarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		bean.setApartadoBaremacion(modelo.getApartadoBaremacionById(codNum));
		
		bean.setVista(RUTA_BEP_CONF + "formApartadoBaremacion.jsp");
		bean.setUltimoCodigo(ModeloBaremacion.obtenerInstancia().getUltimoCodigoApartado());			
	}
	
	private void editarApartadoConfirm(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = this.validateApartadoBaremacion(modelo.getApartadoBaremacionById(codNum), request); 
				
		bean.setVista(RUTA_BEP_CONF + "formApartadoBaremacion.jsp");
		bean.setApartadoBaremacion(apartado);
		
		modelo.actualizaApartado(apartado);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_APARTADO_EDITAR);
		
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
	}
	
	private void seleccionarApartado(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
				
		bean.setApartadoBaremacion(apartado);
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
	}
	
	private ApartadoBaremacion validateApartadoBaremacion(ApartadoBaremacion apartado, HttpServletRequest request) throws UVException {
		apartado.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_CODIGO)));
		if (apartado.getCodigo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CODIGO_VACIO);
		}
		if (apartado.getCodigo().length() > ModeloBaremacion.COLUMN_CODIGO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CODIGO_MAXIMO, ModeloBaremacion.COLUMN_CODIGO_MAXLENGTH));
		}
		
		apartado.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_APARTADO_NOMBRE)));
		if (apartado.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (apartado.getNombre().length() > ModeloBaremacion.COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_MAXIMO, ModeloBaremacion.COLUMN_NOMBRE_MAXLENGTH));
		}
		
		apartado.setPuntuacionMaxima(Formateador.leeParametroFloat(request.getParameter(PARAM_APARTADO_PUNTUACIONMAXIMA)));
		apartado.setPorcentajeMaximo(Formateador.leeParametroFloat(request.getParameter(PARAM_APARTADO_PORCENTAJEMAXIMO)));
		
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
				agregarBloque(bean, request, response);
				break;
			case ACCION_AGREGAR_BLOQUE_CONFIRM:
				agregarBloqueConfirm(bean, request, response);
				break;
			case ACCION_EDITAR_BLOQUE:
				editarBloque(bean, request, response);
				break;
			case ACCION_EDITAR_BLOQUE_CONFIRM:
				editarBloqueConfirm(bean, request, response);
				break;
			case ACCION_DESACTIVAR_BLOQUE:
				desactivarBloque(bean, request, response);
				break;
			case ACCION_ACTIVAR_BLOQUE:
				activarBloque(bean, request, response);
				break;
			case ACCION_BLOQUE_SELECCIONADO:
				seleccionarBloque(bean, request, response);
				break;				
		}
	}
	
	private void listadoBloques(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<BloqueBaremacion> dataTable = modelo.listadoBloquesBaremacionDatatable(
						request.getParameterMap(), apartado);				
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
	
	private void agregarBloque(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		bean.setApartadoBaremacion(apartado);
		bean.setBloqueBaremacion(null);
		bean.setVista(RUTA_BEP_CONF + "formBloqueBaremacion.jsp");	
		
		bean.setUltimoCodigo((Integer.parseInt(modelo.getUltimoCodigoBloque(apartado)) + 1) + "");	
	}
	
	private void agregarBloqueConfirm(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_APARTADO));
		ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
		
		bean.setApartadoBaremacion(apartado);
		bean.setBloqueBaremacion(null);
		bean.setUltimoCodigo(modelo.getUltimoCodigoBloque(apartado));
		bean.setVista(RUTA_BEP_CONF + "formBloqueBaremacion.jsp");
				
		BloqueBaremacion bloque = this.validateBloqueBaremacion(new BloqueBaremacion(), request); 
		bloque.setActivo(true);
		bloque.setApartadoBaremacion(apartado);
		
		// agregamos
		modelo.insertaBloque(bloque);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_AGREGAR);
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");	
	}
	
	private void desactivarBloque(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
		
		modelo.desactivarBloque(bloque);		
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_DESACTIVAR);
		this.seleccionarBloque(bean, request, response);
	}
	
	private void activarBloque(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
		
		modelo.activarBloque(bloque);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_ACTIVAR);
		this.seleccionarBloque(bean, request, response);			
	}
	
	private void editarBloque(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
		
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);
		
		bean.setVista(RUTA_BEP_CONF + "formBloqueBaremacion.jsp");
		bean.setUltimoCodigo(ModeloBaremacion.obtenerInstancia().getUltimoCodigoApartado());			
	}
	
	private void editarBloqueConfirm(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codBloque = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));

		bean.setVista(RUTA_BEP_CONF + "formBloqueBaremacion.jsp");
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codBloque);
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);

		bloque = this.validateBloqueBaremacion(bloque, request);
		modelo.actualizaBloque(bloque);

		bean.getMensajesDeExito().add(MENSAJE_EXITO_BLOQUE_EDITAR);

		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
	}
	
	private void seleccionarBloque(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE)));
				
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);		
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
	}
	
	private BloqueBaremacion validateBloqueBaremacion(BloqueBaremacion bloque, HttpServletRequest request) throws UVException {
		bloque.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_CODIGO)));
		if (bloque.getCodigo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CODIGO_VACIO);
		}
		if (bloque.getCodigo().length() > ModeloBaremacion.COLUMN_CODIGO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CODIGO_MAXIMO, ModeloBaremacion.COLUMN_CODIGO_MAXLENGTH));
		}
		
		bloque.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_BLOQUE_NOMBRE)));
		if (bloque.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (bloque.getNombre().length() > ModeloBaremacion.COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_MAXIMO, ModeloBaremacion.COLUMN_NOMBRE_MAXLENGTH));
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
			case ACCION_AGREGAR_ITEM:
				agregarItem(bean, request, response);
				break;
			case ACCION_AGREGAR_ITEM_CONFIRM:
				agregarItemConfirm(bean, request, response);
				break;
			case ACCION_EDITAR_ITEM:
				editarItem(bean, request, response);
				break;
			case ACCION_EDITAR_ITEM_CONFIRM:
				editarItemConfirm(bean, request, response);
				break;
			case ACCION_DESACTIVAR_ITEM:
				desactivarItem(bean, request, response);
				break;
			case ACCION_ACTIVAR_ITEM:
				activarItem(bean, request, response);
				break;
			case ACCION_ITEM_SELECCIONADO:
				seleccionarItem(bean, request, response);
				break;
		}
	}
	
	private void listadoItems(VistaItemsBaremacion bean, UVDatos datos, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, SQLException, UVException {
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);
		
		datos.setContentType("application/json");
		datos.setRespuestaEnviada(true);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try (PrintWriter writer = response.getWriter()) {
			try {
				BolsaEmpleoDataTable<ItemBaremacion> dataTable = modelo.listadoItemsBaremacionDatatable(request.getParameterMap(), bloque);
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
	
	private void agregarItem(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);
		
		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);
		bean.setItemBaremacion(null);
		
		ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
		bean.setAfinidades(modeloAfinidad.listaAfinidades());
		
		bean.setVista(RUTA_BEP_CONF + "formItemBaremacion.jsp");	
		
		bean.setUltimoCodigo((Integer.parseInt(modelo.getUltimoCodigoItem(bloque)) + 1) + "");	
	}
	
	private void agregarItemConfirm(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_BLOQUE));
		BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);

		bean.setApartadoBaremacion(bloque.getApartadoBaremacion());
		bean.setBloqueBaremacion(bloque);
		bean.setItemBaremacion(null);
		bean.setUltimoCodigo(modelo.getUltimoCodigoItem(bloque));
		bean.setVista(RUTA_BEP_CONF + "formItemBaremacion.jsp");

		// agregamos
		ItemBaremacion item = this.validateItemBaremacion(new ItemBaremacion(), request);
		item.setBloqueBaremacion(bloque);
		item.setActivo(true);
		modelo.insertaItem(item);

		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_AGREGAR);
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
	}
	
	private void editarItem(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		ModeloAfinidad modeloAfinidad = ModeloAfinidad.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		
		bean.setApartadoBaremacion(item.getBloqueBaremacion().getApartadoBaremacion());
		bean.setBloqueBaremacion(item.getBloqueBaremacion());
		bean.setItemBaremacion(item);
		
		bean.setAfinidad(modeloAfinidad.getAfinidadByCodigo(item.getAfinidad()));
		bean.setAfinidades(modeloAfinidad.listaAfinidades());
		
		bean.setVista(RUTA_BEP_CONF + "formItemBaremacion.jsp");
		bean.setUltimoCodigo(ModeloBaremacion.obtenerInstancia().getUltimoCodigoApartado());
	}
	
	private void editarItemConfirm(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));

		bean.setVista(RUTA_BEP_CONF + "formItemBaremacion.jsp");
		bean.setApartadoBaremacion(item.getBloqueBaremacion().getApartadoBaremacion());
		bean.setBloqueBaremacion(item.getBloqueBaremacion());
		bean.setItemBaremacion(item);

		item = this.validateItemBaremacion(item, request);
		modelo.actualizaItem(item);

		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_EDITAR);

		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
	}
	
	private void desactivarItem(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		
		modelo.desactivarItem(item);		
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_DESACTIVAR);
		this.seleccionarItem(bean, request, response);
	}
	
	private void activarItem(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response)
			throws SQLException, UVException, IOException {
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");
		
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		ItemBaremacion item = modelo.getItemBaremacionById(Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM)));
		
		modelo.activarItem(item);
		
		bean.getMensajesDeExito().add(MENSAJE_EXITO_ITEM_ACTIVAR);
		this.seleccionarItem(bean, request, response);			
	}
	
	private void seleccionarItem(VistaItemsBaremacion bean, HttpServletRequest request, HttpServletResponse response) throws SQLException, UVException {
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
		Integer codNum = Formateador.leeParametroInteger(request.getParameter(PARAM_ITEM));
		ItemBaremacion item = modelo.getItemBaremacionById(codNum);
				
		bean.setApartadoBaremacion(item.getBloqueBaremacion().getApartadoBaremacion());
		bean.setBloqueBaremacion(item.getBloqueBaremacion());
		bean.setItemBaremacion(item);
		bean.setVista(RUTA_BEP_CONF + "itemsbaremacion.jsp");		
	}
		
	private ItemBaremacion validateItemBaremacion(ItemBaremacion item, HttpServletRequest request) throws UVException, SQLException {
		item.setCodigo(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_CODIGO)));
		if (item.getCodigo().isBlank()) {
			throw new UVException(MENSAJE_ERROR_CODIGO_VACIO);
		}
		if (item.getCodigo().length() > ModeloBaremacion.COLUMN_CODIGO_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_CODIGO_MAXIMO, ModeloBaremacion.COLUMN_CODIGO_MAXLENGTH));
		}
		if (!BolsaEmpleoUtils.isInteger(item.getCodigo())) {
			throw new UVException(MENSAJE_ERROR_CODIGO_NUMERO);
		}

		item.setNombre(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_NOMBRE)));
		if (item.getNombre().isBlank()) {
			throw new UVException(MENSAJE_ERROR_NOMBRE_VACIO);
		}
		if (item.getNombre().length() > ModeloBaremacion.COLUMN_NOMBRE_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_NOMBRE_MAXIMO, ModeloBaremacion.COLUMN_NOMBRE_MAXLENGTH));
		}
		
		item.setDescripcion(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_DESCRIPCION)));
		if (item.getDescripcion().length() > ModeloBaremacion.COLUMN_DESCRIPCION_MAXLENGTH) {
			throw new UVException(String.format(MENSAJE_ERROR_DESCRIPCION_MAXIMO, ModeloBaremacion.COLUMN_DESCRIPCION_MAXLENGTH));
		}

		item.setUnidades(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_UNIDADES)));
		

		item.setAfinidad(EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_AFINIDAD)));
		if (item.getAfinidad() == null) {
			throw new UVException(MENSAJE_ERROR_AFINIDAD_VACIA);
		}
		if (EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ITEM_AFINIDAD)).equals("1")) {
			throw new UVException(MENSAJE_ERROR_AFINIDAD_VACIA);
		}
		
		item.setValor(Formateador.leeParametroFloat(request.getParameter(PARAM_ITEM_VALOR)));
		if (item.getValor() == null) {
			throw new UVException("El valor unitario no puede estar vacio");
		}

		item.setValorMinimo(Formateador.leeParametroFloat(request.getParameter(PARAM_ITEM_VALOR_MINIMO)));
		if (item.getValorMinimo() == null) {
			throw new UVException("El valor mínimo no puede estar vacio");
		}

		item.setValorMaximo(Formateador.leeParametroFloat(request.getParameter(PARAM_ITEM_VALOR_MAXIMO)));
		if (item.getValorMaximo() == null) {
			throw new UVException("El valor máximo no puede estar vacio");
		}
		
		if (item.getValorMinimo() > item.getValorMaximo()) {
			throw new UVException("El valor mínimo debe ser menor o igual que el valor máximo");
		}
		
		return item;
	}	
	
}
