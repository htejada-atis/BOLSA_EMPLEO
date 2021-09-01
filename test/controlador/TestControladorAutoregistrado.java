package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.UtilsTestAutoregistrado;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.autoregistrado.beans.vista.VistaUsuarioAutoregistrado;
import es.ujaen.uvirtual.modulo.autoregistrado.controlador.ControladorUsuarioAutoregistrado;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador autoregistrado.
 *
 */
public class TestControladorAutoregistrado {
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";

    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     * @throws UVException si error uv
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	UtilsTestAutoregistrado.inicializaDb();
    }
    
	/** muestraLogin.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01MuestraLogin() throws SQLException, ServletException, IOException {
		UVDatos datos = new UVDatos();
		PeticionHttp peticion = new PeticionHttp();
		peticion.setRequestURI("http://uvdesalocal.ujaen.es:8888/pub/es/operaciones/autoregistrado/usuarioautoresgistrado/p/bep");
		peticion.setUVDatos(datos);
		peticion.setParameter(ControladorUsuarioAutoregistrado.PARAM_ACCION, ControladorUsuarioAutoregistrado.ACCION_MUESTRA_LOGIN);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioAutoregistrado controlador = new ControladorUsuarioAutoregistrado();
		controlador.doGet(peticion, respuesta);
		VistaUsuarioAutoregistrado bean = (VistaUsuarioAutoregistrado) peticion.getUVDatos().getVistas().get(VistaUsuarioAutoregistrado.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertNotEquals("modulo no vacio", "", bean.getDescripcionModulo());
		assertNotEquals("redireccion no vacio", "", bean.getPaginaRedireccion());
	}    
}
