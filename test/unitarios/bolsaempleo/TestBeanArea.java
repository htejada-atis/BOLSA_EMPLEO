package unitarios.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import org.junit.Test;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Departamento;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;

/** test areas.
 *
 */
public class TestBeanArea {
	
	private static final String ID_AREA_EXTERNO = "area externo";
	private static final String SECCION = "area seccion";
	private static final String DESCRIPCION = "mi area";
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Area area = new Area();
		Departamento dep = new Departamento(1, "DEP1", "Fisica");
		
		area.setCodNum(id);
		area.setDepartamento(dep);
		area.setDescripcion(DESCRIPCION);
		area.setIdAreaExterno(ID_AREA_EXTERNO);
		area.setIdSeccion(SECCION);
				
		assertEquals(id, area.getCodNum());
		assertEquals(DESCRIPCION, area.getDescripcion());
		assertEquals(ID_AREA_EXTERNO, area.getIdAreaExterno());
		assertEquals(SECCION, area.getIdSeccion());
		assertEquals(dep, area.getDepartamento());
		
		assertNotNull(area.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		Departamento dep = new Departamento(1, "DEP1", "Fisica");
		Area area = new Area(id, dep, ID_AREA_EXTERNO, SECCION, DESCRIPCION);
		Area area2 = new Area(area);
		
		assertEquals(id, area.getCodNum());
		assertEquals(id, area2.getCodNum());
		assertEquals(ID_AREA_EXTERNO, area.getIdAreaExterno());
		assertEquals(ID_AREA_EXTERNO, area2.getIdAreaExterno());
		assertEquals(SECCION, area.getIdSeccion());
		assertEquals(SECCION, area2.getIdSeccion());		
		assertEquals(DESCRIPCION, area.getDescripcion());
		assertEquals(DESCRIPCION, area2.getDescripcion());
		assertEquals(dep, area.getDepartamento());
		assertEquals(dep, area2.getDepartamento());
		
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
		Departamento dep = new Departamento(1, "DEP1", "Fisica");
		
		Area area = new Area();
		Area area2 = new Area();
		Area area3 = new Area(id, dep, ID_AREA_EXTERNO, SECCION, DESCRIPCION);
		    	
		assertTrue(area.equals(area2));
		assertTrue(area2.equals(area));		
		assertEquals(area.hashCode(), area2.hashCode());
		assertFalse(area.equals(null));
		assertFalse(area.equals(area3));
		assertFalse(area3.equals(area));
		assertNotEquals(area.hashCode(), area3.hashCode());
	}
}
