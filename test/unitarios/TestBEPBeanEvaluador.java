package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;

/** test evaluador.
*
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanEvaluador {
	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final String DOCUMENTO = "123456789A";
	private static final String RAZONEXCLUIDO = "test";
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final String EXCLUIDOTIPO = "EJEMPLO";
	private static final Date FECHAEXCLUSIONINICIO = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Date FECHAEXCLUSIONFIN = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Boolean BORRADO = true;
	private static final Date FECHAEXCLUSION = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Date FECHABORRADO = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Boolean ACTIVO = false;
	private static final Integer AREA = 3;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Evaluador evaluador = new Evaluador();
		
		evaluador.setCodNum(id);
		evaluador.setCodCuenta(CODCUENTA);
		evaluador.setRol(ROL);
		evaluador.setListaDist(LISTADIST);
		evaluador.setExcluido(EXCLUIDO);
		evaluador.setRazonExcluido(RAZONEXCLUIDO);
		evaluador.setFechaExclusion(FECHAEXCLUSION);
		evaluador.setBorrado(BORRADO);
		evaluador.setFechaBorrado(FECHABORRADO);
		evaluador.setCodNumArea(AREA);
		
		assertEquals(id, evaluador.getCodNum());
		assertEquals(CODCUENTA, evaluador.getCodCuenta());
		assertEquals(ROL, evaluador.getRol());
		assertEquals(LISTADIST, evaluador.getListaDist());
		assertEquals(EXCLUIDO, evaluador.getExcluido());
		assertEquals(RAZONEXCLUIDO, evaluador.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, evaluador.getFechaExclusion());
		assertEquals(BORRADO, evaluador.getBorrado());
		assertEquals(FECHABORRADO, evaluador.getFechaBorrado());
		assertNotNull(evaluador.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA02() {
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(DOCUMENTO, CODCUENTA, ROL, LISTADIST, EXCLUIDO, EXCLUIDOTIPO, FECHAEXCLUSIONINICIO, FECHAEXCLUSIONFIN);
		Evaluador evaluador = new Evaluador(usuario, AREA, ACTIVO);
		Evaluador evaluador2 = new Evaluador(evaluador);
		
		assertEquals(CODCUENTA, evaluador.getCodCuenta());
		assertEquals(CODCUENTA, evaluador2.getCodCuenta());		
		assertEquals(ROL, evaluador.getRol());
		assertEquals(ROL, evaluador2.getRol());		
		assertEquals(LISTADIST, evaluador.getListaDist());
		assertEquals(LISTADIST, evaluador2.getListaDist());		
		assertEquals(EXCLUIDO, evaluador.getExcluido());
		assertEquals(EXCLUIDO, evaluador2.getExcluido());		
		assertNotNull(evaluador.toString());
		assertNotNull(evaluador2.toString());
		assertTrue(evaluador.equals(evaluador2));
		assertTrue(evaluador.hashCode() == evaluador2.hashCode());		
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	public void testA03() {
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(DOCUMENTO, CODCUENTA, ROL, LISTADIST, EXCLUIDO, EXCLUIDOTIPO, FECHAEXCLUSIONINICIO, FECHAEXCLUSIONFIN);
		
		Evaluador evaluador = new Evaluador();
		Evaluador evaluador2 = new Evaluador();
		Evaluador evaluador3 = new Evaluador(usuario, AREA, ACTIVO);
		    	
		assertTrue(evaluador.equals(evaluador2));
		assertTrue(evaluador2.equals(evaluador));
		assertEquals(evaluador.hashCode(), evaluador2.hashCode());
		assertFalse(evaluador.equals(null));
		assertFalse(evaluador.equals(evaluador3));
		assertFalse(evaluador3.equals(evaluador));
		assertNotEquals(evaluador.hashCode(), evaluador3.hashCode());
	}
}
