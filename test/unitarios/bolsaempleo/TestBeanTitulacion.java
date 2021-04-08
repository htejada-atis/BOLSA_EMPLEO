package unitarios.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;

/** test titulación.
 *
 */
public class TestBeanTitulacion {
	
	private static final String NOMBRE = "nombre";
		
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Titulacion titulacion = new Titulacion(id);
		
		titulacion.setCodNum(id);
		titulacion.setNombre(NOMBRE);
		
		assertEquals(id, titulacion.getCodNum());
		assertEquals(NOMBRE, titulacion.getNombre());
		assertNotNull(titulacion.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		Titulacion titulacion = new Titulacion(NOMBRE);
		
		assertEquals(NOMBRE, titulacion.getNombre());
		assertNotNull(titulacion.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA03() {
		Integer id = 1;
		Titulacion titulacion = new Titulacion(id);
		
		assertEquals(id, titulacion.getCodNum());
		assertNotNull(titulacion.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA04() {
		Integer id = 1;
		
		Titulacion titulacion = new Titulacion(id, NOMBRE);
		Titulacion titulacion2 = new Titulacion(titulacion);
		
		assertEquals(id, titulacion.getCodNum());
		assertEquals(id, titulacion2.getCodNum());
		assertEquals(NOMBRE, titulacion.getNombre());
		assertEquals(NOMBRE, titulacion2.getNombre());
		
		assertNotNull(titulacion.toString());
		assertNotNull(titulacion2.toString());
		assertTrue(titulacion.equals(titulacion2));
		assertTrue(titulacion.hashCode() == titulacion2.hashCode());		
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA05() {
		Integer id = 1;
		
		Titulacion titulacion = new Titulacion();
		Titulacion titulacion2 = new Titulacion();
		Titulacion titulacion3 = new Titulacion(id, NOMBRE);
		    	
		assertTrue(titulacion.equals(titulacion2));
		assertTrue(titulacion2.equals(titulacion));		
		assertEquals(titulacion.hashCode(), titulacion2.hashCode());
		assertFalse(titulacion.equals(null));
		assertFalse(titulacion.equals(titulacion3));
		assertFalse(titulacion3.equals(titulacion));
		assertNotEquals(titulacion.hashCode(), titulacion3.hashCode());
	}
}
