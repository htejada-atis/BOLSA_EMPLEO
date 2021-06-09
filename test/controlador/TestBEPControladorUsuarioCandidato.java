package controlador;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;

/**
 * Test controlador usuarios candidatos bolsa empleo.
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorUsuarioCandidato {
	
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	
	/** prepara la bd con los datos iniciales.
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en io
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
    /** Obtener items usuarios, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doGet(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
}
