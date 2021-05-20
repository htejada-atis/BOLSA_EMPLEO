package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.Memcache;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar memcache. */
public class TestMemcache {

	String clave = "Clave";
	String valor = "Valor de para la clave";
	
	private static final String MENSAJE_IGUAL = "el valor debe ser igual al devuelto";

    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    	System.setProperty("memcacheUrl", BbddRunner.getServidorMemcache());
    }

	/** test acierto poner datos en memcache. */
	@Test
	@Ignore
	public void testA1Set() {
		Memcache.enable();
		Memcache mc = Memcache.getInstance();
		mc.set(clave, valor);
		String devuelto = (String) mc.get(clave);
		Memcache.disable();
		assertEquals(MENSAJE_IGUAL, valor, devuelto);
	}
	
	/** test acierto borrar datos en memcache. */
	@Test
	public void testA2Delete() {
		Memcache.enable();
		Memcache mc = Memcache.getInstance();
		mc.set(clave, valor);
		mc.delete(clave);
		String devuelto = (String) mc.get(clave);
		Memcache.disable();
		assertEquals(MENSAJE_IGUAL, null, devuelto);
	}
	
	/** test acierto invalidar todas las claves en memcache. */
	@Test
	public void testA3InvalidarClaves() {
		Memcache.enable();
		Memcache mc = Memcache.getInstance();
		mc.set(clave, valor);
		mc.invalidateAllKeys();
		String devuelto = (String) mc.get(clave);
		Memcache.disable();
		assertEquals(MENSAJE_IGUAL, null, devuelto);
	}

	/** test acierto disable y enable memcache. */
	@Test
	@Ignore
	public void testA4Disable() {
		Memcache.enable();
		Memcache mc = Memcache.getInstance();
		mc.set(clave, valor);
		Memcache.disable();
		String devueltoDisabled = (String) mc.get(clave);
		assertEquals(MENSAJE_IGUAL, null, devueltoDisabled);
		Memcache.enable();
		mc.set(clave, valor);
		String devuelto = (String) mc.get(clave);
		Memcache.disable();
		assertEquals(MENSAJE_IGUAL, valor, devuelto);
	}

	/** get stats. */
	@Test
	public void testA5GetStats() {
		Memcache.enable();
		Map<SocketAddress, Map<String, String>> salidaEnabled = Memcache.getStats();
		assertNotNull(salidaEnabled);
		Memcache.disable();
		Map<SocketAddress, Map<String, String>> salidaDisabled = Memcache.getStats();
		assertNull(salidaDisabled);
	}

	/** test flush all.
	 */
	@Test
	public void testA6FlushAll() {
		Memcache.enable();
		Memcache mc = Memcache.getInstance();
		mc.set(clave, valor);
		try {
			mc.flushAll();
		} catch (UVException e) {
			fail();
		}
		String devueltoDisabled = (String) mc.get(clave);
		assertEquals(MENSAJE_IGUAL, null, devueltoDisabled);
		Memcache.disable();
	}	
}
