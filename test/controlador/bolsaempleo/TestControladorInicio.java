package controlador.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticias;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorInicio;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador convocatoria crud.
 * @author jmoral
 *
 */
public class TestControladorInicio {
	private static final String MENSAJE_NOTICIAS_DEVUELTAS = "Debe devolver noticias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String FECHA_CORRECTA = "01/01/2030";
	
	
    /** Prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    
    private VistaNoticias obtenerNoticias() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_LISTAR_NOTICIAS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doGet(peticion, respuesta);
		return (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
    }
    
	/** obtener noticias.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		VistaNoticias bean = obtenerNoticias();

		assertNotEquals(MENSAJE_NOTICIAS_DEVUELTAS, 0, bean.getNoticias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertNotEquals("texto no vacio", "", bean.getNoticias().get(0).getTexto());
	}
	
	/** post.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA01Post() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setAttribute(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_AYUDA);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaNoticias bean = (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
		
		assertNotEquals(MENSAJE_NOTICIAS_DEVUELTAS, 0, bean.getNoticias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** post.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA02Post() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setAttribute(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_FAQ);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaNoticias bean = (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** post.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA03Post() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setAttribute(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_AYUDA);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaNoticias bean = (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

}
