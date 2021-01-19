package basicos;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.AyudaURL;

/** pruebas para Ayuda Url.
 *
 */
public class TestAyudaUrl {

	String urlPrivada = "https://uvirtual.ujaen.es/srv/es/informacionacademica/calificacionesprovisionales/p/123/abc";
	String urlPublica = "https://uvirtual.ujaen.es/pub/es/informaciongeneral";

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

	/** obtener controlador de url conocida.
	 */
	@Test
	public void testA01() {
		String controladorPublico = AyudaURL.obtenerControlador(urlPublica);
		assertEquals("controlador publico", "pub.es.informaciongeneral", controladorPublico);
		String controladorPrivado = AyudaURL.obtenerControlador(urlPrivada);
		assertEquals("controlador privado", "srv.es.informacionacademica.calificacionesprovisionales", controladorPrivado);
	}
	
	/** obtener parametros de url conocida.
	 */
	@Test
	public void testA02() {
		String[] parametros = AyudaURL.obtenerParametros(urlPrivada);
		assertEquals("parametro0", "123", parametros[0]);
		assertEquals("parametro1", "abc", parametros[1]);
	}
	
	/** obtener url sin parematros de una conocida.
	 */
	@Test
	public void testA03() {
		String urlSinParametros = AyudaURL.obtenerUrlSinParametros(urlPrivada, false);
		assertEquals("url sin parametros", "https://uvirtual.ujaen.es/srv/es/informacionacademica/calificacionesprovisionales", urlSinParametros);
	}
}
