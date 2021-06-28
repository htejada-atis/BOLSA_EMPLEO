package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorItemsBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaItemsBaremacion;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test controlador items baremación.
 * 
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorItemsBaremacion {

	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_APARTADOS_DEVUELTOS = "Debe devolver apartados";
	private static final String MENSAJE_APARTADO_DEVUELTO = "Debe devolver apartado";
	private static final String MENSAJE_BLOQUES_DEVUELTOS = "Debe devolver bloques";
	private static final String MENSAJE_BLOQUE_DEVUELTO = "Debe devolver bloque";
	private static final String MENSAJE_ITEMS_DEVUELTOS = "Debe devolver ítems";
	private static final String MENSAJE_ITEM_DEVUELTO = "Debe devolver ítem";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";

	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";

	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en io
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}

	/**
	 * Obtener items baremación, sin parametro definido .
	 * 
	 * @throws SQLException     si fallo bd
	 * @throws IOException      si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doGet(peticion, respuesta);

		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/**
	 * obtener datatable apartados .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA02ObtenerApartados() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados(false);

		assertNotEquals(MENSAJE_APARTADOS_DEVUELTOS, 0, bean.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/**
	 * obtener datatable bloques .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA03ObtenerBloques() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques(false);

		assertNotEquals(MENSAJE_BLOQUES_DEVUELTOS, 0, bean.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/**
	 * obtener datatable items .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA04ObtenerItems() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques(true);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(2);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertNotEquals(MENSAJE_ITEMS_DEVUELTOS, 0, bean2.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}

	/**
	 * seleccionar apartado .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA05SeleccionarApartado() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados(true);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_APARTADO_SELECCIONADO);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertNotNull(MENSAJE_APARTADO_DEVUELTO, bean2.getApartadoBaremacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}

	/**
	 * seleccionar bloque .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA06SeleccionarBloque() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques(true);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_BLOQUE_SELECCIONADO);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertNotNull(MENSAJE_BLOQUE_DEVUELTO, bean2.getBloqueBaremacion());
		assertNotNull(MENSAJE_APARTADO_DEVUELTO, bean2.getApartadoBaremacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}

	/**
	 * seleccionar item .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA07SeleccionarItem() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerItems(true);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ITEM_SELECCIONADO);
		ItemBaremacion item = (ItemBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM, item.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertNotNull(MENSAJE_ITEM_DEVUELTO, bean2.getItemBaremacion());
		assertNotNull(MENSAJE_BLOQUE_DEVUELTO, bean2.getBloqueBaremacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}

	/**
	 * desactivar apartado .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA08DesactivarApartado() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados(false);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_APARTADO);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_APARTADO_DESACTIVAR,
				bean2.getMensajesDeExito().get(0));
	}

	/**
	 * activar apartado .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA09ActivarApartado() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados(false);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_APARTADO);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_APARTADO_ACTIVAR,
				bean2.getMensajesDeExito().get(0));
	}

	/**
	 * desactivar bloque .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA10DesactivarBloque() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques(false);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_BLOQUE);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_BLOQUE_DESACTIVAR,
				bean2.getMensajesDeExito().get(0));
	}

	/**
	 * activar bloque .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA11ActivarBloque() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques(false);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_BLOQUE);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_BLOQUE_ACTIVAR,
				bean2.getMensajesDeExito().get(0));
	}

	/**
	 * desactivar item .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA12DesactivarItem() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerItems(false);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_ITEM);
		ItemBaremacion item = (ItemBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM, item.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_ITEM_DESACTIVAR,
				bean2.getMensajesDeExito().get(0));
	}

	/**
	 * activar item .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA13ActivarItem() throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerItems(false);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_ITEM);
		ItemBaremacion item = (ItemBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM, item.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_ITEM_ACTIVAR,
				bean2.getMensajesDeExito().get(0));
	}

	/**
	 * agregar apartado .
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA14agregarapartado() throws ServletException, IOException {
		// form

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO);
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		RespuestaHttp respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getVista(), ControladorItemsBaremacion.JSP_FORM_APARTADO_BAREMACION);
		assertEquals(bean.getApartadoBaremacion(), null);
		assertEquals(bean.getUltimoCodigo(), "");

		// form confirm
		List<ApartadoBaremacion> lista = desactivarTodosApartados();
		
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, "IV");
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE, "Test");
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_PORCENTAJEMAXIMO, "0.5");
		controlador = new ControladorItemsBaremacion();
		respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getMensajesDeExito().size(), 1);
		assertEquals(bean.getMensajesDeExito().get(0), ControladorItemsBaremacion.MENSAJE_EXITO_APARTADO_AGREGAR);
		
		desactivarApartadoPorCodigo("IV");
		activarApartadosLista(lista);
	}
	
	/**
	 * editar apartado .
	 * @throws SQLException 
	 * 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA15editarapartado() throws ServletException, IOException, SQLException {
		// form

		ApartadoBaremacion a = getApartadoBaremacionPorCodigo("IV");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_EDITAR_APARTADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, a.getCodNum().toString());		
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		RespuestaHttp respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getVista(), ControladorItemsBaremacion.JSP_FORM_APARTADO_BAREMACION);
		assertEquals(bean.getApartadoBaremacion(), a);
		assertEquals(bean.getUltimoCodigo(), ModeloBaremacionApartados.obtenerInstancia().getUltimoCodigoApartado());

		// form confirm
		List<ApartadoBaremacion> lista = desactivarTodosApartados();
		
		a = getApartadoBaremacionPorCodigo("IV");
		a.setCodigo("V");
		a.setNombre("BAR");
		a.setPorcentajeMaximo(0.7);
		
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_EDITAR_APARTADO_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, a.getCodNum().toString());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, a.getCodigo());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE, a.getNombre());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_PORCENTAJEMAXIMO, a.getPorcentajeMaximo().toString());
		controlador = new ControladorItemsBaremacion();
		respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getMensajesDeExito().size(), 1);
		assertEquals(bean.getMensajesDeExito().get(0), ControladorItemsBaremacion.MENSAJE_EXITO_APARTADO_EDITAR);
		
		desactivarApartadoPorCodigo("V");
		activarApartadosLista(lista);
	}
	
	/**
	 * agregar bloque .
	 * @throws NumberFormatException 
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA16agregarbloque() throws ServletException, IOException, NumberFormatException, SQLException {
		// form
		ApartadoBaremacion a = getApartadoBaremacionPorCodigo("V");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, a.getCodNum().toString());
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		RespuestaHttp respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getVista(), ControladorItemsBaremacion.JSP_FORM_BLOQUE_BAREMACION);
		assertEquals(bean.getApartadoBaremacion(), a);
		assertEquals(bean.getBloqueBaremacion(), null);
		String ultimoCodigo = (Integer.parseInt(ModeloBaremacionBloques.obtenerInstancia().getUltimoCodigoBloque(a)) + 1) + "";
		assertEquals(bean.getUltimoCodigo(), ultimoCodigo);

		// form confirm		
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, a.getCodNum().toString());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO, ultimoCodigo);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_NOMBRE, "TestBloque");
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_NUMEROMAXIMOMERITOS, "15");
		controlador = new ControladorItemsBaremacion();
		respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getMensajesDeExito().size(), 1);
		assertEquals(bean.getMensajesDeExito().get(0), ControladorItemsBaremacion.MENSAJE_EXITO_BLOQUE_AGREGAR);
		
		desactivarBloquePorCodigo(a, ultimoCodigo);
	}
	
	/**
	 * editar bloque .
	 * @throws UVException .
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testA16editarbloque() throws ServletException, IOException, SQLException, UVException {
		// form

		ApartadoBaremacion a = getApartadoBaremacionPorCodigo("V");
		BloqueBaremacion b = getBloqueBaremacionPorCodigo(a, "1");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, b.getCodNum().toString());		
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		RespuestaHttp respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getVista(), ControladorItemsBaremacion.JSP_FORM_BLOQUE_BAREMACION);
		assertEquals(bean.getApartadoBaremacion(), a);
		assertEquals(bean.getBloqueBaremacion(), b);
		assertEquals(bean.getUltimoCodigo(), ModeloBaremacionApartados.obtenerInstancia().getUltimoCodigoApartado());

		// form confirm
		b.setCodigo("2");
		b.setNombre("test editado");
		b.setNumeroMaximoMeritos(10);
		
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, b.getCodNum().toString());		
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO, b.getCodigo());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_NOMBRE, b.getNombre());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_NUMEROMAXIMOMERITOS, b.getNumeroMaximoMeritos().toString());
		controlador = new ControladorItemsBaremacion();
		respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(bean.getMensajesDeExito().size(), 1);
		assertEquals(bean.getMensajesDeExito().get(0), ControladorItemsBaremacion.MENSAJE_EXITO_BLOQUE_EDITAR);
		
		b = getBloqueBaremacionPorCodigo(a, "2");
		assertEquals(b.getCodigo(), "2");
		assertEquals(b.getNombre(), "test editado");
		assertEquals(b.getNumeroMaximoMeritos(), (Integer) 10);
	}
	
	/**
	 * obtener bloques sin apartado definido .
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE01ObtenerBloquesSinApartado() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_BLOQUES);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}

	/**
	 * obtener ítems sin bloque definido .
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE02ObtenerItemsSinBloque() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}

	/**
	 * seleccionar apartado nulo .
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE03SeleccionarApartadoNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_APARTADO_SELECCIONADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}

	/**
	 * seleccionar bloque nulo .
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE04SeleccionarBloqueNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_BLOQUE_SELECCIONADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}

	/**
	 * activar apartado nulo .
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE05ActivarApartadoNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_APARTADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}

	/**
	 * desactivar apartado nulo .
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE05DesactivarApartadoNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_APARTADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}

	/**
	 * permisos.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE06permisos() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_INDEX);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
		assertEquals(bean2.getMensajesDeError().get(0), ControladorItemsBaremacion.MENSAJE_ERROR_SIN_PERMISO_PERSONAL);
	}

	/**
	 * error fatal.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE07errorfaltal() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, "foo");
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);

		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas()
				.get(VistaItemsBaremacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
		assertEquals(bean2.getMensajesDeError().get(0), ControladorItemsBaremacion.MENSAJE_ERROR_ACCION_NO_DEFINIDA);
	}

	/**
	 * editar con errores apartado .
	 * @throws SQLException     .
	 * @throws ServletException .
	 * @throws IOException      .
	 */
	@Test
	public void testE08editarConErrores() throws ServletException, IOException, SQLException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, "");
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		RespuestaHttp respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(1, bean.getMensajesDeError().size());
		assertEquals(bean.getMensajesDeError().get(0), ControladorItemsBaremacion.MENSAJE_ERROR_CODIGO_VACIO);
		
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, "*".repeat(ModeloBaremacionApartados.COLUMN_CODIGO_MAXLENGTH + 1));
		controlador = new ControladorItemsBaremacion();
		respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(1, bean.getMensajesDeError().size());
		assertEquals(bean.getMensajesDeError().get(0), String.format(
				ControladorItemsBaremacion.MENSAJE_ERROR_CODIGO_MAXIMO, ModeloBaremacionItems.COLUMN_CODIGO_MAXLENGTH));
		
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, "foo");
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE, "");
		controlador = new ControladorItemsBaremacion();
		respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(1, bean.getMensajesDeError().size());
		assertEquals(bean.getMensajesDeError().get(0), ControladorItemsBaremacion.MENSAJE_ERROR_NOMBRE_VACIO);
		
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO_CONFIRM);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, "foo");
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE, "*".repeat(ModeloBaremacionApartados.COLUMN_NOMBRE_MAXLENGTH + 1));
		controlador = new ControladorItemsBaremacion();
		respuesta = new RespuestaHttp();
		controlador.doPost(peticion, respuesta);
		bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		assertEquals(1, bean.getMensajesDeError().size());
		assertEquals(bean.getMensajesDeError().get(0), String.format(ControladorItemsBaremacion.MENSAJE_ERROR_NOMBRE_MAXIMO, 
				ModeloBaremacionItems.COLUMN_NOMBRE_MAXLENGTH));
	}
	
	// UTILS

	private List<ApartadoBaremacion> desactivarTodosApartados() {
		List<ApartadoBaremacion> lista = null;

		try {
			lista = ModeloBaremacionApartados.obtenerInstancia().getApartadosActivos();
			for (ApartadoBaremacion a : lista) {
				desactivarApartado(a.getCodNum());
			}
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}

		return lista;
	}

	private void activarApartadosLista(List<ApartadoBaremacion> lista) {
		for (ApartadoBaremacion a : lista) {
			activarApartado(a.getCodNum());
		}
	}

	private void desactivarApartado(Integer codNum) {
		try {
			ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
			ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
			assertEquals(apartado.getCodNum(), codNum);
			modelo.desactivarApartado(apartado, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			apartado = modelo.getApartadoBaremacionById(codNum);
			assertFalse(apartado.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	private void activarApartado(Integer codNum) {
		try {
			ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
			ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
			assertEquals(apartado.getCodNum(), codNum);
			modelo.activarApartado(apartado, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			apartado = modelo.getApartadoBaremacionById(codNum);
			assertTrue(apartado.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	private void desactivarApartadoPorCodigo(String codigo) {
		try {
			ApartadoBaremacion a = getApartadoBaremacionPorCodigo(codigo);
			assertEquals(a.getCodigo(), codigo);

			a.setActivo(false);
			ModeloBaremacionApartados.obtenerInstancia().actualizaApartado(a,
					UtilsTestBolsaEmpleo.getUsuario("personal1"));
			ApartadoBaremacion apartado = getApartadoBaremacionPorCodigo(codigo);
			assertFalse(apartado.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	private ApartadoBaremacion getApartadoBaremacionPorCodigo(String codigo) {
		try {
			List<ApartadoBaremacion> lista = ModeloBaremacionApartados.obtenerInstancia().getApartados(null);
			for (ApartadoBaremacion a : lista) {
				if (a.getCodigo().equals(codigo)) {
					return a;
				}
			}
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}

		return null;
	}
	
	// método para obtener la vista con una lista de apartados .
	private VistaItemsBaremacion obtenerApartados(boolean activos) throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_APARTADOS);
		if (activos) {
			peticion.setParameter(BolsaEmpleoDataTable.PARAM_FILTER, "{'" + ModeloBaremacionApartados.ORDER_COLUMN_INDEX_APARTADOS_ACTIVO + "': 'true'}");
		}

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doGet(peticion, respuesta);
		return (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
	}

	// método para obtener la vista con una lista de bloques .
	private VistaItemsBaremacion obtenerBloques(boolean activos) throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados(true);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_BLOQUES);
		if (activos) {
			peticion.setParameter(BolsaEmpleoDataTable.PARAM_FILTER, "{'" + ModeloBaremacionBloques.ORDER_COLUMN_INDEX_BLOQUES_ACTIVO + "': 'true'}");
		}
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(2);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		return (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
	}

	// método para obtener la vista con una lista de ítems .
	private VistaItemsBaremacion obtenerItems(boolean activos) throws ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques(true);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_FILTER, "{'" + ModeloBaremacionItems.ORDER_COLUMN_INDEX_ITEMS_ACTIVO + "': 'true'}");
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		return (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
	}

	private BloqueBaremacion getBloqueBaremacionPorCodigo(ApartadoBaremacion a, String codigo) throws UVException {
		try {
			List<BloqueBaremacion> lista = ModeloBaremacionBloques.obtenerInstancia().getBloques(a, null);
			for (BloqueBaremacion b : lista) {
				if (b.getCodigo().equals(codigo)) {
					return b;
				}
			}
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}

		return null;
	}
	
	private void desactivarBloquePorCodigo(ApartadoBaremacion a, String codigo) {
		try {
			BloqueBaremacion b = getBloqueBaremacionPorCodigo(a, codigo);
			assertEquals(b.getCodigo(), codigo);

			b.setActivo(false);
			ModeloBaremacionBloques.obtenerInstancia().actualizaBloque(b, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			BloqueBaremacion b2 = getBloqueBaremacionPorCodigo(a, codigo);
			assertFalse(b2.isActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
}
