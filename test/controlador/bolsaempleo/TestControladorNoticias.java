package controlador.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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

/** test controlador convocatoria crud.
 * @author jmoral
 *
 */
public class TestControladorNoticias {
	private static final String MENSAJE_NOTICIAS_DEVUELTAS = "Debe devolver noticias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	
	
    /** prepara la bd con los datos iniciales.
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
}
