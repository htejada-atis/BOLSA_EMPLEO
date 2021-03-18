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
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEstadoBolsas;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticiasCRUD;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorBolsas;


/** test controlador convocatoria crud.
 * @author ATISoluciones 
 */
public class TestControladorBolsas {	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    private VistaEstadoBolsas getVistaBolsasEmpleo() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();    	
		peticion.setParameter(ControladorBolsas.PARAM_ACCION, ControladorBolsas.ACCION_LISTAR_BOLSAS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorBolsas controlador = new ControladorBolsas();		
		controlador.doGet(peticion, respuesta);
		
		return (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    }
    
	/** Obtener bolsas.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		VistaEstadoBolsas bean = getVistaBolsasEmpleo();

		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
}
