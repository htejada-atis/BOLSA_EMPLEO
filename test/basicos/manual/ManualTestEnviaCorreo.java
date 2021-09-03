package basicos.manual;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.util.ByteArrayDataSource;
import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.EnviaCorreo;

/** prueba manual para envio correo.
 * @author jmoral
 *
 */
public class ManualTestEnviaCorreo {
	static String correo = "jmoral@ujaen.es";
	static List<String> destinatarios = new ArrayList<>();
	String asunto = "asunto";
	String cuerpo = "cuerpo";
	String nombreArchivo = "nombreArchivo";
	static javax.activation.DataSource dataSource;

    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
		
		destinatarios.add(correo);
		dataSource = new ByteArrayDataSource("prueba", "application/pdf");

    }

	/** envia correo adjunto.
	 * @throws IOException si fallo en io
	 */
	@Test
	public void testA1() throws IOException {
		boolean resultado = EnviaCorreo.enviaCorreoAdjunto(correo, destinatarios, asunto, cuerpo, nombreArchivo, dataSource);
		assertTrue("enviado correctamente", resultado);
	}

	/** envia correo html adjunto.
	 * @throws IOException si fallo en io
	 */
	@Test
	public void testA2() throws IOException {
		boolean resultado = EnviaCorreo.enviaCorreoAdjuntoHtml(correo, destinatarios, null, asunto, cuerpo, nombreArchivo, dataSource);
		assertTrue("enviado correctamente", resultado);
	}
	
}
