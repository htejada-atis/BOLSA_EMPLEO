package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;

/** test usuarios bolsa empleo.
*
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBeanUsuarios {

	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final String DOCUMENTO = "123456789A";
	private static final String EMAIL = "test@test";
	private static final String RAZONEXCLUIDO = "test";
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final String EXCLUIDOTIPO = "EJEMPLO";
	private static final Date FECHAEXCLUSIONINICIO = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Date FECHAEXCLUSIONFIN = new java.sql.Date(Calendar.getInstance().getTime().getTime());
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
		usuario.setCodCuenta(CODCUENTA);
		usuario.setEmail(EMAIL);
		usuario.setRol(ROL);
		usuario.setListaDist(LISTADIST);
		usuario.setExcluido(EXCLUIDO);
		usuario.setExcluidoTipo(EXCLUIDOTIPO);
		usuario.setRazonExcluido(RAZONEXCLUIDO);
		usuario.setFechaExclusion(FECHAEXCLUSION);
		usuario.setBorrado(BORRADO);
		usuario.setFechaBorrado(FECHABORRADO);
		
		assertEquals(id, usuario.getCodNum());
		assertEquals(CODCUENTA, usuario.getCodCuenta());
		assertEquals(ROL, usuario.getRol());
		assertEquals(EMAIL, usuario.getEmail());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(EXCLUIDOTIPO, usuario.getExcluidoTipo());
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
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		
		usuario.setPrsNif(DOCUMENTO);
		usuario.setCodCuenta(CODCUENTA);
		usuario.setRol(ROL);
		usuario.setListaDist(LISTADIST);
		usuario.setExcluido(EXCLUIDO);
		usuario.setExcluidoTipo(EXCLUIDOTIPO);
		usuario.setFechaExclusionInicio(FECHAEXCLUSIONINICIO);
		usuario.setFechaExclusionFin(FECHAEXCLUSIONFIN);
		
		assertEquals(CODCUENTA, usuario.getCodCuenta());
		assertEquals(ROL, usuario.getRol());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(EXCLUIDOTIPO, usuario.getExcluidoTipo());
		assertNotNull(usuario.toString());
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		Integer id = 1;
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodNum(id);
		usuario.setExcluido(EXCLUIDO);
		usuario.setExcluidoTipo(EXCLUIDOTIPO);
		usuario.setRazonExcluido(RAZONEXCLUIDO);
		usuario.setFechaExclusion(FECHAEXCLUSION);
		usuario.setFechaExclusionInicio(FECHAEXCLUSIONINICIO);
		usuario.setFechaExclusionFin(FECHAEXCLUSIONFIN);
		
		assertEquals(id, usuario.getCodNum());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(EXCLUIDOTIPO, usuario.getExcluidoTipo());
		assertEquals(RAZONEXCLUIDO, usuario.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, usuario.getFechaExclusion());
	}
	
	/** Test constructor.
	 */
	@Test
	public void testA04() {
		Integer id = 1;
		
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo(ROL, CODCUENTA);
		usuario.setCodNum(id);
		usuario.setListaDist(LISTADIST);
		usuario.setExcluido(EXCLUIDO);
		usuario.setExcluidoTipo(EXCLUIDOTIPO);
		usuario.setRazonExcluido(RAZONEXCLUIDO);
		usuario.setFechaExclusion(FECHAEXCLUSION);
		usuario.setFechaExclusionInicio(FECHAEXCLUSIONINICIO);
		usuario.setFechaExclusionFin(FECHAEXCLUSIONFIN);
		
		assertEquals(id, usuario.getCodNum());
		assertEquals(ROL, usuario.getRol());
		assertEquals(CODCUENTA, usuario.getCodCuenta());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(EXCLUIDOTIPO, usuario.getExcluidoTipo());
		assertEquals(RAZONEXCLUIDO, usuario.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, usuario.getFechaExclusion());
		
	}
	
	
	/** Test constructor.
	 */
	@Test
	public void testA05() {		
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		
		usuario.setCodCuenta(CODCUENTA);
		usuario.setTipoDocumento(DOCUMENTO);
		usuario.setRol(ROL);
		usuario.setListaDist(LISTADIST);
		usuario.setExcluido(EXCLUIDO);
		usuario.setExcluidoTipo(EXCLUIDOTIPO);
		usuario.setRazonExcluido(RAZONEXCLUIDO);
		usuario.setFechaExclusion(FECHAEXCLUSION);
		usuario.setFechaExclusionInicio(FECHAEXCLUSIONINICIO);
		usuario.setFechaExclusionFin(FECHAEXCLUSIONFIN);
		
		assertEquals(CODCUENTA, usuario.getCodCuenta());
		assertEquals(ROL, usuario.getRol());
		assertEquals(LISTADIST, usuario.getListaDist());
		assertEquals(EXCLUIDO, usuario.getExcluido());
		assertEquals(EXCLUIDOTIPO, usuario.getExcluidoTipo());
		assertEquals(RAZONEXCLUIDO, usuario.getRazonExcluido());
		assertEquals(FECHAEXCLUSION, usuario.getFechaExclusion());		
	}
}
