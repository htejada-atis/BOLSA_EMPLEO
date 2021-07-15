package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;

/** test bolsas candidato.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanBolsaSolicitud {	
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
		List<MeritoSolicitudTable> list = new ArrayList<>();
		
		BolsaSolicitud bolsaSolicitud = new BolsaSolicitud(bolsa, list);		
		bolsaSolicitud.setListaMeritos(list);
		
		assertEquals(bolsaSolicitud.getArea(), bolsa.getArea());
		assertEquals(bolsaSolicitud.getBaremable(), bolsa.getBaremable());
		assertEquals(bolsaSolicitud.getCodNum(), bolsa.getCodNum());
		assertEquals(bolsaSolicitud.getEstado(), bolsa.getEstado());		
		assertEquals(bolsaSolicitud.getFechaActualizacion(), bolsa.getFechaActualizacion());
		assertEquals(bolsaSolicitud.getFechaBloqueo(), bolsa.getFechaBloqueo());
		assertEquals(bolsaSolicitud.getFechaDesBloqueo(), bolsa.getFechaDesBloqueo());
		assertEquals(bolsaSolicitud.getListaMeritos(), list);
		assertEquals(bolsaSolicitud.getNumeroMeritos(), (Integer) 0);
		
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
		List<MeritoSolicitudTable> list = new ArrayList<>();
		
		Bolsa bolsa = new Bolsa(id, area, ESTADO, BAREAMABLE, FECHAATUALIZACION, FECHABLOQUEO, FECHADESBLOQUEO);
		BolsaSolicitud bolsa1 = new BolsaSolicitud();
		BolsaSolicitud bolsa2 = new BolsaSolicitud();
		BolsaSolicitud bolsa3 = new BolsaSolicitud(bolsa, list);
		    	
		assertTrue(bolsa1.equals(bolsa2));
		assertTrue(bolsa2.equals(bolsa1));
		assertTrue(bolsa1.equals(bolsa1));
		assertEquals(bolsa1.hashCode(), bolsa2.hashCode());
		assertFalse(bolsa1.equals(null));
		assertFalse(bolsa1.equals(bolsa3));
		assertFalse(bolsa3.equals(bolsa1));
		assertFalse(bolsa3.equals(new Object()));		
		assertNotEquals(bolsa1.hashCode(), bolsa3.hashCode());
		
		bolsa2.setListaMeritos(list);
		assertFalse(bolsa1.equals(bolsa2));
		assertFalse(bolsa2.equals(bolsa1));
	}
}
