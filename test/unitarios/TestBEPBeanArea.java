package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;

/** test areas.
 *
 */
public class TestBEPBeanArea {
	
	private static final String ID_AREA_EXTERNO = "area externo";
	private static final String DESCRIPCION = "mi area";
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Area area = new Area();
		
		area.setCodNum(id);
		area.setDescripcion(DESCRIPCION);
		area.setIdAreaExterno(ID_AREA_EXTERNO);
				
		assertEquals(id, area.getCodNum());
		assertEquals(DESCRIPCION, area.getDescripcion());
		assertEquals(ID_AREA_EXTERNO, area.getIdAreaExterno());
		
		assertNotNull(area.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		Area area = new Area(id, ID_AREA_EXTERNO, DESCRIPCION);
		Area area2 = new Area(area);
		
		assertEquals(id, area.getCodNum());
		assertEquals(id, area2.getCodNum());
		assertEquals(ID_AREA_EXTERNO, area.getIdAreaExterno());
		assertEquals(ID_AREA_EXTERNO, area2.getIdAreaExterno());
		assertEquals(DESCRIPCION, area.getDescripcion());
		assertEquals(DESCRIPCION, area2.getDescripcion());
		
		assertNotNull(area.toString());
		assertNotNull(area2.toString());
		assertTrue(area.equals(area2));
		assertTrue(area.hashCode() == area2.hashCode());		
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Integer id = 1;
		
		Area area = new Area();
		Area area2 = new Area();
		Area area3 = new Area(id, ID_AREA_EXTERNO, DESCRIPCION);
		    	
		assertTrue(area.equals(area2));
		assertTrue(area2.equals(area));		
		assertEquals(area.hashCode(), area2.hashCode());
		assertFalse(area.equals(null));
		assertFalse(area.equals(area3));
		assertFalse(area3.equals(area));
		assertNotEquals(area.hashCode(), area3.hashCode());
	}
}
