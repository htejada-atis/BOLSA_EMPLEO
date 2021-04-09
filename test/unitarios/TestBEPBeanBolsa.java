package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Departamento;

/** test bolsas.
 *
 */
public class TestBEPBeanBolsa {	
	private static final String ESTADO = "BLOQUEADA";
	private static final Boolean BAREAMABLE = true;
	private static final Date FECHAATUALIZACION = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Date FECHABLOQUEO = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Date FECHADESBLOQUEO = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;		
		Departamento dep = new Departamento(1, "DEP1", "Fisica");
		Area area = new Area(1, dep, "idexte", "idsec", "descripcion");
		Bolsa bolsa = new Bolsa();
				
		bolsa.setCodNum(id);
		bolsa.setArea(area);
		bolsa.setEstado(ESTADO);
		bolsa.setBaremable(BAREAMABLE);
		bolsa.setFechaActualizacion(FECHAATUALIZACION);
		bolsa.setFechaBloqueo(FECHABLOQUEO);
		bolsa.setFechaDesBloqueo(FECHADESBLOQUEO);
				
		assertEquals(id, bolsa.getCodNum());
		assertEquals(ESTADO, bolsa.getEstado());
		assertEquals(BAREAMABLE, bolsa.getBaremable());
		assertEquals(FECHAATUALIZACION, bolsa.getFechaActualizacion());
		assertEquals(FECHABLOQUEO, bolsa.getFechaBloqueo());
		assertEquals(FECHADESBLOQUEO, bolsa.getFechaDesBloqueo());
		assertEquals(area, bolsa.getArea());
		
		assertNotNull(bolsa.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		Departamento dep = new Departamento(1, "DEP1", "Fisica");
		Area area = new Area(1, dep, "idexte", "idsec", "descripcion");
		
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		Bolsa bolsa2 = new Bolsa(bolsa);
		
		assertEquals(id, bolsa.getCodNum());
		assertEquals(id, bolsa2.getCodNum());
		assertEquals(ESTADO, bolsa.getEstado());
		assertEquals(ESTADO, bolsa2.getEstado());
		assertEquals(BAREAMABLE, bolsa.getBaremable());
		assertEquals(BAREAMABLE, bolsa2.getBaremable());		
		assertEquals(FECHAATUALIZACION, bolsa.getFechaActualizacion());
		assertEquals(FECHAATUALIZACION, bolsa2.getFechaActualizacion());
		assertEquals(FECHABLOQUEO, bolsa.getFechaBloqueo());
		assertEquals(FECHABLOQUEO, bolsa2.getFechaBloqueo());
		assertEquals(FECHADESBLOQUEO, bolsa.getFechaDesBloqueo());
		assertEquals(FECHADESBLOQUEO, bolsa2.getFechaDesBloqueo());
		assertEquals(area, bolsa.getArea());
		assertEquals(area, bolsa2.getArea());
		
		assertNotNull(bolsa.toString());
		assertNotNull(bolsa2.toString());
		assertTrue(bolsa.equals(bolsa2));
		assertTrue(bolsa.hashCode() == bolsa2.hashCode());		
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Integer id = 1;
		Departamento dep = new Departamento(1, "DEP1", "Fisica");
		Area area = new Area(1, dep, "idexte", "idsec", "descripcion");
		
		Bolsa bolsa = new Bolsa();
		Bolsa bolsa2 = new Bolsa();
		Bolsa bolsa3 = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		    	
		assertTrue(bolsa.equals(bolsa2));
		assertTrue(bolsa2.equals(bolsa));		
		assertEquals(bolsa.hashCode(), bolsa2.hashCode());
		assertFalse(bolsa.equals(null));
		assertFalse(bolsa.equals(bolsa3));
		assertFalse(bolsa3.equals(bolsa));
		assertNotEquals(bolsa.hashCode(), bolsa3.hashCode());
	}
}
