package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;

/** test departamentos.
 *
 */
public class TestBEPBeanDepartamento {

	private static final String COD_DEPARTAMENTO = "dep01";
	private static final String DES_DEPARTAMENTO = "Departamento física y química";
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Departamento departamento = new Departamento();
		
		departamento.setCodNum(id);
		departamento.setIdDepartamentoExterno(COD_DEPARTAMENTO);
		departamento.setDescripcion(DES_DEPARTAMENTO);
		
		assertEquals(id, departamento.getCodNum());
		assertEquals(COD_DEPARTAMENTO, departamento.getIdDepartamentoExterno());
		assertEquals(DES_DEPARTAMENTO, departamento.getDescripcion());
		assertNotNull(departamento.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		Integer id = 1;
		Departamento departamento = new Departamento(id, COD_DEPARTAMENTO, DES_DEPARTAMENTO);
		Departamento departamento2 = new Departamento(departamento);
		
		assertEquals(id, departamento.getCodNum());
		assertEquals(id, departamento2.getCodNum());
		assertEquals(COD_DEPARTAMENTO, departamento.getIdDepartamentoExterno());
		assertEquals(COD_DEPARTAMENTO, departamento2.getIdDepartamentoExterno());
		assertEquals(DES_DEPARTAMENTO, departamento.getDescripcion());
		assertEquals(DES_DEPARTAMENTO, departamento2.getDescripcion());		
		assertNotNull(departamento.toString());
		assertNotNull(departamento2.toString());
		assertTrue(departamento.equals(departamento2));
		assertTrue(departamento.hashCode() == departamento2.hashCode());		
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Integer id = 1;
		
		Departamento departamento = new Departamento();
		Departamento departamento2 = new Departamento();
		Departamento departamento3 = new Departamento(id, COD_DEPARTAMENTO, DES_DEPARTAMENTO);
		    	
		assertTrue(departamento.equals(departamento2));
		assertTrue(departamento2.equals(departamento));		
		assertEquals(departamento.hashCode(), departamento2.hashCode());
		assertFalse(departamento.equals(null));
		assertFalse(departamento.equals(departamento3));
		assertFalse(departamento3.equals(departamento));
		assertNotEquals(departamento.hashCode(), departamento3.hashCode());
	}
}
