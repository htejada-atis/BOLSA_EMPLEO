package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines;

/** test controlador validar no afines .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorValidarNoAfines {
	
	private static final String MENSAJE_BOLSAS_DEVUELTAS = "Debe devolver bolsas";
	private static final String MENSAJE_BOLSA_DEVUELTA = "Debe devolver bolsa";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_MERITOS_DEVUELTOS = "Debe devolver méritos";
	private static final String MENSAJE_MERITO_DEVUELTO = "Debe devolver mérito";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String MENSAJE_SOLICITUDES_DEVUELTAS = "Debe devolver solicitudes";
	private static final String MENSAJE_SOLICITUD_DEVUELTA = "Debe devolver solicitud";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en io .
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de áreas .
    private VistaValidarNoAfines obtenerAreas() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);
		return (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());
    }
    
    // método para obtener la vista con un área .
    private VistaValidarNoAfines obtenerArea() throws ServletException, IOException {
    	VistaValidarNoAfines bean = obtenerAreas();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_BOLSA_SELECCIONADA);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getDatatableBolsas().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);
		return (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());
    }
    
    /** Obtener áreas, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doGet(peticion, respuesta);
		
		VistaValidarNoAfines bean = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener áreas .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02ObtenerAreas() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerAreas();
		
		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, 0, bean.getDatatableBolsas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener áreas .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA03ObtenerArea() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerArea();
		
		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, 0, bean.getDatatableBolsas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
}
