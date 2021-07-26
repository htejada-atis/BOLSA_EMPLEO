package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;

/** test bolsas candidato.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanBolsaCandidato {	
	private static final String ESTADO = "BLOQUEADA";
	private static final Boolean BAREAMABLE = true;
	private static final Date FECHAATUALIZACION = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Date FECHABLOQUEO = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Date FECHADESBLOQUEO = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
		
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Area area = new Area(1, "idexte", "descripcion");
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		Boolean excluido = false; 
		BolsaCandidato bolsaCandidato = new BolsaCandidato(bolsa, excluido);
		
		excluido = true;
		bolsaCandidato.setExcluido(excluido);
		
		assertEquals(bolsaCandidato.getArea(), bolsa.getArea());
		assertEquals(bolsaCandidato.getBaremable(), bolsa.getBaremable());
		assertEquals(bolsaCandidato.getCodNum(), bolsa.getCodNum());
		assertEquals(bolsaCandidato.getEstado(), bolsa.getEstado());
		assertEquals(bolsaCandidato.getExcluido(), excluido);
		assertEquals(bolsaCandidato.getFechaActualizacion(), bolsa.getFechaActualizacion());
		assertEquals(bolsaCandidato.getFechaBloqueo(), bolsa.getFechaBloqueo());
		assertEquals(bolsaCandidato.getFechaDesBloqueo(), bolsa.getFechaDesBloqueo());
		
		assertNotNull(bolsaCandidato.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		Area area = new Area(1, "idexte", "descripcion");
		Boolean excluido = true;
		
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		BolsaCandidato b1 = new BolsaCandidato(bolsa, excluido);
		BolsaCandidato b2 = new BolsaCandidato(b1);
		
		assertEquals(id, b1.getCodNum());
		assertEquals(id, b2.getCodNum());
		assertEquals(ESTADO, b1.getEstado());
		assertEquals(ESTADO, b2.getEstado());
		assertEquals(BAREAMABLE, b1.getBaremable());
		assertEquals(BAREAMABLE, b2.getBaremable());
		assertEquals(FECHAATUALIZACION, b1.getFechaActualizacion());
		assertEquals(FECHAATUALIZACION, b2.getFechaActualizacion());
		assertEquals(FECHABLOQUEO, b1.getFechaBloqueo());
		assertEquals(FECHABLOQUEO, b2.getFechaBloqueo());
		assertEquals(FECHADESBLOQUEO, b1.getFechaDesBloqueo());
		assertEquals(FECHADESBLOQUEO, b2.getFechaDesBloqueo());
		assertEquals(excluido, b1.getExcluido());
		assertEquals(excluido, b2.getExcluido());
		assertEquals(area, b1.getArea());
		assertEquals(area, b2.getArea());
		
		assertNotNull(b1.toString());
		assertNotNull(b2.toString());
		assertTrue(b1.equals(b2));
		assertTrue(b1.hashCode() == b2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Integer id = 1;
		Area area = new Area(1, "idexte", "descripcion");
		Boolean excluido = false;
		
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		BolsaCandidato bolsa1 = new BolsaCandidato();
		BolsaCandidato bolsa2 = new BolsaCandidato();
		BolsaCandidato bolsa3 = new BolsaCandidato(bolsa, excluido);
		    	
		assertTrue(bolsa1.equals(bolsa1));
		assertTrue(bolsa1.equals(bolsa2));
		assertTrue(bolsa2.equals(bolsa1));		
		assertEquals(bolsa1.hashCode(), bolsa2.hashCode());
		assertFalse(bolsa1.equals(null));
		assertFalse(bolsa1.equals(bolsa3));
		assertFalse(bolsa3.equals(bolsa1));
		assertFalse(bolsa3.equals(new Object()));
		assertNotEquals(bolsa1.hashCode(), bolsa3.hashCode());
		
		bolsa2.setExcluido(true);
		assertFalse(bolsa1.equals(bolsa2));
		assertFalse(bolsa2.equals(bolsa1));
	}
}
