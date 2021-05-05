package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;

/** test usuarios bolsa empleo.
*
*/
public class TestBEPBeanUsuarios {

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
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		Integer id = 1;
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodNum(id);
		usuario.setCodPersona(CODPERSONA);
		usuario.setCodCuenta(CODCUENTA);
		usuario.setRol(ROL);
		usuario.setEmail(EMAIL);
		usuario.setListaDist(LISTADIST);
		usuario.setExcluido(EXCLUIDO);
		usuario.setRazonExcluido(RAZONEXCLUIDO);
		usuario.setFechaExclusion(FECHAEXCLUSION);
		usuario.setBorrado(BORRADO);
		usuario.setFechaBorrado(FECHABORRADO);
		
		assertEquals(id, usuario.getCodNum());
		assertEquals(CODPERSONA, usuario.getCodPersona());
		assertEquals(CODCUENTA, usuario.getCodCuenta());
		assertEquals(ROL, usuario.getRol());
		assertEquals(EMAIL, usuario.getEmail());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(RAZONEXCLUIDO, usuario.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, usuario.getFechaExclusion());
		assertEquals(BORRADO, usuario.getBorrado());
		assertEquals(FECHABORRADO, usuario.getFechaBorrado());
		assertNotNull(usuario.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(CODPERSONA, CODCUENTA, ROL, LISTADIST, EXCLUIDO);
		usuario.setCodPersona(CODPERSONA);
		usuario.setCodCuenta(CODCUENTA);
		usuario.setRol(ROL);
		usuario.setListaDist(LISTADIST);
		usuario.setExcluido(EXCLUIDO);
		
		assertEquals(CODPERSONA, usuario.getCodPersona());
		assertEquals(CODCUENTA, usuario.getCodCuenta());
		assertEquals(ROL, usuario.getRol());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertNotNull(usuario.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		Integer id = 1;
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(id, EXCLUIDO, RAZONEXCLUIDO, FECHAEXCLUSION);
		usuario.setCodNum(id);
		usuario.setExcluido(EXCLUIDO);
		usuario.setRazonExcluido(RAZONEXCLUIDO);
		usuario.setFechaExclusion(FECHAEXCLUSION);
		assertEquals(id, usuario.getCodNum());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(RAZONEXCLUIDO, usuario.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, usuario.getFechaExclusion());
	}
	
	/** Test constructor.
	 */
	@Test
	public void testA04() {
		Integer id = 1;
		
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(id, ROL, LISTADIST, EXCLUIDO, RAZONEXCLUIDO, FECHAEXCLUSION);
		assertEquals(id, usuario.getCodNum());
		assertEquals(ROL, usuario.getRol());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(RAZONEXCLUIDO, usuario.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, usuario.getFechaExclusion());
		
	}
	
	
	/** Test constructor.
	 */
	@Test
	public void testA05() {		
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(CODPERSONA, CODCUENTA, ROL, LISTADIST, EXCLUIDO, RAZONEXCLUIDO, FECHAEXCLUSION);
		assertEquals(CODPERSONA, usuario.getCodPersona());
		assertEquals(CODCUENTA, usuario.getCodCuenta());
		assertEquals(ROL, usuario.getRol());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(RAZONEXCLUIDO, usuario.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, usuario.getFechaExclusion());		
	}
}
