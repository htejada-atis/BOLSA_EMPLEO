package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;

/** test afinidad.
*
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanAfinidad {
	private static final Integer ID_AFINIDAD = 1000;
	private static final String CODIGO = "AAA";
	private static final String DESCRIPCION = "pruebas de afinidad";
	private static final Double MODULACION = (double) 10000;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Afinidad afinidad = new Afinidad();
		
		afinidad.setCodNum(ID_AFINIDAD);
		afinidad.setCodigo(CODIGO);
		afinidad.setDescripcion(DESCRIPCION);
		afinidad.setModulacion(MODULACION);
				
		assertEquals(ID_AFINIDAD, afinidad.getCodNum());
		assertEquals(CODIGO, afinidad.getCodigo());
		assertEquals(DESCRIPCION, afinidad.getDescripcion());
		assertEquals(MODULACION, afinidad.getModulacion());
		
		assertNotNull(afinidad.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Afinidad afinidad = new Afinidad(ID_AFINIDAD, CODIGO, DESCRIPCION, MODULACION);
		Afinidad afinidad2 = new Afinidad(afinidad);
		
		assertEquals(ID_AFINIDAD, afinidad.getCodNum());
		assertEquals(ID_AFINIDAD, afinidad2.getCodNum());
		assertEquals(CODIGO, afinidad.getCodigo());
		assertEquals(CODIGO, afinidad2.getCodigo());
		assertEquals(DESCRIPCION, afinidad.getDescripcion());
		assertEquals(DESCRIPCION, afinidad2.getDescripcion());		
		assertEquals(MODULACION, afinidad.getModulacion());
		assertEquals(MODULACION, afinidad2.getModulacion());
		
		assertNotNull(afinidad.toString());
		assertNotNull(afinidad2.toString());
		assertTrue(afinidad.getCodigo().equals(afinidad2.getCodigo()));		
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Afinidad afinidad = new Afinidad();
		Afinidad afinidad2 = new Afinidad();
		Afinidad afinidad3 = new Afinidad(ID_AFINIDAD, CODIGO, DESCRIPCION, MODULACION);
		    	
		assertTrue(afinidad.getCodigo().equals(afinidad2.getCodigo()));
		assertTrue(afinidad2.getCodigo().equals(afinidad.getCodigo()));		
		assertFalse(afinidad.equals(null));
		assertFalse(afinidad.equals(afinidad3));
		assertFalse(afinidad3.equals(afinidad));
	}
}
