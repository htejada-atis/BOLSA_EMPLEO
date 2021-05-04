package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.MeritoPreferente;


/** test merito preferente.
 *
 */
public class TestBEPBeanMeritoPreferente {
	private static final Integer CODNUM = 100;
	private static final String DESCRIPCION = "descripcion";
	private static final String TIPO = "tipo";
	private static final String APLICABLE = "aplicable";
	private static final String FACTOR = "factor";
	private static final Float VALORMAXIMO = (float) 100.0;
	private static final Boolean ACTIVO = true;
	private static final ItemBaremacion ITEM = new ItemBaremacion();
	private static final ItemBaremacion ITEM2 = new ItemBaremacion();
	private static final BloqueBaremacion BLOQUE = new BloqueBaremacion();
	private static final ApartadoBaremacion APARTADO = new ApartadoBaremacion();

	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {

		MeritoPreferente merito = new MeritoPreferente();
		
		merito.setCodNum(CODNUM);
		merito.setDescripcion(DESCRIPCION);
		merito.setTipo(TIPO);
		merito.setAplicable(APLICABLE);
		merito.setFactor(FACTOR);
		merito.setValorMaximo(VALORMAXIMO);
		merito.setTipoItemBaremacion(ITEM);
		merito.setAplicableBloqueBaremacion(BLOQUE);
		merito.setAplicableApartadoBaremacion(APARTADO);
		merito.setAplicableItemBaremacion(ITEM2);
		merito.setActivo(ACTIVO);
		
		assertEquals(CODNUM, merito.getCodNum());
		assertEquals(DESCRIPCION, merito.getDescripcion());
		assertEquals(TIPO, merito.getTipo());
		assertEquals(APLICABLE, merito.getAplicable());
		assertEquals(FACTOR, merito.getFactor());
		assertEquals(VALORMAXIMO, merito.getValorMaximo());
		assertEquals(ITEM, merito.getTipoItemBaremacion());
		assertEquals(BLOQUE, merito.getAplicableBloqueBaremacion());
		assertEquals(APARTADO, merito.getAplicableApartadoBaremacion());
		assertEquals(ITEM2, merito.getAplicableItemBaremacion());
		assertEquals(ACTIVO, merito.getActivo());
		assertNotNull(merito.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {

		MeritoPreferente merito = new MeritoPreferente(CODNUM, DESCRIPCION, TIPO, APLICABLE, FACTOR, VALORMAXIMO, ITEM, BLOQUE, APARTADO, ITEM2, ACTIVO);
		assertEquals(CODNUM, merito.getCodNum());
		assertEquals(DESCRIPCION, merito.getDescripcion());
		assertEquals(TIPO, merito.getTipo());
		assertEquals(APLICABLE, merito.getAplicable());
		assertEquals(FACTOR, merito.getFactor());
		assertEquals(VALORMAXIMO, merito.getValorMaximo());
		assertEquals(ITEM, merito.getTipoItemBaremacion());
		assertEquals(BLOQUE, merito.getAplicableBloqueBaremacion());
		assertEquals(APARTADO, merito.getAplicableApartadoBaremacion());
		assertEquals(ITEM2, merito.getAplicableItemBaremacion());
		assertEquals(ACTIVO, merito.getActivo());
		assertNotNull(merito.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {

		MeritoPreferente merito2 = new MeritoPreferente(CODNUM, DESCRIPCION, TIPO, APLICABLE, FACTOR, VALORMAXIMO, ITEM, BLOQUE, APARTADO, ITEM2, ACTIVO);
		MeritoPreferente merito = new MeritoPreferente(merito2);
		merito.setCodNum(CODNUM);
		merito.setDescripcion(DESCRIPCION);
		merito.setTipo(TIPO);
		merito.setAplicable(APLICABLE);
		merito.setFactor(FACTOR);
		merito.setValorMaximo(VALORMAXIMO);
		merito.setTipoItemBaremacion(ITEM);
		merito.setAplicableBloqueBaremacion(BLOQUE);
		merito.setAplicableApartadoBaremacion(APARTADO);
		merito.setAplicableItemBaremacion(ITEM2);
		merito.setActivo(ACTIVO);
		
		assertEquals(CODNUM, merito.getCodNum());
		assertEquals(DESCRIPCION, merito.getDescripcion());
		assertEquals(TIPO, merito.getTipo());
		assertEquals(APLICABLE, merito.getAplicable());
		assertEquals(FACTOR, merito.getFactor());
		assertEquals(VALORMAXIMO, merito.getValorMaximo());
		assertEquals(ITEM, merito.getTipoItemBaremacion());
		assertEquals(BLOQUE, merito.getAplicableBloqueBaremacion());
		assertEquals(APARTADO, merito.getAplicableApartadoBaremacion());
		assertEquals(ITEM2, merito.getAplicableItemBaremacion());
		assertEquals(ACTIVO, merito.getActivo());
		
		assertNotNull(merito.toString());
		assertTrue(merito.equals(merito2));
		assertTrue(merito.hashCode() == merito2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {
		MeritoPreferente merito3 = new MeritoPreferente(CODNUM, DESCRIPCION, TIPO, APLICABLE, FACTOR, VALORMAXIMO, ITEM, BLOQUE, APARTADO, ITEM2, ACTIVO);
		MeritoPreferente merito2 = new MeritoPreferente();
		MeritoPreferente merito = new MeritoPreferente();
		assertTrue(merito.equals(merito2));
		assertTrue(merito2.equals(merito));
		assertEquals(merito.hashCode(), merito2.hashCode());
		assertFalse(merito.equals(null));
		assertFalse(merito.equals(merito3));
		assertFalse(merito3.equals(merito));
		assertNotEquals(merito.hashCode(), merito3.hashCode());
	}
}
