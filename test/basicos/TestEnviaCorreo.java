package basicos;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.EnviaCorreo;

/** test envia correo.
 *
 */
public class TestEnviaCorreo {
	
	private static final String ASUNTO = "mensaje enviado desde junit de uv-externo";
	private static final String CUERPO = "se ha ejecutado el test de envio correo de junit ev-externo";
	private static final String DESTINATARIO = "jmoral@ujaen.es";
	
	private String obtenerIpMaquina() throws SocketException {
		StringBuilder salida = new StringBuilder();
		for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
            NetworkInterface iface = ifaces.nextElement();
            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                InetAddress inetAddr = inetAddrs.nextElement();
                salida.append(inetAddr.getHostAddress() + "\n");
            }
		}
		return salida.toString();
	}
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    }
    
	/** lee parametros.
	 * @throws SocketException si error socket
	 */
	@Test
	public void testA1() throws SocketException {
		List<String> destinatarios = new ArrayList<>();
		destinatarios.add(DESTINATARIO);
		boolean resultado = EnviaCorreo.enviaCorreo(destinatarios, ASUNTO, CUERPO + obtenerIpMaquina());
		assertTrue("enviado correctamente", resultado);
	}

	/** recerga configuracion.
	 */
	@Test
	public void testA2() {
		EnviaCorreo.recargarConfiguracion();
	}

}
