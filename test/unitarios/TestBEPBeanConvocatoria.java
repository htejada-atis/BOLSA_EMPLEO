package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;

/** test convocatoria.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanConvocatoria {

	private static final String CADENA = "cadena";
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setEstado(CADENA);
    	java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		convocatoria.setFechaComision(ahora);
		convocatoria.setFechaLimite(ahora);
		convocatoria.setIdConvocatoria(id);
		convocatoria.setNombreConvocatoria(CADENA);
		convocatoria.setObservaciones(CADENA);
		assertEquals(CADENA, convocatoria.getEstado());
		assertEquals(ahora, convocatoria.getFechaComision());
		assertEquals(ahora, convocatoria.getFechaLimite());
		assertEquals(id, convocatoria.getIdConvocatoria());
		assertEquals(CADENA, convocatoria.getNombreConvocatoria());
		assertEquals(CADENA, convocatoria.getObservaciones());
		assertNotNull(convocatoria.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
    	java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Convocatoria convocatoria2 = new Convocatoria(id, CADENA, CADENA, CADENA, ahora, ahora);
		Convocatoria convocatoria = new Convocatoria(convocatoria2);
		convocatoria.setEstado(CADENA);
		convocatoria.setFechaComision(ahora);
		convocatoria.setFechaLimite(ahora);
		convocatoria.setIdConvocatoria(id);
		convocatoria.setNombreConvocatoria(CADENA);
		convocatoria.setObservaciones(CADENA);
		assertEquals(CADENA, convocatoria.getEstado());
		assertEquals(ahora, convocatoria.getFechaComision());
		assertEquals(ahora, convocatoria.getFechaLimite());
		assertEquals(id, convocatoria.getIdConvocatoria());
		assertEquals(CADENA, convocatoria.getNombreConvocatoria());
		assertEquals(CADENA, convocatoria.getObservaciones());
		assertNotNull(convocatoria.toString());
		assertTrue(convocatoria.equals(convocatoria2));
		assertTrue(convocatoria.hashCode() == convocatoria2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Integer id = 1;
    	java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Convocatoria convocatoria3 = new Convocatoria(id, CADENA, CADENA, CADENA, ahora, ahora);
		Convocatoria convocatoria2 = new Convocatoria();
		Convocatoria convocatoria = new Convocatoria();
		assertTrue(convocatoria.equals(convocatoria2));
		assertTrue(convocatoria2.equals(convocatoria));
		assertEquals(convocatoria.hashCode(), convocatoria2.hashCode());
		assertFalse(convocatoria.equals(null));
		assertFalse(convocatoria.equals(convocatoria3));
		assertFalse(convocatoria3.equals(convocatoria));
		assertNotEquals(convocatoria.hashCode(), convocatoria3.hashCode());
	}
}
