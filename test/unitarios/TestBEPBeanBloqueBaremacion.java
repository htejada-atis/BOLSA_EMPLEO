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

/** test bloque.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanBloqueBaremacion {
	
	private static final Integer APARTADO_CODNUM = 4;
	private static final String APARTADO_CODIGO = "AAA";
	private static final String APARTADO_NOMBRE = "EEEE";
	private static final Boolean APARTADO_ACTIVO = true;
	private static final Float APARTADO_PUNTUACIONMAXIMA = (float) 1;
	private static final Float APARTADO_PORCENTAJEMAXIMO = (float) 1;
	
	private static final Integer BLOQUE_CODNUM = 7;
	private static final String BLOQUE_CODIGO = "A";
	private static final String BLOQUE_NOMBRE = "EE";
	private static final Boolean BLOQUE_ACTIVO = true;
	private static final ApartadoBaremacion BLOQUE_APARTADO =
			new ApartadoBaremacion(APARTADO_CODNUM, APARTADO_CODIGO, APARTADO_NOMBRE, APARTADO_ACTIVO, APARTADO_PUNTUACIONMAXIMA, APARTADO_PORCENTAJEMAXIMO);
	private static final Integer BLOQUE_NUM_MAXIMO_MERITOS = 1;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {

		BloqueBaremacion bloque = new BloqueBaremacion();
    	bloque.setCodNum(BLOQUE_CODNUM);
    	bloque.setCodigo(BLOQUE_CODIGO);
    	bloque.setNombre(BLOQUE_NOMBRE);
    	bloque.setActivo(BLOQUE_ACTIVO);
    	bloque.setApartadoBaremacion(BLOQUE_APARTADO);
    	bloque.setNumeroMaximoMeritos(BLOQUE_NUM_MAXIMO_MERITOS);
		assertEquals(BLOQUE_CODNUM, bloque.getCodNum());
		assertEquals(BLOQUE_CODIGO, bloque.getCodigo());
		assertEquals(BLOQUE_NOMBRE, bloque.getNombre());
		assertEquals(BLOQUE_ACTIVO, bloque.isActivo());
		assertEquals(BLOQUE_APARTADO, bloque.getApartadoBaremacion());
		assertNotNull(bloque.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {

		BloqueBaremacion bloque = new BloqueBaremacion(BLOQUE_CODNUM, BLOQUE_APARTADO, BLOQUE_CODIGO, BLOQUE_NOMBRE, BLOQUE_ACTIVO, BLOQUE_NUM_MAXIMO_MERITOS);
		assertEquals(BLOQUE_CODNUM, bloque.getCodNum());
		assertEquals(BLOQUE_CODIGO, bloque.getCodigo());
		assertEquals(BLOQUE_NOMBRE, bloque.getNombre());
		assertEquals(BLOQUE_ACTIVO, bloque.isActivo());
		assertEquals(BLOQUE_APARTADO, bloque.getApartadoBaremacion());
		assertNotNull(bloque.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {

		BloqueBaremacion bloque2 = new BloqueBaremacion(BLOQUE_CODNUM, BLOQUE_APARTADO, BLOQUE_CODIGO, BLOQUE_NOMBRE, BLOQUE_ACTIVO, BLOQUE_NUM_MAXIMO_MERITOS);
		BloqueBaremacion bloque = new BloqueBaremacion(bloque2);
		bloque.setCodNum(BLOQUE_CODNUM);
		bloque.setCodigo(BLOQUE_CODIGO);
		bloque.setNombre(BLOQUE_NOMBRE);
		bloque.setActivo(BLOQUE_ACTIVO);
		assertEquals(BLOQUE_CODNUM, bloque.getCodNum());
		assertEquals(BLOQUE_CODIGO, bloque.getCodigo());
		assertEquals(BLOQUE_NOMBRE, bloque.getNombre());
		assertEquals(BLOQUE_ACTIVO, bloque.isActivo());
		assertNotNull(bloque.toString());
		assertTrue(bloque.equals(bloque2));
		assertTrue(bloque.hashCode() == bloque2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {

		BloqueBaremacion bloque3 = new BloqueBaremacion(BLOQUE_CODNUM, BLOQUE_APARTADO, BLOQUE_CODIGO, BLOQUE_NOMBRE, BLOQUE_ACTIVO, BLOQUE_NUM_MAXIMO_MERITOS);
		BloqueBaremacion bloque2 = new BloqueBaremacion();
		BloqueBaremacion bloque = new BloqueBaremacion();
		assertTrue(bloque.equals(bloque2));
		assertTrue(bloque2.equals(bloque));
		assertEquals(bloque.hashCode(), bloque2.hashCode());
		assertFalse(bloque.equals(null));
		assertFalse(bloque.equals(bloque3));
		assertFalse(bloque3.equals(bloque));
		assertNotEquals(bloque.hashCode(), bloque3.hashCode());
	}
}
