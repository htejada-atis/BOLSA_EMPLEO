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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;

/** test bolsas candidato.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanBolsaSolicitudTable {	
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
		Integer numMeritos = 1;
		
		BolsaSolicitudTable bolsaSolicitud = new BolsaSolicitudTable(bolsa, numMeritos);		
		bolsaSolicitud.setNumeroMeritos(numMeritos);
		
		assertEquals(bolsaSolicitud.getArea(), bolsa.getArea());
		assertEquals(bolsaSolicitud.getBaremable(), bolsa.getBaremable());
		assertEquals(bolsaSolicitud.getCodNum(), bolsa.getCodNum());
		assertEquals(bolsaSolicitud.getEstado(), bolsa.getEstado());		
		assertEquals(bolsaSolicitud.getFechaActualizacion(), bolsa.getFechaActualizacion());
		assertEquals(bolsaSolicitud.getFechaBloqueo(), bolsa.getFechaBloqueo());
		assertEquals(bolsaSolicitud.getFechaDesBloqueo(), bolsa.getFechaDesBloqueo());
		assertEquals(bolsaSolicitud.getNumeroMeritos(), numMeritos);
		
		assertNotNull(bolsaSolicitud.toString());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA02() {
		Integer id = 1;
		Area area = new Area(1, "idexte", "descripcion");
		Integer numMeritos = 1;
		
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		BolsaSolicitudTable bolsa1 = new BolsaSolicitudTable();
		BolsaSolicitudTable bolsa2 = new BolsaSolicitudTable();
		BolsaSolicitudTable bolsa3 = new BolsaSolicitudTable(bolsa, numMeritos);
		    	
		assertTrue(bolsa1.equals(bolsa2));
		assertTrue(bolsa2.equals(bolsa1));
		assertTrue(bolsa1.equals(bolsa1));
		assertEquals(bolsa1.hashCode(), bolsa2.hashCode());
		assertFalse(bolsa1.equals(null));
		assertFalse(bolsa1.equals(bolsa3));
		assertFalse(bolsa3.equals(bolsa1));
		assertFalse(bolsa3.equals((Integer) 0));		
		assertNotEquals(bolsa1.hashCode(), bolsa3.hashCode());
		
		bolsa2.setNumeroMeritos(numMeritos);
		assertFalse(bolsa1.equals(bolsa2));
		assertFalse(bolsa2.equals(bolsa1));
	}
}
