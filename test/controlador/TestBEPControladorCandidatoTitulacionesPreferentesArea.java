package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaCandidatoTitulacionesArea;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorCandidatoTitulacionesPreferentesArea;


/** test controlador titulacion.
 * @author ATISoluciones 
 */
public class TestBEPControladorCandidatoTitulacionesPreferentesArea {
	
	private static final String MENSAJE_AREA_DEVUELTA = "Debe devolver area";
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver areas";
	private static final String MENSAJE_TITULACIONES_DEVUELTAS = "Debe devolver titulaciones";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en ficheros .
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de áreas .
    private VistaCandidatoTitulacionesArea getVistaConAreas() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
    	peticion.setParameter(ControladorCandidatoTitulacionesPreferentesArea.PARAM_ACCION, ControladorCandidatoTitulacionesPreferentesArea.ACCION_DATATABLE_BOLSAS);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorCandidatoTitulacionesPreferentesArea controlador = new ControladorCandidatoTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		return (VistaCandidatoTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaCandidatoTitulacionesArea.class.getName());
    }
    
    // método para obtener la vista con una área seleccionada .
    private VistaCandidatoTitulacionesArea getVistaConArea() throws ServletException, IOException {
    	VistaCandidatoTitulacionesArea bean = getVistaConAreas();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorCandidatoTitulacionesPreferentesArea.PARAM_ACCION, ControladorCandidatoTitulacionesPreferentesArea.ACCION_BOLSA_SELECCIONADA);
		peticion.setParameter(ControladorCandidatoTitulacionesPreferentesArea.PARAM_BOLSA, bean.getDatatableAreas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorCandidatoTitulacionesPreferentesArea controlador = new ControladorCandidatoTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		return (VistaCandidatoTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaCandidatoTitulacionesArea.class.getName());
    }
    
                
	/** Obtener areas, sin parametro definido .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorCandidatoTitulacionesPreferentesArea controlador = new ControladorCandidatoTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		VistaCandidatoTitulacionesArea bean = (VistaCandidatoTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaCandidatoTitulacionesArea.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener areas .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA02ObtenerAreas() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorCandidatoTitulacionesPreferentesArea.PARAM_ACCION, ControladorCandidatoTitulacionesPreferentesArea.ACCION_LISTAR_BOLSAS);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorCandidatoTitulacionesPreferentesArea controlador = new ControladorCandidatoTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		VistaCandidatoTitulacionesArea bean = (VistaCandidatoTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaCandidatoTitulacionesArea.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable areas .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA03ObtenerDataTableAreas() throws SQLException, ServletException, IOException {
		VistaCandidatoTitulacionesArea bean = getVistaConAreas();
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getDatatableAreas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar bolsa .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA04SeleccionarBolsa() throws SQLException, ServletException, IOException {
		VistaCandidatoTitulacionesArea bean = getVistaConArea();
		
		assertNotNull(MENSAJE_AREA_DEVUELTA, bean.getArea());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable titulaciones .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA05ObtenerDataTableTitulaciones() throws SQLException, ServletException, IOException {
		VistaCandidatoTitulacionesArea bean = getVistaConArea();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorCandidatoTitulacionesPreferentesArea.PARAM_ACCION, ControladorCandidatoTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES);
		peticion.setParameter(ControladorCandidatoTitulacionesPreferentesArea.PARAM_BOLSA, bean.getDatatableAreas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorCandidatoTitulacionesPreferentesArea controlador = new ControladorCandidatoTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		VistaCandidatoTitulacionesArea bean2 = (VistaCandidatoTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaCandidatoTitulacionesArea.class.getName());
		
		assertNotNull(MENSAJE_AREA_DEVUELTA, bean2.getArea());
		assertEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean2.getDatatableTitulaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
}
