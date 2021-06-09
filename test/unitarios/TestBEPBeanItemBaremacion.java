package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;

/** test item baremación.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanItemBaremacion {

	private static final Integer APARTADO_CODNUM = 4;
	private static final String APARTADO_CODIGO = "AAA";
	private static final String APARTADO_NOMBRE = "EEEE";
	private static final Boolean APARTADO_ACTIVO = true;
	private static final Double APARTADO_PUNTUACIONMAXIMA = (double) 1;
	private static final Double APARTADO_PORCENTAJEMAXIMO = (double) 1;
	
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
	private static final Double ITEM_VALOR = (double) 10;
	private static final Double ITEM_VALORMINIMO = (double) 1;
	private static final Double ITEM_VALORMAXIMO = (double) 100;
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
		ItemBaremacion item = new ItemBaremacion();
		item.setCodNum(ITEM_CODNUM);
		item.setBloqueBaremacion(ITEM_BLOQUE);
		item.setCodigo(ITEM_CODIGO);
		item.setNombre(ITEM_NOMBRE);
		item.setActivo(ITEM_ACTIVO);
		item.setUnidades(ITEM_UNIDADES);
		item.setValor(ITEM_VALOR);
		item.setValorMinimo(ITEM_VALORMINIMO);
		item.setValorMaximo(ITEM_VALORMAXIMO);
		item.setAfinidad(ITEM_AFINIDAD);
		item.setIndividualizado(ITEM_INDIVIDUALIZADO);
		
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
		ItemBaremacion item2 = new ItemBaremacion();
		item2.setCodNum(ITEM_CODNUM);
		item2.setBloqueBaremacion(ITEM_BLOQUE);
		item2.setCodigo(ITEM_CODIGO);
		item2.setNombre(ITEM_NOMBRE);
		item2.setActivo(ITEM_ACTIVO);
		item2.setUnidades(ITEM_UNIDADES);
		item2.setValor(ITEM_VALOR);
		item2.setValorMinimo(ITEM_VALORMINIMO);
		item2.setValorMaximo(ITEM_VALORMAXIMO);
		item2.setAfinidad(ITEM_AFINIDAD);
		item2.setIndividualizado(ITEM_INDIVIDUALIZADO);
		
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

		ItemBaremacion item3 = new ItemBaremacion();
		item3.setCodNum(ITEM_CODNUM);
		item3.setBloqueBaremacion(ITEM_BLOQUE);
		item3.setCodigo(ITEM_CODIGO);
		item3.setNombre(ITEM_NOMBRE);
		item3.setActivo(ITEM_ACTIVO);
		item3.setUnidades(ITEM_UNIDADES);
		item3.setValor(ITEM_VALOR);
		item3.setValorMinimo(ITEM_VALORMINIMO);
		item3.setValorMaximo(ITEM_VALORMAXIMO);
		item3.setAfinidad(ITEM_AFINIDAD);
		item3.setIndividualizado(ITEM_INDIVIDUALIZADO);
		
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
