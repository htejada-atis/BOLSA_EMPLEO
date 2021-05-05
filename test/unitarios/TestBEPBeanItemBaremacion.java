package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;

/** test item baremación.
 *
 */
public class TestBEPBeanItemBaremacion {

	private static final Integer APARTADO_CODNUM = 4;
	private static final String APARTADO_CODIGO = "AAA";
	private static final String APARTADO_NOMBRE = "EEEE";
	private static final Boolean APARTADO_ACTIVO = true;
	private static final Float APARTADO_PUNTUACIONMAXIMA = (float) 1;
	private static final Float APARTADO_PORCENTAJEMAXIMO = (float) 1;
	
	private static final String BLOQUE_CODIGO = "A";
	private static final String BLOQUE_NOMBRE = "EE";
	private static final Boolean BLOQUE_ACTIVO = true;
	private static final ApartadoBaremacion BLOQUE_APARTADO =
			new ApartadoBaremacion(APARTADO_CODNUM, APARTADO_CODIGO, APARTADO_NOMBRE, APARTADO_ACTIVO, APARTADO_PUNTUACIONMAXIMA, APARTADO_PORCENTAJEMAXIMO);
	private static final Integer BLOQUE_NUM_MAXIMO_MERITOS = 1;
	
	private static final Integer ITEM_CODNUM = 1;
	private static final String ITEM_CODIGO = "AAA";
	private static final String ITEM_NOMBRE = "EEEE";
	private static final String ITEM_DESCRIPCION = "EEEE";
	private static final Boolean ITEM_ACTIVO = true;
	private static final BloqueBaremacion ITEM_BLOQUE =
			new BloqueBaremacion(5, BLOQUE_APARTADO, BLOQUE_CODIGO, BLOQUE_NOMBRE, BLOQUE_ACTIVO, BLOQUE_NUM_MAXIMO_MERITOS);
	private static final String ITEM_UNIDADES = "ENTERO";
	private static final Float ITEM_VALOR = (float) 10;
	private static final Float ITEM_VALORMINIMO = (float) 1;
	private static final Float ITEM_VALORMAXIMO = (float) 100;
	private static final String ITEM_AFINIDAD = "EEEE";
	private static final Boolean ITEM_INDIVIDUALIZADO = true;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		ItemBaremacion item = new ItemBaremacion();
    	item.setCodNum(ITEM_CODNUM);
    	item.setCodigo(ITEM_CODIGO);
    	item.setNombre(ITEM_NOMBRE);
    	item.setDescripcion(ITEM_DESCRIPCION);
    	item.setActivo(ITEM_ACTIVO);
    	item.setBloqueBaremacion(ITEM_BLOQUE);
    	item.setUnidades(ITEM_UNIDADES);
    	item.setValor(ITEM_VALOR);
    	item.setValorMinimo(ITEM_VALORMINIMO);
    	item.setValorMaximo(ITEM_VALORMAXIMO);
    	item.setAfinidad(ITEM_AFINIDAD);
		assertEquals(ITEM_CODNUM, item.getCodNum());
		assertEquals(ITEM_CODIGO, item.getCodigo());
		assertEquals(ITEM_NOMBRE, item.getNombre());
		assertEquals(ITEM_DESCRIPCION, item.getDescripcion());
		assertEquals(ITEM_ACTIVO, item.getActivo());
		assertEquals(ITEM_BLOQUE, item.getBloqueBaremacion());
		assertEquals(ITEM_UNIDADES, item.getUnidades());
		assertEquals(ITEM_VALOR, item.getValor());
		assertEquals(ITEM_VALORMINIMO, item.getValorMinimo());
		assertEquals(ITEM_VALORMAXIMO, item.getValorMaximo());
		assertEquals(ITEM_AFINIDAD, item.getAfinidad());
		assertNotNull(item.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		ItemBaremacion item = new ItemBaremacion(ITEM_CODNUM, ITEM_BLOQUE, 
				ITEM_CODIGO, ITEM_NOMBRE, ITEM_ACTIVO, ITEM_UNIDADES, ITEM_VALOR, ITEM_VALORMINIMO, ITEM_VALORMAXIMO, ITEM_AFINIDAD,
				ITEM_INDIVIDUALIZADO);
		assertEquals(ITEM_CODNUM, item.getCodNum());
		assertEquals(ITEM_CODIGO, item.getCodigo());
		assertEquals(ITEM_NOMBRE, item.getNombre());
		assertEquals(ITEM_ACTIVO, item.getActivo());
		assertEquals(ITEM_BLOQUE, item.getBloqueBaremacion());
		assertEquals(ITEM_UNIDADES, item.getUnidades());
		assertEquals(ITEM_VALOR, item.getValor());
		assertEquals(ITEM_VALORMINIMO, item.getValorMinimo());
		assertEquals(ITEM_VALORMAXIMO, item.getValorMaximo());
		assertEquals(ITEM_AFINIDAD, item.getAfinidad());
		assertEquals(ITEM_INDIVIDUALIZADO, item.getIndividualizado());
		assertNotNull(item.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		ItemBaremacion item2 = new ItemBaremacion(ITEM_CODNUM, ITEM_BLOQUE, 
				ITEM_CODIGO, ITEM_NOMBRE, ITEM_ACTIVO, ITEM_UNIDADES, ITEM_VALOR, ITEM_VALORMINIMO, ITEM_VALORMAXIMO, ITEM_AFINIDAD, ITEM_INDIVIDUALIZADO);
		ItemBaremacion item = new ItemBaremacion(item2);

		assertEquals(ITEM_CODNUM, item.getCodNum());
		assertEquals(ITEM_CODIGO, item.getCodigo());
		assertEquals(ITEM_NOMBRE, item.getNombre());
		assertEquals(ITEM_ACTIVO, item.getActivo());
		assertEquals(ITEM_BLOQUE, item.getBloqueBaremacion());
		assertEquals(ITEM_UNIDADES, item.getUnidades());
		assertEquals(ITEM_VALOR, item.getValor());
		assertEquals(ITEM_VALORMINIMO, item.getValorMinimo());
		assertEquals(ITEM_VALORMAXIMO, item.getValorMaximo());
		assertEquals(ITEM_AFINIDAD, item.getAfinidad());
		assertEquals(ITEM_INDIVIDUALIZADO, item.getIndividualizado());		
		assertNotNull(item.toString());
		assertTrue(item.equals(item2));
		assertTrue(item.hashCode() == item2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {

		ItemBaremacion item3 = new ItemBaremacion(ITEM_CODNUM, ITEM_BLOQUE, 
				ITEM_CODIGO, ITEM_NOMBRE, ITEM_ACTIVO, ITEM_UNIDADES, ITEM_VALOR, ITEM_VALORMINIMO, ITEM_VALORMAXIMO, ITEM_AFINIDAD, ITEM_INDIVIDUALIZADO);
		ItemBaremacion item2 = new ItemBaremacion();
		ItemBaremacion item = new ItemBaremacion();
		assertTrue(item.equals(item2));
		assertTrue(item2.equals(item));
		assertEquals(item.hashCode(), item2.hashCode());
		assertFalse(item.equals(null));
		assertFalse(item.equals(item3));
		assertFalse(item3.equals(item));
		assertNotEquals(item.hashCode(), item3.hashCode());
	}
}
