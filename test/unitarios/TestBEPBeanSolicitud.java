package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;

/** test solicitud.
*
*/
public class TestBEPBeanSolicitud {
	private static final Integer CODNUM = 100;
	private static final UsuarioBolsaEmpleo USUARIO = new UsuarioBolsaEmpleo();
	private static final Convocatoria CONVOCATORIA = new Convocatoria();
	private static final String ESTADO = "estado";
	private static final Date FECHACONFIRMACION = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final InputStream ARCHIVO = new ByteArrayInputStream("archivo de prueba".getBytes());
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA01() {
		
		Solicitud solicitud = new Solicitud();		
		
		solicitud.setCodNum(CODNUM);
		solicitud.setUsuario(USUARIO);
		solicitud.setConvocatoria(CONVOCATORIA);
		solicitud.setEstado(ESTADO);
		solicitud.setFechaConfirmacion(FECHACONFIRMACION);
		solicitud.setArchivo(ARCHIVO);
		
		assertEquals(CODNUM, solicitud.getCodNum(), solicitud.getCodNum());
		assertEquals(USUARIO, solicitud.getUsuario());
		assertEquals(CONVOCATORIA, solicitud.getConvocatoria());
		assertEquals(ESTADO, solicitud.getEstado());
		assertEquals(FECHACONFIRMACION, solicitud.getFechaConfirmacion());
		assertEquals(ARCHIVO, solicitud.getArchivo());
		assertNotNull(solicitud.toString());
	}
	
	/** test constructor.
	 * 
	 */
	@Test
	public void testA02() {

		Solicitud solicitud = new Solicitud(CODNUM, USUARIO, CONVOCATORIA, ESTADO);
		
		assertEquals(CODNUM, solicitud.getCodNum());
		assertEquals(USUARIO, solicitud.getUsuario());
		assertEquals(CONVOCATORIA, solicitud.getConvocatoria());
		assertEquals(ESTADO, solicitud.getEstado());
		assertNotNull(solicitud.toString());
		
	}
	
	/** test equals.
	 */
	@Test
	public void testA03() {
		
		Solicitud solicitud2 = new Solicitud(CODNUM, USUARIO, CONVOCATORIA, ESTADO);
		Solicitud solicitud = new Solicitud(solicitud2);
		
		solicitud.setCodNum(CODNUM);
		solicitud.setUsuario(USUARIO);
		solicitud.setConvocatoria(CONVOCATORIA);
		solicitud.setEstado(ESTADO);
		solicitud.setFechaConfirmacion(FECHACONFIRMACION);
		solicitud.setArchivo(ARCHIVO);
		assertNotNull(solicitud.toString());
		
	}
	
	/** test equals.
	 * se quita warning S2159 de equals null, es lo que se quiere probar
	 */
	@Test
	@SuppressWarnings("java:S2159")
	public void testA04() {

    	Solicitud solicitud3 = new Solicitud(CODNUM, USUARIO, CONVOCATORIA, ESTADO);
    	Solicitud solicitud2 = new Solicitud();
		Solicitud solicitud = new Solicitud();
		assertTrue(solicitud.equals(solicitud2));
		assertTrue(solicitud2.equals(solicitud));
		assertEquals(solicitud.hashCode(), solicitud2.hashCode());
		assertFalse(solicitud.equals(null));
		assertFalse(solicitud.equals(solicitud3));
		assertFalse(solicitud3.equals(solicitud));
		assertNotEquals(solicitud.hashCode(), solicitud3.hashCode());
	}
}
