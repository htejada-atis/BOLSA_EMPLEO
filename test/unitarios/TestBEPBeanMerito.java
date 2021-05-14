package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;

/** test merito.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanMerito {

	private static final String CADENA = "cadena";
	private static final Float FLOAT = 1.1f;
	private static final InputStream ARCHIVO = new ByteArrayInputStream("archivo de prueba".getBytes());
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		ItemBaremacion item = new ItemBaremacion();
		Merito merito = new Merito();
		merito.setCodNum(id);
		merito.setDescripcion(CADENA);
		merito.setObservacion(CADENA);
		merito.setValor(FLOAT);
		merito.setArchivo(ARCHIVO);
		merito.setItemBaremacion(item);
		assertEquals(id, merito.getCodNum());
		assertEquals(CADENA, merito.getDescripcion());
		assertEquals(CADENA, merito.getObservacion());
		assertEquals(FLOAT, merito.getValor());
		assertEquals(ARCHIVO, merito.getArchivo());
		assertEquals(item, merito.getItemBaremacion());
		assertNotNull(merito.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		ItemBaremacion item = new ItemBaremacion();
		Merito merito = new Merito(id, FLOAT, CADENA, CADENA, item, ARCHIVO);
		assertEquals(id, merito.getCodNum());
		assertEquals(CADENA, merito.getDescripcion());
		assertEquals(CADENA, merito.getObservacion());
		assertEquals(FLOAT, merito.getValor());
		assertEquals(ARCHIVO, merito.getArchivo());
		assertEquals(item, merito.getItemBaremacion());
		assertNotNull(merito.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		Integer id = 1;
		ItemBaremacion item = new ItemBaremacion();
		Merito merito2 = new Merito(id, FLOAT, CADENA, CADENA, item, ARCHIVO);
		Merito merito = new Merito(merito2);
		merito.setCodNum(id);
		merito.setDescripcion(CADENA);
		merito.setObservacion(CADENA);
		merito.setValor(FLOAT);
		merito.setArchivo(ARCHIVO);
		merito.setItemBaremacion(item);
		assertEquals(id, merito.getCodNum());
		assertEquals(CADENA, merito.getDescripcion());
		assertEquals(CADENA, merito.getObservacion());
		assertEquals(FLOAT, merito.getValor());
		assertEquals(ARCHIVO, merito.getArchivo());
		assertEquals(item, merito.getItemBaremacion());
		assertNotNull(merito.toString());
		assertTrue(merito.equals(merito2));
		assertTrue(merito.hashCode() == merito2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {
		Integer id = 1;
		ItemBaremacion item = new ItemBaremacion();
		Merito merito3 = new Merito(id, FLOAT, CADENA, CADENA, item, ARCHIVO);
		Merito merito2 = new Merito();
		Merito merito = new Merito();
		assertTrue(merito.equals(merito2));
		assertTrue(merito2.equals(merito));
		assertEquals(merito.hashCode(), merito2.hashCode());
		assertFalse(merito.equals(null));
		assertFalse(merito.equals(merito3));
		assertFalse(merito3.equals(merito));
		assertNotEquals(merito.hashCode(), merito3.hashCode());
	}
}
