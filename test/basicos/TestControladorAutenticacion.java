package basicos;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.controlador.ControladorAutenticacion;

/** test controlador autenticacion.
 *
 */
public class TestControladorAutenticacion {
	/** muestra formulario para introducir usuario.
	 * @throws ServletException si error servlet
	 * @throws IOException si error io
	 */
	@Test
	public void testA01() throws ServletException, IOException {
    	PeticionHttp peticion = new PeticionHttp();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ControladorAutenticacion controlador = new ControladorAutenticacion();
    	controlador.doPost(peticion, respuesta);
		assertTrue("respuesta tiene formulario SAML", respuesta.getContenido().contains("action=\"/sso\""));
	}
}
