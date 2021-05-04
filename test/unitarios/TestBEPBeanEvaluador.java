package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Evaluador;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;

/** test evaluador.
*
*/
public class TestBEPBeanEvaluador {
	private static final Integer CODPERSONA = 1;
	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final String EMAIL = "test@test";
	private static final String RAZONEXCLUIDO = "test";
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final Boolean BORRADO = true;
	private static final Date FECHAEXCLUSION = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Date FECHABORRADO = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Boolean ACTIVO = false;
	private static final Integer AREA = 3;
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		Evaluador evaluador = new Evaluador();
		Area area = new Area();
		
		evaluador.setCodNum(id);
		evaluador.setCodPersona(CODPERSONA);
		evaluador.setCodCuenta(CODCUENTA);
		evaluador.setRol(ROL);
		evaluador.setEmail(EMAIL);
		evaluador.setListaDist(LISTADIST);
		evaluador.setExcluido(EXCLUIDO);
		evaluador.setRazonExcluido(RAZONEXCLUIDO);
		evaluador.setFechaExclusion(FECHAEXCLUSION);
		evaluador.setBorrado(BORRADO);
		evaluador.setFechaBorrado(FECHABORRADO);
		evaluador.setCodNumArea(AREA);
		
		assertEquals(id, evaluador.getCodNum());
		assertEquals(CODPERSONA, evaluador.getCodPersona());
		assertEquals(CODCUENTA, evaluador.getCodCuenta());
		assertEquals(ROL, evaluador.getRol());
		assertEquals(EMAIL, evaluador.getEmail());
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
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(CODPERSONA, CODCUENTA, ROL, LISTADIST, EXCLUIDO);
		Evaluador evaluador = new Evaluador(usuario, AREA, ACTIVO);
		Evaluador evaluador2 = new Evaluador(evaluador, usuario);
		
		assertEquals(CODPERSONA, evaluador.getCodPersona());
		assertEquals(CODPERSONA, evaluador2.getCodPersona());
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
	@SuppressWarnings("java:S2159")
	public void testA03() {
		Integer id = 1;
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(CODPERSONA, CODCUENTA, ROL, LISTADIST, EXCLUIDO);
		
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
