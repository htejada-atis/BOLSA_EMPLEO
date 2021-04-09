package controlador.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion;

/** test controlador items baremación.
 * @author jmoral
 *
 */
public class TestControladorItemsBaremacion {
	
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
	
	private static final String CODIGO = "0";
	private static final String CODIGO_2 = "99";
	private static final String NOMBRE = "nombre";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en io
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de apartados .
    private VistaItemsBaremacion obtenerApartados() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_APARTADOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doGet(peticion, respuesta);
		return (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
    }
    
    // método para obtener la vista con una lista de bloques .
    private VistaItemsBaremacion obtenerBloques() throws ServletException, IOException {
    	VistaItemsBaremacion bean = obtenerApartados();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_BLOQUES);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
    	
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		return (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
    }
    
 // método para obtener la vista con una lista de ítems .
    private VistaItemsBaremacion obtenerItems() throws ServletException, IOException {
    	VistaItemsBaremacion bean = obtenerBloques();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS);
		BloqueBaremacion apartado = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, apartado.getCodNum().toString());
    	
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		return (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
    }
    
    /** Obtener items baremación, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doGet(peticion, respuesta);
		
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable apartados .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA02ObtenerApartados() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados();
		
		assertNotEquals(MENSAJE_APARTADOS_DEVUELTOS, 0, bean.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable bloques .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA03ObtenerBloques() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques();
		
		assertNotEquals(MENSAJE_BLOQUES_DEVUELTOS, 0, bean.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable items .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA04ObtenerItems() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());
    	
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertNotEquals(MENSAJE_ITEMS_DEVUELTOS, 0, bean2.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** seleccionar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException . 
	 */
	@Test
	public void testA05SeleccionarApartado() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
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
	
	/** seleccionar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA06SeleccionarBloque() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
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
	
	/** activar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA07ActivarApartado() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_APARTADO);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_ACTIVAR_APARTADO, bean2.getMensajesDeExito().get(0));
	}
	
	/** activar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA08ActivarBloque() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_BLOQUE);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_ACTIVAR_BLOQUE, bean2.getMensajesDeExito().get(0));
	}
	
	/** activar item .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA09ActivarItem() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerItems();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_ITEM);
		ItemBaremacion item = (ItemBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM, item.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_ACTIVAR_ITEM, bean2.getMensajesDeExito().get(0));
	}
	
	/** desactivar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA10DesactivarApartado() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_APARTADO);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_DESACTIVAR_APARTADO, bean2.getMensajesDeExito().get(0));
	}
	
	/** desactivar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA11DesactivarBloque() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_BLOQUE);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_DESACTIVAR_BLOQUE, bean2.getMensajesDeExito().get(0));
	}
	
	/** desactivar item .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA12DesactivarItem() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerItems();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_ITEM);
		ItemBaremacion item = (ItemBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM, item.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_DESACTIVAR_ITEM, bean2.getMensajesDeExito().get(0));
	}
	
	/** editar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA13EditarApartado() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_EDITAR_APARTADO);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, CODIGO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE, NOMBRE);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertNotNull(MENSAJE_APARTADO_DEVUELTO, bean2.getApartadoBaremacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_EDITAR_APARTADO, bean2.getMensajesDeExito().get(0));
	}
	
	/** editar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA14EditarBloque() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE);
		BloqueBaremacion apartado = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, apartado.getCodNum().toString());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO, CODIGO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_NOMBRE, NOMBRE);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertNotNull(MENSAJE_BLOQUE_DEVUELTO, bean2.getBloqueBaremacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_EDITAR_BLOQUE, bean2.getMensajesDeExito().get(0));
	}
	
	/** editar ítem .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA15EditarItem() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerItems();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_EDITAR_ITEM);
		ItemBaremacion apartado = (ItemBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM, apartado.getCodNum().toString());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM_CODIGO, CODIGO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM_NOMBRE, NOMBRE);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertNotNull(MENSAJE_ITEM_DEVUELTO, bean2.getItemBaremacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_EDITAR_ITEM, bean2.getMensajesDeExito().get(0));
	}
	
	/** agregar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA16AgregarApartado() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, CODIGO_2);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE, NOMBRE);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_AGREGAR_APARTADO, bean.getMensajesDeExito().get(0));
	}
	
	/** agregar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA17AgregarBloque() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerApartados();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE);
		ApartadoBaremacion apartado = (ApartadoBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, apartado.getCodNum().toString());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO, CODIGO_2);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE_NOMBRE, NOMBRE);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_AGREGAR_BLOQUE, bean2.getMensajesDeExito().get(0));
	}
	
	/** agregar ítem .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA18AgregarItem() throws SQLException, ServletException, IOException {
		VistaItemsBaremacion bean = obtenerBloques();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_AGREGAR_ITEM);
		BloqueBaremacion bloque = (BloqueBaremacion) bean.getDatatable().getData().get(0);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, bloque.getCodNum().toString());
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM_CODIGO, CODIGO_2);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ITEM_NOMBRE, NOMBRE);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorItemsBaremacion.MENSAJE_EXITO_AGREGAR_ITEM, bean2.getMensajesDeExito().get(0));
	}
	
	/** obtener bloques sin apartado definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01ObtenerBloquesSinApartado() throws SQLException, ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_BLOQUES);
    	
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** obtener ítems sin bloque definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE02ObtenerItemsSinBloque() throws SQLException, ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS);
    	
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** seleccionar apartado nulo .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE03SeleccionarApartadoNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_APARTADO_SELECCIONADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** seleccionar bloque nulo .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE04SeleccionarBloqueNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_BLOQUE_SELECCIONADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_BLOQUE, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** activar apartado nulo .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE05ActivarApartadoNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_ACTIVAR_APARTADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** desactivar apartado nulo .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE05DesactivarApartadoNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorItemsBaremacion.PARAM_ACCION, ControladorItemsBaremacion.ACCION_DESACTIVAR_APARTADO);
		peticion.setParameter(ControladorItemsBaremacion.PARAM_APARTADO, null);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorItemsBaremacion controlador = new ControladorItemsBaremacion();
		controlador.doPost(peticion, respuesta);
		
		VistaItemsBaremacion bean2 = (VistaItemsBaremacion) peticion.getUVDatos().getVistas().get(VistaItemsBaremacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
