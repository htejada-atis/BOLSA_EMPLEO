package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;

/** test fichero.
 *
 */
public class TestBEPBeanFichero {

	private static final String CADENA = "cadena";
	private static final Boolean BOOLEANO = true;
	private static final InputStream ARCHIVO = new ByteArrayInputStream("archivo de prueba".getBytes());
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Fichero fichero = new Fichero();
		fichero.setCodNum(id);
		fichero.setNombre(CADENA);
		fichero.setTitulo(CADENA);
		fichero.setArchivo(ARCHIVO);
		fichero.setPublico(BOOLEANO);
		assertEquals(id, fichero.getCodNum());
		assertEquals(CADENA, fichero.getNombre());
		assertEquals(CADENA, fichero.getTitulo());
		assertEquals(ARCHIVO, fichero.getArchivo());
		assertEquals(BOOLEANO, fichero.isPublico());
		assertNotNull(fichero.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
    	Fichero fichero2 = new Fichero(id, CADENA, CADENA, ARCHIVO, BOOLEANO);
    	Fichero fichero = new Fichero(fichero2);
    	fichero.setCodNum(id);
    	fichero.setNombre(CADENA);
    	fichero.setTitulo(CADENA);
    	fichero.setArchivo(ARCHIVO);
    	fichero.setPublico(BOOLEANO);
		assertEquals(id, fichero.getCodNum());
		assertEquals(CADENA, fichero.getNombre());
		assertEquals(CADENA, fichero.getTitulo());
		assertEquals(ARCHIVO, fichero.getArchivo());
		assertEquals(BOOLEANO, fichero.isPublico());
		assertNotNull(fichero.toString());
		assertTrue(fichero.equals(fichero2));
		assertTrue(fichero.hashCode() == fichero2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Integer id = 1;
    	Fichero fichero3 = new Fichero(id, CADENA, CADENA, ARCHIVO, BOOLEANO);
    	Fichero fichero2 = new Fichero();
    	Fichero fichero = new Fichero();
		assertTrue(fichero.equals(fichero2));
		assertTrue(fichero2.equals(fichero));
		assertEquals(fichero.hashCode(), fichero2.hashCode());
		assertFalse(fichero.equals(null));
		assertFalse(fichero.equals(fichero3));
		assertFalse(fichero3.equals(fichero));
		assertNotEquals(fichero.hashCode(), fichero3.hashCode());
	}
	
}
