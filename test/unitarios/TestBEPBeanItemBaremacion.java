package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;

/** test apartado.
 *
 */
public class TestBEPBeanItemBaremacion {

	private static final String CADENA = "cadena";
	private static final Boolean BOOLEANO = true;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		BloqueBaremacion bloque = new BloqueBaremacion();
		ItemBaremacion item = new ItemBaremacion();
		item.setCodNum(id);
		item.setCodigo(CADENA);
		item.setNombre(CADENA);
		item.setActivo(BOOLEANO);
		item.setBloqueBaremacion(bloque);
		assertEquals(id, item.getCodNum());
		assertEquals(CADENA, item.getCodigo());
		assertEquals(CADENA, item.getNombre());
		assertEquals(BOOLEANO, item.getActivo());
		assertEquals(bloque, item.getBloqueBaremacion());
		assertNotNull(item.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
//		Integer id = 1;
//		BloqueBaremacion bloque = new BloqueBaremacion();
//		ItemBaremacion item = new ItemBaremacion(id, bloque, CADENA, CADENA, BOOLEANO);
//		assertEquals(id, item.getCodNum());
//		assertEquals(CADENA, item.getCodigo());
//		assertEquals(CADENA, item.getNombre());
//		assertEquals(BOOLEANO, item.getActivo());
//		assertEquals(bloque, item.getBloqueBaremacion());
//		assertNotNull(item.toString());
		assertTrue(false);
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
//		Integer id = 1;
//		BloqueBaremacion bloque = new BloqueBaremacion();
//		ItemBaremacion item2 = new ItemBaremacion(id, bloque, CADENA, CADENA, BOOLEANO);
//		ItemBaremacion item = new ItemBaremacion(item2);
//		item.setCodNum(id);
//		item.setCodigo(CADENA);
//		item.setNombre(CADENA);
//		item.setActivo(BOOLEANO);
//		assertEquals(id, item.getCodNum());
//		assertEquals(CADENA, item.getCodigo());
//		assertEquals(CADENA, item.getNombre());
//		assertEquals(BOOLEANO, item.getActivo());
//		assertNotNull(item.toString());
//		assertTrue(item.equals(item2));
//		assertTrue(item.hashCode() == item2.hashCode());
		assertTrue(false);
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {
//		Integer id = 1;
//		BloqueBaremacion bloque = new BloqueBaremacion();
//		ItemBaremacion item3 = new ItemBaremacion(id, bloque, CADENA, CADENA, BOOLEANO);
//		ItemBaremacion item2 = new ItemBaremacion();
//		ItemBaremacion item = new ItemBaremacion();
//		assertTrue(item.equals(item2));
//		assertTrue(item2.equals(item));
//		assertEquals(item.hashCode(), item2.hashCode());
//		assertFalse(item.equals(null));
//		assertFalse(item.equals(item3));
//		assertFalse(item3.equals(item));
//		assertNotEquals(item.hashCode(), item3.hashCode());
		assertTrue(false);
	}
}
