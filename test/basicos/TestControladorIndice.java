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

/** test para los controladores indice.
 *
 */
public class TestControladorIndice {
	
	private static final String MENSAJE_CON_JSP = "jsp obligatorio";
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	BbddRunner.conectarBd();
    }
    
    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA01() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.infadministrativa.ControladorIndice controlador 
    		= new es.ujaen.uvirtual.controlador.infadministrativa.ControladorIndice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }
    
    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA02() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.infadministrativa.docentia.ControladorIndice controlador 
    		= new es.ujaen.uvirtual.controlador.infadministrativa.docentia.ControladorIndice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }
    
    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA03() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.administracion.ControladorIndice controlador 
    		= new es.ujaen.uvirtual.controlador.administracion.ControladorIndice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }

    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA04() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.extuniversitaria.ControladorIndice controlador 
    		= new es.ujaen.uvirtual.controlador.extuniversitaria.ControladorIndice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }

    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA05() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.infacademica.ControladorIndice controlador 
    		= new es.ujaen.uvirtual.controlador.infacademica.ControladorIndice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }
    
    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA06() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.infgeneral.ControladorIndice controlador 
    		= new es.ujaen.uvirtual.controlador.infgeneral.ControladorIndice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }

    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA07() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.operaciones.ControladorIndice controlador 
    		= new es.ujaen.uvirtual.controlador.operaciones.ControladorIndice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }
    
    /** do post.
     * @throws IOException si error io
     * @throws ServletException  si error servlet
     * 
     */
    @Test
    public void testA08() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	RespuestaHttp respuesta = new RespuestaHttp();
    	es.ujaen.uvirtual.controlador.xdefecto.Indice controlador 
    		= new es.ujaen.uvirtual.controlador.xdefecto.Indice();
    	controlador.doPost(peticion, respuesta);
		assertEquals(MENSAJE_CON_JSP, 1, peticion.getUVDatos().getFicherosJSP().size());
    }
    
}
