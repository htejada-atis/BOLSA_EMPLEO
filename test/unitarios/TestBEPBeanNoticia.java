package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import org.junit.Test;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;

/** test noticia.
 *
 */
public class TestBEPBeanNoticia {

	private static final String CADENA = "cadena";
	private static final Boolean BOOLEANO = true;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Noticia noticia = new Noticia();
		noticia.setCodNum(id);
		noticia.setEnlace(CADENA);
    	java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    	noticia.setTexto(CADENA);
		noticia.setFecha(ahora);
		noticia.setPublica(BOOLEANO);
		noticia.setActiva(BOOLEANO);
		assertEquals(id, noticia.getCodNum());
		assertEquals(CADENA, noticia.getEnlace());
		assertEquals(CADENA, noticia.getTexto());
		assertEquals(ahora, noticia.getFecha());
		assertEquals(BOOLEANO, noticia.isPublica());
		assertEquals(BOOLEANO, noticia.isActiva());
		assertNotNull(noticia.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Noticia noticia = new Noticia(CADENA, CADENA, ahora, BOOLEANO, BOOLEANO);
		assertEquals(CADENA, noticia.getEnlace());
		assertEquals(CADENA, noticia.getTexto());
		assertEquals(ahora, noticia.getFecha());
		assertEquals(BOOLEANO, noticia.isPublica());
		assertEquals(BOOLEANO, noticia.isActiva());
		assertNotNull(noticia.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		Integer id = 1;
    	java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Noticia noticia2 = new Noticia(id, CADENA, CADENA, ahora, BOOLEANO, BOOLEANO);
		Noticia noticia = new Noticia(noticia2);
		noticia.setCodNum(id);
		noticia.setEnlace(CADENA);
		noticia.setTexto(CADENA);
		noticia.setFecha(ahora);
		noticia.setPublica(BOOLEANO);
		noticia.setActiva(BOOLEANO);
		assertEquals(id, noticia.getCodNum());
		assertEquals(CADENA, noticia.getEnlace());
		assertEquals(CADENA, noticia.getTexto());
		assertEquals(ahora, noticia.getFecha());
		assertEquals(BOOLEANO, noticia.isPublica());
		assertEquals(BOOLEANO, noticia.isActiva());
		assertNotNull(noticia.toString());
		assertTrue(noticia.equals(noticia2));
		assertTrue(noticia.hashCode() == noticia2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {
		Integer id = 1;
    	java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    	Noticia noticia3 = new Noticia(id, CADENA, CADENA, ahora, BOOLEANO, BOOLEANO);
    	Noticia noticia2 = new Noticia();
    	Noticia noticia = new Noticia();
		assertTrue(noticia.equals(noticia2));
		assertTrue(noticia2.equals(noticia));
		assertEquals(noticia.hashCode(), noticia2.hashCode());
		assertFalse(noticia.equals(null));
		assertFalse(noticia.equals(noticia3));
		assertFalse(noticia3.equals(noticia));
		assertNotEquals(noticia.hashCode(), noticia3.hashCode());
	}
}
