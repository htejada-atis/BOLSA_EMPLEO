package unitarios.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;

/** test apartado.
 *
 */
public class TestBeanApartadoBaremacion {

	private static final String CADENA = "cadena";
	private static final Boolean BOOLEANO = true;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		apartado.setCodNum(id);
		apartado.setCodigo(CADENA);
		apartado.setNombre(CADENA);
		apartado.setActivo(BOOLEANO);
		assertEquals(id, apartado.getCodNum());
		assertEquals(CADENA, apartado.getCodigo());
		assertEquals(CADENA, apartado.getNombre());
		assertEquals(BOOLEANO, apartado.isActivo());
		assertNotNull(apartado.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		ApartadoBaremacion apartado = new ApartadoBaremacion(id, CADENA, CADENA, BOOLEANO);
		assertEquals(id, apartado.getCodNum());
		assertEquals(CADENA, apartado.getCodigo());
		assertEquals(CADENA, apartado.getNombre());
		assertEquals(BOOLEANO, apartado.isActivo());
		assertNotNull(apartado.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		Integer id = 1;
		ApartadoBaremacion apartado2 = new ApartadoBaremacion(id, CADENA, CADENA, BOOLEANO);
		ApartadoBaremacion apartado = new ApartadoBaremacion(apartado2);
		apartado.setCodNum(id);
		apartado.setCodigo(CADENA);
		apartado.setNombre(CADENA);
		apartado.setActivo(BOOLEANO);
		assertEquals(id, apartado.getCodNum());
		assertEquals(CADENA, apartado.getCodigo());
		assertEquals(CADENA, apartado.getNombre());
		assertEquals(BOOLEANO, apartado.isActivo());
		assertNotNull(apartado.toString());
		assertTrue(apartado.equals(apartado2));
		assertTrue(apartado.hashCode() == apartado2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {
		Integer id = 1;
    	ApartadoBaremacion apartado3 = new ApartadoBaremacion(id, CADENA, CADENA, BOOLEANO);
    	ApartadoBaremacion apartado2 = new ApartadoBaremacion();
    	ApartadoBaremacion apartado = new ApartadoBaremacion();
		assertTrue(apartado.equals(apartado2));
		assertTrue(apartado2.equals(apartado));
		assertEquals(apartado.hashCode(), apartado2.hashCode());
		assertFalse(apartado.equals(null));
		assertFalse(apartado.equals(apartado3));
		assertFalse(apartado3.equals(apartado));
		assertNotEquals(apartado.hashCode(), apartado3.hashCode());
	}
}
