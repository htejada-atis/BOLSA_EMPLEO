package basicos;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestDocentia;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;

/** test controlador error.
 * @author jmoral
 *
 */
public class TestControladorError {
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	BbddRunner.conectarBd();
    }
    
    /** controlador error.
     * @throws ServletException si error servlet
     * @throws IOException si error io
     */
    @Test
    public void testA01() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.ControladorError controlador = new es.ujaen.uvirtual.controlador.ControladorError();
    	controlador.doPost(peticion, respuesta);
		assertEquals(1, peticion.getUVDatos().getFicherosJSP().size());
    	
    }

}
