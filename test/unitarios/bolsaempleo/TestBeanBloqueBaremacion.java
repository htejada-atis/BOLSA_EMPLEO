package unitarios.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;

/** test bloque.
 *
 */
public class TestBeanBloqueBaremacion {

	private static final String CADENA = "cadena";
	private static final Boolean BOOLEANO = true;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		BloqueBaremacion bloque = new BloqueBaremacion();
		bloque.setCodNum(id);
		bloque.setCodigo(CADENA);
		bloque.setNombre(CADENA);
		bloque.setActivo(BOOLEANO);
		bloque.setApartadoBaremacion(apartado);
		assertEquals(id, bloque.getCodNum());
		assertEquals(CADENA, bloque.getCodigo());
		assertEquals(CADENA, bloque.getNombre());
		assertEquals(BOOLEANO, bloque.isActivo());
		assertEquals(apartado, bloque.getApartadoBaremacion());
		assertNotNull(bloque.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		BloqueBaremacion bloque = new BloqueBaremacion(id, apartado, CADENA, CADENA, BOOLEANO);
		assertEquals(id, bloque.getCodNum());
		assertEquals(CADENA, bloque.getCodigo());
		assertEquals(CADENA, bloque.getNombre());
		assertEquals(BOOLEANO, bloque.isActivo());
		assertEquals(apartado, bloque.getApartadoBaremacion());
		assertNotNull(bloque.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		Integer id = 1;
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		BloqueBaremacion bloque2 = new BloqueBaremacion(id, apartado, CADENA, CADENA, BOOLEANO);
		BloqueBaremacion bloque = new BloqueBaremacion(bloque2);
		bloque.setCodNum(id);
		bloque.setCodigo(CADENA);
		bloque.setNombre(CADENA);
		bloque.setActivo(BOOLEANO);
		assertEquals(id, bloque.getCodNum());
		assertEquals(CADENA, bloque.getCodigo());
		assertEquals(CADENA, bloque.getNombre());
		assertEquals(BOOLEANO, bloque.isActivo());
		assertNotNull(bloque.toString());
		assertTrue(bloque.equals(bloque2));
		assertTrue(bloque.hashCode() == bloque2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {
		Integer id = 1;
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		BloqueBaremacion bloque3 = new BloqueBaremacion(id, apartado, CADENA, CADENA, BOOLEANO);
		BloqueBaremacion bloque2 = new BloqueBaremacion();
		BloqueBaremacion bloque = new BloqueBaremacion();
		assertTrue(bloque.equals(bloque2));
		assertTrue(bloque2.equals(bloque));
		assertEquals(bloque.hashCode(), bloque2.hashCode());
		assertFalse(bloque.equals(null));
		assertFalse(bloque.equals(bloque3));
		assertFalse(bloque3.equals(bloque));
		assertNotEquals(bloque.hashCode(), bloque3.hashCode());
	}
}
