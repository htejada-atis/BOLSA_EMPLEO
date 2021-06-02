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

/** test apartado.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanApartadoBaremacion {

	private static final Integer CODNUM = 4;
	private static final String CODIGO = "AAA";
	private static final String NOMBRE = "EEEE";
	private static final Boolean ACTIVO = true;
	private static final Float PUNTUACIONMAXIMA = (float) 1;
	private static final Float PORCENTAJEMAXIMO = (float) 1;
	
	/**
	 * test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		apartado.setCodNum(CODNUM);
		apartado.setCodigo(CODIGO);
		apartado.setNombre(NOMBRE);
		apartado.setActivo(ACTIVO);
		apartado.setPorcentajeMaximo(PORCENTAJEMAXIMO);
		assertEquals(CODNUM, apartado.getCodNum());
		assertEquals(CODIGO, apartado.getCodigo());
		assertEquals(NOMBRE, apartado.getNombre());
		assertEquals(ACTIVO, apartado.getActivo());
		assertNotNull(apartado.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		
		ApartadoBaremacion apartado = new ApartadoBaremacion(CODNUM, CODIGO, NOMBRE, ACTIVO, PUNTUACIONMAXIMA, PORCENTAJEMAXIMO);
		assertEquals(CODNUM, apartado.getCodNum());
		assertEquals(CODIGO, apartado.getCodigo());
		assertEquals(NOMBRE, apartado.getNombre());
		assertEquals(ACTIVO, apartado.getActivo());
		assertEquals(PORCENTAJEMAXIMO, apartado.getPorcentajeMaximo());
		assertNotNull(apartado.toString());
	}
	
	/**
	 * test equals.
	 */
	@Test
	public void testA03() {
		ApartadoBaremacion apartado2 = new ApartadoBaremacion(CODNUM, CODIGO, NOMBRE, ACTIVO, PUNTUACIONMAXIMA, PORCENTAJEMAXIMO);
		ApartadoBaremacion apartado = new ApartadoBaremacion(apartado2);
		apartado.setCodNum(CODNUM);
		apartado.setCodigo(CODIGO);
		apartado.setNombre(NOMBRE);
		apartado.setActivo(ACTIVO);
		apartado.setPorcentajeMaximo(PORCENTAJEMAXIMO);
		assertEquals(CODNUM, apartado.getCodNum());
		assertEquals(CODIGO, apartado.getCodigo());
		assertEquals(NOMBRE, apartado.getNombre());
		assertEquals(ACTIVO, apartado.getActivo());
		assertNotNull(apartado.toString());
		assertTrue(apartado.equals(apartado2));
		assertTrue(apartado.hashCode() == apartado2.hashCode());
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {
		ApartadoBaremacion apartado3 = new ApartadoBaremacion(CODNUM, CODIGO, NOMBRE, ACTIVO, PUNTUACIONMAXIMA, PORCENTAJEMAXIMO);
		ApartadoBaremacion apartado2 = new ApartadoBaremacion();
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		assertTrue(apartado.equals(apartado2));
		assertTrue(apartado2.equals(apartado));
		assertEquals(apartado.hashCode(), apartado2.hashCode());
		assertFalse(apartado.equals(null));
		assertFalse(apartado.equals(apartado3));
		assertFalse(apartado3.equals(apartado));
		assertNotEquals(apartado.hashCode(), apartado3.hashCode());
	}
}
