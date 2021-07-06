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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;

/** test bolsas candidato.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanBolsaValidacion {	
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
				
		BolsaValidacion bolsaValidacion = new BolsaValidacion(bolsa);		
		
		assertEquals(bolsaValidacion.getArea(), bolsa.getArea());
		assertEquals(bolsaValidacion.getBaremable(), bolsa.getBaremable());
		assertEquals(bolsaValidacion.getCodNum(), bolsa.getCodNum());
		assertEquals(bolsaValidacion.getEstado(), bolsa.getEstado());		
		assertEquals(bolsaValidacion.getFechaActualizacion(), bolsa.getFechaActualizacion());
		assertEquals(bolsaValidacion.getFechaBloqueo(), bolsa.getFechaBloqueo());
		assertEquals(bolsaValidacion.getFechaDesBloqueo(), bolsa.getFechaDesBloqueo());
		
		assertNotNull(bolsaValidacion.toString());
		
		Integer pmeritosNoValidados = 1;
		Integer pmeritosValidados = 2;
		Integer pmeritosExcluidos = pmeritosValidados * 2;
		Integer ptotalMeritos = pmeritosExcluidos * 2;
		
		bolsaValidacion = new BolsaValidacion(bolsa, pmeritosNoValidados, pmeritosValidados, pmeritosExcluidos, ptotalMeritos);
		assertEquals(bolsaValidacion.getTotalMeritosNoValidados(), pmeritosNoValidados);
		assertEquals(bolsaValidacion.getTotalMeritosValidados(), pmeritosValidados);
		assertEquals(bolsaValidacion.getTotalMeritosExcluidos(), pmeritosExcluidos);
		assertEquals(bolsaValidacion.getTotalMeritos(), ptotalMeritos);
		
		pmeritosNoValidados = pmeritosNoValidados * 2;
		pmeritosValidados = pmeritosNoValidados * 2;
		pmeritosExcluidos = pmeritosValidados * 2;
		ptotalMeritos = pmeritosExcluidos * 2;
		
		bolsaValidacion.setTotalMeritosNoValidados(pmeritosNoValidados);
		bolsaValidacion.setTotalMeritosValidados(pmeritosValidados);
		bolsaValidacion.setTotalMeritosExcluidos(pmeritosExcluidos);
		bolsaValidacion.setTotalMeritos(ptotalMeritos);
		assertEquals(bolsaValidacion.getTotalMeritosNoValidados(), pmeritosNoValidados);
		assertEquals(bolsaValidacion.getTotalMeritosValidados(), pmeritosValidados);
		assertEquals(bolsaValidacion.getTotalMeritosExcluidos(), pmeritosExcluidos);
		assertEquals(bolsaValidacion.getTotalMeritos(), ptotalMeritos);
		
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		Area area = new Area(1, "idexte", "descripcion");
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		Integer pmeritosNoValidados = 1;
		Integer pmeritosValidados = 2;
		Integer pmeritosExcluidos = pmeritosValidados * 2;
		Integer ptotalMeritos = pmeritosExcluidos * 2;
		
		BolsaValidacion b1 = new BolsaValidacion(bolsa, pmeritosNoValidados, pmeritosValidados, pmeritosExcluidos, ptotalMeritos);
		BolsaValidacion b2 = new BolsaValidacion(b1);
		
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
		assertEquals(pmeritosNoValidados, b1.getTotalMeritosNoValidados());
		assertEquals(pmeritosNoValidados, b2.getTotalMeritosNoValidados());
		assertEquals(pmeritosValidados, b1.getTotalMeritosValidados());
		assertEquals(pmeritosValidados, b2.getTotalMeritosValidados());
		assertEquals(pmeritosExcluidos, b1.getTotalMeritosExcluidos());
		assertEquals(pmeritosExcluidos, b2.getTotalMeritosExcluidos());
		assertEquals(ptotalMeritos, b1.getTotalMeritos());
		assertEquals(ptotalMeritos, b2.getTotalMeritos());
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
		Integer pmeritosNoValidados = 1;
		Integer pmeritosValidados = 2;
		Integer pmeritosExcluidos = pmeritosValidados * 2;
		Integer ptotalMeritos = pmeritosExcluidos * 2;
		
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		BolsaValidacion bolsa1 = new BolsaValidacion();
		BolsaValidacion bolsa2 = new BolsaValidacion();
		BolsaValidacion bolsa3 = new BolsaValidacion(bolsa, pmeritosNoValidados, pmeritosValidados, pmeritosExcluidos, ptotalMeritos);
		    	
		assertTrue(bolsa1.equals(bolsa2));
		assertTrue(bolsa2.equals(bolsa1));
		assertTrue(bolsa1.equals(bolsa1));
		assertEquals(bolsa1.hashCode(), bolsa2.hashCode());
		assertFalse(bolsa1.equals(null));
		assertFalse(bolsa1.equals(bolsa3));
		assertFalse(bolsa3.equals(bolsa1));
		assertFalse(bolsa3.equals((Integer) 0));
		assertNotEquals(bolsa1.hashCode(), bolsa3.hashCode());
		
		bolsa2.setTotalMeritosNoValidados(pmeritosNoValidados);
		assertFalse(bolsa1.equals(bolsa2));
		assertFalse(bolsa2.equals(bolsa1));
		
		bolsa2.setTotalMeritosNoValidados(null);		
		bolsa2.setTotalMeritosValidados(pmeritosValidados);
		assertFalse(bolsa1.equals(bolsa2));
		assertFalse(bolsa2.equals(bolsa1));
		
		bolsa2.setTotalMeritosValidados(null);
		bolsa2.setTotalMeritosExcluidos(pmeritosExcluidos);
		assertFalse(bolsa1.equals(bolsa2));
		assertFalse(bolsa2.equals(bolsa1));
		
		bolsa2.setTotalMeritosExcluidos(null);
		bolsa2.setTotalMeritos(ptotalMeritos);
		assertFalse(bolsa1.equals(bolsa2));
		assertFalse(bolsa2.equals(bolsa1));		
	}
}
